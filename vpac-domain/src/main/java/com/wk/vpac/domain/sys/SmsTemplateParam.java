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
 * 短信模板参数
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_sms_template_param")
@DynamicUpdate
public class SmsTemplateParam implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** template_id - template_id */
  @Column(name = "template_id")
  @Param("template_id")
  private String templateId;

  /** param_code - param_code */
  @Column(name = "param_code")
  @Param("param_code")
  private String paramCode;

  /** param_name - param_name */
  @Column(name = "param_name")
  @Param("param_name")
  private String paramName;

  /** type - type */
  @Column(name = "type", nullable = false)
  @Param("type")
  private Integer type;

  /** sys_param_type - sys_param_type */
  @Column(name = "sys_param_type")
  @Param("sys_param_type")
  private String sysParamType;

  
  public String getId() {
    return this.id;
  }

  public String getTemplateId() {
    return templateId;
  }

  public String getParamCode() {
    return paramCode;
  }

  public String getParamName() {
    return paramName;
  }

  public Integer getType() {
    return type;
  }

  public String getSysParamType() {
    return sysParamType;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public void setParamCode(String paramCode) {
    this.paramCode = paramCode;
  }

  public void setParamName(String paramName) {
    this.paramName = paramName;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public void setSysParamType(String sysParamType) {
    this.sysParamType = sysParamType;
  }

}