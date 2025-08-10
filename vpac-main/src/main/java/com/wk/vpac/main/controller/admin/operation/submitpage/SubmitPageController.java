package com.wk.vpac.main.controller.admin.operation.submitpage;

import com.base.components.common.log.system.SystemLogger;
import com.wk.vpac.common.constants.sys.GatewayPath;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * SubmitPageController 小程序页面推送给微信收录
 *
 * @author Jiangwen
 * @version 1.0.0, 2018-03-27 13:46
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class SubmitPageController {


  /**
   * 跳转到管理页面
   *
   * @param request
   * @param response
   * @param modelMap
   *
   * @return
   */
  @GetMapping({"/submitPage/index"})
  public String typeIndex(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
    return "operation/submitPage/index";
  }






}
