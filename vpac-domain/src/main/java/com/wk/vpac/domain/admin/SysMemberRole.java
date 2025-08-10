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
 * 后台用户关联角色权限
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "admin_sys_member_role")
@DynamicUpdate
public class SysMemberRole implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** member_id - member_id */
  @Column(name = "member_id")
  @Param("member_id")
  private String memberId;

  /** role_id - role_id */
  @Column(name = "role_id")
  @Param("role_id")
  private String roleId;

  
  public String getId() {
    return this.id;
  }

  public String getMemberId() {
    return memberId;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setMemberId(String memberId) {
    this.memberId = memberId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

}