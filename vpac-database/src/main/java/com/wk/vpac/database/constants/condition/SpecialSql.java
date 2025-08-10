package com.wk.vpac.database.constants.condition;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * SpecialSql
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2023-09-19 15:14
 */
public class SpecialSql {
  private String sql;
  private Map<String, Object> params;

  public SpecialSql setSql(String sql){
    this.sql = sql;
    return this;
  }
  public SpecialSql addParam(String key, Object value){
    if(params == null){
      params = Maps.newHashMap();
    }
    params.put(key, value);
    return this;
  }

  public String getSql() {
    return sql;
  }

  public Map<String, Object> getParams() {
    return params;
  }
}
