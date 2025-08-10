package com.wk.vpac.main.service.admin.user;

import com.base.components.common.constants.Valid;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.exception.Exceptions;
import com.base.components.common.token.TokenManager;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.ExcelTool;
import com.base.components.common.util.IPUtils;
import com.base.components.common.util.IpLocationUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.PasswordUtil;
import com.base.components.common.util.ValidatorUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wk.vpac.common.constants.UserType;
import com.wk.vpac.common.constants.sys.Gender;
import com.wk.vpac.common.constants.user.DataSource;
import com.wk.vpac.common.service.sms.SmsService;
import com.wk.vpac.database.dao.user.BaseUserTypeDao;
import com.wk.vpac.database.dao.user.ExpertDao;
import com.wk.vpac.database.dao.user.UserArchiveDao;
import com.wk.vpac.database.dao.user.UserBaseInfoDao;
import com.wk.vpac.domain.sys.Region;
import com.wk.vpac.domain.user.Expert;
import com.wk.vpac.domain.user.UserBaseInfo;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import com.wk.vpac.main.service.admin.region.RegionService;
import com.wk.vpac.main.util.PasswordCheckConfig;
import com.wk.vpac.main.util.PasswordCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * UserBaseInfoService
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2018/3/28 0028 14:21
 */
@Service
public class UserBaseInfoService extends AbstractJpaService<UserBaseInfo, String, UserBaseInfoDao> {
  @Autowired
  AttachmentService attachmentService;
  @Autowired
  private BaseUserTypeDao baseUserTypeDao;
  @Autowired
  private RegionService regionService;
  @Autowired
  private TokenManager tokenManager;
  @SuppressWarnings("all")
  @Autowired
  private SmsService smsService;
  @Autowired
  private UserBaseInfoDao userBaseInfoDao;
  @Autowired
  private ExpertDao expertDao;
  @Autowired
  private UserArchiveDao userArchiveDao;

  public DataPage page(Map<String, String> params){
    return getDao().page(params);
  }
  public DataPage expertPage(Map<String, String> params){
    return expertDao.page(params);
  }

  public UserBaseInfo save(Map<String, String> params){
    String id = ConvertUtil.convert(params.get("id"), "");
    String name = ConvertUtil.checkNotNull(params.get("userNickName"), "请填写姓名", String.class);
    String pwd = ConvertUtil.convert(params.get("pwd"), "").trim();
    UserType userType = UserType.parse(ConvertUtil.convert(params.get("userType"), -1));
    UserBaseInfo user;
    Date now = new Date();
    if(StringUtils.isBlank(id)){
      Assert.notNull(userType, ()->"请选择用户类型");
      Assert.isTrue(!UserType.EXPERT.equals(userType), ()->"用户类型不能直接设置为“专家”类型");
      String phone = ConvertUtil.checkNotNull(params.get("phone"), "请填写手机号", String.class);
      Assert.isTrue(ValidatorUtil.isPhoneNum(phone), "手机号格式不正确");
      if (userBaseInfoDao.existsByPhone(phone)) {
        throw new IllegalArgumentException("手机号已经存在：" + phone);
      }
      user = new UserBaseInfo();
      user.setUserTypeId(baseUserTypeDao.findIdByUserType(userType.getCode()));
      user.setUserType(userType.getCode());
      user.setRegistTime(now);
      user.setAccount(phone);
      user.setPhone(phone);
      pwd = StringUtils.isBlank(pwd) ? phone.substring(phone.length() - 6) : pwd;
      if (!PasswordCheckUtil.checkPasswordLength(pwd)) {
        throw new IllegalArgumentException("登录密码长度至少需要"+PasswordCheckConfig.MIN_LENGTH+"位以上");
      }
      user.setPwd(PasswordUtil.encryptPassword(phone, pwd));
    }else{
      UserBaseInfo exists = findById(id);
      if(userType != null && exists.getUserType() != userType.getCode()){
        Assert.isTrue(!UserType.EXPERT.equals(userType), ()->"用户类型不能直接设置为“专家”类型");
        exists.setUserTypeId(baseUserTypeDao.findIdByUserType(userType.getCode()));
        exists.setUserType(userType.getCode());
      }
      Assert.notNull(exists, "未找到数据");
      if(StringUtils.isNotBlank(pwd)){
        String srcPwd = StringUtils.isBlank(exists.getPwd()) ? "" : PasswordUtil.decryptPassword(exists.getPhone(), exists.getPwd());
        if(!srcPwd.equals(pwd)){
          Assert.isTrue(PasswordCheckUtil.evalPassword(pwd), "密码过于简单，请更换密码！");
//          if (!PasswordCheckUtil.checkPasswordLength(pwd)) {
//            throw new IllegalArgumentException("登录密码长度至少需要"+PasswordCheckConfig.MIN_LENGTH+"位以上");
//          }
          exists.setPwd(PasswordUtil.encryptPassword(exists.getPhone(), pwd));
        }
      }
      user = exists;
    }
    user.setUpdateTime(now);
    user.setUserNickName(name);
    AtomicReference<Runnable> updateExpert = new AtomicReference<>();
    if (user.getUserType() == UserType.USER.getCode()) {
      String expertId = StringUtils.trimToEmpty(params.get("expertId"));
      if(StringUtils.isNotBlank(expertId)){
        if(!expertId.equals(user.getExpertId())){
          Expert expert = expertDao.findById(expertId).orElseThrow(Exceptions.of(() -> "未找到的绑定专家信息"));
          Assert.isTrue(!expert.getUserId().equals(user.getId()), ()->"不能绑定自己作为绑定的专家");
          updateExpert.set(()->userArchiveDao.updateExpertId(user.getId(), expertId));
        }
      }
      user.setExpertId(expertId);
    }
    user.setStatus(ConvertUtil.convert(params.get("status"), 1));
    user.setRealName(params.getOrDefault("realName", ""));
    user.setRemark(params.getOrDefault("remark", ""));
    user.setGender(ConvertUtil.convert(params.get("gender"), Gender.unknown.getCode()));
    user.setIdCard(params.getOrDefault("idCard", ""));
    if(StringUtils.isNotBlank(user.getIdCard())){
      if(!ValidatorUtil.isIDCard(user.getIdCard())){
        throw new IllegalArgumentException("身份证长度错误");
      }
      if(StringUtils.isBlank(id)){
        if (getDao().existsByIdCard(user.getIdCard())) {
          throw new IllegalArgumentException("身份证号已存在："+user.getIdCard());
        }
      }else{
        if (getDao().existsByIdCardAndIdNot(user.getIdCard(), id)) {
          throw new IllegalArgumentException("身份证号已存在："+user.getIdCard());
        }
      }
      user.setBirth(user.getIdCard().substring(6,10) + "-" + user.getIdCard().substring(10,12)+ "-" + user.getIdCard().substring(12,14));
    }
    return TransactionActivation.start(()->{
      if(updateExpert.get() != null){
        updateExpert.get().run();
      }
      return save(user);
    });
  }

