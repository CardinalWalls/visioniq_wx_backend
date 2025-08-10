package com.wk.vpac.domain.admin;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.dto.tree.NestedTree;
import com.base.components.common.id.jpa.JpaId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;



/**
 * 后台用户
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "admin_sys_member")
@DynamicUpdate
public class SysMember implements Domain<String>, NestedTree {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** account - account */
  @Column(name = "account")
  @Param("account")
  private String account;

  /** pwd - pwd */
  @Column(name = "pwd")
  @Param("pwd")
  private String pwd;

  /** avatar - avatar */
  @Column(name = "avatar")
  @Param("avatar")
  private String avatar;

  /** member_code - member_code */
  @Column(name = "member_code")
  @Param("member_code")
  private String memberCode;

  /** member_name - member_name */
  @Column(name = "member_name")
  @Param("member_name")
  private String memberName;

  /** gender - 性别; 0、 未填写  1、男  2、女 */
  @Column(name = "gender")
  @Param("性别; 0、 未填写  1、男  2、女")
  private Integer gender = 0;

  /** email_address - email_address */
  @Column(name = "email_address")
  @Param("email_address")
  private String emailAddress;

  /** member_type - 0、部门 1、员工用户 */
  @Column(name = "member_type", nullable = false)
  @Param("0、部门 1、员工用户")
  private Integer memberType = 0;

  /** create_time - create_time */
  @Column(name = "create_time")
  @Param("create_time")
  private java.util.Date createTime;

  /** last_login_time - last_login_time */
  @Column(name = "last_login_time")
  @Param("last_login_time")
  private java.util.Date lastLoginTime;

  /** last_login_ip - last_login_ip */
  @Column(name = "last_login_ip")
  @Param("last_login_ip")
  private String lastLoginIp;

  /** status - 启用状态; 1,正常 0.限制登录 */
  @Column(name = "status", nullable = false)
  @Param("启用状态; 1,正常 0.限制登录")
  private Integer status = 1;

  /** parent_id - parent_id */
  @Column(name = "parent_id")
  @Param("parent_id")
  private String parentId;

  /** left_val - left_val */
  @Column(name = "left_val", nullable = false)
  @Param("left_val")
  private Integer leftVal = 0;

  /** right_val - right_val */
  @Column(name = "right_val", nullable = false)
  @Param("right_val")
  private Integer rightVal = 0;

  /** tree_kind - tree_kind */
  @Column(name = "tree_kind")
  @Param("tree_kind")
  private String treeKind;

  /** region_id - region_id */
  @Column(name = "region_id")
  @Param("region_id")
  private String regionId;

  /** default_org - 是否默认机构 */
  @Column(name = "default_org")
  @Param("是否默认机构")
  private Integer defaultOrg = 0;

  
  @Override
  public String getId() {
    return this.id;
  }

  public String getAccount() {
    return account;
  }

  public String getPwd() {
    return pwd;
  }

  public String getAvatar() {
    return avatar;
  }

  public String getMemberCode() {
    return memberCode;
  }

  public String getMemberName() {
    return memberName;
  }

  public Integer getGender() {
    return gender;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public Integer getMemberType() {
    return memberType;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public java.util.Date getLastLoginTime() {
    return lastLoginTime;
  }

  public String getLastLoginIp() {
    return lastLoginIp;
  }

  public Integer getStatus() {
    return status;
  }
  @Override
  public String getParentId() {
    return parentId;
  }
  @Override
  public Integer getLeftVal() {
    return leftVal;
  }
  @Override
  public Integer getRightVal() {
    return rightVal;
  }
  @Override
  public String getTreeKind() {
    return treeKind;
  }

  public String getRegionId() {
    return regionId;
  }

  public Integer getDefaultOrg() {
    return defaultOrg;
  }
  @Override
  public void setId(String id) {
    this.id = id;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public void setMemberCode(String memberCode) {
    this.memberCode = memberCode;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }

  public void setGender(Integer gender) {
    this.gender = gender;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setMemberType(Integer memberType) {
    this.memberType = memberType;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setLastLoginTime(java.util.Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  public void setLastLoginIp(String lastLoginIp) {
    this.lastLoginIp = lastLoginIp;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
  @Override
  public void setParentId(String parentId) {
    this.parentId = parentId;
  }
  @Override
  public void setLeftVal(Integer leftVal) {
    this.leftVal = leftVal;
  }
  @Override
  public void setRightVal(Integer rightVal) {
    this.rightVal = rightVal;
  }
  @Override
  public void setTreeKind(String treeKind) {
    this.treeKind = treeKind;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public void setDefaultOrg(Integer defaultOrg) {
    this.defaultOrg = defaultOrg;
  }

}