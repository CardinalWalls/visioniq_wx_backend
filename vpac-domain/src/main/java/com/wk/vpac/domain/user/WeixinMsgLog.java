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
 * 微信消息发送记录
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_weixin_msg_log")
@DynamicUpdate
public class WeixinMsgLog implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** user_id - user_id */
  @Column(name = "user_id")
  @Param("user_id")
  private String userId;

  /** receive_id - receive_id */
  @Column(name = "receive_id")
  @Param("receive_id")
  private String receiveId;

  /** open_id - open_id */
  @Column(name = "open_id")
  @Param("open_id")
  private String openId;

  /** msg_id - msg_id */
  @Column(name = "msg_id")
  @Param("msg_id")
  private String msgId;

  /** template_id - template_id */
  @Column(name = "template_id")
  @Param("template_id")
  private String templateId;

  /** send_time - send_time */
  @Column(name = "send_time")
  @Param("send_time")
  private java.util.Date sendTime;

  /** recevie_status - 接受状态 */
  @Column(name = "recevie_status")
  @Param("接受状态")
  private Integer recevieStatus;

  /** wx_name - wx_name */
  @Column(name = "wx_name")
  @Param("wx_name")
  private String wxName;

  /** template_name - template_name */
  @Column(name = "template_name")
  @Param("template_name")
  private String templateName;

  
  public String getId() {
    return this.id;
  }

  public String getUserId() {
    return userId;
  }

  public String getReceiveId() {
    return receiveId;
  }

  public String getOpenId() {
    return openId;
  }

  public String getMsgId() {
    return msgId;
  }

  public String getTemplateId() {
    return templateId;
  }

  public java.util.Date getSendTime() {
    return sendTime;
  }

  public Integer getRecevieStatus() {
    return recevieStatus;
  }

  public String getWxName() {
    return wxName;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setReceiveId(String receiveId) {
    this.receiveId = receiveId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public void setSendTime(java.util.Date sendTime) {
    this.sendTime = sendTime;
  }

  public void setRecevieStatus(Integer recevieStatus) {
    this.recevieStatus = recevieStatus;
  }

  public void setWxName(String wxName) {
    this.wxName = wxName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

}