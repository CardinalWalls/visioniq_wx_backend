package com.wk.vpac.domain.msgqueue;

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
 * 系统消息事件错误日志
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "sys_message_event_error")
@DynamicUpdate
public class SysMessageEventError implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** message_id - 消息事件ID */
  @Column(name = "message_id")
  @Param("消息事件ID")
  private String messageId;

  /** channel - channel */
  @Column(name = "channel")
  @Param("channel")
  private String channel;

  /** error_server - 调用异常的服务名 */
  @Column(name = "error_server")
  @Param("调用异常的服务名")
  private String errorServer;

  /** error_host - 调用异常的服务host */
  @Column(name = "error_host")
  @Param("调用异常的服务host")
  private String errorHost;

  /** error_stack - 异常堆栈 */
  @Column(name = "error_stack")
  @Param("异常堆栈")
  private String errorStack;

  
  public String getId() {
    return this.id;
  }

  public String getMessageId() {
    return messageId;
  }

  public String getChannel() {
    return channel;
  }

  public String getErrorServer() {
    return errorServer;
  }

  public String getErrorHost() {
    return errorHost;
  }

  public String getErrorStack() {
    return errorStack;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public void setErrorServer(String errorServer) {
    this.errorServer = errorServer;
  }

  public void setErrorHost(String errorHost) {
    this.errorHost = errorHost;
  }

  public void setErrorStack(String errorStack) {
    this.errorStack = errorStack;
  }

}