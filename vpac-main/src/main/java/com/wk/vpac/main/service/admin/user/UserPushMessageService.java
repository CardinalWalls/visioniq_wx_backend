package com.wk.vpac.main.service.admin.user;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.exception.Exceptions;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.MapUtil;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import com.base.components.transaction.TransactionActivation;
import com.wk.vpac.common.constants.sys.ChatAutoReplyType;
import com.wk.vpac.database.constants.condition.ConditionField;
import com.wk.vpac.database.constants.condition.SpecialSql;
import com.wk.vpac.database.dao.chat.ChatMessageDao;
import com.wk.vpac.database.dao.user.UserPushMessageDao;
import com.wk.vpac.database.dao.user.UserPushMessageTypeDao;
import com.wk.vpac.domain.chat.ChatMessage;
import com.wk.vpac.domain.user.UserArchive;
import com.wk.vpac.domain.user.UserPushMessage;
import com.wk.vpac.domain.user.UserPushMessageType;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.Arrays;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * UserPushMessage Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class UserPushMessageService extends AbstractJpaService<UserPushMessage, String, UserPushMessageDao> {
  public static final Map<String, ConditionField> COLUMN_MAP = MapUtil.linked(
    "risk_level", new ConditionField("风险等级", "risk_level", ConditionField.Type.RISK_LEVEL),
    "gender", new ConditionField("性别", "gender", ConditionField.Type.GENDER),
    "birth", new ConditionField("年龄", "birth", ConditionField.Type.INT, (i, op, val, alias)->
      new SpecialSql().setSql("YEAR(FROM_DAYS(DATEDIFF(NOW(), "+alias+".birth))) " + op + " (:_birth__" +i+")")
                      .addParam("_birth__" + i, val)),
    "region", new ConditionField("地区", "region", ConditionField.Type.REGION, (i, op, val, alias)->{
      String v = val.toString();
      if(StringUtils.isNotBlank(v)){
        String[] arr = StringUtils.split(v, "-");
        if(!Arrays.isNullOrEmpty(arr)){
          return new SpecialSql()
            .setSql("EXISTS (SELECT 1 FROM base_region r0 INNER JOIN base_region r1 ON r1.left_val >= r0.left_val "
                      + "AND r1.right_val <= r0.right_val WHERE r0.id = :_region__"+i+" AND "+alias+".region_id = r1.id)")
            .addParam("_region__" + i, arr[arr.length - 1]);
        }
      }
      return null;
    }),
    "expertPhone", new ConditionField("专家手机", "expertPhone", ConditionField.Type.TEXT, (i, op, val, alias)->
      new SpecialSql()
        .setSql("EXISTS (SELECT 1 FROM base_user_base_info eu__"+i+" INNER JOIN wk_expert e__"+i+" ON eu__"+i+".id = e__"+i+".user_id "
                  + "WHERE e__"+i+".id = "+alias+".expert_id AND eu__"+i+".phone "+op+" (:expertPhone__"+i+") )")
        .addParam("expertPhone__" + i, val)
    )
  );
  private final UserPushMessageTypeDao userPushMessageTypeDao;
  private final ChatMessageDao chatMessageDao;

  public UserPushMessageService(UserPushMessageTypeDao userPushMessageTypeDao, ChatMessageDao chatMessageDao) {
    this.userPushMessageTypeDao = userPushMessageTypeDao;
    this.chatMessageDao = chatMessageDao;
  }

  public DataPage<RowMap> userArchivePage(PageParamMap params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_user_archive", "a");
    sqlBuilder.joinTable("LEFT JOIN wk_expert e ON e.id = a.expert_id", "e");
    sqlBuilder.joinTable("LEFT JOIN base_user_base_info eu ON eu.id = e.user_id", "eu", "e");
    sqlBuilder.joinTable("LEFT JOIN base_user_base_info u ON u.id = a.user_id", "u");
    sqlBuilder.select(findColumns(UserArchive.class, "a")
                        + ", eu.real_name expertName, eu.phone expertPhone, u.phone parentPhone");
    sqlBuilder.addWhereLeEq("a", "create_time", params.getStrTrim("publishTime"));
    String conditionJson = params.getStrTrim("conditionJson");
    if (StringUtils.isNotBlank(conditionJson)) {
      ConditionField.build(sqlBuilder, COLUMN_MAP, conditionJson, "a");
    }
    sqlBuilder.orderBy("a.id DESC");
    return sqlBuilder.toRowPage(getEntityManager()).query(params);
  }
  public DataPage<RowMap> page(PageParamMap params){
    return getDao().page(params);
  }

  public UserPushMessage save(ParamMap params){
    String id = params.getStrTrimOrEmpty("id");
    params.hasText("content", ()->"请填写内容");
    Date now = new Date();
    UserPushMessage msg;
    if(StringUtils.isBlank(id)){
      msg = params.populate(new UserPushMessage(), false, "id");
      msg.setCreateTime(now);
    }else{
      UserPushMessage exists = getDao().findById(id).orElseThrow(Exceptions.of(()->"未找到数据"));
      Assert.isTrue(StringUtils.isBlank(exists.getPublishTime()), ()->"此消息已发布，不能再修改");
      msg = params.populate(exists, false, "id");
    }
    msg.setUpdateTime(now);
    ConditionField.build(DynamicSqlBuilder.create("wk_user_archive", "a"), COLUMN_MAP, msg.getConditionJson(), "a");
    return TransactionActivation.of(()->saveAndFlush(msg)).start();
  }

  public int publish(String id){
    UserPushMessage userPushMessage = getDao().findById(id).orElseThrow(Exceptions.of(() -> "未找到数据"));
    Assert.isTrue(StringUtils.isBlank(userPushMessage.getPublishTime()), ()->"此消息已发布，请勿重复操作");
    String typeName = userPushMessageTypeDao.findById(userPushMessage.getTypeId()).map(UserPushMessageType::getName).orElse("");
    DateTime now = DateTime.now();
    Date nowDate = now.toDate();
    return TransactionActivation.start(()->{
//      DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_user_archive", "a")
//                                                      .select("a.id, a.user_id userId");
//      ConditionField.build(sqlBuilder, COLUMN_MAP, userPushMessage.getConditionJson(), "a");
//      List<RowMap> archiveList = toRowResult(sqlBuilder.build(getEntityManager())).getResultList();
//      if(!archiveList.isEmpty()){
//        for (RowMap archive : archiveList) {
//          String archiveId = archive.getStr("id");
//          String groupId = idGenerator.generate().toString();
//          chatMessageGroupDao.insert(groupId, archiveId);
//          String groupUserId = idGenerator.generate().toString();
//          chatMessageGroupUserDao.insert(groupUserId, groupId, archive.getStr("userId"), archiveId);
//
//        }
//      }
      //创建会话组
      DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_user_archive", "a")
                                                      .select("UUID_SHORT(), :nowDate, a.id");
      sqlBuilder.addWhereSql("AND NOT EXISTS (SELECT 1 FROM wk_chat_message_group g WHERE g.user_archive_id = a.id)");
      ConditionField.build(sqlBuilder, COLUMN_MAP, userPushMessage.getConditionJson(), "a");
      Query nativeQuery = getEntityManager().createNativeQuery(
        "INSERT INTO wk_chat_message_group " + sqlBuilder.concatSql());
      for (Map.Entry<String, Object> entry : sqlBuilder.getParamMap().entrySet()) {
        nativeQuery.setParameter(entry.getKey(), entry.getValue());
      }
      nativeQuery.setParameter("nowDate", nowDate).executeUpdate();
      //创建会话用户
      getEntityManager().createNativeQuery(
        "INSERT INTO wk_chat_message_group_user "
          + "SELECT UUID_SHORT(), g.id, a.user_id, 0, true, '', NOW(), NOW(), true FROM wk_chat_message_group g "
          + "INNER JOIN wk_user_archive a ON a.id = g.user_archive_id WHERE NOT EXISTS "
          + "(SELECT 1 FROM wk_chat_message_group_user gu WHERE gu.group_id = g.id AND gu.user_id = a.user_id)")
                        .executeUpdate();
      sqlBuilder = DynamicSqlBuilder.create("wk_user_archive", "a")
        .joinTable("INNER JOIN wk_chat_message_group g ON g.user_archive_id = a.id", "g")
        .select("a.user_id userId, g.id");
      ConditionField.build(sqlBuilder, COLUMN_MAP, userPushMessage.getConditionJson(), "a");
      List<RowMap> groups = toRowResult(sqlBuilder.build(getEntityManager())).getResultList();
      StringBuilder updateSql = new StringBuilder();
      for (RowMap group : groups) {
        String groupId = group.getStr("id");
        ChatMessage message = new ChatMessage();
        message.setGroupId(groupId);
        message.setSystemMsg(ChatAutoReplyType.SYSTEM_PUSH.getVal());
        message.setUrl(userPushMessage.getUrl());
        message.setContent(userPushMessage.getContent());
        message.setCreateTime(nowDate);
        message.setSystemMsgTypeName(typeName);
        message = chatMessageDao.save(message);
        updateSql.append("UPDATE wk_chat_message_group_user SET last_message_id='").append(message.getId())
                 .append("', last_message_time=:nowDate, unread=unread+1, visible=true WHERE group_id='")
                 .append(groupId).append("' AND user_id='").append(group.getStr("userId")).append("'; \n");
      }
      if(!updateSql.isEmpty()){
        getEntityManager().createNativeQuery(updateSql.toString()).setParameter("nowDate", nowDate).executeUpdate();
      }
      userPushMessage.setPublishTime(now.toString(Dates.DATE_TIME_FORMATTER));
      userPushMessage.setPublishCount(groups.size());
      userPushMessage.setOperatorName(TokenThreadLocal.getTokenObjNonNull().objName());
      saveAndFlush(userPushMessage);
      return groups.size();
    });
  }
}