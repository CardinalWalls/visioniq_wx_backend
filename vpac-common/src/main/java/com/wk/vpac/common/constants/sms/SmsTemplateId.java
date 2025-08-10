

package com.wk.vpac.common.constants.sms;

/**
 * 短信模板ID
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2017-08-18 13:54
 */
public enum SmsTemplateId {
  /**
   * 登录确认验证码 -
   * <p> 拓展参数：{"code":"123456"}
   * <p> code: 验证码
   */
  ALiSMS_Login("SMS_268700617"),

  /**
   * 登录异常验证码 -
   * <p> 拓展参数：{"code":"123456"}
   * <p> code: 验证码
   */
  ALiSMS_Login_Exc("SMS_268700617"),

  /**
   *修改手机号码 -
   * <p> code
   */
  ALiSMS_PhoneChange("SMS_268700617"),

  /**
   * 用户注册验证码 -
   * <p> 拓展参数：{"code":"123456"}
   * <p> code: 验证码
   */
  ALiDaYu_Register("SMS_268700617"),
  /**
   * 修改密码验证码 -
   * <p> 拓展参数：{"code":"123456"}
   * <p> code: 验证码
   */
  ALiDaYu_ChangePwd("SMS_268700617"),



  /**
   * 用户需要激活 -
   * <p> 拓展参数：{"phone":"123456"}
   */
//  ALiDaYu_Need_Activated("SMS_169641221"),

  /**
   * 用户已激活 -
   */
//  ALiDaYu_Activated("SMS_169636135"),

  /**
   * 已下单 -
   * <p> orderCode 订单编号
   */
//  ALiSMS_ordered("SMS_169641222"),

  /**
   * 已支付 -
   * <p> amount 金额
   * <p> orderCode 订单编号
   */
//  ALiSMS_Payed("SMS_169641223"),
  /**
   * 已发货 -
   * <p> orderCode 订单编号  {"expressCompany":"快递公司", "expressCode":"快递单号"}
   */
  ALiSMS_Delivered("SMS_215067299"),
  /**
   * 提现申请 -
   * <p> code
   */
//  ALiSMS_withdraw("SMS_169641182"),
  /**
   * 提现成功 -
   * <p> amount
   */
//  ALiSMS_withdraw_success("SMS_169636111"),
  /**
   * 提现失败 -
   * <p> amount
   */
//  ALiSMS_withdraw_fail("SMS_169641233"),

  /**
   * 赠送积分 -
   * <p> code: 验证码
   */
//  ALiDaYu_integral_gift("SMS_169641238"),



  /**
   * --------用户余额支付
   * <p> code: 验证码
   */
  ALiDaYu_balance_pay("SMS_187545710"),


  /**
   * --------邮箱设置
   * <p> code
   */
  ALiSMS_set_email("SMS_187545710"),

  /**
   * --------绑定手机
   * <p> code: 验证码
   */
  ALiSMS_PhoneBind("SMS_193517363"),

  /**
   * --------回复问答
   * <p> expertName: 专家姓名
   */
//  ALiSMS_QuestionReply("SMS_200190645"),

  /**
   * --------开通VIP
   * <p> vipName: VIP名称
   * <p> expireTime: 有效期至
   */
//  ALiSMS_VipOpen("SMS_205440426"),
  /**
   * --------VIP续费
   * <p> vipName: VIP名称
   * <p> expireTime: 有效期至
   */
//  ALiSMS_VipRenew("SMS_205435489"),
  /**
   * --------积分兑换成功
   * <p> productName: 商品名称
   */
//  ALiSMS_IntegralExchange("SMS_200190659"),
  /**
   * --------商品购买成功
   * <p> productName: 商品名称
   */
//  ALiSMS_PaySuccess("SMS_201683572"),

  /**
   * --------积分赠送
   * <p> value: 积分数额
   */
//  ALiSMS_GiveIntegral("SMS_206551489"),
  /**
   * --------发票申请
   * <p> createTime: 申请时间
   * <p> status: 过程状态（已开票/已驳回）
   */
  ALiSMS_Invoice("SMS_215122278"),

  /**
   * ---------服务订单，派单通知服务人员
   */
  ALiSMS_ProductOrderToManager("SMS_215354174"),
  /**
   * ---------生成账单，通知服务人员
   * <p> name : 姓名
   * <p> billMonth : 月份
   */
  ALiSMS_CreateMonthBillToManager("SMS_215339268"),

  /**
   * --------- 待办事件通知后台管理员
   * <p> name : 事件名称
   */
  ALiSMS_TodoToAdmin("SMS_217429312"),



  /**
   * --------- 消息会话未读通知
   * <p> name : 发件信人姓名
   * <p> appName : 小程序名称
   */
  ALiSMS_CHAT_NOTIFY("SMS_270310599")
  ;
  private String templateId;

  SmsTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public String getTemplateId() {
    return templateId;
  }

}
