

package com.wk.vpac.main.service.api.user;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.base.components.cache.Cache;
import com.base.components.cache.CacheLock;
import com.base.components.cache.CacheManager;
import com.base.components.common.constants.Valid;
import com.base.components.common.constants.sys.Dates;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.exception.business.BusinessException;
import com.base.components.common.exception.business.PasswordErrorException;
import com.base.components.common.exception.business.PasswordErrorLimitException;
import com.base.components.common.exception.business.SmsException;
import com.base.components.common.exception.cache.CacheLockTimeoutException;
import com.base.components.common.exception.other.ForbiddenException;
import com.base.components.common.log.system.SystemLogManager;
import com.base.components.common.service.message.ChannelMessageSender;
import com.base.components.common.token.TokenCacheObj;
import com.base.components.common.token.TokenManager;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.Callable;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.IpLocationUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.ObjectTool;
import com.base.components.common.util.PasswordUtil;
import com.base.components.common.util.RandomCodeHelper;
import com.base.components.common.util.UUIDUtils;
import com.base.components.common.util.ValidatorUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.oss.OssService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wk.vpac.cache.CacheName;
import com.wk.vpac.common.constants.UserType;
import com.wk.vpac.common.constants.msgqueue.channels.MessageChannel;
import com.wk.vpac.common.constants.sys.Gender;
import com.wk.vpac.common.constants.user.DataSource;
import com.wk.vpac.common.service.sms.SmsService;
import com.wk.vpac.common.token.user.ExpertToken;
import com.wk.vpac.common.token.user.UserToken;
import com.wk.vpac.database.dao.region.CommunityDao;
import com.wk.vpac.database.dao.user.BaseUserTypeDao;
import com.wk.vpac.database.dao.user.ExpertDao;
import com.wk.vpac.database.dao.user.UserBaseInfoDao;
import com.wk.vpac.domain.sys.Region;
import com.wk.vpac.domain.user.Expert;
import com.wk.vpac.domain.user.UserBaseInfo;
import com.wk.vpac.main.service.api.region.RegionService;
import com.wk.vpac.main.util.PasswordCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UserBaseInfoService
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2018-03-16 11:26
 */
@Service
//@RefreshScope
public class UserBaseInfoService extends AbstractJpaService<UserBaseInfo, String, UserBaseInfoDao> {
//  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String USER_REGIST_LOCK_PRE = "USER_REGIST_LOCK::";
  @Autowired
  private CacheManager cacheManager;
  @Autowired
  private TokenManager tokenManager;
  @Autowired
  private UserCountOfTimeService userCountOfTimeService;
  @Value("${base.userPwdError.limit.countOfHour:100}")
  private int userPwdErrorCountOfHour;
  @Autowired
  private BaseUserTypeDao baseUserTypeDao;
  @SuppressWarnings("all")
  @Autowired
  private OssService ossService;
  @Autowired
  private RegionService regionService;
  @SuppressWarnings("all")
  @Autowired
  private ChannelMessageSender channelMessageSender;
  @SuppressWarnings("all")
  @Autowired
  private SmsService smsService;
  @Autowired
  private CacheLock cacheLock;
  @Autowired
  private WeChatService weChatService;
  @Autowired
  private CommunityDao communityDao;
  @Autowired
  private ExpertDao expertDao;

  public DataPage querySimpleUser(Map<String, String> params){
    return getDao().querySimpleUser(params);
  }

  public UserBaseInfo findByAccountOrPhone(String account, String phone){
    return getDao().findByAccountOrPhone(account, phone);
  }

  public <T> T lockRegistUser(String lockKey, Callable<T> callable){
    try {
      return cacheLock.lock(USER_REGIST_LOCK_PRE + lockKey, callable);
    } catch (CacheLockTimeoutException e) {
      throw new IllegalArgumentException("请勿频繁操作，请稍后重试");
    }
  }

  @Transactional(rollbackFor = Exception.class)
  public ObjectNode userLogin(UserBaseInfo user, Map<String, String> param, boolean checkLimit) {
    String loginIp = param.get("loginIp");
    if (user == null) {
      user = loginByAccount(param, checkLimit, account->getDao().findByAccountOrPhone(account, account));
    } else {
      Assert.notNull(loginIp, "登录IP不能为空");
      if (user.getStatus() == 0) {
        throw new ForbiddenException("账户已禁用");
      }
    }
    // 传入此参数时，该参数代表限定的用户类型，即该类型的用户才能登录
    if (StringUtils.isNotEmpty(param.get("restrictType")) && !param.get("restrictType")
                                                                   .equals(String.valueOf(user.getUserType()))) {
      throw new IllegalArgumentException("该账号不存在!");
    }
    // 设置登录信息（登录IP、登录时间、选择地区等等）
    setLoginInfo(user, param, loginIp);
    // 登录时携带微信登录信息，则处理信息
    //loginByWeChatHandle(user, param);
    getDao().save(user);
    UserType type = UserType.parse(user.getUserType());
    Assert.notNull(type, "用户登录类型匹配错误！");
    // 缓存userToken并将用户信息添加至返回信息对象
    ObjectNode res = cacheTokenAndPutRes(user, type);
    // 发送登录事件
    sendLoginEvent(user);
    return res;
  }

