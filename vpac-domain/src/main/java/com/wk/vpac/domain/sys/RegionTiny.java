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
 * 关联地区表（base_region）
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-03-08
 */
@Entity
@Table(name = "base_region_tiny")
@DynamicUpdate
public class RegionTiny implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** region_id -  */
  @Column(name = "region_id", nullable = false)
  @Param(value = "regionId", required = true)
  private String regionId;

  /** order_no - 区域编码 */
  @Column(name = "order_no")
  @Param(value = "区域编码")
  private String orderNo;

  /** name - 区域名称 */
  @Column(name = "name")
  @Param(value = "区域名称")
  private String name;

  /** remark - remark */
  @Column(name = "remark")
  @Param(value = "remark")
  private String remark;

  /** create_time - create_time */
  @Column(name = "create_time")
  @Param(value = "create_time")
  private java.util.Date createTime;

  /** status - 启用状态; 0、禁用 1、启用 */
  @Column(name = "status")
  @Param(value = "启用状态; 0、禁用 1、启用")
  private Integer status = 1;

  
  public String getId() {
    return this.id;
  }

  public String getRegionId() {
    return regionId;
  }

  public String getOrderNo() {
    return orderNo;
  }

  public String getName() {
    return name;
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

  public void setId(String id) {
    this.id = id;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  public void setName(String name) {
    this.name = name;
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

}