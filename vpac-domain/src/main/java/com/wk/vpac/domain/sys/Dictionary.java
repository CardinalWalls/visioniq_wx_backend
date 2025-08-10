package com.wk.vpac.domain.sys;

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
 * 数据字典
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_dictionary")
@DynamicUpdate
public class Dictionary implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** dict_code - dict_code */
  @Column(name = "dict_code")
  @Param("dict_code")
  private String dictCode;

  /** dict_name - dict_name */
  @Column(name = "dict_name")
  @Param("dict_name")
  private String dictName;

  /** remarks - remarks */
  @Column(name = "remarks")
  @Param("remarks")
  private String remarks;

  /** data_type - data_type */
  @Column(name = "data_type")
  @Param("data_type")
  private String dataType;

  /** order_no - order_no */
  @Column(name = "order_no")
  @Param("order_no")
  private Integer orderNo = 1;

  /** status - 0、禁用 1、启用 */
  @Column(name = "status", nullable = false)
  @Param("0、禁用 1、启用")
  private Integer status = 0;

  /** can_edit - 0、不可编辑 1、可编辑 */
  @Column(name = "can_edit", nullable = false)
  @Param("0、不可编辑 1、可编辑")
  private Integer canEdit = 1;

  
  public String getId() {
    return this.id;
  }

  public String getDictCode() {
    return dictCode;
  }

  public String getDictName() {
    return dictName;
  }

  public String getRemarks() {
    return remarks;
  }

  public String getDataType() {
    return dataType;
  }

  public Integer getOrderNo() {
    return orderNo;
  }

  public Integer getStatus() {
    return status;
  }

  public Integer getCanEdit() {
    return canEdit;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setDictCode(String dictCode) {
    this.dictCode = dictCode;
  }

  public void setDictName(String dictName) {
    this.dictName = dictName;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public void setOrderNo(Integer orderNo) {
    this.orderNo = orderNo;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setCanEdit(Integer canEdit) {
    this.canEdit = canEdit;
  }

}