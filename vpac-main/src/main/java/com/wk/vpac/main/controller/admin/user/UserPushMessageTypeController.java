package com.wk.vpac.main.controller.admin.user;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.util.SetsHelper;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.main.service.admin.user.UserPushMessageTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

/**
 * UserPushMessageType Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2024-04-23
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class UserPushMessageTypeController {

  private final UserPushMessageTypeService userPushMessageTypeService;

  public UserPushMessageTypeController(UserPushMessageTypeService userPushMessageTypeService){
    this.userPushMessageTypeService = userPushMessageTypeService;
  }

  @GetMapping(value = "/user/userPushMessageType/index")
  public String index(ModelMap model) {
    return "user/userPushMessageType/index";
  }

  @GetMapping(value = "/user/userPushMessageType/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(PageParamMap params) {
    return new ResponseEntity<>(userPushMessageTypeService.page(params), HttpStatus.OK);
  }

  @PostMapping(value = "/user/userPushMessageType/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> save(@RequestBody ParamMap params) {
    return new ResponseEntity<>(JsonResult.success(userPushMessageTypeService.save(params)), HttpStatus.OK);
  }

  @DeleteMapping(value = "/user/userPushMessageType/del", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@RequestBody ParamMap params) {
    Set<String> ids = SetsHelper.toStringSets(params.getStrTrim("id"), ",");
    userPushMessageTypeService.delete(ids);
    return new ResponseEntity<>(JsonResult.success(), HttpStatus.OK);
  }
}
