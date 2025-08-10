package com.wk.vpac.domain.user;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.constants.sys.Dates;
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
 * 视力功能检查报告
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2024-05-09
 */
@Data
@Entity
@Table(name = "wk_user_visual_function_report")
@DynamicUpdate
public class UserVisualFunctionReport implements Domain<String> {
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
   * user_archive_id - 用户档案ID
   */
  @Column(name = "user_archive_id", nullable = false)
  @Param(value = "用户档案ID", required = true)
  private String userArchiveId = "";

  /**
   * inspect_date - 检查日期
   */
  @Column(name = "inspect_date", nullable = false)
  @Param(value = "检查日期，例：" + Dates.DATE_DOC_EXP, required = true)
  private String inspectDate = "";

  /**
   * dominant_eye - 主视眼
   */
  @Column(name = "dominant_eye", nullable = false)
  @Param(value = "主视眼", required = true)
  private String dominantEye = "";

  /**
   * cover_test - 遮盖实验
   */
  @Column(name = "cover_test", nullable = false)
  @Param(value = "遮盖实验", required = true)
  private String coverTest = "";

  /**
   * appeal - 诉求
   */
  @Column(name = "appeal", nullable = false)
  @Param(value = "诉求", required = true)
  private String appeal = "";

  /**
   * left_sphere - 左眼球镜
   */
  @Column(name = "left_sphere", nullable = false)
  @Param(value = "左眼球镜", required = true)
  private String leftSphere = "";

  /**
   * right_sphere - 右眼球镜
   */
  @Column(name = "right_sphere", nullable = false)
  @Param(value = "右眼球镜", required = true)
  private String rightSphere = "";

  /**
   * left_cylinder - 左眼柱镜
   */
  @Column(name = "left_cylinder", nullable = false)
  @Param(value = "左眼柱镜", required = true)
  private String leftCylinder = "";

  /**
   * right_cylinder - 右眼柱镜
   */
  @Column(name = "right_cylinder", nullable = false)
  @Param(value = "右眼柱镜", required = true)
  private String rightCylinder = "";

  /**
   * left_axial - 左眼轴位
   */
  @Column(name = "left_axial", nullable = false)
  @Param(value = "左眼轴位", required = true)
  private String leftAxial = "";

  /**
   * right_axial - 右眼轴位
   */
  @Column(name = "right_axial", nullable = false)
  @Param(value = "右眼轴位", required = true)
  private String rightAxial = "";

  /**
   * left_bcva - 左眼最佳矫正视力
   */
  @Column(name = "left_bcva", nullable = false)
  @Param(value = "左眼最佳矫正视力", required = true)
  private String leftBcva = "";

  /**
   * right_bcva - 右眼最佳矫正视力
   */
  @Column(name = "right_bcva", nullable = false)
  @Param(value = "右眼最佳矫正视力", required = true)
  private String rightBcva = "";

  /**
   * left_phva - 左眼针孔视力
   */
  @Column(name = "left_phva", nullable = false)
  @Param(value = "左眼针孔视力", required = true)
  private String leftPhva = "";

  /**
   * right_phva - 右眼针孔视力
   */
  @Column(name = "right_phva", nullable = false)
  @Param(value = "右眼针孔视力", required = true)
  private String rightPhva = "";

  /**
   * adds - 近附加
   */
  @Column(name = "adds", nullable = false)
  @Param(value = "近附加", required = true)
  private String adds = "";

  /**
   * far_ghost - 远重影
   */
  @Column(name = "far_ghost", nullable = false)
  @Param(value = "远重影", required = true)
  private Boolean farGhost = Boolean.FALSE;

  /**
   * near_ghost - 近重影
   */
  @Column(name = "near_ghost", nullable = false)
  @Param(value = "近重影", required = true)
  private Boolean nearGhost = Boolean.FALSE;

  /**
   * far_clearly - 视远不清
   */
  @Column(name = "far_clearly", nullable = false)
  @Param(value = "视远不清", required = true)
  private Boolean farClearly = Boolean.FALSE;

  /**
   * near_clearly - 视近不清
   */
  @Column(name = "near_clearly", nullable = false)
  @Param(value = "视近不清", required = true)
  private Boolean nearClearly = Boolean.FALSE;

  /**
   * ophthalmalgia - 眼痛
   */
  @Column(name = "ophthalmalgia", nullable = false)
  @Param(value = "眼痛", required = true)
  private Boolean ophthalmalgia = Boolean.FALSE;

  /**
   * headache - 头痛
   */
  @Column(name = "headache", nullable = false)
  @Param(value = "头痛", required = true)
  private Boolean headache = Boolean.FALSE;

  /**
   * skip_lines - 串字跳行
   */
  @Column(name = "skip_lines", nullable = false)
  @Param(value = "串字跳行", required = true)
  private Boolean skipLines = Boolean.FALSE;

  /**
   * fatigue - 疲劳
   */
  @Column(name = "fatigue", nullable = false)
  @Param(value = "疲劳", required = true)
  private Boolean fatigue = Boolean.FALSE;

  /**
   * other_symptoms - 其它症状
   */
  @Column(name = "other_symptoms", nullable = false)
  @Param(value = "其它症状", required = true)
  private String otherSymptoms = "";

  /**
   * stereopsis - 立体视
   */
  @Column(name = "stereopsis", nullable = false)
  @Param(value = "立体视", required = true)
  private String stereopsis = "";

  /**
   * worth_4_dot - worth 4 dot
   */
  @Column(name = "worth_4_dot", nullable = false)
  @Param(value = "worth 4 dot", required = true)
  private String worth4Dot = "";

  /**
   * dlp - 远距水平隐斜
   */
  @Column(name = "dlp", nullable = false)
  @Param(value = "远距水平隐斜", required = true)
  private String dlp = "";

