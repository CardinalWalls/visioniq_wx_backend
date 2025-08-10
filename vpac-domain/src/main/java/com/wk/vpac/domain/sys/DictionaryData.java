package com.wk.vpac.domain.sys;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.dto.dictionary.DictionaryNode;
import com.base.components.common.id.jpa.JpaId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;



/**
 * 数据字典明细
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_dictionary_data")
@DynamicUpdate
public class DictionaryData implements DictionaryNode, Domain {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** dict_id - dict_id */
  @Column(name = "dict_id")
  @Param("dict_id")
  private String dictId;

  /** data_value - data_value */
  @Column(name = "data_value")
  @Param("data_value")
  private String dataValue;

  /** data_key - data_key */
  @Column(name = "data_key")
  @Param("data_key")
  private String dataKey;

  /** data_name - data_name */
  @Column(name = "data_name")
  @Param("data_name")
  private String dataName;

  /** order_no - order_no */
  @Column(name = "order_no", nullable = false)
  @Param("order_no")
  private Integer orderNo;

  /** remarks - remarks */
  @Column(name = "remarks")
  @Param("remarks")
  private String remarks;

  /** status - status */
  @Column(name = "status", nullable = false)
  @Param("status")
  private Integer status;

  
  public String getId() {
    return this.id;
  }

  public String getDictId() {
    return dictId;
  }

  @Override
  public String getDataValue() {
    return dataValue;
  }

  @Override
  public String getDataKey() {
    return dataKey;
  }

  public String getDataName() {
    return dataName;
  }

  public Integer getOrderNo() {
    return orderNo;
  }

  public String getRemarks() {
    return remarks;
  }

  public Integer getStatus() {
    return status;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setDictId(String dictId) {
    this.dictId = dictId;
  }

  public void setDataValue(String dataValue) {
    this.dataValue = dataValue;
  }

  public void setDataKey(String dataKey) {
    this.dataKey = dataKey;
  }

  public void setDataName(String dataName) {
    this.dataName = dataName;
  }

  public void setOrderNo(Integer orderNo) {
    this.orderNo = orderNo;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}