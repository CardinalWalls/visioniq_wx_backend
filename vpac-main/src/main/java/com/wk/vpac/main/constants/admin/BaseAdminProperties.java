package com.wk.vpac.main.constants.admin;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Set;

/**
 * BaseAdminProperties
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2022-09-30 14:57
 */
@ConfigurationProperties("base.admin")
public class BaseAdminProperties {
  /** 登录路径 */
  private String loginPath = "/admin/login";

  /** 需要登录后就能直接访问的权限路径（跳过后台权限效验），Spring MVC 语法 */
  private List<String> onlyLoginAuthPath = Lists.newArrayList();

  /** 超级管理员角色ID, 无需配置菜单权限，拥有所有权限 */
  private Set<String> superRoleIds = Sets.newHashSet();

  /** 后台基本信息 */
  private Info info;

  public String getLoginPath() {
    return loginPath;
  }

  public void setLoginPath(String loginPath) {
    this.loginPath = loginPath;
  }

  public List<String> getOnlyLoginAuthPath() {
    return onlyLoginAuthPath;
  }

  public void setOnlyLoginAuthPath(List<String> onlyLoginAuthPath) {
    this.onlyLoginAuthPath = onlyLoginAuthPath;
  }

  public Set<String> getSuperRoleIds() {
    return superRoleIds;
  }

  public void setSuperRoleIds(Set<String> superRoleIds) {
    this.superRoleIds = superRoleIds;
  }

  public Info getInfo() {
    return info;
  }

  public void setInfo(Info info) {
    this.info = info;
  }

  public static class Info{
    /** 后台系统名称 */
    private String systemName = "";
    /** copyright */
    private String copyright = "";
    /** 登录页标题 */
    private String loginTitle = "";

    public String getSystemName() {
      return systemName;
    }

    public void setSystemName(String systemName) {
      this.systemName = systemName;
    }

    public String getCopyright() {
      return copyright;
    }

    public void setCopyright(String copyright) {
      this.copyright = copyright;
    }

    public String getLoginTitle() {
      return loginTitle;
    }

    public void setLoginTitle(String loginTitle) {
      this.loginTitle = loginTitle;
    }
  }
}
