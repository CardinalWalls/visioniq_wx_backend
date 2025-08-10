package com.wk.vpac.database.dao.operations;

import com.base.components.common.constants.Valid;
import com.base.components.common.constants.sys.PageSort;
import com.base.components.common.constants.sys.Pages;
import com.base.components.common.constants.sys.SortBuilder;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.JpaHelper;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.google.common.collect.Sets;
import com.wk.vpac.domain.operations.Advertisement;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Advertisement DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-03-15
 */
@Repository
public interface AdvertisementDao extends GenericJpaDao<Advertisement, String>{


  @Query(value = "select id,img,descri,url,remark,position_code as positionCode,order_no as orderNo from base_advertisement where status= :status and position_code= :positionCode order by order_no asc", nativeQuery = true)
  List<Map> banner(@org.springframework.data.repository.query.Param("positionCode") String positionCode, 
                   @org.springframework.data.repository.query.Param("status") Integer status);

  @Query(value = "update base_advertisement set url=:newUrl where url=:oldUrl", nativeQuery = true)
  @Modifying
  int updateUrls(@org.springframework.data.repository.query.Param("oldUrl") String oldUrl,
                 @org.springframework.data.repository.query.Param("newUrl") String newUrl);

  default Map<String, Object> findByIdToRender(Serializable id){
    String querySql = "SELECT " + JpaHelper.findColumns(Advertisement.class, "t") + " FROM base_advertisement t WHERE t.id = :id";
    NativeSQLBuilder sqlBuilder = new NativeSQLBuilder();
    sqlBuilder.setParameter("id", id);
    List resultList = JpaHelper.setMapResult(sqlBuilder.bindQuerySql(querySql).build(JpaHelper.getEntityManager()))
                               .getResultList();
    return resultList.isEmpty() ? null : (Map<String, Object>) resultList.get(0);
  }

  default DataPage<Map<String, Object>> pageToRender(Map params) {
    String commonSql = " FROM base_advertisement a "
      + "LEFT JOIN base_advertisement_group b on a.group_id = b.id where 1=1";
    String countSql = "SELECT COUNT(*) "+commonSql;
    String querySql = "SELECT " + JpaHelper.findColumns(Advertisement.class, "a") + ", b.name groupName, b.type_code typeCode " + commonSql;
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    sqlBuilder.addWhereEq("a", "id", params.get("id"));
    sqlBuilder.addWhereEq("a", "status", Valid.TRUE.getVal());
    String[] typeCodes = StringUtils.split(ConvertUtil.convert(params.get("typeCode"), ""), ",");
    if(typeCodes != null && typeCodes.length > 0){
      sqlBuilder.addWhereIn("b", "type_code", Sets.newHashSet(typeCodes));
    }
    String[] positionCodes = StringUtils.split(ConvertUtil.convert(params.get("positionCode"), ""), ",");
    if(positionCodes != null && positionCodes.length > 0){
      sqlBuilder.addWhereIn("a", "position_code", Sets.newHashSet(positionCodes));
    }
    jakarta.persistence.Query countQuery = sqlBuilder.bindQuerySql(countSql).build(JpaHelper.getEntityManager());
    PageSort sort = SortBuilder.by("a.order_no", "a.order_no DESC").build(params);
    jakarta.persistence.Query listQuery = JpaHelper.setMapResult(sqlBuilder.bindQuerySql(querySql)
                                                                         .orderBy(sort.toUnderlineWithPrefix())
                                                                         .build(JpaHelper.getEntityManager()));
    return JpaHelper.<Map<String, Object>>pageByQuery(countQuery, listQuery, params).setSortField(sort.toCamelCase());
  }

