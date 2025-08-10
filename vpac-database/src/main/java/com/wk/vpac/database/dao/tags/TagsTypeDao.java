package com.wk.vpac.database.dao.tags;

import com.base.components.common.dto.page.DataPage;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.JpaHelper;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.wk.vpac.domain.tags.TagsType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Map;

/**
 * TagsType DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-04-30
 */
@Repository
public interface TagsTypeDao extends GenericJpaDao<TagsType, String> {

  default DataPage page(Map<String, String> params){
    String querySql = "SELECT " + JpaHelper.findColumns(TagsType.class, "t") + " FROM mjc_tags_type t WHERE 1=1";
    String countSql = "SELECT COUNT(1) FROM mjc_tags_type t WHERE 1=1";
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    sqlBuilder.addWhereEq("t","status",params.get("status"));
    sqlBuilder.addWhereRightLike("t","name",params.get("name"));
    sqlBuilder.addWhereRightLike("t","type_code",params.get("typeCode"));

    jakarta.persistence.Query count = sqlBuilder.bindQuerySql(countSql).build(JpaHelper.getEntityManager());
    jakarta.persistence.Query query = JpaHelper.setMapResult(
      sqlBuilder.bindQuerySql(querySql).orderBy("t.sort_no asc").build(JpaHelper.getEntityManager()));
    return JpaHelper.pageByQuery(count, query, params);
  }

  default DataPage<Map<String, Object>> simplePage(Map<String, String> params){
    String querySql = "select d.id,d.name as name from mjc_tags_type d where 1=1 ";
    String countSql = "select count(d.id) from mjc_tags_type d where 1=1 ";
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    if (StringUtils.isNotEmpty(params.get("searchValue"))) {
      sqlBuilder.addWhereIn("d", params.get("searchKey"), Arrays.asList(params.get("searchValue").split(",")));
    }
    String name = params.get("name");
    if(StringUtils.isNotBlank(name)){
      sqlBuilder.addWhereRightLike("d", "name", name);
    }
    String typeCode = params.get("typeCode");
    if(StringUtils.isNotBlank(typeCode)){
      sqlBuilder.addWhereEq("d", "type_code", typeCode);
    }

    jakarta.persistence.Query count = sqlBuilder.bindQuerySql(countSql).build(JpaHelper.getEntityManager());
    jakarta.persistence.Query query = JpaHelper.setMapResult(
      sqlBuilder.bindQuerySql(querySql).orderBy("d.sort_no asc").build(JpaHelper.getEntityManager()));
    return JpaHelper.pageByQuery(count, query, params);
  }

}

