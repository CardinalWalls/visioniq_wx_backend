package com.wk.vpac.common.constants.admin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AuthUriMapping - 使用在Admin的controller方法上，用于带有PathVariable的Uri权限匹配，如： /admin/user/{userId}
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2019-04-24 9:56
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthUriMapping {

  /** 带有PathVariable的Uri，如： /admin/user/{userId} */
  String[] value();
}
