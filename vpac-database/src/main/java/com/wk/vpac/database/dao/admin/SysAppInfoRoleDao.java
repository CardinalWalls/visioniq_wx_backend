

package com.wk.vpac.database.dao.admin;

import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.wk.vpac.domain.admin.SysAppInfoRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * SysAppInfoRole DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-04-03
 */
@Repository
public interface SysAppInfoRoleDao extends GenericJpaDao<SysAppInfoRole, String> {

  @Query(value = "SELECT ar.role_id roleId, a.id id, a.`name` `name`, a.description description, a.target_url targetUrl, a.params_json paramsJson, a.auth_url_prefix authUrlPrefix, a.icon_class iconClass, a.`type` `type`, a.read_write_type readWriteType, a.parent_id parentId, a.left_val leftVal, a.right_val rightVal, a.`status` "
    + "FROM admin_sys_app_info a LEFT JOIN admin_sys_app_info_role ar ON ar.app_info_id = a.id AND ar.role_id in (:roleIds) ORDER BY a.left_val", nativeQuery = true)
  List<Map<String,Object>> findAllAppInfoJoinRole(@Param("roleIds")Collection<String> roleIds);

  int deleteByRoleId(String roleId);
}

