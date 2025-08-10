package com.wk.vpac.domain.dynamicform;

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
 * 动态表单数据
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_dynamic_form_data")
@DynamicUpdate
public class DynamicFormData implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** code - code */
  @Column(name = "code")
  @Param("code")
  private String code;

  /** title - title */
  @Column(name = "title")
  @Param("title")
  private String title;

  /** json_data - json_data */
  @Column(name = "json_data")
  @Param("json_data")
  private String jsonData;

  /** create_time - create_time */
  @Column(name = "create_time")
  @Param("create_time")
  private Date createTime;

  /** status - status; 0=已取消，1=待处理，2=已处理 */
  @Column(name = "status", nullable = false)
  @Param("status; 0=已取消，1=待处理，2=已处理 ")
  private Integer status;

  /** user_id - user_id */
  @Column(name = "user_id", nullable = false)
  @Param("user_id")
  private String userId = "";

  /** update_time - update_time */
  @Column(name = "update_time")
  @Param("update_time")
  private Date updateTime;

  /** remark - remark */
  @Column(name = "remark", nullable = false)
  @Param("remark")
  private String remark = "";

  /** result - 处理结果 */
  @Column(name = "result", nullable = false)
  @Param("处理结果")
  private String result = "";


  public String getId() {
    return this.id;
  }

  public String getCode() {
    return code;
  }

  public String getTitle() {
    return title;
  }

  public String getJsonData() {
    return jsonData;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public Integer getStatus() {
    return status;
  }

  public String getUserId() {
    return userId;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public String getRemark() {
    return remark;
  }

  public String getResult() {
    return result;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setJsonData(String jsonData) {
    this.jsonData = jsonData;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setResult(String result) {
    this.result = result;
  }
}