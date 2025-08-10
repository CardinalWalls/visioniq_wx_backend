package com.wk.vpac.database.dao.region;


import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import com.wk.vpac.domain.region.Community;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Community DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-02-15
 */
@Repository
public interface CommunityDao extends GenericJpaDao<Community, String> {

  default DataPage<Map<String, Object>> page(Map<String, String> params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_community", "t");
    sqlBuilder.joinTable("LEFT JOIN base_region r ON r.id = t.region_id", "r");
    sqlBuilder.joinTable("LEFT JOIN wk_expert e ON e.id = t.expert_id", "e");
    sqlBuilder.joinTable("LEFT JOIN base_user_base_info eu ON e.user_id = eu.id", "eu", "e");
    sqlBuilder.select(findColumns(Community.class, "t") +
                        ", e.hospital expertHospital, e.department expertDepartment, eu.real_name expertName, r.name_path regionName");
    sqlBuilder.addWhereLike("t", "street_name", params.get("streetName"));
    sqlBuilder.addWhereLike("t", "name", params.get("name"));
    sqlBuilder.addWhereEq("t", "valid", ConvertUtil.convert(params.get("valid"), Boolean.class));
    sqlBuilder.addWhereLike("eu", "real_name", params.get("expertName"));
    sqlBuilder.addWhereEq("t", "id", params.get("id"));
    if(!sqlBuilder.containsParamsKey("id")){
      return sqlBuilder.orderBy("t.sort_no,t.id").toPage(getEntityManager()).query(params);
    }
    return DataPage.nonPage(setMapResult(sqlBuilder.build(getEntityManager())).getResultList());
  }
}

