package com.wk.vpac.domain.sys;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.dto.mail.Mailable;
import com.base.components.common.id.jpa.JpaId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;



/**
 * 邮箱配置
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_mailbox")
@DynamicUpdate
public class Mailbox implements Domain<String>, Mailable {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** address - address */
  @Column(name = "address")
  @Param("address")
  private String address;

  /** password - password */
  @Column(name = "password")
  @Param("password")
  private String password;

  /** sign - sign */
  @Column(name = "sign")
  @Param("sign")
  private String sign;

  /** create_time - create_time */
  @Column(name = "create_time")
  @Param("create_time")
  private java.util.Date createTime;

  /** status - 0、禁用 1、启用 */
  @Column(name = "status", nullable = false)
  @Param("0、禁用 1、启用")
  private Integer status = 0;

  /** priority - priority */
  @Column(name = "priority", nullable = false)
  @Param("priority")
  private Integer priority = 0;

  /** smtp - smtp */
  @Column(name = "smtp")
  @Param("smtp")
  private String smtp;

  
  public String getId() {
    return this.id;
  }

  @Override
  public String getAddress() {
    return address;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public String getSign() {
    return sign;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public Integer getStatus() {
    return status;
  }

  public Integer getPriority() {
    return priority;
  }

  @Override
  public String getSmtp() {
    return smtp;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public void setSmtp(String smtp) {
    this.smtp = smtp;
  }

}