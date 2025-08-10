

package com.wk.vpac.main.interceptors;

import com.base.components.common.dto.auth.AuthorizationProperties;
import com.base.components.common.dto.auth.AuthorizationPropertiesMap;
import com.base.components.common.exception.auth.AuthException;
import com.base.components.common.exception.other.ForbiddenException;
import com.base.components.common.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.wk.vpac.main.dto.member.AdminAuth;
import com.wk.vpac.main.service.admin.member.SysMemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * 接收由网关层传输过来的token对象，在 AdminOnlyLoginInterceptor 之后
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2017-07-12 14:03
 */
@Component
//@RefreshScope
public class AdminAuthInterceptor extends BaseAdminInterceptor {
  /** 已授权并传参的应用程序请求标记 */
  public static final String PASS_AUTH_APP_FORWARD = "011fcb32-7201-4f06-9dc6-e719fd971654";
  public static final String PASS_AUTH_APP_FORWARD_KEY = "PASS_AUTH_APP_FORWARD";
  private static final String APP_NAME_KEY = "_APP_NAME_";
  @Value("${spring.application.name}")
  private String appName;
  @Autowired
  private SysMemberService sysMemberService;
  @Autowired
  private AuthorizationPropertiesMap authorizationPropertiesMap;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String uri = request.getRequestURI();
    if (PASS_AUTH_APP_FORWARD.equals(request.getAttribute(PASS_AUTH_APP_FORWARD_KEY))) {
      //让已授权并传参的应用通过
      return true;
    }
    AdminAuth adminAuth = sysMemberService.getCurrentAdminAuth();
    if (adminAuth != null) {
      String checked = adminAuth.checkAuthPath(uri, request.getMethod());
      if (checked != null) {
        request.setAttribute(PASS_AUTH_APP_FORWARD_KEY, PASS_AUTH_APP_FORWARD);
        request.setAttribute(APP_NAME_KEY, checked);
//        String params = getParams(checked.getParamsJson());
//        if (params != null) {
//          request.getRequestDispatcher(uri + "?aaa=1" ).forward(request, response);
//          return false;
//        }
        return true;
      }
      else{
        if (adminAuth.getSuperRole()) {
          return true;
        }
        return preHandleReturn(handler, request, response, HttpStatus.FORBIDDEN, new ForbiddenException());
      }
    }
    else {
      return preHandleReturn(handler, request, response, HttpStatus.UNAUTHORIZED, new AuthException());
    }
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                         ModelAndView modelAndView) throws Exception {
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    throws Exception {
    sysMemberService.cleanCurrentAdminAuth();
  }

  public List<String> getClearSkipAuthUri(){
    AuthorizationProperties properties = authorizationPropertiesMap.get(appName);
    List<String> uriList = Lists.newArrayList();
    for (String uri : properties.getSkipCheckMapping()) {
      if(uri.contains(":")){
        String[] arr = StringUtils.split(uri, ":");
        uriList.add(arr[1]);
      }
      else{
        uriList.add(uri);
      }
    }
    return uriList;
  }

  private String getParams(String paramJson) {
    if (StringUtils.isNotBlank(paramJson)
      && !"{}".equals(paramJson) && paramJson.startsWith("{") && paramJson.endsWith("}")) {
      try {
        Map<String, Object> params = JsonUtils.reader(paramJson, new TypeReference<Map<String, Object>>() {
        });
        if (!params.isEmpty()) {
          StringBuilder sb = new StringBuilder();
          for (Map.Entry<String, Object> param : params.entrySet()) {
            sb.append("&").append(param.getKey()).append("=").append(param.getValue());
          }
          return sb.toString();
        }
      } catch (Exception ignored) {
      }
    }
    return null;
  }

  @Override
  protected SysMemberService getSysMemberService() {
    return sysMemberService;
  }



}
