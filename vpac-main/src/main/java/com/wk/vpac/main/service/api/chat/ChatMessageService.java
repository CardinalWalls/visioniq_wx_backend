package com.wk.vpac.main.service.api.chat;

import com.base.components.cache.CacheLock;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.exception.Exceptions;
import com.base.components.common.id.IdGenerator;
import com.base.components.common.token.TokenCacheObj;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.Md5Util;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;
import com.wk.vpac.common.constants.sys.ChatAutoReplyType;
import com.wk.vpac.common.token.user.ExpertToken;
import com.wk.vpac.common.token.user.OperatorToken;
import com.wk.vpac.common.token.user.UserToken;
import com.wk.vpac.database.dao.chat.ChatMessageDao;
import com.wk.vpac.database.dao.chat.ChatMessageGroupDao;
import com.wk.vpac.database.dao.chat.ChatMessageGroupUserDao;
import com.wk.vpac.database.dao.sys.SysAutoReplyDao;
import com.wk.vpac.database.dao.user.ExpertDao;
import com.wk.vpac.database.dao.user.UserArchiveDao;
import com.wk.vpac.database.dao.user.UserBaseInfoDao;
import com.wk.vpac.domain.chat.ChatMessage;
import com.wk.vpac.domain.chat.ChatMessageGroup;
import com.wk.vpac.domain.chat.ChatMessageGroupUser;
import com.wk.vpac.domain.sys.SysAutoReply;
import com.wk.vpac.domain.user.UserArchive;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

