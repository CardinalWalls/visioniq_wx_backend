package com.wk.vpac.main.controller.api.user;

import com.base.components.common.constants.sys.Pages;
import com.base.components.common.doc.Scope;
import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Check;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestBodyModel;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.doc.annotation.Token;
import com.base.components.common.doc.type.ArrayType;
import com.base.components.common.doc.validation.EL;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.token.RequireToken;
import com.base.components.common.util.SetsHelper;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.token.TokenConstant;
import com.wk.vpac.common.token.user.ExpertToken;
import com.wk.vpac.common.token.user.OperatorToken;
import com.wk.vpac.common.token.user.UserToken;
import com.wk.vpac.domain.user.UserArchive;
import com.wk.vpac.main.service.api.user.UserArchiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * UserArchive Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "用户档案")
public class UserArchiveController {

  private final UserArchiveService userArchiveService;

  public UserArchiveController(UserArchiveService userArchiveService){
    this.userArchiveService = userArchiveService;
  }

  @Operation(summary="档案-分页")
  @ApiExtension(author = "code generator", update = "2023-09-11 15:29",
      token = {@Token, @Token(name = TokenConstant.EXPERT_TOKEN_KEY), @Token(name = TokenConstant.OPERATOR_TOKEN_KEY)})
  @RequestModel({
    @Param(name = "id", value = "档案ID"),
    @Param(name = "userPhone", value = "父母用户手机号"),
    @Param(name = "name", value = "档案姓名"),
    @Param(name = "searchKey", value = "搜索关键词"),
    @Param(name = "riskLevel", value = "风险等级；0=无，1=低风险，2=中风险，3=高风险", dataType = Integer.class),
    @Param(name = Pages.SORT_VAR_NAME, value = "排序字段, createTime DESC（默认）、updateTime DESC"),
    @Param(name = Pages.PAGE_NUM_VAR_NAME, value = "当前页数; 默认" + Pages.PAGE_NUM, dataType = Integer.class),
    @Param(name = Pages.PAGE_SIZE_VAR_NAME, value = "每页行数; 默认" + Pages.PAGE_SIZE, dataType = Integer.class)
  })
  @ReturnModel(baseModel = DataPage.class, value = {
    @Param(name = "list[]", value = "数据列表", dataType = ArrayType.class, genericType = UserArchive.class),
    @Param(name = "list[].expertName", value = "专家姓名"),
    @Param(name = "list[].expertAvatar", value = "专家头像"),
    @Param(name = "list[].expertTitle", value = "专家职称"),
    @Param(name = "list[].expertJobPosition", value = "专家职位"),
    @Param(name = "list[].expertHospital", value = "专家所在医院"),
    @Param(name = "list[].expertProficient", value = "专家擅长专业"),
    @Param(name = "list[].parentPhone", value = "父母账户手机号"),
    @Param(name = "list[].unread", value = "对话未读数", dataType = Integer.class),
    @Param(name = "list[].lastMessage", value = "对话最后一条消息"),
    @Param(name = "list[].lastMessageTime", value = "对话最后一条消息发送时间"),
    @Param(name = "list[].chatGroupId", value = "对话组ID"),
  })
  @RequireToken({UserToken.class, ExpertToken.class, OperatorToken.class})
  @GetMapping(value = "/user/userArchive/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(PageParamMap params) {
    return new ResponseEntity<>(userArchiveService.page(params), HttpStatus.OK);
  }

