package com.wk.vpac.common.constants.sys;

import com.base.components.cache.dictionary.DictionaryKeys;

/**
 * ChatNotifyConfig
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2023-02-19 21:57
 */
public enum ChatAutoReplyType implements DictionaryKeys {
  NORMAL(0, false,"非系统消息"),
  SYSTEM_PUSH(1, false,"系统推送"),
  AI(2, true,"AI回复"),
  NEW_USER_SYSTEM_GREETING(3, true,"新用户系统问候"),
  NEW_USER_EXPERT_GREETING(4, true,"新用户医生问候"),

  VISION_INTERVENE(5, false,"视力干预"),


  ;

  public static final String DOC = "0=非系统消息, 1=系统推送, 2=AI回复, 3=新用户系统问候, 4=新用户医生问候, 5=干预方式（内容为JSON，参考视力干预接口）";
  public static final String AUTO_REPLY_DOC = "2=AI回复, 3=新用户系统问候, 4=新用户医生问候";

  ChatAutoReplyType(int val, boolean autoReply, String desc) {
    this.val = val;
    this.desc = desc;
    this.autoReply = autoReply;
  }

  private int val;
  private boolean autoReply;
  private String desc;


  public int getVal() {
    return val;
  }

  public String getDesc() {
    return desc;
  }

  public boolean isAutoReply() {
    return autoReply;
  }
}
