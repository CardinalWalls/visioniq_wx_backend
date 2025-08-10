package com.wk.vpac.main.controller.admin.user;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.util.SetsHelper;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.user.UserVisionReport;
import com.wk.vpac.main.service.admin.user.UserVisionReportService;
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
 * UserVisionReport Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class UserVisionReportController {

  private final UserVisionReportService userVisionReportService;

  public UserVisionReportController(UserVisionReportService userVisionReportService){
    this.userVisionReportService = userVisionReportService;
  }

  @GetMapping(value = "/user/userVisionReport/index")
  public String index(ModelMap model) {
    return "user/userVisionReport/index";
  }

  @GetMapping(value = "/user/userVisionReport/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(PageParamMap params) {
    return new ResponseEntity<>(userVisionReportService.page(params), HttpStatus.OK);
  }

  @GetMapping(value = "/user/userVisionReport/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserVisionReport> findById(@PathVariable("id") String id) {
    return new ResponseEntity<>(userVisionReportService.findById(id), HttpStatus.OK);
  }

  @PostMapping(value = "/user/userVisionReport/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> save(@RequestBody ParamMap params) {
    return new ResponseEntity<>(JsonResult.success(userVisionReportService.save(params)), HttpStatus.OK);
  }

  @DeleteMapping(value = "/user/userVisionReport/del", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@RequestBody ParamMap params) {
    Set<String> ids = SetsHelper.toStringSets(params.getStrTrim("id"), ",");
    int count = ids.isEmpty() ? 0 : userVisionReportService.deleteInBatch(ids);
    return new ResponseEntity<>(JsonResult.success(count), HttpStatus.OK);
  }
}
