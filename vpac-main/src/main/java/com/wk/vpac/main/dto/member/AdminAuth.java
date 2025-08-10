

package com.wk.vpac.main.dto.member;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * AdminAuth
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2018-04-04 9:53
 */
public class AdminAuth implements Serializable {
  public static final PathMatcher MATCHER = new AntPathMatcher();
  private static final String SPACER = ":";
  private static final long serialVersionUID = 1L;
  private boolean superRole;
  private Serializable tokenObjId;
  private Set<String> roleIds;
  private Set<String> roleCodes;
  private Map<String, String> indexUri;
  private Set<String> equalsUri;
  private Set<String> patternUri;

  public Serializable getTokenObjId() {
    return tokenObjId;
  }

  public void setTokenObjId(Serializable tokenObjId) {
    this.tokenObjId = tokenObjId;
  }

  public boolean getSuperRole() {
    return superRole;
  }

  public void setSuperRole(boolean superRole) {
    this.superRole = superRole;
  }

  public Set<String> getRoleIds() {
    return roleIds;
  }

  public void setRoleIds(Set<String> roleIds) {
    this.roleIds = roleIds;
  }

  public Set<String> getRoleCodes() {
    return roleCodes;
  }

  public void setRoleCodes(Set<String> roleCodes) {
    this.roleCodes = roleCodes;
  }

  public Map<String, String> getIndexUri() {
    return indexUri;
  }

  public void setIndexUri(Map<String, String> indexUri) {
    this.indexUri = indexUri;
  }

  public Set<String> getEqualsUri() {
    return equalsUri;
  }

  public void setEqualsUri(Set<String> equalsUri) {
    this.equalsUri = equalsUri;
  }

  public Set<String> getPatternUri() {
    return patternUri;
  }

  public void setPatternUri(Set<String> patternUri) {
    this.patternUri = patternUri;
  }

  /**
   * 权限验证，返回非空时则有权限访问
   * @param uri
   * @param requestMethod
   *
   * @return
   */
  public String checkAuthPath(String uri, String requestMethod) {
    requestMethod = requestMethod != null ? requestMethod.toUpperCase() : HttpMethod.GET.name();
    String appName = checkIndexUri(uri);
    if(appName == null){
      appName = checkEqualsUri(uri, requestMethod);
    }
    if(appName == null){
      appName = checkPatternUri(uri, requestMethod);
    }
    return appName;
  }

  /**
   * 检查权限，是否含有此角色ID
   * @param roleId   nonnull - str - 角色ID
   * @return 是否含有此角色权限
   */
  public boolean checkRoleId(String roleId) {
    return roleIds != null && roleIds.contains(roleId);
  }

  /**
   * 检查权限，是否含有此角色code
   * @param roleCode   nonnull - str - 角色code
   * @return 是否含有此角色权限
   */
  public boolean checkRoleCode(String roleCode) {
    return roleCodes != null && roleCodes.contains(roleCode);
  }

//  private SysAppInfoDto check(SysAppInfoDto sysAppInfoDto, String uri, String requestMethod, ReadOrWrite rw) {
//    String urlPrefix = sysAppInfoDto.getAuthUrlPrefix();
//    if(StringUtils.isNotBlank(urlPrefix)){
//      for (String prefix : StringUtils.split(urlPrefix, ",")) {
//        if (StringUtils.isNotBlank(prefix) && !"/".equals(prefix) && uri.startsWith(prefix)) {
//          if (rw == null || rw.checkRequestMethod(requestMethod)) {
//            return sysAppInfoDto;
//          }
//        }
//      }
//    }
//    if (!CollectionUtils.isEmpty(sysAppInfoDto.getChildren())) {
//      for (SysAppInfoDto child : sysAppInfoDto.getChildren()) {
//        SysAppInfoDto checked = check(
//          child, uri, requestMethod, EnumUtil.parse(ReadOrWrite.class, "value", child.getReadWriteType()));
//        if (null != checked) {
//          return checked;
//        }
//      }
//    }
//    return null;
//  }

  private String checkIndexUri(String path){
    String appName = null;
    if(indexUri != null){
        appName = indexUri.get(path);
    }
    return appName;
  }

  private String checkEqualsUri(String path, String requestMethod){
    String appName = null;
    if(equalsUri != null){
      //优先匹配指定请求方法
      appName = equalsUri.contains(requestMethod + SPACER + path) ? "" : null;
      if(appName == null){
        appName = equalsUri.contains(path) ? "" : null;
      }
    }
    return appName;
  }

  private String checkPatternUri(String path, String requestMethod){
    if(patternUri != null){
      String methodPath = requestMethod + SPACER + path;
      for (String uri : patternUri) {
        if(MATCHER.match(uri, methodPath)){
          return "";
        }
        if(MATCHER.match(uri, path)){
          return "";
        }
      }
    }
    return null;
  }
}
