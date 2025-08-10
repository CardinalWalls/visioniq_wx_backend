package com.wk.vpac.domain.user;

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
 * 基础用户类别
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_user_base_type")
@DynamicUpdate
public class UserBaseType implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** user_type - user_type */
  @Column(name = "user_type", nullable = false)
  @Param("user_type")
  private Integer userType = 0;

  /** user_type_name - user_type_name */
  @Column(name = "user_type_name")
  @Param("user_type_name")
  private String userTypeName;

  /** remark - remark */
  @Column(name = "remark")
  @Param("remark")
  private String remark;

  
  public String getId() {
    return this.id;
  }

  public Integer getUserType() {
    return userType;
  }

  public String getUserTypeName() {
    return userTypeName;
  }

  public String getRemark() {
    return remark;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setUserType(Integer userType) {
    this.userType = userType;
  }

  public void setUserTypeName(String userTypeName) {
    this.userTypeName = userTypeName;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}