package com.wk.vpac.main.controller.api.chat;

import com.base.components.common.constants.sys.Dates;
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
import com.base.components.common.util.ConvertUtil;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.token.TokenConstant;
import com.wk.vpac.domain.chat.ChatMessage;
import com.wk.vpac.domain.chat.ChatMessageGroupUser;
import com.wk.vpac.domain.user.UserArchive;
import com.wk.vpac.main.service.api.chat.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
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
 * ChatMessage Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2021-07-08
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "会话消息")
public class ChatMessageController {
  private final ChatMessageService chatMessageService;
  public ChatMessageController(ChatMessageService chatMessageService) {
    this.chatMessageService = chatMessageService;
  }

//  @Operation(summary = "1、会话分页")
//  @ApiExtension(author = "李赓", update = "2021-07-08 14:00:05", order = 1, token = {@Token, @Token(name = TokenConstant.EXPERT_TOKEN_KEY)})
//  @RequestModel(value = {
//    @Param(name = "targetUserNickName", value = "对方用户名称"),
//    @Param(name = "targetUserRealName", value = "对方用户真实姓名"),
//    @Param(name = "targetUserPhone", value = "对方用户手机"),
//    @Param(name = "pageNum", value = "当前页数; 默认1", dataType = Integer.class),
//    @Param(name = "pageSize", value = "每页行数; 默认30", dataType = Integer.class)
//  })
//  @ReturnModel(baseModel = DataPage.class, value = {
//    @Param(name = "list[]", value = "结果集", dataType = ArrayType.class, genericType = ChatMessageGroupUser.class),
//    @Param(name = "list[].targetUserNickName", value = "收信用户名称"),
//    @Param(name = "list[].targetUserPhone", value = "收信用户手机，targetUserType=1 时出现"),
//    @Param(name = "list[].targetUserAvatar", value = "收信用户头像"),
//    @Param(name = "list[].targetUserBirth", value = "收信用户生日"),
//    @Param(name = "list[].targetUserGender", value = "收信用户性别; 1=男，2=女，0=未设置", dataType = Integer.class),
//    @Param(name = "list[].targetUserType", value = "收信用户类型; 1=普通用户, 2=专家", dataType = Integer.class),
//    @Param(name = "list[].expertTitle", value = "收信人专家信息，职称，targetUserType=2 时出现"),
//    @Param(name = "list[].expertLevel", value = "收信人专家信息，等级，targetUserType=2 时出现"),
//    @Param(name = "list[].expertJobPosition", value = "收信人专家信息，职位，targetUserType=2 时出现"),
//    @Param(name = "list[].expertHospital", value = "收信人专家信息，医院，targetUserType=2 时出现"),
//    @Param(name = "list[].expertDepartment", value = "收信人专家信息，科室，targetUserType=2 时出现"),
//    @Param(name = "list[].lastMessage", value = "最后的消息"),
//  })
//  @GetMapping(value = "/chat/message/group", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<?> groupPage(@RequestParam Map<String, String> params) {
//    String userId = TokenThreadLocal.getTokenObjNonNull().objId().toString();
//    params.put("userId", userId);
//    params.put("visible", String.valueOf(true));
//    return new ResponseEntity<>(chatMessageService.groupPage(params), HttpStatus.OK);
//  }

