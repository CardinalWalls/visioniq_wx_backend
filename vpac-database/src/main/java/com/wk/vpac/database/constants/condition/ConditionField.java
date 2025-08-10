package com.wk.vpac.database.constants.condition;

import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.Arrays;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * ConditionField
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2023-09-19 10:21
 */
public class ConditionField implements Serializable {
  @Serial
  private static final long serialVersionUID = 4810428600858628346L;

  /** 名称 */
  private String name;

  /** 字段 */
  private String column;

  /** 类型 */
  private Type type;
  /** 特殊sql构造 */
  private SpecialSqlBuilder specialSqlBuilder;

  private Function<Object, Object> specialValueBuilder;

  public ConditionField(String name, String column) {
    this(name, column, null, (SpecialSqlBuilder) null);
  }

  public ConditionField(String name, String column, Type type) {
    this(name, column, type, (SpecialSqlBuilder) null);
  }

  public ConditionField(String name, String column, Type type, SpecialSqlBuilder specialSqlBuilder) {
    Assert.hasText(name, ()->"name could not empty");
    Assert.hasText(column, ()->"column could not empty");
    this.name = name;
    this.column = column;
    this.type = type == null ? Type.TEXT : type;
    this.specialSqlBuilder = specialSqlBuilder;
  }

  public ConditionField(String name, String column, Type type, Function<Object, Object> specialValueBuilder) {
    Assert.hasText(name, ()->"name could not empty");
    Assert.hasText(column, ()->"column could not empty");
    this.name = name;
    this.column = column;
    this.type = type == null ? Type.TEXT : type;
    this.specialValueBuilder = specialValueBuilder;
  }

  public String getName() {
    return name;
  }

  public String getColumn() {
    return column;
  }

  public Type getType() {
    return type;
  }

  public SpecialSqlBuilder getSpecialSqlBuilder() {
    return specialSqlBuilder;
  }

  public Function<Object, Object> getSpecialValueBuilder() {
    return specialValueBuilder;
  }

  public enum Type{
    TEXT,
    INT(Integer.class),
    DOUBLE(BigDecimal.class),
    GENDER(Integer.class),
    REGION,
    RISK_LEVEL(Integer.class)
    ;
    private Class<?> valueType = String.class;
    Type() {}
    Type(Class<?> valueType) {
      this.valueType = valueType;
    }
    public Class<?> getValueType() {
      return valueType;
    }
  }

  private static Set<String> OPERATORS = Sets.newHashSet("=",">","<",">=","<=","!=","IN");

  /**
   * 构建sql
   * @param columnMap       以column为key
   * @param conditionJson   例：{"join":"AND","groups":[{"join":"AND","items":[{"column":"gender","operator":"=","value":"1","type":"GENDER","alias":"性别"}]}]}
   * @param tableAlias      表别名
   */
  public static void build(DynamicSqlBuilder sqlBuilder, Map<String, ConditionField> columnMap, String conditionJson, String tableAlias){
    if (StringUtils.isBlank(conditionJson)) {
      return;
    }
    ObjectNode condition;
    try {
      condition = JsonUtils.reader(conditionJson, ObjectNode.class);
    } catch (IOException e) {
      throw new IllegalArgumentException("条件值格式错误", e);
    }
    JsonNode groups = condition.path("groups");
    if(!groups.isEmpty()){
      String allJoin = "AND".equalsIgnoreCase(condition.path("join").asText())?"AND":"OR";
      Set<String> groupsSql = Sets.newLinkedHashSet();
      int i = 0;
      for (JsonNode group : groups) {
        JsonNode items = group.path("items");
        if(!items.isEmpty()){
          Set<String> itemsSql = Sets.newLinkedHashSet();
          String join = "AND".equalsIgnoreCase(group.path("join").asText())?"AND":"OR";
          for (JsonNode item : items) {
            String column = item.path("column").asText();
            String operator = item.path("operator").asText();
            ConditionField field = columnMap.get(column);
            if(field != null && OPERATORS.contains(operator)){
              Object value = getValue(field, item.path("value"), "IN".equalsIgnoreCase(operator));
              if(value != null && StringUtils.isNotBlank(value.toString())){
                SpecialSqlBuilder builder = field.getSpecialSqlBuilder();
                if(builder != null){
                  SpecialSql sql = builder.build(i, operator, value, tableAlias);
                  if(sql != null && StringUtils.isNotBlank(sql.getSql())){
                    itemsSql.add(" " + sql.getSql() + " ");
                    Map<String, Object> params = sql.getParams();
                    if(params != null){
                      for (Map.Entry<String, Object> entry : params.entrySet()) {
                        sqlBuilder.setParameter(entry.getKey(), entry.getValue(), tableAlias);
                      }
                    }
                  }
                }
                else{
                  Function<Object, Object> valueBuilder = field.getSpecialValueBuilder();
                  if(valueBuilder != null){
                    value = valueBuilder.apply(value);
                  }
                  itemsSql.add(" " + tableAlias + "." + column + " " + operator + " (:_" + column + "_" + i + ") ");
                  sqlBuilder.setParameter("_" + column + "_" + i, value, tableAlias);
                }
                i++;
              }
            }
          }
          if(!itemsSql.isEmpty()){
            groupsSql.add("(" + StringUtils.join(itemsSql, join) + ")");
          }
        }
      }
      if(!groupsSql.isEmpty()){
        sqlBuilder.addWhereSql("AND (" + StringUtils.join(groupsSql, allJoin) + ")");
      }
    }
  }

  private static Object getValue(ConditionField field, JsonNode value, boolean isIn){
    if(value instanceof MissingNode || value instanceof NullNode){
      return null;
    }
    String src = value.asText();
    Object targetVal = value.asText();
    if(!isIn){
      targetVal = ConvertUtil.convert(targetVal, field.getType().getValueType());
      Assert.notNull(targetVal, ()-> field.getName() + " 格式错误：" + src);
      return targetVal;
    }
    else{
      String[] array = StringUtils.split(src, ",");
      if(!Arrays.isNullOrEmpty(array)){
        Set<Object> set = Sets.newHashSet();
        for (String s : array) {
          Object v = ConvertUtil.convert(s, field.getType().getValueType());
          Assert.notNull(v, ()-> field.getName() + " 格式错误：" + src);
          set.add(s);
        }
        return set.isEmpty() ? null : set;
      }
    }
    return null;
  }
}
