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
 * 短信模板
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_sms_template")
@DynamicUpdate
public class SmsTemplate implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** tpl_name - tpl_name */
  @Column(name = "tpl_name")
  @Param("tpl_name")
  private String tplName;

  /** type_name - type_name */
  @Column(name = "type_name")
  @Param("type_name")
  private String typeName;

  /** content - content */
  @Column(name = "content")
  @Param("content")
  private String content;

  /** ali_sms_id - ali_sms_id */
  @Column(name = "ali_sms_id")
  @Param("ali_sms_id")
  private String aliSmsId;

  /** status - 发送状态; 0、未发送 1、已发送 */
  @Column(name = "status", nullable = false)
  @Param("发送状态; 0、未发送 1、已发送")
  private Integer status = 0;

  /** create_time - create_time */
  @Column(name = "create_time")
  @Param("create_time")
  private java.util.Date createTime;

  
  public String getId() {
    return this.id;
  }

  public String getTplName() {
    return tplName;
  }

  public String getTypeName() {
    return typeName;
  }

  public String getContent() {
    return content;
  }

  public String getAliSmsId() {
    return aliSmsId;
  }

  public Integer getStatus() {
    return status;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setTplName(String tplName) {
    this.tplName = tplName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setAliSmsId(String aliSmsId) {
    this.aliSmsId = aliSmsId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

}