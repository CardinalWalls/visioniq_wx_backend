package com.wk.vpac.database.dao.user;

import com.base.components.database.jpa.util.SelectPageHelper;
import com.wk.vpac.domain.user.UserPushMessageType;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import org.springframework.stereotype.Repository;


/**
 * UserPushMessageType DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2024-04-23
 */
@Repository
public interface UserPushMessageTypeDao extends GenericJpaDao<UserPushMessageType, String> {

  default DataPage<RowMap> page(PageParamMap params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_user_push_message_type", "t");
    sqlBuilder.select(findColumns(UserPushMessageType.class, "t"));
    sqlBuilder.addWhereEq("t", "valid", params.getBoolean("valid"));
    sqlBuilder.addWhereEq("t", "name", params.get("name"));
    SelectPageHelper.query(sqlBuilder, params, "t", "id", "name");
    return sqlBuilder.orderBy("t.sort_no, t.id").toRowPage(getEntityManager()).query(params);
  }

}

