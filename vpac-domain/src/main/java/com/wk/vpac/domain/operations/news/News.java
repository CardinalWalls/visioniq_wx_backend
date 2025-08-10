/*
 * Copyright (c) 2017.  mj.he800.com Inc. All rights reserved.
 */

package com.wk.vpac.domain.operations.news;

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
import java.math.BigDecimal;



/**
 * 新闻
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-03-15
 */
@Entity
@Table(name = "base_news")
@DynamicUpdate
public class News implements Domain<String> {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;

  /** type_id - 类型id */
  @Column(name = "type_id", nullable = false)
  @Param(value = "类型id", required = true)
  private String typeId = "";

  /** title - 标题 */
  @Column(name = "title", nullable = false)
  @Param(value = "标题", required = true)
  private String title = "";

  /** content - 内容 */
  @Column(name = "content", nullable = false)
  @Param(value = "内容", required = true)
  private String content = "";


  /** publish_time - 发布时间 */
  @Column(name = "publish_time", nullable = false)
  @Param(value = "发布时间", required = true)
  private java.util.Date publishTime;

  /** status - 状态 */
  @Column(name = "status", nullable = false)
  @Param(value = "状态", required = true)
  private Integer status = 0;

  /** create_time - 创建时间 */
  @Column(name = "create_time", nullable = false)
  @Param(value = "创建时间", required = true)
  private java.util.Date createTime;

  /** author_id - 作者id */
  @Column(name = "author_id", nullable = false)
  @Param(value = "作者id", required = true)
  private String authorId = "";

  /** img - 封面图 */
  @Column(name = "img", nullable = false)
  @Param(value = "封面图", required = true)
  private String img = "";

  /** is_recommend - 是否推荐 */
  @Column(name = "is_recommend", nullable = false)
  @Param(value = "是否推荐", required = true)
  private Integer isRecommend = 0;

  /** template_type - 前端模板类型; 0=普通，1=红头文件 */
  @Column(name = "template_type", nullable = false)
  @Param(value = "前端模板类型; 0=普通，1=红头文件", required = true)
  private Integer templateType = 0;

  /** region_news - 是否是地区政策; 0=不是，1=是 */
  @Column(name = "region_news", nullable = false)
  @Param(value = "是否是地区政策; 0=不是，1=是", required = true)
  private Integer regionNews = 0;

  /** vip_view - 会员可看; 0=免费，1=会员 */
  @Column(name = "vip_view", nullable = false)
  @Param(value = "会员可看; 0=免费，1=会员", required = true)
  private Integer vipView = 0;

  /** summary - 摘要 */
  @Column(name = "summary", nullable = false)
  @Param(value = "摘要", required = true)
  private String summary;

  /** author_name - 作者名称 */
  @Column(name = "author_name", nullable = false)
  @Param(value = "作者名称", required = true)
  private String authorName = "";

  /** keyword - 关键字 */
  @Column(name = "keyword", nullable = false)
  @Param(value = "关键字", required = true)
  private String keyword = "";

  /** display_type - 列表展示类型（1=文左图右，2=文右图左，3=文上图下，4=文下图上） */
  @Column(name = "display_type", nullable = false)
  @Param(value = "列表展示类型（1=文左图右，2=文右图左，3=文上图下，4=文下图上）", required = true)
  private Integer displayType = 0;

  /** detail_type - 税法政策文章详情页类型（1=红色五星，2=普通文章） */
  @Column(name = "detail_type", nullable = false)
  @Param(value = "税法政策文章详情页类型（1=红色五星，2=普通文章）", required = true)
  private Integer detailType = 1;

  /** the_source - 来源 */
  @Column(name = "the_source", nullable = false)
  @Param(value = "来源", required = true)
  private String theSource = "";

  /** expert_id - 关联的专家ID */
  @Column(name = "expert_id", nullable = false)
  @Param(value = "关联的专家ID", required = true)
  private String expertId = "";

  /** price - 价格 */
  @Column(name = "price", nullable = false)
  @Param(value = "价格", required = true)
  private BigDecimal price = BigDecimal.ZERO;

  /** imgs - 多图 */
  @Column(name = "imgs", nullable = false)
  @Param(value = "多图", required = true)
  private String imgs = "[]";

  /** subtitle - 副标题 */
  @Column(name = "subtitle", nullable = false)
  @Param(value = "副标题", required = true)
  private String subtitle = "";

  /** other_attr - 其它属性 */
  @Column(name = "other_attr", nullable = false)
  @Param(value = "其它属性", required = true)
  private String otherAttr = "";


  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Integer getDetailType() {
    return detailType;
  }

  public void setDetailType(Integer detailType) {
    this.detailType = detailType;
  }

  public String getExpertId() {
    return expertId;
  }

  public void setExpertId(String expertId) {
    this.expertId = expertId;
  }

  public String getTheSource() {
    return theSource;
  }

  public void setTheSource(String theSource) {
    this.theSource = theSource;
  }

  public Integer getDisplayType() {
    return displayType;
  }

  public void setDisplayType(Integer displayType) {
    this.displayType = displayType;
  }

  public Integer getRegionNews() {
    return regionNews;
  }

  public void setRegionNews(Integer regionNews) {
    this.regionNews = regionNews;
  }

  public Integer getTemplateType() {
    return templateType;
  }

  public void setTemplateType(Integer templateType) {
    this.templateType = templateType;
  }

  public Integer getVipView() {
    return vipView;
  }

  public void setVipView(Integer vipView) {
    this.vipView = vipView;
  }

  public String getId() {
    return this.id;
  }

  public String getTypeId() {
    return typeId;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public java.util.Date getPublishTime() {
    return publishTime;
  }

  public Integer getStatus() {
    return status;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public String getAuthorId() {
    return authorId;
  }

  public String getImg() {
    return img;
  }

  public Integer getIsRecommend() {
    return isRecommend;
  }

  public String getSummary() {
    return summary;
  }

  public String getAuthorName() {
    return authorName;
  }

  public String getKeyword() {
    return keyword;
  }

  public String getImgs() {
    return imgs;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public String getOtherAttr() {
    return otherAttr;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setPublishTime(java.util.Date publishTime) {
    this.publishTime = publishTime;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setAuthorId(String authorId) {
    this.authorId = authorId;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public void setIsRecommend(Integer isRecommend) {
    this.isRecommend = isRecommend;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }



  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }


  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setImgs(String imgs) {
    this.imgs = imgs;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public void setOtherAttr(String otherAttr) {
    this.otherAttr = otherAttr;
  }
}