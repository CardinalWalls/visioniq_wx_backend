package com.wk.vpac.common.constants.work;

/**
 * WorkOrderStatus
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2023-01-04 15:07
 */
public enum WorkOrderStatus {
  WAIT_PUBLISH(-2, "待审核"),
  INVALID(-1, "无效"),
  EDIT(0, "未发布"),
  CANCEL(1, "已取消"),
  RECEIVING(2, "接单中"),
  WORKING(3, "进行中"),
  ACCEPTED(4, "已验收"),
  COMPLETE(5, "已完成"),


  ;
  private int value;
  private String desc;

  WorkOrderStatus(int value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public int getValue() {
    return value;
  }

  public String getDesc() {
    return desc;
  }

  public static boolean checkEdit(int status){
    return status == EDIT.value || status == RECEIVING.value || status == WORKING.value;
  }

  public static boolean checkReceiveEdit(int status){
    return status != WorkOrderStatus.CANCEL.getValue() && status != WorkOrderStatus.COMPLETE.getValue()
      && status != WorkOrderStatus.INVALID.getValue();
  }

  public static WorkOrderStatus parse(int status){
    for (WorkOrderStatus value : values()) {
      if(value.value == status){
        return value;
      }
    }
    return null;
  }
}
