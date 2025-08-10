package com.wk.vpac.domain.operations;

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
 * 广告分组
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_advertisement_group")
@DynamicUpdate
public class AdvertisementGroup implements Domain {  
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

  /** create_time - create_time */
  @Column(name = "create_time")
  @Param("create_time")
  private java.util.Date createTime;

  /** status - status */
  @Column(name = "status", nullable = false)
  @Param("status")
  private Integer status = 0;

  /** type_code - 类型编号 */
  @Column(name = "type_code", nullable = false)
  @Param(value = "类型编号", required = true)
  private String typeCode = "";
  
  public String getId() {
    return this.id;
  }

  public String getName() {
    return name;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public Integer getStatus() {
    return status;
  }

  public String getTypeCode() {
    return typeCode;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setTypeCode(String typeCode) {
    this.typeCode = typeCode;
  }
}