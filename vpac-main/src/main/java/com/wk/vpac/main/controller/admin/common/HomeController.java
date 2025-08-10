

package com.wk.vpac.main.controller.admin.common;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ServletContextHolder;
import com.google.common.collect.Maps;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.token.user.UserMemberToken;
import com.wk.vpac.database.dao.user.UserBaseInfoDao;
import com.wk.vpac.main.constants.admin.BaseAdminProperties;
import com.wk.vpac.main.service.admin.member.SysMemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;


@Controller
//@RefreshScope
public class HomeController {
  @Autowired
  private SysMemberService sysMemberService;
  @Autowired
  private BaseAdminProperties baseAdminProperties;
  @Autowired
  private UserBaseInfoDao userBaseInfoDao;

  @GetMapping({GatewayPath.ADMIN, GatewayPath.ADMIN +"/"})
  public String index(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    if(TokenThreadLocal.getTokenObj() == null){
      return ServletContextHolder.sendRedirectString("redirect:" + GatewayPath.ADMIN + "/login");
    }
    return ServletContextHolder.sendRedirectString("redirect:" + GatewayPath.ADMIN + "/index");
  }

  /**
   * 首页
   *
   * @return
   */
  @GetMapping(GatewayPath.ADMIN + "/index")
  public String indexPage(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response)
    throws IOException {
    UserMemberToken memberToken = TokenThreadLocal.getTokenObjNonNull(UserMemberToken.class);
    modelMap.put("systemName", baseAdminProperties.getInfo().getSystemName());
    modelMap.put("memberToken", memberToken);
    modelMap.put("appMenu", sysMemberService.loadAppInfoMenuJson(memberToken.getId()));
    return "common/index";
  }

  @GetMapping(value = GatewayPath.ADMIN + "/home")
  public String home(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    modelMap.put("systemName", baseAdminProperties.getInfo().getSystemName());
    return "common/home";
  }
  @GetMapping(value = GatewayPath.ADMIN + "/information")
  public String information(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    UserMemberToken memberToken = TokenThreadLocal.getTokenObjNonNull(UserMemberToken.class);
    modelMap.put("memberToken", memberToken);
    return "common/information";
  }

  @SystemLogger
  @PostMapping(value = GatewayPath.ADMIN + "/information/update")
  @ResponseBody
  public ResponseEntity informationUpdate(HttpServletRequest request, HttpServletResponse response,
                                          @RequestParam Map<String, String> param) {
    return ResponseEntity.ok(JsonResult.success(sysMemberService.updateBaseInfoAndTokenObj(param)));
  }

  @GetMapping(value = GatewayPath.ADMIN + "/home/statics")
  public ResponseEntity homeStatics(@RequestParam Map<String, String> params) {
    Map<String, Object> map = Maps.newHashMap();
    map.putAll(userBaseInfoDao.userTypeSum());
    return new ResponseEntity<>(JsonResult.success(map), HttpStatus.OK);
  }
  @GetMapping(value = GatewayPath.ADMIN + "/home/statics/chart")
  public ResponseEntity homeStaticsChart(@RequestParam Map<String, String> params) {
    Map<String, Object> map = Maps.newHashMap();
    return new ResponseEntity<>(JsonResult.success(map), HttpStatus.OK);
  }

}
