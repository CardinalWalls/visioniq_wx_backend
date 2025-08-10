

package com.wk.vpac.database.dao.user;

import com.base.components.common.constants.Valid;
import com.base.components.common.constants.sys.Dates;
import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import com.base.components.database.jpa.util.JpaHelper;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.base.components.database.jpa.util.SelectPageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wk.vpac.common.constants.UserType;
import com.wk.vpac.domain.user.UserBaseInfo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * UserBaseInfo DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-03-15
 */
@Repository
public interface UserBaseInfoDao extends GenericJpaDao<UserBaseInfo, String> {
  default DataPage page(Map<String, String> params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("base_user_base_info", "t");
    sqlBuilder.select(findColumns(UserBaseInfo.class, "t", "pwd")
                        + ",c.name communityName, c.street_name streetName, eu.phone expertPhone, "
                        + "CASE WHEN eu.real_name = '' THEN eu.user_nick_name ELSE eu.real_name END expertName, "
                        + "e.hospital expertHospital, e.department expertDepartment");
    sqlBuilder.joinTable("LEFT JOIN wk_community c ON c.id = t.community_id", "c");
    sqlBuilder.joinTable("LEFT JOIN wk_expert e ON e.id = t.expert_id AND t.user_type = "+ UserType.USER.getCode(), "e");
    sqlBuilder.joinTable("LEFT JOIN base_user_base_info eu ON eu.id = e.user_id", "eu", "e");
    sqlBuilder.addWhereEq("t", "phone", params.get("phone"));
    sqlBuilder.addWhereEq("t", "id_card", params.get("idCard"));
    sqlBuilder.addWhereEq("t", "user_type", ConvertUtil.convert(params.get("userType"), Integer.class));
    sqlBuilder.addWhereEq("t", "user_type_id", params.get("userTypeId"));
    sqlBuilder.addWhereLike("t", "user_nick_name", params.get("userNickName"));
    sqlBuilder.addWhereLike("c", "name", params.get("communityName"), "communityName");
    sqlBuilder.addWhereLike("c", "street_name", params.get("streetName"), "streetName");
    String expertName = StringUtils.trimToEmpty(params.get("expertName"));
    if (StringUtils.isNotBlank(expertName)) {
      sqlBuilder.addWhere("AND (eu.real_name LIKE :expertName OR eu.user_nick_name LIKE :expertName)", "expertName", expertName, "eu");
    }
    sqlBuilder.addWhereLike("eu", "phone", StringUtils.trimToEmpty(params.get("expertPhone")), "expertPhone");
    return sqlBuilder.orderBy("t.regist_time DESC").toPage(getEntityManager()).queryUnlimited(params);
  }

  @Modifying
  @Query(value = "update base_user_base_info set `level` = :updateLevel where id = :userId", nativeQuery = true)
  int updateLevel(@Param("userId") String userId, @Param("updateLevel") int updateLevel);

  @Modifying
  @Query(value = "update base_user_base_info set parent_user_id = :parentUserId where id = :userId", nativeQuery = true)
  int updateParentUserId(@Param("userId") String userId, @Param("parentUserId") String parentUserId);

  UserBaseInfo findByPhone(String phone);

  @Query(value = "select phone from base_user_base_info where id = :id", nativeQuery = true)
  String findPhoneById(@Param("id") String id);

  @Query(value = "update base_user_base_info set user_type = :userType where user_type_id = :typeId ", nativeQuery = true)
  @Modifying
  void updateUserType(@Param("typeId") String typeId, @Param("userType") Integer userType);

  @Query(value = "update base_user_base_info set user_type = :userType, user_type_id = :userTypeId, update_time=NOW() where id=:id ", nativeQuery = true)
  @Modifying
  void updateUserType(@Param("id") String id, @Param("userType") Integer userType, @Param("userTypeId") String userTypeId);

  @Query(value = "update base_user_base_info set user_type = :userType, user_type_id = :unknownId where user_type_id = "
    + ":id ", nativeQuery = true)
  @Modifying
  void updateUnknown(@Param("id") String id, @Param("unknownId") String unknownId, @Param("userType") Integer userType);