  /**
   * 发送登录事件
   *
   * @param user
   */
  public void sendLoginEvent(UserBaseInfo user) {
    ObjectNode objectNode = JsonUtils.createObjectNode();
    /*{{first.DATA}}
    帐号登录时间：{{keyword1.DATA}}
    帐号登录IP：{{keyword2.DATA}}
    {{remark.DATA}}*/
    objectNode.put("userName", user.getUserNickName());
    objectNode.put("targetId", user.getId());
    objectNode.put("localId", user.getId());
    objectNode.put("first", "您好，您账号已经登录");
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    objectNode.put("keyword1", format.format(user.getLastLoginTime()));
    objectNode.put("keyword2", user.getLastLoginIp());
    objectNode.put("remark", "如果本次登录不是您本人所为，说明您的帐号已经被盗！为减少您的损失，请立即联系客服");
    channelMessageSender.sendMessage(MessageChannel.USER_LOGIN, objectNode);
  }

  /**
   * 设置登录信息（登录IP、登录时间、选择地区等等）
   *
   * @param user
   * @param param
   * @param loginIp
   */
  private void setLoginInfo(UserBaseInfo user, Map<String, String> param, String loginIp) {
    user.setLastLoginTime(new Date());
    user.setLastLoginIp(loginIp);
    String selectRegionId = param.get("selectRegionId");
    if (StringUtils.isNotBlank(selectRegionId)) {
      Region region = regionService.findById(selectRegionId);
      if (region != null) {
        user.setSelectRegion(region.getName());
        user.setSelectRegionId(selectRegionId);
      }
    }
  }

  /**
   * 缓存userToken并将用户信息添加至返回信息对象
   *
   * @param user
   * @param type
   */
  private ObjectNode cacheTokenAndPutRes(UserBaseInfo user, UserType type) {
    UserToken userToken;
    try {
      userToken = (UserToken) type.getTokenType().getTypeClass().newInstance();
    } catch (Exception e) {
      throw new BusinessException("用户登录类型匹配错误！", e);
    }
    userToken.setUserId(user.getId());
    userToken.setLoginTime(user.getLastLoginTime());
    userToken.setUserNickName(user.getUserNickName());
    userToken.setPhone(user.getPhone());
    userToken.setToken(UUIDUtils.generateKey());
    userToken.setType(type.getCode());
    userToken.setTypeName(baseUserTypeDao.findNameByUserType(type.getCode()));
    userToken.setWxImg(user.getWxImg());
    userToken.setWxName(user.getWxName());
    userToken.setAvatar(user.getAvatar());
    if(userToken instanceof ExpertToken e){
      Expert expert = expertDao.findByUserId(user.getId());
      if(expert == null){
        throw new ForbiddenException("此账号未绑定专家信息，请联系系统管理员");
      }
      if(expert.getStatus() != 1){
        throw new ForbiddenException("此专家账号未启用");
      }
      e.setExpertId(expert.getId());
    }
    tokenManager.cacheToken(userToken);
    SystemLogManager.getInstance().addLog(userToken);
    ObjectNode returnNode = JsonUtils.convert(userToken, ObjectNode.class);
    returnNode.put("status", user.getStatus());
    returnNode.put("tokenKey", type.getTokenType().getTokenKey());
    return returnNode;
  }

  /**
   * 通过账号或手机登录
   *
   * @param param
   * @param checkLimit
   *
   * @return
   */
  public UserBaseInfo loginByAccount(Map<String, String> param, boolean checkLimit, Function<String, UserBaseInfo> findUser) {
    UserBaseInfo user;
    String account = param.get("account");
    String pwd = param.get("pwd");
    String verifyCode = param.get("verifyCode");
    String errorLimitCode = param.get("errorLimitCode");
    Assert.isTrue(StringUtils.isNotBlank(pwd) || StringUtils.isNotBlank(verifyCode), "请输入密码");
    user = findUser.apply(account);
    Assert.notNull(user, "未找到用户");
    if (user.getStatus() != 1) {
      throw new ForbiddenException("账户已禁用");
    }
    //    if(user.getStatus() == 2){
    //      throw new BusinessException("账户未激活", 101);
    //    }
    Assert.isTrue(user.getUserType() != UserType.UNKNOWN.getCode(), "账户未分配角色");
    //密码登录
    if (StringUtils.isNotBlank(pwd) && StringUtils.isEmpty(errorLimitCode)) {
      if (checkLimit) {
        if (userCountOfTimeService.checkIsUserPwdErrorLimit(CacheName.PASSWORD_ERROR, account)) {
          throw new PasswordErrorLimitException(
            SmsException.ErrorCode.password_error_limit.getInfo().replace("{count}", userPwdErrorCountOfHour + ""),
            SmsException.ErrorCode.password_error_limit.getCode()
          );
        }
      }
      if(StringUtils.isBlank(user.getPwd())){
        throw new ForbiddenException("非密码账号");
      }
      boolean pwdChecked = pwd.equals(PasswordUtil.decryptPassword(account, user.getPwd()));
      if (!pwdChecked) {
        throw new PasswordErrorException("密码错误");
      } else {
        userCountOfTimeService.removeUserPwdErrorLimit(CacheName.PASSWORD_ERROR, account);
      }
    }
    //密码+验证码登录
    if (StringUtils.isNotBlank(pwd) && StringUtils.isNotEmpty(errorLimitCode)) {
      Cache cache = cacheManager.getCache(CacheName.VERIFY_CODE_LG);
      String cachedErrorCode = cache.get(errorLimitCode, String.class);
      RandomCodeHelper.checkCacheCode(errorLimitCode, cachedErrorCode, "验证码错误");
      boolean pwdChecked = pwd.equals(PasswordUtil.decryptPassword(account, user.getPwd()));
      if (pwdChecked) {
        cache.evict(errorLimitCode);
        userCountOfTimeService.removeUserPwdErrorLimit(CacheName.PASSWORD_ERROR, account);
      } else {
        throw new PasswordErrorException("密码错误");
      }
    }
    //短信登录
    else if (StringUtils.isNotBlank(verifyCode) && StringUtils.isEmpty(pwd)) {
      Cache cache = cacheManager.getCache(CacheName.VERIFY_CODE_LG);
      String cachedVerifyCode = cache.get(account, String.class);
      RandomCodeHelper.checkCacheCode(verifyCode, cachedVerifyCode, "短信验证码错误");
      cache.evict(account);
    }
    return user;
  }

