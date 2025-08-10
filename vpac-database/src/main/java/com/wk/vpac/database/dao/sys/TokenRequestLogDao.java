package com.wk.vpac.database.dao.sys;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.constants.sys.PageSort;
import com.base.components.common.constants.sys.SortBuilder;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.JpaHelper;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.google.common.collect.ImmutableMap;
import com.wk.vpac.domain.sys.TokenRequestLog;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * TokenRequestLog DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-09-02
 */
@Repository
public interface TokenRequestLogDao extends GenericJpaDao<TokenRequestLog, String> {

  default DataPage page(Map<String, String> params){
    String querySql = "SELECT " + JpaHelper.findColumns(TokenRequestLog.class, "t") + ", l.token_obj_name tokenObjName, "
      + "CASE WHEN l.login_location = t.ip_location THEN 1 ELSE 0 END sameIp FROM sys_token_request_log t "
      + "LEFT JOIN sys_token_login_log l ON l.token = t.token WHERE 1=1";
    String countSql = "SELECT COUNT(1) FROM sys_token_request_log t LEFT JOIN sys_token_login_log l ON l.token = t.token WHERE 1=1";
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    sqlBuilder.addWhereEq("t", "token", params.get("token"));
    sqlBuilder.addWhereEq("t", "method", params.get("method"));
    sqlBuilder.addWhereEq("t", "ip", params.get("ip"));
    sqlBuilder.addWhereRightLike("t", "uri", params.get("uri"));

    DateTime start = ConvertUtil.dateNullable(params.get("startTime"), "开始时间", Dates.DATE_TIME_FORMATTER);
    if(start != null){
      sqlBuilder.addWhereGe("t", "create_time", start.toDate(), "startTime");
    }
    DateTime end = ConvertUtil.dateNullable(params.get("endTime"), "结束时间", Dates.DATE_TIME_FORMATTER);
    if(end != null){
      sqlBuilder.addWhereLe("t", "create_time", end.toDate(), "endTime");
    }
    Boolean sameIp = ConvertUtil.convert(params.get("sameIp"), Boolean.class);
    if(sameIp != null){
      sqlBuilder.addWhere("AND t.ip_location " + (sameIp?"=":"<>") + " l.login_location");
    }
    sqlBuilder.addWhere("t", "params", params.get("params"), ConditionEnum.OPERATE_LIKE, true);
    sqlBuilder.addWhere("t", "server_host", params.get("serverHost"), ConditionEnum.OPERATE_LIKE, true);
    sqlBuilder.addWhere("t", "ip_location", params.get("ipLocation"), ConditionEnum.OPERATE_LIKE, true);
    PageSort sort = SortBuilder.by("uri, apiName, createTime", "createTime DESC").build(params);
    jakarta.persistence.Query count = sqlBuilder.bindQuerySql(countSql).build(JpaHelper.getEntityManager());
    jakarta.persistence.Query query = JpaHelper.setMapResult(
      sqlBuilder.bindQuerySql(querySql).orderBy(sort.toCamelCase()).build(JpaHelper.getEntityManager()));
    return JpaHelper.pageByQuery(count, query, params).setSortField(sort.toCamelCase());
  }

  default DataPage group(Map<String, String> params){
    String querySql = "SELECT t.uri, t.method, t.api_name apiName, COUNT(t.uri) requestCount, "
      + "SUM(CASE WHEN l.login_location <> t.ip_location THEN 1 ELSE 0 END) errorIp FROM sys_token_request_log t "
      + "LEFT JOIN sys_token_login_log l ON l.token = t.token WHERE 1=1";
    String countSql = "SELECT 1 FROM sys_token_request_log t LEFT JOIN sys_token_login_log l ON l.token = t.token WHERE 1=1";
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    sqlBuilder.addWhereEq("t", "token", params.get("token"));
    sqlBuilder.addWhereEq("t", "method", params.get("method"));
    sqlBuilder.addWhereEq("t", "ip", params.get("ip"));
    sqlBuilder.addWhereRightLike("t", "uri", params.get("uri"));

    DateTime start = ConvertUtil.dateNullable(params.get("startTime"), "开始时间", Dates.DATE_TIME_FORMATTER);
    if(start != null){
      sqlBuilder.addWhereGe("t", "create_time", start.toDate(), "startTime");
    }
    DateTime end = ConvertUtil.dateNullable(params.get("endTime"), "结束时间", Dates.DATE_TIME_FORMATTER);
    if(end != null){
      sqlBuilder.addWhereLe("t", "create_time", end.toDate(), "endTime");
    }
    Boolean sameIp = ConvertUtil.convert(params.get("sameIp"), Boolean.class);
    if(sameIp != null){
      sqlBuilder.addWhere("AND t.ip_location " + (sameIp?"=":"<>") + " l.login_location");
    }
    sqlBuilder.addWhere("t", "params", params.get("params"), ConditionEnum.OPERATE_LIKE, true);
    sqlBuilder.addWhere("t", "server_host", params.get("serverHost"), ConditionEnum.OPERATE_LIKE, true);
    sqlBuilder.addWhere("t", "ip_location", params.get("ipLocation"), ConditionEnum.OPERATE_LIKE, true);

    jakarta.persistence.Query count = new NativeSQLBuilder().bindQuerySql("SELECT COUNT(1) FROM (_COUNT_SQL_) t").buildFrom(
      JpaHelper.getEntityManager(), ImmutableMap.of("_COUNT_SQL_", sqlBuilder.bindQuerySql(countSql).groupBy("t.method, t.uri")));
    PageSort sort = SortBuilder.by("requestCount, uri, errorIp, apiName", "requestCount DESC").build(params);
    jakarta.persistence.Query query = JpaHelper.setMapResult(
      sqlBuilder.bindQuerySql(querySql).orderBy(sort.toCamelCase()).groupBy("t.method, t.uri").build(JpaHelper.getEntityManager()));
    return JpaHelper.pageByQuery(count, query, params).setSortField(sort.toCamelCase());
  }

  @Modifying
  @Query(value = "DELETE FROM sys_token_request_log WHERE "
    + "NOT EXISTS ( SELECT 1 FROM sys_token_login_log l WHERE l.token = sys_token_request_log.token )", nativeQuery = true)
  int deleteWithoutLoginRecord();

}

