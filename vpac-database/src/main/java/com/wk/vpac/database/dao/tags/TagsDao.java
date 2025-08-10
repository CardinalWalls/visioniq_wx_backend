package com.wk.vpac.database.dao.tags;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.SetsHelper;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.JpaHelper;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.google.common.collect.Sets;
import com.wk.vpac.domain.tags.Tags;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tags DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-01-03
 */
@Repository
public interface TagsDao extends GenericJpaDao<Tags, String> {
  @SuppressWarnings("all")
  default Map<String, Object> findByIdToRender(String id){
    String querySql = "SELECT " + JpaHelper.findColumns(Tags.class, "t") + " FROM mjc_tags t WHERE t.id = :id";
    NativeSQLBuilder sqlBuilder = new NativeSQLBuilder();
    sqlBuilder.setParameter("id", id);
    List resultList = JpaHelper.setMapResult(sqlBuilder.bindQuerySql(querySql).build(JpaHelper.getEntityManager()))
                               .getResultList();
    return resultList.isEmpty() ? null : (Map<String, Object>) resultList.get(0);
  }

  @SuppressWarnings("unchecked")
  default DataPage<Map<String, Object>> pageToRender(Map<String, String> params) {
    String querySql = "select "+JpaHelper.findColumns(Tags.class, "t")+" from mjc_tags t where 1=1 ";
    String countSql = "select count(*) from mjc_tags t where 1=1 ";
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    sqlBuilder.addWhereEq("t", "tag_name", params.get("tagName"));
    sqlBuilder.addWhereEq("t", "tag_code", params.get("tagCode"));
    sqlBuilder.addWhereEq("t", "type", ConvertUtil.convert(params.get("type"), Integer.class));
    jakarta.persistence.Query count = sqlBuilder.bindQuerySql(countSql).build(JpaHelper.getEntityManager());
    jakarta.persistence.Query query = JpaHelper.setMapResult(
      sqlBuilder.bindQuerySql(querySql).orderBy("d.sort_no").build(JpaHelper.getEntityManager()));
    return JpaHelper.pageByQuery(count, query, params);
  }

  default DataPage<Map<String, Object>> simplePage(Map<String, String> params){
    String querySql = "select d.id, concat_ws('-',b.name,d.tag_name)  as name  from mjc_tags d left join mjc_tags_type b on d.type_id=b.id where 1=1 ";
    String countSql = "select count(d.id) from mjc_tags d left join mjc_tags_type b on d.type_id=b.id where 1=1 ";
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    if (StringUtils.isNotEmpty(params.get("searchValue"))) {
      sqlBuilder.addWhereIn("d", params.get("searchKey"), Arrays.asList(params.get("searchValue").split(",")));
    }
    String name = params.get("name");
    if(StringUtils.isNotBlank(name)){
      sqlBuilder.addWhereRightLike("d", "tag_name", name);
    }
    Set<String> typeCodes = Sets.newHashSet("ALL");
    String typeCode = params.get("typeCode");
    if(StringUtils.isNotBlank(typeCode)){
      typeCodes.addAll(SetsHelper.toStringSets(typeCode, ","));
      sqlBuilder.addWhereIn("b", "type_code", typeCodes);
    }

    jakarta.persistence.Query count = sqlBuilder.bindQuerySql(countSql).build(JpaHelper.getEntityManager());
    jakarta.persistence.Query query = JpaHelper.setMapResult(
      sqlBuilder.bindQuerySql(querySql).orderBy("b.type_code, d.sort_no asc").build(JpaHelper.getEntityManager()));
    return JpaHelper.pageByQuery(count, query, params);
  }

  default DataPage<Map<String, Object>> page(Map<String, String> params){
    String querySql = "SELECT " + JpaHelper.findColumns(Tags.class, "a")
                      + ",b.`name` as typeName,b.type_code as typeCode from mjc_tags a left join mjc_tags_type b on a.type_id=b.id where 1=1 ";
    String countSql = "select count(a.id) from mjc_tags a left join mjc_tags_type b on a.type_id=b.id where 1=1 ";
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();

    String tagName = params.get("tagName");
    if(StringUtils.isNotBlank(tagName)){
      sqlBuilder.addWhereRightLike("a", "tag_name", tagName);
    }
    String tagCode = params.get("tagCode");
    if(StringUtils.isNotBlank(tagCode)){
      sqlBuilder.addWhereRightLike("a", "tag_code", tagCode);
    }
    String typeId = params.get("typeId");
    if(StringUtils.isNotBlank(typeId)){
      sqlBuilder.addWhereEq("a", "type_id", typeId);
    }
    String typeCode = params.get("typeCode");
    if(StringUtils.isNotBlank(typeCode)){
      sqlBuilder.addWhereEq("b", "type_code", typeCode);
    }
    sqlBuilder.addWhereEq("a","status",params.get("status"));

    jakarta.persistence.Query count = sqlBuilder.bindQuerySql(countSql).build(JpaHelper.getEntityManager());
    jakarta.persistence.Query query = JpaHelper.setMapResult(
      sqlBuilder.bindQuerySql(querySql).orderBy("a.sort_no asc").build(JpaHelper.getEntityManager()));
    return JpaHelper.pageByQueryUnlimited(count, query, params);
  }

}