  /**
   * 用户登录
   *
   * @param param -
   * <p> account        - Notnull  - Str - 账号或手机
   * <p> passwd       - Nullable  - Str - 登录密码，与verifycode不能都为空
   * <p> verifyCode   - Nullable  - Str - 短信验证码，与passwd不能都为空，见 /edges/sms/code
   * <p> loginIp      - Nullable - Str - 登录IP
   *
   * @return UserToken
   */
  @Transactional(rollbackFor = Exception.class)
  public ObjectNode userLogin(Map<String, String> param) {
    return userLogin(null, param, true);
  }


  /**
   * 输入错误密码，累加次数
   *
   * @param cacheKey        - Notnull  - Str - 缓存key
   */
  @Transactional(rollbackFor = Exception.class)
  public void userLoginPasswordError(UserBaseInfo user, String cacheKey) {
    if (user != null) {
      user.setPasswordErrors(user.getPasswordErrors() == null ? 1 : user.getPasswordErrors() + 1);
      getDao().saveAndFlush(user);
      userCountOfTimeService.userPwdErrorCount(CacheName.PASSWORD_ERROR, cacheKey);
    }
  }


  /**
   * 用户注册, 并登录
   *
   * @param param -
   * <p> phone        - Notnull  - Str - 手机号
   * <p> passwd       - Notnull  - Str - 登录密码
   * <p> registIp     - Nullable - Str - 注册IP
   * <p> referrerId   - Nullable - Str - 推荐人id
   *
   * @return UserInfo
   */
  @Transactional(rollbackFor = Exception.class)
  public ObjectNode userRegister(Map<String, String> param) {
    String phone = ConvertUtil.checkNotNull(param, "phone", "手机号不能为空", String.class);
    Assert.isTrue(ValidatorUtil.isPhoneNum(phone), "手机格式不正确");
    ConvertUtil.checkNotNull(param, "pwd", "密码不能为空", String.class);
    String verifyCode = ConvertUtil.checkNotNull(param, "verifyCode", "短信验证码不能为空", String.class);
    Cache cache = cacheManager.getCache(CacheName.VERIFY_CODE_RG);
    String cachedVerifyCode = cache.get(phone, String.class);
    RandomCodeHelper.checkCacheCode(verifyCode, cachedVerifyCode, "短信验证码错误");
    cache.evict(phone);
    Assert.isTrue(!checkExists(phone), "该手机用户已注册");
    String pwd = param.get("pwd");
    String registIp = param.get("registIp");
    String[] address = IpLocationUtil.find(registIp);

    Date now = new Date();
    String userNickName = StringUtils.isBlank(param.get("userNickName"))
                          ? phone.substring(phone.length() - 4)
                          : param.get("userNickName");
    //用户主表
    UserBaseInfo userBase = new UserBaseInfo();
    userBase.setAccount(phone);
    userBase.setPhone(phone);
    userBase.setUserNickName(userNickName);
    Assert.isTrue(PasswordCheckUtil.evalPassword(pwd), "密码过于简单，请更换密码！");
    userBase.setPwd(PasswordUtil.encryptPassword(phone, pwd));
    userBase.setGender(Gender.unknown.getCode());
    userBase.setUserType(UserType.USER.getCode());
    userBase.setUserTypeId(baseUserTypeDao.findIdByUserType(UserType.USER.getCode()));
    userBase.setRegistTime(now);
    userBase.setRegistIp(registIp);
    userBase.setRegistRegion(StringUtils.join(address, "-"));
    String regionId = regionService.getRegionId(address);
    userBase.setRegionId(regionId == null ? "" : regionId);
    userBase.setPasswordErrors(0);
    userBase.setStatus(1);
    userBase.setLastLoginIp("");
    userBase.setRemark("");
    String isMobile = param.get("isMobile");
    userBase.setDataSource(StringUtils.isBlank(isMobile) ? DataSource.PC.getVal() : Integer.valueOf(isMobile));
    String selectRegionId = param.get("selectRegionId");
    if (StringUtils.isNotBlank(selectRegionId)) {
      Region region = regionService.findById(selectRegionId);
      if (region != null) {
        userBase.setSelectRegion(region.getName());
        userBase.setSelectRegionId(selectRegionId);
      }
    }
    //绑定携带的微信信息
//    loginByWeChatHandle(userBase, param);
    getDao().saveAndFlush(userBase);

    ObjectNode jsonNode = JsonUtils.createObjectNode();
    jsonNode.put("userId", userBase.getId());
    jsonNode.put("phone", phone);
    channelMessageSender.sendMessage(MessageChannel.USER_REGISTER_CHANNEL, jsonNode);
    param.put("account", userBase.getAccount());
    param.put("loginIp", registIp);
    return userLogin(param);
  }


