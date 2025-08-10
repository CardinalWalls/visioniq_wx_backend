package com.wk.vpac.common.constants.com;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * WorkOrderStatus
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2023-01-04 15:07
 */
public enum PayStatus {
  FAIL(-1, "支付失败"),
  TODO(0, "待支付"),
  PAYING(1, "支付中"),
  PAYED(2, "已支付"),


  ;
  private int value;
  private String desc;

  PayStatus(int value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public int getValue() {
    return value;
  }

  public String getDesc() {
    return desc;
  }

  public static Set<Integer> canPayStatus(){
    return ImmutableSet.of(FAIL.value, TODO.value);
  }

  public static Set<Integer> paymentStatus(){
    return ImmutableSet.of(PAYING.value, PAYED.value);
  }

  public static PayStatus parse(int status){
    for (PayStatus value : values()) {
      if(value.value == status){
        return value;
      }
    }
    return null;
  }
}
