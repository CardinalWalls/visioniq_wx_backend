package com.wk.vpac.domain.admin;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.id.jpa.JpaId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;



/**
 * 后台应用关联角色权限
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "admin_sys_app_info_role")
@DynamicUpdate
public class SysAppInfoRole implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** app_info_id - app_info_id */
  @Column(name = "app_info_id")
  @Param("app_info_id")
  private String appInfoId;

  /** role_id - role_id */
  @Column(name = "role_id")
  @Param("role_id")
  private String roleId;

  
  public String getId() {
    return this.id;
  }

  public String getAppInfoId() {
    return appInfoId;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setAppInfoId(String appInfoId) {
    this.appInfoId = appInfoId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

}