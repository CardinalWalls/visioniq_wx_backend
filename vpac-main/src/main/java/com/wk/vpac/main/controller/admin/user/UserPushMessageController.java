package com.wk.vpac.main.controller.admin.user;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.SetsHelper;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.main.service.admin.user.UserPushMessageService;
import com.wk.vpac.main.service.admin.user.UserPushMessageTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Set;

/**
 * UserPushMessage Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class UserPushMessageController {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final UserPushMessageService userPushMessageService;
  private final UserPushMessageTypeService userPushMessageTypeService;

  public UserPushMessageController(UserPushMessageService userPushMessageService,
                                   UserPushMessageTypeService userPushMessageTypeService){
    this.userPushMessageService = userPushMessageService;
    this.userPushMessageTypeService = userPushMessageTypeService;
  }

  @GetMapping(value = "/user/userPushMessage/index")
  public String index(ModelMap model) throws IOException {
    model.put("conditionFields", JsonUtils.toString(UserPushMessageService.COLUMN_MAP.values()));
    return "user/userPushMessage/index";
  }

  @GetMapping(value = "/user/userPushMessage/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(PageParamMap params) {
    return new ResponseEntity<>(userPushMessageService.page(params), HttpStatus.OK);
  }
  @GetMapping(value = "/user/userPushMessage/type/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> typePage(PageParamMap params) {
    return new ResponseEntity<>(userPushMessageTypeService.page(params), HttpStatus.OK);
  }

  @GetMapping(value = "/user/userPushMessage/userArchive/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> userArchivePage(PageParamMap params) {
    return new ResponseEntity<>(userPushMessageService.userArchivePage(params), HttpStatus.OK);
  }

  @PostMapping(value = "/user/userPushMessage/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> save(@RequestBody ParamMap params) {
    return new ResponseEntity<>(JsonResult.success(userPushMessageService.save(params)), HttpStatus.OK);
  }
  @PutMapping(value = "/user/userPushMessage/publish/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> publish(@PathVariable("id") String id, @RequestBody ParamMap params) {
    return new ResponseEntity<>(JsonResult.success(userPushMessageService.publish(id)), HttpStatus.OK);
  }

  @DeleteMapping(value = "/user/userPushMessage/del", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@RequestBody ParamMap params) {
    Set<String> ids = SetsHelper.toStringSets(params.getStrTrim("id"), ",");
    int count = ids.isEmpty() ? 0 : userPushMessageService.deleteInBatch(ids);
    return new ResponseEntity<>(JsonResult.success(count), HttpStatus.OK);
  }
}
