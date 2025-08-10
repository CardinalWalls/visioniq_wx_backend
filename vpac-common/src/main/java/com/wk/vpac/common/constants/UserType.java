

package com.wk.vpac.common.constants;


import com.wk.vpac.common.token.TokenType;

/**
 * UserType
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2018-03-16 13:39
 */
public enum  UserType {

  /** 未分配角色 */
  UNKNOWN(null, 0),
  /** 普通注册用户 */
  USER(TokenType.USER_TOKEN, 1),
  /** 专家 */
  EXPERT(TokenType.EXPERT_TOKEN, 2),
  /** 客服运营 */
  OPERATOR(TokenType.OPERATOR_TOKEN, 3),

  ;

  private TokenType tokenType;
  private int code;

  UserType(TokenType tokenType, int code) {
    this.tokenType = tokenType;
    this.code = code;
  }

  public TokenType getTokenType() {
    return tokenType;
  }

  public int getCode() {
    return code;
  }

  public static UserType parse(int code){
    for (UserType userType : values()) {
      if(userType.code == code){
        return userType;
      }
    }
    return null;
  }
}
