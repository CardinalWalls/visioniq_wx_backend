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
 * 短信发送批次
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_sms")
@DynamicUpdate
public class Sms implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** remarks - remarks */
  @Column(name = "remarks")
  @Param("remarks")
  private String remarks;

  /** create_time - create_time */
  @Column(name = "create_time")
  @Param("create_time")
  private java.util.Date createTime;

  /** author_id - author_id */
  @Column(name = "author_id")
  @Param("author_id")
  private String authorId;

  /** status - 发送状态; 0、未发送 1、已发送 2、未完成 */
  @Column(name = "status", nullable = false)
  @Param("发送状态; 0、未发送 1、已发送 2、未完成")
  private Integer status = 0;

  /** send_time - send_time */
  @Column(name = "send_time")
  @Param("send_time")
  private java.util.Date sendTime;

  /** type - 0 系统发送，1 后台管理发送，template_id 短信模板id */
  @Column(name = "type", nullable = false)
  @Param("0 系统发送，1 后台管理发送，template_id 短信模板id")
  private Integer type = 0;

  /** template_id - template_id */
  @Column(name = "template_id")
  @Param("template_id")
  private String templateId;

  /** batch - batch */
  @Column(name = "batch")
  @Param("batch")
  private String batch;

  /** param_json - param_json */
  @Column(name = "param_json")
  @Param("param_json")
  private String paramJson;

  /** sms_channel - 短信通道 */
  @Column(name = "sms_channel")
  @Param("短信通道")
  private Integer smsChannel;

  /** user_filter - user_filter */
  @Column(name = "user_filter")
  @Param("user_filter")
  private String userFilter;

  /** sms_send_mode - 短信发送模式，0 系统用户，1 只输手机号 */
  @Column(name = "sms_send_mode")
  @Param("短信发送模式，0 系统用户，1 只输手机号")
  private Integer smsSendMode;

  
  public String getId() {
    return this.id;
  }

  public String getRemarks() {
    return remarks;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public String getAuthorId() {
    return authorId;
  }

  public Integer getStatus() {
    return status;
  }

  public java.util.Date getSendTime() {
    return sendTime;
  }

  public Integer getType() {
    return type;
  }

  public String getTemplateId() {
    return templateId;
  }

  public String getBatch() {
    return batch;
  }

  public String getParamJson() {
    return paramJson;
  }

  public Integer getSmsChannel() {
    return smsChannel;
  }

  public String getUserFilter() {
    return userFilter;
  }

  public Integer getSmsSendMode() {
    return smsSendMode;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setAuthorId(String authorId) {
    this.authorId = authorId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setSendTime(java.util.Date sendTime) {
    this.sendTime = sendTime;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public void setBatch(String batch) {
    this.batch = batch;
  }

  public void setParamJson(String paramJson) {
    this.paramJson = paramJson;
  }

  public void setSmsChannel(Integer smsChannel) {
    this.smsChannel = smsChannel;
  }

  public void setUserFilter(String userFilter) {
    this.userFilter = userFilter;
  }

  public void setSmsSendMode(Integer smsSendMode) {
    this.smsSendMode = smsSendMode;
  }

}