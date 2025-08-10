package com.wk.vpac.main.interceptors;

import com.wk.vpac.main.service.admin.member.SysMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * BaseAdminInterceptor
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2019-04-30 13:51
 */
public abstract class BaseAdminInterceptor implements HandlerInterceptor {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected boolean preHandleReturn(Object handler, HttpServletRequest request, HttpServletResponse response,
                                    HttpStatus httpStatus, Exception exception) throws Exception{
    if(handler instanceof ResourceHttpRequestHandler){
      return true;
    }
    if (exception != null) {
      getSysMemberService().cleanCurrentAdminAuth();
      throw exception;
    }
    else{
      response.setStatus(httpStatus.value());
      request.getRequestDispatcher("/admin/error/" + httpStatus.value()).forward(request, response);
      logger.error(request.getRequestURI() + " > " + httpStatus.value());
      return false;
    }
  }

  protected abstract SysMemberService getSysMemberService();
}
