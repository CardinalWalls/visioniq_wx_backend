package com.wk.vpac.database.dao.user;

import com.base.components.common.constants.sys.PageSort;
import com.base.components.common.constants.sys.Pages;
import com.base.components.common.constants.sys.SortBuilder;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.util.SetsHelper;
import com.base.components.common.util.SqlLikeHelper;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import com.wk.vpac.domain.user.UserArchive;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * UserArchive DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Repository
public interface UserArchiveDao extends GenericJpaDao<UserArchive, String> {

  default RowMap findRowMapById(String id){
    PageParamMap params = new PageParamMap();
    params.put("id", id);
    List<RowMap> list = page(params).getList();
    return list.isEmpty() ? null : list.get(0);
  }

  default DataPage<RowMap> page(PageParamMap params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_user_archive", "t");
    sqlBuilder.joinTable("LEFT JOIN wk_expert e ON e.id = t.expert_id", "e");
    sqlBuilder.joinTable("LEFT JOIN base_user_base_info eu ON eu.id = e.user_id", "eu", "e");
    sqlBuilder.joinTable("LEFT JOIN base_user_base_info u ON u.id = t.user_id", "u");
    String select = findColumns(UserArchive.class, "t")
      + ", eu.real_name expertName, eu.avatar expertAvatar, e.title expertTitle, e.proficient expertProficient, "
      + "e.job_position expertJobPosition, e.hospital expertHospital, u.phone parentPhone, eu.id expertUserId";
    if(Objects.equals(true, params.getBoolean("admin"))){
      select += ", eu.phone expertPhone";
    }
    String sortStr = params.getStrTrim(Pages.SORT_VAR_NAME);
    PageSort pageSort = SortBuilder.by("createTime, updateTime", "createTime DESC").build(params);
    String orderBy = pageSort.toCamelCase();
    String currentUserId = params.getStrTrim("currentUserId");
    if (StringUtils.isNotBlank(currentUserId)) {
      select += ",mgu.unread, m.content lastMessage, m.create_time lastMessageTime, mgu.group_id chatGroupId";
      orderBy = (StringUtils.isBlank(sortStr) ? "mgu.unread DESC, m.id DESC, " : "") + orderBy;
      sqlBuilder.setParameter("currentUserId", currentUserId, "mgu");
      sqlBuilder.joinTable("LEFT JOIN wk_chat_message_group mg ON mg.user_archive_id = t.id", "mg");
      sqlBuilder.joinTable("LEFT JOIN wk_chat_message_group_user mgu ON mgu.group_id = mg.id AND mgu.user_id = :currentUserId", "mgu", "mg");
      sqlBuilder.joinTable("LEFT JOIN wk_chat_message m ON mgu.last_message_id = m.id", "m", "mgu");
      sqlBuilder.addNecessary("mgu");
    }
    sqlBuilder.select(select);
    String id = params.getStrTrim("id");
    sqlBuilder.addWhereEq("t", "id", id);
    sqlBuilder.addWhereEq("t", "risk_level", params.getInteger("riskLevel"));
    sqlBuilder.addWhereEq("t", "user_id", params.get("userId"));
    sqlBuilder.addWhereEq("t", "expert_id", params.get("expertId"));
    sqlBuilder.addWhereLike("t", "name", params.get("name"));
    sqlBuilder.addWhereEq("t", "gender", params.getInteger("gender"));
    sqlBuilder.addWhereEq("t", "idcard", params.get("idcard"));
    sqlBuilder.addWhereEq("u", "phone", params.get("userPhone"), "userPhone");
    sqlBuilder.addWhereEq("eu", "phone", params.get("expertPhone"), "expertPhone");
    String searchKey = params.getStr("searchKey");
    if (StringUtils.isNotBlank(searchKey)) {
      sqlBuilder.addWhere("AND (t.name LIKE :searchKey OR t.idcard = :searchKey OR u.phone = :searchKey)",
                          "searchKey", SqlLikeHelper.leftAndRight(searchKey), "u");
    }
    return sqlBuilder.orderBy(orderBy).toRowPage(
      getEntityManager(), c->c.setNonPage(StringUtils.isNotBlank(id))).query(params);
  }

