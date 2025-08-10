

package com.wk.vpac.main.controller.admin.auth;

import com.base.components.common.exception.ExceptionViewPathHandler;
import com.wk.vpac.common.constants.sys.GatewayPath;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ErrorController
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2018-04-08 15:08
 */
@Controller
@RequestMapping(GatewayPath.ADMIN + "/error")
public class ErrorController implements ExceptionViewPathHandler {
  @Override
  @RequestMapping("/500")
  public String internalServerError() {
    return "error/500";
  }
  @Override
  @RequestMapping("/400")
  public String badRequest() {
    return "error/400";
  }
  @Override
  @RequestMapping("/401")
  public String unauthorized() {
    return "error/401";
  }
  @Override
  @RequestMapping("/403")
  public String forbidden() {
    return "error/403";
  }
  @Override
  @RequestMapping("/404")
  public String notFound() {
    return "error/404";
  }
}
