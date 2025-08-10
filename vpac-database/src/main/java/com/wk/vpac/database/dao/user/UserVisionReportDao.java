package com.wk.vpac.database.dao.user;

import com.wk.vpac.domain.user.UserVisionReport;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


/**
 * UserVisionReport DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Repository
public interface UserVisionReportDao extends GenericJpaDao<UserVisionReport, String> {

  default DataPage<RowMap> page(PageParamMap params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_user_vision_report", "t");
    sqlBuilder.select(findColumns(UserVisionReport.class, "t"));
    return sqlBuilder.orderBy("t.id DESC").toRowPage(getEntityManager()).query(params);
  }

  @Modifying
  int deleteByUserArchiveId(String userArchiveId);
}

