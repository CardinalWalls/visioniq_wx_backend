

package com.wk.vpac.main.controller.api.edge;

import com.base.components.cache.Cache;
import com.base.components.cache.CacheManager;
import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.doc.validation.EL;
import com.base.components.common.exception.business.SmsException;
import com.base.components.common.exception.business.SmsException.ErrorCode;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.util.CaptchaUtil;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.IPUtils;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.RandomCodeHelper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.common.collect.Maps;
import com.wk.vpac.cache.CacheName;
import com.wk.vpac.common.constants.sms.SmsTemplateId;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.service.sms.SmsService;
import com.wk.vpac.main.constants.common.CaptchaProperties;
import com.wk.vpac.main.service.api.user.UserBaseInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
@EnableConfigurationProperties(CaptchaProperties.class)
@Tag(name = "消息通知和验证")
public class CaptchaController {
  public static final String SMS_COUNT_EXPIRE_KEY_PREFIX = "expire_";

  @Autowired
  private CacheManager cacheManager;

  @Autowired
  private UserBaseInfoService userBaseInfoService;

  @Autowired
  private CaptchaProperties captchaProperties;

  @Value("${base.sms.limit.countOfDay:10}")
  private int smsCountOfDay;

  @Autowired(required = false)
  private SmsService smsService;

  @Value("${base.sms.limit:60000}")
  private long smsReqLimit;

  @Operation(summary="图片验证码", description =  "通常表单图片验证码（5分钟有效）")
  @ApiExtension(author = "李赓", update = "2021-03-12 10:05:59")
  @RequestModel({
    @Param(name = "captchaPreCode", value = "前端存储的一个唯一性很高的随机值，表单提交时会用到此值和图片验证码值一起效验", required = true)
  })
  @GetMapping(value = "/edge/captcha/normal", produces = MediaType.APPLICATION_JSON_VALUE)
  public void captchaImg(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
    String preCode = params.get("captchaPreCode");
    Assert.hasText(preCode, "preCode con not be null!");
    Cache preCache = cacheManager.getCache(CacheName.NORMAL_CAPTCHA_IMG);
    DefaultKaptcha kaptcha = captchaProperties.build();
    String code = kaptcha.createText();
    preCache.put(preCode, code);
    preCache.expire(preCache, 5, TimeUnit.MINUTES);
    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    ImageIO.write(kaptcha.createImage(code), "jpg", response.getOutputStream());
  }

  @Operation(summary="图片验证码（发送前）", description =  "返回发送前验证码图片")
  @ApiExtension(author = "李赓", update = "2019-06-10 16:18:30")
  @RequestModel({
    @Param(name = "type", value = "发送类型; " + CaptchaTypeProperties.TYPES,
      dataType = Integer.class, required = true, checkEL = EL.NOT_BLANK)
  })
  @GetMapping(value = "/edge/captcha", produces = MediaType.APPLICATION_JSON_VALUE)
  public void preSend(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
    SmsException.assertIsTrue(params.get("type") != null, ErrorCode.type_empty);
    int type = Integer.parseInt(params.get("type"));
    Cache preCache = cacheManager.getCache(CaptchaTypeProperties.get(type).getPreCodeCache());
    DefaultKaptcha kaptcha = captchaProperties.build();
    String code = kaptcha.createText();
    preCache.put(code, code);
    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    ImageIO.write(kaptcha.createImage(code), "jpg", response.getOutputStream());
  }

