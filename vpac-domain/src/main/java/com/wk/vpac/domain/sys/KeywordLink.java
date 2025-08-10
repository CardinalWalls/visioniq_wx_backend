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
 * 关键词链接
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2022-09-29
 */
@Entity
@Table(name = "base_keyword_link")
@DynamicUpdate
public class KeywordLink implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** keyword - 关键词 */
  @Column(name = "keyword", nullable = false)
  @Param(value = "关键词", required = true)
  private String keyword = "";

  /** url_link - 跳转链接 */
  @Column(name = "url_link", nullable = false)
  @Param(value = "跳转链接", required = true)
  private String urlLink = "";

  
  public String getId() {
    return this.id;
  }

  public String getKeyword() {
    return keyword;
  }

  public String getUrlLink() {
    return urlLink;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setUrlLink(String urlLink) {
    this.urlLink = urlLink;
  }

}