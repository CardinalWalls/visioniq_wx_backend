package com.wk.vpac.database.dao.operations.news;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ColumnsUtil;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.SetsHelper;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import com.base.components.database.jpa.util.JpaHelper;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.google.common.collect.Maps;
import com.wk.vpac.domain.operations.news.News;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * News DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-08-07
 */
@Repository
public interface NewsDao extends GenericJpaDao<News, String> {

  /**
   * 新闻上线
   *
   * @param id
   * @param status
   * @param publishTime
   */
  default int online(String id, Integer status, Date publishTime) {
    Assert.hasText(id, "id不能为空");
    Assert.notNull(status, "状态不能为空");
    Assert.notNull(publishTime, "发布时间不能为空");

    String querySql = "update base_news set status = :status, publish_time = :publishTime where id = :id ";
    NativeSQLBuilder sqlBuilder = new NativeSQLBuilder();
    jakarta.persistence.Query query = sqlBuilder.bindQuerySql(querySql).build(JpaHelper.getEntityManager());
    query.setParameter("id", id);
    query.setParameter("status", status);
    query.setParameter("publishTime", publishTime);
    return query.executeUpdate();
  }

  /**
   * 改变状态
   *
   * @param id
   * @param status
   */
  default void changeStatus(String id, Integer status) {
    Assert.hasText(id, "id不能为空");
    Assert.notNull(status, "状态不能为空");

    String querySql = "update base_news set status = :status where id = :id ";
    NativeSQLBuilder sqlBuilder = new NativeSQLBuilder();
    jakarta.persistence.Query query = sqlBuilder.bindQuerySql(querySql).build(JpaHelper.getEntityManager());
    query.setParameter("id", id);
    query.setParameter("status", status);
    query.executeUpdate();
  }


  /**
   * 推荐
   *
   * @param id
   * @param val
   */
  default void recommend(String id, int val) {
    Assert.hasText(id, "id不能为空");
    Assert.notNull(val, "值不能为空");

    String querySql = "update base_news set is_recommend = :val where id = :id ";
    NativeSQLBuilder sqlBuilder = new NativeSQLBuilder();
    jakarta.persistence.Query query = sqlBuilder.bindQuerySql(querySql).build(JpaHelper.getEntityManager());
    query.setParameter("id", id);
    query.setParameter("val", val);
    query.executeUpdate();
  }

