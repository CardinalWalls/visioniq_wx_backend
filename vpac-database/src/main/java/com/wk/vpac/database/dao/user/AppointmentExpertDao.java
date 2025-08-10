package com.wk.vpac.database.dao.user;


import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import com.wk.vpac.domain.user.AppointmentExpert;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

/**
 * AppointmentExpert DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-02-15
 */
@Repository
public interface AppointmentExpertDao extends GenericJpaDao<AppointmentExpert, String> {

  default DataPage<Map<String, Object>> page(Map<String, String> params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_appointment_expert", "t");
    sqlBuilder.select(findColumns(AppointmentExpert.class, "t")
                        + ", ua.name userName, u.phone parentPhone, ua.idcard userIdCard, ua.gender userGender, "
                          + "ua.birth userBirth, ua.risk_level userRiskLevel, ua.remark userRemark, eu.avatar expertAvatar, "
                          + "eu.real_name expertName, e.hospital expertHospital, e.department expertDepartment,"
                          + "e.title expertTitle, e.job_position expertJobPosition");
    sqlBuilder.joinTable("LEFT JOIN wk_user_archive ua ON ua.id = t.user_archive_id", "ua");
    sqlBuilder.joinTable("LEFT JOIN base_user_base_info u ON u.id = ua.user_id", "u", "ua");
    sqlBuilder.joinTable("LEFT JOIN wk_expert e ON e.id = t.expert_id", "e");
    sqlBuilder.joinTable("LEFT JOIN base_user_base_info eu ON eu.id = e.user_id", "eu", "e");
    sqlBuilder.addWhereEq("ua", "user_id", params.get("userId"));
    sqlBuilder.addWhereLike("ua", "name", params.get("userName"), "userName");
    sqlBuilder.addWhereEq("ua", "risk_level", ConvertUtil.convert(params.get("userRiskLevel"), Integer.class));
    sqlBuilder.addWhereEq("u", "phone", params.get("parentPhone"), "parentPhone");
    sqlBuilder.addWhereEq("t", "expert_id", params.get("expertId"));
    sqlBuilder.addWhereEq("t", "user_archive_id", params.get("userArchiveId"));
    sqlBuilder.addWhereLike("eu", "real_name", params.get("expertName"), "expertName");
    sqlBuilder.addWhereGeEq("t", "target_time", params.get("targetTimeStart"), "targetTimeStart");
    sqlBuilder.addWhereLeEq("t", "target_time", params.get("targetTimeEnd"), "targetTimeEnd");
    return sqlBuilder.orderBy("t.target_time DESC").toPage(getEntityManager()).query(params);
  }
  boolean existsByUserArchiveIdAndExpertIdAndTargetTime(String userArchiveId, String expertId, String targetTime);

  @Modifying
  @Query(value = "DELETE t FROM wk_appointment_expert t WHERE t.id IN :ids "
    + "AND EXISTS(SELECT 1 FROM wk_user_archive u WHERE u.id = t.user_archive_id AND u.user_id = :userId)", nativeQuery = true)
  int deleteByUserIdAndIdIn(@Param("userId") String userId, @Param("ids") Collection<String> ids);

  @Modifying
  int deleteByUserArchiveId(String userArchiveId);
}

