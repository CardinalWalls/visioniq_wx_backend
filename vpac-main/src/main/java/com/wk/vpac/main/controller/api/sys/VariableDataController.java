package com.wk.vpac.main.controller.api.sys;

import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.sys.VariableData;
import com.wk.vpac.main.service.api.sys.VariableDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="tecyun@foxmail.com">Huangyunyang</a>
 * @version 1.0.0, 2018/5/21 0021 11:55
 */
@RestController
@Tag(name = "通用接口")
public class VariableDataController {

  @Autowired
  private VariableDataService variableDataService;

  @Operation(summary="动态配置数据查询")
  @ApiExtension(author = "李赓", update = "2019-01-09 09:29")
  @RequestModel({
    @Param(name = "type", value = "类别", dataType = Integer.class),
    @Param(name = "refId", value = "关联项"),
  })
  @GetMapping(path = GatewayPath.API + "/sys/variabledata", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<VariableData>> listData(@RequestParam(required = false) Map<String, String> params) {
    ConditionGroup<VariableData> build = ConditionGroup.build();
    if(StringUtils.isNotBlank(params.get("type"))){
      build.addCondition("type", ConditionEnum.OPERATE_EQUAL, Integer.parseInt(params.get("type")));
    }
    if(StringUtils.isNotBlank(params.get("refId"))){
      build.addCondition("refId", ConditionEnum.OPERATE_EQUAL, params.get("refId"));
    }
    List<VariableData> list = variableDataService.findAll(build, Sort.by(Sort.Direction.ASC, "orderNo"));
    return ResponseEntity.ok(list);
  }
}
