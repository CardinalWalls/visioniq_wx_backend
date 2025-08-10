package com.wk.vpac.main.service.api.user;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.util.crypt.WxMaCryptUtils;
import com.base.components.cache.Cache;
import com.base.components.cache.CacheManager;
import com.base.components.common.dto.io.FileDTO;
import com.base.components.common.exception.business.BusinessException;
import com.base.components.common.id.IdGenerator;
import com.base.components.common.service.message.ChannelMessageSender;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.Md5Util;
import com.base.components.common.util.UUIDUtils;
import com.base.components.oss.OssService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wk.vpac.cache.CacheName;
import com.wk.vpac.database.dao.user.UserBaseInfoDao;
import com.wk.vpac.domain.user.UserBaseInfo;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * WeiXinService
 *
 * @author <a href="lijian_jie@163.com">LiJian</a>
 * @version 1.0.0, 2018-03-16 14:54
 */
@Service
//@RefreshScope
//@ConfigurationProperties(prefix = "base.pm.wechat.messgae")
public class WeChatService {
  /** SESSION_TICKET 无效时，异常编码 */
  public static final int WX_SESSION_TICKET_INVALID_ERROR = 80000;

  /** SESSION_TICKET 对象失效时间 */
  public static final int WX_SESSION_TICKET_EXPIRE_DAYS = 2;
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
//  public static final String WX_MINI_OPENID_PARAM_KEY = "__mini_open_id__";
//  public static final String WX_MP_OPENID_PARAM_KEY = "__mp_open_id__";
//  public static final String WX_AUTO_LOGIN_TOKEN_KEY = "__token__";
//  public static final String WX_AUTO_CACHE_CODE_KEY = "__cache_code__";
//  @Autowired
//  private UserBaseInfoService userBaseInfoService;
  @Autowired
  private UserBaseInfoDao baseInfoDao;
//  @Autowired
//  private WxMpTemplateMsgService msgService;
//  @Autowired
//  private WeixinMsgLogDao msgLogDao;
//  @Autowired
//  private WxPayService wxPayService;
//  @Autowired
//  private OfficialAccountsProperties accountsProperties;
  @Autowired
  private WxMaService wxMaService;
//  @Autowired
//  private BaseUserTypeDao baseUserTypeDao;
//  @Autowired
//  private RegionService regionService;
  @SuppressWarnings("all")
  @Autowired
  private ChannelMessageSender channelMessageSender;
  @Autowired
  private CacheManager cacheManager;
  @Autowired
  private OssService ossService;

  public String buildWxSession(String wxLoginCode){
    Assert.hasText(wxLoginCode, ()->"授权参数错误（wxLoginCode is empty）");
    try {
      WxMaJscode2SessionResult result = verifyMiniCode(wxLoginCode);
      return cacheSessionTicket(result, WX_SESSION_TICKET_EXPIRE_DAYS, TimeUnit.DAYS);
    } catch (WxErrorException e) {
      throw new IllegalArgumentException("请求失败，请重试！", e);
    }
  }

  /**
   * 获取 WxSession
   * @param wxSessionTicket  ticket
   * @param function         执行方法参数：WxSession对象，newTicket
   */
  public <T> T getWxSession(String wxSessionTicket, @NonNull BiFunction<WxMaJscode2SessionResult, String, T> function) {
    if (StringUtils.isNotBlank(wxSessionTicket)) {
      Cache cache = cacheManager.getCache(CacheName.OPENID);
      WxMaJscode2SessionResult re = cache.get(wxSessionTicket, WxMaJscode2SessionResult.class);
      if(re != null){
        Long expire = cache.getExpire(wxSessionTicket, TimeUnit.SECONDS);
        String newKey;
        //失效旧的key
        cache.evict(wxSessionTicket);
        if(expire != null && expire > 0){
          //缓存新的key
          newKey = cacheSessionTicket(re, expire, TimeUnit.SECONDS);
        }else{
          newKey = cacheSessionTicket(re, WX_SESSION_TICKET_EXPIRE_DAYS, TimeUnit.DAYS);
        }
        return function.apply(re, newKey);
      }
    }
    throw new BusinessException("请重新登录（wxSessionTicket is invalid: "+wxSessionTicket+"）",
                                WX_SESSION_TICKET_INVALID_ERROR
    );
  }