  @Transactional(rollbackFor = Exception.class)
  public ObjectNode selectRegion(String userId, String regionId) {
    Assert.hasText(userId, "用户未找到");
    Assert.hasText(regionId, "请设置区域");
    UserBaseInfo user = findById(userId);
    Assert.notNull(user, "用户未找到");
    Region region = regionService.findById(regionId);
    Assert.notNull(region, "区域未找到");
    Assert.isTrue(region.getLeaf() != null && Valid.TRUE.getVal() == region.getLeaf(), "请选择末级区域");
    user.setSelectRegionId(regionId);
    user.setSelectRegion(region.getName());
    getDao().saveAndFlush(user);
    return JsonUtils.createObjectNode().put("selectRegionId", regionId).put("selectRegion", region.getName());
  }


  /**
   * 用户修改密码
   *
   * @param param -
   * <p> phone        - Notnull  - Str - 手机号
   * <p> pwd          - Notnull  - Str - 新登录密码
   * <p> oldPwd       - Nullable  - Str - 旧密码，非空时会验证
   */
  @Transactional(rollbackFor = Exception.class)
  public void changePassword(Map<String, String> param) {
    String phone = ConvertUtil.checkNotNull(param, "phone", "手机号不能为空", String.class);
    Assert.isTrue(ValidatorUtil.isPhoneNum(phone), "手机格式不正确");
    UserBaseInfo user = getDao().findByAccountOrPhone(phone, phone);
    Assert.notNull(user, "未找到用户（" + phone + "）");
    String pwd = ConvertUtil.checkNotNull(param, "pwd", "新密码不能为空", String.class);
    Assert.isTrue(PasswordCheckUtil.evalPassword(pwd), "密码过于简单，请更换密码！");
    String oldPwd = param.get("oldPwd");
    if (StringUtils.isBlank(oldPwd)) {
      String verifyCode = ConvertUtil.checkNotNull(param, "verifyCode", "短信验证码不能为空", String.class);
      Cache cache = cacheManager.getCache(CacheName.VERIFY_CODE_CP);
      String cachedVerifyCode = cache.get(phone, String.class);
      RandomCodeHelper.checkCacheCode(verifyCode, cachedVerifyCode, "短信验证码错误");
      cache.evict(phone);
    } else {
      Assert.hasText(oldPwd, "旧密码不能为空");
      Assert.isTrue(PasswordUtil.decryptPassword(phone, user.getPwd()).equals(oldPwd), "旧密码不正确");
      Assert.isTrue(!oldPwd.equals(pwd), "新旧密码不能一样");
    }
    Assert.isTrue(PasswordCheckUtil.evalPassword(pwd), "密码过于简单，请更换密码！");
    user.setPwd(PasswordUtil.encryptPassword(phone, pwd));
    getDao().saveAndFlush(user);
  }

  /**
   * 修改手机号
   *
   * @param param -
   * <p> userId        - Notnull  - Str - userId
   * <p> phone         - Notnull  - Str - 新手机
   * <p> verifyCode    - Notnull  - Str - 新手机接收验证码
   */
  @Transactional(rollbackFor = Exception.class)
  public UserBaseInfo updateUserPhone(Map<String, String> param) {
    String phone = param.get("phone");
    Assert.isTrue(ValidatorUtil.isPhoneNum(phone), "手机格式不正确");
    Assert.isTrue(!checkExists(phone), "该手机用户已注册");
    String verifyCode = param.get("verifyCode");
    Cache cache = cacheManager.getCache(CacheName.VERIFY_CODE_PHONE);
    String cachedVerifyCode = cache.get(phone, String.class);
    RandomCodeHelper.checkCacheCode(verifyCode, cachedVerifyCode, "短信验证码错误");
    cache.evict(phone);
    UserBaseInfo user = findById(param.get("userId"));
    Assert.notNull(user, "未找到用户");
    changePhoneResetPwd(user, phone);
    getDao().saveAndFlush(user);
    return user;
  }

  public void changePhoneResetPwd(UserBaseInfo user, String changePhone){
    String oldPhone = user.getPhone();
    if (StringUtils.isNotBlank(oldPhone) && StringUtils.isNotBlank(user.getPwd())) {
      String pwd = PasswordUtil.decryptPassword(oldPhone, user.getPwd());
      user.setPwd(PasswordUtil.encryptPassword(changePhone, pwd));
    }
    user.setPhone(changePhone);
    user.setAccount(changePhone);
  }


  /**
   * 检查手机号是否存在
   *
   * @param phone
   *
   * @return true=存在, false=不存在
   */
  public boolean checkExists(String phone) {
    Assert.hasText(phone, "手机号为空");
    ConditionGroup<UserBaseInfo> build = ConditionGroup.build(ConditionEnum.Link.LINK_OR);
    build.addCondition("phone", ConditionEnum.OPERATE_EQUAL, phone);
    //    build.addCondition("phone2", ConditionEnum.OPERATE_EQUAL, phone);
    long count = getDao().count(build);
    return count > 0;
  }

  public UserBaseInfo findUserByPhone(String phone) {
    return getDao().findByPhone(phone);
  }


