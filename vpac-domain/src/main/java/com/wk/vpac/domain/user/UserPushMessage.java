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
import java.util.Date;



/**
 * 用户档案推送消息
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Entity
@Table(name = "wk_user_push_message")
@DynamicUpdate
public class UserPushMessage implements Domain<String> {
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

  /** url - 链接地址 */
  @Column(name = "url", nullable = false)
  @Param(value = "链接地址", required = true)
  private String url = "";

  /** content - 文字内容 */
  @Column(name = "content", nullable = false)
  @Param(value = "文字内容", required = true)
  private String content = "";

  /** publish_time - 发布时间 */
  @Column(name = "publish_time", nullable = false)
  @Param(value = "发布时间", required = true)
  private String publishTime = "";

  /** operator_name - 操作人 */
  @Column(name = "operator_name", nullable = false)
  @Param(value = "操作人", required = true)
  private String operatorName = "";

  @Column(name = "publish_count", nullable = false)
  @Param(value = "收到的人数", required = true)
  private Integer publishCount = 0;

  @Column(name = "condition_json", nullable = false)
  @Param(value = "过滤条件", required = true)
  private String conditionJson = "";

  @Column(name = "type_id", nullable = false)
  @Param(value = "分类ID", required = true)
  private String typeId = "";

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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(String publishTime) {
    this.publishTime = publishTime;
  }

  public String getOperatorName() {
    return operatorName;
  }

  public void setOperatorName(String operatorName) {
    this.operatorName = operatorName;
  }

  public Integer getPublishCount() {
    return publishCount;
  }

  public void setPublishCount(Integer publishCount) {
    this.publishCount = publishCount;
  }

  public String getConditionJson() {
    return conditionJson;
  }

  public void setConditionJson(String conditionJson) {
    this.conditionJson = conditionJson;
  }

  public String getTypeId() {
    return typeId;
  }

  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }
}