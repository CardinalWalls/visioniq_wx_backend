package com.wk.vpac.domain.operations.news;

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
import java.util.Date;



/**
 * 新闻类型
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-08-07
 */
@Entity
@Table(name = "base_news_type")
@DynamicUpdate
public class NewsType implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;

  /** name - 类型名称 */
  @Column(name = "name", nullable = false)
  @Param(value = "类型名称", required = true)
  private String name = "";

  /** create_time - 创建时间 */
  @Column(name = "create_time", nullable = false)
  @Param(value = "创建时间", required = true)
  private Date createTime;

  /** status - 状态,0、禁用 1、启用 */
  @Column(name = "status", nullable = false)
  @Param(value = "状态,0、禁用 1、启用", required = true)
  private Integer status = 0;

  /** sort_no - 排序号 */
  @Column(name = "sort_no", nullable = false)
  @Param(value = "排序号", required = true)
  private Integer sortNo = 0;

  /** remark - 备注 */
  @Column(name = "remark", nullable = false)
  @Param(value = "备注", required = true)
  private String remark = "";

  /** type_code - 类型编号 */
  @Column(name = "type_code", nullable = false)
  @Param(value = "类型编号", required = true)
  private String typeCode = "";

  /** is_ordinary - 是否是普通的，1-是，展示在主列表里面，0-否 */
  @Column(name = "is_ordinary", nullable = false)
  @Param(value = "是否是普通的，1-是，展示在主列表里面，0-否", required = true)
  private Integer isOrdinary = 1;

  /** img_json - 图片json */
  @Column(name = "img_json", nullable = false)
  @Param(value = "图片json", required = true)
  private String imgJson="[]";

  /** group_name - 分组名称 */
  @Column(name = "group_name", nullable = false)
  @Param(value = "分组名称", required = true)
  private String groupName = "";

  /** target_link - 跳转地址 */
  @Column(name = "target_link", nullable = false)
  @Param(value = "跳转地址", required = true)
  private String targetLink = "";


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getTypeCode() {
    return typeCode;
  }

  public void setTypeCode(String typeCode) {
    this.typeCode = typeCode;
  }

  public Integer getIsOrdinary() {
    return isOrdinary;
  }

  public void setIsOrdinary(Integer isOrdinary) {
    this.isOrdinary = isOrdinary;
  }

  public String getImgJson() {
    return imgJson;
  }

  public void setImgJson(String imgJson) {
    this.imgJson = imgJson;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getTargetLink() {
    return targetLink;
  }

  public void setTargetLink(String targetLink) {
    this.targetLink = targetLink;
  }
}