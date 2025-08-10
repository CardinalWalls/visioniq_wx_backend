package com.wk.vpac.domain.dynamicform;

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
 * 动态表单数据关联
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_dynamic_data_child")
@DynamicUpdate
public class DynamicDataChild implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** ref_id - ref_id */
  @Column(name = "ref_id")
  @Param("ref_id")
  private String refId;

  /** type - type */
  @Column(name = "type")
  @Param("type")
  private String type;

  /** json_data - json_data */
  @Column(name = "json_data")
  @Param("json_data")
  private String jsonData;

  /** status - status */
  @Column(name = "status", nullable = false)
  @Param("status")
  private Integer status;

  /** code - code */
  @Column(name = "code")
  @Param("code")
  private String code;

  
  public String getId() {
    return this.id;
  }

  public String getRefId() {
    return refId;
  }

  public String getType() {
    return type;
  }

  public String getJsonData() {
    return jsonData;
  }

  public Integer getStatus() {
    return status;
  }

  public String getCode() {
    return code;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setRefId(String refId) {
    this.refId = refId;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setJsonData(String jsonData) {
    this.jsonData = jsonData;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setCode(String code) {
    this.code = code;
  }

}