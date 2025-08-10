package com.wk.vpac.database.dao.sys;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.JpaHelper;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.wk.vpac.domain.sys.TokenLoginLog;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;

/**
 * TokenLoginLog DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-09-02
 */
@Repository
public interface TokenLoginLogDao extends GenericJpaDao<TokenLoginLog, String> {

  default DataPage page(Map<String, String> params){
    String querySql = "SELECT " + JpaHelper.findColumns(TokenLoginLog.class, "t") + ", COUNT(r.id) requestCount FROM sys_token_login_log t "
      + "LEFT JOIN sys_token_request_log r ON r.token = t.token WHERE 1=1";
    String countSql = "SELECT COUNT(1) FROM sys_token_login_log t WHERE 1=1";
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    sqlBuilder.addWhereEq("t", "user_id", params.get("userId"));
    sqlBuilder.addWhereEq("t", "token", params.get("token"));
    sqlBuilder.addWhereEq("t", "token_type", params.get("tokenType"));
    sqlBuilder.addWhereEq("t", "login_ip", params.get("loginIp"));
    DateTime start = ConvertUtil.dateNullable(params.get("startTime"), "开始时间", Dates.DATE_TIME_FORMATTER);
    if(start != null){
      sqlBuilder.addWhereGe("t", "create_time", start.toDate(), "startTime");
    }
    DateTime end = ConvertUtil.dateNullable(params.get("endTime"), "结束时间", Dates.DATE_TIME_FORMATTER);
    if(end != null){
      sqlBuilder.addWhereLe("t", "create_time", end.toDate(), "endTime");
    }
    sqlBuilder.addWhere("t", "login_location", params.get("loginLocation"), ConditionEnum.OPERATE_LIKE, true);
    sqlBuilder.addWhere("t", "token_obj_json", params.get("tokenObjJson"), ConditionEnum.OPERATE_LIKE, true);
    sqlBuilder.addWhere("t", "server_host", params.get("serverHost"), ConditionEnum.OPERATE_LIKE, true);
    jakarta.persistence.Query count = sqlBuilder.bindQuerySql(countSql).build(JpaHelper.getEntityManager());
    jakarta.persistence.Query query = JpaHelper.setMapResult(
      sqlBuilder.bindQuerySql(querySql).groupBy("t.id").orderBy("t.create_time DESC").build(JpaHelper.getEntityManager()));
    return JpaHelper.pageByQuery(count, query, params);
  }

  @Modifying
  @Query(value = "DELETE FROM sys_token_login_log WHERE create_time < :deleteLine", nativeQuery = true)
  int deleteByLastTime(@Param("deleteLine") Date deleteLine);
}

