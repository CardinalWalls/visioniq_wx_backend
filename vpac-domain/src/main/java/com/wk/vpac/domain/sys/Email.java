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
 * 邮件记录
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_email")
@DynamicUpdate
public class Email implements Domain<String>{  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** subject - subject */
  @Column(name = "subject")
  @Param("subject")
  private String subject;

  /** content - content */
  @Column(name = "content")
  @Param("content")
  private String content;

  /** addressee - addressee */
  @Column(name = "addressee")
  @Param("addressee")
  private String addressee;

  /** remark - remark */
  @Column(name = "remark")
  @Param("remark")
  private String remark;

  /** create_time - create_time */
  @Column(name = "create_time")
  @Param("create_time")
  private java.util.Date createTime;

  /** author_id - author_id */
  @Column(name = "author_id")
  @Param("author_id")
  private String authorId;

  /** status - 0、未发送 1、已发送 */
  @Column(name = "status", nullable = false)
  @Param("0、未发送 1、已发送")
  private Integer status = 0;

  /** mailbox_id - mailbox_id */
  @Column(name = "mailbox_id")
  @Param("mailbox_id")
  private String mailboxId;

  /** count - count */
  @Column(name = "count", nullable = false)
  @Param("count")
  private Integer count = 0;

  /** send_time - send_time */
  @Column(name = "send_time")
  @Param("send_time")
  private java.util.Date sendTime;

  
  public String getId() {
    return this.id;
  }

  public String getSubject() {
    return subject;
  }

  public String getContent() {
    return content;
  }

  public String getAddressee() {
    return addressee;
  }

  public String getRemark() {
    return remark;
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

  public String getMailboxId() {
    return mailboxId;
  }

  public Integer getCount() {
    return count;
  }

  public java.util.Date getSendTime() {
    return sendTime;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setAddressee(String addressee) {
    this.addressee = addressee;
  }

  public void setRemark(String remark) {
    this.remark = remark;
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

  public void setMailboxId(String mailboxId) {
    this.mailboxId = mailboxId;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public void setSendTime(java.util.Date sendTime) {
    this.sendTime = sendTime;
  }

}