  /**
   * dvp - 远距垂直隐斜
   */
  @Column(name = "dvp", nullable = false)
  @Param(value = "远距垂直隐斜", required = true)
  private String dvp = "";

  /**
   * nlp - 近距水平隐斜
   */
  @Column(name = "nlp", nullable = false)
  @Param(value = "近距水平隐斜", required = true)
  private String nlp = "";

  /**
   * nvp - 近距垂直隐斜
   */
  @Column(name = "nvp", nullable = false)
  @Param(value = "近距垂直隐斜", required = true)
  private String nvp = "";

  /**
   * ac_a - AC/A
   */
  @Column(name = "ac_a", nullable = false)
  @Param(value = "AC/A", required = true)
  private String acA = "";

  /**
   * bcc - 调节反应
   */
  @Column(name = "bcc", nullable = false)
  @Param(value = "调节反应", required = true)
  private String bcc = "";

  /**
   * nra - 负相对调节
   */
  @Column(name = "nra", nullable = false)
  @Param(value = "负相对调节", required = true)
  private String nra = "";

  /**
   * pra - 正相对调节
   */
  @Column(name = "pra", nullable = false)
  @Param(value = "正相对调节", required = true)
  private String pra = "";

  /**
   * npc - 集合近点
   */
  @Column(name = "npc", nullable = false)
  @Param(value = "集合近点", required = true)
  private String npc = "";

  /**
   * far_fusion_range_bi - 远融像范围BI
   */
  @Column(name = "far_fusion_range_bi", nullable = false)
  @Param(value = "远融像范围BI", required = true)
  private String farFusionRangeBi = "";

  /**
   * far_fusion_range_bo - 远融像范围BO
   */
  @Column(name = "far_fusion_range_bo", nullable = false)
  @Param(value = "远融像范围BO", required = true)
  private String farFusionRangeBo = "";

  /**
   * near_fusion_range_bi - 近融像范围BI
   */
  @Column(name = "near_fusion_range_bi", nullable = false)
  @Param(value = "近融像范围BI", required = true)
  private String nearFusionRangeBi = "";

  /**
   * near_fusion_range_bo - 近融像范围BO
   */
  @Column(name = "near_fusion_range_bo", nullable = false)
  @Param(value = "近融像范围BO", required = true)
  private String nearFusionRangeBo = "";

  /**
   * remark - 备注
   */
  @Column(name = "remark", nullable = false)
  @Param(value = "备注", required = true)
  private String remark = "";

  /**
   * relief_glasses - 缓解眼镜
   */
  @Column(name = "relief_glasses", nullable = false)
  @Param(value = "缓解眼镜", required = true)
  private String reliefGlasses = "";

  /**
   * training_content - 训练内容
   */
  @Column(name = "training_content", nullable = false)
  @Param(value = "训练内容", required = true)
  private String trainingContent = "";

  /**
   * training_style - 训练方式
   */
  @Column(name = "training_style", nullable = false)
  @Param(value = "训练方式", required = true)
  private String trainingStyle = "";

  /**
   * home_training_equipment - 家庭训练器械
   */
  @Column(name = "home_training_equipment", nullable = false)
  @Param(value = "家庭训练器械", required = true)
  private String homeTrainingEquipment = "";

  /**
   * review_time - 复查时间
   */
  @Column(name = "review_time", nullable = false)
  @Param(value = "复查时间", required = true)
  private String reviewTime = "";

  /**
   * optometrists - 视光师
   */
  @Column(name = "optometrists", nullable = false)
  @Param(value = "视光师", required = true)
  private String optometrists = "";

  /**
   * file_array - 检查单据
   */
  @Column(name = "file_array", nullable = false)
  @Param(value = "检查单据; 例：[{url:xxx, name:xx}]", required = true)
  private String fileArray = "[]";

  /**
   * af_od - 调节灵敏度OD
   */
  @Column(name = "af_od", nullable = false)
  @Param(value = "调节灵敏度OD", required = true)
  private String afOd = "";
  /**
   * af_os - 调节灵敏度OS
   */
  @Column(name = "af_os", nullable = false)
  @Param(value = "调节灵敏度OS", required = true)
  private String afOs = "";
  /**
   * af_ou - 调节灵敏度OU
   */
  @Column(name = "af_ou", nullable = false)
  @Param(value = "调节灵敏度OU", required = true)
  private String afOu = "";

  /**
   * amp_od - 调节幅度OD
   */
  @Column(name = "amp_od", nullable = false)
  @Param(value = "调节幅度OD", required = true)
  private String ampOd = "";
  /**
   * amp_os - 调节幅度OS
   */
  @Column(name = "amp_os", nullable = false)
  @Param(value = "调节幅度OS", required = true)
  private String ampOs = "";

  @Column(name = "pressure", nullable = false)
  @Param(value = "眼压kapa", required = true)
  private String pressure = "";

  @Column(name = "choroidal_thickness", nullable = false)
  @Param(value = "脉络膜厚度mm", required = true)
  private String choroidalThickness = "";

  @Column(name = "side_center_defocus", nullable = false)
  @Param(value = "旁中心离焦", required = true)
  private String sideCenterDefocus = "";

  @Column(name = "electrophysiology", nullable = false)
  @Param(value = "电生理检测", required = true)
  private String electrophysiology = "";

  @Column(name = "eyeground_img", nullable = false)
  @Param(value = "眼底照相", required = true)
  private String eyegroundImg = "[]";

  @Column(name = "oct_img", nullable = false)
  @Param(value = "眼科OCT", required = true)
  private String octImg = "[]";

  @Column(name = "visual_field_img", nullable = false)
  @Param(value = "视野检查", required = true)
  private String visualFieldImg = "[]";
}