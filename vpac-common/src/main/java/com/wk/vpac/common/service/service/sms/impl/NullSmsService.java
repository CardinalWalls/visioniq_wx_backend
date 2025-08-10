package com.wk.vpac.common.service.service.sms.impl;

import com.base.components.common.util.Logs;
import com.wk.vpac.common.constants.sms.SmsTemplateId;
import com.wk.vpac.common.service.sms.SmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 短信服务具体实现
 *
 * @author <a href="tecyun@foxmail.com">Huangyunyang</a>
 * @version 1.0.0, 2018/3/26 0026 14:10
 */
@Service
@ConditionalOnProperty(name = "base.sms.impl", havingValue = "Null", matchIfMissing = true)
public class NullSmsService implements SmsService {

  @Override
  public boolean enabled() {
    return false;
  }

  /**
   * 异步发送短信
   *
   * @param phone 非空，手机号
   * @param content 可空，发送内容，根据服务商规则与templateId二选一选填
   * @param templateId 可空，发送内容模板ID，根据服务商规则与content二选一选填
   * @param extras 可空，拓展参数，根据具体的短信服务实现传参，参数格式见{@link SmsTemplateId}
   */
  @Override
  public void sendSmsAsync(String phone, String content, SmsTemplateId templateId, Map<String, Object> extras) {
    Logs.get().info("phone: {}, templateId: {}, extras: {}", phone, templateId, extras);
  }

  @Override
  public boolean sendSmsSync(String phone, String content, SmsTemplateId templateId, Map<String, Object> extras) {
    Logs.get().info("phone: {}, templateId: {}, extras: {}", phone, templateId, extras);
    return false;
  }

  @Override
  public void sendSmsAsync(String phone, String content, String templateId, Map<String, Object> extras) {
    Logs.get().info("phone: {}, templateId: {}, extras: {}", phone, templateId, extras);
  }

  @Override
  public boolean sendSmsSync(String phone, String content, String templateId, Map<String, Object> extras) {
    Logs.get().info("phone: {}, templateId: {}, extras: {}", phone, templateId, extras);
    return false;
  }
}