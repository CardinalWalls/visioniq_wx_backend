package com.wk.vpac.common.constants.admin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用作统一管理后台特殊权限的代码
 *
 * @author <a href="tecyun@foxmail.com">Huangyunyang</a>
 * @version 1.0.0, 2018/6/12 0012 17:27
 */
public enum AuthCode {
  SYSTEM_STATISTICS("系统数据统计"),


  /**
   * 后台查看私有资源的权限
   */
  PRIVATE_ATTACHMENT_AUTH("后台查看私有资源的权限"),

  EXPERT_AUTH("专家角色"),

  COST_PAYMENT_MANAGER("平台成本管理"),

  FINANCE("财务角色")
  ;

  private String desc;

  AuthCode(String desc){
    this.desc = desc;
  }

  public static List<Map> getCodes(){
    ArrayList<Map> list = Lists.newArrayList();
    for (AuthCode authCode : values()) {
      HashMap<String, String> map = Maps.newHashMap();
      map.put("key", authCode.toString());
      map.put("val", authCode.getDesc());
      list.add(map);
    }
    return list;
  }

  public String getDesc(){
    return desc;
  }
}
