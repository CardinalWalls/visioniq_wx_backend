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
 * 干预方式
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2024-06-11
 */
@Data
@Entity
@Table(name = "wk_user_intervene")
@DynamicUpdate
public class UserIntervene implements Domain<String> {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;

  /**
   * user_id -
   */
  @Column(name = "user_archive_id", nullable = false)
  @Param(value = "用户档案ID", required = true)
  private String userArchiveId = "";


  /**
   * type - 干预类型；0=正常、1=远视储备不足、2=轻度-中度近视、3=中度-高度近视、4=超高度近视
   */
  @Column(name = "type", nullable = false)
  @Param(value = "干预类型；0=正常、1=远视储备不足、2=轻度-中度近视、3=中度-高度近视、4=超高度近视", required = true)
  private Integer type = 0;

  /**
   * scheme - 干预措施
   */
  @Column(name = "scheme", nullable = false)
  @Param(value = "干预措施", required = true)
  private String scheme = "";

  /**
   * remark - 备注
   */
  @Column(name = "remark", nullable = false)
  @Param(value = "备注", required = true)
  private String remark = "";

  /**
   * create_time - 创建时间
   */
  @Column(name = "create_time", nullable = false)
  @Param(value = "创建时间", required = true)
  private java.util.Date createTime;

  /**
   * push_time - 推送时间
   */
  @Column(name = "push_time", nullable = false)
  @Param(value = "推送时间", required = true)
  private String pushTime = "";

}