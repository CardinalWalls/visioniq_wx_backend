package com.wk.vpac.domain.sys;

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
import java.util.Date;



/**
 * MD5 参数
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-09-02
 */
@Entity
@Table(name = "sys_md5_json")
@DynamicUpdate
public class SysMd5Json implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "PARAM_JSON_MD5", required = true)
  private String id;
  
  /** param_json - 参数Json */
  @Column(name = "param_json", nullable = false)
  @Param(value = "参数Json", required = true)
  private String paramJson = "{}";

  /** create_time - 创建时间 */
  @Column(name = "create_time", nullable = false)
  @Param(value = "创建时间", required = true)
  private Date createTime = new Date();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getParamJson() {
    return paramJson;
  }

  public void setParamJson(String paramJson) {
    this.paramJson = paramJson;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}