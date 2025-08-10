package com.wk.vpac.domain.region;

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
 * 社区
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-02-15
 */
@Entity
@Table(name = "wk_community")
@DynamicUpdate
public class Community implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;

  /**
   * create_time - 创建时间
   */
  @Column(name = "create_time", nullable = false)
  @Param(value = "创建时间", required = true)
  private java.util.Date createTime;

  /**
   * update_time - 修改时间
   */
  @Column(name = "update_time", nullable = false)
  @Param(value = "修改时间", required = true)
  private java.util.Date updateTime;

  /**
   * region_id - 地区ID
   */
  @Column(name = "region_id", nullable = false)
  @Param(value = "地区ID", required = true)
  private String regionId = "";

  /**
   * expert_id - 专家ID
   */
  @Column(name = "expert_id", nullable = false)
  @Param(value = "专家ID", required = true)
  private String expertId = "";

  /**
   * street_name - 街道名称
   */
  @Column(name = "street_name", nullable = false)
  @Param(value = "街道名称", required = true)
  private String streetName = "";

  /**
   * name - 社区名称
   */
  @Column(name = "name", nullable = false)
  @Param(value = "社区名称", required = true)
  private String name = "";

  /**
   * address - 社区地址
   */
  @Column(name = "address", nullable = false)
  @Param(value = "社区地址", required = true)
  private String address = "";

  /**
   * sort_no - 排序
   */
  @Column(name = "sort_no", nullable = false)
  @Param(value = "排序", required = true)
  private Integer sortNo = 0;

  /**
   * valid - 是否有效
   */
  @Column(name = "valid", nullable = false)
  @Param(value = "是否有效", required = true)
  private Boolean valid = true;


  public String getId() {
    return this.id;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public java.util.Date getUpdateTime() {
    return updateTime;
  }

  public String getRegionId() {
    return regionId;
  }

  public String getExpertId() {
    return expertId;
  }

  public String getStreetName() {
    return streetName;
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public Boolean getValid() {
    return valid;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setUpdateTime(java.util.Date updateTime) {
    this.updateTime = updateTime;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public void setExpertId(String expertId) {
    this.expertId = expertId;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }

  public void setValid(Boolean valid) {
    this.valid = valid;
  }
}