  @Transactional(rollbackFor = Exception.class)
  public String addUser(Map<String, String> params, HttpServletRequest request){
    String phone = ConvertUtil.checkNotNull(params,"phone", String.class);
    Assert.isTrue(ValidatorUtil.isPhoneNum(phone), "手机格式不正确");
    Assert.isTrue(!checkExists(phone), "该手机用户已注册");
    Date now = new Date();
    String userNickName = StringUtils.isBlank(params.get("userNickName"))
                          ? phone.substring(phone.length() - 4)
                          : params.get("userNickName");

    UserBaseInfo userBase = new UserBaseInfo();
    userBase.setAccount(phone);
    userBase.setPhone(phone);
    userBase.setUserNickName(userNickName);
    String registIp = IPUtils.getRealIp(request);
    String[] address = IpLocationUtil.find(registIp);
    String password = phone.substring(phone.length() - 6);
    userBase.setPwd(PasswordUtil.encryptPassword(phone, password));
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
    userBase.setDataSource(DataSource.PC.getVal());
    String selectRegionId = params.get("selectRegionId");
    if (StringUtils.isNotBlank(selectRegionId)) {
      Region region = regionService.findById(selectRegionId);
      if (region != null) {
        userBase.setSelectRegion(region.getName());
        userBase.setSelectRegionId(selectRegionId);
      }
    }
    saveAndFlush(userBase);
    return password;
  }

  public DataPage listPage(Map<String, String> params) throws IOException {
    String regionId = params.get("regionId");
    if (StringUtils.isNotEmpty(regionId)) {
      List<Region> all = regionService.findAll();
      Set<String> allChildren = regionService.findAllChildren(regionId, all);
      params.put("regionIds", JsonUtils.toString(allChildren));
    }
    return getDao().listPage(params).map(val -> {
      if (val.get("avatar") != null) {
        val.put("avatar", attachmentService.displayAtta(String.valueOf(val.get("avatar"))));
      }
      return val;
    });
  }



  public UserBaseInfo findByPhone(String phone) {
    return getDao().findByPhone(phone);
  }


  @Transactional(rollbackFor = Exception.class)
  public void disableUser(String userId) {
    UserBaseInfo user = findById(userId);
    user.setStatus(Valid.FALSE.getVal());
    getDao().saveAndFlush(user);
    // 冻结用户则需清除token信息
    if (user.getStatus() == Valid.FALSE.getVal()) {
      tokenManager.cleanTokenWithObjId(user.getId());
    }
  }

