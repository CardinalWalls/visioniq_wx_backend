package com.wk.vpac.domain.user;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.id.jpa.JpaId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 基础用户
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-04-30
 */
@Data
@Entity
@Table(name = "base_user_base_info")
@DynamicUpdate
public class UserBaseInfo implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** account - account */
  @Column(name = "account", nullable = false)
  @Param(value = "account", required = true)
  private String account = "";

  /** phone - phone */
  @Column(name = "phone", nullable = false)
  @Param(value = "phone", required = true)
  private String phone = "";

  /** pwd - pwd */
  @Column(name = "pwd", nullable = false)
  @Param(value = "pwd", required = true)
  private String pwd = "";

  /** avatar - avatar */
  @Column(name = "avatar", nullable = false)
  @Param(value = "avatar", required = true)
  private String avatar = "";

  /** user_nick_name - user_nick_name */
  @Column(name = "user_nick_name", nullable = false)
  @Param(value = "user_nick_name", required = true)
  private String userNickName = "";

  /** gender - 性别; 0、 未填写  1、男  2、女 */
  @Column(name = "gender", nullable = false)
  @Param(value = "性别; 0、 未填写  1、男  2、女", required = true)
  private Integer gender = 0;

  /** birth - 生日 */
  @Column(name = "birth", nullable = false)
  @Param(value = "生日", required = true)
  private String birth = "";

  /** user_email - user_email */
  @Column(name = "user_email", nullable = false)
  @Param(value = "user_email", required = true)
  private String userEmail = "";

  /** user_qq - user_qq */
  @Column(name = "user_qq", nullable = false)
  @Param(value = "user_qq", required = true)
  private String userQq = "";

  /** user_type - 用户类别; 1、注册用户 */
  @Column(name = "user_type", nullable = false)
  @Param(value = "用户类别; 1、注册用户", required = true)
  private Integer userType = 1;

  /** user_type_id - user_type_id */
  @Column(name = "user_type_id", nullable = false)
  @Param(value = "user_type_id", required = true)
  private String userTypeId = "";

  /** regist_time - regist_time */
  @Column(name = "regist_time")
  @Param(value = "regist_time")
  private java.util.Date registTime;

  /** regist_ip - regist_ip */
  @Column(name = "regist_ip", nullable = false)
  @Param(value = "regist_ip", required = true)
  private String registIp = "127.0.0.1";

  /** last_login_time - last_login_time */
  @Column(name = "last_login_time")
  @Param(value = "last_login_time")
  private java.util.Date lastLoginTime;

  /** last_login_ip - last_login_ip */
  @Column(name = "last_login_ip", nullable = false)
  @Param(value = "last_login_ip", required = true)
  private String lastLoginIp = "";

  /** status - 启用状态; 2、待激活，1、正常，0、限制登录 */
  @Column(name = "status", nullable = false)
  @Param(value = "启用状态; 2、待激活，1、正常，0、限制登录", required = true)
  private Integer status = 1;

  /** password_errors - 密码错误次数 */
  @Column(name = "password_errors", nullable = false)
  @Param(value = "密码错误次数", required = true)
  private Integer passwordErrors = 0;

  /** profile - 简介 */
  @Column(name = "profile", nullable = false)
  @Param(value = "简介", required = true)
  private String profile = "";

  /** regist_region - regist_region */
  @Column(name = "regist_region", nullable = false)
  @Param(value = "regist_region", required = true)
  private String registRegion = "";

  /** region_id - region_id */
  @Column(name = "region_id", nullable = false)
  @Param(value = "region_id", required = true)
  private String regionId = "";

  /** select_region - select_region */
  @Column(name = "select_region", nullable = false)
  @Param(value = "select_region", required = true)
  private String selectRegion = "";

  /** select_region_id - select_region_id */
  @Column(name = "select_region_id", nullable = false)
  @Param(value = "select_region_id", required = true)
  private String selectRegionId = "";

  /** homepage - homepage */
  @Column(name = "homepage", nullable = false)
  @Param(value = "homepage", required = true)
  private String homepage = "";

  /** remark - remark */
  @Column(name = "remark", nullable = false)
  @Param(value = "remark", required = true)
  private String remark = "";

  /** data_source - 数据来源; 0 - PC, 1 - MOBILE, 2 - 存量数据导入, 3 - 机器人 */
  @Column(name = "data_source", nullable = false)
  @Param(value = "数据来源; 0 - PC, 1 - MOBILE, 2 - 存量数据导入, 3 - 机器人", required = true)
  private Integer dataSource = 0;

  /** wx_name - 微信名称 */
  @Column(name = "wx_name", nullable = false)
  @Param(value = "微信名称", required = true)
  private String wxName = "";

  /** wx_img - 微信头像 */
  @Column(name = "wx_img", nullable = false)
  @Param(value = "微信头像", required = true)
  private String wxImg = "";

  /** mp_open_id - 公众号 */
  @Column(name = "mp_open_id", nullable = false)
  @Param(value = "公众号", required = true)
  private String mpOpenId = "";

  /** app_open_id - 微信开放平台 */
  @Column(name = "app_open_id", nullable = false)
  @Param(value = "微信开放平台", required = true)
  private String appOpenId = "";

  /** mini_open_id - 小程序 */
  @Column(name = "mini_open_id", nullable = false)
  @Param(value = "小程序", required = true)
  private String miniOpenId = "";

  /** wx_union_id - wx_union_id */
  @Column(name = "wx_union_id", nullable = false)
  @Param(value = "wx_union_id", required = true)
  private String wxUnionId = "";

  /** integral - 积分 */
  @Column(name = "integral", nullable = false)
  @Param(value = "积分", required = true)
  private Integer integral = 0;

  /** balance - 余额 */
  @Column(name = "balance", nullable = false)
  @Param(value = "余额", required = true)
  private BigDecimal balance = new BigDecimal("0.00");

  /** id_card - 身份证号 */
  @Column(name = "id_card", nullable = false)
  @Param(value = "身份证号", required = true)
  private String idCard = "";

  /** parent_user_id - 上级推荐人ID */
  @Column(name = "parent_user_id", nullable = false)
  @Param(value = "上级推荐人ID", required = true)
  private String parentUserId = "";

  /** company_name - 公司名称 */
  @Column(name = "company_name", nullable = false)
  @Param(value = "公司名称", required = true)
  private String companyName = "";

  /** company_desc - 公司简介 */
  @Column(name = "company_desc", nullable = false)
  @Param(value = "公司简介", required = true)
  private String companyDesc = "";

  /** job_position - 职位 */
  @Column(name = "job_position", nullable = false)
  @Param(value = "职位", required = true)
  private String jobPosition = "";

  /** qrcode - 二维码 */
  @Column(name = "qrcode", nullable = false)
  @Param(value = "二维码", required = true)
  private String qrcode = "";

  /** ability - 能力值 */
  @Column(name = "ability", nullable = false)
  @Param(value = "能力值", required = true)
  private Integer ability = 0;

  /** company_department - 公司部门 */
  @Column(name = "company_department", nullable = false)
  @Param(value = "公司部门", required = true)
  private String companyDepartment = "";

  /** company_address - 公司地址 */
  @Column(name = "company_address", nullable = false)
  @Param(value = "公司地址", required = true)
  private String companyAddress = "";

  /** real_name - 真实名字 */
  @Column(name = "real_name", nullable = false)
  @Param(value = "真实名字", required = true)
  private String realName = "";

  /** level - 会员等级 */
  @Column(name = "level", nullable = false)
  @Param(value = "会员等级", required = true)
  private Integer level = 0;

  /** distribution - 分销收益 */
  @Column(name = "distribution")
  @Param("分销收益")
  private BigDecimal distribution = BigDecimal.ZERO;

  /** update_time - 修改时间 */
  @Column(name = "update_time")
  @Param(value = "修改时间")
  private Date updateTime = new Date();

  @Column(name = "community_id")
  @Param(value = "社区ID")
  private String communityId = "";

  @Column(name = "expert_id")
  @Param(value = "专家ID")
  private String expertId = "";

  @Column(name = "bind_expert_time")
  @Param(value = "绑定专家的时间")
  private Date bindExpertTime;

  @Column(name = "contact_phone")
  @Param(value = "联系电话")
  private String contactPhone = "";

}