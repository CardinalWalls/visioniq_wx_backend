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



/**
 * 专家
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-02-15
 */
@Data
@Entity
@Table(name = "wk_expert")
@DynamicUpdate
public class Expert implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;

  /**
   * create_time - 创建时间
   */
  @Column(name = "create_time", nullable = false)
  @Param(value = "创建时间", required = true)
  private java.util.Date createTime;

  /**
   * update_time - 修改时间
   */
  @Column(name = "update_time", nullable = false)
  @Param(value = "修改时间", required = true)
  private java.util.Date updateTime;

  /**
   * user_id - 用户ID
   */
  @Column(name = "user_id", nullable = false)
  @Param(value = "用户ID", required = true)
  private String userId = "";

  /**
   * title - 职称
   */
  @Column(name = "title", nullable = false)
  @Param(value = "职称", required = true)
  private String title = "";

  /**
   * job_position - 职位
   */
  @Column(name = "job_position", nullable = false)
  @Param(value = "职位", required = true)
  private String jobPosition = "";

  /**
   * hospital - 医院
   */
  @Column(name = "hospital", nullable = false)
  @Param(value = "医院", required = true)
  private String hospital = "";

  /**
   * department - 科室
   */
  @Column(name = "department", nullable = false)
  @Param(value = "科室", required = true)
  private String department = "";

  /**
   * level - 等级
   */
  @Column(name = "level", nullable = false)
  @Param(value = "等级", required = true)
  private String level = "";

  /**
   * profile - 个人简介
   */
  @Column(name = "profile", nullable = false)
  @Param(value = "个人简介", required = true)
  private String profile = "";


  @Column(name = "status", nullable = false)
  @Param(value = "状态；1=有效，0=无效，-1=审核中", required = true)
  private Integer status = 1;

  /**
   * appointment_week_limit - 预约限制（星期）
   */
  @Column(name = "appointment_week_limit", nullable = false)
  @Param(value = "预约限制（星期）", required = true)
  private String appointmentWeekLimit = "";

  @Column(name = "region_id", nullable = false)
  @Param(value = "地区ID", required = true)
  private String regionId = "";

  @Column(name = "region_name", nullable = false)
  @Param(value = "地区名称", required = true)
  private String regionName = "";

  @Column(name = "view_count", nullable = false)
  @Param(value = "查看次数", required = true)
  private Integer viewCount = 0;

  @Column(name = "proficient", nullable = false)
  @Param(value = "擅长专业", required = true)
  private String proficient = "";

  @Column(name = "work_card", nullable = false)
  @Param(value = "工作证件", required = true)
  private String workCard = "[]";

  @Column(name = "qr_code", nullable = false)
  @Param(value = "二维码", required = true)
  private String qrCode = "";
}