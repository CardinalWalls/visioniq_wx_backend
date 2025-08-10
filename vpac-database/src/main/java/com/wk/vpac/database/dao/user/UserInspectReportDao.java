package com.wk.vpac.database.dao.user;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import com.wk.vpac.domain.user.UserInspectReport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;


/**
 * UserInspectReport DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Repository
public interface UserInspectReportDao extends GenericJpaDao<UserInspectReport, String> {

  default DataPage<RowMap> page(PageParamMap params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_user_inspect_report", "t")
      .joinTable("LEFT JOIN wk_user_archive a ON a.idcard = t.idcard", "a", "t")
      .joinTable("LEFT JOIN base_user_base_info u ON u.id = a.user_id", "u", "a");
    sqlBuilder.select(findColumns(UserInspectReport.class, "t", "userArchiveId")
                          + ",CASE WHEN t.user_archive_id = '' OR t.user_archive_id is null THEN a.id ELSE t.user_archive_id END userArchiveId, "
                          + "GROUP_CONCAT(DISTINCT u.phone) accountPhone, a.name archiveName, a.gender archiveGender, "
                          + "a.school_name archiveSchool, a.region_name regionName");
    sqlBuilder.addWhereEq("t", "id", params.get("id"));
    sqlBuilder.addWhereEq("t", "gender", params.getInteger("gender"));
    sqlBuilder.addWhereEq("t", "import_num", params.get("importNum"));
    sqlBuilder.addWhereEq("t", "idcard", params.get("idcard"));
    String accountPhone = params.getStr("accountPhone");
    if (StringUtils.isNotBlank(accountPhone)) {
      sqlBuilder.addWhere("AND EXISTS(SELECT 1 FROM wk_user_archive a0 INNER JOIN base_user_base_info u0 ON u0.id = a0.user_id "
                            + "WHERE a0.idcard = t.idcard AND u0.phone=:accountPhone)", "accountPhone", accountPhone, "t");
    }

    sqlBuilder.addWhereRightLike("t", "hospital", params.get("hospital"));
    sqlBuilder.addWhereRightLike("t", "school", params.get("school"));
    sqlBuilder.addWhereRightLike("t", "name", params.get("name"));
    sqlBuilder.addWhereEq("a", "id", params.get("userArchiveId"), "userArchiveId");
    Date start = params.dateNullable("inspectDateS", Dates.DATE_FORMATTER_PATTERN,e -> "检查日期开始" + e);
    Date end = params.dateNullable("inspectDateE", Dates.DATE_FORMATTER_PATTERN, e -> "检查日期截止" + e);
    if(start != null){
      sqlBuilder.addWhereGeEq("t", "inspect_date", start, "inspectDateS");
    }
    if(end != null){
      sqlBuilder.addWhereLeEq("t", "inspect_date", end, "inspectDateE");
    }
    Boolean hasArchive = params.getBoolean("hasArchive");
    if(hasArchive != null){
      sqlBuilder.addWhere("AND a.id IS "+(hasArchive?"NOT":"")+" NULL", "a");
    }
    return sqlBuilder.groupBy("t.id").orderBy("t.inspect_date DESC, t.id DESC").toRowPage(
      getEntityManager(), conf -> conf.setNonPage(sqlBuilder.containsParamsKey("id"))).queryUnlimited(params);
  }

  @Modifying
  @Query(value = "DELETE t FROM wk_user_inspect_report t WHERE t.id IN :ids", nativeQuery = true)
  int deleteByIdIn(@Param("ids") Collection<String> ids);

  @Modifying
  int deleteByImportNum(String importNum);

  boolean existsByUserArchiveIdAndInspectDate(@Param("userArchiveId") String userArchiveId,
                                              @Param("inspectDate") String inspectDate);

  boolean existsByUserArchiveIdAndInspectDateAndIdNot(@Param("userArchiveId") String userArchiveId,
                                                      @Param("inspectDate") String inspectDate,
                                                      @Param("idNot") String idNot);
}

