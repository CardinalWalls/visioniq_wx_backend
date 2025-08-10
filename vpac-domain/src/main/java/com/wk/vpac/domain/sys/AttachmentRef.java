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
 * 附件关联
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_attachment_ref")
@DynamicUpdate
public class AttachmentRef implements Domain<String> {  
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

  /** ref_id - ref_id */
  @Column(name = "ref_id")
  @Param("ref_id")
  private String refId;

  /** ref_type - ref_type */
  @Column(name = "ref_type")
  @Param("ref_type")
  private String refType;

  
  public String getId() {
    return this.id;
  }

  public String getAttachmentId() {
    return attachmentId;
  }

  public String getRefId() {
    return refId;
  }

  public String getRefType() {
    return refType;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setAttachmentId(String attachmentId) {
    this.attachmentId = attachmentId;
  }

  public void setRefId(String refId) {
    this.refId = refId;
  }

  public void setRefType(String refType) {
    this.refType = refType;
  }

}