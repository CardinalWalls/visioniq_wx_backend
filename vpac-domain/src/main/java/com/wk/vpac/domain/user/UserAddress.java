package com.wk.vpac.domain.user;

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
 * 用户地址
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-06-09
 */
@Entity
@Table(name = "base_user_address")
@DynamicUpdate
public class UserAddress implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** user_id - user_id */
  @Column(name = "user_id", nullable = false)
  @Param(value = "user_id", required = true)
  private String userId;

  /** region_id - region_id */
  @Column(name = "region_id", nullable = false)
  @Param(value = "region_id", required = true)
  private String regionId;
  /** region_id_array - region_id_array */
  @Column(name = "region_ids", nullable = false)
  @Param(value = "region_ids", required = true)
  private String regionIds;

  /** region_name - 地区省市区名称 */
  @Column(name = "region_name")
  @Param(value = "地区省市区名称")
  private String regionName = "";

  /** receive_name - 收件人姓名 */
  @Column(name = "receive_name", nullable = false)
  @Param(value = "收件人姓名", required = true)
  private String receiveName;

  /** receive_phone - 收件人电话 */
  @Column(name = "receive_phone", nullable = false)
  @Param(value = "收件人电话", required = true)
  private String receivePhone;

  /** address - 详细地址 */
  @Column(name = "address", nullable = false)
  @Param(value = "详细地址", required = true)
  private String address;

  /** valid - 0=备用，1=使用 */
  @Column(name = "valid", nullable = false)
  @Param(value = "0=备用，1=使用", required = true)
  private Boolean valid = Boolean.TRUE;

  /** longitude - 定位经度 */
  @Column(name = "longitude", nullable = false)
  @Param(value = "定位经度", required = true)
  private String longitude = "0.0";

  /** latitude - 定位纬度 */
  @Column(name = "latitude", nullable = false)
  @Param(value = "定位纬度", required = true)
  private String latitude = "0.0";

  /** full_address - 地址全称 */
  @Column(name = "full_address", nullable = false)
  @Param(value = "详细地址", required = true)
  private String fullAddress = "";
  
  public String getId() {
    return this.id;
  }

  public String getUserId() {
    return userId;
  }

  public String getRegionId() {
    return regionId;
  }

  public String getRegionName() {
    return regionName;
  }

  public String getReceiveName() {
    return receiveName;
  }

  public String getReceivePhone() {
    return receivePhone;
  }

  public String getAddress() {
    return address;
  }

  public Boolean getValid() {
    return valid;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  public void setReceiveName(String receiveName) {
    this.receiveName = receiveName;
  }

  public void setReceivePhone(String receivePhone) {
    this.receivePhone = receivePhone;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setValid(Boolean valid) {
    this.valid = valid;
  }

  public String getRegionIds() {
    return regionIds;
  }

  public void setRegionIds(String regionIds) {
    this.regionIds = regionIds;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getFullAddress() {
    return fullAddress;
  }

  public void setFullAddress(String fullAddress) {
    this.fullAddress = fullAddress;
  }
}