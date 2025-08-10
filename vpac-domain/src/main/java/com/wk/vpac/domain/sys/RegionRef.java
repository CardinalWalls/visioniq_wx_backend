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
 * 地区关联表
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-04-30
 */
@Entity
@Table(name = "mjc_region_ref")
@DynamicUpdate
public class RegionRef implements Domain<String> {  
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

  /** region_id - 地区id */
  @Column(name = "region_id", nullable = false)
  @Param(value = "地区id", required = true)
  private String regionId = "";

  /** region_name - 地区*/
  @Column(name = "region_name", nullable = false)
  @Param(value = "地区", required = true)
  private String regionName = "";

  /** ref_id - 关联id */
  @Column(name = "ref_id", nullable = false)
  @Param(value = "关联id", required = true)
  private String refId = "";

  /** level - 级别 */
  @Column(name = "level")
  @Param("级别")
  private Integer level = 1;

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  public String getRegionId() {
    return regionId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public String getId() {
    return this.id;
  }

  public java.util.Date getCreateTime() {
    return createTime;
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

  public void setRefId(String refId) {
    this.refId = refId;
  }

}