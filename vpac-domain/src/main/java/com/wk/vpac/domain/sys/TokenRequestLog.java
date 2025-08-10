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
 * 已登录token请求接口日志
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-09-02
 */
@Entity
@Table(name = "sys_token_request_log")
@DynamicUpdate
public class TokenRequestLog implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** token - 登录的token */
  @Column(name = "token", nullable = false)
  @Param(value = "登录的token", required = true)
  private String token = "";

  /** uri - 请求uri */
  @Column(name = "uri", nullable = false)
  @Param(value = "请求uri", required = true)
  private String uri = "";

  /** method - Http方法 */
  @Column(name = "method", nullable = false)
  @Param(value = "Http方法", required = true)
  private String method = "";

  /** params - 请求参数 */
  @Column(name = "params", nullable = false)
  @Param(value = "请求参数", required = true)
  private String params = "{}";

  /** ip - 请求IP */
  @Column(name = "ip", nullable = false)
  @Param(value = "请求IP", required = true)
  private String ip = "";

  /** ip_location - 请求地区 */
  @Column(name = "ip_location", nullable = false)
  @Param(value = "请求地区", required = true)
  private String ipLocation = "";

  /** server_host - 应用服务器 */
  @Column(name = "server_host", nullable = false)
  @Param(value = "应用服务器", required = true)
  private String serverHost = "";

  /** create_time -  */
  @Column(name = "create_time", nullable = false)
  @Param(value = "createTime", required = true)
  private java.util.Date createTime;

  /** api_name - 接口名称 */
  @Column(name = "api_name", nullable = false)
  @Param(value = "接口名称", required = true)
  private String apiName = "";

  public String getId() {
    return this.id;
  }

  public String getToken() {
    return token;
  }

  public String getUri() {
    return uri;
  }

  public String getMethod() {
    return method;
  }

  public String getParams() {
    return params;
  }

  public String getIp() {
    return ip;
  }

  public String getIpLocation() {
    return ipLocation;
  }

  public String getServerHost() {
    return serverHost;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public String getApiName() {
    return apiName;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public void setIpLocation(String ipLocation) {
    this.ipLocation = ipLocation;
  }

  public void setServerHost(String serverHost) {
    this.serverHost = serverHost;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setApiName(String apiName) {
    this.apiName = apiName;
  }
}