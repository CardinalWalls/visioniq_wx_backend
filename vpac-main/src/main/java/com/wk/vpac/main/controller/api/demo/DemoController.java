

package com.wk.vpac.main.controller.api.demo;

import com.base.components.common.service.ThreadPoolService;
import com.base.components.common.util.IPUtils;
import com.base.components.common.util.JsonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Test Controller
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2017-11-10 10:47
 */
//@RestController
//@RequestMapping(GatewayPath.API)
//@Profile("!prod")
public class DemoController {

  @Autowired
  private ThreadPoolService threadPoolService;

  @GetMapping(value = "/demo", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getTest(@RequestParam Map<String, String> params, HttpServletRequest request) {
    ObjectNode node = JsonUtils.createObjectNode().put("addr", request.getRemoteAddr())
                              .put("x-forward-for", request.getHeader(HttpHeaders.X_FORWARDED_FOR))
                              .put("scheme", request.getScheme())
                              .put("host", request.getRemoteHost())
                              .put("serverName", request.getServerName())
                              .put("url", request.getRequestURL().toString())
                              .put("thread", Thread.currentThread().toString())
                              .put("realIp", IPUtils.getRealIp(request));
    long b0 = System.currentTimeMillis();
    threadPoolService.runWithMainThreadWait(1, ()->{
      System.out.println(Thread.currentThread());
      LoggerFactory.getLogger(DemoController.class).error("aaaaa");
    });
    LoggerFactory.getLogger(DemoController.class).error("bbbbb");
    System.err.println(" >>> " + (System.currentTimeMillis() - b0));
    return new ResponseEntity<>(node, HttpStatus.OK);
  }


}
