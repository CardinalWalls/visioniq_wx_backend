

package com.wk.vpac.cache;


import com.base.components.cache.Nameable;

/**
 * 缓存类别，对应 xxx-cache.yml 中 base.common.cache.expires
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2017-11-16 09:39
 */
public enum CacheName implements Nameable {

  /**
   * admin端，登录权限
   */
  ADMIN_AUTH_INFO,


  /**
   * 登录-短信验证码
   */
  PRE_SMS_CODE_LG,
  VERIFY_CODE_LG,

  /**
   * 注册-短信验证码
   */
  PRE_SMS_CODE_RG,
  VERIFY_CODE_RG,

  /**
   * 修改密码-短信验证码
   */
  PRE_SMS_CODE_CP,
  VERIFY_CODE_CP,

  /**
   * 修改手机号-短信验证码
   */
  PRE_SMS_CODE_PHONE,
  VERIFY_CODE_PHONE,

  /**
   * 提现-短信验证码
   */
  PRE_SMS_CODE_TX,
  VERIFY_CODE_TX,

  /**
   * 修改邮箱-验证码
   */
  PRE_SMS_CODE_EMAIL,
  VERIFY_CODE_EMAIL,

  /**
   * 余额支付-验证码
   */
  PRE_BALANCE_PAY,
  BALANCE_PAY,

  /**
   * JS加解密代码
   */
  JS_DECRYPT,

  /**
   * 附件id对应url
   */
  ATTACHMENT_ID_URL,
  /**
   * 附件id对应的权限列表
   */
  ATTACHMENT_ID_LIST,

  /**
   * 用户一个小时内密码输入错误
   */
  PASSWORD_ERROR,

  /**
   * 微信绑定
   */
  WECHAT_BIND,

  /**
   * 用户上次发送短信验证码的时间,用作发送间隔限制
   */
  USER_SMS_LASTTIME,

  /**
   * 是否是当天第一次登录
   */
  FIRST_LOGIN_OF_DAY,

  /**
   * 微信登录重定向临时对象
   */
  WECHAT_LOGIN_TOKENOBJ,


  PHONE_BIND,
  VERIFY_PHONE_BIND,


  /** 一般表单图形验证码 */
  NORMAL_CAPTCHA_IMG,


  /** 小程序openid缓存 */
  OPENID,
  ;
}
