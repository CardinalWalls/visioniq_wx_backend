package com.wk.vpac.database.dao.dynamicform;

import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.wk.vpac.domain.dynamicform.DynamicDataChild;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DynamicDataChild DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-20
 */
@Repository
public interface DynamicDataChildDao extends GenericJpaDao<DynamicDataChild, String> {

  @Query(value = "select type from base_dynamic_data_child where code = :code group by type", nativeQuery = true)
  List<String> groupTypeByCode(@Param("code") String code);

  Optional<DynamicDataChild> findFirstByCodeAndType(@Param("code") String code, @Param("type") String type);
}

