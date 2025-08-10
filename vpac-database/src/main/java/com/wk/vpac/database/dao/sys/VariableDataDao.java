package com.wk.vpac.database.dao.sys;

import com.base.components.common.constants.sys.PageSort;
import com.base.components.common.constants.sys.SortBuilder;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.JpaHelper;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wk.vpac.domain.sys.VariableData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * VariableData DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-05-21
 */
@Repository
public interface VariableDataDao extends GenericJpaDao<VariableData, String>{
  default Map<String, Object> findByIdToRender(Serializable id){
    String querySql = "SELECT " + JpaHelper.findColumns(VariableData.class, "t") + " FROM base_variable_data t WHERE t.id = :id";
    NativeSQLBuilder sqlBuilder = new NativeSQLBuilder();
    sqlBuilder.setParameter("id", id);
    List resultList = JpaHelper.setMapResult(sqlBuilder.bindQuerySql(querySql).build(JpaHelper.getEntityManager()))
                               .getResultList();
    return resultList.isEmpty() ? null : (Map<String, Object>) resultList.get(0);
  }

  default DataPage<Map<String, Object>> pageToRender(Map params){
    String commonSql = " FROM base_variable_data a WHERE 1=1";

    String countSql = "SELECT COUNT(a.id) "+commonSql;
    String querySql = "SELECT  "+JpaHelper.findColumns(VariableData.class,"a") +commonSql;
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    sqlBuilder.addWhereEq("a", "id", params.get("id"));
    String[] types = StringUtils.split(ConvertUtil.convert(params.get("type"), ""), ",");
    if(types != null && types.length > 0){
      sqlBuilder.addWhereIn("a", "type", Sets.newHashSet(types));
    }
    String[] refIds = StringUtils.split(ConvertUtil.convert(params.get("refId"), ""), ",");
    if(refIds != null && refIds.length > 0){
      sqlBuilder.addWhereIn("a", "ref_id", Sets.newHashSet(refIds));
    }
    jakarta.persistence.Query countQuery = sqlBuilder.bindQuerySql(countSql).build(JpaHelper.getEntityManager());
    PageSort sort = SortBuilder.by("a.order_no", "a.order_no DESC").build(params);
    jakarta.persistence.Query listQuery = JpaHelper.setMapResult(sqlBuilder.bindQuerySql(querySql)
                                                                         .orderBy(sort.toUnderlineWithPrefix())
                                                                         .build(JpaHelper.getEntityManager()));
    DataPage<Map<String, Object>> dataPage = JpaHelper.<Map<String, Object>>pageByQuery(countQuery, listQuery, params)
      .setSortField(sort.toCamelCase());
    return dataPage.map(o -> {
      Object jsonData = o.get("jsonData");
      if(jsonData!=null){
        String s = jsonData.toString();
        try {
          o.put("jsonData", JsonUtils.reader(s,Map.class));
        } catch (IOException e) {
          o.put("jsonData", Maps.newHashMap());
        }
      }
      return o;
    });
  }
}

