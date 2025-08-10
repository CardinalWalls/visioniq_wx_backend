package com.wk.vpac.domain.user;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.id.jpa.JpaId;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;



/**
 * 用户档案
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Data
@Entity
@Table(name = "wk_user_archive")
@DynamicUpdate
public class UserArchive implements Domain<String> {
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

  /** update_time - 修改时间 */
  @Column(name = "update_time", nullable = false)
  @Param(value = "修改时间", required = true)
  private java.util.Date updateTime;

  /** user_id - 用户ID */
  @Column(name = "user_id", nullable = false)
  @Param(value = "用户ID", required = true)
  private String userId = "";

  /** expert_id - 所属医生ID */
  @Column(name = "expert_id", nullable = false)
  @Param(value = "所属医生ID", required = true)
  private String expertId = "";

  /** name - 姓名 */
  @Column(name = "name", nullable = false)
  @Param(value = "姓名", required = true)
  private String name = "";

  /** gender - 性别 */
  @Column(name = "gender", nullable = false)
  @Param(value = "性别", required = true)
  private Integer gender = 0;

  /** birth - 出生日期 */
  @Column(name = "birth", nullable = false)
  @Param(value = "出生日期", required = true)
  private String birth = "";

  /** parents_myopia - 双亲是否近视; 0=无, 1=父, 2=母, 3=父母 */
  @Column(name = "parents_myopia", nullable = false)
  @Param(value = "双亲是否近视; 0=无, 1=父, 2=母, 3=父母", required = true)
  private Integer parentsMyopia = 0;

  /** region_id - 地区ID */
  @Column(name = "region_id", nullable = false)
  @Param(value = "地区ID", required = true)
  private String regionId = "";

  /** region_name - 地区名称 */
  @Column(name = "region_name", nullable = false)
  @Param(value = "地区名称", required = true)
  private String regionName = "";

  /** idcard - 身份证 */
  @Column(name = "idcard", nullable = false)
  @Param(value = "身份证", required = true)
  private String idcard = "";

  /** school_name - 学校名称 */
  @Column(name = "school_name", nullable = false)
  @Param(value = "学校名称", required = true)
  private String schoolName = "";

  /** org_name - 机构名称 */
  @Column(name = "org_name", nullable = false)
  @Param(value = "机构名称", required = true)
  private String orgName = "";

  @Column(name = "remark", nullable = false)
  @Param(value = "备注", required = true)
  private String remark = "";

  @Column(name = "risk_level", nullable = false)
  @Param(value = "风险等级；0=无，1=低风险，2=中风险，3=高风险", required = true)
  private Integer riskLevel = 0;
}