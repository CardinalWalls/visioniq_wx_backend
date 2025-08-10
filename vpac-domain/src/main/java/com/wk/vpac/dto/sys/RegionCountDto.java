

package com.wk.vpac.dto.sys;

import com.base.components.common.dto.convert.Convertible;
import com.wk.vpac.domain.sys.Region;

import java.io.Serializable;



/**
 * 地区 代业务统计数
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-03-15
 */
public class RegionCountDto extends Region implements Serializable, Convertible {
  private static final long serialVersionUID = 1L;
  /** 业务计数 */
  private Integer count;

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}