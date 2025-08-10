package com.wk.vpac.domain.operations;

import com.base.components.common.constants.db.Domain;
import com.base.components.common.constants.sys.Dates;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.id.jpa.JpaId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;



/**
 * 广告
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-01-21
 */
@Entity
@Table(name = "base_advertisement")
@DynamicUpdate
public class Advertisement implements Domain {  
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @JpaId
  @GeneratedValue(generator = JPA_ID_GENERATOR_NAME)
  @Column(name = "id", nullable = false)
  @Param(value = "id", required = true)
  private String id;
  
  /** group_id - group_id */
  @Column(name = "group_id")
  @Param("group_id")
  private String groupId;

  /** descri - 描述 */
  @Column(name = "descri")
  @Param("描述")
  private String descri;

  /** position_code - 位置编码 */
  @Column(name = "position_code")
  @Param("位置编码")
  private String positionCode;

  /** img - 图片地址 */
  @Column(name = "img")
  @Param("图片地址")
  private String img;

  /** width - 宽 */
  @Column(name = "width")
  @Param("宽")
  private Integer width;

  /** height - 高 */
  @Column(name = "height")
  @Param("高")
  private Integer height;

  /** url - 链接地址 */
  @Column(name = "url")
  @Param("链接地址")
  private String url;

  /** click_count - 点击数量 */
  @Column(name = "click_count", nullable = false)
  @Param("点击数量")
  private Integer clickCount = 0;

  /** create_time - create_time */
  @Column(name = "create_time")
  @Param("create_time")
  private java.util.Date createTime;

  /** status - 状态; 0禁用,1启用 */
  @Column(name = "status", nullable = false)
  @Param("状态; 0禁用,1启用")
  private Integer status = 0;

  /** remark - 备注 */
  @Column(name = "remark")
  @Param("备注")
  private String remark;

  /** order_no - 排序号 */
  @Column(name = "order_no")
  @Param("排序号")
  private Integer orderNo;

  /** begin_time - begin_time */
  @Column(name = "begin_time")
  @Param("begin_time")
  @DateTimeFormat(pattern = Dates.DATE_TIME_FORMATTER_PATTERN)
  private java.util.Date beginTime;

  /** end_time - end_time */
  @Column(name = "end_time")
  @Param("end_time")
  @DateTimeFormat(pattern = Dates.DATE_TIME_FORMATTER_PATTERN)
  private java.util.Date endTime;

  
  public String getId() {
    return this.id;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getDescri() {
    return descri;
  }

  public String getPositionCode() {
    return positionCode;
  }

  public String getImg() {
    return img;
  }

  public Integer getWidth() {
    return width;
  }

  public Integer getHeight() {
    return height;
  }

  public String getUrl() {
    return url;
  }

  public Integer getClickCount() {
    return clickCount;
  }

  public java.util.Date getCreateTime() {
    return createTime;
  }

  public Integer getStatus() {
    return status;
  }

  public String getRemark() {
    return remark;
  }

  public Integer getOrderNo() {
    return orderNo;
  }

  public java.util.Date getBeginTime() {
    return beginTime;
  }

  public java.util.Date getEndTime() {
    return endTime;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public void setDescri(String descri) {
    this.descri = descri;
  }

  public void setPositionCode(String positionCode) {
    this.positionCode = positionCode;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setClickCount(Integer clickCount) {
    this.clickCount = clickCount;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setOrderNo(Integer orderNo) {
    this.orderNo = orderNo;
  }

  public void setBeginTime(java.util.Date beginTime) {
    this.beginTime = beginTime;
  }

  public void setEndTime(java.util.Date endTime) {
    this.endTime = endTime;
  }

}