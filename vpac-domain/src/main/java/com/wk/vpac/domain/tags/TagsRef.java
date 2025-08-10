package com.wk.vpac.domain.tags;

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
 * 标签关联表
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-04-30
 */
@Entity
@Table(name = "mjc_tags_ref")
@DynamicUpdate
public class TagsRef implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** create_time - 创建时间 */
  @Column(name = "create_time", nullable = false)
  @Param(value = "创建时间", required = true)
  private java.util.Date createTime;

  /** tags_id - 标签id */
  @Column(name = "tags_id", nullable = false)
  @Param(value = "标签id", required = true)
  private String tagsId = "";

  /** ref_id - 关联id */
  @Column(name = "ref_id", nullable = false)
  @Param(value = "关联id", required = true)
  private String refId = "";

  
  public String getId() {
    return this.id;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public String getTagsId() {
    return tagsId;
  }

  public String getRefId() {
    return refId;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setTagsId(String tagsId) {
    this.tagsId = tagsId;
  }

  public void setRefId(String refId) {
    this.refId = refId;
  }

}