/**
 * ChatMessage Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class ChatMessageService extends AbstractJpaService<ChatMessage, String, ChatMessageDao> {
//  private final String appName;
  private final ChatMessageGroupUserDao chatMessageGroupUserDao;
  private final UserBaseInfoDao userBaseInfoDao;
  private final CacheLock cacheLock;
  private final ExpertDao expertDao;
  private final ChatMessageGroupDao chatMessageGroupDao;
//  private final SmsService smsService;
  private final UserArchiveDao userArchiveDao;
  private final IdGenerator idGenerator;
  private final SysAutoReplyDao sysAutoReplyDao;

  public DataPage<Map<String, Object>> page(String groupId, Map<String, String> params){
    if(StringUtils.isBlank(groupId)){
      return DataPage.getEmpty();
    }
    params.put("groupId", groupId);
    String userId = findCurrentUserId(groupId, (isOperatorToken, id)->{
      if(!isOperatorToken){
        params.put("userId", id);
      }
    });
    if (StringUtils.isNotBlank(userId)) {
      clearUnread(userId, groupId);
    }
    return getDao().page(params);
  }



//  public DataPage<Map<String, Object>> groupPage(Map<String, String> params){
//    return chatMessageGroupUserDao.page(params);
//  }

  private String buildMD5(String...userIds){
    Arrays.sort(userIds);
    return Md5Util.md5(StringUtils.join(userIds));
  }

  private Pair<String, String> findChatUserIds(UserArchive userArchive){
    TokenCacheObj token = TokenThreadLocal.getTokenObjNonNull();
    String tokenUserId = token.objId().toString();
    if(token instanceof ExpertToken){
      Assert.isTrue(tokenUserId.equals(expertDao.findUserIdById(userArchive.getExpertId())), ()->"无权限操作");
      return Pair.of(token.objId().toString(), userArchive.getUserId());
    }
    else{
      String expertUserId = userArchiveDao.findExpertUserId(userArchive.getId());
      Assert.hasText(expertUserId, ()->"未找到专家信息");
      if(token instanceof OperatorToken){
        //客服运营代替专家对话
        return Pair.of(expertUserId, userArchive.getUserId());
      }else{
        Assert.isTrue(tokenUserId.equals(userArchive.getUserId()), ()->"无权限操作");
        return Pair.of(token.objId().toString(), expertUserId);
      }
    }
  }

  public String findCurrentUserId(String groupId, BiConsumer<Boolean, String> isOperatorToken){
    TokenCacheObj token = TokenThreadLocal.getTokenObjNonNull();
    String userId = token.objId().toString();
    boolean operatorToken = false;
    if(token instanceof OperatorToken){
      operatorToken = true;
      userId = chatMessageGroupDao.findExpertUserIdByGroupId(groupId);
    }
    if(isOperatorToken != null){
      isOperatorToken.accept(operatorToken, userId);
    }
    return userId;
  }

  public ChatMessageGroupUser findOrCreateGroup(UserArchive userArchive){
    Assert.notNull(userArchive, ()->"请选择一个用户档案");
    Pair<String, String> userIds = findChatUserIds(userArchive);
    String userId = userIds.getFirst();
    String targetUserId = userIds.getSecond();
    Assert.isTrue(!userId.equals(targetUserId), ()->"发送者和接收者不能相同");
    Assert.isTrue(userBaseInfoDao.existsById(targetUserId), ()->"未找到接收消息的用户");
    String groupId = findOrCreateGroupId(userId, targetUserId, userArchive);
    ChatMessageGroupUser group = chatMessageGroupUserDao.findByUserIdAndGroupId(userId, groupId);
    Assert.notNull(group, ()->"系统繁忙，请稍后重试");
//    UserBaseInfo user = userBaseInfoDao.findById(group.getTargetUserId()).orElseThrow(Exceptions.of(()->"未找到收信人用户信息"));
    return group;
//                               .put("targetUserNickName", user.getUserNickName())
//      .put("targetUserRealName", user.getRealName()).put("targetUserAvatar", user.getAvatar())
//      .put("targetUserType", user.getUserType()).put("targetUserGender", user.getGender())
//      .put("targetUserBirth", user.getBirth())
//      .putPOJO("userArchive", userArchive);
//    if(user.getUserType() == UserType.USER.getCode()){
//      node.put("targetUserPhone", user.getPhone());
//    }
//    else if (user.getUserType() == UserType.EXPERT.getCode()){
//      Expert expert = expertDao.findByUserId(user.getId());
//      if(expert != null){
//        node.putPOJO("targetUserExpertInfo", expert);
//      }
//    }
//    return node;
  }
  public ObjectNode findOrCreateGroupAndUserArchive(String userArchiveId){
    Assert.hasText(userArchiveId, ()->"请选择一个用户档案");
    UserArchive userArchive = userArchiveDao.findById(userArchiveId).orElseThrow(Exceptions.of(() -> "未找到用户档案信息"));
    ChatMessageGroupUser group = findOrCreateGroup(userArchive);
    return JsonUtils.convert(group, ObjectNode.class).putPOJO("userArchive", userArchiveDao.findRowMapById(userArchiveId));
  }

  @Transactional(rollbackFor = Exception.class)
  public ChatMessage send(String groupId, String content){
    //    userBaseInfoDao.checkCompleteInfo(userId);
    Assert.hasText(groupId, "请选择接一个聊天会话");
    Assert.hasText(content, "请选择填写消息内容");
    String currentUserId = findCurrentUserId(groupId, null);
    Assert.hasText(currentUserId, ()->"未找到会话信息");
    ChatMessageGroupUser group = chatMessageGroupUserDao.findByUserIdAndGroupId(currentUserId, groupId);
    Assert.notNull(group, ()->"未找到聊天会话信息");
    ChatMessage message = new ChatMessage();
    message.setContent(content);
    message.setSendUserId(currentUserId);
    message.setCreateTime(new Date());
    message.setGroupId(groupId);
    ChatMessage m = doSend(message, groupId, currentUserId);
    afterSendEvent(m);
    return m;
  }

  private ChatMessage doSend(ChatMessage message, String groupId, String currentUserId){
    return TransactionActivation.start(()->{
      ChatMessage m = save(message);
      chatMessageGroupUserDao.sendMessageAfter(groupId, message.getId(), message.getCreateTime(), 1, currentUserId);
      //超过通知间隔时间，可发送未读通知短信
      //      sendSmsNotify(Collections.singleton(targetUserId), groupId);
      return m;
    });
  }

//  public int sendSmsNotify(Collection<String> userIds, String groupId){
//    List<RowMap> notifyList = chatMessageGroupUserDao.findNotifyList(
//      userIds, groupId, ChatNotifyConfig.getNotifyLimitTimeLine().toDate());
//    if(!notifyList.isEmpty()){
//      Set<String> ids = Sets.newHashSet();
//      for (RowMap row : notifyList) {
//        String phone = row.getStr("contactPhone");
//        if(StringUtils.isBlank(phone)){
//          phone = row.getStr("phone");
//        }
//        smsService.sendSmsAsync(phone, null, SmsTemplateId.ALiSMS_CHAT_NOTIFY,
//                                Map.of("name", row.get("targetName"), "appName", appName));
//        ids.add(row.getStr("id"));
//      }
//      TransactionActivation.start(()-> chatMessageGroupUserDao.updateNotifyTime(ids));
//    }
//    return notifyList.size();
//  }

//
//  @Transactional(rollbackFor = Exception.class)
//  public void deleteMessage(String userId, String msgIds){
//    if(StringUtils.isNoneBlank(userId, msgIds)){
//      Set<String> ids = SetsHelper.toStringSets(msgIds, ",");
//      if(!ids.isEmpty()){
//        for (String id : ids) {
//          ChatMessage message = findById(id);
//          if(message != null){
//            if(!message.getSendUserId().equals(userId)){
//              if(message.getSendUserId().startsWith(ChatMessageDao.SYSTEM_USER_ID_PRE)){
//                return;
//              }
//              throw new ForbiddenException();
//            }
//            deleteById(id);
//            ChatMessageGroupDao.clearLastMessageId(id);
//            //删除所有此消息的关联
//            ChatMessageGroupRefDao.deleteByMessageId(id);
//          }
//        }
//      }
//    }
//  }
//
  public void hideGroup(String userId, String groupId){
    if(StringUtils.isNoneBlank(userId, groupId)){
      TransactionActivation.start(()-> chatMessageGroupUserDao.hideGroup(userId, groupId));
    }
  }
//
  public void clearUnread(String userId, String groupId){
    if(StringUtils.isNoneBlank(userId, groupId)){
      TransactionActivation.start(()-> chatMessageGroupUserDao.clearUnread(userId, groupId));
    }
  }
//
//  @Transactional(rollbackFor = Exception.class)
//  public void clearAllUnread(String userId){
//    if(StringUtils.isNotBlank(userId)){
//      ChatMessageGroupDao.clearAllUnread(userId);
//    }
//  }
//
//  @Transactional(rollbackFor = Exception.class)
//  public int sendBatch(String userId, String content, String tagIds){
//    Assert.hasText(content, "请选择填写消息内容");
//    Cache cache = cacheManager.getCache(CacheName.CHAT_MESSAGE_BATCH_INTERVAL);
//    Long expire = cache.getExpire(userId, TimeUnit.SECONDS);
//    DateTime now = DateTime.now();
//    if(expire != null && expire > 0){
//      throw new IllegalArgumentException("请勿频繁操作，请在 " + now.plusSeconds(expire.intValue()).toString(
//        Dates.DATE_MINUTE_FORMATTER_PATTERN) + " 之后再操作");
//    }
//    Set<String> tagIdSet = SetsHelper.toStringSets(tagIds, ",");
//    String sql = "SELECT c.user_id FROM dm_user_register_tree r INNER JOIN dm_user_register_tree c "
//      + "ON c.tree_kind = r.tree_kind AND c.left_val > r.left_val AND c.right_val < r.right_val "
//      + (tagIdSet.isEmpty() ? "":"LEFT JOIN dm_user_tags_ref tgr ON tgr.target_id = c.user_id ")
//      + "WHERE r.user_id = :userId "+(tagIdSet.isEmpty() ?"":"AND tgr.tag_id IN (:tagIds)")+" GROUP BY c.user_id";
//    Query query = getEntityManager().createNativeQuery(sql).setParameter("userId", userId);
//    if(!tagIdSet.isEmpty()){
//      query.setParameter("tagIds", tagIdSet);
//    }
//    List<String> targetUserIds = query.getResultList();
//    if(!targetUserIds.isEmpty()){
//      ChatMessage message = new ChatMessage();
//      message.setContent(content);
//      message.setSendUserId(userId);
//      message.setCreateTime(now.toDate());
//      message = save(message);
//      for (String targetUserId : targetUserIds) {
//        Group group = findGroup(userId, targetUserId);
//        ChatMessageGroupRef ref = new ChatMessageGroupRef();
//        ref.setMessageId(message.getId());
//        ref.setGroupId(group.groupId);
//        ChatMessageGroupRefDao.save(ref);
//        group.sendLastMessageId(message.getId());
//        group.saveGroup();
//      }
//      cache.put(userId, 0);
//      cache.expire(userId, 1, TimeUnit.DAYS);
//    }
//    return targetUserIds.size();
//  }
//
//  /** 后台发送系统消息 */
//  @Transactional(rollbackFor = Exception.class)
//  public void systemSendBatch(String orgId, String content){
//    Assert.hasText(content, "请选择填写消息内容");
//    String systemId = ChatMessageDao.systemId(orgId);
//    ChatMessage message = new ChatMessage();
//    message.setContent(content);
//    message.setSendUserId(systemId);
//    message.setCreateTime(new Date());
//    message = save(message);
//    ChatMessageGroup group = ChatMessageGroupDao.findByUserIdAndTargetUserId(systemId, "");
//    if(group == null){
//      group = new ChatMessageGroup();
//      group.setUserId(systemId);
//      group.setGroupId(systemId);
//    }
//    group.setLastMessageId(message.getId());
//    group = ChatMessageGroupDao.save(group);
//    ChatMessageGroupRef ref = new ChatMessageGroupRef();
//    ref.setMessageId(message.getId());
//    ref.setGroupId(group.getGroupId());
//    ChatMessageGroupRefDao.save(ref);
//  }
//
//  /** 查询接收者和发送者的分组，没有就创建 */
  private String findOrCreateGroupId(String userId, String targetUserId, UserArchive userArchive){
    String groupId = chatMessageGroupDao.findIdByUserArchiveId(userArchive.getId());
//    String groupId = chatMessageGroupUserDao.findByUserIdAndTargetUserId(userId, targetUserId, userArchive.getId());
    AtomicBoolean create = new AtomicBoolean(false);
    if(groupId == null){
      try {
//        groupId = cacheLock.lock(buildMD5(userId, targetUserId, userArchive.getId()), ()->{
        groupId = cacheLock.lock(userArchive.getId(), ()->{
          String gId = chatMessageGroupDao.findIdByUserArchiveId(userArchive.getId());
//          String gId = chatMessageGroupUserDao.findByUserIdAndTargetUserId(userId, targetUserId, userArchive.getId());
          if(gId == null){
            gId = TransactionActivation.start(()->{
              ChatMessageGroup group = new ChatMessageGroup();
              group.setCreateTime(new Date());
              group.setUserArchiveId(userArchive.getId());
              group = chatMessageGroupDao.save(group);
              create.set(true);
              return group.getId();
            });
          }
          return gId;
        });
      } catch (Exception e) {
        throw new IllegalArgumentException("系统繁忙，请稍后重试", e);
      }
    }
    String gId = groupId;
    TransactionActivation.start(()->{
      chatMessageGroupUserDao.insert(idGenerator.generate().toString(), gId, userId,  userArchive.getId());
      chatMessageGroupUserDao.insert(idGenerator.generate().toString(), gId, targetUserId, userArchive.getId());
      if(create.get()){
        String expertUserId;
        //用户发送消息时
        if(userArchive.getUserId().equals(userId)){
          expertUserId = targetUserId;
        }else{
          expertUserId = expertDao.findUserIdById(userArchive.getExpertId());
        }
        createGroupEvent(gId, userId, expertUserId);
      }
      return null;
    });

    return groupId;
  }
