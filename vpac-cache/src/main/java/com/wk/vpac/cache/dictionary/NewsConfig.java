package com.wk.vpac.cache.dictionary;

import com.base.components.cache.dictionary.DictionaryKeyValue;

/**
 * NewsConfig
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2022-11-04 16:26
 */
public enum NewsConfig implements DictionaryKeyValue<String> {
  /** 文章跳转地址，需包含 {id} */
  DETAIL_LINK;

  private String value;

  @Override
  public String defaultValue() {
    return "";
  }
}
