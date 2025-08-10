package com.wk.vpac.common.constants.msgqueue.channels;

import com.base.components.common.constants.msgqueue.Channel;

/**
 * 消息Channel，用来区分listener的业务类别
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2017-11-14 15:57
 */
public enum MessageChannel implements Channel {

  /** 测试 */
  TEST_CHANNEL("测试"),
  /** 用户注册 */
  USER_REGISTER_CHANNEL("用户注册"),
  /** 用户登录*/
  USER_LOGIN("用户登录"),
  /** 购买会员*/
  VIP_PAY("购买会员"),
  /** 用户能力值改变*/
  USER_ABILITY("用户能力值改变"),
  /** 发票申请*/
  INVOICE_APPLY("发票申请"),
  ;

  private String desc;

  MessageChannel(String desc) {
    this.desc = desc;
  }

  @Override
  public String getDesc() {
    return desc;
  }

}
