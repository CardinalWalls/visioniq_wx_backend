package com.wk.vpac.main.controller.admin.user;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.util.ConvertUtil;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.main.service.admin.user.ExpertService;
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

import java.util.Map;

/**
 * Expert Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-02-15
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class ExpertController {

  private final ExpertService expertService;

  public ExpertController(ExpertService expertService){
    this.expertService = expertService;
  }

  @GetMapping(value = "/user/expert/index")
  public String index(ModelMap model) {
    return "user/expert/index";
  }

  @GetMapping(value = "/user/expert/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(expertService.page(params), HttpStatus.OK);
  }

  @PostMapping(value = "/user/expert/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> save(@RequestBody Map<String, String> params) {
    return new ResponseEntity<>(JsonResult.success(expertService.save(params)), HttpStatus.OK);
  }

  @PostMapping(value = "/user/expert/audit", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> audit(@RequestBody Map<String, String> params) {
    return new ResponseEntity<>(JsonResult.success(expertService.audit(ConvertUtil.checkNotNull(params.get("id"), "请选择一个专家数据", String.class))), HttpStatus.OK);
  }

  @PostMapping(value = "/user/expert/qrCode", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> qrCode(@RequestBody Map<String, String> params) {
    return new ResponseEntity<>(JsonResult.success(expertService.qrCode(ConvertUtil.checkNotNull(params.get("id"), "请选择一个专家数据", String.class))), HttpStatus.OK);
  }

}
