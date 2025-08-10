package com.wk.vpac.main.controller.api.user;

import com.base.components.common.constants.Valid;
import com.base.components.common.constants.sys.Pages;
import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestBodyModel;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.doc.annotation.Token;
import com.base.components.common.token.TokenThreadLocal;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.token.TokenConstant;
import com.wk.vpac.common.token.user.ExpertToken;
import com.wk.vpac.common.token.user.OperatorToken;
import com.wk.vpac.domain.user.Expert;
import com.wk.vpac.main.service.admin.user.ExpertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Expert Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-02-15
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "专家、客服模块")
public class ExpertController {

  private final ExpertService expertService;

  public ExpertController(ExpertService expertService){
    this.expertService = expertService;
  }

  @Operation(summary="专家-查询信息")
  @ApiExtension(author = "code generator", update = "2023-02-15 17:05", token = {@Token, @Token(name = TokenConstant.EXPERT_TOKEN_KEY)})
  @RequestModel({
    @Param(name = "id", value = "专家ID, 为空时查询当前专家用户的信息"),
  })
  @ReturnModel(baseModel = Expert.class, value = {
    @Param(name = "name", value = "姓名"),
    @Param(name = "phone", value = "手机号"),
    @Param(name = "avatar", value = "头像"),
    @Param(name = "gender", value = "性别，1=男，2=女，0=未设置", dataType = Integer.class),
    @Param(name = "bindCount", value = "绑定人数", dataType = Integer.class),
  })
  @GetMapping(value = "/user/expert/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> info(@RequestParam Map<String, String> params) {
    String id = params.get("id");
    if(StringUtils.isBlank(id)){
      ExpertToken expertToken = TokenThreadLocal.getTokenObj(ExpertToken.class);
      if(expertToken != null){
        id = expertToken.getExpertId();
      }
    }else{
      params.put("hidePhone", String.valueOf(true));
    }
    if(StringUtils.isBlank(id)){
      return new ResponseEntity<>(HttpStatus.OK);
    }
    params.put("id", id);
    List<Map<String, Object>> list = expertService.page(params).getList();
    if(list.isEmpty()){
      return new ResponseEntity<>(HttpStatus.OK);
    }
    expertService.incrementViewCount(id, 1);
    Map<String, Object> row = list.get(0);
    row.put("bindCount", expertService.findBindCount(id));
    return new ResponseEntity<>(row, HttpStatus.OK);
  }

//  @Operation(summary="专家-查询绑定自己的平台用户")
//  @ApiExtension(author = "code generator", update = "2023-02-15 17:05", token = @Token(name = TokenConstant.EXPERT_TOKEN_KEY))
//  @RequestModel({
//    @Param(name = "searchKey", value = "查询关键字"),
//    @Param(name = "pageNum", value = "当前页数; 默认1", dataType = Integer.class),
//    @Param(name = "pageSize", value = "每页行数; 默认30", dataType = Integer.class)
//  })
//  @ReturnModel(baseModel = DataPage.class, value = {
//    @Param(name = "list", value = "数据列表", dataType = ArrayType.class, genericType = Expert.class),
//    @Param(name = "list.name", value = "昵称"),
//    @Param(name = "list.realName", value = "真实姓名"),
//    @Param(name = "list.phone", value = "手机号"),
//    @Param(name = "list.avatar", value = "头像"),
//    @Param(name = "list.birth", value = "生日"),
//    @Param(name = "list.gender", value = "性别，1=男，2=女，0=未设置", dataType = Integer.class),
//    @Param(name = "list.bindExpertTime", value = "绑定专家的时间")
//  })
//  @GetMapping(value = "/user/expert/bind-user/page", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<?> bindUserPage(@RequestParam Map<String, String> params) {
//    String expertId = TokenThreadLocal.getTokenObjNonNull(ExpertToken.class).getExpertId();
//    params.put("expertId", expertId);
//    return new ResponseEntity<>(userBaseInfoService.querySimpleUser(params), HttpStatus.OK);
//  }
//
//  @Operation(summary="专家-当前用户统计")
//  @ApiExtension(author = "code generator", update = "2023-02-15 17:05", token = @Token(name = TokenConstant.EXPERT_TOKEN_KEY))
//  @ReturnModel(value = {
//    @Param(name = "todayUser", value = "今日绑定用户数", dataType = Integer.class),
//    @Param(name = "totalUser", value = "总绑定用户数", dataType = Integer.class),
//    @Param(name = "todoAppointment", value = "待处理预约数（含今天）", dataType = Integer.class),
//    @Param(name = "totalAppointment", value = "总预约数", dataType = Integer.class),
//    @Param(name = "unreadChatGroup", value = "未读会话数", dataType = Integer.class),
//    @Param(name = "totalChatGroup", value = "总会话数", dataType = Integer.class),
//  })
//  @GetMapping(value = "/user/expert/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<?> statistics(@RequestParam Map<String, String> params) {
//    ExpertToken expert = TokenThreadLocal.getTokenObjNonNull(ExpertToken.class);
//    return new ResponseEntity<>(expertService.statistics(expert), HttpStatus.OK);
//  }
  @Operation(summary="专家-发起申请")
  @ApiExtension(author = "code generator", update = "2024-06-19 12:25:25", token = @Token)
  @RequestBodyModel({
    @Param(name = "name", value = "姓名", required = true),
    @Param(name = "regionId", value = "地区ID", required = true),
    @Param(name = "avatar", value = "头像", required = true),
    @Param(name = "title", value = "职称"),
    @Param(name = "jobPosition", value = "职位"),
    @Param(name = "hospital", value = "医院", required = true),
    @Param(name = "department", value = "科室", required = true),
    @Param(name = "level", value = "等级"),
    @Param(name = "profile", value = "个人简介"),
    @Param(name = "gender", value = "性别；1=男，2=女"),
    @Param(name = "avatar", value = "形象照片", required = true),
    @Param(name = "workCard", value = "工作证件", required = true),
  })
  @PostMapping(value = "/user/expert/apply", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Expert> apply(@RequestBody Map<String, String> params) {
    return new ResponseEntity<>(expertService.apply(params), HttpStatus.CREATED);
  }

  @Operation(summary="专家-修改信息")
  @ApiExtension(author = "code generator", update = "2023-02-15 17:06", token = @Token(name = TokenConstant.EXPERT_TOKEN_KEY))
  @RequestBodyModel({
    @Param(name = "name", value = "姓名"),
    @Param(name = "regionId", value = "地区ID"),
    @Param(name = "avatar", value = "头像"),
    @Param(name = "title", value = "职称"),
    @Param(name = "jobPosition", value = "职位"),
    @Param(name = "hospital", value = "医院"),
    @Param(name = "department", value = "科室"),
    @Param(name = "level", value = "等级"),
    @Param(name = "profile", value = "个人简介"),
    @Param(name = "gender", value = "性别；1=男，2=女"),
    @Param(name = "avatar", value = "形象照片"),
    @Param(name = "workCard", value = "工作证件"),
  })
  @PutMapping(value = "/user/expert/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Expert> update(@RequestBody Map<String, String> params) {
    ExpertToken token = TokenThreadLocal.getTokenObjNonNull(ExpertToken.class);
    params.remove("userId");
    params.remove("valid");
    params.remove("regionName");
    params.put("id", token.getExpertId());
    return new ResponseEntity<>(expertService.save(params), HttpStatus.CREATED);
  }

  @Operation(summary="专家-修改预约日期限制")
  @ApiExtension(author = "code generator", update = "2023-02-15 17:06", token = @Token(name = TokenConstant.EXPERT_TOKEN_KEY))
  @RequestBodyModel({
    @Param(name = "limitWeekDays", value = "限制预约星期，例：星期一三五，135", required = true)
  })
  @PutMapping(value = "/user/expert/appointmentLimit", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> appointmentLimit(@RequestBody Map<String, String> params) {
    expertService.updateAppointmentLimit(TokenThreadLocal.getTokenObjNonNull(ExpertToken.class),
                                         params.get("limitWeekDays"));
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
  @Operation(summary="专家-统计")
  @ApiExtension(author = "code generator", update = "2023-02-15 17:05", token = {@Token(name = TokenConstant.EXPERT_TOKEN_KEY)})
  @ReturnModel(value = {
    @Param(name = "totalUnread", value = "未读消息总数", dataType = Integer.class),
  })
  @GetMapping(value = "/user/expert/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> expertStatistics(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(expertService.expertStatistics(TokenThreadLocal.getTokenObjNonNull(ExpertToken.class)), HttpStatus.OK);
  }

  @Operation(summary="客服-统计")
  @ApiExtension(author = "code generator", update = "2023-02-15 17:05", token = {@Token(name = TokenConstant.EXPERT_TOKEN_KEY)})
  @ReturnModel(value = {
    @Param(name = "totalUnread", value = "未读消息总数", dataType = Integer.class),
  })
  @GetMapping(value = "/user/operator/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> operatorStatistics(@RequestParam Map<String, String> params) {
    TokenThreadLocal.getTokenObjNonNull(OperatorToken.class);
    return new ResponseEntity<>(expertService.operatorStatistics(), HttpStatus.OK);
  }


  @Operation(summary="专家-分页")
  @ApiExtension(author = "code generator", update = "2023-02-15 17:05", token = {@Token(name = TokenConstant.EXPERT_TOKEN_KEY)})
  @ReturnModel(value = {
      @Param(name = "name", value = "姓名"),
      @Param(name = "phone", value = "手机号"),
      @Param(name = Pages.PAGE_NUM_VAR_NAME, value = "当前页数; 默认" + Pages.PAGE_NUM, dataType = Integer.class),
      @Param(name = Pages.PAGE_SIZE_VAR_NAME, value = "每页行数; 默认" + Pages.PAGE_SIZE, dataType = Integer.class)
  })
  @GetMapping(value = "/user/expert/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(@RequestParam Map<String, String> params) {
    params.put("status", String.valueOf(Valid.TRUE.getVal()));
    return new ResponseEntity<>(expertService.page(params), HttpStatus.OK);
  }
}
