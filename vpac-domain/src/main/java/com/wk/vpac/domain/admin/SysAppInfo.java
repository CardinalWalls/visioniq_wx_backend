package com.wk.vpac.domain.admin;

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
 * 后台应用菜单
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "admin_sys_app_info")
@DynamicUpdate
public class SysAppInfo implements Domain<String> {
  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** name - name */
  @Column(name = "name")
  @Param("name")
  private String name;

  /** description - description */
  @Column(name = "description")
  @Param("description")
  private String description;

  /** target_url - target_url */
  @Column(name = "target_url")
  @Param("target_url")
  private String targetUrl;

  /** params_json - params_json */
  @Column(name = "params_json")
  @Param("params_json")
  private String paramsJson;

  /** auth_url_prefix - auth_url_prefix */
  @Column(name = "auth_url_prefix")
  @Param("auth_url_prefix")
  private String authUrlPrefix;

  /** icon_class - icon_class */
  @Column(name = "icon_class")
  @Param("icon_class")
  private String iconClass;

  /** type - 类型; 0、目录，1、菜单页面，2、功能资源 */
  @Column(name = "type", nullable = false)
  @Param("类型; 0、目录，1、菜单页面，2、功能资源")
  private Integer type = 0;

  /** read_write_type - 控制授权url下，读写级别，优先级比排除授权资源高，0、读写，1、只读，2、只写 */
  @Column(name = "read_write_type", nullable = false)
  @Param("控制授权url下，读写级别，优先级比排除授权资源高，0、读写，1、只读，2、只写")
  private Integer readWriteType = 0;

  /** parent_id - parent_id */
  @Column(name = "parent_id")
  @Param("parent_id")
  private String parentId;

  /** left_val - left_val */
  @Column(name = "left_val", nullable = false)
  @Param("left_val")
  private Integer leftVal = 1;

  /** right_val - right_val */
  @Column(name = "right_val", nullable = false)
  @Param("right_val")
  private Integer rightVal = 2;

  /** status - 是否启用; 0、否，1、是 */
  @Column(name = "status", nullable = false)
  @Param("是否启用; 0、否，1、是")
  private Integer status = 0;

  
  public String getId() {
    return this.id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getTargetUrl() {
    return targetUrl;
  }

  public String getParamsJson() {
    return paramsJson;
  }

  public String getAuthUrlPrefix() {
    return authUrlPrefix;
  }

  public String getIconClass() {
    return iconClass;
  }

  public Integer getType() {
    return type;
  }

  public Integer getReadWriteType() {
    return readWriteType;
  }

  public String getParentId() {
    return parentId;
  }

  public Integer getLeftVal() {
    return leftVal;
  }

  public Integer getRightVal() {
    return rightVal;
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

  public void setDescription(String description) {
    this.description = description;
  }

  public void setTargetUrl(String targetUrl) {
    this.targetUrl = targetUrl;
  }

  public void setParamsJson(String paramsJson) {
    this.paramsJson = paramsJson;
  }

  public void setAuthUrlPrefix(String authUrlPrefix) {
    this.authUrlPrefix = authUrlPrefix;
  }

  public void setIconClass(String iconClass) {
    this.iconClass = iconClass;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public void setReadWriteType(Integer readWriteType) {
    this.readWriteType = readWriteType;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public void setLeftVal(Integer leftVal) {
    this.leftVal = leftVal;
  }

  public void setRightVal(Integer rightVal) {
    this.rightVal = rightVal;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}