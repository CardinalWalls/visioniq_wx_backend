

package com.wk.vpac.common.token;

/**
 * Token常量
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2017-07-11 15:32
 */
public interface TokenConstant {
  /**
   * user token 的 key
   */
  String USER_TOKEN_KEY = "token";
  /**
   * expert token 的 key
   */
  String EXPERT_TOKEN_KEY = "expert-token";
  /** 客服运营 token key */
  String OPERATOR_TOKEN_KEY = "operator-token";
  /**
   * 后台 token 的 key
   */
  String USER_MEMBER_TOKEN_KEY = "member_token";


  /**
   * 传递注册用户对象对应的key
   */
  String USER_TOKEN_RECEIVE_KEY = "_user_token_json_";

  /**
   * 传递注册专家用户对象对应的key
   */
  String EXPERT_TOKEN_RECEIVE_KEY = "_expert_token_json_";

  /**
   * 传递注册客服运营用户对象对应的key
   */
  String OPERATOR_TOKEN_RECEIVE_KEY = "_operator_token_json_";
  /**
   * 传递后台用户对象对应的key
   */
  String USER_MEMBER_TOKEN_RECEIVE_KEY = "_user_member_token_json_";
}
