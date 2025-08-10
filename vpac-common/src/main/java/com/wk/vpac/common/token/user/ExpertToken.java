package com.wk.vpac.common.token.user;

import com.base.components.common.doc.annotation.Param;

import java.io.Serial;

/**
 * ExpertToken
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2023-01-03 21:40
 */
public class ExpertToken extends UserToken {
  @Serial
  private static final long serialVersionUID = 765611454591528507L;
  @Param("专家ID")
  private String expertId;

  public String getExpertId() {
    return expertId;
  }

  public void setExpertId(String expertId) {
    this.expertId = expertId;
  }
}
