package com.wk.vpac.common.service.service.sms.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.base.components.common.exception.business.BusinessException;
import com.base.components.common.exception.business.SmsException;
import com.base.components.common.service.ThreadPoolService;
import com.base.components.common.util.JsonUtils;
import com.wk.vpac.common.constants.sms.SmsTemplateId;
import com.wk.vpac.common.service.sms.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 短信服务具体实现
 *
 * @author <a href="tecyun@foxmail.com">Huangyunyang</a>
 * @version 1.0.0, 2018/3/26 0026 14:10
 */
@Service
//@RefreshScope
@ConditionalOnProperty(name = "base.sms.impl", havingValue = "Aliyun")
public class AliyunSmsService implements SmsService {

  private static final Logger logger = LoggerFactory.getLogger(AliyunSmsService.class);

  @Autowired
  private ThreadPoolService threadPoolService;

  @Value("${base.sms.Aliyun.accessKeyId}")
  private String accessKeyId;

  @Value("${base.sms.Aliyun.accessKeySecret}")
  private String accessKeySecret;

  @Value("${base.sms.Aliyun.regionId}")
  private String regionId;

  @Value("${base.sms.Aliyun.signName}")
  private String signName;

  @Value("${base.sms.Aliyun.timeout}")
  private String timeout;

  @Override
  public boolean enabled() {
    return true;
  }

  /**
   * 异步发送短信
   *
   * @param phone             非空，手机号
   * @param content           可空，发送内容，根据服务商规则与templateId二选一选填
   * @param templateId        可空，发送内容模板ID，根据服务商规则与content二选一选填
   * @param extras            可空，拓展参数，根据具体的短信服务实现传参，参数格式见{@link SmsTemplateId}
   */
  @Override
  public void sendSmsAsync(String phone, String content, SmsTemplateId templateId, Map<String, Object> extras) {
    Assert.isTrue(templateId != null, "短信模板为空");
    threadPoolService.run(() -> sendSms(phone, templateId.getTemplateId(), extras));
  }

  @Override
  public boolean sendSmsSync(String phone, String content, SmsTemplateId templateId, Map<String, Object> extras) {
    Assert.isTrue(templateId != null, "短信模板为空");
    return sendSms(phone, templateId.getTemplateId(), extras);
  }

  @Override
  public void sendSmsAsync(String phone, String content, String templateId, Map<String, Object> extras) {
    Assert.hasText(templateId, "短信模板为空");
    threadPoolService.run(() -> sendSms(phone, templateId, extras));
  }

  @Override
  public boolean sendSmsSync(String phone, String content, String templateId, Map<String, Object> extras) {
    Assert.hasText(templateId, "短信模板为空");
    return sendSms(phone, templateId, extras);
  }

  private boolean sendSms(String phone, String templateId, Map<String, Object> extras) {
    //设置超时时间-可自行调整
    System.setProperty("sun.net.client.defaultConnectTimeout", timeout);
    System.setProperty("sun.net.client.defaultReadTimeout", timeout);
    //初始化ascClient需要的几个参数
    //短信API产品名称（短信产品名固定，无需修改）
    final String product = "Dysmsapi";
    //短信API产品域名（接口地址固定，无需修改）
    final String domain = "dysmsapi.aliyuncs.com";
    //初始化ascClient,暂时不支持多region（请勿修改）
    IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId,
                                                       accessKeySecret);
    try {
      DefaultProfile.addEndpoint(regionId, regionId, product, domain);
      IAcsClient acsClient = new DefaultAcsClient(profile);
      //组装请求对象
      SendSmsRequest request = new SendSmsRequest();
      //使用post提交
      request.setMethod(MethodType.POST);
      //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
      request.setPhoneNumbers(phone);
      //必填:短信签名-可在短信控制台中找到
      request.setSignName(signName);
      //必填:短信模板-可在短信控制台中找到
      request.setTemplateCode(templateId);
      //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
      //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
      request.setTemplateParam(JsonUtils.toString(extras));
      //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
      //request.setSmsUpExtendCode("90997");
      //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//      request.setOutId("yourOutId");
      //请求失败这里会抛ClientException异常

      SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
      if(sendSmsResponse.getCode() != null && "OK".equals(sendSmsResponse.getCode())) {
        logger.info("短信发送成功："+phone+",templateId:"+templateId);
        //请求成功
      } else {
        if("isv.BUSINESS_LIMIT_CONTROL".equals(sendSmsResponse.getCode())){
          throw new SmsException(
            SmsException.ErrorCode.count_limit.getInfo().replace("{count}", sendSmsResponse.getMessage()),
            SmsException.ErrorCode.count_limit.getCode());
        }
        throw new BusinessException("阿里云通信异常，发送短信失败 -> " + sendSmsResponse.getMessage() + ", code = " + sendSmsResponse.getCode());
        //logger.error("阿里云通信异常，发送短信失败 -> " + sendSmsResponse.getMessage() + ", code = " + sendSmsResponse.getCode());
      }
    } catch (Exception e) {
      logger.error("发送短信消息失败（"+templateId+"）", e);
      return false;
    }
    return true;
  }
}
