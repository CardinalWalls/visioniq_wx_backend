package com.wk.vpac.common.dto.admin;

import com.base.components.common.dto.convert.Convertible;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;



/**
 * 管理端授权应用
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-04-02
 */
public class SysAppInfoDto implements Serializable, Convertible {
  private static final long serialVersionUID = 1L;

  /** id - */
  private String id;

  /** name - */
  private String name;

  /** description - 描述 */
  private String description;

  /** target_url - 跳转路径 */
  private String targetUrl;

  /** params_json - 跳转参数 */
  private String paramsJson;

  @JsonIgnore
  /** auth_url_prefix - 授权uri（Spring MVC 语法），不能使用根目录 */
  private String authUrlPrefix;

  /** icon_class - 图标 */
  private String iconClass;

  /** type - 是否为目录，0、目录，1、菜单页面，2、功能资源 */
  private Integer type;

  @JsonIgnore
  @Deprecated
  /** read_write_type - 控制授权url下，读写级别，优先级比排除授权资源高，0、读写，1、只读，2、只写 */
  private Integer readWriteType;

  /** parent_id - */
  private String parentId;

  private List<SysAppInfoDto> children;

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTargetUrl() {
    return targetUrl;
  }

  public void setTargetUrl(String targetUrl) {
    this.targetUrl = targetUrl;
  }

  public String getParamsJson() {
    return paramsJson;
  }

  public void setParamsJson(String paramsJson) {
    this.paramsJson = paramsJson;
  }

  public String getAuthUrlPrefix() {
    return authUrlPrefix;
  }

  public void setAuthUrlPrefix(String authUrlPrefix) {
    this.authUrlPrefix = authUrlPrefix;
  }

  public String getIconClass() {
    return iconClass;
  }

  public void setIconClass(String iconClass) {
    this.iconClass = iconClass;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getReadWriteType() {
    return readWriteType;
  }

  public void setReadWriteType(Integer readWriteType) {
    this.readWriteType = readWriteType;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public List<SysAppInfoDto> getChildren() {
    return children;
  }

  public void setChildren(List<SysAppInfoDto> children) {
    this.children = children;
  }

}