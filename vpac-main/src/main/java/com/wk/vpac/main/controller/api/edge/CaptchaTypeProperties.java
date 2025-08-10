package com.wk.vpac.main.controller.api.edge;

import com.base.components.common.exception.business.SmsException;
import com.wk.vpac.cache.CacheName;
import com.wk.vpac.common.constants.sms.SmsTemplateId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * CaptchaTypeProperties 发短信-不同验证码类型对应的属性配置
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2019/6/13 0013 13:39
 */
public class CaptchaTypeProperties {

  public static final String TYPES = "0=登录，1=注册，2=找回密码，3=修改手机号，4=绑定手机号，5=余额支付，6=修改邮箱或者验证原手机号，7、赠送积分";
  private static final Map<Integer, CaptchaTypeProperties> TYPE_MAP = new ConcurrentHashMap<>();

  private CaptchaTypeProperties(int type, CacheName preCodeCache, CacheName codeCache, SmsTemplateId smsTemplateId) {
    this.type = type;
    this.preCodeCache = preCodeCache;
    this.codeCache = codeCache;
    this.smsTemplateId = smsTemplateId;
  }
  private int type;
  /**
   * 发短信前的验证码
   */
  private CacheName preCodeCache;
  /**
   * 短信验证码
   */
  private CacheName codeCache;
  /**
   * 短信模板ID
   */
  private SmsTemplateId smsTemplateId;

  public CacheName getPreCodeCache() {
    return preCodeCache;
  }

  public CacheName getCodeCache() {
    return codeCache;
  }

  public SmsTemplateId getSmsTemplateId() {
    return smsTemplateId;
  }

  public void check(Supplier<Boolean> checkSuccess, SmsException.ErrorCode smsErrorCode, int ... types){
    if(smsErrorCode != null && checkSuccess != null && types != null){
      for (int i : types) {
        if(type == i){
          SmsException.assertIsTrue(checkSuccess.get(), smsErrorCode);
          return;
        }
      }
    }
  }

  /**
   * @param type 短信类型
   * @see #TYPES
   */
  public static CaptchaTypeProperties get(int type) {
    return getFromMap(type);
  }

  private static CaptchaTypeProperties getFromMap(int type) {
    CaptchaTypeProperties properties = TYPE_MAP.get(type);
    if (properties == null) {
      switch (type) {
        //登录
        case 0:
          properties = new CaptchaTypeProperties(0, CacheName.PRE_SMS_CODE_LG, CacheName.VERIFY_CODE_LG,
                                                 SmsTemplateId.ALiSMS_Login);
          break;
        // 注册
        case 1:
          properties = new CaptchaTypeProperties(1, CacheName.PRE_SMS_CODE_RG, CacheName.VERIFY_CODE_RG,
                                                 SmsTemplateId.ALiDaYu_Register);
          break;
        // 找回密码
        case 2:
          properties = new CaptchaTypeProperties(2, CacheName.PRE_SMS_CODE_CP, CacheName.VERIFY_CODE_CP,
                                                 SmsTemplateId.ALiDaYu_ChangePwd);
          break;
        // 修改手机号
        case 3:
          properties = new CaptchaTypeProperties(3, CacheName.PRE_SMS_CODE_PHONE, CacheName.VERIFY_CODE_PHONE,
                                                 SmsTemplateId.ALiSMS_PhoneChange);
          break;
        // 绑定手机号
        case 4:
          properties = new CaptchaTypeProperties(4, CacheName.PHONE_BIND, CacheName.VERIFY_PHONE_BIND,
                                                 SmsTemplateId.ALiSMS_PhoneBind);
          break;
        // 余额支付
        case 5:
          properties = new CaptchaTypeProperties(5, CacheName.PRE_BALANCE_PAY, CacheName.BALANCE_PAY,
                                                 SmsTemplateId.ALiDaYu_balance_pay);
          break;
        // 修改邮箱或者验证原手机号
        case 6:
          properties = new CaptchaTypeProperties(6, CacheName.PRE_SMS_CODE_EMAIL, CacheName.VERIFY_CODE_EMAIL,
                                                 SmsTemplateId.ALiSMS_set_email);
          break;
        default:
          throw new SmsException(SmsException.ErrorCode.type_error);
      }
      TYPE_MAP.put(type, properties);
    }
    return properties;
  }


}
