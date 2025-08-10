package com.wk.vpac.main.rpc;

import com.base.components.common.dto.sys.ParamMap;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * DemoClient
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2024-07-04 16:14
 */
@FeignClient(name = "vpac-main")
public interface DemoClient {
  @GetMapping(value = "/api/demo/rpc", produces = MediaType.APPLICATION_JSON_VALUE)
  ObjectNode demoRpc(@RequestParam ParamMap params);
}