  /**
   * 完善用户信息
   *
   * @param userId
   * @param params -
   * <p>  userNickName             - Nullable  - Str - 姓名
   * <p>  gender                   - Nullable  - Int - 性别
   * <p>  birth                    - Nullable  - Str - 生日
   * <p>  userEmail                - Nullable  - Str - 邮箱
   * <p>  avatar                   - Nullable  - Str - 头像
   * <p>  selectRegionId           - Nullable  - Str - 用户选择的地区Id
   * <p>  selectRegion             - Nullable  - Str - 用户选择的地区
   */
  @Transactional(rollbackFor = Exception.class)
  public void completeInfo(String userId, Map<String, String> params) {
    Assert.notNull(userId, "无用户信息");
    Optional<UserBaseInfo> optional = getDao().findById(userId);
    Assert.isTrue(optional.isPresent(), "未查到用户信息");
    UserBaseInfo userBaseInfo = optional.get();
    ConvertUtil.setPropertyIfNotBlank(userBaseInfo, "realName", params);
    ConvertUtil.setPropertyIfNotBlank(userBaseInfo, "birth", params);
    Gender gender = Gender.parseCode(ConvertUtil.convert(params.get("gender"), -1));
    if(gender != null){
      userBaseInfo.setGender(gender.getCode());
    }
    String idCard = params.get("idCard");
    if(StringUtils.isNotBlank(idCard)){
      if(!ValidatorUtil.isIDCard(idCard)){
        throw new IllegalArgumentException("身份证位数不正确");
      }
      if (getDao().existsByIdCardAndIdNot(idCard, userId)) {
        throw new IllegalArgumentException("身份证已存在");
      }
      userBaseInfo.setIdCard(idCard);
      userBaseInfo.setBirth(idCard.substring(6,10) + "-" + idCard.substring(10,12)+ "-" +idCard.substring(12,14));
      try {
        userBaseInfo.setGender(Integer.parseInt(idCard.substring(16, 17)) % 2 == 0 ? Gender.female.getCode() : Gender.male.getCode());
      } catch (Exception ignore) {
      }
    }
    String userNickName = params.get("userNickName");
    if(StringUtils.isBlank(userBaseInfo.getUserNickName())){
      userBaseInfo.setUserNickName(StringUtils.isBlank(userNickName)?userBaseInfo.getRealName():userNickName);
    }
    else if(StringUtils.isNotBlank(userNickName)){
      userBaseInfo.setUserNickName(userNickName);
    }
    Date now = new Date();
//    ConvertUtil.setPropertyIfNotBlank(userBaseInfo, "communityId", params);
//    String expertId = params.get("expertId");
//    if(StringUtils.isNotBlank(expertId) && !expertId.equals(userBaseInfo.getExpertId())){
//      userBaseInfo.setExpertId(expertId);
//      userBaseInfo.setBindExpertTime(now);
//    }
    String contactPhone = params.getOrDefault("contactPhone", "");
    if (StringUtils.isNotBlank(contactPhone) && !ValidatorUtil.isPhoneNum(contactPhone)) {
      throw new IllegalArgumentException("联系电话格式错误");
    }
    userBaseInfo.setContactPhone(contactPhone);
    ConvertUtil.setPropertyIfNotBlank(userBaseInfo, "profile", params);
    ConvertUtil.setPropertyIfNotBlank(userBaseInfo, "jobPosition", params);
    ConvertUtil.setPropertyIfNotBlank(userBaseInfo, "wxImg", params);
    ConvertUtil.setPropertyIfNotBlank(userBaseInfo, "wxName", params);
    ConvertUtil.setPropertyIfNotBlank(userBaseInfo, "selectRegionId", params);
    ConvertUtil.setPropertyIfNotBlank(userBaseInfo, "selectRegion", params);
    String avatar = params.get("avatar");
    if (StringUtils.isNotBlank(avatar)) {
      if (ossService.checkIsTemp(avatar)) {
        //头像持久化
        ArrayNode arrayNode = JsonUtils.mapper.createArrayNode();
        ObjectNode objectNode = JsonUtils.mapper.createObjectNode();
        objectNode.put("name", userBaseInfo.getUserNickName());
        objectNode.put("url", avatar);
        arrayNode.add(objectNode);
        ArrayNode re = ossService.persistent("", "", arrayNode);
        if (re != null && re.size() != 0) {
          avatar = re.get(0).get("url").asText();
        }
      }
      userBaseInfo.setAvatar(avatar);
      if(StringUtils.isBlank(userBaseInfo.getWxImg()) || userBaseInfo.getWxImg().startsWith("/temp/")){
        userBaseInfo.setWxImg(avatar);
      }
    }
    String userEmail = params.get("userEmail");
    if (StringUtils.isNotBlank(userEmail) && StringUtils.isEmpty(userBaseInfo.getUserEmail())) {
      Assert.isTrue(checkEmail(userEmail), "邮箱格式错误");
      userBaseInfo.setUserEmail(userEmail);
    }
    userBaseInfo.setUpdateTime(now);
    getDao().saveAndFlush(userBaseInfo);
    // 完善用户信息
    //    if(notComplete && StringUtils.isNoneBlank(userBaseInfo.getBirth(), userBaseInfo.getSelectRegionId(), userBaseInfo.getProfile())){
    //      channelMessageSender.sendMessage(MessageChannel.COMPLETE_USER_INFO,
    //                                       JsonUtils.createObjectNode().put("userId", userId));
    //    }
  }


  /**
   * 获取用户资料(去除敏感信息)
   *
   * @param userId
   *
   * @return
   */
  public UserBaseInfo findUserInfoWithoutPwdById(String userId) {
    Optional<UserBaseInfo> optional = getDao().findById(userId);
    if (optional.isPresent()) {
      UserBaseInfo userBaseInfo = optional.get();
      userBaseInfo = ObjectTool
        .setFieldsToNull(userBaseInfo, "pwd", "passwordErrors", "lastLoginIp", "registIp", "registTime", "payPwd");
      return userBaseInfo;
    }
    return null;
  }

