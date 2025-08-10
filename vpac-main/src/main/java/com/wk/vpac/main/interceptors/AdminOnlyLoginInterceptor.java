package com.wk.vpac.main.interceptors;

import com.base.components.common.exception.auth.AuthException;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ServletContextHolder;
import com.google.common.collect.Sets;
import com.wk.vpac.common.token.user.UserMemberToken;
import com.wk.vpac.main.constants.admin.BaseAdminProperties;
import com.wk.vpac.main.service.admin.member.SysMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * AdminOnlyLoginInterceptor 在 TokenObjInterceptor 之后
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2019-04-29 15:26
 */
@Component
public class AdminOnlyLoginInterceptor extends BaseAdminInterceptor {
  private Set<String> indexPath = Sets.newHashSet("/admin", "/admin/");
  @Autowired
  private SysMemberService sysMemberService;
  @Autowired
  private BaseAdminProperties baseAdminProperties;
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    sysMemberService.addTargetPathCookie(request, response);
    if(TokenThreadLocal.getTokenObj(UserMemberToken.class) != null){
      //刷新token时间
      sysMemberService.getCurrentAdminAuth();
      request.setAttribute(AdminAuthInterceptor.PASS_AUTH_APP_FORWARD_KEY, AdminAuthInterceptor.PASS_AUTH_APP_FORWARD);
      return true;
    }
    if(indexPath.contains(request.getRequestURI())){
      ServletContextHolder.sendRedirect(baseAdminProperties.getLoginPath());
      return false;
    }
    return preHandleReturn(handler, request, response, HttpStatus.UNAUTHORIZED, new AuthException());
  }

  @Override
  protected SysMemberService getSysMemberService() {
    return sysMemberService;
  }

  public List<String> getOnlyLoginAuthPath() {
    return baseAdminProperties.getOnlyLoginAuthPath();
  }

}