  default DataPage<Map<String, Object>> queryNewsPage2(Map<String, String> params) {
    String userId = params.get("userId");
    String userCount = ", 0 userCollect, 0 userPraise, 0 userEvaluate ";
    boolean hasUserId = StringUtils.isNotBlank(userId);
    if (hasUserId) {
      userCount = ",COUNT(DISTINCT (CASE WHEN h.user_id = :userId THEN 1 END)) userCollect, "
        + "COUNT(DISTINCT (CASE WHEN j.user_id = :userId THEN 1 END)) userPraise, "
        + "COUNT(DISTINCT (CASE WHEN k.user_id = :userId THEN 1 END)) userEvaluate ";
    }
    String startTime = params.get("startTime");
    String endTime = params.get("endTime");
    String title = params.get("title");
    String categoryId = params.get("categoryId");
    String typeId = params.get("typeId");
    String typeCode = params.get("typeCode");
    String typeGroupName = params.get("typeGroupName");
    String status = params.get("status");
    String recommend = params.get("recommend");
    String vipView = params.get("vipView");
    String templateType = params.get("templateType");
    String regionNews = params.get("regionNews");
    int pageNum = Pages.Helper.pageNum(params.get("pageNum"));
    int pageSize = Pages.Helper.pageSize(params.get("pageSize"));
    String tagsId = params.get("tagsId");
    String tagsCode = params.get("tagsCode");
    String regionId = params.get("regionId");
    String regionName = params.get("regionName");
    String id = params.get("id");
    String isOrdinary = params.get("isOrdinary");
    // 关联专家
    String expertId = params.get("expertId");
    String expertName = params.get("expertName");
    String hasExpert = params.get("hasExpert");
    String hasRed = params.get("hasRed");
    String theSource = params.get("theSource");

    String sql = "SELECT * FROM ( SELECT n.summary,n.id,HAS_CONTENT "
      + " nt.`name` AS categoryName,nt.type_code as typeCode,nt.`name` AS typeName,nt.type AS typeType,n.type_id AS typeId, nt.group_name AS typeGroupName, "
      + "n.keyword,n.vip_view AS vipView,n.template_type AS templateType,n.display_type AS displayType,n.detail_type AS detailType,n.price, "
      + " n.title, n.expert_id AS expertId,q.name as expertNameForCloud,q.avatar as expertAvatarForCloud,"
      + " q.profile as expertProfileCloud,q.job_position as expertJobPositionForCloud,q.job_title as expertJobTitleForCloud,"
      + " GROUP_CONCAT(DISTINCT s.id) expertBusinessAreaIdsForCloud, GROUP_CONCAT(DISTINCT s.name) expertBusinessAreaNamesForCloud,"
      + " n.img,n.region_news as regionNews,n.the_source as theSource, n.subtitle, n.imgs, n.other_attr otherAttr, "
      + " n.create_time AS createTime,n.publish_time AS publishTime, "
      + " n.author_name AS authorName, l.expert_id AS expertId," + " n.`status` AS `status`,"
      + " o.org_name AS orgName," + " o.publish_number AS publishNumber," + " sm.member_name AS nickName, "
      + " n.is_recommend AS recommend, " + "GROUP_CONCAT(DISTINCT f.id) AS tagsId,"
      + "GROUP_CONCAT(DISTINCT f.tag_code) AS tagCode,  " + "GROUP_CONCAT(DISTINCT f.tag_name) AS tags, "
      + "GROUP_CONCAT(DISTINCT p.region_id ORDER BY p.region_id ASC) AS regionId,"
      + "GROUP_CONCAT(DISTINCT p.region_name ORDER BY p.region_id ASC) AS regionName,"
      + "ifnull(g.visit_count,0) AS visitCount, " + "COUNT(DISTINCT h.id) AS collectCount, "
      + "COUNT(DISTINCT j.id) AS praiseCount, " + "COUNT(DISTINCT k.id) AS evaluateCount " + userCount
      + "FROM base_news n " + "LEFT JOIN mjc_tags_ref e ON n.id=e.ref_id  " + "LEFT JOIN mjc_tags f ON f.id=e.tags_id  "
      + " LEFT JOIN base_news_type nt ON n.type_id = nt.id " + " LEFT JOIN admin_sys_member sm ON n.author_id = sm.id "
      + " LEFT JOIN mjc_visit_count g ON g.target_id=n.id " + " LEFT JOIN mjc_collect h ON h.target_id=n.id "
      + " LEFT JOIN mjc_praise j ON j.target_id=n.id " + " LEFT JOIN mjc_evaluate k ON k.target_id=n.id "
      + " LEFT JOIN mjc_news_explanation l ON  n.id = l.news_id " + " LEFT JOIN mjc_expert m ON l.expert_id= m.id   "
      + " LEFT JOIN mjc_news_other o ON o.news_id= n.id  " + " LEFT JOIN mjc_region_ref p ON p.ref_id= n.id  "
      + " LEFT JOIN mjc_expert q ON n.expert_id= q.id  "
      + " LEFT JOIN mjc_business_area_ref r ON r.ref_id = q.id "
      + " LEFT JOIN mjc_business_area s ON s.id = r.area_id "
      + " WHERE " + " 1 = 1  " + " EXTRA_SQL " + "GROUP BY " + " n.id  "
      + " ORDER BY n.publish_time DESC ) z WHERE 1=1  ";
    String countSql = "SELECT " + " COUNT(*)  " + "FROM " + " ( " + "SELECT " + " n.id " + " FROM " + " base_news n "
      + "LEFT JOIN mjc_tags_ref e ON n.id=e.ref_id  " + "LEFT JOIN mjc_tags f ON f.id=e.tags_id  "
      + " LEFT JOIN base_news_type nt ON n.type_id = nt.id " + " LEFT JOIN admin_sys_member sm ON n.author_id = sm.id "
      + " LEFT JOIN mjc_news_explanation l ON  n.id = l.news_id " + " LEFT JOIN mjc_expert m ON l.expert_id= m.id   "
      + " LEFT JOIN mjc_news_other o ON o.news_id= n.id  " + "WHERE " + " 1 = 1  " + " EXTRA_SQL " + "GROUP BY "
      + " n.id  " + " ) z WHERE 1=1 ";

    NativeSQLBuilder sqlBuilder = new NativeSQLBuilder();
    String EXTRA_SQL = "";
    String HAS_CONTENT = "";

    if (StringUtils.isNotEmpty(expertId)) {
      EXTRA_SQL += " and n.expert_id=:expertId ";
      sqlBuilder.setParameter("expertId", expertId);
    }

    if (StringUtils.isNotEmpty(hasExpert)) {
      if ("0".equals(hasExpert)) {
        EXTRA_SQL += " and (l.id is null or l.status=0)";
      }
      if ("1".equals(hasExpert)) {
        EXTRA_SQL += " and l.status=1 ";
      }
    }

    if (StringUtils.isNotEmpty(hasRed)) {
      if ("0".equals(hasRed)) {
        EXTRA_SQL += " and o.id is null ";
      }
      if ("1".equals(hasRed)) {
        EXTRA_SQL += " and o.id is not null ";
      }
    }

    if (StringUtils.isNotEmpty(regionNews)) {
      EXTRA_SQL += " and n.region_news = :regionNews ";
      sqlBuilder.setParameter("regionNews", regionNews);
    }

    if (StringUtils.isNotEmpty(expertId)) {
      EXTRA_SQL += " and l.expert_id = :expertId ";
      sqlBuilder.setParameter("expertId", expertId);
    }
    if (StringUtils.isNotEmpty(expertName)) {
      EXTRA_SQL += " and m.name like :expertName ";
      sqlBuilder.setParameter("expertName", expertName + "%");
    }
    if (StringUtils.isNotEmpty(id)) {
      EXTRA_SQL += " and n.id = :id ";
      HAS_CONTENT += " n.content,";
      sqlBuilder.setParameter("id", id);
    }
    if (StringUtils.isNotBlank(startTime)) {
      EXTRA_SQL += " and n.create_time >= :startTime ";
      sqlBuilder.setParameter("startTime", startTime);
    }
    if (StringUtils.isNotBlank(endTime)) {
      EXTRA_SQL += " and n.create_time <= :endTime ";
      sqlBuilder.setParameter("endTime", endTime);
    }
    if (StringUtils.isNotBlank(title)) {
      EXTRA_SQL += " and n.title like :title ";
      sqlBuilder.setParameter("title", "%" + title + "%");
    }

    if (StringUtils.isNotBlank(theSource)) {
      EXTRA_SQL += " and n.the_source like :theSource ";
      sqlBuilder.setParameter("theSource", theSource + "%");
    }

    if (StringUtils.isNotBlank(categoryId)) {
      EXTRA_SQL += " and n.type_id = :categoryId ";
      sqlBuilder.setParameter("categoryId", categoryId);
    }
    if (StringUtils.isNotBlank(typeId)) {
      EXTRA_SQL += " and n.type_id = :typeId ";
      sqlBuilder.setParameter("typeId", typeId);
    }
    if (StringUtils.isNotBlank(typeCode)) {
      EXTRA_SQL += " and nt.type_code = :typeCode ";
      sqlBuilder.setParameter("typeCode", typeCode);
    }
    if (StringUtils.isNotBlank(typeGroupName)) {
      EXTRA_SQL += " and nt.group_name = :typeGroupName ";
      sqlBuilder.setParameter("typeGroupName", typeGroupName);
    }
    if (StringUtils.isNotBlank(isOrdinary)) {
      EXTRA_SQL += " and nt.is_ordinary = :isOrdinary ";
      sqlBuilder.setParameter("isOrdinary", isOrdinary);
    }
    if (StringUtils.isNotBlank(status)) {
      EXTRA_SQL += " and n.status = :status ";
      sqlBuilder.setParameter("status", status);
    } else {
      EXTRA_SQL += " and n.status != :status ";
      sqlBuilder.setParameter("status", 2);
    }
    if (StringUtils.isNotBlank(recommend)) {
      EXTRA_SQL += " and n.is_recommend = :recommend ";
      sqlBuilder.setParameter("recommend", recommend);
    }
    if (StringUtils.isNotBlank(vipView)) {
      EXTRA_SQL += " and n.vip_view = :vipView ";
      sqlBuilder.setParameter("vipView", vipView);
    }
    if (StringUtils.isNotBlank(templateType)) {
      EXTRA_SQL += " and n.template_type = :templateType ";
      sqlBuilder.setParameter("templateType", templateType);
    }
    if (StringUtils.isNotEmpty(tagsId)) {
      Set<String> tags = SetsHelper.toStringSets(tagsId, ",");
      if(!tags.isEmpty()){
        EXTRA_SQL += " and exists(select 1 from  mjc_tags_ref  where ref_id=n.id and tags_id in :tagsId ) ";
        sqlBuilder.setParameter("tagsId", tags);
      }
    }
    if (StringUtils.isNotEmpty(tagsCode)) {
      EXTRA_SQL
        += " and exists(select 1 from  mjc_tags_ref mtr left join mjc_tags mt on mt.id=mtr.tags_id where mtr.ref_id=n.id and mt.tag_code in :tagsCode ) ";
      sqlBuilder.setParameter("tagsCode", Arrays.asList(tagsCode.split(",")));
    }

    if (StringUtils.isNotEmpty(regionId)) {
      EXTRA_SQL += " and exists(select 1 from  mjc_region_ref  where ref_id=n.id and region_id in :regionId ) ";
      sqlBuilder.setParameter("regionId", Arrays.asList(regionId.split(",")));
    }
    if (StringUtils.isNotEmpty(regionName)) {
      EXTRA_SQL += " and exists(select 1 from  mjc_region_ref  where ref_id=n.id and region_name in :regionName ) ";
      sqlBuilder.setParameter("regionName", Arrays.asList(regionName.split(",")));
    }

    String searchKey = params.get("searchKey");
    if (StringUtils.isNotBlank(searchKey)) {
      EXTRA_SQL
        += " AND MATCH (n.`title`, n.`content`, n.`summary`, n.`author_name`, n.`keyword`) AGAINST (:searchKey) ";
      sqlBuilder.setParameter("searchKey", searchKey);
    }

    // 互动
    String interactiveType = params.get("interactiveType");
    if (hasUserId && StringUtils.isNotEmpty(interactiveType)) {
      String INTERACTIVE_TIME_SQL = "";
      String s = " AND EXISTS (SELECT 1 FROM mjc_" + interactiveType
        + " _i WHERE n.id = _i.target_id AND _i.user_id = :interactiveUserId INTERACTIVE_TIME_SQL) ";
      sqlBuilder.setParameter("interactiveUserId", userId);

      String interactiveStartTime = params.get("interactiveStartTime");
      if (StringUtils.isNotEmpty(interactiveStartTime)) {
        INTERACTIVE_TIME_SQL += " AND _i.create_time >= :interactiveStartTime ";
        sqlBuilder.setParameter("interactiveStartTime", interactiveStartTime);
      }
      String interactiveEndTime = params.get("interactiveEndTime");
      if (StringUtils.isNotEmpty(interactiveEndTime)) {
        INTERACTIVE_TIME_SQL += " AND _i.create_time <= :interactiveEndTime ";
        sqlBuilder.setParameter("interactiveEndTime", interactiveEndTime);
      }
      s = s.replace("INTERACTIVE_TIME_SQL", INTERACTIVE_TIME_SQL);
      EXTRA_SQL += s;
    }

    sql = sql.replace("EXTRA_SQL", EXTRA_SQL);
    sql = sql.replace("HAS_CONTENT", HAS_CONTENT);
    countSql = countSql.replace("EXTRA_SQL", EXTRA_SQL);

    sqlBuilder.bindQuerySql(countSql);
    jakarta.persistence.Query countQuery = sqlBuilder.build(getEntityManager());
    if (hasUserId) {
      sqlBuilder.setParameter("userId", userId);
    }
    sqlBuilder.bindQuerySql(sql);
    jakarta.persistence.Query listQuery = setMapResult(sqlBuilder.build(getEntityManager()));

    return pageByQuery(countQuery, listQuery, pageNum, pageSize);
  }

