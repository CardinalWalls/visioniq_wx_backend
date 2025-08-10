package com.wk.vpac.domain.user;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.id.jpa.JpaId;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;



/**
 * 用户档案消息推送分类
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2024-04-23
 */
@Entity
@Table(name = "wk_user_push_message_type")
@DynamicUpdate
public class UserPushMessageType implements Domain<String> {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;

/** name - 分类名称 */
@Column(name = "name", nullable = false)
  @Param(value = "分类名称", required = true)
  private String name = "";

/** sort_no - 排序 */
@Column(name = "sort_no", nullable = false)
  @Param(value = "排序", required = true)
  private Integer sortNo = 0;

/** valid - 是否有效 */
@Column(name = "valid", nullable = false)
  @Param(value = "是否有效", required = true)
  private Boolean valid = Boolean.TRUE;


  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }

  public Boolean getValid() {
    return valid;
  }

  public void setValid(Boolean valid) {
    this.valid = valid;
  }


}