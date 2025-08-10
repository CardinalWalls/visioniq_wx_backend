package com.wk.vpac.main.controller.admin.user;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.main.service.admin.user.UserArchiveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * UserArchive Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class UserArchiveController {

  private final UserArchiveService userArchiveService;

  public UserArchiveController(UserArchiveService userArchiveService){
    this.userArchiveService = userArchiveService;
  }

  @GetMapping(value = "/user/userArchive/index")
  public String index(ModelMap model) {
    return "user/userArchive/index";
  }

  @GetMapping(value = "/user/userArchive/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(PageParamMap params) {
    return new ResponseEntity<>(userArchiveService.page(params), HttpStatus.OK);
  }

  @PostMapping(value = "/user/userArchive/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> save(@RequestBody ParamMap params) {
    return new ResponseEntity<>(JsonResult.success(userArchiveService.save(params)), HttpStatus.OK);
  }
}
