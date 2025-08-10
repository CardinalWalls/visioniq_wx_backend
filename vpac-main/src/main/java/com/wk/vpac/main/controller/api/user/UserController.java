

package com.wk.vpac.main.controller.api.user;

import com.base.components.cache.CacheManager;
import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestBodyModel;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.doc.annotation.Token;
import com.base.components.common.doc.validation.EL;
import com.base.components.common.dto.dictionary.DictionaryNode;
import com.base.components.common.exception.business.BusinessException;
import com.base.components.common.exception.business.PasswordErrorException;
import com.base.components.common.exception.business.PasswordErrorLimitException;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.service.cache.DictionaryCacheService;
import com.base.components.common.token.RequireToken;
import com.base.components.common.token.TokenCacheObj;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.IPUtils;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.ServletContextHolder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import com.wk.vpac.cache.CacheName;
import com.wk.vpac.cache.dictionary.Register;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.constants.user.DataSource;
import com.wk.vpac.common.token.user.UserToken;
import com.wk.vpac.domain.user.UserBaseInfo;
import com.wk.vpac.main.service.api.user.UserBaseInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Map;

/**
 * UserController
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2017-07-10 17:44
 */
@SystemLogger
@RestController
@RequestMapping(GatewayPath.API)
//@RefreshScope
@Tag(name = "用户中心")
public class UserController {
  @Autowired
  private UserBaseInfoService userBaseInfoService;
  @Autowired
  private CacheManager cacheManager;
  @Autowired
  private DictionaryCacheService dictionaryCacheService;

  @Operation(summary = "用户登录", description =  "异常返回，errorCode：101=账号未激活")
  @ApiExtension(author = "李赓", update = "2019-05-13 12:33")
  @RequestBodyModel({@Param(name = "account", value = "登录账号或手机号", required = true),
    @Param(name = "pwd", value = "登录密码，与verifyCode不能都为空"),
    @Param(name = "verifyCode", value = "短信验证码，与pwd不能都为空，见 /edge/sms/code"),
    @Param(name = "selectRegionId", value = "选择的地区ID"),
    @Param(name = "__cache_code__", value = "微信重定向接口返回的code，用于绑定openId")})
  @ReturnModel(baseModel = UserToken.class)
  @PostMapping(value = "/user/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ObjectNode> userLogin(@RequestBody Map<String, String> params) {
    HttpServletRequest request = ServletContextHolder.getRequestNonNull();
    String account = ConvertUtil.checkNotNull(params, "account", "登录账号或手机号不能为空", String.class);
    params.put("loginIp", IPUtils.getRealIp(request));
    try {
      ObjectNode res = userBaseInfoService.userLogin(params);
      return new ResponseEntity<>(res, HttpStatus.CREATED);
    } catch (PasswordErrorException e) {
      userBaseInfoService.userLoginPasswordError(userBaseInfoService.findByAccountOrPhone(account, account), account);
      throw new IllegalArgumentException(e.getMessage());
    } catch (PasswordErrorLimitException e) {
      throw new BusinessException(e.getMessage(), e, e.getErrorCode());
    }
  }