  private String cacheSessionTicket(WxMaJscode2SessionResult wxSession, long timeout, TimeUnit timeUnit){
    String key = IdGenerator.getInstance().generate().toString();
    Cache cache = cacheManager.getCache(CacheName.OPENID);
    cache.put(key, wxSession);
    cache.expire(key, timeout, timeUnit);
    return key;
  }

//  /**
//   * 公众号消息穿透配置
//   */
//  private Map<String, String> penetrate;

//  /**
//   * 微信绑定
//   *
//   * @param phone 手机号
//   *
//   * @throws WxErrorException
//   */
//  public void bind(String phone, WxMpUser wxUser) {
//    String nickName = wxUser.getNickname();
//    String headImgUrl = wxUser.getHeadImgUrl();
//    UserBaseInfo userBaseInfo = userBaseInfoService.findUserByPhone(phone);
//    Assert.notNull(userBaseInfo, "未找到对应用户");
//    if (StringUtils.isBlank(wxUser.getNickname())) {
//      nickName = userBaseInfo.getUserNickName();
//    }
//    userBaseInfo.setMpOpenId(wxUser.getOpenId());
//    userBaseInfo.setWxName(nickName);
//    userBaseInfo.setWxImg(headImgUrl);
//    baseInfoDao.saveAndFlush(userBaseInfo);
//  }

  /**
   * 根据code获取微信用户
   *
   * @param code
   *
   * @return
   *
   * @throws WxErrorException
   */
//  public WxMpUser verifyCode(String code) throws WxErrorException {
//    WxMpOAuth2AccessToken accessToken = mpService.oauth2getAccessToken(code);
//    //获取微信用户信息
//    return mpService.oauth2getUserInfo(accessToken, null);
//  }

  /**
   * 根据小程序code获取微信用户
   *
   * @param code jsCode
   */
  public WxMaJscode2SessionResult jsCode2SessionInfo(String code) throws WxErrorException {
    return wxMaService.jsCode2SessionInfo(code);
  }

  public WxMaUserInfo getUserInfo(WxMaJscode2SessionResult session, String signature, String rawData,
                                  String encryptedData, String iv) {
    if (session != null && wxMaService.getUserService().checkUserInfo(session.getSessionKey(), rawData, signature)) {
      // 解密用户信息
      return wxMaService.getUserService().getUserInfo(session.getSessionKey(), encryptedData, iv);
    }
    return null;
  }

//
//  /**
//   * 根据openId获取用户信息
//   *
//   * @param openId
//   *
//   * @return
//   */
//  public UserBaseInfo verifyOpenId(String openId) {
//    return baseInfoDao.findByMpOpenId(openId);
//  }


//  /**
//   * 微信解绑
//   *
//   * @param userId
//   */
//  public void unBind(String userId) {
//    Optional<UserBaseInfo> user = baseInfoDao.findById(userId);
//    Assert.isTrue(user.isPresent(), "user is not present!");
//    UserBaseInfo userBaseInfo = user.get();
//    userBaseInfo.setMpOpenId(null);
//    userBaseInfo.setWxName(null);
//    userBaseInfo.setWxImg(null);
//    baseInfoDao.saveAndFlush(userBaseInfo);
//  }

//  /**
//   * 获取微信绑定状态
//   *
//   * @param userId
//   *
//   * @return
//   */
//  public boolean getBoundStates(String userId) {
//    Optional<UserBaseInfo> user = baseInfoDao.findById(userId);
//    Assert.isTrue(user.isPresent(), "user is not present!");
//    UserBaseInfo userBaseInfo = user.get();
//    return StringUtils.isNotBlank(userBaseInfo.getMpOpenId());
//  }

