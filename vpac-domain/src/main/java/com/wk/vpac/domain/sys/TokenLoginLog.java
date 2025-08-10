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
 * 系统token登录日志
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-09-02
 */
@Entity
@Table(name = "sys_token_login_log")
@DynamicUpdate
public class TokenLoginLog implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** user_id -  */
  @Column(name = "user_id", nullable = false)
  @Param(value = "userId", required = true)
  private String userId = "";

  /** token_obj_name - token对象名称 */
  @Column(name = "token_obj_name", nullable = false)
  @Param(value = "token对象名称", required = true)
  private String tokenObjName = "";

  /** token_obj_json - token对象JSON */
  @Column(name = "token_obj_json", nullable = false)
  @Param(value = "token对象JSON", required = true)
  private String tokenObjJson = "{}";

  /** token_type - 用户类型 */
  @Column(name = "token_type", nullable = false)
  @Param(value = "用户类型", required = true)
  private String tokenType = "";

  /** login_ip - 登录IP */
  @Column(name = "login_ip", nullable = false)
  @Param(value = "登录IP", required = true)
  private String loginIp = "";

  /** login_location - 登录地区 */
  @Column(name = "login_location", nullable = false)
  @Param(value = "登录地区", required = true)
  private String loginLocation = "";

  /** token - 登录的token */
  @Column(name = "token", nullable = false)
  @Param(value = "登录的token", required = true)
  private String token = "";

  /** user_agent - 用户代理 */
  @Column(name = "user_agent", nullable = false)
  @Param(value = "用户代理", required = true)
  private String userAgent = "";

  /** server_host - 应用服务器 */
  @Column(name = "server_host", nullable = false)
  @Param(value = "应用服务器", required = true)
  private String serverHost = "";

  /** create_time - 登录时间 */
  @Column(name = "create_time", nullable = false)
  @Param(value = "登录时间", required = true)
  private java.util.Date createTime;

  
  public String getId() {
    return this.id;
  }

  public String getUserId() {
    return userId;
  }

  public String getTokenObjName() {
    return tokenObjName;
  }

  public String getTokenObjJson() {
    return tokenObjJson;
  }

  public String getTokenType() {
    return tokenType;
  }

  public String getLoginIp() {
    return loginIp;
  }

  public String getLoginLocation() {
    return loginLocation;
  }

  public String getToken() {
    return token;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public String getServerHost() {
    return serverHost;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setTokenObjName(String tokenObjName) {
    this.tokenObjName = tokenObjName;
  }

  public void setTokenObjJson(String tokenObjJson) {
    this.tokenObjJson = tokenObjJson;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public void setLoginIp(String loginIp) {
    this.loginIp = loginIp;
  }

  public void setLoginLocation(String loginLocation) {
    this.loginLocation = loginLocation;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public void setServerHost(String serverHost) {
    this.serverHost = serverHost;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

}