  default DataPage list(Map<String, String> params) {

    int pageNum = Pages.Helper.pageNum(params.get("pageNum"));
    int pageSize = Pages.Helper.pageSize(params.get("pageSize"));
    String startDate = params.get("startDate");
    String endDate = params.get("endDate");
    String groupId = ConvertUtil.convertNullable(params.get("searchGroupId"), String.class);
    String descri = ConvertUtil.convertNullable(params.get("searchDescri"), String.class);
    Integer status = ConvertUtil.convertNullable(params.get("searchStatus"), Integer.class);
    String positionCode = ConvertUtil.convertNullable(params.get("positionCode"),String.class);
    String querySql = "select a.id as id,a.group_id as groupId,a.descri as descri,a.position_code as positionCode,"
      + "a.img as img,a.width as width,a.height as height,a.url as url,a.click_count as clickCount, a.create_time as createTime,"
      + "a.begin_time as beginTime,a.end_time as endTime,a.status as status,a.remark as remark,a.order_no as orderNo,b.name as groupName,"
      + "GROUP_CONCAT(DISTINCT t.tag_name) tagName, GROUP_CONCAT(DISTINCT t.id) tagsId "
      + "from base_advertisement a left join base_advertisement_group b on a.group_id=b.id "
      + "LEFT JOIN mjc_tags_ref r ON r.ref_id = a.id LEFT JOIN mjc_tags t ON t.id = r.tags_id where 1 = 1 ";
    String countSql
      = "select count(a.id) from base_advertisement a left join base_advertisement_group b on a.group_id=b.id where 1=1 ";
    NativeSQLBuilder sqlBuilder = new NativeSQLBuilder();

    //起止时间
    if (StringUtils.isNotEmpty(startDate)) {
      sqlBuilder.and().addWhere("a", "create_time", startDate, ConditionEnum.OPERATE_GREATER_EQUAL, "startDate");
    }
    if (StringUtils.isNotEmpty(endDate)) {
      sqlBuilder.and().addWhere("a", "create_time", endDate, ConditionEnum.OPERATE_LESS_EQUAL,"endDate");
    }
    if (StringUtils.isNotEmpty(groupId)) {
      sqlBuilder.and().addWhere("a", "group_id", groupId, ConditionEnum.OPERATE_EQUAL);
    }
    if (StringUtils.isNotEmpty(descri)) {
      sqlBuilder.and().addWhere("a", "descri", descri, ConditionEnum.OPERATE_RIGHT_LIKE);
    }
    if(StringUtils.isNotEmpty(positionCode)){
      sqlBuilder.and().addWhere("a", "position_code", positionCode, ConditionEnum.OPERATE_EQUAL);
    }
    if (status != null) {
      if (Valid.parseVal(status) != null) {
        sqlBuilder.and().addWhere("a", "status", status, ConditionEnum.OPERATE_EQUAL);
      }
    }
    sqlBuilder.addWhere(()->"AND EXISTS (SELECT 1 FROM mjc_tags_ref WHERE ref_id = a.id AND tags_id IN :tagsId)",
                        "tagsId", StringUtils.split(params.get("tagsId"),","));
    sqlBuilder.addWhere(()->"AND EXISTS (SELECT 1 FROM mjc_tags_ref mtr LEFT JOIN mjc_tags mt ON mt.id=mtr.tags_id WHERE mtr.ref_id=a.id and mt.tag_code IN :tagsCode )",
                        "tagsCode", StringUtils.split(params.get("tagsCode"),","));
    sqlBuilder.bindQuerySql(countSql);
    jakarta.persistence.Query countQuery = sqlBuilder.build(getEntityManager());
    PageSort sort = SortBuilder.by("a.createTime, a.order_no", "a.order_no").build(params);
    sqlBuilder.orderBy(sort.toUnderlineWithPrefix());
    sqlBuilder.bindQuerySql(querySql).groupBy("a.id");
    jakarta.persistence.Query listQuery = setMapResult(sqlBuilder.build(getEntityManager()));
    return pageByQuery(countQuery, listQuery, pageNum, pageSize).setSortField(sort::toCamelCase);
  }
}

