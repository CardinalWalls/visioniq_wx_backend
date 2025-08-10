package com.wk.vpac.domain.chat;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.id.jpa.JpaId;
import com.wk.vpac.common.constants.sys.ChatAutoReplyType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.util.Date;



/**
 * 会话消息
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2021-07-08
 */
@Data
@Entity
@Table(name = "wk_chat_message")
@DynamicUpdate
public class ChatMessage implements Domain<String> {
  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** create_time -  */
  @Column(name = "create_time", nullable = false)
  @Param(value = "发送时间", required = true)
  private Date createTime;

  /** send_user_id - 发送者ID */
  @Column(name = "send_user_id", nullable = false)
  @Param(value = "发送者ID", required = true)
  private String sendUserId = "";

  /** content - 内容 */
  @Column(name = "content", nullable = false)
  @Param(value = "内容", required = true)
  private String content = "";

  @Column(name = "group_id", nullable = false)
  @Param(value = "会话ID", required = true)
  private String groupId = "";

  @Column(name = "url", nullable = false)
  @Param(value = "链接地址", required = true)
  private String url = "";

  @Column(name = "system_msg", nullable = false)
  @Param(value = "系统消息类别；" + ChatAutoReplyType.DOC, required = true)
  private Integer systemMsg = ChatAutoReplyType.NORMAL.getVal();

  @Column(name = "system_msg_type_name", nullable = false)
  @Param(value = "系统消息分类名称", required = true)
  private String systemMsgTypeName = "";

}