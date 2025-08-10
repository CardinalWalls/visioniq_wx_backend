package com.wk.vpac.main.controller.api.user;

import com.base.components.common.doc.Scope;
import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestBodyModel;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.doc.annotation.Token;
import com.base.components.common.doc.type.ArrayType;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.token.TokenCacheObj;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.SetsHelper;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.token.TokenConstant;
import com.wk.vpac.common.token.user.ExpertToken;
import com.wk.vpac.common.token.user.UserToken;
import com.wk.vpac.domain.user.AppointmentExpert;
import com.wk.vpac.main.service.api.user.AppointmentExpertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * AppointmentExpert Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-02-15
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "在线预约模块")
public class AppointmentExpertController {

  private final AppointmentExpertService appointmentExpertService;

  public AppointmentExpertController(AppointmentExpertService appointmentExpertService){
    this.appointmentExpertService = appointmentExpertService;
  }

  @Operation(summary="在线预约分页")
  @ApiExtension(author = "code generator", update = "2023-02-15 17:06", token = {@Token, @Token(name = TokenConstant.EXPERT_TOKEN_KEY)})
  @RequestModel({
    @Param(name = "targetTimeStart", value = "预约时间开始，格式：yyyy-MM-dd"),
    @Param(name = "targetTimeEnd", value = "预约时间截止，格式：yyyy-MM-dd"),
    @Param(name = "pageNum", value = "当前页数; 默认1", dataType = Integer.class),
    @Param(name = "pageSize", value = "每页行数; 默认30", dataType = Integer.class),
    @Param(name = "userArchiveId", value = "档案ID"),
    @Param(name = "userRiskLevel", value = "档案风险等级；0=无，1=低风险，2=中风险，3=高风险", dataType = Integer.class),
  })
  @ReturnModel(baseModel = DataPage.class, value = {
    @Param(name = "list[]", value = "数据列表", dataType = ArrayType.class, genericType = AppointmentExpert.class),
    @Param(name = "list[].userName", value = "档案姓名"),
    @Param(name = "list[].parentPhone", value = "父母手机号"),
    @Param(name = "list[].userIdCard", value = "档案身份证"),
    @Param(name = "list[].userBirth", value = "档案生日"),
    @Param(name = "list[].userGender", value = "档案性别，1=男，2=女，0=未设置", dataType = Integer.class),
    @Param(name = "list[].expertName", value = "专家姓名"),
    @Param(name = "list[].expertAvatar", value = "专家头像"),
    @Param(name = "list[].expertHospital", value = "专家的医院"),
    @Param(name = "list[].expertDepartment", value = "专家的科室"),
    @Param(name = "list[].expertTitle", value = "专家的职称"),
    @Param(name = "list[].expertJobPosition", value = "专家的职位")
  })
  @GetMapping(value = "/user/appointmentExpert/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(@RequestParam Map<String, String> params) {
    TokenCacheObj tokenObj = TokenThreadLocal.getTokenObjNonNull();
    if(tokenObj instanceof ExpertToken t){
      params.put("expertId", t.getExpertId());
    }
    else if(tokenObj instanceof UserToken t){
      params.put("userId", t.getUserId());
    }
    return new ResponseEntity<>(appointmentExpertService.page(params), HttpStatus.OK);
  }

  @Operation(summary="在线预约新增", description = "未完成实名信息会返回异常errorCode=100")
  @ApiExtension(author = "code generator", update = "2023-02-15 17:06", token = @Token)
  @RequestBodyModel({
    @Param(name = "expertId", value = "专家ID", required = true),
    @Param(name = "userArchiveId", value = "档案ID", required = true),
    @Param(name = "targetTime", value = "预约时间，格式：yyyy-MM-dd", required = true)
  })
  @PostMapping(value = "/user/appointmentExpert", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AppointmentExpert> save(@RequestBody Map<String, String> params) {
    return new ResponseEntity<>(appointmentExpertService.save(
      params.get("expertId"), params.get("userArchiveId"), params.get("targetTime")
    ), HttpStatus.CREATED);
  }

  @Operation(summary="在线预约删除", description = "未完成实名信息会返回异常errorCode=100")
  @ApiExtension(author = "code generator", update = "2023-02-15 17:06", token = @Token)
  @RequestModel({
    @Param(name = "ids", value = "ID多个以逗号分隔", required = true, requestScope = Scope.PATH)
  })
  @DeleteMapping(value = "/user/appointmentExpert/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Integer> delete(@PathVariable String ids) {
    int count = appointmentExpertService.delete(SetsHelper.toStringSets(ids, ","));
    return new ResponseEntity<>(count, HttpStatus.CREATED);
  }
}