  @Query(value = " update base_user_base_info set wx_name='',wx_img='',mp_open_id='' where mp_open_id=:mpOpenId and phone!=:phone ", nativeQuery = true)
  @Modifying
  void clearMpOtherBind(@Param("mpOpenId") String mpOpenId, @Param("phone") String phone);

  @Query(value = " update base_user_base_info set mini_open_id='' where mini_open_id=:miniOpenId and phone!=:phone ", nativeQuery = true)
  @Modifying
  void clearOtherMiniBind(@Param("miniOpenId") String miniOpenId, @Param("phone") String phone);

  @Query(value = "update base_user_base_info set wx_name='',wx_img='',mp_open_id='' where id=:userId ", nativeQuery = true)
  @Modifying
  void clearMpBind(@Param("userId") String userId);

  UserBaseInfo findByAccountOrPhone(String account, String phone);

  @Query("select user from UserBaseInfo user where (user.account=:account or user.phone=:phone) ")
  UserBaseInfo loadUser(@Param("account") String account, @Param("phone") String phone);

  @Query("select u.id from UserBaseInfo u where u.phone = :phone")
  String findIdByPhone(@Param("phone") String phone);

  default DataPage querySimpleUser(Map<String, String> params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("base_user_base_info", "a")
      .select("a.id, a.user_nick_name name, a.real_name realName, a.phone, a.birth, a.avatar, a.gender, a.bind_expert_time bindExpertTime")
      .orderBy("a.id DESC");
    sqlBuilder.addWhereEq("a", "user_type", ConvertUtil.convert(params.get("userType"), Integer.class));
    sqlBuilder.addWhereEq("a", "expert_id", params.get("expertId"));
    SelectPageHelper.query(sqlBuilder, params, "a", "id", "a.phone", "a.user_nick_name", "a.real_name");
    return sqlBuilder.toPage(getEntityManager()).query(params);
  }




