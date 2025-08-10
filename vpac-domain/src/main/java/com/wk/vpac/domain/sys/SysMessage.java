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
 * 系统消息
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_sys_message")
@DynamicUpdate
public class SysMessage implements Domain<String> {  
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

  /** content - content */
  @Column(name = "content")
  @Param("content")
  private String content;

  /** create_time - create_time */
  @Column(name = "create_time")
  @Param("create_time")
  private java.util.Date createTime;

  /** know_time - know_time */
  @Column(name = "know_time")
  @Param("know_time")
  private java.util.Date knowTime;

  /** status - 0、未读 1、已读 */
  @Column(name = "status", nullable = false)
  @Param("0、未读 1、已读")
  private Integer status = 0;

  /** biz_type - biz_type */
  @Column(name = "biz_type")
  @Param("biz_type")
  private String bizType;

  /** biz_id - biz_id */
  @Column(name = "biz_id")
  @Param("biz_id")
  private String bizId;

  /** del_flag - 0、已删除 1、未删除 */
  @Column(name = "del_flag")
  @Param("0、已删除 1、未删除")
  private Integer delFlag = 1;

  
  public String getId() {
    return this.id;
  }

  public String getUserId() {
    return userId;
  }

  public String getContent() {
    return content;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public java.util.Date getKnowTime() {
    return knowTime;
  }

  public Integer getStatus() {
    return status;
  }

  public String getBizType() {
    return bizType;
  }

  public String getBizId() {
    return bizId;
  }

  public Integer getDelFlag() {
    return delFlag;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setKnowTime(java.util.Date knowTime) {
    this.knowTime = knowTime;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setBizType(String bizType) {
    this.bizType = bizType;
  }

  public void setBizId(String bizId) {
    this.bizId = bizId;
  }

  public void setDelFlag(Integer delFlag) {
    this.delFlag = delFlag;
  }

}