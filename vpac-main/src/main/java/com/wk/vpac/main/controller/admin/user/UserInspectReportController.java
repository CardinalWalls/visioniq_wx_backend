package com.wk.vpac.main.controller.admin.user;

import com.base.components.cache.CacheLock;
import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.exception.cache.CacheLockOnLockException;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.Logs;
import com.base.components.common.util.SetsHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.user.UserInspectReport;
import com.wk.vpac.main.service.admin.user.UserInspectReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

/**
 * UserInspectReport Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class UserInspectReportController {

  private final UserInspectReportService userInspectReportService;
  private final CacheLock cacheLock;

  public UserInspectReportController(UserInspectReportService userInspectReportService, CacheLock cacheLock){
    this.userInspectReportService = userInspectReportService;
    this.cacheLock = cacheLock;
  }

  @GetMapping(value = "/user/userInspectReport/index")
  public String index(ModelMap model) {
    return "user/userInspectReport/index";
  }

  @GetMapping(value = "/user/userInspectReport/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(PageParamMap params) {
    return new ResponseEntity<>(userInspectReportService.page(params), HttpStatus.OK);
  }

  @GetMapping(value = "/user/userInspectReport/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserInspectReport> findById(@PathVariable("id") String id) {
    return new ResponseEntity<>(userInspectReportService.findById(id), HttpStatus.OK);
  }

  @PostMapping(value = "/user/userInspectReport/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> save(@RequestBody ParamMap params) {
    return new ResponseEntity<>(JsonResult.success(userInspectReportService.save(params)), HttpStatus.OK);
  }

  @DeleteMapping(value = "/user/userInspectReport/del", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@RequestBody ParamMap params) {
    Set<String> ids = SetsHelper.toStringSets(params.getStrTrim("id"), ",");
    int count = ids.isEmpty() ? 0 : userInspectReportService.deleteInBatch(ids);
    return new ResponseEntity<>(JsonResult.success(count), HttpStatus.OK);
  }
  @DeleteMapping(value = "/user/userInspectReport/importNum/del", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> importNumDelete(@RequestBody ParamMap params) {
    int count = userInspectReportService.importNumDelete(params.hasText("importNum", ()->"请输入导入批次"));
    return new ResponseEntity<>(JsonResult.success(count), HttpStatus.OK);
  }

  @PostMapping(value = "/user/userInspectReport/import", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity importUser(@RequestBody ParamMap params) {
    try {
      JsonNode dataFile = JsonUtils.reader(params.getStr("dataFile"), ArrayNode.class).get(0);
      params.put("fileUrl", dataFile.path("attaUrl").textValue());
      params.put("fileName", dataFile.path("attaName").textValue());
    } catch (Exception e) {
      throw new IllegalArgumentException("文件解析错误", e);
    }
    try {
      String fileUrl = params.hasText("fileUrl", () -> "请上传数据文件");
      ObjectNode node = cacheLock.lockIfAbsentAutoUnlock(
        "importOrder", () -> userInspectReportService.importData(
          fileUrl, params.getStrTrimOrEmpty("fileName"), params.get("startRow", 2) - 1));
      return new ResponseEntity<>(JsonResult.success(node), HttpStatus.OK);
    } catch (CacheLockOnLockException e) {
      Logs.get().error("", e);
      throw new IllegalArgumentException("正在导入数据中，请勿稍后再试");
    }
  }
}