  @Operation(summary="验证码（发送前）校验")
  @ApiExtension(author = "李赓", update = "2019-06-10 16:18:30")
  @RequestModel({
    @Param(name = "type", value = "发送类型; " + CaptchaTypeProperties.TYPES,
      dataType = Integer.class, required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "preCode", value = "校验的发送前验证码", required = true, checkEL = EL.NOT_BLANK)
  })
  @GetMapping(value = "/edge/captcha/validate", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> validateCaptcha(@RequestParam Map<String, String> params) {
    SmsException.assertIsTrue(params.get("preCode") != null, ErrorCode.validate_empty);
    String preCode = params.get("preCode");
    SmsException.assertIsTrue(params.get("type") != null, ErrorCode.type_empty);
    int type = Integer.parseInt(params.get("type"));
    Cache cache = cacheManager.getCache(CaptchaTypeProperties.get(type).getPreCodeCache());
    String cachedVerifyCode = cache.get(preCode, String.class);
    SmsException.assertIsTrue(RandomCodeHelper.checkCacheCode(preCode, cachedVerifyCode), ErrorCode.validate_error);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Operation(summary="获取PreCode（发送前）, JS混淆第二步", description =  "前端解析示例：\n"
    + "$.get('/api/edge/captcha/key/js', function (r1) {  \n"
    + "   var length = 5;"
    + "   $.get('/api/edge/captcha/key?length='+ length +'&type=1&decryptJs=' + encodeURIComponent(r1.decryptJs), function (r2) {  \n"
    + "        var preCode = __decrypt__(r2.accessKey, r1.decryptJs, length);  \n"
    + "   });  \n"
    + "});  \n"
    + "function __decrypt__(Ti1,ptUnEmkB2,qa3){var FuNZmpP4=/<script(.| )*?>(.| | )*?<\\/script>/ig;var gIiQWk5=ptUnEmkB2[\"\\x6d\\x61\\x74\\x63\\x68\"](FuNZmpP4);if(gIiQWk5){var LHiEB6=/<script(.| )*?>((.| | )*)?<\\/script>/im;var dyoYpXWK7=gIiQWk5[\"\\x6c\\x65\\x6e\\x67\\x74\\x68\"];for(var py$IgsiO8=0;py$IgsiO8<dyoYpXWK7;py$IgsiO8++){var S_mo9=gIiQWk5[py$IgsiO8][\"\\x6d\\x61\\x74\\x63\\x68\"](LHiEB6);if(S_mo9[2]){if(window[\"\\x65\\x78\\x65\\x63\\x53\\x63\\x72\\x69\\x70\\x74\"]){window[\"\\x65\\x78\\x65\\x63\\x53\\x63\\x72\\x69\\x70\\x74\"](S_mo9[2])}else{window[\"\\x65\\x76\\x61\\x6c\"](S_mo9[2])}}}return jsDecrypt(qa3,Ti1)}};")
  @ApiExtension(author = "李赓", update = "2019-06-10 16:18:30")
  @RequestModel({
    @Param(name = "type", value = "发送类型; " + CaptchaTypeProperties.TYPES,
      dataType = Integer.class, required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "length", value = "code长度(4-10位数字)", dataType = Integer.class, required = true),
    @Param(name = "phone", value = "手机号，可空，用于 type = 1，3"),
    @Param(name = "decryptJs", value = "第一步中的js混淆代码", required = true),
  })
  @ReturnModel(@Param(name = "accessKey", value = "根据此值调用示例方法得到PreCode", required = true))
  @RequestMapping(value = "/edge/captcha/key", produces = MediaType.APPLICATION_JSON_VALUE,
    method = {RequestMethod.GET, RequestMethod.POST})
  public ResponseEntity preSendSafe(@RequestParam Map<String, String> params, HttpServletRequest request) {
    Integer length = ConvertUtil.checkNotNull(params, "length", ErrorCode.encrypt_code_type.getInfo(), Integer.class);
    String decryptJs = ConvertUtil.checkNotNull(params, "decryptJs", ErrorCode.js_null.getInfo(), String.class);
    Assert.isTrue(length >= 4 && length <= 10, ErrorCode.encrypt_code_limit.getInfo());
    //注册和修改手机
    Integer type = ConvertUtil.checkNotNull(params, "type", Integer.class);
    //-------------
    CaptchaTypeProperties captchaTypeProperties = CaptchaTypeProperties.get(type);
    String phone = params.get("phone");
    captchaTypeProperties.check(()->!userBaseInfoService.checkExists(phone), ErrorCode.phone_has_register, 1, 3);
    Cache preCache = cacheManager.getCache(captchaTypeProperties.getPreCodeCache());
    String code = "";
    String accessKey = "";
    // 同一个IP每一个js只能调用一次，过期前段重新获取
    String ip = IPUtils.getRealIp(request);
    ip.replace(":", ".");
    Cache decryptJsCache = cacheManager.getCache(CacheName.JS_DECRYPT);
    String decryptJsCacheStr = decryptJsCache.get(ip, String.class);
    SmsException.assertIsTrue(decryptJsCacheStr != null, ErrorCode.js_expire.getCode(), ErrorCode.js_expire.getInfo());
    if (decryptJs.equals(CaptchaUtil.jsDecrypt_1)) {
      accessKey = CaptchaUtil.getRandomCode(32);
      code = CaptchaUtil.getCode(accessKey, length);
    }
    if (decryptJs.equals(CaptchaUtil.jsDecrypt_2)) {
      code = CaptchaUtil.getRandomCode(length);
      accessKey = CaptchaUtil.getEncryptRandomCode(code);
    }
    if (decryptJs.equals(CaptchaUtil.jsDecrypt_3)) {
      accessKey = CaptchaUtil.getRandomCode(32);
      code = CaptchaUtil.getSimpleCode(accessKey, length);
    }
    preCache.put(code, code);

    // 移除js
    decryptJsCache.evict(ip);

    return new ResponseEntity<>(JsonUtils.mapper.createObjectNode().
      put("accessKey", accessKey), HttpStatus.OK);
  }



  @Operation(summary="获取PreCode（发送前）, JS混淆第一步", description =  "前段随机获取解密js方法")
  @ApiExtension(author = "李赓", update = "2019-06-10 16:26:16")
  @ReturnModel(@Param(name = "decryptJs", value = "将此值用于第二步的参数", required = true))
  @GetMapping(value = "/edge/captcha/key/js", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity preSendJs(@RequestParam Map<String, String> params, HttpServletRequest request) {
    String ip = IPUtils.getRealIp(request);
    ip.replace(":", ".");
    // 同一个IP每次需要不一样的js方法
    Cache decryptJsCache = cacheManager.getCache(CacheName.JS_DECRYPT);
    String decryptJs = "";
    decryptJs = decryptJsCache.get(ip, String.class);
    String newJs = getDecryptJs();
    while (!StringUtils.isEmpty(decryptJs) && decryptJs.equals(newJs)) {
      newJs = getDecryptJs();
    }
    decryptJsCache.put(ip, newJs);
    return new ResponseEntity<>(JsonUtils.mapper.createObjectNode().
      put("decryptJs", newJs), HttpStatus.OK);
  }



  @Operation(summary="发送短信获取验证码", description =  "开发环境会直接返回验证码")
  @ApiExtension(author = "李赓", update = "2019-06-10 16:18:30")
  @RequestModel({
    @Param(name = "type", value = "发送类型; " + CaptchaTypeProperties.TYPES,
      dataType = Integer.class, required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "preCode", value = "校验的发送前验证码", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "phone", value = "手机号", required = true, checkEL = EL.NOT_BLANK)
  })
  @ReturnModel(@Param(name = "code", value = "开发环境会返回此验证码", example = "开发环境会返回此验证码"))
  @GetMapping(value = "/edge/sms/code", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity smsVerifyCode(@RequestParam Map<String, String> params, HttpServletRequest request) {
    SmsException.assertIsTrue(params.get("phone") != null, ErrorCode.phone_empty);
    String phone = params.get("phone");
    filterSmsReq(phone);
    SmsException.assertIsTrue(checkPhone(phone), ErrorCode.phone_error);
    SmsException.assertIsTrue(params.get("preCode") != null, ErrorCode.validate_empty);
    String preCode = params.get("preCode");
    SmsException.assertIsTrue(params.get("type") != null, ErrorCode.type_empty);
    int type = Integer.parseInt(params.get("type"));
    CaptchaTypeProperties captchaTypeProperties = CaptchaTypeProperties.get(type);
    CacheName preCacheName = captchaTypeProperties.getPreCodeCache();
    CacheName cacheName = captchaTypeProperties.getCodeCache();
    SmsTemplateId smsTemplateId = captchaTypeProperties.getSmsTemplateId();
    if (preCacheName != null) {
      Cache preCache = cacheManager.getCache(preCacheName);
      String cachedVerifyCode = preCache.get(preCode, String.class);
      SmsException.assertIsTrue(RandomCodeHelper.checkCacheCode(preCode, cachedVerifyCode), ErrorCode.validate_error);
      preCache.evict(preCode);
    }
    captchaTypeProperties.check(()->!userBaseInfoService.checkExists(phone), ErrorCode.phone_has_register, 1, 3);
    String verifyCode = RandomCodeHelper.random(6);
    Cache cache = cacheManager.getCache(cacheName);
    //非登录时查看短信次数是否超过当天限制
    if (type != 0) {
      checkSmsCount(cache, phone);
    }
    cache.put(phone, verifyCode);
    incrementSmsCount(cache, phone);

    String testMsg = null;
    //未开起短信服务
    if (smsService == null || !smsService.enabled()) {
      testMsg = "{\"code\":\"" + verifyCode + "\", \"msg\":\"短信发送服务未开启\"}";
    }
    //开起短信服务
    else {
      Map<String, Object> extras = Maps.newHashMap();
      extras.put("code", verifyCode);
      //      long expireMinutes =  cacheManager.getCache(cacheName).getDefaultExpiresSecond() / 60;
      //      extras.put("LASTTIME", expireMinutes);
      smsService.sendSmsSync(phone, null, smsTemplateId, extras);
      cacheManager.getCache(CacheName.USER_SMS_LASTTIME).put(phone, System.currentTimeMillis());
    }
    return new ResponseEntity<>(testMsg, HttpStatus.OK);
  }

  /**
   * 过滤过于频繁的短信发送请求
   *
   * @param phone
   */
  private void filterSmsReq(String phone) {
    org.springframework.cache.Cache.ValueWrapper lasttime = cacheManager.getCache(CacheName.USER_SMS_LASTTIME)
                                                                        .get(phone);
    SmsException
      .assertIsTrue(lasttime.get() == null || (System.currentTimeMillis() - ((long) lasttime.get())) >= smsReqLimit,
                    ErrorCode.request_frequently
      );
  }

  @Operation(summary="短信验证码校验")
  @ApiExtension(author = "李赓", update = "2019-06-10 16:18:30")
  @RequestModel({
    @Param(name = "type", value = "发送类型; " + CaptchaTypeProperties.TYPES,
      dataType = Integer.class, required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "code", value = "短信验证码", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "phone", value = "手机号", required = true, checkEL = EL.NOT_BLANK)
  })
  @GetMapping(value = "/edge/sms/validate", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> smsValidate(@RequestParam Map<String, String> params, HttpServletRequest request) {
    SmsException.assertIsTrue(params.get("phone") != null, ErrorCode.phone_empty);
    String phone = params.get("phone");
    SmsException.assertIsTrue(checkPhone(phone), ErrorCode.phone_error);
    SmsException.assertIsTrue(params.get("code") != null, ErrorCode.validate_empty);
    String code = params.get("code");
    SmsException.assertIsTrue(params.get("type") != null, ErrorCode.type_empty);
    int type = Integer.parseInt(params.get("type"));
    Cache cache = cacheManager.getCache(CaptchaTypeProperties.get(type).getCodeCache());
    String cachedVerifyCode = cache.get(phone, String.class);
    SmsException.assertIsTrue(RandomCodeHelper.checkCacheCode(code, cachedVerifyCode), ErrorCode.validate_error);
    return new ResponseEntity<>(HttpStatus.OK);
  }



  private void checkSmsCount(Cache cache, String phone) {
    String key = SMS_COUNT_EXPIRE_KEY_PREFIX + phone;
    Integer count = cache.get(key, Integer.class);
    SmsException.assertIsTrue(count == null || count < smsCountOfDay, ErrorCode.count_limit.getCode(),
                              ErrorCode.count_limit.getInfo().replace("{count}", String.valueOf(smsCountOfDay))
    );
  }

  private void incrementSmsCount(Cache cache, String phone) {
    //生成失效时间：当天最后一毫秒，此操作需在获取缓存之前执行
    Date expireDate = DateTime.now().millisOfDay().withMaximumValue().toDate();
    String key = SMS_COUNT_EXPIRE_KEY_PREFIX + phone;
    Integer count = cache.get(key, Integer.class);
    if (count == null) {
      count = 1;
    } else {
      count++;
    }
    cache.put(key, count);
    cache.expireAt(key, expireDate);
  }

  private Pattern phonePattern = Pattern.compile("^[1][3-9]\\d{9}$");

  public boolean checkPhone(String phone) {
    Matcher m = phonePattern.matcher(phone);
    return m.matches();
  }

  /**
   * 获取decryptJs
   *
   * @return
   */
  private String getDecryptJs() {
    String decryptJs = "";
    Random r = new Random();
    int i = r.nextInt(3) + 1;
    switch (i) {
      case 1:
        decryptJs = CaptchaUtil.jsDecrypt_1;
        break;
      case 2:
        decryptJs = CaptchaUtil.jsDecrypt_2;
        break;
      case 3:
        decryptJs = CaptchaUtil.jsDecrypt_3;
        break;
      default:
        decryptJs = CaptchaUtil.jsDecrypt_1;
        break;
    }
    return decryptJs;
  }


  /**
   * 获取余额支付验证码
   *
   * @param params -
   * <p> phone             - Notnull  - Str - 电话
   * <p> orderCode         - Notnull  - Str - 订单编号
   * <p> preCode           - Notnull  - Str - 发送前code
   * @param request
   *
   * @return
   */
  @GetMapping(value = "/edge/sms/pay/code", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity smsPayVerifyCode(@RequestParam Map<String, String> params, HttpServletRequest request) {
    SmsException.assertIsTrue(params.get("phone") != null, ErrorCode.phone_empty);
    String phone = params.get("phone");
    filterSmsReq(phone);
    SmsException.assertIsTrue(checkPhone(phone), ErrorCode.phone_error);
    SmsException.assertIsTrue(params.get("preCode") != null, ErrorCode.validate_empty);
    String preCode = params.get("preCode");
    SmsException.assertIsTrue(params.get("orderCode") != null, ErrorCode.order_code_null);
    String orderCode = params.get("orderCode");

    // 发送前的code验证
    CacheName preCacheName = CacheName.PRE_BALANCE_PAY;
    Cache preCache = cacheManager.getCache(preCacheName);
    String cachedVerifyCode = preCache.get(preCode, String.class);
    SmsException.assertIsTrue(RandomCodeHelper.checkCacheCode(preCode, cachedVerifyCode), ErrorCode.validate_error);
    preCache.evict(preCode);

    CacheName cacheName = CacheName.BALANCE_PAY;
    String verifyCode = RandomCodeHelper.random(6);
    Cache cache = cacheManager.getCache(cacheName);
    String key = phone + orderCode;
    cache.put(key, verifyCode);

    String testMsg = null;
    //未开起短信服务
    if (smsService == null || !smsService.enabled()) {
      testMsg = "{\"code\":\"" + verifyCode + "\", \"msg\":\"短信发送服务未开启\"}";
    }
    //开起短信服务
    else {
      Map<String, Object> extras = Maps.newHashMap();
      extras.put("code", verifyCode);
      smsService.sendSmsSync(phone, null, SmsTemplateId.ALiDaYu_balance_pay, extras);
      cacheManager.getCache(CacheName.USER_SMS_LASTTIME).put(phone, System.currentTimeMillis());
    }
    return new ResponseEntity<>(testMsg, HttpStatus.OK);
  }

  /**
   * 余额支付短信验证码-校验
   *
   * @param params -
   * <p> phone             - Notnull  - Str - 电话
   * <p> orderCode         - Notnull  - Str - 订单编号
   * <p> code              - Notnull  - Str - 短信验证码
   * @param request
   *
   * @return
   */
  @GetMapping(value = "/edge/sms/pay/validate", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity smsPayValidate(@RequestParam Map<String, String> params, HttpServletRequest request) {
    SmsException.assertIsTrue(params.get("phone") != null, ErrorCode.phone_empty);
    String phone = params.get("phone");
    SmsException.assertIsTrue(checkPhone(phone), ErrorCode.phone_error);
    SmsException.assertIsTrue(params.get("code") != null, ErrorCode.validate_empty);
    String code = params.get("code");
    SmsException.assertIsTrue(params.get("orderCode") != null, ErrorCode.order_code_null);
    String orderCode = params.get("orderCode");
    CacheName cacheName = CacheName.BALANCE_PAY;
    Cache preCache = cacheManager.getCache(cacheName);
    String key = phone + orderCode;
    String cachedVerifyCode = preCache.get(key, String.class);
    return ResponseEntity.ok(RandomCodeHelper.checkCacheCode(code, cachedVerifyCode));
  }

//  @Operation(summary="手机短信发送验证码")
//  @ApiExtension(author = "李赓", update = "2019-01-09 09:44", token = @Token)
//  @RequestModel({
//    @Param(name = "phone", value = "手机号", required = true, checkEL = EL.NOT_BLANK),
//    @Param(name = "type", value = "发送类型; 0=登录，1=注册，2=找回密码, 3=修改手机号,4=余额提现,5=登录密码错误超过限制,6=修改邮箱或者验证原手机号,7、赠送积分，8、服务人员注册",
//      required = true, dataType = Integer.class, checkEL = EL.NOT_BLANK),
//  })
//  @GetMapping(value = "/user/sms/code", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<Void> smsVerifyCodeNoPreCode(@RequestParam Map<String, String> params, HttpServletRequest request) {
//    params.put("preCode", RandomCodeHelper.SUPER_CODE);
//    return smsVerifyCode(params, request);
//  }
}