  @Operation(summary = "根据token获取微信登录的平台用户对象", description =  "参考：/api/weixin/autoLogin")
  @ApiExtension(author = "李赓", update = "2019-05-13 12:33")
  @RequestModel(@Param(name = "token", value = "通过autoLogin重定向后获取到的token", required = true))
  @ReturnModel(baseModel = UserToken.class)
  @GetMapping(value = "/user/token-type/user", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity userByToken(@RequestParam Map<String, String> params) {
    String token = params.get("token");
    Object cached = null;
    if (StringUtils.isNotBlank(token)) {
      try {
        cached = cacheManager.getCache(CacheName.WECHAT_LOGIN_TOKENOBJ).get(token, ObjectNode.class);
      } catch (Exception ignore) {
      }
      if (cached != null) {
        cacheManager.getCache(CacheName.WECHAT_LOGIN_TOKENOBJ).evict(token);
        return new ResponseEntity<>(cached, HttpStatus.OK);
      }
    }
    return new ResponseEntity<>(JsonUtils.createObjectNode(), HttpStatus.OK);
  }

  @Operation(summary = "用户注册")
  @ApiExtension(author = "李赓", update = "2019-05-13 12:33")
  @RequestBodyModel({@Param(name = "phone", value = "手机号", required = true),
    @Param(name = "pwd", value = "登录密码", required = true),
    @Param(name = "verifyCode", value = "短信验证码，见 /edges/sms/code", required = true),
    @Param(name = "userNickName", value = "用户昵称"),
    @Param(name = "__cache_code__", value = "微信重定向接口返回的code，用于绑定openId")})
  @ReturnModel({@Param(name = "msg", value = "注册成功后的提示信息", required = true)})
  @PostMapping(value = "/user/register", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity userRegister(@RequestBody Map<String, String> params, HttpServletRequest request) {
    DictionaryNode data = dictionaryCacheService.listData(Register.class, Register.REGISTRY_DISABLED);
    if (data != null && Boolean.parseBoolean(data.getDataValue())) {
      throw new IllegalArgumentException("用户注册已关闭！");
    }
    params.put("registIp", IPUtils.getRealIp(request));
    params.put("isMobile", String.valueOf(DataSource.MOBILE.getVal()));
    ObjectNode res = userBaseInfoService.userRegister(params);
    return new ResponseEntity<>(res, HttpStatus.CREATED);
  }

  @Operation(summary = "用户修改密码")
  @ApiExtension(author = "李赓", update = "2019-05-13 12:33")
  @RequestBodyModel({@Param(name = "phone", value = "手机号", required = true),
    @Param(name = "pwd", value = "新登录密码", required = true), @Param(name = "oldPwd", value = "旧密码，如果不填则通过手机短信验证"),
    @Param(name = "verifyCode", value = "短信验证码，见 /edges/sms/code")})
  @ReturnModel(baseModel = UserToken.class)
  @PutMapping(value = "/user/password", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity userChangePassword(@RequestBody Map<String, String> params, HttpServletRequest request) {
    params.put("loginIp", IPUtils.getRealIp(request));
    userBaseInfoService.changePassword(params);
    params.put("account", params.get("phone"));
    //修改密码完成后，重新登录返回token
    ObjectNode userToken = userBaseInfoService.userLogin(null, params, false);
    return new ResponseEntity<>(userToken, HttpStatus.OK);
  }


  @Operation(summary = "用户手机修改", description =  "修改后会自动重新登录，返回新的Token对象")
  @ApiExtension(author = "李赓", update = "2019-01-03 16:49", token = @Token)
  @RequestBodyModel({@Param(name = "phone", value = "新手机", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "verifyCode", value = "新手机接收的验证码", required = true, checkEL = EL.NOT_BLANK),})
  @ReturnModel(baseModel = UserToken.class, value = {@Param(name = "selectRegionId", value = "选择的地区ID"),
    @Param(name = "selectRegion", value = "选择的地区"),
    @Param(name = "isFirstLoginOfDay", value = "当前是否登录过", dataType = Boolean.class),})
  @RequireToken(UserToken.class)
  @PutMapping(value = "/user/phone", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity updatePhone(HttpServletRequest request, @RequestBody Map<String, String> params) {
    params.put("userId", TokenThreadLocal.getTokenObjNonNull().objId().toString());
    UserBaseInfo user = userBaseInfoService.updateUserPhone(params);
    Map<String, String> paramsLogin = Maps.newHashMap();
    paramsLogin.put("loginIp", IPUtils.getRealIp(request));
    ObjectNode userToken = userBaseInfoService.userLogin(user, paramsLogin, false);
    return new ResponseEntity<>(userToken, HttpStatus.CREATED);
  }


  @Operation(summary = "用户信息修改")
  @ApiExtension(author = "李赓", update = "2019-01-03 15:08", token = @Token)
  @RequestBodyModel({
    @Param(name = "userNickName", value = "昵称"),
    @Param(name = "gender", value = "性别; 0、 未填写  1、男  2、女", dataType = Integer.class),
    @Param(name = "birth", value = "生日；yyyy-MM-dd"),
    @Param(name = "userEmail", value = "Email"),
    @Param(name = "avatar", value = "头像"),
    @Param(name = "selectRegionId", value = "选择地区ID"),
    @Param(name = "selectRegion", value = "选择地区名称"),
    @Param(name = "profile", value = "用户签名"),
    @Param(name = "idCard", value = "身份证号码"),
    @Param(name = "realName", value = "真实姓名"),
    @Param(name = "jobPosition", value = "职位"),
    @Param(name = "wxName", value = "微信名称"),
    @Param(name = "wxImg", value = "微信头像"),
//    @Param(name = "expertId", value = "专家ID"),
//    @Param(name = "communityId", value = "社区ID"),
    @Param(name = "contactPhone", value = "联系电话"),
  })
  @ReturnModel({
    @Param(name = "id", value = "用户ID"),
    @Param(name = "userNickName", value = "昵称"),
    @Param(name = "avatar", value = "头像"),
    @Param(name = "gender", value = "性别;  0、 未填写  1、男  2、女", dataType = Integer.class),
    @Param(name = "birth", value = "生日"),
    @Param(name = "idCard", value = "身份证号码"),
    @Param(name = "selectRegionId", value = "选择地区ID"),
    @Param(name = "selectRegion", value = "选择地区名称"),
    @Param(name = "registTime", value = "注册时间"),
    @Param(name = "profile", value = "个人签名"),
    @Param(name = "phone", value = "手机号"),
    @Param(name = "lastLoginTime", value = "最后登录时间"),
    @Param(name = "integral", value = "剩余积分", dataType = Integer.class),
    @Param(name = "userEmail", value = "用户邮箱"),
    @Param(name = "hasMiniOpenId", value = "是否已绑定微信小程序", dataType = Boolean.class),
    @Param(name = "jobPosition", value = "职位"),
    @Param(name = "realName", value = "真实姓名"),
    @Param(name = "wxName", value = "微信名称"),
    @Param(name = "wxImg", value = "微信头像"),
    @Param(name = "contactPhone", value = "联系电话"),
  })
  @PutMapping(value = "/user/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity completeInfo(@RequestBody Map<String, String> params) {
    String userId = TokenThreadLocal.getTokenObjNonNull().objId().toString();
    userBaseInfoService.completeInfo(userId, params);
    return new ResponseEntity<>(userBaseInfoService.findUserInfo(userId), HttpStatus.CREATED);
  }

  @Operation(summary = "用户信息查询")
  @ApiExtension(author = "李赓", update = "2020-05-09 16:42:07", token = @Token)
  @ReturnModel({
    @Param(name = "id", value = "用户ID，普通用户无权限查看其它用户ID"),
    @Param(name = "userNickName", value = "昵称"),
    @Param(name = "avatar", value = "头像"),
    @Param(name = "gender", value = "性别;  0、 未填写  1、男  2、女", dataType = Integer.class),
    @Param(name = "birth", value = "生日"),
    @Param(name = "idCard", value = "身份证号码"),
    @Param(name = "selectRegionId", value = "选择地区ID"),
    @Param(name = "selectRegion", value = "选择地区名称"),
    @Param(name = "registTime", value = "注册时间"),
    @Param(name = "profile", value = "个人签名"),
    @Param(name = "phone", value = "手机号"),
    @Param(name = "lastLoginTime", value = "最后登录时间"),
    @Param(name = "integral", value = "剩余积分", dataType = Integer.class),
    @Param(name = "userEmail", value = "用户邮箱"),
    @Param(name = "hasMiniOpenId", value = "是否已绑定微信小程序", dataType = Boolean.class),
    @Param(name = "jobPosition", value = "职位"),
    @Param(name = "realName", value = "真实姓名"),
    @Param(name = "wxName", value = "微信名称"),
    @Param(name = "wxImg", value = "微信头像"),
    @Param(name = "contactPhone", value = "联系电话"),
  })
  @GetMapping(value = "/user/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity userInfo(@RequestParam Map<String, String> params) {
    TokenCacheObj token = TokenThreadLocal.getTokenObjNonNull();
    String tokenUserId = token.objId().toString();
    String id = params.get("id");
    if(StringUtils.isBlank(id)){
      id = tokenUserId;
    }
    return new ResponseEntity<>(userBaseInfoService.findUserInfo(id), HttpStatus.OK);
  }


  @Operation(summary = "用户注销（清除微信绑定）")
  @ApiExtension(author = "李赓", update = "2019-07-09 16:39:29", token = @Token)
  @PutMapping(value = "/user/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> logout(@RequestBody Map<String, String> params) {
    userBaseInfoService.logout();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Operation(summary = "用户手机绑定", description =  "用户手机绑定")
  @ApiExtension(author = "李建", update = "2019-11-14 16:49", token = @Token)
  @RequestBodyModel({@Param(name = "phone", value = "新手机", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "verifyCode", value = "短信验证码 (发送短信获取验证码接口 type=4)", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "pwd", value = "密码", required = true, checkEL = EL.NOT_BLANK),})
  @PutMapping(value = "/user/phone/bind", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity bindPhone(@RequestBody Map<String, String> params) {
    String phone = ConvertUtil.checkNotNull(params, "phone", "手机不能为空", String.class);
    String verifyCode = ConvertUtil.checkNotNull(params, "verifyCode", "验证码不能为空", String.class);
    String pw = ConvertUtil.checkNotNull(params, "pwd", "密码不能为空", String.class);
    userBaseInfoService.bindPhone(TokenThreadLocal.getTokenObjNonNull().objId().toString(), phone, verifyCode, pw);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * 修改用户邮箱
   *
   * @param params -
   * <p> email             - Notnull  - Str - 新邮箱
   * <p> verifyCode        - Notnull  - Str - 手机接收的验证码
   * <p> oldEmail          - Nullable  - Str - 原邮箱
   *
   * @return
   */
  @RequireToken({UserToken.class})
  @PutMapping(value = "/user/email/edit", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity updateUserEmail(@RequestBody Map<String, String> params) {
    String email = ConvertUtil.checkNotNull(params, "email", "邮箱不能为空", String.class);
    String verifyCode = ConvertUtil.checkNotNull(params, "verifyCode", "验证码不能为空", String.class);
    String oldEmail = ConvertUtil.convertNullable(params.get("oldEmail"), String.class);
    String userId = TokenThreadLocal.getTokenObjNonNull().objId().toString();
    userBaseInfoService.updateUserEmail(userId, email, verifyCode, oldEmail);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * 第一次设置用户邮箱
   *
   * @param params -
   * <p> email             - Notnull  - Str - 邮箱
   *
   * @return
   */
  @RequireToken({UserToken.class})
  @PutMapping(value = "/user/email", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity setUserEmail(@RequestBody Map<String, String> params) {
    String email = ConvertUtil.checkNotNull(params, "email", "邮箱不能为空", String.class);
    String userId = TokenThreadLocal.getTokenObjNonNull().objId().toString();
    userBaseInfoService.setUserEmail(userId, email);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * 选择区域
   *
   * @param regionId 区域id
   */
  @PostMapping(value = "/user/region/{regionId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity selectRegion(@PathVariable("regionId") String regionId) {
    Serializable userId = TokenThreadLocal.getTokenObjNonNull().objId();
    return new ResponseEntity<>(userBaseInfoService.selectRegion(userId.toString(), regionId), HttpStatus.CREATED);
  }
}