  //  /**
  //   * 公众号消息发送
  //   *
  //   * @param userId 发送用户id
  //   * @param receiveId 接受用户id
  //   * @param template 消息模板
  //   * @param params 模板参数
  //   *
  //   * @throws WxErrorException
  //   */
  //  @Transactional(rollbackFor = Exception.class)
  //  public void sendWxMessage(String userId, String receiveId, WxMsgTemplate template, Map<String, String> params) {
  //    String[] ids = receiveId.split(",");
  //    if(ids.length == 0){
  //      return;
  //    }
  //    List<UserBaseInfo> user = baseInfoDao.findAllById(Arrays.asList(ids));
  //    user.forEach(userBaseInfo -> {
  //      //未绑定时，不发送消息
  //      if (StringUtils.isNotBlank(userBaseInfo.getMpOpenId())) {
  //        WxMpTemplateMessage.WxMpTemplateMessageBuilder mpTemplateMessage = WxMpTemplateMessage.builder();
  //        mpTemplateMessage.templateId(template.getTemplateId());
  //        mpTemplateMessage.toUser(userBaseInfo.getMpOpenId());
  //        //设置参数
  //        List<WxMpTemplateData> dataList = new ArrayList<>();
  //        for (Map.Entry<String, String> entry : params.entrySet()) {
  //          WxMpTemplateData templateData = new WxMpTemplateData();
  //          templateData.setName(entry.getKey());
  //          templateData.setValue(entry.getValue());
  //          dataList.add(templateData);
  //        }
  //        String url = penetrate.get(template.toString());
  //        if(StringUtils.isNotBlank(params.get("wxMessageUrl"))){
  //          url = penetrate.get(params.get("wxMessageUrl"));
  //        }
  //        logger.info("template:"+template.toString());
  //        if (StringUtils.isNotBlank(url)) {
  //          String urlParams = params.get("urlParams");
  //          logger.info("urlParams:" + urlParams);
  //          String penetrateUrl = url;
  //          if (StringUtils.isNotBlank(urlParams)) {
  //            penetrateUrl = String.format(url, (Object[]) urlParams.split(","));
  //          }
  //          mpTemplateMessage.url(penetrateUrl);
  //          logger.info("parseUrl:" + penetrateUrl);
  //        }
  //        mpTemplateMessage.data(dataList);
  //        String msgId = null;
  //        try {
  //          msgId = msgService.sendTemplateMsg(mpTemplateMessage.build());
  //        } catch (WxErrorException e) {
  //          logger.error(e.getMessage());
  //        }
  //        //消息发送记录
  //        WeixinMsgLog msgLog = new WeixinMsgLog();
  //        msgLog.setMsgId(msgId);
  //        msgLog.setOpenId(userBaseInfo.getMpOpenId());
  //        msgLog.setReceiveId(receiveId);
  //        msgLog.setSendTime(new Date());
  //        msgLog.setTemplateId(template.getTemplateId());
  //        msgLog.setUserId(userId);
  //        msgLog.setWxName(userBaseInfo.getWxName());
  //        msgLog.setTemplateName(template.getDesc());
  //        msgLogDao.saveAndFlush(msgLog);
  //      }
  //    });
  //
  //  }

//  /**
//   * 更新公众号消息接受状态
//   *
//   * @param msgId 消息id
//   * @param status
//   */
//  @Transactional(rollbackFor = Exception.class)
//  public void updateMsgStatus(Long msgId, String status) {
//    ConditionGroup<WeixinMsgLog> condition = ConditionGroup.build().addCondition("msgId", ConditionEnum.OPERATE_EQUAL,
//                                                                                 String.valueOf(msgId)
//    );
//    Optional<WeixinMsgLog> optionalMsgLog = msgLogDao.findOne(condition);
//    Assert.isTrue(optionalMsgLog.isPresent(), "WeixinMsgLog " + msgId + " is not present");
//    WeixinMsgLog msgLog = optionalMsgLog.get();
//    TemplateMsgReceiveStatus logStatus = EnumUtil.parse(TemplateMsgReceiveStatus.class, "weChatVal", status);
//    Assert.notNull(logStatus, "未找到对应状态值：" + status);
//    msgLog.setRecevieStatus(logStatus.getVal());
//    msgLogDao.saveAndFlush(msgLog);
//  }

//  @Transactional(rollbackFor = Exception.class)
//  public UserBaseInfo saveAndUpdateMiniUser(WxMaUserInfo wxMaUserInfo, String userId, String realIp,
//                                            String referrerUserId) {
//    UserBaseInfo userBaseInfo = null;
//    boolean addUser = false;
//    if (userId != null) {
//      userBaseInfo = userBaseInfoService.findById(userId);
//    }
//    if (userBaseInfo == null) {
//      addUser = true;
//      userBaseInfo = new UserBaseInfo();
//      userBaseInfo.setPhone(UUIDUtils.generateKey());
//      userBaseInfo.setPwd(PasswordUtil.encryptPassword(userBaseInfo.getPhone(), "-_-"));
//      userBaseInfo.setUserType(UserType.USER.getCode());
//      userBaseInfo.setUserTypeId(baseUserTypeDao.findIdByUserType(UserType.USER.getCode()));
//      userBaseInfo.setRegistTime(new Date());
//      userBaseInfo.setRegistIp(realIp);
//      userBaseInfo.setStatus(Valid.TRUE.getVal());
//      userBaseInfo.setPasswordErrors(0);
//      try {
//        String[] address = IpLocationUtil.find(realIp);
//        userBaseInfo.setRegistRegion(StringUtils.join(address, "-"));
//        String regionId = regionService.getRegionId(address);
//        userBaseInfo.setRegionId(regionId == null ? "" : regionId);
//      } catch (Exception ignore) {
//      }
//      userBaseInfo.setRemark("");
//      userBaseInfo.setDataSource(DataSource.MOBILE.getVal());
//      userBaseInfo.setAvatar(wxMaUserInfo.getAvatarUrl());
//
//    }
//    ModelUtil.setStringIfNotEmpty(wxMaUserInfo.getNickName(), userBaseInfo::setUserNickName);
//    ModelUtil.setStringIfNotEmpty(wxMaUserInfo.getNickName(), userBaseInfo::setWxName);
//    ModelUtil.setStringIfNotEmpty(wxMaUserInfo.getAvatarUrl(), userBaseInfo::setWxImg);
//    ModelUtil.setIfNotNull(Integer.valueOf(wxMaUserInfo.getGender()), userBaseInfo::setGender);
//    ModelUtil.setStringIfNotEmpty(wxMaUserInfo.getOpenId(), userBaseInfo::setMiniOpenId);
//    ModelUtil.setStringIfNotEmpty(wxMaUserInfo.getUnionId(), userBaseInfo::setWxUnionId);
//    userBaseInfo = baseInfoDao.saveAndFlush(userBaseInfo);
//    if (addUser) {
//      ObjectNode jsonNode = JsonUtils.createObjectNode();
//      jsonNode.put("userId", userBaseInfo.getId());
//      if (StringUtils.isNotEmpty(referrerUserId)) {
//        jsonNode.put("referrerId", referrerUserId);
//      }
//      channelMessageSender.sendMessage(MessageChannel.USER_REGISTER_CHANNEL, jsonNode);
//    }
//    return userBaseInfo;
//  }

//  /**
//   * pc发起订单支付
//   *
//   * @param desc 商品描述
//   * @param total 总金额（分）
//   * @param orderNo 订单编号
//   * @param orderCreateTime 订单创建时间
//   * @param orderExpireTime 订单过期时间
//   * @param ip 终端ip
//   *
//   * @return 二维码地址
//   *
//   */
//  public String weChatPreCreate(String desc, Integer total, String orderNo, String orderCreateTime,
//                                String orderExpireTime, String ip, PreCreateType attach) {
//    WxPayUnifiedOrderRequest.WxPayUnifiedOrderRequestBuilder orderRequest = WxPayUnifiedOrderRequest.newBuilder();
//    //商品描述
//    orderRequest.body(desc.length() > 40 ? desc.substring(0, 40) : desc);
//    //总金额
//    orderRequest.totalFee(total);
//    //商品id
//    orderRequest.productId(orderNo);
//    orderRequest.outTradeNo(orderNo);
//    if (StringUtils.isNotBlank(orderCreateTime)) {
//      //订单生成时间
//      orderRequest.timeStart(orderCreateTime);
//    }
//    if (StringUtils.isNotBlank(orderExpireTime)) {
//      //订单失效时间
//      orderRequest.timeExpire(orderExpireTime);
//    }
//    //终端IP
//    orderRequest.spbillCreateIp(ip);
//    orderRequest.attach(String.valueOf(attach.getVal()));
//    WxPayConfig config = wxPayService.getConfig();
//    WxPayUnifiedOrderResult wxPayUnifiedOrderResult = wxPayUnifiedOrderResult(config, orderRequest);
//    return wxPayUnifiedOrderResult.getCodeURL();
//  }

//  /**
//   * H5订单支付
//   *
//   * @param desc
//   * @param total
//   * @param orderNo
//   * @param orderCreateTime
//   * @param orderExpireTime
//   * @param ip
//   * @param attach
//   *
//   * @return
//   *
//   * @throws Exception
//   */
//  public String h5WeChatPreCreate(String desc, int total, String orderNo, String orderCreateTime,
//                                  String orderExpireTime, String ip, PreCreateType attach) {
//    WxPayUnifiedOrderRequest.WxPayUnifiedOrderRequestBuilder orderRequest = WxPayUnifiedOrderRequest.newBuilder();
//    //商品描述
//    orderRequest.body(desc.length() > 40 ? desc.substring(0, 40) : desc);
//    //总金额
//    orderRequest.totalFee(total);
//    //商品id
//    orderRequest.productId(orderNo);
//    orderRequest.outTradeNo(orderNo);
//    if (StringUtils.isNotBlank(orderCreateTime)) {
//      //订单生成时间
//      orderRequest.timeStart(orderCreateTime);
//    }
//    if (StringUtils.isNotBlank(orderExpireTime)) {
//      //订单失效时间
//      orderRequest.timeExpire(orderExpireTime);
//    }
//    //终端IP
//    orderRequest.spbillCreateIp(ip);
//    orderRequest.attach(String.valueOf(attach.getVal()));
//    orderRequest.tradeType(WxPayConstants.TradeType.MWEB);
//    WxPayConfig config = wxPayService.getConfig();
//    WxPayUnifiedOrderResult wxPayUnifiedOrderResult = wxPayUnifiedOrderResult(config, orderRequest);
//    return wxPayUnifiedOrderResult.getMwebUrl();
//  }

//  public String h5WeChatBrowserPreCreate(String desc, int total, String orderNo, String orderCreateTime,
//                                         String orderExpireTime, String ip, PreCreateType attach, String openId) {
//    WxPayUnifiedOrderRequest.WxPayUnifiedOrderRequestBuilder orderRequest = WxPayUnifiedOrderRequest.newBuilder();
//    //商品描述
//    orderRequest.body(desc.length() > 40 ? desc.substring(0, 40) : desc);
//    //总金额
//    orderRequest.totalFee(total);
//    //商品id
//    orderRequest.productId(orderNo);
//    orderRequest.outTradeNo(orderNo);
//    orderRequest.openid(openId);
//    if (StringUtils.isNotBlank(orderCreateTime)) {
//      //订单生成时间
//      orderRequest.timeStart(orderCreateTime);
//    }
//    if (StringUtils.isNotBlank(orderExpireTime)) {
//      //订单失效时间
//      orderRequest.timeExpire(orderExpireTime);
//    }
//    //终端IP
//    orderRequest.spbillCreateIp(ip);
//    orderRequest.attach(String.valueOf(attach.getVal()));
//    orderRequest.tradeType(WxPayConstants.TradeType.JSAPI);
//    WxPayConfig config = wxPayService.getConfig();
//    WxPayUnifiedOrderResult wxPayUnifiedOrderResult = wxPayUnifiedOrderResult(config, orderRequest);
//    Map<String, String> param = new HashMap<>(6);
//    String prepayId = wxPayUnifiedOrderResult.getPrepayId();
//    param.put("appId", config.getAppId());
//    DateTime now = DateTime.now();
//    DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMddHHmmss");
//    String timeStamp = now.toString(format);
//    param.put("timeStamp", timeStamp);
//    param.put("nonceStr", RandomUtils.getRandomStr());
//    param.put("package", "prepay_id=" + prepayId);
//    param.put("signType", config.getSignType());
//    String sign = SignUtils.createSign(param, config.getSignType(), config.getMchKey(), new String[0]);
//    param.put("paySign", sign);
//    ObjectNode paramObj = JsonUtils.convert(param, ObjectNode.class);
//    return paramObj.toString();
//  }
//
//
//  private WxPayUnifiedOrderResult wxPayUnifiedOrderResult(WxPayConfig config,
//                                                          WxPayUnifiedOrderRequest.WxPayUnifiedOrderRequestBuilder orderRequest) {
//    try {
//      if (config.isUseSandboxEnv()) {
//        if (StringUtils.isBlank(accountsProperties.getSandboxKey())) {
//          accountsProperties.setSandboxKey(wxPayService.getSandboxSignKey());
//        }
//        config.setMchKey(accountsProperties.getSandboxKey());
//      }
//      return wxPayService.unifiedOrder(orderRequest.build());
//    } catch (WxPayException e) {
//      logger.error("微信支付异常：" + e.getReturnMsg(), e);
//      throw new BusinessException("获取支付链接异常", 100);
//    }
//  }
//
//  public WxPayOrderQueryResult queryPayInfo(String bizNo) {
//    try {
//      return wxPayService.queryOrder(null, bizNo);
//    } catch (WxPayException e) {
//      logger.error("查询支付结果异常", e);
//      throw new BusinessException("查询支付结果异常 " + e.getErrCode());
//    }
//  }
//
//  public Map<String, String> getPenetrate() {
//    return penetrate;
//  }
//
//  public void setPenetrate(Map<String, String> penetrate) {
//    this.penetrate = penetrate;
//  }

