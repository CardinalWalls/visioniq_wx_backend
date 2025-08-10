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
 * 附件权限
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_attachment_auth")
@DynamicUpdate
public class AttachmentAuth implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** attachment_id - attachment_id */
  @Column(name = "attachment_id")
  @Param("attachment_id")
  private String attachmentId;

  /** user_id - user_id */
  @Column(name = "user_id")
  @Param("user_id")
  private String userId;

  /** url - url */
  @Column(name = "url")
  @Param("url")
  private String url;

  
  public String getId() {
    return this.id;
  }

  public String getAttachmentId() {
    return attachmentId;
  }

  public String getUserId() {
    return userId;
  }

  public String getUrl() {
    return url;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setAttachmentId(String attachmentId) {
    this.attachmentId = attachmentId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setUrl(String url) {
    this.url = url;
  }

}