//
//  private void checkGroupIdAuth(String userId, String groupId){
//    if(StringUtils.isNoneBlank(userId, groupId)){
//      if(!groupId.startsWith(ChatMessageDao.SYSTEM_USER_ID_PRE)){
//        ChatMessageGroup group = ChatMessageGroupDao.findByUserIdAndGroupId(userId, groupId);
//        if(group == null){
//          throw new ForbiddenException();
//        }
//      }
//    }
//  }
//
//
//  private class Group{
//    String groupId;
//    ChatMessageGroup userGroup;
//    ChatMessageGroup targetGroup;
//
//    public Group(String groupId, ChatMessageGroup userGroup, ChatMessageGroup targetGroup) {
//      this.groupId = groupId;
//      this.userGroup = userGroup;
//      this.targetGroup = targetGroup;
//    }
//    void sendLastMessageId(String messageId){
//      userGroup.setLastMessageId(messageId);
//      userGroup.setVisible(true);
//      targetGroup.setLastMessageId(messageId);
//      targetGroup.setVisible(true);
//      targetGroup.setUnread(targetGroup.getUnread() + 1);
//    }
//    void saveGroup(){
//      ChatMessageGroupDao.save(userGroup);
//      ChatMessageGroupDao.save(targetGroup);
//    }
//  }

  public void afterSendEvent(ChatMessage sentMessage){
    TokenCacheObj token = TokenThreadLocal.getTokenObjNonNull();
    String userId = token.objId().toString();
    if(UserToken.class.equals(token.getClass()) && sentMessage.getSendUserId().equals(userId)){
      List<SysAutoReply> list = sysAutoReplyDao.matchKeyword(sentMessage.getContent(), Collections.singleton(ChatAutoReplyType.AI.getVal()));
      if(!list.isEmpty()){
        SysAutoReply reply = list.getFirst();
        ChatMessage message = new ChatMessage();
        message.setGroupId(sentMessage.getGroupId());
        message.setSystemMsg(ChatAutoReplyType.AI.getVal());
        message.setUrl(reply.getUrl());
        message.setContent(reply.getContent());
        message.setCreateTime(new Date());
        doSend(message, sentMessage.getGroupId(), userId);
      }
    }
  }

  public void createGroupEvent(String groupId, String userId, String expertUserId){
    TokenCacheObj token = TokenThreadLocal.getTokenObjNonNull();
    if(token instanceof UserToken){
      List<SysAutoReply> list = sysAutoReplyDao.findByTypeInOrderBySortNoAscIdAsc(
        Sets.newHashSet(ChatAutoReplyType.NEW_USER_SYSTEM_GREETING.getVal(), ChatAutoReplyType.NEW_USER_EXPERT_GREETING.getVal()));
      for (SysAutoReply reply : list) {
        ChatMessage message = new ChatMessage();
        message.setGroupId(groupId);
        message.setUrl(reply.getUrl());
        message.setContent(reply.getContent());
        message.setCreateTime(new Date());
        if(reply.getType() == ChatAutoReplyType.NEW_USER_EXPERT_GREETING.getVal()){
          message.setSendUserId(expertUserId);
          message.setSystemMsg(ChatAutoReplyType.NEW_USER_EXPERT_GREETING.getVal());
        }else{
          message.setSystemMsg(ChatAutoReplyType.NEW_USER_SYSTEM_GREETING.getVal());
        }
        doSend(message, groupId, "");
      }

    }
  }

  public ChatMessageService(
    //@Value("${base.wechat.miniapp.name}") String appName,
    ChatMessageGroupUserDao chatMessageGroupUserDao, UserBaseInfoDao userBaseInfoDao,
    CacheLock cacheLock, ExpertDao expertDao, ChatMessageGroupDao chatMessageGroupDao,
    UserArchiveDao userArchiveDao, IdGenerator idGenerator, SysAutoReplyDao sysAutoReplyDao) {
//    this.appName = appName;
    this.chatMessageGroupUserDao = chatMessageGroupUserDao;
    this.userBaseInfoDao = userBaseInfoDao;
    this.cacheLock = cacheLock;
    this.expertDao = expertDao;
    this.chatMessageGroupDao = chatMessageGroupDao;
//    this.smsService = smsService;
    this.userArchiveDao = userArchiveDao;
    this.idGenerator = idGenerator;
    this.sysAutoReplyDao = sysAutoReplyDao;
  }

}