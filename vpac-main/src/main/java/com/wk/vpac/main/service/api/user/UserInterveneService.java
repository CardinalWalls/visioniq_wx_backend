package com.wk.vpac.main.service.api.user;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.exception.Exceptions;
import com.base.components.common.util.JsonUtils;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.wk.vpac.common.constants.sys.ChatAutoReplyType;
import com.wk.vpac.database.dao.user.ExpertDao;
import com.wk.vpac.database.dao.user.UserArchiveDao;
import com.wk.vpac.database.dao.user.UserInterveneDao;
import com.wk.vpac.domain.chat.ChatMessage;
import com.wk.vpac.domain.chat.ChatMessageGroupUser;
import com.wk.vpac.domain.user.UserArchive;
import com.wk.vpac.domain.user.UserIntervene;
import com.wk.vpac.main.service.api.chat.ChatMessageService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * UserIntervene Service
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class UserInterveneService extends AbstractJpaService<UserIntervene, String, UserInterveneDao> {
  private final ChatMessageService chatMessageService;
  private final UserArchiveDao userArchiveDao;
  private final ExpertDao expertDao;

  public UserInterveneService(ChatMessageService chatMessageService, UserArchiveDao userArchiveDao, ExpertDao expertDao) {
    this.chatMessageService = chatMessageService;
    this.userArchiveDao = userArchiveDao;
    this.expertDao = expertDao;
  }


  public DataPage<RowMap> page(PageParamMap params) {
    return getDao().page(params);
  }

  public UserIntervene save(ParamMap params) {
    String id = params.getStrTrimOrEmpty("id");
    String scheme = params.hasText("scheme", ()->"请填写干预措施").trim();
    Integer type = params.notNull("type", Integer.class, () -> "请选择干预类型");
    Assert.isTrue(type >= 0 && type <= 4, () -> "请选择正确的干预类型");
    UserIntervene userIntervene;
    if (StringUtils.isBlank(id)) {
      String userArchiveId = params.hasText("userArchiveId", ()->"请选择用户档案").trim();
      userIntervene = new UserIntervene();
      userIntervene.setCreateTime(new Date());
      userIntervene.setUserArchiveId(userArchiveId);
    } else {
      userIntervene = getDao().findById(id).orElseThrow(Exceptions.of(() -> "未找到数据"));
      Assert.isTrue(StringUtils.isBlank(userIntervene.getPushTime()), () -> "此记录已推送不能修改");
    }
    userIntervene.setScheme(scheme);
    userIntervene.setRemark(params.getStrTrimOrEmpty("remark"));
    userIntervene.setType(type);
    return TransactionActivation.start(() -> saveAndFlush(userIntervene));
  }

  public UserIntervene push(String id) {
    UserIntervene userIntervene = getDao().findById(id).orElseThrow(Exceptions.of(() -> "未找到数据"));
    Assert.isTrue(StringUtils.isBlank(userIntervene.getPushTime()), () -> "此记录已推送，请勿重复操作");
    UserArchive userArchive = userArchiveDao.findById(userIntervene.getUserArchiveId())
                                            .orElseThrow(Exceptions.of(() -> "未找到用户档案信息"));
    String expertUserId = expertDao.findUserIdById(userArchive.getExpertId());
    return TransactionActivation.start(()->{
      Assert.hasText(expertUserId, ()->"未找到专家信息");
      ChatMessageGroupUser groupUser = chatMessageService.findOrCreateGroup(userArchive);
      DateTime now = DateTime.now();
      Date nowDate = now.toDate();
      ChatMessage message = new ChatMessage();
      message.setGroupId(groupUser.getGroupId());
      message.setSystemMsg(ChatAutoReplyType.VISION_INTERVENE.getVal());
      message.setContent(JsonUtils.createObjectNode()
                                  .put("type", userIntervene.getType())
                                  .put("scheme", userIntervene.getScheme())
                                  .put("remark", userIntervene.getRemark()).toString());
      message.setCreateTime(nowDate);
      message.setSendUserId(expertUserId);
      message.setSystemMsgTypeName(ChatAutoReplyType.VISION_INTERVENE.getDesc());
      message = chatMessageService.save(message);
      getEntityManager().createNativeQuery("UPDATE wk_chat_message_group_user SET last_message_id=:msgId, "
                                             + "last_message_time=:nowDate, unread=unread+1, visible=true "
                                             + "WHERE group_id=:groupId AND user_id = :userId")
                        .setParameter("msgId", message.getId())
                        .setParameter("groupId", groupUser.getGroupId())
                        .setParameter("userId", userArchive.getUserId())
                        .setParameter("nowDate", nowDate).executeUpdate();
      userIntervene.setPushTime(now.toString(Dates.DATE_TIME_FORMATTER));
      saveAndFlush(userIntervene);
      return userIntervene;
    });
  }
  @Transactional(rollbackFor = Exception.class)
  public void delete(Set<String> ids) {
    List<UserIntervene> list = findAllById(ids);
    for (UserIntervene userIntervene : list) {
      Assert.isTrue(StringUtils.isBlank(userIntervene.getPushTime()), () -> "不能删除已推送的记录");
      getDao().delete(userIntervene);
    }
  }
}