package com.wk.vpac.database.dao.sys;

import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.wk.vpac.domain.sys.RegionTiny;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * RegionTiny DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-02-26
 */
@Repository
public interface RegionTinyDao extends GenericJpaDao<RegionTiny, String> {

  @Query(value = "select id, region_id parentId, name from base_region_tiny", nativeQuery = true)
  List<Map> allTinyBycitySelect();
}

