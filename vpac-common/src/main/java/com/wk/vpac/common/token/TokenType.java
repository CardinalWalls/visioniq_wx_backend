

package com.wk.vpac.common.token;



import com.base.components.common.token.TokenCacheObj;
import com.base.components.common.token.TokenTypes;
import com.wk.vpac.common.token.user.ExpertToken;
import com.wk.vpac.common.token.user.OperatorToken;
import com.wk.vpac.common.token.user.UserMemberToken;
import com.wk.vpac.common.token.user.UserToken;


/**
 * tokenObj类型
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2017-07-12 14:05
 */
public enum TokenType implements TokenTypes {

  USER_TOKEN(TokenConstant.USER_TOKEN_KEY, TokenConstant.USER_TOKEN_RECEIVE_KEY, UserToken.class, TokenSrcType.HEADER),

  EXPERT_TOKEN(TokenConstant.EXPERT_TOKEN_KEY, TokenConstant.EXPERT_TOKEN_RECEIVE_KEY, ExpertToken.class, TokenSrcType.HEADER),

  OPERATOR_TOKEN(TokenConstant.OPERATOR_TOKEN_KEY, TokenConstant.OPERATOR_TOKEN_RECEIVE_KEY, OperatorToken.class, TokenSrcType.HEADER),

  USER_MEMBER_TOKEN(TokenConstant.USER_MEMBER_TOKEN_KEY, TokenConstant.USER_MEMBER_TOKEN_RECEIVE_KEY, UserMemberToken.class, TokenSrcType.COOKIE),
;

  /** 请求中token值所在header或参数中的key */
  private String tokenKey;

  /** 请求转发时将缓存中token对象序列化后, 传递到下游服务的参数key */
  private String receiveJsonKey;

  /** 一种类型对应一个枚举 */
  private Class<? extends TokenCacheObj> typeClass;
  /** token 来源 */
  private TokenSrcType tokenSrcType;

  @Override
  public String getTokenKey() {
    return tokenKey;
  }

  @Override
  public String getReceiveJsonKey() {
    return receiveJsonKey;
  }

  @Override
  public Class<? extends TokenCacheObj> getTypeClass() {
    return typeClass;
  }

  @Override
  public TokenSrcType getTokenSrcType() {
    return tokenSrcType;
  }

  TokenType(String tokenKey, String receiveJsonKey, Class<? extends TokenCacheObj> typeClass,
            TokenSrcType tokenSrcType) {
    this.tokenKey = tokenKey;
    this.receiveJsonKey = receiveJsonKey;
    this.typeClass = typeClass;
    this.tokenSrcType = tokenSrcType;
  }

}