  public UserBaseInfo verifyMiniOpenId(String openId) {
    return baseInfoDao.findByMiniOpenId(openId);
  }

//  public Optional<UserBaseInfo> verifyWxUnionId(String wxUnionId) {
//    return baseInfoDao.findByWxUnionId(wxUnionId);
//  }

  private WxMaJscode2SessionResult verifyMiniCode(String code) throws WxErrorException {
    return wxMaService.jsCode2SessionInfo(code);
  }

  public WxMaUserInfo getMiniUserInfo(WxMaJscode2SessionResult wxSession, Map<String, String> params) {
    String signature = ConvertUtil.checkNotNull(params, "signature", String.class);
    String rawData = ConvertUtil.checkNotNull(params, "rawData", String.class);
    String encryptedData = ConvertUtil.checkNotNull(params, "encryptedData", String.class);
    String iv = ConvertUtil.checkNotNull(params, "iv", String.class);
    final String generatedSignature = DigestUtils.sha1Hex(rawData + wxSession.getSessionKey());
    if (logger.isInfoEnabled()) {
      logger.info("signature:"+signature);
      logger.info("generatedSignature:"+generatedSignature);
      logger.info("data"+WxMaCryptUtils.decrypt(wxSession.getSessionKey(), encryptedData, iv));
    }
    Assert.isTrue(wxMaService.getUserService().checkUserInfo(wxSession.getSessionKey(), rawData, signature), "用户校验失败");
    return wxMaService.getUserService().getUserInfo(wxSession.getSessionKey(), encryptedData, iv);
  }

