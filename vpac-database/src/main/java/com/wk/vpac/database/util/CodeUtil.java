package com.wk.vpac.database.util;

import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.dao.base.GenericJpaDao;

/**
 * CodeUtil
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2019/8/16 0016 10:00
 */
public class CodeUtil {

  /**
   * 检查某个编码出现的次数
   * @param r dao
   * @param codeFieldName 编码属性名称
   * @param codeValue 要检测的编码值
   * @param <R>
   * @return 编码出现的数量
   */
  public static <R extends GenericJpaDao> long codeCount(R r, String codeFieldName, String codeValue) {
    ConditionGroup condition = ConditionGroup.build();
    return r.count(condition.addCondition(codeFieldName, ConditionEnum.OPERATE_EQUAL, codeValue));
  }

}
