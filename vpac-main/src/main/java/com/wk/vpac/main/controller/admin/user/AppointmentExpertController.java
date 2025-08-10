package com.wk.vpac.main.controller.admin.user;

import com.wk.vpac.main.service.admin.user.AppointmentExpertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * AppointmentExpert Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-02-15
 */
@Controller
@RequestMapping("/admin")
public class AppointmentExpertController {

  private final AppointmentExpertService appointmentExpertService;

  public AppointmentExpertController(AppointmentExpertService appointmentExpertService){
    this.appointmentExpertService = appointmentExpertService;
  }

  @GetMapping(value = "/user/appointmentExpert/index")
  public String index(ModelMap model) {
    return "user/appointmentExpert/index";
  }

  @GetMapping(value = "/user/appointmentExpert/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(appointmentExpertService.page(params), HttpStatus.OK);
  }

//  @GetMapping(value = "/user/appointmentExpert/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<AppointmentExpert> findById(@PathVariable String id) {
//    return new ResponseEntity<>(appointmentExpertService.findById(id), HttpStatus.OK);
//  }
//
//  @PostMapping(value = "/user/appointmentExpert/save", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<?> save(@RequestBody Map<String, String> params) {
//    return new ResponseEntity<>(JsonResult.success(appointmentExpertService.save(params)), HttpStatus.OK);
//  }
//
//  @DeleteMapping(value = "/user/appointmentExpert/del", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<?> delete(@RequestBody Map<String,String> params) {
//    String deleteId = ConvertUtil.checkNotNull(params, "id", "删除id不能为空", String.class);
//    int count = appointmentExpertService.deleteInBatch(SetsHelper.toStringSets(deleteId, ","));
//    return new ResponseEntity<>(JsonResult.success(count), HttpStatus.OK);
//  }
}
