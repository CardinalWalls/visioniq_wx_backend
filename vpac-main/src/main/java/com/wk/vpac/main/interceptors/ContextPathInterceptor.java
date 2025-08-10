package com.wk.vpac.main.interceptors;

import com.base.components.common.exception.GlobalExceptionHandler;
import com.wk.vpac.common.constants.sys.GatewayPath;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ContextPathInterceptor
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2019-05-09 15:39
 */
public class ContextPathInterceptor extends GlobalExceptionHandler.ResultHttpOkInterceptor {
  public static final String TEMPLATE_ROOT_PATH_KEY = "_ROOT_PATH_";
  public static final String CONTEXT_PATH_KEY = "ctx";

  @Override
  protected boolean doSetSimpleResult(HttpServletRequest request, HttpServletResponse response, Object handler) {
    request.setAttribute(TEMPLATE_ROOT_PATH_KEY, "");
    String contextPath = request.getContextPath();
    request.setAttribute(CONTEXT_PATH_KEY, "/".equals(contextPath) ? "" : contextPath);
    //设置ADMIN只返回200状态
    return request.getRequestURI().startsWith(GatewayPath.ADMIN);
  }
}
