package com.wk.vpac.main.service.admin.user;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.exception.Exceptions;
import com.base.components.common.token.TokenCacheObj;
import com.base.components.common.token.TokenManager;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.PasswordUtil;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wk.vpac.common.constants.UserType;
import com.wk.vpac.common.constants.sys.Gender;
import com.wk.vpac.common.token.user.ExpertToken;
import com.wk.vpac.database.dao.sys.RegionDao;
import com.wk.vpac.database.dao.user.BaseUserTypeDao;
import com.wk.vpac.database.dao.user.ExpertDao;
import com.wk.vpac.database.dao.user.UserBaseInfoDao;
import com.wk.vpac.domain.sys.Region;
import com.wk.vpac.domain.user.Expert;
import com.wk.vpac.domain.user.UserBaseInfo;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import com.wk.vpac.main.service.api.sys.SysMd5JsonService;
import com.wk.vpac.main.service.api.user.WeChatService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Expert Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class ExpertService extends AbstractJpaService<Expert, String, ExpertDao> {
  private final AttachmentService attachmentService;
  private final UserBaseInfoDao userBaseInfoDao;
  private final BaseUserTypeDao baseUserTypeDao;
  private final RegionDao regionDao;
  private final TokenManager tokenManager;
  private final WeChatService weChatService;
  private final SysMd5JsonService sysMd5JsonService;

  public void incrementViewCount(String id, int addCount){
    TransactionActivation.start(()->{
      getDao().incrementViewCount(id, addCount);
      return null;
    });
  }

  public int findBindCount(String id){
    return getDao().countBind(id);
  }

  public DataPage<Map<String, Object>> page(Map<String, String> params){
    return getDao().page(params);
  }

  @Transactional(rollbackFor = Exception.class)
  public Expert apply(Map<String, String> params){
    TokenCacheObj token = TokenThreadLocal.getTokenObjNonNull();
    UserBaseInfo user = userBaseInfoDao.findById(token.objId().toString())
                                       .orElseThrow(Exceptions.of(() -> "未找到账户信息"));
    Assert.isTrue(user.getUserType() == UserType.USER.getCode(), ()->"当前账号类型不能申请为专家");
    Expert expert = getDao().findByUserId(user.getId());
    Assert.isNull(expert, ()->"当前账号已提交申请，请勿重复操作");
    expert = new Expert();
    String name = ConvertUtil.checkNotNull(params.get("name"), "请输入真实姓名", String.class);
    String avatar = attachmentService.persistent(params.get("avatar"));
    Assert.hasText(avatar, ()->"请上传形象照片");
    ArrayNode workCard = attachmentService.persistentArray(params.get("workCard"));
    Assert.isTrue(!workCard.isEmpty(), ()->"请上传工作证件");
    String regionId = ConvertUtil.checkNotNull(params.get("regionId"), "请选择所在地区", String.class);
    Region region = regionDao.findById(regionId).orElseThrow(Exceptions.of(() -> "未找到地区数据"));
    expert.setRegionId(region.getId());
    expert.setRegionName(region.getNamePath());
    expert.setHospital(ConvertUtil.checkNotNull(params.get("hospital"), "请填写所在医院或机构", String.class));
    expert.setDepartment(ConvertUtil.checkNotNull(params.get("department"), "请填写所在科室或部门", String.class));
    expert.setJobPosition(StringUtils.trimToEmpty(params.get("jobPosition")));
    expert.setTitle(StringUtils.trimToEmpty(params.get("title")));
    expert.setProfile(StringUtils.trimToEmpty(params.get("profile")));
    expert.setLevel(StringUtils.trimToEmpty(params.get("level")));
    expert.setProficient(StringUtils.trimToEmpty(params.get("proficient")));
    expert.setCreateTime(new Date());
    expert.setUpdateTime(expert.getCreateTime());
    expert.setUserId(user.getId());
    expert.setStatus(-1);
    expert.setWorkCard(workCard.toString());
    user.setRealName(name);
    user.setGender(ConvertUtil.convert(params.get("gender"), 1) == 1 ? 1 : 2);
    user.setUpdateTime(expert.getCreateTime());
    user.setAvatar(avatar);
    userBaseInfoDao.save(user);
    return saveAndFlush(expert);
  }

  @Transactional(rollbackFor = Exception.class)
  public Expert audit(String id){
    Expert expert = findById(id);
    Assert.notNull(expert, ()->"未找到专家数据");
    Assert.isTrue(expert.getStatus() == -1, ()->"此专家信息已审核，请勿重复操作");
    UserBaseInfo user = userBaseInfoDao.findById(expert.getUserId())
                                       .orElseThrow(Exceptions.of(() -> "未找到申请的账户信息"));
    expert.setStatus(1);
    expert.setUpdateTime(new Date());
    user.setUpdateTime(expert.getUpdateTime());
    user.setUserType(UserType.EXPERT.getCode());
    user.setUserTypeId(baseUserTypeDao.findIdByUserType(UserType.EXPERT.getCode()));
    userBaseInfoDao.save(user);
    tokenManager.cleanTokenWithObjId(user.getId());
    return saveAndFlush(expert);
  }

  @Transactional(rollbackFor = Exception.class)
  public String qrCode(String id){
    Expert expert = findById(id);
    Assert.notNull(expert, ()->"未找到专家数据");
    TreeMap md5Param = new TreeMap();
    md5Param.put("appId", weChatService.getMaAppId());
    md5Param.put("expertId", id);
    md5Param.put("userid", expert.getUserId());
    String md5 = sysMd5JsonService.toMd5(md5Param);
    Map<String, String> param = Maps.newHashMap();
    param.put("qrcode_name", "myuser_" + expert.getUserId());
    param.put("scene", md5);
    param.put("page", "pages/login/login");
    param.put("width", "280");
    String uri = weChatService.getWxacodeunlimitWithPath(param);
    if(StringUtils.isNotBlank(uri)){
      expert.setQrCode(uri.startsWith("/") ? uri : ("/" + uri));
      save(expert);
    }
    return uri;
  }

  public Expert save(Map<String, String> params){
    String id = ConvertUtil.convert(params.get("id"), "");
    String name = params.get("name");
    String regionId = params.get("regionId");
    UserBaseInfo user;
    Expert expert;
    Date now = new Date();
    String changeBeforeUserId = null;
    if(StringUtils.isBlank(id)){
      ConvertUtil.checkNotNull(name, "请输入专家姓名", String.class);
      ConvertUtil.checkNotNull(regionId, "请选择所在地区", String.class);
      String phone = ConvertUtil.checkNotNull(params.get("phone"), "请输入专家手机号", String.class);
      expert = ConvertUtil.populate(new Expert(), params, false, "id");
      expert.setCreateTime(now);
      user = addExpertUser(phone, now);
      expert.setUserId(user.getId());
    }else{
      params.remove("regionId");
      Expert exists = getDao().findById(id).orElseThrow(()->new IllegalArgumentException("未找到数据"));
      expert = ConvertUtil.populate(exists, params, false, "id", "userId");
      String userId = params.get("userId");
      //更换用户ID
      if(StringUtils.isNotBlank(userId) && !userId.equals(exists.getUserId())){
        if (getDao().findByUserId(userId) != null) {
          throw new IllegalArgumentException("此用户信息已被其它专家绑定");
        }
        changeBeforeUserId = exists.getUserId();
        exists.setUserId(userId);
      }
      user = userBaseInfoDao.findById(exists.getUserId())
                            .orElseThrow(() -> new IllegalArgumentException("未找到专家用户信息"));
      if(user.getUserType() != UserType.EXPERT.getCode()){
        user.setUserType(UserType.EXPERT.getCode());
        user.setUserTypeId(baseUserTypeDao.findIdByUserType(UserType.EXPERT.getCode()));
        tokenManager.cleanTokenWithObjId(user.getId());
      }
    }
    if(StringUtils.isNotBlank(regionId)){
      Region region = regionDao.findById(regionId).orElseThrow(Exceptions.of(() -> "未找到地区数据"));
      expert.setRegionId(region.getId());
      expert.setRegionName(region.getNamePath());
    }
    ArrayNode workCard = attachmentService.persistentArray(params.get("workCard"));
    expert.setWorkCard(workCard.toString());
    expert.setUpdateTime(now);
    String avatar = attachmentService.persistent(params.get("avatar"));
    ConvertUtil.setPropertyIfNotBlank(user, "avatar", avatar);
    ConvertUtil.setPropertyIfNotBlank(user, "realName", name);
    Gender gender = Gender.parseCode(ConvertUtil.convert(params.get("gender"), -1));
    if(gender != null){
      user.setGender(gender.getCode());
    }
    user.setUpdateTime(now);
    UserBaseInfo finalUser = user;
    String finalChangeBeforeUserId = changeBeforeUserId;
    return TransactionActivation.of(()->{
      userBaseInfoDao.save(finalUser);
      if(finalChangeBeforeUserId != null){
        userBaseInfoDao.updateUserType(finalChangeBeforeUserId, UserType.USER.getCode(),
                                       baseUserTypeDao.findIdByUserType(UserType.USER.getCode()));
        tokenManager.cleanTokenWithObjId(finalChangeBeforeUserId);
      }
      return saveAndFlush(expert);
    }).start();
  }

  public UserBaseInfo addExpertUser(String phone, Date now){
    UserBaseInfo user = userBaseInfoDao.findByPhone(phone);
    if(user == null){
      user = new UserBaseInfo();
      user.setRegistTime(now);
      user.setAccount(phone);
      user.setPhone(phone);
      user.setPwd(PasswordUtil.encryptPassword(phone, phone.substring(phone.length() - 6)));
      user.setUpdateTime(now);
      user.setRegistIp("127.0.0.1");
      userBaseInfoDao.save(user);
    }
    user.setUserTypeId(baseUserTypeDao.findIdByUserType(UserType.EXPERT.getCode()));
    user.setUserType(UserType.EXPERT.getCode());
    return user;
  }

//  @SuppressWarnings("all")
//  public Map<String, Object> statistics(ExpertToken expert){
//    DateTime today = DateTime.now().secondOfDay().withMinimumValue();
//    List<Map<String, Object>> list = setMapResult(getEntityManager().createNativeQuery("""
//       SELECT * FROM
//       (SELECT SUM(CASE WHEN bind_expert_time >= :todayBegin THEN 1 ELSE 0 END) todayUser, COUNT(1) totalUser
//         FROM base_user_base_info WHERE expert_id = :expertId) t1,
//       (SELECT SUM(CASE WHEN target_time >= :today THEN 1 ELSE 0 END) todoAppointment, COUNT(1) totalAppointment
//         FROM wk_appointment_expert WHERE expert_id = :expertId) t2,
//       (SELECT SUM(CASE WHEN unread > 0 THEN 1 ELSE 0 END) unreadChatGroup, COUNT(1) totalChatGroup
//         FROM wk_chat_message_group_user WHERE user_id = :userId AND visible = true) t3""")
//                                                                    .setParameter("todayBegin", today.toDate())
//                                                                    .setParameter("today", today.toString(Dates.DATE_FORMATTER))
//                                                                    .setParameter("expertId", expert.getExpertId())
//                                                                    .setParameter("userId", expert.getUserId())).getResultList();
//    return list.isEmpty() ? Collections.emptyMap() : list.get(0);
//  }

  public Map<String, Object> expertStatistics(ExpertToken expert){
    List<RowMap> rows = toRowResult(getEntityManager().createNativeQuery("""
    SELECT SUM(unread) totalUnread FROM wk_chat_message_group_user gu 
    INNER JOIN wk_chat_message_group g ON g.id = gu.group_id
    INNER JOIN wk_user_archive a ON a.id = g.user_archive_id
    WHERE gu.user_id = :userId AND a.expert_id = :expertId
    """).setParameter("userId", expert.getUserId()))
      .setParameter("expertId", expert.getExpertId()).getResultList();
    return rows.isEmpty() ? Collections.emptyMap() : rows.get(0);
  }

  public Map<String, Object> operatorStatistics(){
    List<RowMap> rows = toRowResult(getEntityManager().createNativeQuery("""
    SELECT SUM(gu.unread) totalUnread FROM wk_chat_message_group_user gu INNER JOIN wk_expert e ON e.user_id = gu.user_id
    INNER JOIN wk_chat_message_group g ON g.id = gu.group_id
    INNER JOIN wk_user_archive a ON a.id = g.user_archive_id AND a.expert_id = e.id
    """)).getResultList();
    return rows.isEmpty() ? Collections.emptyMap() : rows.get(0);
  }

  public void updateAppointmentLimit(ExpertToken expertToken, String limits){
    Expert expert = findById(expertToken.getExpertId());
    Assert.notNull(expert, ()->"未找到专家信息");
    expert.setAppointmentWeekLimit(weekDays(limits));
    TransactionActivation.start(()->save(expert));
  }

  private static String weekDays(String src){
    if(StringUtils.isNotBlank(src)){
      Set<String> days = Sets.newTreeSet();
      for (int i = 0; i < src.length(); i++) {
        String s = src.substring(i, i+1);
        Integer day = ConvertUtil.convert(s, 0);
        if(day >= DateTimeConstants.MONDAY && day <= DateTimeConstants.SUNDAY){
          days.add(day.toString());
        }
      }
      return StringUtils.join(days, "");
    }
    return "";
  }

  public ExpertService(AttachmentService attachmentService, UserBaseInfoDao userBaseInfoDao,
                       BaseUserTypeDao baseUserTypeDao, RegionDao regionDao, TokenManager tokenManager,
                       WeChatService weChatService, SysMd5JsonService sysMd5JsonService) {
    this.attachmentService = attachmentService;
    this.userBaseInfoDao = userBaseInfoDao;
    this.baseUserTypeDao = baseUserTypeDao;
    this.regionDao = regionDao;
    this.tokenManager = tokenManager;
    this.weChatService = weChatService;
    this.sysMd5JsonService = sysMd5JsonService;
  }
}