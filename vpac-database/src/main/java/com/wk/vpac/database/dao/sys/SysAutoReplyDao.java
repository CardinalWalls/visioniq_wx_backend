package com.wk.vpac.database.dao.sys;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import com.wk.vpac.domain.sys.SysAutoReply;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * SysAutoReply DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Repository
public interface SysAutoReplyDao extends GenericJpaDao<SysAutoReply, String> {

  default DataPage<RowMap> page(PageParamMap params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_sys_auto_reply", "t");
    sqlBuilder.select(findColumns(SysAutoReply.class, "t"));
    sqlBuilder.addWhere("AND MATCH (t.keyword) AGAINST (:keyword)", "keyword", params.get("keyword"));
    return sqlBuilder.orderBy("t.sort_no, t.id").toRowPage(getEntityManager()).query(params);
  }


  List<SysAutoReply> findByTypeInOrderBySortNoAscIdAsc(Set<Integer> type);

  @Query(value = "SELECT t.* FROM wk_sys_auto_reply t WHERE t.keyword != '' AND MATCH (t.keyword) AGAINST (:message) AND t.type IN :types "
    + "AND CHAR_LENGTH(:message) <= t.match_length_max ORDER BY MATCH (t.keyword) AGAINST (:message) DESC", nativeQuery = true)
  List<SysAutoReply> matchKeyword(@Param("message") String message, @Param("types") Set<Integer> types);
}