  default DataPage<RowMap> pageForOperator(PageParamMap params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_user_archive", "t");
    sqlBuilder.joinTable("LEFT JOIN wk_expert e ON e.id = t.expert_id", "e");
    sqlBuilder.joinTable("LEFT JOIN base_user_base_info eu ON eu.id = e.user_id", "eu", "e");
    sqlBuilder.joinTable("LEFT JOIN base_user_base_info u ON u.id = t.user_id", "u");
    String select = findColumns(UserArchive.class, "t")
      + ", eu.real_name expertName, eu.avatar expertAvatar, e.title expertTitle, e.job_position expertJobPosition, "
      + "e.hospital expertHospital, u.phone parentPhone, eu.id expertUserId, e.proficient expertProficient, "
      + "mgu.unread, m.content lastMessage, m.create_time lastMessageTime, mgu.group_id chatGroupId";
    String orderBy = "mgu.unread DESC, m.id DESC, t.update_time DESC";
    sqlBuilder.joinTable("LEFT JOIN wk_chat_message_group mg ON mg.user_archive_id = t.id", "mg");
    sqlBuilder.joinTable("LEFT JOIN wk_chat_message_group_user mgu ON mgu.group_id = mg.id AND mgu.user_id = e.user_id", "mgu", "mg", "e");
    sqlBuilder.joinTable("LEFT JOIN wk_chat_message m ON mgu.last_message_id = m.id", "m", "mgu");
    sqlBuilder.addNecessary("mgu");
    sqlBuilder.select(select);
    String id = params.getStrTrim("id");
    sqlBuilder.addWhereEq("t", "id", id);
    sqlBuilder.addWhereEq("t", "risk_level", params.getInteger("riskLevel"));
    sqlBuilder.addWhereEq("t", "user_id", params.get("userId"));
    Set<String> expertIds = SetsHelper.toStringSets(params.getStrTrim("expertIds"), ",");
    if (CollectionUtils.isNotEmpty(expertIds)) {
      sqlBuilder.addWhereIn("t", "expert_id", expertIds, "expertIds");
    }
    Set<String> regionIds = SetsHelper.toStringSets(params.getStrTrim("regionIds"), ",");
    if (CollectionUtils.isNotEmpty(regionIds)) {
      sqlBuilder.addWhereIn("t", "region_id", regionIds, "regionIds");
    }
    sqlBuilder.addWhereEq("t", "expert_id", params.get("expertId"));
    sqlBuilder.addWhereLike("t", "name", params.get("name"));
    sqlBuilder.addWhereEq("t", "gender", params.getInteger("gender"));
    sqlBuilder.addWhereEq("t", "idcard", params.get("idcard"));
    sqlBuilder.addWhereEq("u", "phone", params.get("userPhone"), "userPhone");
    sqlBuilder.addWhereEq("eu", "phone", params.get("expertPhone"), "expertPhone");
    String searchKey = params.getStr("searchKey");
    if (StringUtils.isNotBlank(searchKey)) {
      sqlBuilder.addWhere("AND (t.name LIKE :searchKey OR t.idcard = :searchKey OR u.phone = :searchKey)",
                          "searchKey", SqlLikeHelper.leftAndRight(searchKey), "u");
    }
    return sqlBuilder.orderBy(orderBy).toRowPage(
      getEntityManager(), c->c.setNonPage(StringUtils.isNotBlank(id))).query(params);
  }

  List<UserArchive> findByUserIdAndIdcardOrderByIdDesc(String userId, String idcard);
  List<UserArchive> findByExpertIdAndIdcardOrderByIdDesc(String expertId, String idcard);
  boolean existsByUserIdAndId(String userId, String id);
  boolean existsByExpertIdAndId(String expertId, String id);

  @Query(value = "SELECT e.user_id FROM wk_user_archive a INNER JOIN wk_expert e ON e.id = a.expert_id WHERE a.id = :id", nativeQuery = true)
  String findExpertUserId(@Param("id") String id);


  default RowMap statistics(ParamMap params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_user_archive", "t");
    sqlBuilder.select("COUNT(id) total, SUM(CASE WHEN gender = 1 THEN 1 ELSE 0 END) male,"
                        + "SUM(CASE WHEN gender = 2 THEN 1 ELSE 0 END) female");
    sqlBuilder.addWhereEq("t", "expert_id", params.get("expertId"));
    sqlBuilder.addWhereEq("t", "user_id", params.get("userId"));
    return toRowResult(sqlBuilder.build(getEntityManager())).getSingleResult();
  }

  @Modifying
  @Query(value = "UPDATE wk_user_archive SET expert_id = :expertId WHERE user_id = :userId", nativeQuery = true)
  int updateExpertId(@Param("userId") String userId, @Param("expertId") String expertId);
}

