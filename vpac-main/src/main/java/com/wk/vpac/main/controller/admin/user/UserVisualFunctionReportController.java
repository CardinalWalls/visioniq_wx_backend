package com.wk.vpac.main.controller.admin.user;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.util.SetsHelper;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.user.UserVisualFunctionReport;
import com.wk.vpac.main.service.admin.user.UserVisualFunctionReportService;
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
 * UserVisualFunctionReport Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2024-05-09
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class UserVisualFunctionReportController {

  private final UserVisualFunctionReportService userVisualFunctionReportService;

  public UserVisualFunctionReportController(UserVisualFunctionReportService userVisualFunctionReportService){
    this.userVisualFunctionReportService = userVisualFunctionReportService;
  }

  @GetMapping(value = "/user/userVisualFunctionReport/index")
  public String index(ModelMap model) {
    return "user/userVisualFunctionReport/index";
  }

  @GetMapping(value = "/user/userVisualFunctionReport/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(PageParamMap params) {
    return new ResponseEntity<>(userVisualFunctionReportService.page(params), HttpStatus.OK);
  }

  @GetMapping(value = "/user/userVisualFunctionReport/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserVisualFunctionReport> findById(@PathVariable("id") String id) {
    return new ResponseEntity<>(userVisualFunctionReportService.findById(id), HttpStatus.OK);
  }

  @PostMapping(value = "/user/userVisualFunctionReport/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> save(@RequestBody ParamMap params) {
    return new ResponseEntity<>(JsonResult.success(userVisualFunctionReportService.save(params)), HttpStatus.OK);
  }

  @DeleteMapping(value = "/user/userVisualFunctionReport/del", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@RequestBody ParamMap params) {
    Set<String> ids = SetsHelper.toStringSets(params.getStrTrim("id"), ",");
    int count = ids.isEmpty() ? 0 : userVisualFunctionReportService.deleteInBatch(ids);
    return new ResponseEntity<>(JsonResult.success(count), HttpStatus.OK);
  }
}
