package com.wk.vpac.domain.msgqueue;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.id.jpa.JpaId;
import com.base.components.common.service.message.MessageEventHandler;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;



/**
 * 系统消息事件处理记录
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "sys_message_event_handle")
@DynamicUpdate
public class SysMessageEventHandle implements MessageEventHandler, Domain {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** event_id - 消息事件ID */
  @Column(name = "event_id")
  @Param("消息事件ID")
  private String eventId;

  /** listener_id - listener_id */
  @Column(name = "listener_id")
  @Param("listener_id")
  private String listenerId;

  /** listener_class - listener_class */
  @Column(name = "listener_class")
  @Param("listener_class")
  private String listenerClass;

  /** has_done - 完成状态; -1=执行中，0=未开始，1=已完成，2=异常，3=其它监听器处理，4=重试成功 */
  @Column(name = "has_done")
  @Param("完成状态; -1=执行中，0=未开始，1=已完成，2=异常，3=其它监听器处理，4=重试成功 ")
  private Integer hasDone;

  /** create_time - 创建时间 */
  @Column(name = "create_time")
  @Param("创建时间")
  private java.util.Date createTime;

  /** done_server - 完成服务host */
  @Column(name = "done_server")
  @Param("完成服务host")
  private String doneServer;

  /** done_host - done_host */
  @Column(name = "done_host")
  @Param("done_host")
  private String doneHost;

  /** done_time - 完成时间 */
  @Column(name = "done_time")
  @Param("完成时间")
  private java.util.Date doneTime;

  /** error_time - 异常时间 */
  @Column(name = "error_time")
  @Param("异常时间")
  private java.util.Date errorTime;

  /** error_stack - 异常信息 */
  @Column(name = "error_stack")
  @Param("异常信息")
  private String errorStack;

  /** remark - remark */
  @Column(name = "remark")
  @Param("remark")
  private String remark;

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public String getEventId() {
    return eventId;
  }

  public String getListenerId() {
    return listenerId;
  }

  @Override
  public String getListenerClass() {
    return listenerClass;
  }

  @Override
  public Integer getHasDone() {
    return hasDone;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public String getDoneServer() {
    return doneServer;
  }

  public String getDoneHost() {
    return doneHost;
  }

  public java.util.Date getDoneTime() {
    return doneTime;
  }

  public java.util.Date getErrorTime() {
    return errorTime;
  }

  public String getErrorStack() {
    return errorStack;
  }

  public String getRemark() {
    return remark;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public void setListenerId(String listenerId) {
    this.listenerId = listenerId;
  }

  public void setListenerClass(String listenerClass) {
    this.listenerClass = listenerClass;
  }

  public void setHasDone(Integer hasDone) {
    this.hasDone = hasDone;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setDoneServer(String doneServer) {
    this.doneServer = doneServer;
  }

  public void setDoneHost(String doneHost) {
    this.doneHost = doneHost;
  }

  public void setDoneTime(java.util.Date doneTime) {
    this.doneTime = doneTime;
  }

  public void setErrorTime(java.util.Date errorTime) {
    this.errorTime = errorTime;
  }

  public void setErrorStack(String errorStack) {
    this.errorStack = errorStack;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}