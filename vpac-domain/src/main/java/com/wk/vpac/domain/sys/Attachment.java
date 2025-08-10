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



/**
 * 附件记录
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_attachment")
@DynamicUpdate
public class Attachment implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** url - url */
  @Column(name = "url")
  @Param("url")
  private String url;

  /** name - name */
  @Column(name = "name")
  @Param("name")
  private String name;

  /** file_type - 0、其它，1、企业营业执照，2、法人身份证，3、银行流水，4、协议书，5、合同 */
  @Column(name = "file_type", nullable = false)
  @Param("0、其它，1、企业营业执照，2、法人身份证，3、银行流水，4、协议书，5、合同")
  private Integer fileType;

  /** remark - remark */
  @Column(name = "remark")
  @Param("remark")
  private String remark;

  /** ref_id - ref_id */
  @Column(name = "ref_id")
  @Param("ref_id")
  private String refId;

  /** size - size */
  @Column(name = "size")
  @Param("size")
  private String size;

  
  public String getId() {
    return this.id;
  }

  public String getUrl() {
    return url;
  }

  public String getName() {
    return name;
  }

  public Integer getFileType() {
    return fileType;
  }

  public String getRemark() {
    return remark;
  }

  public String getRefId() {
    return refId;
  }

  public String getSize() {
    return size;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setFileType(Integer fileType) {
    this.fileType = fileType;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setRefId(String refId) {
    this.refId = refId;
  }

  public void setSize(String size) {
    this.size = size;
  }

}