  default DataPage<Map> listPage(Map<String, String> params) {
    int pageNum = Pages.Helper.pageNum(params.get("pageNum"));
    int pageSize = Pages.Helper.pageSize(params.get("pageSize"));
    String userNickName = ConvertUtil.convertNullable(params.get("searchUserNickName"), String.class);
    String companyName = ConvertUtil.convertNullable(params.get("searchCompanyName"), String.class);
    String phone = ConvertUtil.convertNullable(params.get("searchPhone"), String.class);
    Integer status = ConvertUtil.convertNullable(params.get("searchStatus"), Integer.class);
    String regionId = ConvertUtil.convertNullable(params.get("regionId"), String.class);
    String hand = ConvertUtil.convertNullable(params.get("hand"), String.class);
    Integer dataSource = ConvertUtil.convertNullable(params.get("dataSource"), Integer.class);
    String parentUserName = ConvertUtil.convertNullable(params.get("parentUserName"), String.class);
    String parentUserPhone = ConvertUtil.convertNullable(params.get("parentUserPhone"), String.class);
    String sortName = params.get("sortName");
    DateTime registTimeStart = ConvertUtil
      .dateNullable(params.get("registTimeStart"), "注册开始时间", Dates.DATE_TIME_FORMATTER);
    DateTime registTimeEnd = ConvertUtil.dateNullable(params.get("registTimeEnd"), "注册截止时间", Dates.DATE_TIME_FORMATTER);
    if (registTimeEnd == null) {
      registTimeEnd = DateTime.now().millisOfDay().withMaximumValue();
    }
    String querySql = "select "
      + "a.id,a.avatar,a.wx_img as wxImg,a.wx_name as wxName,a.phone,a.user_nick_name as userNickName,"
      + "a.status,a.regist_time as registTime, a.gender, a.birth, a.integral, a.level, a.balance, a.distribution "
      + ",a.regist_region as registRegion,a.last_login_time as lastLoginTime,"
      + "b.user_nick_name as parentUserNickName,a.ability,b.phone as parentUserPhone from base_user_base_info a "
      + "left join base_user_base_info b on a.parent_user_id = b.id "
      + "where 1=1 ";
    String countSql = "select count(*) from base_user_base_info a "
      + "left join base_user_base_info b on a.parent_user_id = b.id "
      + "where 1=1 ";
    NativeSQLBuilder sqlBuilder = new NativeSQLBuilder();
    //拼接条件
    if (StringUtils.isNotEmpty(userNickName)) {
      sqlBuilder.and().addWhere("a", "user_nick_name", userNickName, ConditionEnum.OPERATE_RIGHT_LIKE);
    }
    if (StringUtils.isNotEmpty(companyName)) {
      sqlBuilder.and().addWhere("b", "name", companyName, ConditionEnum.OPERATE_RIGHT_LIKE);
    }
    if (StringUtils.isNotEmpty(phone)) {
      sqlBuilder.and().addWhere("a", "phone", phone, ConditionEnum.OPERATE_RIGHT_LIKE);
    }
    if (dataSource != null) {
      sqlBuilder.and().addWhere("a", "data_source", dataSource, ConditionEnum.OPERATE_EQUAL);
    }else {
      sqlBuilder.and().addWhere("a", "data_source", 3, ConditionEnum.OPERATE_UNEQUAL);
    }
    if (StringUtils.isNotBlank(regionId)) {
      if ("other".equals(regionId)) {
        sqlBuilder.and().addWhere("a", "region_id", null, ConditionEnum.OPERATE_IS_NULL);
      } else {
        String regionIds = params.get("regionIds");
        List<String> list = Lists.newArrayList();
        try {
          list = JsonUtils.reader(regionIds, List.class);
        } catch (IOException e) {
          e.printStackTrace();
        }
        sqlBuilder.and().addWhere("a", "region_id", list, ConditionEnum.OPERATE_IN);
      }
    }
    if (status != null) {
      if (Valid.parseVal(status) != null) {
        sqlBuilder.and().addWhere("a", "status", status, ConditionEnum.OPERATE_EQUAL);
      }
    }
    if (registTimeStart != null) {
      sqlBuilder.and().addWhere("a", "regist_time", registTimeStart.toDate(), ConditionEnum.OPERATE_GREATER_EQUAL,
                                "registTimeStart"
      );
    }
    if (StringUtils.isNotBlank(hand)) {
      sqlBuilder.and().addWhere("a", "remark", "CRM",
                                "0".equals(hand) ? ConditionEnum.OPERATE_UNEQUAL : ConditionEnum.OPERATE_EQUAL
      );
    }
    sqlBuilder.and()
              .addWhere("a", "regist_time", registTimeEnd.toDate(), ConditionEnum.OPERATE_LESS_EQUAL, "registTimeEnd");
    sqlBuilder.and().addWhere("a", "user_type", UserType.USER.getCode(), ConditionEnum.OPERATE_EQUAL);

    if (StringUtils.isNotBlank(parentUserName)) {
      sqlBuilder.and().addWhere("b","user_nick_name",parentUserName,ConditionEnum.OPERATE_EQUAL);
    }
    if (StringUtils.isNotBlank(parentUserPhone)) {
      sqlBuilder.and().addWhere("b","phone",parentUserPhone,ConditionEnum.OPERATE_EQUAL);
    }

    sqlBuilder.bindQuerySql(querySql).orderBy(" a.regist_time desc ");
    jakarta.persistence.Query listQuery = setMapResult(sqlBuilder.build(getEntityManager()));
    sqlBuilder.bindQuerySql(countSql);
    jakarta.persistence.Query countQuery = sqlBuilder.build(getEntityManager());
    return pageByQuery(countQuery, listQuery, pageNum, pageSize);

  }


  boolean existsByPhone(@Param("phone") String phone);
  boolean existsByPhoneAndIdNot(@Param("phone") String phone, @Param("idNot") String idNot);
  boolean existsByIdCard(@Param("idCard") String idCard);
  boolean existsByIdCardAndIdNot(@Param("idCard") String idCard, @Param("idNot") String idNot);

  @Query(value = "SELECT id, phone FROM base_user_base_info WHERE phone IN :phone", nativeQuery = true)
  List<Map<String, String>> findIdAndPhone(@Param("phone") Collection<String> phone);

