package com.wk.vpac.main.controller.admin.operation.advertisement;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.util.JsonUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wk.vpac.common.constants.advertisement.BannerPositionCode;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.main.service.admin.operation.advertisement.AdvertisementGroupService;
import com.wk.vpac.main.service.admin.operation.advertisement.AdvertisementService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * AdvertisementController 广告
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2018/4/8 0008 11:20
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class AdvertisementController {


  @Autowired
  private AdvertisementService advertisementService;
  @Autowired
  private AdvertisementGroupService advertisementGroupService;

  /**
   * 跳转到管理页面
   *
   * @param request
   * @param response
   * @param modelMap
   *
   * @return
   */
  @GetMapping(value = {"operation/banner/info/index"})
  public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws IOException {
    //查询活动分组
    modelMap.put("groups", advertisementGroupService.findAll());
    //查询banner positionCode枚举
    List<Map> res = Lists.newArrayList();
    for (BannerPositionCode item : BannerPositionCode.values()) {
      Map<String, String> bannerPositionCode = Maps.newHashMap();
      bannerPositionCode.put(item.getCode(), item.getDesc());
      res.add(bannerPositionCode);
    }
    modelMap.put("bannerPositionCode", JsonUtils.toString(res));
    return "operation/advertisement/info/index";
  }



  /**
   * 分页查询
   *
   * @param params
   *
   * @return
   */
  @GetMapping(value = {"/operation/banner/info/listpage"})
  @ResponseBody
  public ResponseEntity listPage(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(advertisementService.listPage(params), HttpStatus.OK);
  }


  /**
   * 新增编辑
   *
   * @return
   */
  @PostMapping(value = "/operation/banner/info/saveOrUpdate")
  @ResponseBody
  public ResponseEntity saveOrUpdate(@RequestParam Map<String, String> params) {
    if (StringUtils.isNotBlank(params.get("id"))) {
      advertisementService.update(params);
    } else {
      advertisementService.add(params);
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
  @PostMapping(value = "/operation/banner/info/delete")
  @ResponseBody
  public ResponseEntity delete(@RequestParam Map<String, String> params) {
    advertisementService.delete(params);
    return ResponseEntity.ok(JsonResult.success());
  }


  /**
   * 批量修改链接
   *
   * @return
   */
  @PutMapping(value = "/operation/banner/info/urls")
  @ResponseBody
  public ResponseEntity updateUrls(@RequestParam Map<String, String> params) {
    advertisementService.updateUrls(params);
    return ResponseEntity.ok(JsonResult.success());
  }


}
