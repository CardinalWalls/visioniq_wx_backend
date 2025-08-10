package com.wk.vpac.database.dao.user;

import com.wk.vpac.domain.user.UserVisualFunctionReport;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;


/**
 * UserVisualFunctionReport DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2024-05-09
 */
@Repository
public interface UserVisualFunctionReportDao extends GenericJpaDao<UserVisualFunctionReport, String> {

  default DataPage<RowMap> page(PageParamMap params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_user_visual_function_report", "t")
      .joinTable("LEFT JOIN wk_user_archive a ON a.id = t.user_archive_id", "a", "t");
    sqlBuilder.select(findColumns(UserVisualFunctionReport.class, "t")
                        + ", a.name, a.gender, a.birth, a.idcard, a.region_name regionName");
    sqlBuilder.addWhereEq("t", "id", params.get("id"));
    sqlBuilder.addWhereEq("t", "user_archive_id", params.get("userArchiveId"));
    return sqlBuilder.orderBy("t.inspect_date DESC, t.id DESC").toRowPage(
      getEntityManager(), conf -> conf.setNonPage(sqlBuilder.containsParamsKey("id"))).query(params);
  }

  @Modifying
  int deleteByUserArchiveId(String userArchiveId);

  @Modifying
  @Query(value = "DELETE t FROM wk_user_visual_function_report t WHERE t.id IN :ids "
    + "AND EXISTS(SELECT 1 FROM wk_user_archive u WHERE u.id = t.user_archive_id AND u.user_id = :userId)", nativeQuery = true)
  int deleteByUserIdAndIdIn(@Param("userId") String userId, @Param("ids") Collection<String> ids);
}

