

package com.wk.vpac.database.dao.msgqueue;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.JpaHelper;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.wk.vpac.domain.msgqueue.SysMessageEvent;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * SysMessageEvent DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2017-11-15
 */
@Repository
public interface SysMessageEventDao extends GenericJpaDao<SysMessageEvent, String> {

  default DataPage<Map> page(Map<String, String> params){
    DateTime startDate = ConvertUtil.dateNullable(params.get("startDate"), "开始日期", Dates.DATE_FORMATTER);
    DateTime endDate = ConvertUtil.dateNullable(params.get("endDate"), "结束日期", Dates.DATE_FORMATTER);
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    sqlBuilder.addWhereGeEq("s", "create_time", startDate == null ? null : startDate.toDate(), true, "startDate");
    sqlBuilder.addWhereLeEq("s", "create_time", endDate == null ? null : endDate.toDate(), true, "endDate");
    String having = "";
    Integer status = ConvertUtil.convert(params.get("hasDone"), Integer.class);
    if(status != null){
      if(status == Integer.MIN_VALUE){
        sqlBuilder.addWhereIsNull("sh", "id");
      }
      else{
        having = "locate(CONCAT(:symbol, :status, :symbol), handleStatus) > 0";
        sqlBuilder.setParameter("status", status);
      }
    }
    sqlBuilder.addWhereRightLike("s", "channel", params.get("channel"));
    sqlBuilder.setParameter("symbol", "'");
    sqlBuilder.having(having);
    String querySql = "SELECT " + JpaHelper.findColumns(SysMessageEvent.class, "s") + ", GROUP_CONCAT(CONCAT(:symbol, sh.has_done, :symbol) ORDER BY sh.has_done) handleStatus "
      + "FROM sys_message_event s LEFT JOIN sys_message_event_handle sh ON sh.event_id = s.id WHERE 1=1";
    String countSql = "SELECT GROUP_CONCAT(CONCAT(:symbol, sh.has_done, :symbol) ORDER BY sh.has_done) handleStatus FROM sys_message_event s "
      + "LEFT JOIN sys_message_event_handle sh ON sh.event_id = s.id WHERE 1=1";
    jakarta.persistence.Query count = NativeSQLBuilder.defaultAnd("SELECT COUNT(1) FROM (%s) t").buildFrom(
      JpaHelper.getEntityManager(), sqlBuilder.bindQuerySql(countSql).groupBy("s.id").toReplaceMap("%s"));
    jakarta.persistence.Query query = JpaHelper.setMapResult(
      sqlBuilder.bindQuerySql(querySql).orderBy("s.create_time desc").groupBy("s.id").build(JpaHelper.getEntityManager()));
    return JpaHelper.pageByQuery(count, query, params);
  }

  @SuppressWarnings("unchecked")
  default List<String> findIds(Map<String, String> params){
    DateTime startDate = ConvertUtil.dateNullable(params.get("startDate"), "开始日期", Dates.DATE_FORMATTER);
    DateTime endDate = ConvertUtil.dateNullable(params.get("endDate"), "结束日期", Dates.DATE_FORMATTER);
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    sqlBuilder.addWhereGeEq("s", "create_time", startDate == null ? null : startDate.toDate(), true, "startDate");
    sqlBuilder.addWhereLeEq("s", "create_time", endDate == null ? null : endDate.toDate(), true, "endDate");
    Integer status = ConvertUtil.convert(params.get("hasDone"), Integer.class);
    if(status != null){
      if(status == Integer.MIN_VALUE){
        sqlBuilder.addWhereIsNull("sh", "id");
      }
      else{
        sqlBuilder.addWhereEq("sh", "has_done", status);
      }
    }
    sqlBuilder.addWhereEq("s", "channel", params.get("channel"));
    String querySql = "SELECT s.id FROM sys_message_event s LEFT JOIN sys_message_event_handle sh ON sh.event_id = s.id WHERE 1=1";
    return sqlBuilder.bindQuerySql(querySql).groupBy("s.id").build(JpaHelper.getEntityManager()).getResultList();
  }
}