  /**
   * 修改用户邮箱
   *
   * @param userId
   * @param email
   * @param verifyCode
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateUserEmail(String userId, String email, String verifyCode, String oldEmail) {
    Assert.isTrue(checkEmail(email), "邮箱格式错误");
    UserBaseInfo user = findById(userId);
    Assert.notNull(user, "未查到用户");
    if (StringUtils.isNotEmpty(oldEmail)) {
      Assert.isTrue(oldEmail.equals(user.getUserEmail()), "原邮箱不正确");
    }
    String phone = user.getPhone();
    Assert.notNull(phone, "用户接受短信的电话不正确");
    Cache cache = cacheManager.getCache(CacheName.VERIFY_CODE_EMAIL);
    String cachedVerifyCode = cache.get(phone, String.class);
    RandomCodeHelper.checkCacheCode(verifyCode, cachedVerifyCode, "短信验证码错误");
    cache.evict(phone);
    user.setUserEmail(email);
    getDao().saveAndFlush(user);
  }


  /**
   * 第一次设置用户邮箱
   *
   * @param userId
   * @param email
   */
  @Transactional(rollbackFor = Exception.class)
  public void setUserEmail(String userId, String email) {
    Assert.isTrue(checkEmail(email), "邮箱格式错误");
    UserBaseInfo user = findById(userId);
    Assert.notNull(user, "未查到用户");
    Assert.isTrue(StringUtils.isEmpty(user.getUserEmail()), "已存在邮箱，请走修改流程");
    user.setUserEmail(email);
    getDao().saveAndFlush(user);
  }

  private static final Pattern emailPattern = Pattern
    .compile("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$");

  public boolean checkEmail(String email) {
    Matcher m = emailPattern.matcher(email);
    return m.matches();
  }


  public String findPhoneById(String targetId) {
    return getDao().findPhoneById(targetId);
  }

  /**
   * 判断当日是否登录过
   *
   * @param userId
   *
   * @return
   */
  public boolean isLoginFirstOfDay(String userId) {
    Cache cache = cacheManager.getCache(CacheName.FIRST_LOGIN_OF_DAY);
    Integer count = cache.get(userId, Integer.class);
    boolean isFirst = false;
    if (count == null) {
      isFirst = true;
    }
    return isFirst;
  }

  @Transactional(rollbackFor = Exception.class)
  public void clearMpOtherBind(String openId, String phone) {
    getDao().clearMpOtherBind(openId, phone);
  }

  @Transactional(rollbackFor = Exception.class)
  public void clearOtherMiniBind(String miniOpenId, String phone) {
    getDao().clearOtherMiniBind(miniOpenId, phone);
  }

  public ObjectNode findUserInfo(String userId) {
    Optional<UserBaseInfo> optional = getDao().findById(userId);
    ObjectNode node = JsonUtils.createObjectNode();
    if (optional.isPresent()) {
      UserBaseInfo userBaseInfo = optional.get();
      node.put("id", userId);
      if (ValidatorUtil.isPhoneNum(userBaseInfo.getPhone())) {
        node.put("phone", userBaseInfo.getPhone());
      }
      node.put("lastLoginTime",
               new DateTime(userBaseInfo.getLastLoginTime()).toString(Dates.DATE_TIME_FORMATTER_PATTERN)
      );
      node.put("userNickName", userBaseInfo.getUserNickName());
      node.put("gender", userBaseInfo.getGender());
      node.put("userType", userBaseInfo.getUserType());
      node.put("idCard", userBaseInfo.getIdCard());
      node.put("avatar", userBaseInfo.getAvatar());
      node.put("gender", userBaseInfo.getGender());
      node.put("wxName", userBaseInfo.getWxName());
      node.put("wxImg", userBaseInfo.getWxImg());
      node.put("birth", userBaseInfo.getBirth());
      node.put("selectRegionId", userBaseInfo.getSelectRegionId());
      node.put("selectRegion", userBaseInfo.getSelectRegion());
      DateTime dateTime = new DateTime(userBaseInfo.getRegistTime());
      node.put("registTime", dateTime.toString(Dates.DATE_TIME_FORMATTER_PATTERN));
      node.put("registDays", Days.daysBetween(dateTime, DateTime.now()).getDays());
      node.put("userEmail", userBaseInfo.getUserEmail());
      node.put("infoComplete", StringUtils
        .isNoneBlank(userBaseInfo.getBirth(), userBaseInfo.getSelectRegionId(), userBaseInfo.getProfile()));
      node.put("hasMiniOpenId", StringUtils.isNotBlank(userBaseInfo.getMiniOpenId()));
      node.put("integral", userBaseInfo.getIntegral());
      node.put("jobPosition", userBaseInfo.getJobPosition());
      node.put("realName", userBaseInfo.getRealName());
      node.put("parentUserId", userBaseInfo.getParentUserId());
//      node.put("expertId", userBaseInfo.getExpertId());
      node.put("contactPhone", userBaseInfo.getContactPhone());
//      communityDao.findById(userBaseInfo.getCommunityId()).ifPresent(c-> node.putPOJO("community", c));
//      DataPage<Map<String, Object>> expert = expertDao.page(Map.of("id", userBaseInfo.getExpertId()));
//      if(expert.getTotal() >= 0){
//        node.putPOJO("expert", expert.getList().get(0));
//      }
    }
    return node;
  }

