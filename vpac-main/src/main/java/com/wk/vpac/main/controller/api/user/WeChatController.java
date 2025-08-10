package com.wk.vpac.main.controller.api.user;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.doc.annotation.Token;
import com.base.components.common.token.TokenCacheObj;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.IPUtils;
import com.base.components.common.util.JsonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.token.user.UserToken;
import com.wk.vpac.main.service.api.user.UserBaseInfoService;
import com.wk.vpac.main.service.api.user.WeChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * WeiXinController
 *
 * @author <a href="lijian_jie@163.com">LiJian</a>
 * @version 1.0.0, 2018-03-16 09:57
 */
@RestController
@RequestMapping(GatewayPath.API)
//@RefreshScope
@Tag(name = "微信相关")
public class WeChatController {
//  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
//  @Autowired
//  private WxMpService mpService;
//  @Autowired
//  private OfficialAccountsProperties accountsProperties;
  @Autowired
  private UserBaseInfoService userBaseInfoService;
  @Autowired
  private WeChatService weChatService;
//  @Autowired
//  private WxMpMessageRouter messageRouter;
//  @Autowired
//  private WxPayService wxPayService;
//  @Autowired
//  private CacheManager cacheManager;
//  @Autowired
//  private WxMpConfigStorage configStorage;
//  private static final String WECHAT_BIND_PREFIX = "bind_prefix_";
//  @Value("${base.pm.wechat.success-url}")
//  private String successUrl;
//  @Value("${base.pm.wechat.mobileBindSuccess}")
//  private String mobileBindSuccess;
//  @Autowired
//  private WxMaService maService;

//  @Autowired
//  private PayService payService;


