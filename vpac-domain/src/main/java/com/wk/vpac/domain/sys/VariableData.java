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
 * 基础变量数据
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_variable_data")
@DynamicUpdate
public class VariableData implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** type - 类别 */
  @Column(name = "type", nullable = false)
  @Param("类别")
  private Integer type;

  /** ref_id - 关联项 */
  @Column(name = "ref_id")
  @Param("关联项")
  private String refId;

  /** json_data - Json数据 */
  @Column(name = "json_data")
  @Param("Json数据")
  private String jsonData;

  /** remark - 备注 */
  @Column(name = "remark")
  @Param("备注")
  private String remark;

  /** order_no - 排序号 */
  @Column(name = "order_no", nullable = false)
  @Param("排序号")
  private Integer orderNo = 1;

  /** avatar - 图片地址 */
  @Column(name = "avatar")
  @Param("图片地址")
  private String avatar;

  
  public String getId() {
    return this.id;
  }

  public Integer getType() {
    return type;
  }

  public String getRefId() {
    return refId;
  }

  public String getJsonData() {
    return jsonData;
  }

  public String getRemark() {
    return remark;
  }

  public Integer getOrderNo() {
    return orderNo;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public void setRefId(String refId) {
    this.refId = refId;
  }

  public void setJsonData(String jsonData) {
    this.jsonData = jsonData;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setOrderNo(Integer orderNo) {
    this.orderNo = orderNo;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

}