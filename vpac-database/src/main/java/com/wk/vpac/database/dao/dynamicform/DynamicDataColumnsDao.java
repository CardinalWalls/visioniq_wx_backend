package com.wk.vpac.database.dao.dynamicform;

import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.wk.vpac.domain.dynamicform.DynamicDataColumns;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * DynamicDataColumns DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-20
 */
@Repository
public interface DynamicDataColumnsDao extends GenericJpaDao<DynamicDataColumns, String> {

  DynamicDataColumns findByCode(@Param("code") String code);
}