  @Operation(summary = "获取SessionTicket", description = "获取小程序登录成功后的平台临时wxSessionTicket，只能使用一次，"
    + "其它接口使用此Ticket后，会返回一个新的Ticket，但原失效时间不会延长")
  @ApiExtension(author = "李赓", update = "2023-02-13 14:08:38")
  @RequestModel(@Param(name = "code", value = "wx.login()返回的code", required = true))
  @ReturnModel(value = {
    @Param(name = "wxSessionTicket", value = "失效时间为："+WeChatService.WX_SESSION_TICKET_EXPIRE_DAYS+"天；只能使用一次")
  })
  @GetMapping(value = "/mini/session", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getSessionTicket(@RequestParam Map<String, String> params, HttpServletRequest request) {
    String code = ConvertUtil.checkNotNull(params, "code", String.class);
    ObjectNode sessionKey = JsonUtils.createObjectNode().put("wxSessionTicket", weChatService.buildWxSession(code));
    return ResponseEntity.ok(sessionKey);
  }

//  @SystemLogger
//  @Operation(summary = "小程序自动登录（用户基本信息）",description = "将自动注册用户")
//  @ApiExtension(author = "李建", update = "2019-09-10 12:33")
//  @RequestModel({
//    @Param(name = "code", value = "小程序code", required = true),
//    @Param(name = "signature", value = "signature", required = true),
//    @Param(name = "rawData", value = "rawData", required = true),
//    @Param(name = "encryptedData", value = "encryptedData", required = true),
//    @Param(name = "iv", value = "iv", required = true),
//    @Param(name = "parentUserId", value = "推荐人ID"),
//    @Param(name = "remark", value = "注册时的来源描述")
//  })
//  @ReturnModel(baseModel = UserToken.class, value = {
//    @Param(name = "sessionKey", value = "小程序sessionKey")
//  })
//  @GetMapping(value = "/mini/login/auto", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity miniAutoLogin(@RequestParam Map<String, String> params, HttpServletRequest request) {
//    String ip = IPUtils.getRealIp(request);
//    ObjectNode tokenObjectNode;
//    tokenObjectNode = userBaseInfoService.registerOrLoginByMaUser(params, ip);
//    return ResponseEntity.ok(tokenObjectNode);
//  }

  @Operation(summary = "小程序自动登录(手机号)",description = "自动登录或注册用户")
  @ApiExtension(author = "李赓", update = "2023-02-13 14:08:38")
  @RequestModel({
    @Param(name = "wxSessionTicket", value = "调用 /mini/session 获取，无效时返回errorCode=" + WeChatService.WX_SESSION_TICKET_INVALID_ERROR, required = true),
    @Param(name = "encryptedData", value = "未能自动登录时，微信授权手机号的加密参数"),
    @Param(name = "iv", value = "未能自动登录时，微信授权手机号的加密参数"),
    @Param(name = "parentUserId", value = "推荐人ID"),
    @Param(name = "remark", value = "注册时的来源描述"),
    @Param(name = "expertId", value = "绑定的专家ID")
  })
  @ReturnModel(baseModel = UserToken.class, value = {
    @Param(name = "wxSessionTicket", value = "新的ticket，以便下次使用", required = true),
  })
  @PostMapping(value = "/mini/login/phone/auto", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity miniPhoneAutoLogin(@RequestBody Map<String, String> params, HttpServletRequest request) {
    String ip = IPUtils.getRealIp(request);
    ObjectNode tokenObjectNode;
    tokenObjectNode = userBaseInfoService.registerOrLoginByPhone(params, ip);
    return ResponseEntity.ok(tokenObjectNode);
  }

  @GetMapping(value = "/mini/login/phone/auto", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity miniPhoneAutoLogin1(@RequestParam Map<String, String> params, HttpServletRequest request) {
    return miniPhoneAutoLogin(params, request);
  }

  @Operation(summary = "小程序获取用户信息")
  @ApiExtension(author = "李赓", update = "2023-02-13 14:08:38")
  @RequestModel({
    @Param(name = "wxSessionTicket", value = "调用 /mini/session 获取，无效时返回errorCode=" + WeChatService.WX_SESSION_TICKET_INVALID_ERROR, required = true),
    @Param(name = "signature", value = "signature", required = true),
    @Param(name = "rawData", value = "rawData", required = true),
    @Param(name = "encryptedData", value = "encryptedData", required = true),
    @Param(name = "iv", value = "iv", required = true),})
  @ReturnModel(baseModel = WxMaUserInfo.class, value = {
    @Param(name = "wxSessionTicket", value = "新的ticket，以便下次使用", required = true),
  })
  @PostMapping(value = "/mini/user/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity miniUserInfo(@RequestBody Map<String, String> params) {
    return ResponseEntity.ok(weChatService.getWxSession(
      ConvertUtil.checkNotNull(params, "wxSessionTicket", String.class), (wxSession, newTicket) -> {
        WxMaUserInfo miniUserInfo = weChatService.getMiniUserInfo(wxSession, params);
        TokenCacheObj tokenThreadLocal = TokenThreadLocal.getTokenObj();
        if (tokenThreadLocal != null) {
          Map<String, String> re = Maps.newHashMap();
          re.put("avatarUrl", miniUserInfo.getAvatarUrl());
          re.put("userNickName", miniUserInfo.getNickName());
          re.put("avatar", miniUserInfo.getAvatarUrl());
          re.put("gender", miniUserInfo.getGender());
          re.put("wxImg", miniUserInfo.getAvatarUrl());
          re.put("wxName", miniUserInfo.getNickName());
          userBaseInfoService.completeInfo(tokenThreadLocal.objId().toString(), re);
        }
        return JsonUtils.convert(miniUserInfo, ObjectNode.class).put("wxSessionTicket", newTicket);
      }));
  }
  @GetMapping(value = "/mini/user/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity miniUserInfo1(@RequestParam Map<String, String> params) {
    return miniUserInfo(params);
  }
  @Operation(summary = "小程序获取用户信息手机号",description = "-")
  @ApiExtension(author = "李建", update = "2019-09-10 12:33")
  @RequestModel({
    @Param(name = "wxSessionTicket", value = "调用 /mini/session 获取，无效时返回errorCode=" + WeChatService.WX_SESSION_TICKET_INVALID_ERROR, required = true),
    @Param(name = "signature", value = "signature", required = true),
    @Param(name = "rawData", value = "rawData", required = true),
    @Param(name = "encryptedData", value = "encryptedData", required = true),
    @Param(name = "iv", value = "iv", required = true)})
  @ReturnModel({
    @Param(name = "phone", value = "手机号", required = true),
    @Param(name = "wxSessionTicket", value = "新的ticket，以便下次使用", required = true),
  })
  @GetMapping(value = "/mini/user/phone", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity phone(@RequestParam Map<String, String> params) {
    String wxSessionTicket = ConvertUtil.checkNotNull(params, "wxSessionTicket", String.class);
    return weChatService.getWxSession(wxSessionTicket, (wxSession, newTicket)->{
      String phone = weChatService.decodeMiniUserPhone(wxSession, params);
      userBaseInfoService.bindPhone(TokenThreadLocal.getTokenObjNonNull().objId().toString(), phone);
      return ResponseEntity.ok(JsonUtils.createObjectNode().put("phone", phone).put("wxSessionTicket", newTicket));
    });
  }

  @Operation(summary = "小程序绑定",description = "用户通过手机号注册未绑定小程序时手动绑定")
  @ApiExtension(author = "李建", update = "2019-08-21 08:56", token = @Token)
  @RequestModel({
    @Param(name = "wxSessionTicket", value = "调用 /mini/session 获取，无效时返回errorCode=" + WeChatService.WX_SESSION_TICKET_INVALID_ERROR, required = true),
    @Param(name = "signature", value = "signature", required = true),
    @Param(name = "rawData", value = "rawData", required = true),
    @Param(name = "encryptedData", value = "encryptedData", required = true),
    @Param(name = "iv", value = "iv", required = true)
  })
  @ReturnModel({
    @Param(name = "wxSessionTicket", value = "新的ticket，以便下次使用", required = true),
  })
  @GetMapping("/mini/binding")
  public ResponseEntity bindWxMaUserInfo(@RequestParam Map<String, String> params) {
    String userId = TokenThreadLocal.getTokenObjNonNull().objId().toString();
    return weChatService.getWxSession(
      ConvertUtil.checkNotNull(params, "wxSessionTicket", String.class), (wxSession, newTicket)->{
      userBaseInfoService.bindWxMaUser(userId, wxSession, params);
      return new ResponseEntity<>(JsonUtils.createObjectNode().put("wxSessionTicket", newTicket),HttpStatus.OK);
    });
  }


//  /**
//   * 微信事件接收
//   *
//   * @param request
//   *
//   * @return
//   *
//   * @throws Exception
//   */
//  @GetMapping(value = "/weixin/event")
//  public String event(HttpServletRequest request) throws Exception {
//    String msgSignature = request.getParameter("signature");
//    String nonce = request.getParameter("nonce");
//    String timestamp = request.getParameter("timestamp");
//    String echostr = request.getParameter("echostr");
//
//    Enumeration<String> parameterNames = request.getParameterNames();
//    while (parameterNames.hasMoreElements()) {
//      logger.info("event param:" + parameterNames.nextElement());
//    }
//    logger.info("msg_signature:" + msgSignature);
//    logger.info("nonce:" + nonce);
//    logger.info("timestamp:" + timestamp);
//    if (StringUtils.isNotBlank(echostr)) {
//      String[] s = {accountsProperties.getToken(), timestamp, nonce};
//      Arrays.sort(s);
//      String gen = SHA1.gen(s);
//      logger.info("gen:" + gen);
//      if (msgSignature.equals(gen)) {
//        return echostr;
//      }
//    }
//    return "";
//  }

//  /**
//   * 微信事件接收
//   *
//   * @param request
//   *
//   * @return
//   *
//   * @throws Exception
//   */
//  @PostMapping(value = "/weixin/event")
//  public String receiveEvent(HttpServletRequest request) throws Exception {
//    String msgSignature = request.getParameter("msg_signature");
//    String nonce = request.getParameter("nonce");
//    String timestamp = request.getParameter("timestamp");
//
//    Enumeration<String> parameterNames = request.getParameterNames();
//    while (parameterNames.hasMoreElements()) {
//      logger.info("event param:" + parameterNames.nextElement());
//    }
//    logger.info("msg_signature:" + msgSignature + " nonce:" + nonce + " timestamp:" + timestamp);
//    WxMpCryptUtil wxMpCryptUtil = new WxMpCryptUtil(configStorage);
//    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//    StringBuilder stb = new StringBuilder();
//    String line;
//    while ((line = in.readLine()) != null) {
//      stb.append(line);
//    }
//    logger.info(stb.toString());
//    String decrypt = wxMpCryptUtil.decrypt(msgSignature, timestamp, nonce, stb.toString());
//    logger.info(decrypt);
//    WxMpXmlMessage message = XStreamTransformer.fromXml(WxMpXmlMessage.class, decrypt);
//    WxMpXmlOutMessage route = messageRouter.route(message);
//    return wxMpCryptUtil.encrypt(route.toString());
//  }

//  /**
//   * 支付完成后微信通知地址
//   *
//   * @param request
//   *
//   * @return
//   *
//   * @throws Exception
//   */
//  @PostMapping(value = "/weixin/pay/notify")
//  public ResponseEntity payNotify(HttpServletRequest request) throws Exception {
//    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//    StringBuilder stb = new StringBuilder();
//    String line;
//    while ((line = in.readLine()) != null) {
//      stb.append(line);
//    }
//    WxPayOrderNotifyResult result = WxPayOrderNotifyResult.fromXML(stb.toString());
//    logger.info("wechat notify", JsonUtils.convert(result, ObjectNode.class).toString());
//    //校验
//    /*WxPayServiceAbstractImpl abcPayService = new WxPayServiceApacheHttpImpl();
//    abcPayService.setConfig(wxPayService.getConfig());*/
//    result.checkResult(wxPayService, WxPayConstants.SignType.MD5, true);
//
//    //校验支付结果
//    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//
//    PreCreateType preCreateType = EnumUtil.parse(PreCreateType.class, "val", Integer.valueOf(result.getAttach()));
//    PayVerifyDto verifyDto = new PayVerifyDto();
//    verifyDto.setBizNo(result.getOutTradeNo());
//    verifyDto.setTransactionId(result.getTransactionId());
//    verifyDto.setPayAmount(new BigDecimal(WxPayOrderNotifyResult.fenToYuan(result.getTotalFee())));
//    verifyDto.setTimeEnd(format.parse(result.getTimeEnd()));
//    verifyDto.setPreCreateType(preCreateType);
//    verifyDto.setTransactionChannelType(TransactionChannelType.WECHAT);
//    WechatTradeStatus tradeStatus = EnumUtil.parse(WechatTradeStatus.class, "code", result.getResultCode());
//
//    verifyDto.setTradeSuccess(tradeStatus.equals(WechatTradeStatus.SUCCESS));
//    String response = (String) payService.verifyPayResult(verifyDto);
//
//    return new ResponseEntity<>(response, HttpStatus.OK);
//  }
//
//  @PostMapping(value = "/weixin/refund/notify")
//  public ResponseEntity refundNotify(HttpServletRequest request) throws Exception {
//    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//    StringBuilder stb = new StringBuilder();
//    String line;
//    while ((line = in.readLine()) != null) {
//      stb.append(line);
//    }
//    WxPayRefundNotifyResult result = WxPayRefundNotifyResult.fromXML(stb.toString(), accountsProperties.getMchKey());
//    logger.info("退款通知", JsonUtils.convert(result, ObjectNode.class).toString());
//
//    RefundResultDto.RefundResultDtoBuilder builder = RefundResultDto.builder();
//    builder.outRefundNo(result.getReqInfo().getOutRefundNo());
//    builder.outTradeNo(result.getReqInfo().getOutTradeNo());
//    builder.refundFee(new BigDecimal(WxPayRefundNotifyResult.fenToYuan(result.getReqInfo().getRefundFee())));
//    builder.settlementRefundFee(new BigDecimal(WxPayRefundNotifyResult.fenToYuan(result.getReqInfo().getSettlementRefundFee())));
//    builder.refundStatus(EnumUtil.parse(WechatRefundStatus.class,"code",result.getReqInfo().getRefundStatus()));
//    builder.refundId(result.getReqInfo().getRefundId());
//    builder.transactionId(result.getReqInfo().getTransactionId());
//    RefundResultDto build = builder.build();
//
//    String res = payService.refundSuccess(build,PreCreateType.PRODUCT);
//    return new ResponseEntity<>(res, HttpStatus.OK);
//  }


  //  @Operation(summary = "生成的小程序码 - 永久有效 - 无次数限制 - 可接受页面参数较短")
  //  @ApiExtension(author = "蒋文", update = "2020-01-14 09:43:30", token = @Token(require = false))
  //  @RequestModel({
  //    @Param(name = "scene", value = "最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其它编码方式）", required = true),
  //    @Param(name = "page", value = "必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index, 根路径前不要填加 /,不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面"),
  //    @Param(name = "width", value = "二维码的宽度，单位 px，最小 280px，最大 1280px", dataType = Integer.class),
  //    @Param(name = "auto_color", value = "自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false", dataType = Boolean.class),
  //    @Param(name = "line_color", value = "auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {\"r\":\"xxx\",\"g\":\"xxx\",\"b\":\"xxx\"} 十进制表示"),
  //    @Param(name = "is_hyaline", value = "是否需要透明底色，为 true 时，生成透明底色的小程序", dataType = Boolean.class)})
  //  @ReturnModel({@Param(name = "base64字符串", value = "返回的图片src", required = true)})
  //  @GetMapping(value = "/mini/getwxacodeunlimit")
  //  public ResponseEntity getwxacodeunlimit(@RequestParam Map<String, String> params) {
  //    String imgSrc = "data:image/png;base64," + Base64.getEncoder()
  //                                                     .encodeToString(weChatService.getWxacodeunlimit(params));
  //    return new ResponseEntity<>(imgSrc, HttpStatus.OK);
  //  }

  @Operation(summary = "生成的小程序码 - 服务器直传 - 永久有效 - 无次数限制 - 可接受页面参数较短")
  @ApiExtension(author = "蒋文", update = "2020-01-14 09:43:30", token = @Token(require = false))
  @RequestModel({
    @Param(name = "scene", value = "最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其它编码方式）", required = true),
    @Param(name = "page", value = "必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index, 根路径前不要填加 /,不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面"),
    @Param(name = "width", value = "二维码的宽度，单位 px，最小 280，最大 1280，默认430", dataType = Integer.class),
    @Param(name = "auto_color", value = "自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认false", dataType = Boolean.class),
    @Param(name = "line_color", value = "auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {\"r\":\"xxx\",\"g\":\"xxx\",\"b\":\"xxx\"} 十进制表示"),
    @Param(name = "is_hyaline", value = "是否需要透明底色，为 true 时，生成透明底色的小程序, 默认false", dataType = Boolean.class),
    @Param(name = "qrcode_name", value = "二维码图片名字(用于上传服务器，尽量不用中文)", required = true),
    @Param(name = "envVersion", value = "要打开的小程序版本。正式版为 release（默认），体验版为 trial，开发版为 develop"),
  })
  @ReturnModel({@Param(name = "服务器图片url", value = "返回的图片src", required = true)})
  @GetMapping(value = "/mini/getwxacodeunlimit/fromservice")
  public ResponseEntity getwxacodeunlimitFromService(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(weChatService.getWxacodeunlimitWithPath(params), HttpStatus.OK);
  }


  //  @Operation(summary = "生成的小程序码 - 永久有效 - 有次数限制 - 可接受 path 参数较长")
  //  @ApiExtension(author = "蒋文", update = "2020-01-14 09:43:30", token = @Token(require = false))
  //  @RequestModel({
  //    @Param(name = "path", value = "扫码进入的小程序页面路径，最大长度 128 字节，不能为空；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 \"?foo=bar\"，即可在 wx.getLaunchOptionsSync 接口中的 query 参数获取到 {foo:\"bar\"}。"),
  //    @Param(name = "width", value = "二维码的宽度，单位 px，最小 280px，最大 1280px", dataType = Integer.class),
  //    @Param(name = "auto_color", value = "自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false", dataType = Boolean.class),
  //    @Param(name = "line_color", value = "auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {\"r\":\"xxx\",\"g\":\"xxx\",\"b\":\"xxx\"} 十进制表示"),
  //    @Param(name = "is_hyaline", value = "是否需要透明底色，为 true 时，生成透明底色的小程序", dataType = Boolean.class)})
  //  @ReturnModel({@Param(name = "base64字符串", value = "返回的图片src", required = true)})
  //  @GetMapping(value = "/mini/getwxaCode")
  //  public ResponseEntity getwxaCode(@RequestParam Map<String, String> params) {
  //    String imgSrc = "data:image/png;base64," + Base64.getEncoder().encodeToString(weChatService.getWxaCode(params));
  //    return new ResponseEntity<>(imgSrc, HttpStatus.OK);
  //  }

  @Operation(summary = "生成的小程序码 - 服务器直传 - 永久有效 - 有次数限制 - 可接受 path 参数较长")
  @ApiExtension(author = "蒋文", update = "2020-01-14 09:43:30", token = @Token(require = false))
  @RequestModel({
    @Param(name = "path", value = "扫码进入的小程序页面路径，最大长度 128 字节，不能为空；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 \"?foo=bar\"，即可在 wx.getLaunchOptionsSync 接口中的 query 参数获取到 {foo:\"bar\"}。"),
    @Param(name = "width", value = "二维码的宽度，单位 px，最小 280，最大 1280，默认430", dataType = Integer.class),
    @Param(name = "auto_color", value = "自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false", dataType = Boolean.class),
    @Param(name = "line_color", value = "auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {\"r\":\"xxx\",\"g\":\"xxx\",\"b\":\"xxx\"} 十进制表示"),
    @Param(name = "is_hyaline", value = "是否需要透明底色，为 true 时，生成透明底色的小程序，默认false", dataType = Boolean.class),
    @Param(name = "envVersion", value = "要打开的小程序版本。正式版为 release（默认），体验版为 trial，开发版为 develop"),
  })
  @ReturnModel({@Param(name = "服务器图片url", value = "返回的图片src", required = true)})
  @GetMapping(value = "/mini/getwxaCode/fromservice")
  public ResponseEntity getwxaCodeFromService(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(weChatService.getWxaCodeWithPath(params), HttpStatus.OK);
  }

  @Operation(summary = "小程序页面推送给微信收录")
  @ApiExtension(author = "蒋文", update = "2020-01-14 09:43:30")
  @RequestModel({@Param(name = "path", value = "页面路径，如：pages/index/index", required = true),
    @Param(name = "query", value = "参数，如：id=1&key=123，需encode后传过来"),})
  @GetMapping(value = "/mini/submitpage")
  public ResponseEntity submitpage(@RequestParam Map<String, String> params) {
    weChatService.submitpage(params);
    return new ResponseEntity<>(true, HttpStatus.OK);
  }



}