  default DataPage<Map<String, Object>> queryNewsPage(Map<String, String> params) {
    String userId = params.get("userId");
    String id = params.get("id");
    boolean hasId = StringUtils.isNotBlank(id);
    DynamicSqlBuilder builder = DynamicSqlBuilder.create("base_news", "n");
    builder.select(ColumnsUtil.findColumns(News.class, "n", hasId?"":"content")
                     + ", nt.type_code typeCode, nt.`name` typeName, nt.type typeType, nt.group_name typeGroupName, nt.target_link targetLink");
    builder.joinTable("LEFT JOIN base_news_type nt ON n.type_id = nt.id", "nt", "n");
    builder.addWhereEq("n", "id", id);
    builder.addWhereEq("n", "is_recommend", ConvertUtil.convert(params.get("isRecommend"), Integer.class));
    Integer status = ConvertUtil.convert(params.get("status"), Integer.class);
    if(status != null){
      builder.addWhereEq("n", "status",status);
    }else{
      builder.addWhereNotEq("n", "status", 2);
    }
    builder.addWhereLike("n", "title", params.get("title"));
    builder.addWhereRightLike("n", "the_source", params.get("theSource"));
    builder.addWhereIn("n", "type_id", SetsHelper.toStringSets(params.get("typeId"), ","));
    builder.addWhereIn("nt", "type_code", SetsHelper.toStringSets(params.get("typeCode"), ","));
    builder.addWhereIn("nt", "group_name", SetsHelper.toStringSets(params.get("typeGroupName"), ","));
    Integer regionNews = ConvertUtil.convert(params.get("regionNews"), Integer.class);
    if(regionNews != null){
      builder.addWhere("AND "+(regionNews == 1?"":"NOT")
                         +" EXISTS (SELECT 1 FROM mjc_region_ref rr WHERE rr.ref_id = n.id)", "n");
    }
    DateTime start = ConvertUtil.dateNullable(params.get("startTime"), "开始时间", Dates.DATE_TIME_FORMATTER);
    builder.addWhereGeEq("n", "create_time", start == null ? null : start.toDate(), "startTime");
    DateTime end = ConvertUtil.dateNullable(params.get("endTime"), "截止时间", Dates.DATE_TIME_FORMATTER);
    builder.addWhereLeEq("n", "create_time", end == null ? null : end.toDate(), "endTime");

    Set<String> tags = SetsHelper.toStringSets(params.get("tagsId"), ",");
    Set<String> tagsCode = SetsHelper.toStringSets(params.get("tagsCode"), ",");
    if(!tags.isEmpty() || !tagsCode.isEmpty()){
      builder.addWhere("AND EXISTS (SELECT 1 FROM mjc_tags_ref tr LEFT JOIN mjc_tags t ON t.id = tr.tags_id WHERE tr.ref_id=n.id "
                         + (!tags.isEmpty()?" AND tr.tags_id IN :tagsId":"")
                         + (!tagsCode.isEmpty()?" AND t.tag_code IN :tagsCode":"")
                         + ")", "n");
      if(!tags.isEmpty()){
        builder.setParameter("tagsId", tags, "n");
      }
      if(!tagsCode.isEmpty()){
        builder.setParameter("tagsCode", tagsCode, "n");
      }
    }
    Set<String> regionId = SetsHelper.toStringSets(params.get("regionId"), ",");
    Set<String> regionName = SetsHelper.toStringSets(params.get("regionName"), ",");
    if(!regionId.isEmpty() || !regionName.isEmpty()){
      builder.addWhere("AND EXISTS (SELECT 1 FROM mjc_region_ref rr WHERE rr.ref_id=n.id "
                         + (!regionId.isEmpty()?" AND rr.region_id IN :regionId":"")
                         + (!regionName.isEmpty()?" AND rr.region_name IN :regionName":"")
                         + ")", "n");
      if(!regionId.isEmpty()){
        builder.setParameter("regionId", regionId, "n");
      }
      if(!regionName.isEmpty()){
        builder.setParameter("regionName", regionName, "n");
      }
    }
    String searchKey = params.get("searchKey");
    if (StringUtils.isNotBlank(searchKey)) {
      builder.addWhere(" AND MATCH (n.`title`, n.`content`, n.`summary`, n.`author_name`, n.`keyword`) AGAINST (:searchKey) ", "searchKey", searchKey);
    }
    builder.orderBy("n.publish_time DESC");
    Map<String, Map<String, Object>> rows = Maps.newHashMap();
    DataPage<Map<String, Object>> page = builder.toPage(getEntityManager()).resultConsumer(row -> {
      rows.put(row.get("id").toString(), row);
    }).query(params);
    if(!rows.isEmpty()){
      JpaHelper.setMapResult(getEntityManager().createNativeQuery(
        "SELECT n.id, "
          + "GROUP_CONCAT( DISTINCT t.id ) tagsId, GROUP_CONCAT( DISTINCT t.tag_code ) tagCode, "
          + "GROUP_CONCAT( DISTINCT t.tag_name ) tags, "
          + "GROUP_CONCAT( DISTINCT rr.region_id ORDER BY rr.region_id ) regionId, "
          + "GROUP_CONCAT( DISTINCT rr.region_name ORDER BY rr.region_id ) regionName "
          + "FROM base_news n "
          + "LEFT JOIN mjc_tags_ref tr ON n.id = tr.ref_id LEFT JOIN mjc_tags t ON t.id = tr.tags_id "
          + "LEFT JOIN mjc_region_ref rr ON rr.ref_id = n.id WHERE n.id IN :ids GROUP BY n.id")
                        .setParameter("ids", rows.keySet()), map->{
        Map<String, Object> row = rows.get(map.get("id"));
        if(row != null){
          row.putAll(map);
        }
      }).getResultList();
    }
    return page;
  }
}

