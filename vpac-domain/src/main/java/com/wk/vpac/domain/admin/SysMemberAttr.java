package com.wk.vpac.domain.admin;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.id.jpa.JpaId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;



/**
 * 后台用户属性
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "admin_sys_member_attr")
@DynamicUpdate
public class SysMemberAttr implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** member_id - member_id */
  @Column(name = "member_id")
  @Param("member_id")
  private String memberId;

  /** attr_key - attr_key */
  @Column(name = "attr_key")
  @Param("attr_key")
  private String attrKey;

  /** attr_name - attr_name */
  @Column(name = "attr_name")
  @Param("attr_name")
  private String attrName;

  /** attr_value - attr_value */
  @Column(name = "attr_value")
  @Param("attr_value")
  private String attrValue;

  
  public String getId() {
    return this.id;
  }

  public String getMemberId() {
    return memberId;
  }

  public String getAttrKey() {
    return attrKey;
  }

  public String getAttrName() {
    return attrName;
  }

  public String getAttrValue() {
    return attrValue;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setMemberId(String memberId) {
    this.memberId = memberId;
  }

  public void setAttrKey(String attrKey) {
    this.attrKey = attrKey;
  }

  public void setAttrName(String attrName) {
    this.attrName = attrName;
  }

  public void setAttrValue(String attrValue) {
    this.attrValue = attrValue;
  }

}