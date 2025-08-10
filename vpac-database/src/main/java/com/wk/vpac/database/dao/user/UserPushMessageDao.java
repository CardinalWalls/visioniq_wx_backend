package com.wk.vpac.database.dao.user;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import com.wk.vpac.domain.user.UserPushMessage;
import org.springframework.stereotype.Repository;


/**
 * UserPushMessage DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Repository
public interface UserPushMessageDao extends GenericJpaDao<UserPushMessage, String> {

  default DataPage<RowMap> page(PageParamMap params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_user_push_message", "t")
      .joinTable("LEFT JOIN wk_user_push_message_type tp ON tp.id = t.type_id", "tp", "t");
    sqlBuilder.select(findColumns(UserPushMessage.class, "t")+", tp.name typeName");
    sqlBuilder.addWhereLike("t", "content", params.getStrTrimOrEmpty("content"));
    sqlBuilder.addWhereEq("t", "type_id", params.get("typeId"));
    return sqlBuilder.orderBy("t.id").toRowPage(getEntityManager()).query(params);
  }

  boolean existsByTypeId(String typeId);
}

