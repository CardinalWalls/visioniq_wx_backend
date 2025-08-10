package com.wk.vpac.database.dao.chat;

import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.wk.vpac.domain.chat.ChatMessageGroup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * ChatMessageGroup DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2021-07-08
 */
@Repository
public interface ChatMessageGroupDao extends GenericJpaDao<ChatMessageGroup, String> {

  default void deleteGroup(String userArchiveId){
    String id = findIdByUserArchiveId(userArchiveId);
    if(StringUtils.isNotBlank(id)){
      deleteById(id);
      getEntityManager().createNativeQuery("DELETE FROM wk_chat_message WHERE group_id = :id ; "
                                             + "DELETE FROM wk_chat_message_group_user WHERE group_id = :id ; ")
        .setParameter("id", id).executeUpdate();
    }
  }

  @Query(value = "SELECT id FROM wk_chat_message_group WHERE user_archive_id = :userArchiveId", nativeQuery = true)
  String findIdByUserArchiveId(@Param("userArchiveId") String userArchiveId);

  @Query(value = "SELECT e.user_id FROM wk_chat_message_group g INNER JOIN wk_user_archive a ON a.id = g.user_archive_id "
    + "INNER JOIN wk_expert e ON e.id = a.expert_id WHERE g.id = :groupId", nativeQuery = true)
  String findExpertUserIdByGroupId(@Param("groupId") String groupId);

//  @Query(value = "SELECT a.user_id FROM wk_chat_message_group g INNER JOIN wk_user_archive a "
//    + "ON a.id = g.user_archive_id WHERE g.id = :groupId", nativeQuery = true)
//  String findUserIdByGroupId(@Param("groupId") String groupId);
//
//  @Query(value = "SELECT a.user_id userId, e.user_id expertUserId FROM wk_chat_message_group g INNER JOIN wk_user_archive a "
//    + "ON a.id = g.user_archive_id INNER JOIN wk_expert e ON e.id = a.expert_id WHERE g.id = :groupId", nativeQuery = true)
//  RowMap findUserIdsByGroupId(@Param("groupId") String groupId);
}

