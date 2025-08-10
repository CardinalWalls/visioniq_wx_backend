package com.wk.vpac.database.dao.user;

import com.wk.vpac.domain.user.HealthyForm;

import com.base.components.common.dto.page.DataPage;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * HealthyForm DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-04-06
 */
@Repository
public interface HealthyFormDao extends GenericJpaDao<HealthyForm, String> {

  default DataPage<Map<String, Object>> page(Map<String, String> params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_healthy_form", "t");
    sqlBuilder.select(findColumns(HealthyForm.class, "t"));
    sqlBuilder.addWhereEq("t", "name", params.get("name"));
    return sqlBuilder.orderBy("t.create_time DESC").toPage(getEntityManager()).query(params);
  }
}