  @Transactional(rollbackFor = Exception.class)
  public void logout() {
    TokenCacheObj tokenCacheObj = TokenThreadLocal.getTokenObjNonNull();
    tokenManager.removeToken(tokenCacheObj.getToken());
    getDao().clearMpBind(tokenCacheObj.objId().toString());
  }

//  @Transactional(rollbackFor = Exception.class)
//  public ObjectNode registerOrLoginByMaUser(Map<String, String> params, String ip) {
//    String code = ConvertUtil.checkNotNull(params, "code", String.class);
//    WxMaJscode2SessionResult result;
//    try {
//      result = weChatService.verifyMiniCode(code);
//    } catch (WxErrorException e) {
//      throw new IllegalArgumentException("请求失败，请重试！", e);
//    }
//    UserBaseInfo userBaseInfo = weChatService.verifyMiniOpenId(result.getOpenid());
//    ObjectNode objectNode;
//    if (userBaseInfo == null) {
//      objectNode = lockRegistUser(result.getOpenid(), ()->{
//        params.put("sessionKey", result.getSessionKey());
//        //注册新用户
//        return userRegisterByWxMaUser(result, params, ip);
//      });
//    } else {
//      params.put("loginIp", ip);
//      objectNode = userLogin(userBaseInfo, params, true);
//    }
//    objectNode.put("sessionKey", result.getSessionKey());
//    return objectNode;
//  }

  public ObjectNode userRegisterByWxMaUser(WxMaJscode2SessionResult session, Map<String, String> params, String ip) {
    WxMaUserInfo miniUserInfo = weChatService.getMiniUserInfo(session, params);
    UserBaseInfo userBase = createBaseUserInfo(miniUserInfo.getNickName(), miniUserInfo.getAvatarUrl(), ip);
    userBase.setWxName(miniUserInfo.getNickName());
    userBase.setWxImg(miniUserInfo.getAvatarUrl());
    userBase.setMiniOpenId(session.getOpenid());
    Gender gender = Gender.parseDesc(miniUserInfo.getGender());
    userBase.setGender(gender == null ? Gender.unknown.getCode() : gender.getCode());
    userBase.setParentUserId(ConvertUtil.convert(params.get("parentUserId"), ""));
    userBase.setRemark(ConvertUtil.convert(params.get("remark"), ""));
    //    int freeQuestionCountInit = dictionaryCacheService
    //      .listDataVal(Register.class, Register.FREE_QUESTION_COUNT_INIT, 1);
    getDao().saveAndFlush(userBase);
    UserType type = UserType.parse(userBase.getUserType());

    ObjectNode jsonNode = JsonUtils.createObjectNode();
    jsonNode.put("userId", userBase.getId());
    jsonNode.put("parentUserId", userBase.getParentUserId());
    channelMessageSender.sendMessage(MessageChannel.USER_REGISTER_CHANNEL, jsonNode);
    //登录 存入缓存
    return cacheTokenAndPutRes(userBase, type);
  }

  /**
   * 创建用户基础信息，不包含账号密码
   *
   * @param nickName
   * @param registIp
   *
   * @return
   */
  private UserBaseInfo createBaseUserInfo(String nickName, String avatar, String registIp) {
    String[] address = IpLocationUtil.find(registIp);
    UserBaseInfo userBase = new UserBaseInfo();
    userBase.setUserNickName(nickName);
    userBase.setAvatar(avatar);
    userBase.setUserType(UserType.USER.getCode());
    userBase.setUserTypeId(baseUserTypeDao.findIdByUserType(UserType.USER.getCode()));
    userBase.setRegistTime(new Date());
    userBase.setRegistIp(registIp);
    userBase.setRegistRegion(StringUtils.join(address, "-"));
    String regionId = regionService.getRegionId(address);
    userBase.setRegionId(regionId == null ? "" : regionId);
    userBase.setPasswordErrors(0);
    userBase.setStatus(1);
    userBase.setLastLoginIp(registIp);
    userBase.setRemark("");
    return userBase;
  }

  public void bindWxMaUser(String userId, WxMaJscode2SessionResult wxSession, Map<String, String> params) {
    WxMaUserInfo miniUserInfo = weChatService.getMiniUserInfo(wxSession, params);
    if (miniUserInfo != null) {
      UserBaseInfo oldUser = getDao().findByMiniOpenId(wxSession.getOpenid());
      if (oldUser != null) {
        oldUser.setMiniOpenId("");
        oldUser.setWxName("");
        saveAndFlush(oldUser);
      }
      UserBaseInfo user = findById(userId);
      if (!wxSession.getOpenid().equals(user.getMpOpenId())) {
        user.setMiniOpenId(wxSession.getOpenid());
        user.setWxName(miniUserInfo.getNickName());
        user.setWxImg(miniUserInfo.getAvatarUrl());
        saveAndFlush(user);
      }
    }
  }

  @Transactional(rollbackFor = Exception.class)
  public void bindPhone(String userId, String phone, String verifyCode, String pwd) {
    Assert.isTrue(ValidatorUtil.isPhoneNum(phone), "手机格式不正确");

    Cache cache = cacheManager.getCache(CacheName.VERIFY_PHONE_BIND);
    String cachedVerifyCode = cache.get(phone, String.class);
    RandomCodeHelper.checkCacheCode(verifyCode, cachedVerifyCode, "短信验证码错误");
    cache.evict(phone);
    UserBaseInfo user = getDao().loadUser(phone, phone);
    Assert.isNull(user, "该手机号已被其它账号注册");

    UserBaseInfo bindUser = findById(userId);
    bindUser.setAccount(phone);
    bindUser.setPhone(phone);
    Assert.isTrue(PasswordCheckUtil.evalPassword(pwd), "密码过于简单，请更换密码！");
    bindUser.setPwd(PasswordUtil.encryptPassword(phone, pwd));
    getDao().saveAndFlush(bindUser);
  }

