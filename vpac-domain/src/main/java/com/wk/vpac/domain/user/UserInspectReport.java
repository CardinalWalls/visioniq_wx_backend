package com.wk.vpac.domain.user;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.constants.sys.Dates;
import com.base.components.common.doc.annotation.Check;
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
 * 检查报告
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Data
@Entity
@Table(name = "wk_user_inspect_report")
@DynamicUpdate
public class UserInspectReport implements Domain<String> {
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

  /** user_archive_id - 用户档案ID */
  @Column(name = "user_archive_id", nullable = false)
  @Param(value = "用户档案ID", required = true)
  private String userArchiveId = "";

  /** inspect_date - 检查日期 */
  @Column(name = "inspect_date", nullable = false)
  @Param(value = "检查日期，例：" + Dates.DATE_DOC_EXP, required = true)
  private String inspectDate = "";

  /** hospital - 检测医院 */
  @Column(name = "hospital", nullable = false)
  @Param(value = "检测医院", required = true)
  private String hospital = "";

  /** height - 当前身高 */
  @Column(name = "height", nullable = false)
  @Param(value = "当前身高", required = true, check = @Check(min = "0"))
  private Integer height = 0;

  /** left_diopter_s - 左眼屈光度S */
  @Column(name = "left_diopter_s", nullable = false)
  @Param(value = "左眼屈光度S（球镜度数）", required = true)
  private String leftDiopterS = "";

  /** left_diopter_c - 左眼屈光度C */
  @Column(name = "left_diopter_c", nullable = false)
  @Param(value = "左眼屈光度C（柱镜度数）", required = true)
  private String leftDiopterC = "";

  /** left_axis - 左眼眼轴 */
  @Column(name = "left_axis", nullable = false)
  @Param(value = "左眼眼轴", required = true)
  private String leftAxis = "";

  /** left_curvature_radius - 左眼曲率半径 */
  @Column(name = "left_curvature_radius", nullable = false)
  @Param(value = "左眼曲率半径", required = true)
  private String leftCurvatureRadius = "";

  /** right_diopter_s - 右眼屈光度S */
  @Column(name = "right_diopter_s", nullable = false)
  @Param(value = "右眼屈光度S（球镜度数）", required = true)
  private String rightDiopterS = "";

  /** right_diopter_c - 右眼屈光度C */
  @Column(name = "right_diopter_c", nullable = false)
  @Param(value = "右眼屈光度C（柱镜度数）", required = true)
  private String rightDiopterC = "";

  /** right_axis - 右眼眼轴 */
  @Column(name = "right_axis", nullable = false)
  @Param(value = "右眼眼轴", required = true)
  private String rightAxis = "";

  /** right_curvature_radius - 右眼曲率半径 */
  @Column(name = "right_curvature_radius", nullable = false)
  @Param(value = "右眼曲率半径", required = true)
  private String rightCurvatureRadius = "";

  /** file_array - 检查单据 */
  @Column(name = "file_array", nullable = false)
  @Param(value = "检查单据; 例：[{url:xxx, name:xx}]", required = true)
  private String fileArray = "[]";

  /** out_school_hours - 校外用眼时长 */
  @Column(name = "out_school_hours", nullable = false)
  @Param(value = "校外用眼时长", required = true)
  private String outSchoolHours = "";

  /** incorrect_sittin - 坐姿不正确 */
  @Column(name = "incorrect_sittin", nullable = false)
  @Param(value = "坐姿不正确", required = true)
  private String incorrectSittin = "";

  /** incorrect_glasses - 佩戴眼镜不正确 */
  @Column(name = "incorrect_glasses", nullable = false)
  @Param(value = "佩戴眼镜不正确", required = true)
  private String incorrectGlasses = "";

  /** allergy - 过敏情况 */
  @Column(name = "allergy", nullable = false)
  @Param(value = "过敏情况", required = true)
  private String allergy = "";

  /** glasses_type - 眼镜种类 */
  @Column(name = "glasses_type", nullable = false)
  @Param(value = "眼镜种类", required = true)
  private String glassesType = "";

  /** other_solution - 其它措施 */
  @Column(name = "other_solution", nullable = false)
  @Param(value = "其它措施", required = true)
  private String otherSolution = "";

  /** outdoors_hours - 户外用眼时长 */
  @Column(name = "outdoors_hours", nullable = false)
  @Param(value = "户外用眼时长", required = true)
  private String outdoorsHours = "";

  /** other_description - 其它说明 */
  @Column(name = "other_description", nullable = false)
  @Param(value = "其它说明", required = true)
  private String otherDescription = "";

  /** dilated_refraction - 散瞳验光 */
  @Column(name = "dilated_refraction", nullable = false)
  @Param(value = "散瞳验光", required = true)
  private Boolean dilatedRefraction = false;

  /** other_file_array - 其它检查单据 */
  @Column(name = "other_file_array", nullable = false)
  @Param(value = "其它检查单据; 例：[{url:xxx, name:xx}]", required = true)
  private String otherFileArray = "[]";

  @Column(name = "left_k1", nullable = false)
  @Param(value = "左眼K1", required = true)
  private String leftK1 = "";

  @Column(name = "left_k2", nullable = false)
  @Param(value = "左眼K2", required = true)
  private String leftK2 = "";

  @Column(name = "right_k1", nullable = false)
  @Param(value = "右眼K1", required = true)
  private String rightK1 = "";

  @Column(name = "right_k2", nullable = false)
  @Param(value = "右眼K2", required = true)
  private String rightK2 = "";

  @Column(name = "outdoors_hours_explain", nullable = false)
  @Param(value = "户外用眼时长说明", required = true)
  private String outdoorsHoursExplain = "";

  @Column(name = "glasses_type_explain", nullable = false)
  @Param(value = "眼镜种类说明", required = true)
  private String glassesTypeExplain = "";

  @Column(name = "other_solution_explain", nullable = false)
  @Param(value = "其它措施说明", required = true)
  private String otherSolutionExplain = "";

  @Column(name = "idcard", nullable = false)
  @Param(value = "身份证", required = true)
  private String idcard = "";

  @Column(name = "name", nullable = false)
  @Param(value = "姓名", required = true)
  private String name = "";

  @Column(name = "gender", nullable = false)
  @Param(value = "性别", required = true)
  private Integer gender = 1;

  @Column(name = "school", nullable = false)
  @Param(value = "学校", required = true)
  private String school = "";

  @Column(name = "class_name", nullable = false)
  @Param(value = "班级", required = true)
  private String className = "";

  @Column(name = "left_shaft_position", nullable = false)
  @Param(value = "左眼轴位（L/A）", required = true)
  private String leftShaftPosition = "";

  @Column(name = "right_shaft_position", nullable = false)
  @Param(value = "右眼轴位（R/A）", required = true)
  private String rightShaftPosition = "";

  @Column(name = "left_visual", nullable = false)
  @Param(value = "右眼裸眼(RV)", required = true)
  private String leftVisual = "";

  @Column(name = "right_visual", nullable = false)
  @Param(value = "左眼裸眼(LV)", required = true)
  private String rightVisual = "";

  @Column(name = "import_num", nullable = false)
  @Param(value = "导入批次号", required = true)
  private String importNum = "";
}