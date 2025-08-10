package com.wk.vpac.database.dao.user;

import com.wk.vpac.domain.user.UserIntervene;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;


/**
 * UserIntervene DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2024-06-11
 */
@Repository
public interface UserInterveneDao extends GenericJpaDao<UserIntervene, String> {

  default DataPage<RowMap> page(PageParamMap params){
    String userArchiveId = params.getStrTrim("userArchiveId");
    if (StringUtils.isBlank(userArchiveId)){
      return DataPage.getEmpty();
    }
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_user_intervene", "t");
    sqlBuilder.select(findColumns(UserIntervene.class, "t"));
    sqlBuilder.addWhereEq("t", "user_archive_id", userArchiveId);
    Boolean pushed = params.getBoolean("pushed");
    if(pushed != null){
      sqlBuilder.addWhere("AND t.push_time "+(pushed?"!=":"=")+" ''", "t");
    }
    return sqlBuilder.orderBy("t.id DESC").toRowPage(getEntityManager()).query(params);
  }
}

