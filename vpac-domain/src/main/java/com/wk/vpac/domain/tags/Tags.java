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
 * 标签表
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-04-30
 */
@Entity
@Table(name = "mjc_tags")
@DynamicUpdate
public class Tags implements Domain<String> {  
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

  /** status - 状态; 0=禁用，1=启用 */
  @Column(name = "status", nullable = false)
  @Param(value = "状态; 0=禁用，1=启用", required = true)
  private Integer status = 0;

  /** tag_name - 标签名 */
  @Column(name = "tag_name", nullable = false)
  @Param(value = "标签名", required = true)
  private String tagName = "";

  /** sort_no - 排序号 */
  @Column(name = "sort_no", nullable = false)
  @Param(value = "排序号", required = true)
  private Integer sortNo = 0;

  /** tag_code - 标签编码 */
  @Column(name = "tag_code", nullable = false)
  @Param(value = "标签编码", required = true)
  private String tagCode = "";

  /** type_id - 类型ID */
  @Column(name = "type_id", nullable = false)
  @Param(value = "类型ID", required = true)
  private String typeId = "";

  /** img_json - 图片json */
  @Column(name = "img_json", nullable = false)
  @Param(value = "图片json", required = true)
  private String imgJson = "[]";

  public String getImgJson() {
    return imgJson;
  }

  public void setImgJson(String imgJson) {
    this.imgJson = imgJson;
  }

  public String getId() {
    return this.id;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public Integer getStatus() {
    return status;
  }

  public String getTagName() {
    return tagName;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public String getTagCode() {
    return tagCode;
  }

  public String getTypeId() {
    return typeId;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }

  public void setTagCode(String tagCode) {
    this.tagCode = tagCode;
  }

  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }

}