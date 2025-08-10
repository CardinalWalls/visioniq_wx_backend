package com.wk.vpac.domain.user;

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
 * 后台相册表
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-06-30
 */
@Entity
@Table(name = "mjc_gallery")
@DynamicUpdate
public class Gallery implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** user_id - 用户id */
  @Column(name = "user_id", nullable = false)
  @Param(value = "用户id", required = true)
  private String userId = "";

  /** url - 链接 */
  @Column(name = "url", nullable = false)
  @Param(value = "链接", required = true)
  private String url = "";

  /** create_time - 创建时间 */
  @Column(name = "create_time", nullable = false)
  @Param(value = "创建时间", required = true)
  private java.util.Date createTime;

  
  public String getId() {
    return this.id;
  }

  public String getUserId() {
    return userId;
  }

  public String getUrl() {
    return url;
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

  public void setUrl(String url) {
    this.url = url;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

}