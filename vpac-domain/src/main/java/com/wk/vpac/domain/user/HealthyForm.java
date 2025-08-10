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
 * 健康问卷表单
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-04-06
 */
@Data
@Entity
@Table(name = "wk_healthy_form")
@DynamicUpdate
public class HealthyForm implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** user_id -  */
  @Column(name = "user_id", nullable = false)
  @Param(value = "userId", required = true)
  private String userId = "";

  /** name - 姓名 */
  @Column(name = "name", nullable = false)
  @Param(value = "姓名", required = true)
  private String name = "";

  /** gender - 性别 */
  @Column(name = "gender", nullable = false)
  @Param(value = "性别", required = true)
  private Integer gender = 1;

  /** age - 年龄 */
  @Column(name = "age", nullable = false)
  @Param(value = "年龄", required = true)
  private Integer age = 1;

  /** height - 身高cm */
  @Column(name = "height", nullable = false)
  @Param(value = "身高cm", required = true)
  private java.math.BigDecimal height = new java.math.BigDecimal("0.00");

  /** weight - 体重kg */
  @Column(name = "weight", nullable = false)
  @Param(value = "体重kg", required = true)
  private java.math.BigDecimal weight = new java.math.BigDecimal("0.00");

  /** bmi - BMI */
  @Column(name = "bmi", nullable = false)
  @Param(value = "BMI", required = true)
  private java.math.BigDecimal bmi = new java.math.BigDecimal("0.00");

  /** weight_one_month - 近一个月体重 */
  @Column(name = "weight_one_month", nullable = false)
  @Param(value = "近一个月体重", required = true)
  private java.math.BigDecimal weightOneMonth = new java.math.BigDecimal("0.000");

  /** weight_two_month - 近二个月体重 */
  @Column(name = "weight_two_month", nullable = false)
  @Param(value = "近二个月体重", required = true)
  private java.math.BigDecimal weightTwoMonth = new java.math.BigDecimal("0.000");

  /** weight_three_month - 近三个月体重 */
  @Column(name = "weight_three_month", nullable = false)
  @Param(value = "近三个月体重", required = true)
  private java.math.BigDecimal weightThreeMonth = new java.math.BigDecimal("0.000");

  /** weight_four_month - 近四个月体重 */
  @Column(name = "weight_four_month", nullable = false)
  @Param(value = "近四个月体重", required = true)
  private java.math.BigDecimal weightFourMonth = new java.math.BigDecimal("0.000");

  /** weight_five_month - 近五个月体重 */
  @Column(name = "weight_five_month", nullable = false)
  @Param(value = "近五个月体重", required = true)
  private java.math.BigDecimal weightFiveMonth = new java.math.BigDecimal("0.000");

  /** weight_six_month - 近六个月体重 */
  @Column(name = "weight_six_month", nullable = false)
  @Param(value = "近六个月体重", required = true)
  private java.math.BigDecimal weightSixMonth = new java.math.BigDecimal("0.000");

  /** diseases - 基础疾病 */
  @Column(name = "diseases", nullable = false)
  @Param(value = "基础疾病", required = true)
  private String diseases = "";

  /** breakfast_main - 早餐主食 */
  @Column(name = "breakfast_main", nullable = false)
  @Param(value = "早餐主食", required = true)
  private String breakfastMain = "";

  /** breakfast_vegetable - 早餐蔬菜 */
  @Column(name = "breakfast_vegetable", nullable = false)
  @Param(value = "早餐蔬菜", required = true)
  private String breakfastVegetable = "";

  /** breakfast_meat - 早餐肉类 */
  @Column(name = "breakfast_meat", nullable = false)
  @Param(value = "早餐肉类", required = true)
  private String breakfastMeat = "";

  /** breakfast_bean - 早餐豆类 */
  @Column(name = "breakfast_bean", nullable = false)
  @Param(value = "早餐豆类", required = true)
  private String breakfastBean = "";

  /** breakfast_milk - 早餐奶类 */
  @Column(name = "breakfast_milk", nullable = false)
  @Param(value = "早餐奶类", required = true)
  private String breakfastMilk = "";

  /** breakfast_fruits - 早餐水果 */
  @Column(name = "breakfast_fruits", nullable = false)
  @Param(value = "早餐水果", required = true)
  private String breakfastFruits = "";

  /** lunch_main - 午餐主食 */
  @Column(name = "lunch_main", nullable = false)
  @Param(value = "午餐主食", required = true)
  private String lunchMain = "";

  /** lunch_vegetable - 午餐蔬菜 */
  @Column(name = "lunch_vegetable", nullable = false)
  @Param(value = "午餐蔬菜", required = true)
  private String lunchVegetable = "";

  /** lunch_meat - 午餐肉类 */
  @Column(name = "lunch_meat", nullable = false)
  @Param(value = "午餐肉类", required = true)
  private String lunchMeat = "";

  /** lunch_bean - 午餐豆类 */
  @Column(name = "lunch_bean", nullable = false)
  @Param(value = "午餐豆类", required = true)
  private String lunchBean = "";

  /** lunch_milk - 午餐奶类 */
  @Column(name = "lunch_milk", nullable = false)
  @Param(value = "午餐奶类", required = true)
  private String lunchMilk = "";

  /** lunch_fruits - 午餐水果 */
  @Column(name = "lunch_fruits", nullable = false)
  @Param(value = "午餐水果", required = true)
  private String lunchFruits = "";

  /** dinner_main - 晚餐主食 */
  @Column(name = "dinner_main", nullable = false)
  @Param(value = "晚餐主食", required = true)
  private String dinnerMain = "";

  /** dinner_vegetable - 晚餐蔬菜 */
  @Column(name = "dinner_vegetable", nullable = false)
  @Param(value = "晚餐蔬菜", required = true)
  private String dinnerVegetable = "";

  /** dinner_meat - 晚餐肉类 */
  @Column(name = "dinner_meat", nullable = false)
  @Param(value = "晚餐肉类", required = true)
  private String dinnerMeat = "";

  /** dinner_bean - 晚餐豆类 */
  @Column(name = "dinner_bean", nullable = false)
  @Param(value = "晚餐豆类", required = true)
  private String dinnerBean = "";

  /** dinner_milk - 晚餐奶类 */
  @Column(name = "dinner_milk", nullable = false)
  @Param(value = "晚餐奶类", required = true)
  private String dinnerMilk = "";

  /** dinner_fruits - 晚餐水果 */
  @Column(name = "dinner_fruits", nullable = false)
  @Param(value = "晚餐水果", required = true)
  private String dinnerFruits = "";

  /** health_products - 保健品 */
  @Column(name = "health_products", nullable = false)
  @Param(value = "保健品", required = true)
  private String healthProducts = "";

  /** physical_labour - 体力活动 */
  @Column(name = "physical_labour", nullable = false)
  @Param(value = "体力活动", required = true)
  private String physicalLabour = "";

  @Column(name = "sport_info", nullable = false)
  @Param(value = "运动方式", required = true)
  private String sportInfo  = "";

  @Column(name = "sport_frequency", nullable = false)
  @Param(value = "运动频率", required = true)
  private String sportFrequency  = "";

  /** shit_info - 大便情况 */
  @Column(name = "shit_info", nullable = false)
  @Param(value = "大便情况", required = true)
  private String shitInfo = "";

  /** sleep_info - 睡眠情况 */
  @Column(name = "sleep_info", nullable = false)
  @Param(value = "睡眠情况", required = true)
  private String sleepInfo = "";

  /** update_time - 更新时间 */
  @Column(name = "update_time", nullable = false)
  @Param(value = "更新时间", required = true)
  private java.util.Date updateTime;

  /** create_time - 创建时间 */
  @Column(name = "create_time", nullable = false)
  @Param(value = "创建时间", required = true)
  private java.util.Date createTime;

}