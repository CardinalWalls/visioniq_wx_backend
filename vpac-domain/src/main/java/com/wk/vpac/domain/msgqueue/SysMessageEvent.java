package com.wk.vpac.domain.msgqueue;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.id.jpa.JpaId;
import com.base.components.common.service.message.MessageEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;



/**
 * 系统消息事件
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "sys_message_event")
@DynamicUpdate
public class SysMessageEvent implements Domain<String>, MessageEvent {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param("消息队列会以此ID作为消息体进行发送")
  private String id;
  
  /** channel - 业务channel */
  @Column(name = "channel")
  @Param("业务channel")
  private String channel;

  /** event_info_json - 业务Json */
  @Column(name = "event_info_json")
  @Param("业务Json")
  private String eventInfoJson;

  /** has_done - 完成状态; -1=执行中，0=未开始，1=已完成，2=异常，3=其它监听器处理  */
  @Column(name = "has_done")
  @Param("完成状态; -1=执行中，0=未开始，1=已完成，2=异常，3=其它监听器处理 ")
  private Integer hasDone;

  /** create_time - 创建时间 */
  @Column(name = "create_time")
  @Param("创建时间")
  private java.util.Date createTime;

  /** sender_info - 发送者调用栈信息 */
  @Column(name = "sender_info")
  @Param("发送者调用栈信息")
  private String senderInfo;

  /** send_time - 发送时间 */
  @Column(name = "send_time")
  @Param("发送时间")
  private java.util.Date sendTime;

  /** send_count - 被发送的次数 */
  @Column(name = "send_count")
  @Param("被发送的次数")
  private Integer sendCount;

  /** done_server - 完成调用的服务名 */
  @Column(name = "done_server")
  @Param("完成调用的服务名")
  private String doneServer;

  /** done_host - 完成调用的服务host */
  @Column(name = "done_host")
  @Param("完成调用的服务host")
  private String doneHost;

  /** done_time - 完成时间 */
  @Column(name = "done_time")
  @Param("完成时间")
  private java.util.Date doneTime;

  /** error_time - 最近发生异常的时间 */
  @Column(name = "error_time")
  @Param("最近发生异常的时间")
  private java.util.Date errorTime;

  /** error_stack - 最近异常栈信息 */
  @Column(name = "error_stack")
  @Param("最近异常栈信息")
  private String errorStack;

  /** remark - 备注 */
  @Column(name = "remark")
  @Param("备注")
  private String remark;

  /** uniqueHandle - 是否单一监听器处理 */
  @Column(name = "unique_handle", nullable = false)
  @Param("是否单一监听器处理")
  private Boolean uniqueHandle = true;

  
  @Override
  public String getId() {
    return this.id;
  }

  public String getChannel() {
    return channel;
  }

  @Override
  public String getEventInfoJson() {
    return eventInfoJson;
  }

  public Integer getHasDone() {
    return hasDone;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public String getSenderInfo() {
    return senderInfo;
  }

  public java.util.Date getSendTime() {
    return sendTime;
  }

  public Integer getSendCount() {
    return sendCount;
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

  @Override
  public String getRemark() {
    return remark;
  }

  public Boolean getUniqueHandle() {
    return uniqueHandle;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public void setEventInfoJson(String eventInfoJson) {
    this.eventInfoJson = eventInfoJson;
  }

  public void setHasDone(Integer hasDone) {
    this.hasDone = hasDone;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setSenderInfo(String senderInfo) {
    this.senderInfo = senderInfo;
  }

  public void setSendTime(java.util.Date sendTime) {
    this.sendTime = sendTime;
  }

  public void setSendCount(Integer sendCount) {
    this.sendCount = sendCount;
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

  public void setUniqueHandle(Boolean uniqueHandle) {
    this.uniqueHandle = uniqueHandle;
  }

  @Override
  public boolean uniqueHandle() {
    return this.uniqueHandle == null ? true : this.uniqueHandle;
  }
}