  @Transactional(rollbackFor = Exception.class)
  public void enableUser(String userId) {
    UserBaseInfo user = findById(userId);
    user.setStatus(Valid.TRUE.getVal());
    getDao().saveAndFlush(user);
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

  public DataPage querySimpleUser(Map<String, String> params) {
    return getDao().querySimpleUser(params);
  }
  @Transactional(rollbackFor = Exception.class)
  public int updateParentUserId(String userId, String parentUserId){
    return getDao().updateParentUserId(userId, parentUserId);
  }

  @Transactional(rollbackFor = Exception.class)
  public int updateLevel(String userId, int level){
    if(level >= 0){
      return getDao().updateLevel(userId, level);
    }
    return 0;
  }

  public ObjectNode importUser(Map<String, String> params){
    URL fileUrl;
    try {
      fileUrl = new URL(ConvertUtil.checkNotNull(params, "fileUrl", "请上传数据文件", String.class));
    } catch (Exception e) {
      throw new IllegalArgumentException("导入数据错误", e);
    }
    boolean toUpdate = ConvertUtil.convert(params.get("toUpdate"), false);
    int startRow = ConvertUtil.convert(params.get("startRow"), 1) - 1;
    List<String> errors = Lists.newArrayList();
    AtomicInteger add = new AtomicInteger();
    Set<String> update = Sets.newHashSet();
    Date now = new Date();
    String userTypeId = baseUserTypeDao.findIdByUserType(UserType.USER.getCode());
    List<UserBaseInfo> userList = Lists.newArrayList();
    Set<String> phones = Sets.newHashSet();
    Set<String> idCards = Sets.newHashSet();
    try(BufferedInputStream bis = new BufferedInputStream(fileUrl.openConnection().getInputStream())) {
      ExcelTool.readExcel(bis, startRow, fileUrl.getPath().endsWith(".xlsx"), 2000, (row, data, e) -> {
        if(data != null){
          String phone = getVal(data, 0);
          if(StringUtils.isNotBlank(phone)){
            if (!ValidatorUtil.isPhoneNum(phone)) {
              errors.add(errorMsg(row, "手机号位数错误"));
            }
            else{
              UserBaseInfo user = getDao().findByPhone(phone);
              if(!toUpdate && user != null){
                errors.add(errorMsg(row, "手机号已存在"));
              }
              else {
                if(user == null){
                  user = new UserBaseInfo();
                  user.setPhone(phone);
                  user.setAccount(phone);
                  user.setRegistTime(now);
                  user.setUserTypeId(userTypeId);
                  user.setUserType(UserType.USER.getCode());
                  user.setPwd(PasswordUtil.encryptPassword(phone, phone.substring(phone.length() - 6)));
                }
                int errorSize = errors.size();
                for (Map.Entry<Integer, Handler> entry : IMPORT_HANDLER.entrySet()) {
                  entry.getValue().apply(errors, user, row, getVal(data, entry.getKey()));
                  if(errors.size() > errorSize){
                    return null;
                  }
                }
                if (!phones.add(user.getPhone())) {
                  errors.add(errorMsg(row, "手机号已存在"));
                  return null;
                }
                if (StringUtils.isNotBlank(user.getIdCard()) && !idCards.add(user.getIdCard())) {
                  errors.add(errorMsg(row, "身份证已存在"));
                  return null;
                }
                if(user.getId() != null){
                  update.add(phone);
                }else{
                  add.getAndIncrement();
                }
                user.setUpdateTime(now);
                userList.add(user);
              }
            }
          }
        }
        return null;
      });
    } catch (Exception e) {
      if(e.getCause() instanceof NotOLE2FileException){
        throw new IllegalArgumentException("导入的文件格式错误，请将文件另存为“.xls”或“.xlsx”格式");
      }
      throw new IllegalArgumentException("导入数据错误", e);
    }
    TransactionActivation.start(()->saveAll(userList));
    return JsonUtils.createObjectNode().put("add", add.get())
                    .put("update", update.size()).putPOJO("errors", errors);
  }

  private String getVal(String[] data, int index){
    try {
      return data[index];
    } catch (Exception ignore) {
      return "";
    }
  }

  private String errorMsg(Row row, String msg){
    return (row.getRowNum() + 1) + "行：" + msg;
  }

  private interface Handler{
    void apply(List<String> errors, UserBaseInfo user, Row row, String value);
  }

  private final Map<Integer, Handler> IMPORT_HANDLER = Maps.newLinkedHashMap();
  {
    //0=phone
    IMPORT_HANDLER.put(0, (errors, user, row, value) -> user.setPhone(value));
    //1=name
    IMPORT_HANDLER.put(1, (errors, user, row, value) -> {
      if(StringUtils.isBlank(value)){
        errors.add(errorMsg(row, "姓名未填写"));
      } else {
        user.setUserNickName(value);
        user.setRealName(value);
      }
    });
    //2=gender
    IMPORT_HANDLER.put(2, (errors, user, row, value) -> user.setGender("男".equals(value) ? 1 : ("女".equals(value) ? 2 : 0)));
    //3=idCard
    IMPORT_HANDLER.put(3, (errors, user, row, value) -> {
      if(StringUtils.isNotBlank(value)){
        if(user.getId() == null && getDao().existsByIdCard(value)){
          errors.add(errorMsg(row, "身份证已存在"));
        }
        else if (user.getId() != null && getDao().existsByIdCardAndIdNot(value, user.getId())) {
          errors.add(errorMsg(row, "身份证已存在"));
        }
        else{
          user.setIdCard(value);
        }
      }
    });
  }

}
