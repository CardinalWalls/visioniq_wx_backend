package com.wk.vpac.main.controller.api.statistics;

import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.doc.annotation.Token;
import com.base.components.common.doc.type.ObjectType;
import com.google.common.collect.Maps;
import com.wk.vpac.common.constants.sys.GatewayPath;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * StatisticsController
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2023-01-12 14:56
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "统计数据")
public class StatisticsController {
  @Operation(summary = "业务数据统计")
  @ApiExtension(author = "code generator", update = "2023-01-07 23:07", token = @Token)
  @ReturnModel(baseModel = ObjectType.class, value = {
  })
  @GetMapping(value = "/statistics/base", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> base(@RequestParam Map<String, String> params) {
    Map<String, Object> map = Maps.newHashMap();
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
}
