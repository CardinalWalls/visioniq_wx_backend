package com.wk.vpac.domain.chat;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.id.jpa.JpaId;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;



/**
 * 会话消息分组
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Entity
@Table(name = "wk_chat_message_group")
@DynamicUpdate
public class ChatMessageGroup implements Domain<String> {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** create_time - 创建时间 */
  @Column(name = "create_time", nullable = false)
  @Param(value = "创建时间", required = true)
  private java.util.Date createTime;

  /** user_archive_id - 用户档案ID */
  @Column(name = "user_archive_id", nullable = false)
  @Param(value = "用户档案ID", required = true)
  private String userArchiveId = "";

  
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public String getUserArchiveId() {
    return userArchiveId;
  }

  public void setUserArchiveId(String userArchiveId) {
    this.userArchiveId = userArchiveId;
  }


}