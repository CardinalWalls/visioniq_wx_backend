package com.wk.vpac.domain.chat;

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
import java.util.Date;



/**
 * 会话消息组关联用户
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2021-07-08
 */
@Entity
@Table(name = "wk_chat_message_group_user")
@DynamicUpdate
public class ChatMessageGroupUser implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;

  /** group_id - 会话ID */
  @Column(name = "group_id", nullable = false)
  @Param(value = "会话ID", required = true)
  private String groupId = "";

  /** user_id - 用户ID */
  @Column(name = "user_id", nullable = false)
  @Param(value = "用户ID", required = true)
  private String userId = "";

  /** unread - 未读数量 */
  @Column(name = "unread", nullable = false)
  @Param(value = "未读数量", required = true)
  private Integer unread = 0;

  /** visible - 是否显示 */
  @Column(name = "visible", nullable = false)
  @Param(value = "是否显示", required = true)
  private Boolean visible = true;

  /** last_message_id - 最后的消息ID */
  @Column(name = "last_message_id", nullable = false)
  @Param(value = "最后的消息ID", required = true)
  private String lastMessageId = "";

  /** last_message_time - 最后的消息时间 */
  @Column(name = "last_message_time", nullable = false)
  @Param(value = "最后的消息时间", required = true)
  private Date lastMessageTime;

  @Column(name = "notify_time", nullable = false)
  @Param(value = "通知时间", required = true)
  private Date notifyTime;

  @Column(name = "notified", nullable = false)
  @Param(value = "新的消息是否已发通知", required = true)
  private Boolean notified = false;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Integer getUnread() {
    return unread;
  }

  public void setUnread(Integer unread) {
    this.unread = unread;
  }

  public Boolean getVisible() {
    return visible;
  }

  public void setVisible(Boolean visible) {
    this.visible = visible;
  }

  public String getLastMessageId() {
    return lastMessageId;
  }

  public void setLastMessageId(String lastMessageId) {
    this.lastMessageId = lastMessageId;
  }

  public Date getLastMessageTime() {
    return lastMessageTime;
  }

  public void setLastMessageTime(Date lastMessageTime) {
    this.lastMessageTime = lastMessageTime;
  }

  public Date getNotifyTime() {
    return notifyTime;
  }

  public void setNotifyTime(Date notifyTime) {
    this.notifyTime = notifyTime;
  }

  public Boolean getNotified() {
    return notified;
  }

  public void setNotified(Boolean notified) {
    this.notified = notified;
  }
}