  @Operation(summary="档案-分页（客服运营）")
  @ApiExtension(author = "code generator", update = "2023-09-11 15:29", token = @Token(name = TokenConstant.OPERATOR_TOKEN_KEY))
  @RequestModel({
    @Param(name = "userPhone", value = "父母用户手机号"),
    @Param(name = "name", value = "档案姓名"),
    @Param(name = "searchKey", value = "搜索关键词"),
    @Param(name = "regionIds", value = "所在地区ID，多个用应为逗号分隔"),
    @Param(name = "expertIds", value = "所属专家ID，多个用应为逗号分隔"),
    @Param(name = "riskLevel", value = "风险等级；0=无，1=低风险，2=中风险，3=高风险", dataType = Integer.class),
    @Param(name = Pages.PAGE_NUM_VAR_NAME, value = "当前页数; 默认" + Pages.PAGE_NUM, dataType = Integer.class),
    @Param(name = Pages.PAGE_SIZE_VAR_NAME, value = "每页行数; 默认" + Pages.PAGE_SIZE, dataType = Integer.class)
  })
  @ReturnModel(baseModel = DataPage.class, value = {
    @Param(name = "list[]", value = "数据列表", dataType = ArrayType.class, genericType = UserArchive.class),
    @Param(name = "list[].expertName", value = "专家姓名"),
    @Param(name = "list[].expertAvatar", value = "专家头像"),
    @Param(name = "list[].expertTitle", value = "专家职称"),
    @Param(name = "list[].expertJobPosition", value = "专家职位"),
    @Param(name = "list[].expertHospital", value = "专家所在医院"),
    @Param(name = "list[].expertProficient", value = "专家擅长专业"),
    @Param(name = "list[].parentPhone", value = "父母账户手机号"),
    @Param(name = "list[].unread", value = "对话未读数", dataType = Integer.class),
    @Param(name = "list[].lastMessage", value = "对话最后一条消息"),
    @Param(name = "list[].lastMessageTime", value = "对话最后一条消息发送时间"),
    @Param(name = "list[].chatGroupId", value = "对话组ID"),
  })
  @RequireToken(OperatorToken.class)
  @GetMapping(value = "/user/userArchive/pageForOperator", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> pageForOperator(PageParamMap params) {
    return new ResponseEntity<>(userArchiveService.pageForOperator(params), HttpStatus.OK);
  }

  @Operation(summary="档案-根据ID查询")
  @ApiExtension(author = "code generator", update = "2023-09-11 15:29",
    token = {@Token, @Token(name = TokenConstant.EXPERT_TOKEN_KEY), @Token(name = TokenConstant.OPERATOR_TOKEN_KEY)})
  @RequestModel({
    @Param(name = "id", value = "ID", required = true, requestScope = Scope.PATH)
  })
  @ReturnModel(baseModel = UserArchive.class, value = {
    @Param(name = "expertName", value = "专家姓名"),
    @Param(name = "expertAvatar", value = "专家头像"),
    @Param(name = "expertTitle", value = "专家职称"),
    @Param(name = "expertJobPosition", value = "专家职位"),
    @Param(name = "expertHospital", value = "专家所在医院"),
    @Param(name = "expertProficient", value = "专家擅长专业"),
    @Param(name = "parentPhone", value = "父母账户手机号"),
  })
  @GetMapping(value = "/user/userArchive/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findById(@PathVariable("id") String id) {
    return new ResponseEntity<>(userArchiveService.findRowMapById(id), HttpStatus.OK);
  }

  @Operation(summary="档案-新增")
  @ApiExtension(author = "code generator", update = "2023-09-11 15:29",
      token = {@Token, @Token(name = TokenConstant.EXPERT_TOKEN_KEY), @Token(name = TokenConstant.OPERATOR_TOKEN_KEY)})
  @RequestBodyModel({
    @Param(name = "phone", value = "归属账号的手机号码, 专家账户录入时必填"),
    @Param(name = "name", value = "姓名", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "gender", value = "性别; 1=男，2=女", required = true, dataType = Integer.class, checkEL = EL.NOT_BLANK),
    @Param(name = "birth", value = "出生日期(yyyy-MM-dd)", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "parentsMyopia", value = "双亲是否近视; 0=无, 1=父, 2=母, 3=父母", dataType = Integer.class, required = true,
      check = @Check(contains = {"0", "1", "2", "3"})),
    @Param(name = "regionId", value = "所在地区", required = true),
    @Param(name = "idcard", value = "身份证", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "schoolName", value = "学校名称"),
    @Param(name = "orgName", value = "机构名称"),
    @Param(name = "remark", value = "备注"),
    @Param(name = "riskLevel", value = "风险等级；0=无，1=低风险，2=中风险，3=高风险", dataType = Integer.class),
  })
  @RequireToken({UserToken.class, ExpertToken.class, OperatorToken.class})
  @PostMapping(value = "/user/userArchive", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserArchive> save(@RequestBody ParamMap params) {
    return new ResponseEntity<>(userArchiveService.save(params), HttpStatus.CREATED);
  }

  @Operation(summary="档案-修改")
  @ApiExtension(author = "code generator", update = "2023-09-11 15:29",
      token = {@Token, @Token(name = TokenConstant.EXPERT_TOKEN_KEY), @Token(name = TokenConstant.OPERATOR_TOKEN_KEY)})
  @RequestBodyModel({
    @Param(name = "id", value = "ID", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "name", value = "姓名", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "gender", value = "性别; 1=男，2=女", required = true, dataType = Integer.class, checkEL = EL.NOT_BLANK),
    @Param(name = "birth", value = "出生日期(yyyy-MM-dd)", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "parentsMyopia", value = "双亲是否近视; 0=无, 1=父, 2=母, 3=父母", dataType = Integer.class, required = true,
      check = @Check(contains = {"0", "1", "2", "3"})),
    @Param(name = "regionId", value = "所在地区", required = true),
    @Param(name = "idcard", value = "身份证", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "schoolName", value = "学校名称"),
    @Param(name = "orgName", value = "机构名称"),
    @Param(name = "remark", value = "备注"),
    @Param(name = "riskLevel", value = "风险等级；0=无，1=低风险，2=中风险，3=高风险", dataType = Integer.class),
  })
  @RequireToken({UserToken.class, ExpertToken.class, OperatorToken.class})
  @PutMapping(value = "/user/userArchive", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserArchive> update(@RequestBody ParamMap params) {
    return new ResponseEntity<>(userArchiveService.save(params), HttpStatus.CREATED);
  }

  @Operation(summary="档案-删除")
  @ApiExtension(author = "code generator", update = "2023-09-11 15:29",
      token = {@Token, @Token(name = TokenConstant.EXPERT_TOKEN_KEY), @Token(name = TokenConstant.OPERATOR_TOKEN_KEY)})
  @RequestModel({
    @Param(name = "ids", value = "ID多个以逗号分隔", required = true, requestScope = Scope.PATH)
  })
  @RequireToken({UserToken.class, ExpertToken.class, OperatorToken.class})
  @DeleteMapping(value = "/user/userArchive/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@PathVariable("ids") String ids) {
    Set<String> idSet = SetsHelper.toStringSets(ids, ",");
    userArchiveService.delete(idSet);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Operation(summary="档案-统计")
  @ApiExtension(author = "code generator", update = "2023-09-11 15:29",
    token = {@Token(name = TokenConstant.OPERATOR_TOKEN_KEY), @Token(name = TokenConstant.EXPERT_TOKEN_KEY)})
  @RequireToken({OperatorToken.class, ExpertToken.class})
  @GetMapping(value = "/user/userArchive/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> statistics(ParamMap params) {
    return new ResponseEntity<>(userArchiveService.statistics(params), HttpStatus.OK);
  }
}
