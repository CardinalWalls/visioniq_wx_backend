package com.wk.vpac.main.controller.admin.operation.advertisement;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.log.system.SystemLogger;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.operations.AdvertisementGroup;
import com.wk.vpac.main.service.admin.operation.advertisement.AdvertisementGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * AdvertisementGroupController 广告分组
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2018/4/8 0008 11:20
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class AdvertisementGroupController {

  @Autowired
  AdvertisementGroupService advertisementGroupService;


  /**
   * 跳转到分组管理页面
   *
   * @param request
   * @param response
   * @param modelMap
   *
   * @return
   */
  @GetMapping(value = {"/operation/banner/group/index"})
  public String typeIndex(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
    return "operation/advertisement/group/index";
  }


  @GetMapping(value = {"/operation/banner/group/list"})
  @ResponseBody
  public ResponseEntity list(@RequestParam Map<String, String> params) {
    List<AdvertisementGroup> all = advertisementGroupService.findAll();
    return new ResponseEntity<>(all, HttpStatus.OK);
  }
  /**
   * 分页查询
   *
   * @param params
   *
   * @return
   */
  @GetMapping(value = {"/operation/banner/group/listpage"})
  @ResponseBody
  public ResponseEntity listPage(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(DataPage.from(advertisementGroupService.listPage(params)), HttpStatus.OK);
  }


  @PostMapping("/operation/banner/group/saveOrUpdate")
  @ResponseBody
  public ResponseEntity saveOrUpdate(AdvertisementGroup ag) {
    AdvertisementGroup rs = null;
    if (StringUtils.isNotBlank(ag.getId())) {
      advertisementGroupService.update(ag);
    } else {
      advertisementGroupService.add(ag);
    }
    return ResponseEntity.ok(JsonResult.success());
  }

  /**
   * 删除
   *
   * @param params
   *
   * @return
   */
  @PostMapping("/operation/banner/group/delete")
  @ResponseBody
  public ResponseEntity delete(@RequestParam Map<String, String> params) {
    advertisementGroupService.delete(params);
    return ResponseEntity.ok(JsonResult.success());
  }



}
