package com.wk.vpac.database.constants.condition;

import org.springframework.lang.NonNull;

/**
 * SpecialSql
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2023-09-19 15:13
 */
@FunctionalInterface
public interface SpecialSqlBuilder {

  SpecialSql build(int index, @NonNull String operator, @NonNull Object value, @NonNull String tableAlias);
}