  public String decodeMiniUserPhone(WxMaJscode2SessionResult wxSession, Map<String, String> params) {
    String encryptedData = ConvertUtil.checkNotNull(params, "encryptedData", String.class);
    String iv = ConvertUtil.checkNotNull(params, "iv", String.class);
    if(logger.isInfoEnabled()){
      String decrypt = WxMaCryptUtils.decrypt(wxSession.getSessionKey(), encryptedData, iv);
      logger.info("sessionKey:"+wxSession.getSessionKey());
      logger.info("encryptedData:"+encryptedData);
      logger.info("iv:"+iv);
      logger.info("decrypt:"+decrypt);
    }
//    // 解密
    WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(wxSession.getSessionKey(), encryptedData, iv);
    return phoneNoInfo.getPhoneNumber();
  }

  /** 获取步数前30天步数信息，日期升序 */
//  public List<WxMaRunStepInfo> getRunData(Map<String, String> params) {
//    String sessionKey = ConvertUtil.checkNotNull(params, "sessionKey", String.class);
//    String encryptedData = ConvertUtil.checkNotNull(params, "encryptedData", String.class);
//    String iv = ConvertUtil.checkNotNull(params, "iv", String.class);
//    return wxMaService.getRunService().getRunStepInfo(sessionKey, encryptedData, iv);
//  }

