package com.wk.vpac.database.dao.sys;


import com.base.components.common.dto.page.DataPage;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import com.wk.vpac.domain.sys.KeywordLink;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * KeywordLink DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2022-09-29
 */
@Repository
public interface KeywordLinkDao extends GenericJpaDao<KeywordLink, String> {

  default DataPage page(Map<String, String> params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("base_keyword_link", "t");
    sqlBuilder.select(findColumns(KeywordLink.class, "t"));
    return sqlBuilder.orderBy("t.id").toPage(getEntityManager()).query(params);
  }
}

