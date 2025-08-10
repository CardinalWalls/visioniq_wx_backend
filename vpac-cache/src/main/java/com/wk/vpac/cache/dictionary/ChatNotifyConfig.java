package com.wk.vpac.cache.dictionary;

import com.base.components.cache.dictionary.DictionaryKeyValue;
import com.base.components.common.boot.SpringContextUtil;
import com.base.components.common.service.cache.DictionaryCacheService;
import org.joda.time.DateTime;
import org.springframework.lang.NonNull;

/**
 * ChatNotifyConfig
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2023-02-19 21:57
 */
public enum ChatNotifyConfig implements DictionaryKeyValue<Object> {

  /** 短信通知间隔分钟数，默认30分钟 */
  SMS_LIMIT_MINUTES(30);

  ChatNotifyConfig(Object defaultValue) {
    this.defaultValue = defaultValue;
  }

  private final Object defaultValue;

  @NonNull
  @Override
  public Object defaultValue() {
    return defaultValue;
  }


  /** 获取能发送短信通知的最大时间线 */
  public static DateTime getNotifyLimitTimeLine(){
    int limit = (int) SpringContextUtil.getBean(DictionaryCacheService.class).listDataVal(ChatNotifyConfig.SMS_LIMIT_MINUTES);
    return DateTime.now().minusMinutes(limit);
  }
}
