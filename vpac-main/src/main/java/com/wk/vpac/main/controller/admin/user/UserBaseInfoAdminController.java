package com.wk.vpac.main.controller.admin.user;

import com.base.components.cache.CacheLock;
import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.exception.cache.CacheLockOnLockException;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.Logs;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.database.dao.user.BaseUserTypeDao;
import com.wk.vpac.domain.user.UserBaseType;
import com.wk.vpac.main.service.admin.user.UserBaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

/**
 * userBaseInfo Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-01-06
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class UserBaseInfoAdminController {

  @Autowired
  private UserBaseInfoService userBaseInfoService;
  @Autowired
  private BaseUserTypeDao baseUserTypeDao;
  @Autowired
  private CacheLock cacheLock;

  @GetMapping(value = "/user/userBaseInfo/index")
  public String index(ModelMap model) throws IOException {
    Map<String, String> types = Maps.newLinkedHashMap();
    for (UserBaseType value : baseUserTypeDao.findAll(Sort.by("userType"))) {
      if(value.getUserType() > 0 ){
        types.put(value.getId(), value.getUserTypeName());
      }
    }
    model.put("userTypes", JsonUtils.toString(types));
    model.put("userTypeMap", types);
    return "user/userBaseInfo/index";
  }

  @GetMapping(value = "/user/userBaseInfo/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity page(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(userBaseInfoService.page(params), HttpStatus.OK);
  }
  @GetMapping(value = "/user/userBaseInfo/expert/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity expertPage(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(userBaseInfoService.expertPage(params), HttpStatus.OK);
  }

  @PostMapping(value = "/user/userBaseInfo/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity save(@RequestBody Map<String, String> params) {
    return new ResponseEntity<>(JsonResult.success(userBaseInfoService.save(params)), HttpStatus.OK);
  }

  @PostMapping(value = "/user/userBaseInfo/import", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity importUser(@RequestBody Map<String, String> params) {
    try {
      String url = JsonUtils.reader(params.get("dataFile"), ArrayNode.class).get(0).path("attaUrl").textValue();
      params.put("fileUrl", url);
    } catch (Exception e) {
      throw new IllegalArgumentException("文件解析错误", e);
    }
    try {
      ObjectNode node = cacheLock.lockIfAbsentAutoUnlock("importUser", () -> userBaseInfoService.importUser(params));
      return new ResponseEntity<>(JsonResult.success(node), HttpStatus.OK);
    } catch (CacheLockOnLockException e) {
      Logs.get().error("", e);
      throw new IllegalArgumentException("正在导入数据中，请勿稍后再试");
    }
  }
}
