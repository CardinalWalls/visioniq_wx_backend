package com.wk.vpac.database.dao.chat;

import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.wk.vpac.domain.chat.ChatMessageGroupUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

/**
 * ChatMessageGroupRef DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2021-07-08
 */
@Repository
public interface ChatMessageGroupUserDao extends GenericJpaDao<ChatMessageGroupUser, String> {

//  default DataPage<Map<String, Object>> page(Map<String, String> params){
//    String userId = params.get("userId");
//    if(StringUtils.isBlank(userId)){
//      return DataPage.getEmpty();
//    }
//    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder
//      .create("wk_chat_message_group_user", "t")
//      .joinTable("LEFT JOIN wk_chat_message m ON m.id = t.last_message_id", "m")
//      .joinTable("LEFT JOIN base_user_base_info tu ON tu.id = t.target_user_id", "tu")
//      .joinTable("LEFT JOIN wk_expert e ON tu.id = e.user_id", "e", "tu")
//      .select(JpaHelper.findColumns(ChatMessageGroupUser.class, "t") + ", m.content lastMessage, "
//                + "tu.user_nick_name targetUserNickName, tu.real_name targetUserRealName, tu.avatar targetUserAvatar, "
//                + "tu.user_type targetUserType, tu.gender targetUserGender, tu.birth targetUserBirth, "
//                + "CASE WHEN tu.user_type = 1 THEN tu.phone ELSE '' END targetUserPhone, e.level expertLevel, "
//                + "e.title expertTitle, e.job_position expertJobPosition, e.hospital expertHospital, e.department expertDepartment");
//    sqlBuilder.addWhereEq("t", "user_id", userId);
//    sqlBuilder.addWhereLike("tu", "user_nick_name", params.get("targetUserNickName"));
//    sqlBuilder.addWhereLike("tu", "real_name", params.get("targetUserRealName"));
//    sqlBuilder.addWhereEq("tu", "phone", params.get("targetUserPhone"));
//    sqlBuilder.addWhereEq("t", "visible", ConvertUtil.convert(params.get("visible"), Boolean.class));
//    return sqlBuilder.orderBy("t.last_message_time DESC").toPage(getEntityManager()).query(params);
//  }

//  @Query(value = "SELECT gu.group_id FROM wk_chat_message_group_user gu INNER JOIN wk_chat_message_group g ON g.id = gu.group_id "
//    + "WHERE gu.user_id=:userId AND gu.target_user_id=:targetUserId AND g.user_archive_id = :userArchiveId", nativeQuery = true)
//  String findByUserIdAndTargetUserId(@Param("userId") String userId, @Param("targetUserId") String targetUserId,
//                                     @Param("userArchiveId") String userArchiveId);

  ChatMessageGroupUser findByUserIdAndGroupId(@Param("userId") String userId, @Param("groupId") String groupId);

  @Modifying
  @Query(value = "UPDATE wk_chat_message_group_user "
    + "SET unread = (CASE WHEN user_id = :clearUnreadUserId THEN 0 ELSE unread + :unreadAdd END), visible=true, "
    + "last_message_id = :lastMessageId, last_message_time = :lastMessageTime, notified = false WHERE group_id = :groupId", nativeQuery = true)
  int sendMessageAfter(@Param("groupId") String groupId, @Param("lastMessageId") String lastMessageId,
                       @Param("lastMessageTime") Date lastMessageTime, @Param("unreadAdd") int unreadAdd,
                       @Param("clearUnreadUserId") String clearUnreadUserId);
  @Modifying
  @Query(value = "UPDATE wk_chat_message_group_user SET unread = 0 WHERE user_id = :userId AND group_id = :groupId", nativeQuery = true)
  int clearUnread(@Param("userId") String userId, @Param("groupId") String groupId);

  @Modifying
  @Query(value = "INSERT INTO wk_chat_message_group_user (id, group_id, user_id, last_message_time, notify_time, notified) "
    + "SELECT :id, :groupId, :userId, NOW(), NOW(), 1 FROM DUAL WHERE NOT EXISTS "
    + "(SELECT 1 FROM wk_chat_message_group_user gu INNER JOIN wk_chat_message_group g ON g.id = gu.group_id "
    + "WHERE gu.user_id = :userId AND g.user_archive_id = :userArchiveId)", nativeQuery = true)
  int insert(@Param("id") String id, @Param("groupId") String groupId, @Param("userId") String userId,
             @Param("userArchiveId") String userArchiveId);

  @Modifying
  @Query(value = "UPDATE wk_chat_message_group_user SET unread = 0, visible = false WHERE user_id =:userId AND group_id = :groupId", nativeQuery = true)
  int hideGroup(@Param("userId") String userId, @Param("groupId") String groupId);


//  default List<RowMap> findNotifyList(Collection<String> userIds, String groupId, Date notifyLimitTimeLine){
//    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder
//      .create("wk_chat_message_group_user", "t")
//      .joinTable("LEFT JOIN base_user_base_info u ON u.id = t.user_id", "u")
//      .joinTable("LEFT JOIN base_user_base_info tu ON tu.id = t.target_user_id", "tu")
//      .select("t.id, u.phone, u.contact_phone contactPhone, CASE WHEN tu.real_name = '' THEN tu.user_nick_name ELSE tu.real_name END targetName")
//      .addWhereSql("AND t.notified = false AND t.unread > 0")
//      .addWhereIn("t", "user_id", userIds)
//      .addWhereEq("t", "group_id", groupId)
//      .addWhereLe("t", "notify_time", notifyLimitTimeLine);
//    return toRowResult(sqlBuilder.build(getEntityManager())).getResultList();
//  }

  @Modifying
  @Query(value = "UPDATE wk_chat_message_group_user SET notify_time = NOW(), notified = true WHERE id IN :ids", nativeQuery = true)
  int updateNotifyTime(@Param("ids") Collection<String> ids);
}

