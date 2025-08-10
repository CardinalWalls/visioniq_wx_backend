package com.wk.vpac.main.controller.api.user;

import com.base.components.common.constants.sys.Pages;
import com.base.components.common.doc.Scope;
import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestBodyModel;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.doc.annotation.Token;
import com.base.components.common.doc.type.ArrayType;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.SetsHelper;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.user.HealthyForm;
import com.wk.vpac.main.service.admin.user.HealthyFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * HealthyForm Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-04-06
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "健康问卷表单")
public class HealthyFormController {

  private final HealthyFormService healthyFormService;

  public HealthyFormController(HealthyFormService healthyFormService){
    this.healthyFormService = healthyFormService;
  }

  @Operation(summary="问卷-分页")
  @ApiExtension(author = "code generator", update = "2023-04-06 13:58", token = @Token)
  @RequestModel({
    @Param(name = "pageNum", value = "当前页数; 默认1", dataType = Integer.class),
    @Param(name = "pageSize", value = "每页行数; 默认30", dataType = Integer.class)
  })
  @ReturnModel(baseModel = DataPage.class, value = {
    @Param(name = "list[]", value = "数据列表", dataType = ArrayType.class, genericType = HealthyForm.class)
  })
  @GetMapping(value = "/user/healthyForm/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(@RequestParam Map<String, String> params) {
    String userId = TokenThreadLocal.getTokenObjNonNull().objId().toString();
    return new ResponseEntity<>(DataPage.from(healthyFormService.findAll(
      ConditionGroup.build().addCondition("userId", ConditionEnum.OPERATE_EQUAL, userId),
      Pages.Helper.pageable(params, Sort.by(Sort.Direction.DESC, "createTime")))), HttpStatus.OK);
  }

  @Operation(summary="问卷-根据ID查询")
  @ApiExtension(author = "code generator", update = "2023-04-06 13:58", token = @Token)
  @RequestModel({
    @Param(name = "id", value = "ID", required = true, requestScope = Scope.PATH)
  })
  @GetMapping(value = "/user/healthyForm/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HealthyForm> findById(@PathVariable String id) {
    return new ResponseEntity<>(healthyFormService.findById(id), HttpStatus.OK);
  }

  @Operation(summary="问卷-新增")
  @ApiExtension(author = "code generator", update = "2023-04-06 13:58", token = @Token)
  @RequestBodyModel(baseModel = HealthyForm.class, value = {
    @Param(name = "userId", hidden = true),
  })
  @PostMapping(value = "/user/healthyForm", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HealthyForm> save(@RequestBody Map<String, String> params) {
    params.put("userId", TokenThreadLocal.getTokenObjNonNull().objId().toString());
    return new ResponseEntity<>(healthyFormService.save(params), HttpStatus.CREATED);
  }

  @Operation(summary="问卷-修改")
  @ApiExtension(author = "code generator", update = "2023-04-06 13:58", token = @Token)
  @RequestBodyModel(baseModel = HealthyForm.class, value = {
    @Param(name = "id", value = "表单ID", required = true),
    @Param(name = "userId", hidden = true),
  })
  @PutMapping(value = "/user/healthyForm", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HealthyForm> update(@RequestBody Map<String, String> params) {
    HealthyForm healthyForm = ConvertUtil.populate(healthyFormService.findById(params.get("id")), params);
    return new ResponseEntity<>(healthyFormService.save(healthyForm), HttpStatus.CREATED);
  }

  @Operation(summary="问卷-删除")
  @ApiExtension(author = "code generator", update = "2023-04-06 13:58", token = @Token)
  @RequestModel({
    @Param(name = "ids", value = "ID多个以逗号分隔", required = true, requestScope = Scope.PATH)
  })
  @DeleteMapping(value = "/user/healthyForm/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Integer> delete(@PathVariable String ids) {
    int count = healthyFormService.deleteInBatch(SetsHelper.toStringSets(ids, ","));
    return new ResponseEntity<>(count, HttpStatus.CREATED);
  }
}