  default Map<String, Object> userTypeSum(){
    Map<String, Object> count = Maps.newHashMap();
    JpaHelper.setMapResult(getEntityManager().createNativeQuery(
      "SELECT user_type userType, COUNT(user_type) userCount FROM base_user_base_info GROUP BY user_type"), r->{
      Integer userType = ConvertUtil.convert(r.get("userType"), Integer.class);
      if(userType != null){
        count.put("userType_" + userType, ConvertUtil.convert(r.get("userCount"), 0));
      }
    }).getResultList();
    return count;
  }

  default List<RowMap> statistics(boolean daysOrMonth, boolean cumulative){
    int count = daysOrMonth ? 30 : 12;
    DateTime time = daysOrMonth ? DateTime.now().minusDays(count) : DateTime.now().minusMonths(count);
    Map<String, Object> params = Maps.newHashMap();
    StringBuilder sql = new StringBuilder();
    int userCode = UserType.USER.getCode();
    int expertCode = UserType.EXPERT.getCode();
    for (int i = 1; i <= count; i++) {
      String end = "e" + i;
      if(i>1){
        sql.append(" UNION ALL ");
      }
      DateTime current = daysOrMonth ? time.plusDays(i) : time.plusMonths(i);
      sql.append("SELECT '").append(current.toString(daysOrMonth ? "MMdd" : "yyyyMM"))
         .append("' unitTime, SUM(CASE WHEN user_type = ").append(userCode)
         .append(" THEN 1 ELSE 0 END) userCount, SUM(CASE WHEN user_type = ").append(expertCode)
         .append(" THEN 1 ELSE 0 END) expertCount FROM base_user_base_info WHERE regist_time <= :").append(end);
      if(!cumulative){
        String start = "s" + i;
        sql.append(" AND regist_time >= :").append(start);
        if(daysOrMonth){
          params.put(start, current.dayOfMonth().withMinimumValue().secondOfDay().withMinimumValue().toDate());
        }else{
          params.put(start, current.secondOfDay().withMinimumValue().toDate());
        }
      }
      if(daysOrMonth){
        params.put(end, current.secondOfDay().withMaximumValue().toDate());
      }else{
        params.put(end, current.dayOfMonth().withMaximumValue().secondOfDay().withMaximumValue().toDate());
      }
    }
    jakarta.persistence.Query query = getEntityManager().createNativeQuery(sql.toString());
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      query.setParameter(entry.getKey(), entry.getValue());
    }
    return toRowResult(query).getResultList();
  }

  UserBaseInfo findByMiniOpenId(String miniOpenId);

  @Query(value = "UPDATE base_user_base_info SET mini_open_id='' WHERE mini_open_id=:miniOpenId ", nativeQuery = true)
  @Modifying
  void clearMiniBind(@Param("miniOpenId") String miniOpenId);

  @Modifying
  @Query(value = "UPDATE UserBaseInfo SET miniOpenId=?2 WHERE id=?1")
  void bindMiniOpenId(String id, String openId);

//  @Modifying
//  @Query(value = "UPDATE base_user_base_info SET community_id=?2, update_time=NOW() WHERE id=?1", nativeQuery = true)
//  void bindCommunityId(String id, String communityId);

//  @Modifying
//  @Query(value = "UPDATE base_user_base_info SET expert_id=?2, update_time=NOW() WHERE id=?1 AND expert_id=''", nativeQuery = true)
//  void bindExpertId(String id, String expertId);

//  default void checkCompleteInfo(String id){
//    setMapResult(getEntityManager().createNativeQuery(
//      "SELECT real_name realName, id_card idCard FROM base_user_base_info WHERE id = :id AND user_type = "
//        + UserType.USER.getCode())
//                                   .setParameter("id", id), row->{
//      if(StringUtils.isAnyBlank(row.getOrDefault("realName", "").toString(),
//                                row.getOrDefault("idCard", "").toString())){
//        throw new BusinessException("操作失败，未填写个人资料实名信息", 100);
//      }
//    }).getResultList();
//  }
}