  @Transactional(rollbackFor = Exception.class)
  public void bindPhone(String userId, String phone) {
    Assert.isTrue(ValidatorUtil.isPhoneNum(phone), "手机格式不正确");
    UserBaseInfo user = getDao().loadUser(phone, phone);
    Assert.isNull(user, "该手机号已被其它账号注册");
    UserBaseInfo bindUser = findById(userId);
    if (StringUtils.isNotBlank(bindUser.getPwd()) && StringUtils.isNotBlank(bindUser.getPhone())) {
      //旧手机号解密，新手机号加密
      bindUser.setPwd(
        PasswordUtil.encryptPassword(phone, PasswordUtil.decryptPassword(bindUser.getPhone(), bindUser.getPwd())));
    }
    bindUser.setAccount(phone);
    bindUser.setPhone(phone);
    getDao().saveAndFlush(bindUser);
  }

  @Transactional(rollbackFor = Exception.class)
  public ObjectNode registerOrLoginByPhone(Map<String, String> params, String ip) {
    String wxSessionTicket = ConvertUtil.checkNotNull(params.get("wxSessionTicket"), "wxSessionTicket is empty", String.class);
    //未找到用户并且微信用户已授权获取手机号时 根据手机号注册新用户
    return weChatService.getWxSession(wxSessionTicket, (wxSession, newTicket)->{
      ObjectNode node = null;
      String openId = wxSession.getOpenid();
      UserBaseInfo userBaseInfo = weChatService.verifyMiniOpenId(openId);
      if (userBaseInfo != null) {
        params.put("loginIp", ip);
        node = userLogin(userBaseInfo, params, true);
      }
      else{
        if(StringUtils.isNoneBlank(params.get("encryptedData"), params.get("iv"))){
          String phone = weChatService.decodeMiniUserPhone(wxSession, params);
          UserBaseInfo user = getDao().findByAccountOrPhone(phone, phone);
          //手机号不存在，则注册用户
          if(user==null){
            node = lockRegistUser(phone, ()->{
              //清除原账号的openId
              getDao().clearMiniBind(openId);
              //注册用户
              params.put("phone",phone);
              return userRegisterByWxPhone(openId, params, ip);
            });
          }
          else{
            //手机号账号的openId和当前微信openId不一致，重新绑定openId
            if(!openId.equals(user.getMiniOpenId())){
              //清除原账号的openId
              getDao().clearMiniBind(openId);
              getDao().bindMiniOpenId(user.getId(), openId);
              user.setMiniOpenId(openId);
            }
            params.put("loginIp", ip);
            node = userLogin(user, params, false);
          }
        }
      }
      return (node == null?JsonUtils.createObjectNode() : node).put("wxSessionTicket", newTicket);
    });
  }

  private ObjectNode userRegisterByWxPhone(String openId, Map<String, String> params, String ip) {
    String phone = params.get("phone");
    String name = phone.substring(7, 11);
    UserBaseInfo userBase = createBaseUserInfo(
      name, "", ip);
    userBase.setMiniOpenId(openId);
    userBase.setPhone(phone);
    userBase.setAccount(phone);
    userBase.setGender(Gender.unknown.getCode());
    userBase.setParentUserId(ConvertUtil.convert(params.get("parentUserId"), ""));
    userBase.setRemark(ConvertUtil.convert(params.get("remark"), ""));
    userBase.setExpertId(ConvertUtil.convert(params.get("expertId"), ""));
    //    int freeQuestionCountInit = dictionaryCacheService
    //      .listDataVal(Register.class, Register.FREE_QUESTION_COUNT_INIT, 1);
//    userBase.setFreeQuestionCount(0);

    getDao().saveAndFlush(userBase);

    UserType type = UserType.parse(userBase.getUserType());

    // 积分事件
//    ObjectNode objectNode = JsonUtils.createObjectNode();
//    objectNode.put("userId", userBase.getParentUserId());
//    objectNode.put("srcId",userBase.getId());
//    objectNode.put("title",userBase.getPhone());
//    channelMessageSender
//      .sendMessage(MessageChannel.USER_INTEGRAL_CHANNEL, objectNode, TransactionSrc.PROMOTIONREGIST.getCode() + "");

    //登录 存入缓存
    return cacheTokenAndPutRes(userBase, type);
  }

  @Transactional(rollbackFor = Exception.class)
  public String findOrCreateUserByPhone(String phone){
    String id = getDao().findIdByPhone(phone);
    if(id == null){
      UserBaseInfo userBase = new UserBaseInfo();
      userBase.setAccount(phone);
      userBase.setPhone(phone);
      userBase.setUserNickName(StringUtils.substring(phone, 7));
      userBase.setGender(Gender.unknown.getCode());
      userBase.setUserType(UserType.USER.getCode());
      userBase.setUserTypeId(baseUserTypeDao.findIdByUserType(UserType.USER.getCode()));
      userBase.setRegistTime(new Date());
      userBase.setRegistIp("127.0.0.1");
      userBase.setPasswordErrors(0);
      userBase.setStatus(1);
      userBase.setLastLoginIp("");
      userBase.setRemark("");
      userBase.setDataSource(DataSource.MOBILE.getVal());
      userBase = getDao().saveAndFlush(userBase);
      id = userBase.getId();
    }
    return id;
  }
}