  /** 生成的小程序码 - 永久有效 - 有次数限制 */
  public String getWxaCodeWithPath(Map<String, String> params) {
    return pathWithExists(params, this::getWxaCode);
  }
  private byte[] getWxaCode(Map<String, String> params) {
    try {
      String path = params.get("path");
      int width = ConvertUtil.convert(params.get("width"), 430);
      boolean auto_color = ConvertUtil.convert(params.get("auto_color"), false);
      String line_color = params.get("line_color");
      WxMaCodeLineColor wxMaCodeLineColor = null;
      if (StringUtils.isNotEmpty(line_color)) {
        ObjectNode objectNode = JsonUtils.reader(line_color, ObjectNode.class);
        if (objectNode != null && objectNode.get("r") != null) {
          wxMaCodeLineColor = new WxMaCodeLineColor(objectNode.get("r").asText(), objectNode.get("g").asText(),
                                                    objectNode.get("b").asText()
          );
        }
      }
      boolean is_hyaline = ConvertUtil.convert(params.get("is_hyaline"), false);
      return wxMaService.getQrcodeService()
                        .createWxaCodeBytes(path, params.getOrDefault("envVersion", "release"),
                                            width, auto_color, wxMaCodeLineColor, is_hyaline);
    } catch (WxErrorException e) {
      throw new IllegalArgumentException(e.getError().getErrorMsg(), e);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  /** 生成的小程序码 - 永久有效 */
  public String getWxacodeunlimitWithPath(Map<String, String> params) {
    return pathWithExists(params, this::getWxacodeunlimit);
  }
  private byte[] getWxacodeunlimit(Map<String, String> params) {
    try {
      String scene = params.get("scene");
      String page = params.get("page");
      int width = ConvertUtil.convert(params.get("width"), 430);
      boolean auto_color = ConvertUtil.convert(params.get("auto_color"), false);
      String line_color = params.get("line_color");
      WxMaCodeLineColor wxMaCodeLineColor = null;
      if (StringUtils.isNotEmpty(line_color)) {
        ObjectNode objectNode = JsonUtils.reader(line_color, ObjectNode.class);
        if (objectNode != null && objectNode.get("r") != null) {
          wxMaCodeLineColor = new WxMaCodeLineColor(objectNode.get("r").asText(), objectNode.get("g").asText(),
                                                    objectNode.get("b").asText()
          );
        }
      }
      boolean is_hyaline = ConvertUtil.convert(params.get("is_hyaline"), false);
      return wxMaService.getQrcodeService().createWxaCodeUnlimitBytes(scene, page, false,
                                                                      params.getOrDefault("envVersion", "release"),
                                                                      width, auto_color, wxMaCodeLineColor, is_hyaline
      );
    } catch (WxErrorException e) {
      throw new IllegalArgumentException(e.getError().getErrorMsg(), e);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * 小程序推送页面给微信(单页面)
   *
   * @param params
   */
  public void submitpage(Map<String, String> params) {
    try {
      ArrayNode pages = JsonUtils.createArrayNode();
      ObjectNode page = JsonUtils.createObjectNode();
      String path = ConvertUtil.checkNotNull(params, "path", "路径不能为空", String.class);
      path = URLDecoder.decode(path, "UTF-8");
      page.put("path", path);
      String query = params.get("query");
      if (StringUtils.isNotEmpty(query)) {
        query = URLDecoder.decode(query, "UTF-8");
        page.put("query", query);
      }
      submitpage(pages);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * 小程序推送页面给微信(多页面)
   * @param pages 每次最多1000页
   * {
   *     "pages": [
   *         {
   *             "path": "pages/index/index",
   *             "query": "userName=wechat_user"
   *         },
   *         {
   *             "path": "pages/video/index",
   *             "query": "vid=123"
   *         }
   *     ]
   * }
   */
  public void submitpage(ArrayNode pages) {
    try {
      ObjectNode param = JsonUtils.createObjectNode();
      param.putPOJO("pages", pages);
      wxMaService
        .post("https://api.weixin.qq.com/wxa/search/wxaapi_submitpages?access_token=" + wxMaService.getAccessToken(),
              param
        );
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }




  public String pathWithExists(Map params, Function<Map, byte[]> createImg){
    String md5 = toMd5(params);
    String path = OssService.PUBLIC_PATH + "/qrcode/" + md5 + ".jpg";
    String downloadUrl = ossService.getDownloadToken(null, path, true);
    if(StringUtils.isNotBlank(downloadUrl)){
      try {
        HttpURLConnection connection = (HttpURLConnection) new URL(downloadUrl).openConnection();
        int code = connection.getResponseCode();
        if(code != 200){
          byte[] bytes = createImg.apply(params);
          FileDTO file = new FileDTO(bytes);
          file.setPrivateFile(false);
          file.setTempFile(false);
          file.setPath("qrcode");
          file.setFileName(md5 + ".jpg");
          file.setValidatePath(false);
          ossService.serverUploadFile(file);
        }
      } catch (Exception e) {
        throw new IllegalArgumentException("生成图片失败", e);
      }
    }
    return path;
  }

  private String toMd5(Map param){
    String md5;
    if(param == null || param.isEmpty()){
      throw new IllegalArgumentException("无请求参数");
    }
    try {
      TreeMap treeMap = new TreeMap<>(param);
      String json = JsonUtils.toString(treeMap);
      md5 = Md5Util.md5(json);
    } catch (Exception ignore) {
      md5 = UUIDUtils.generateKey();
    }
    return md5;
  }

  public String getMaAppId(){
    return wxMaService.getWxMaConfig().getAppid();
  }

}
