

package com.wk.vpac.database.dao.sys;

import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.page.DataPage;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.wk.vpac.domain.sys.SysMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * SysMessage DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-03-15
 */
@Repository
public interface SysMessageDao extends GenericJpaDao<SysMessage, String> {

  /**
   * 设置所有消息已读
   *
   * @param userId
   */
  @Query(value = "update base_sys_message set status=1,know_time=sysdate() where user_id=:userId and status=0 ", nativeQuery = true)
  @Modifying
  void updateMessageReadStatus(@Param("userId") String userId);

  @Query(value = "select count(1) from base_sys_message where status = :status", nativeQuery = true)
  long countByUnread(@Param("status") int status);

  /**
   * 设置消息删除标记
   *
   * @param ids
   * @param delFlag
   */
  @Query(value = "update base_sys_message set del_flag=:delFlag where user_id= :userId and id in (:ids)", nativeQuery = true)
  @Modifying
  void updateDelFlag(@Param("userId") String userId, @Param("ids") List<String> ids, @Param("delFlag") Integer delFlag);

  /**
   * 设置全部消息删除标记
   *
   * @param delFlag
   */
  @Query(value = "update base_sys_message set del_flag=:delFlag where user_id= :userId", nativeQuery = true)
  @Modifying
  void updateDelFlag(@Param("userId") String userId, @Param("delFlag") Integer delFlag);

  default DataPage<Map<String,Object>> findDetailPage(Map<String,String> searchParam){
    int pageNum = Pages.Helper.pageNum(searchParam.get("pageNum"));
    int pageSize = Pages.Helper.pageSize(searchParam.get("pageSize"));
    String userNickName = searchParam.get("userNickName");
    String phone = searchParam.get("phone");
    String status = searchParam.get("status");
    String userTypeId = searchParam.get("userTypeId");
    String content = searchParam.get("content");
    String sql = "SELECT me.id, me.content, me.create_time createTime, me.know_time knowTime, me.`status`,me.del_flag delFlag, ui.user_nick_name userNickName, ui.phone, ut.user_type_name userTypeName FROM base_sys_message me "
      + "LEFT JOIN base_user_base_info ui ON ui.id = me.user_id "
      + "LEFT JOIN base_user_base_type ut ON ut.id = ui.user_type_id WHERE 1=1 ";
    String countSql = "SELECT COUNT(1) FROM base_sys_message me "
      + "LEFT JOIN base_user_base_info ui ON ui.id = me.user_id "
      + "LEFT JOIN base_user_base_type ut ON ut.id = ui.user_type_id WHERE 1=1 ";
    NativeSQLBuilder builder = new NativeSQLBuilder();
    if(StringUtils.isNotBlank(userNickName)){
      builder.and().addWhere("ui", "user_nick_name", userNickName, ConditionEnum.OPERATE_RIGHT_LIKE);
    }
    if(StringUtils.isNotBlank(phone)){
      builder.and().addWhere("ui", "phone", phone, ConditionEnum.OPERATE_RIGHT_LIKE);
    }
    if(StringUtils.isNotBlank(status)){
      builder.and().addWhere("me", "status", status, ConditionEnum.OPERATE_EQUAL);
    }
    if(StringUtils.isNotBlank(userTypeId)){
      builder.and().addWhere("ui", "user_type_id", userTypeId, ConditionEnum.OPERATE_EQUAL);
    }
    if(StringUtils.isNotBlank(content)){
      builder.and().addWhere("me", "content", content, ConditionEnum.OPERATE_RIGHT_LIKE);
    }
    builder.bindQuerySql(sql).orderBy("me.create_time desc");
    jakarta.persistence.Query listQuery = setMapResult(builder.build(getEntityManager()));
    builder.bindQuerySql(countSql).orderBy("");
    jakarta.persistence.Query countQuery = builder.build(getEntityManager());
    return pageByQuery(countQuery, listQuery, pageNum, pageSize);
  }
}

