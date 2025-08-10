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
 * 短信发送记录
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_sms_send_record")
@DynamicUpdate
public class SmsSendRecord implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** sms_id - sms_id */
  @Column(name = "sms_id")
  @Param("sms_id")
  private String smsId;

  /** template_id - template_id */
  @Column(name = "template_id")
  @Param("template_id")
  private String templateId;

  /** template_name - template_name */
  @Column(name = "template_name")
  @Param("template_name")
  private String templateName;

  /** user_id - user_id */
  @Column(name = "user_id")
  @Param("user_id")
  private String userId;

  /** phone - phone */
  @Column(name = "phone")
  @Param("phone")
  private String phone;

  /** status - status */
  @Column(name = "status", nullable = false)
  @Param("status")
  private Integer status;

  /** send_time - send_time */
  @Column(name = "send_time")
  @Param("send_time")
  private java.util.Date sendTime;

  /** create_time - create_time */
  @Column(name = "create_time")
  @Param("create_time")
  private java.util.Date createTime;

  /** batch - batch */
  @Column(name = "batch")
  @Param("batch")
  private String batch;

  /** user_name - user_name */
  @Column(name = "user_name")
  @Param("user_name")
  private String userName;

  /** remarks - remarks */
  @Column(name = "remarks")
  @Param("remarks")
  private String remarks;

  
  public String getId() {
    return this.id;
  }

  public String getSmsId() {
    return smsId;
  }

  public String getTemplateId() {
    return templateId;
  }

  public String getTemplateName() {
    return templateName;
  }

  public String getUserId() {
    return userId;
  }

  public String getPhone() {
    return phone;
  }

  public Integer getStatus() {
    return status;
  }

  public java.util.Date getSendTime() {
    return sendTime;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public String getBatch() {
    return batch;
  }

  public String getUserName() {
    return userName;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setSmsId(String smsId) {
    this.smsId = smsId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setSendTime(java.util.Date sendTime) {
    this.sendTime = sendTime;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setBatch(String batch) {
    this.batch = batch;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

}