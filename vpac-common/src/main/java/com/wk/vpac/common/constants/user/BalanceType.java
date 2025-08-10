package com.wk.vpac.common.constants.user;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.TreeMap;

/**
 * BalanceType - 交易账户
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2019-06-13 11:34
 */
public enum BalanceType {
  /** 无 */
  NONE(0, "", "", "无"),

  /** 余额 */
  BALANCE(1, "balance", "balance", "余额"),

  /** 积分 */
  INTEGRAL(2, "integral", "integral", "积分"),

  /** 能力值 */
  ABILITY(3, "ability", "ability", "能力值"),

  /** 分销收益 */
  DISTRIBUTION(4, "distribution", "distribution", "分销收益"),
  ;

  private int code;
  private String field;
  private String column;
  private boolean canWithdraw;
  private String desc;

  BalanceType(int code, String field, String column, String desc) {
    this.code = code;
    this.field = field;
    this.column = column;
    this.canWithdraw = false;
    this.desc = desc;
  }

  BalanceType(int code, String field, String column, boolean canWithdraw) {
    this.code = code;
    this.field = field;
    this.column = column;
    this.canWithdraw = canWithdraw;
  }

  public int getCode() {
    return code;
  }

  public String getField() {
    return field;
  }

  public String getColumn() {
    return column;
  }

  public boolean getCanWithdraw() {
    return canWithdraw;
  }

  private static final TreeMap<Integer, String> DESC_MAP = Maps.newTreeMap();
  static {
    for (BalanceType value : values()) {
      DESC_MAP.put(value.code, value.desc);
    }
  }
  public static Map<Integer, String> getDescMap(){
    return Maps.newTreeMap(DESC_MAP);
  }
}
