package com.wk.vpac.main.controller.api.sys;

import com.base.components.common.doc.Scope;
import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestBodyModel;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.util.JsonUtils;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.main.service.api.sys.SysMd5JsonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.TreeMap;

/**
 * SysMd5JsonController
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2021-09-28 09:42
 */
@RestController
@Tag(name = "通用接口")
public class SysMd5JsonController {
  @Autowired
  private SysMd5JsonService sysMd5JsonService;

  @Operation(summary="持久化参数-生成MD5码")
  @ApiExtension(author = "李赓", update = "2021-09-28 09:29:30")
  @RequestBodyModel({
    @Param(name = "paramJson", value = "Json参数", example = "{\"a\":\"1\",\"b\":\"2\"}", required = true)
  })
  @PostMapping(path = GatewayPath.API + "/public/param-md5", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> toMd5(@RequestBody Map<String, String> params) {
    String md5 = "";
    try {
      md5 = sysMd5JsonService.toMd5(JsonUtils.reader(params.get("paramJson"), TreeMap.class));
    } catch (Exception ignore) {
    }
    return ResponseEntity.ok(md5);
  }

  @Operation(summary="持久化参数-根据MD5码获取参数")
  @ApiExtension(author = "李赓", update = "2021-09-28 09:29:30")
  @RequestModel({
    @Param(name = "md5", value = "MD5码", requestScope = Scope.PATH, required = true)
  })
  @GetMapping(path = GatewayPath.API + "/public/param-md5/{md5}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getParam(@PathVariable("md5") String md5) {
    return ResponseEntity.ok(sysMd5JsonService.getParam(md5));
  }
}
