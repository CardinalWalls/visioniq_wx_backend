package com.wk.vpac.main.controller.admin.user;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.SetsHelper;
import com.wk.vpac.domain.user.HealthyForm;
import com.wk.vpac.main.service.admin.user.HealthyFormService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * HealthyForm Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-04-06
 */
@Controller
@RequestMapping("/admin")
public class HealthyFormController {

  private final HealthyFormService healthyFormService;

  public HealthyFormController(HealthyFormService healthyFormService){
    this.healthyFormService = healthyFormService;
  }

  @GetMapping(value = "/user/healthyForm/index")
  public String index(ModelMap model) {
    return "user/healthyForm/index";
  }

  @GetMapping(value = "/user/healthyForm/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(healthyFormService.page(params), HttpStatus.OK);
  }

  @GetMapping(value = "/user/healthyForm/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HealthyForm> findById(@PathVariable String id) {
    return new ResponseEntity<>(healthyFormService.findById(id), HttpStatus.OK);
  }

  @PostMapping(value = "/user/healthyForm/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> save(@RequestBody Map<String, String> params) {
    return new ResponseEntity<>(JsonResult.success(healthyFormService.save(params)), HttpStatus.OK);
  }

  @DeleteMapping(value = "/user/healthyForm/del", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@RequestBody Map<String,String> params) {
    String deleteId = ConvertUtil.checkNotNull(params, "id", "删除id不能为空", String.class);
    int count = healthyFormService.deleteInBatch(SetsHelper.toStringSets(deleteId, ","));
    return new ResponseEntity<>(JsonResult.success(count), HttpStatus.OK);
  }
}
