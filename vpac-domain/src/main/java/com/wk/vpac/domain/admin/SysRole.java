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
 * 后台权限角色
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "admin_sys_role")
@DynamicUpdate
public class SysRole implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** name - name */
  @Column(name = "name")
  @Param("name")
  private String name;

  /** order_no - order_no */
  @Column(name = "order_no", nullable = false)
  @Param("order_no")
  private Integer orderNo = 1;

  /** description - description */
  @Column(name = "description")
  @Param("description")
  private String description;

  /** code - code */
  @Column(name = "code")
  @Param("code")
  private String code;

  /** status - 启用状态; 0、禁用，1、启用 */
  @Column(name = "status", nullable = false)
  @Param("启用状态; 0、禁用，1、启用")
  private Integer status = 1;

  
  public String getId() {
    return this.id;
  }

  public String getName() {
    return name;
  }

  public Integer getOrderNo() {
    return orderNo;
  }

  public String getDescription() {
    return description;
  }

  public String getCode() {
    return code;
  }

  public Integer getStatus() {
    return status;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOrderNo(Integer orderNo) {
    this.orderNo = orderNo;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}