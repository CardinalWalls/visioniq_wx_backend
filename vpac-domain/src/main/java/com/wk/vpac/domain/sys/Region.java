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
 * 地区
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_region")
@DynamicUpdate
public class Region implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** province_id - 省Id */
  @Column(name = "province_id")
  @Param("省Id")
  private String provinceId;

  /** city_id - 市Id */
  @Column(name = "city_id")
  @Param("市Id")
  private String cityId;

  /** order_no - order_no */
  @Column(name = "order_no")
  @Param("order_no")
  private String orderNo;

  /** name - name */
  @Column(name = "name")
  @Param("name")
  private String name;

  /** name_path - name_path */
  @Column(name = "name_path")
  @Param("name_path")
  private String namePath;

  /** parent_id - parent_id */
  @Column(name = "parent_id")
  @Param("parent_id")
  private String parentId;

  /** level - 级别 */
  @Column(name = "level")
  @Param("级别")
  private Integer level;

  /** leaf - 是否叶子节点; 0、否 1、是 */
  @Column(name = "leaf")
  @Param("是否叶子节点; 0、否 1、是")
  private Integer leaf = 0;

  /** remark - remark */
  @Column(name = "remark")
  @Param("remark")
  private String remark;

  /** create_time - create_time */
  @Column(name = "create_time")
  @Param("create_time")
  private java.util.Date createTime;

  /** status - 启用状态; 0、禁用 1、启用 */
  @Column(name = "status")
  @Param("启用状态; 0、禁用 1、启用")
  private Integer status = 1;

  /** left_val - left_val */
  @Column(name = "left_val")
  @Param("left_val")
  private Integer leftVal = 1;

  /** right_val - right_val */
  @Column(name = "right_val")
  @Param("right_val")
  private Integer rightVal = 2;

  
  public String getId() {
    return this.id;
  }

  public String getProvinceId() {
    return provinceId;
  }

  public String getCityId() {
    return cityId;
  }

  public String getOrderNo() {
    return orderNo;
  }

  public String getName() {
    return name;
  }

  public String getNamePath() {
    return namePath;
  }

  public String getParentId() {
    return parentId;
  }

  public Integer getLevel() {
    return level;
  }

  public Integer getLeaf() {
    return leaf;
  }

  public String getRemark() {
    return remark;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public Integer getStatus() {
    return status;
  }

  public Integer getLeftVal() {
    return leftVal;
  }

  public Integer getRightVal() {
    return rightVal;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setProvinceId(String provinceId) {
    this.provinceId = provinceId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setNamePath(String namePath) {
    this.namePath = namePath;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public void setLeaf(Integer leaf) {
    this.leaf = leaf;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setLeftVal(Integer leftVal) {
    this.leftVal = leftVal;
  }

  public void setRightVal(Integer rightVal) {
    this.rightVal = rightVal;
  }

}