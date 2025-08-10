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
 * 标签类型/分组
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-04-30
 */
@Entity
@Table(name = "mjc_tags_type")
@DynamicUpdate
public class TagsType implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** name - 分组名称 */
  @Column(name = "name", nullable = false)
  @Param(value = "分组名称", required = true)
  private String name = "";

  /** sort_no - 排序号 */
  @Column(name = "sort_no", nullable = false)
  @Param(value = "排序号", required = true)
  private Integer sortNo = 1;

  /** type_code - 分组编码 */
  @Column(name = "type_code", nullable = false)
  @Param(value = "分组编码", required = true)
  private String typeCode = "";

  /** status - 状态 */
  @Column(name = "status", nullable = false)
  @Param(value = "状态", required = true)
  private Integer status = 1;

  /** img_json - 图片json */
  @Column(name = "img_json", nullable = false)
  @Param(value = "图片json", required = true)
  private String imgJson="[]";

  public String getImgJson() {
    return imgJson;
  }

  public void setImgJson(String imgJson) {
    this.imgJson = imgJson;
  }

  public String getId() {
    return this.id;
  }

  public String getName() {
    return name;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public String getTypeCode() {
    return typeCode;
  }

  public Integer getStatus() {
    return status;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }

  public void setTypeCode(String typeCode) {
    this.typeCode = typeCode;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}