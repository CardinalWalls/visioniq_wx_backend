package com.wk.vpac.domain.dynamicform;

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
 * 动态表单编码
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_dynamic_data_code")
@DynamicUpdate
public class DynamicDataCode implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** code - code */
  @Column(name = "code")
  @Param("code")
  private String code;

  /** begin_time - begin_time */
  @Column(name = "begin_time")
  @Param("begin_time")
  private java.util.Date beginTime;

  /** end_time - end_time */
  @Column(name = "end_time")
  @Param("end_time")
  private java.util.Date endTime;

  /** title - title */
  @Column(name = "title")
  @Param("title")
  private String title;

  /** remark - remark */
  @Column(name = "remark")
  @Param("remark")
  private String remark;

  /** status - status */
  @Column(name = "status", nullable = false)
  @Param("status")
  private Integer status;

  /** captcha - captcha */
  @Column(name = "captcha", nullable = false)
  @Param("captcha")
  private Boolean captcha = false;

  /** user_auth - user_auth */
  @Column(name = "user_auth", nullable = false)
  @Param("user_auth")
  private Boolean userAuth = false;

  /** notify_wx_admin - notify_wx_admin */
  @Column(name = "notify_wx_admin", nullable = false)
  @Param("notify_wx_admin")
  private Boolean notifyWxAdmin = false;

  /** notify_wx_user - 消息通知微信用户：0=不通知，1=公众号，2=小程序 */
  @Column(name = "notify_wx_user", nullable = false)
  @Param("消息通知微信用户：0=不通知，1=公众号，2=小程序")
  private Integer notifyWxUser = 0;

  /** notify_wx_user_link - 消息通知微信用户跳转链接 */
  @Column(name = "notify_wx_user_link", nullable = false)
  @Param("消息通知微信用户跳转链接")
  private String notifyWxUserLink = "";

  public String getId() {
    return this.id;
  }

  public String getCode() {
    return code;
  }

  public java.util.Date getBeginTime() {
    return beginTime;
  }

  public java.util.Date getEndTime() {
    return endTime;
  }

  public String getTitle() {
    return title;
  }

  public String getRemark() {
    return remark;
  }

  public Integer getStatus() {
    return status;
  }

  public Boolean getCaptcha() {
    return captcha;
  }

  public Boolean getUserAuth() {
    return userAuth;
  }

  public Boolean getNotifyWxAdmin() {
    return notifyWxAdmin;
  }

  public Integer getNotifyWxUser() {
    return notifyWxUser;
  }

  public String getNotifyWxUserLink() {
    return notifyWxUserLink;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setBeginTime(java.util.Date beginTime) {
    this.beginTime = beginTime;
  }

  public void setEndTime(java.util.Date endTime) {
    this.endTime = endTime;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setCaptcha(Boolean captcha) {
    this.captcha = captcha;
  }

  public void setUserAuth(Boolean userAuth) {
    this.userAuth = userAuth;
  }

  public void setNotifyWxAdmin(Boolean notifyWxAdmin) {
    this.notifyWxAdmin = notifyWxAdmin;
  }

  public void setNotifyWxUser(Integer notifyWxUser) {
    this.notifyWxUser = notifyWxUser;
  }

  public void setNotifyWxUserLink(String notifyWxUserLink) {
    this.notifyWxUserLink = notifyWxUserLink;
  }
}