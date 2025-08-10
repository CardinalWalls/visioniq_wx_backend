

package com.wk.vpac.main.controller.admin.common;

import com.base.components.cache.Cache;
import com.base.components.cache.CacheManager;
import com.base.components.common.exception.SimpleErrorLogger;
import com.base.components.common.exception.business.PasswordErrorException;
import com.base.components.common.id.IdGenerator;
import com.base.components.common.util.IPUtils;
import com.base.components.common.util.ServletContextHolder;
import com.wk.vpac.cache.CacheName;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.token.TokenType;
import com.wk.vpac.common.token.user.UserMemberToken;
import com.wk.vpac.main.service.admin.member.SysMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 登录/退出 Controller
 *
 * @author <a href="yijianhua@xianyunsoft.com">Yi Jianhua</a>
 * @version 1.0.0, 2017-11-27 16:48
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
//@RefreshScope
public class LoginController {
//  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  @Autowired
  private SysMemberService memberService;

  @Value("${base.admin.info.system-name:}")
  private String systemName;
  @Value("${base.admin.info.login-title:}")
  private String loginTitle;
  @Value("${base.admin.info.copyright:}")
  private String copyright;
  @Autowired
  private CacheManager cacheManager;



  /**
   * 登录页
   *
   * @return
   */
  @GetMapping("/login")
  public String login(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
//    modelMap.put("captchaPreCode", IdGenerator.getInstance().generate().toString());
    modelMap.put("copyright", copyright);
    modelMap.put("systemName", systemName);
    modelMap.put("loginTitle", loginTitle.replace("{systemName}", systemName));
    modelMap.put("topWindow", "1".equals(request.getParameter("topWindow")) ? "1" : "0");
    memberService.addTargetPathCookie(request, response);
    return "common/login";
  }

  /**
   * 机构注册页
   *
   * @return
   */
  @GetMapping("/register/org")
  public String registerOrg() {
    return "common/org_register";
  }

  /**
   * 服务人员注册页
   *
   * @return
   */
  @GetMapping("/register/servicer")
  public String registerServicer() {
    return "common/servicer_register";
  }

  /**
   * 实现登录
   *
   * @param username
   * @param password
   * @param model
   *
   * @return
   */
  @PostMapping("/login")
  public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                      ModelMap model, HttpServletRequest request, HttpServletResponse response) {
    model.put("copyright", copyright);
    model.put("systemName", systemName);
    model.put("loginTitle", loginTitle.replace("{systemName}", systemName));
    model.put("topWindow", "1".equals(request.getParameter("topWindow")) ? "1" : "0");
    Cache preCache = cacheManager.getCache(CacheName.NORMAL_CAPTCHA_IMG);
    String pwdErrorCountKey = "pwdErrorCount:" + username;
    Integer pwdErrorCount = preCache.get(pwdErrorCountKey, Integer.class);
    if(pwdErrorCount == null){
      pwdErrorCount = 0;
    }
    int maxErrorCount = 3;
    try {
      if(pwdErrorCount >= maxErrorCount){
        String captchaCode = request.getParameter("captchaCode");
        String captchaPreCode = request.getParameter("captchaPreCode");
        Assert.hasText(captchaPreCode, "请填写验证码");
        Assert.hasText(captchaCode, "请填写验证码");
        String cacheCode = preCache.get(captchaPreCode, String.class);
        Assert.isTrue(captchaCode.equalsIgnoreCase(cacheCode), "验证码错误");
      }
      UserMemberToken userMemberToken = memberService.login(username, password, IPUtils.getRealIp(request));
      Cookie cookie = new Cookie(TokenType.USER_MEMBER_TOKEN.getTokenKey(), userMemberToken.getToken());
      cookie.setPath(GatewayPath.ADMIN);
      cookie.setHttpOnly(true);
      response.addCookie(cookie);
      preCache.evict(pwdErrorCountKey);
      return ServletContextHolder.sendRedirectString("redirect:" + GatewayPath.ADMIN + "/index");
    } catch (Exception e) {
      if(e instanceof PasswordErrorException){
        preCache.put("pwdErrorCount:" + username, ++pwdErrorCount);
      }
      if(pwdErrorCount >= 3){
        model.put("captchaPreCode", IdGenerator.getInstance().generate().toString());
      }
      SimpleErrorLogger.error(e);
      model.put("errorMsg", e.getMessage());
      return "common/login";
    }
  }

  /**
   * 退出登录
   *
   * @return
   */
  @GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    String token = null;
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if(TokenType.USER_MEMBER_TOKEN.getTokenKey().equals(cookie.getName())){
        token = cookie.getValue();
        break;
      }
    }
    if(token != null){
      memberService.logout(token);
    }
    Cookie cookie = new Cookie(TokenType.USER_MEMBER_TOKEN.getTokenKey(), "");
    cookie.setMaxAge(0);
    cookie.setPath(GatewayPath.ADMIN);
    response.addCookie(cookie);
    return ServletContextHolder.sendRedirectString("redirect:" + GatewayPath.ADMIN + "/login");
  }
}
