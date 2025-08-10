package com.wk.vpac.domain.sys;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.id.jpa.JpaId;
import com.wk.vpac.common.constants.sys.ChatAutoReplyType;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.util.Date;



/**
 * 会话自动回复资料库
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Entity
@Table(name = "wk_sys_auto_reply")
@DynamicUpdate
public class SysAutoReply implements Domain<String> {
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

  /** content - 回复内容 */
  @Column(name = "content", nullable = false)
  @Param(value = "回复内容", required = true)
  private String content = "";

  /** keyword - 关键词 */
  @Column(name = "keyword", nullable = false)
  @Param(value = "关键词", required = true)
  private String keyword = "";

  @Column(name = "type", nullable = false)
  @Param(value = "分类；" + ChatAutoReplyType.AUTO_REPLY_DOC, required = true)
  private Integer type = ChatAutoReplyType.AI.getVal();

  @Column(name = "url", nullable = false)
  @Param(value = "链接地址", required = true)
  private String url = "";

  @Column(name = "sort_no", nullable = false)
  @Param(value = "排序", required = true)
  private Integer sortNo = 0;

  @Column(name = "match_length_max", nullable = false)
  @Param(value = "匹配关键词时的最大文字长度", required = true)
  private Integer matchLengthMax = 6;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }

  public Integer getMatchLengthMax() {
    return matchLengthMax;
  }

  public void setMatchLengthMax(Integer matchLengthMax) {
    this.matchLengthMax = matchLengthMax;
  }
}