  @Operation(summary = "2、创建或查找会话-群聊模式")
  @ApiExtension(author = "李赓", update = "2021-07-08 14:00:05", order = 2,
    token = {@Token, @Token(name = TokenConstant.EXPERT_TOKEN_KEY), @Token(name = TokenConstant.OPERATOR_TOKEN_KEY)})
  @RequestBodyModel(value = {
    @Param(name = "userArchiveId", value = "用户档案ID", required = true),
  })
  @ReturnModel(baseModel = ChatMessageGroupUser.class, value = {
//    @Param(name = "targetUserNickName", value = "收信人昵称", required = true),
//    @Param(name = "targetUserRealName", value = "收信人真实姓名", required = true),
//    @Param(name = "targetUserAvatar", value = "收信人头像", required = true),
//    @Param(name = "targetUserBirth", value = "收信人生日", required = true),
//    @Param(name = "targetUserGender", value = "收信人性别; 1=男，2=女，0=未设置", dataType = Integer.class, required = true),
//    @Param(name = "targetUserType", value = "收信人用户类型; 1=普通用户，2=专家", dataType = Integer.class, required = true),
//    @Param(name = "targetUserPhone", value = "收信人手机号，targetUserType=1 时出现"),
//    @Param(name = "targetUserExpertInfo", value = "收信人专家信息，targetUserType=2 时出现", dataType = Expert.class),
    @Param(name = "userArchive", value = "此会话的档案信息", dataType = UserArchive.class)
  })
  @PostMapping(value = "/chat/message/group", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findOrCreateGroup(@RequestBody Map<String, String> params) {
    return new ResponseEntity<>(chatMessageService.findOrCreateGroupAndUserArchive(params.get("userArchiveId")), HttpStatus.CREATED);
  }

  @Operation(summary = "3、消息分页", description = "调用后会自动清除未读消息数；按发送时间排倒序<br/>"
    + "建议：每次调用后，本地记录一个最小的发送时间=earlyTime，和最大的发送时间=lastTime，无数据时则记录当前时间；<br/>"
    + "使用场景一：进入会话页时，pageNum=1，pageSize=按需设置；<br/>"
    + "使用场景二：轮询最新消息（间隔3-5秒），pageNum=1，pageSize=按需设置，sortType=ASC，createTimeGe={lastTime}；<br/>"
    + "使用场景三：查询历史记录，pageNum=1，pageSize=按需设置，createTimeLe={earlyTime}；<br/>"
    + "以上几种场景返回的数据均有可能与本地的消息重复，建议在本地使用Json对象缓存id值来做数据去重。")
  @ApiExtension(author = "李赓", update = "2024-04-23 15:17:35", order = 3,
    token = {@Token, @Token(name = TokenConstant.EXPERT_TOKEN_KEY), @Token(name = TokenConstant.OPERATOR_TOKEN_KEY)})
  @RequestModel(value = {
    @Param(name = "groupId", value = "会话ID", required = true, requestScope = Scope.PATH),
    @Param(name = "createTimeGe", value = "发送时间大于等于，格式：" + Dates.DATE_TIME_DOC_EXP),
    @Param(name = "createTimeLe", value = "发送时间小于等于，格式：" + Dates.DATE_TIME_DOC_EXP),
    @Param(name = "sortType", value = "排序方式；DESC默认，ASC"),
    @Param(name = "pageNum", value = "当前页数; 默认1", dataType = Integer.class),
    @Param(name = "pageSize", value = "每页行数; 默认30，最大" + Pages.MAX, dataType = Integer.class)
  })
  @ReturnModel(baseModel = DataPage.class, value = {
    @Param(name = "list[]", value = "结果集", dataType = ArrayType.class, genericType = ChatMessage.class),
    @Param(name = "list[].sendUserNickName", value = "发送人用户昵称"),
    @Param(name = "list[].sendUserRealName", value = "发送人用户真实名称"),
    @Param(name = "list[].sendUserAvatar", value = "发送人用户头像"),
    @Param(name = "list[].sendUserType", value = "发送人用户类型; 1=普通用户, 2=专家", dataType = Integer.class),
    @Param(name = "list[].expertJobPosition", value = "专家职位"),
    @Param(name = "list[].expertTitle", value = "专家职称"),
    @Param(name = "list[].expertHospital", value = "专家所在医院"),
  })
  @GetMapping(value = "/chat/message/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(@PathVariable("groupId") String groupId, @RequestParam Map<String, String> params) {
    return new ResponseEntity<>(chatMessageService.page(groupId, params), HttpStatus.OK);
  }

  @Operation(summary = "4、清除未读")
  @ApiExtension(author = "李赓", update = "2021-07-08 14:00:05", order = 4,
    token = {@Token, @Token(name = TokenConstant.EXPERT_TOKEN_KEY), @Token(name = TokenConstant.OPERATOR_TOKEN_KEY)})
  @RequestModel(value = {
    @Param(name = "groupId", value = "会话ID", required = true, requestScope = Scope.PATH)
  })
  @PutMapping(value = "/chat/message/read/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> read(@PathVariable("groupId") String groupId) {
    String currentUserId = chatMessageService.findCurrentUserId(groupId, null);
    chatMessageService.clearUnread(currentUserId, groupId);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Operation(summary = "5、发送消息", description = "；返回空对象表示未发送；未完成实名信息会返回异常errorCode=100")
  @ApiExtension(author = "李赓", update = "2021-07-08 14:00:05", order = 5,
    token = {@Token, @Token(name = TokenConstant.EXPERT_TOKEN_KEY), @Token(name = TokenConstant.OPERATOR_TOKEN_KEY)})
  @RequestBodyModel(value = {
    @Param(name = "groupId", value = "会话ID", required = true),
    @Param(name = "content", value = "消息内容", required = true)
  })
  @PostMapping(value = "/chat/message/send", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> send(@RequestBody Map<String, String> params) {
    String content = StringUtils.trimToEmpty(params.get("content"));
    return new ResponseEntity<>(
      StringUtils.isBlank(content) ? "{}" : chatMessageService.send(params.get("groupId"), content), HttpStatus.CREATED);
  }

  @Operation(summary = "6、删除会话", description = "只是隐藏，不会真的删除")
  @ApiExtension(author = "李赓", update = "2021-07-08 14:00:05", order = 6,
    token = {@Token, @Token(name = TokenConstant.EXPERT_TOKEN_KEY), @Token(name = TokenConstant.OPERATOR_TOKEN_KEY)})
  @RequestModel(value = {
    @Param(name = "groupId", value = "会话ID", required = true, requestScope = Scope.PATH)
  })
  @DeleteMapping(value = "/chat/message/group/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> deleteGroup(@PathVariable("groupId") String groupId) {
    ConvertUtil.checkNotNull(groupId, "请选择会话", String.class);
    String userId = chatMessageService.findCurrentUserId(groupId, null);
    chatMessageService.hideGroup(userId, groupId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

//  @ApiOperation(value = "删除消息（所有关联包括群发）")
//  @ApiExtension(author = "李赓", update = "2021-07-08 14:00:05", token = @Token)
//  @RequestModel(value = {
//    @Param(name = "id", value = "消息ID", required = true, requestScope = Scope.PATH)
//  })
//  @DeleteMapping(value = "/chat/message/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<Void> deleteMessage(@PathVariable("id") String id) {
//    String userId = TokenThreadLocal.getTokenObjNonNull().objId().toString();
//    chatMessageService.deleteMessage(userId, id);
//    return new ResponseEntity<>(HttpStatus.CREATED);
//  }

}
