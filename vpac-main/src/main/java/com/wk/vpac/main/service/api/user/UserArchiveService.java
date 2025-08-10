package com.wk.vpac.main.service.api.user;

import com.base.components.common.boot.secret.SecretHelper;
import com.base.components.common.constants.sys.Dates;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.exception.Exceptions;
import com.base.components.common.token.TokenCacheObj;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.Logs;
import com.base.components.common.util.ValidatorUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.wk.vpac.common.constants.sys.Gender;
import com.wk.vpac.common.token.user.ExpertToken;
import com.wk.vpac.common.token.user.OperatorToken;
import com.wk.vpac.common.token.user.UserToken;
import com.wk.vpac.database.dao.chat.ChatMessageGroupDao;
import com.wk.vpac.database.dao.sys.RegionDao;
import com.wk.vpac.database.dao.user.AppointmentExpertDao;
import com.wk.vpac.database.dao.user.ExpertDao;
import com.wk.vpac.database.dao.user.UserArchiveDao;
import com.wk.vpac.database.dao.user.UserInspectReportDao;
import com.wk.vpac.database.dao.user.UserVisionReportDao;
import com.wk.vpac.database.dao.user.UserVisualFunctionReportDao;
import com.wk.vpac.domain.sys.Region;
import com.wk.vpac.domain.user.UserArchive;
import com.wk.vpac.domain.user.UserBaseInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * UserArchive Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class UserArchiveService extends AbstractJpaService<UserArchive, String, UserArchiveDao> {
  private final AppointmentExpertDao appointmentExpertDao;
  private final ChatMessageGroupDao chatMessageGroupDao;
  private final UserInspectReportDao userInspectReportDao;
  private final UserVisionReportDao userVisionReportDao;
  private final UserVisualFunctionReportDao userVisualFunctionReportDao;
  private final ExpertDao expertDao;
  private final RegionDao regionDao;
  private final UserBaseInfoService userBaseInfoService;

  public UserArchiveService(AppointmentExpertDao appointmentExpertDao, ChatMessageGroupDao chatMessageGroupDao,
                            UserInspectReportDao userInspectReportDao, UserVisionReportDao userVisionReportDao,
                            UserVisualFunctionReportDao userVisualFunctionReportDao,
                            ExpertDao expertDao, RegionDao regionDao, UserBaseInfoService userBaseInfoService) {
    this.appointmentExpertDao = appointmentExpertDao;
    this.chatMessageGroupDao = chatMessageGroupDao;
    this.userInspectReportDao = userInspectReportDao;
    this.userVisionReportDao = userVisionReportDao;
    this.userVisualFunctionReportDao = userVisualFunctionReportDao;
    this.expertDao = expertDao;
    this.regionDao = regionDao;
    this.userBaseInfoService = userBaseInfoService;
  }

//  public UserArchive findByUserIdAndIdcard(String userId, String idcard){
//    return getDao().findByUserIdAndIdcard(userId, idcard);
//  }
//
//  public UserArchive findByExpertIdAndIdcard(String expertId, String idcard){
//    return getDao().findByExpertIdAndIdcard(expertId, idcard);
//  }
  public void checkArchive(String userArchiveId){
    TokenCacheObj token = TokenThreadLocal.getTokenObjNonNull();
    if(token instanceof OperatorToken){
      return;
    }
    if(token instanceof ExpertToken e){
      Assert.isTrue(getDao().existsByExpertIdAndId(e.getExpertId(), userArchiveId), ()->"无权限操作");
    }
    else{
      Assert.isTrue(getDao().existsByUserIdAndId(token.objId().toString(), userArchiveId), ()->"无权限操作");
    }
  }
  public UserArchive findArchive(TokenCacheObj token, String idcard){
    UserArchive userArchive;
    if(token instanceof ExpertToken e){
      List<UserArchive> list = getDao().findByExpertIdAndIdcardOrderByIdDesc(e.getExpertId(), idcard);
      userArchive = list.isEmpty() ? null : list.getFirst();
    }
    else{
      List<UserArchive> list = getDao().findByUserIdAndIdcardOrderByIdDesc(token.objId().toString(), idcard);
      userArchive = list.isEmpty() ? null : list.getFirst();
    }
    Assert.notNull(userArchive, ()->"无权限操作");
    return userArchive;
  }
  public RowMap findRowMapById(String id){
    return getDao().findRowMapById(id);
  }
  public DataPage<RowMap> page(PageParamMap params){
    TokenCacheObj tokenObj = TokenThreadLocal.getTokenObjNonNull();
    if(!params.containsKey("id")){
      if(tokenObj instanceof ExpertToken e){
        params.put("expertId", e.getExpertId());
      }else{
        params.put("userId", tokenObj.objId().toString());
      }
    }
    params.put("currentUserId", tokenObj.objId().toString());
    params.put("admin", false);
    return getDao().page(params);
  }
  public DataPage<RowMap> pageForOperator(PageParamMap params){
    TokenThreadLocal.getTokenObjNonNull(OperatorToken.class);
    return getDao().pageForOperator(params);
  }

  @Transactional(rollbackFor = Exception.class)
  public UserArchive save(ParamMap params){
    params.dateNotNull("birth", Dates.DATE_FORMATTER_PATTERN, e->"出生日期" + e);
    Assert.isTrue(ValidatorUtil.isIDCard(params.getStr("idcard")), ()->"请填写身份证");
    Region region = regionDao.findById(params.hasText("regionId", () -> "请选择所在地区"))
                             .orElseThrow(Exceptions.of(() -> "未找到地区数据"));
    String id = params.getStrTrimOrEmpty("id");
    Date now = new Date();
    UserArchive userArchive;
    if(StringUtils.isBlank(id)){
      TokenCacheObj token = TokenThreadLocal.getTokenObjNonNull();
      userArchive = params.populate(new UserArchive(), false, "id");
      String userId;
      if(token instanceof ExpertToken e){
        String phone = params.hasText("phone", () -> "请正确输入此档案所属账号的手机号码");
        Assert.isTrue(ValidatorUtil.isPhoneNum(phone), () -> "所属账号的手机号码，格式错误");
        userId = userBaseInfoService.findOrCreateUserByPhone(phone);
        userArchive.setExpertId(e.getExpertId());
        UserBaseInfo user = userBaseInfoService.findById(userId);
        if(user != null && StringUtils.isBlank(user.getExpertId())){
          user.setExpertId(userArchive.getExpertId());
          userBaseInfoService.save(user);
        }
      }
      else{
        userId = token.objId().toString();
        if(UserToken.class.equals(token.getClass())){
          UserBaseInfo user = userBaseInfoService.findById(userId);
          if(user != null){
            if(StringUtils.isNotBlank(user.getExpertId())){
              userArchive.setExpertId(user.getExpertId());
            }else{
              userArchive.setExpertId(expertDao.matching(region.getId()));
              user.setExpertId(userArchive.getExpertId());
              userBaseInfoService.save(user);
            }
          }
        }
        if (StringUtils.isBlank(userArchive.getExpertId())) {
          userArchive.setExpertId(expertDao.matching(region.getId()));
        }
      }
      Assert.isTrue(!getDao().exists(ConditionGroup.build().addCondition("idcard", ConditionEnum.OPERATE_EQUAL, params.getStr("idcard"))
                                                  .addCondition("userId", ConditionEnum.OPERATE_EQUAL, userId)),
                    ()->"此身份证号您已经添加，请勿重复操作");
      userArchive.setCreateTime(now);
      userArchive.setUserId(userId);
    }else{
      checkArchive(id);
      params.removes("expertId", "userId");
      UserArchive exists = getDao().findById(id).orElseThrow(Exceptions.of(()->"未找到数据"));
      Assert.isTrue(!getDao().exists(ConditionGroup.build().addCondition("idcard", ConditionEnum.OPERATE_EQUAL, params.getStr("idcard"))
                                                   .addCondition("userId", ConditionEnum.OPERATE_EQUAL, exists.getUserId())
                                                   .addCondition("id", ConditionEnum.OPERATE_UNEQUAL, id)),
                    ()->"此身份证号您已经添加，请勿重复操作");
      userArchive = params.populate(exists, false, "id");
    }
    userArchive.setUpdateTime(now);
    userArchive.setGender(userArchive.getGender() == Gender.male.getCode() ? Gender.male.getCode() : Gender.female.getCode());
    userArchive.setRegionId(region.getId());
    userArchive.setRegionName(region.getNamePath());
    userArchive.setRiskLevel(params.get("riskLevel", 0));
    return saveAndFlush(userArchive);
  }

  @Transactional(rollbackFor = Exception.class)
  public void delete(Set<String> ids){
    if(!CollectionUtils.isEmpty(ids)){
      List<UserArchive> list = findAllById(ids);
      TokenCacheObj token = TokenThreadLocal.getTokenObjNonNull();
      String tokenStr = ToStringBuilder.reflectionToString(TokenThreadLocal.getTokenObjNonNull(), ToStringStyle.JSON_STYLE);
      for (UserArchive archive : list) {
        if(token instanceof ExpertToken e){
          Assert.isTrue(e.getExpertId().equals(archive.getExpertId()), ()->"无权限操作");
        }
        else{
          Assert.isTrue(archive.getUserId().equals(token.objId()), ()->"无权限操作");
        }
        Logs.get().info("删除档案：{id: {}, userId:{}, name:{}}; 操作账户：{}",
                        archive.getId(), archive.getUserId(), archive.getName(), tokenStr);
        deleteById(archive.getId());
        appointmentExpertDao.deleteByUserArchiveId(archive.getId());
        chatMessageGroupDao.deleteGroup(archive.getId());
        userVisionReportDao.deleteByUserArchiveId(archive.getId());
        userVisualFunctionReportDao.deleteByUserArchiveId(archive.getId());
      }
    }
  }

  public RowMap statistics(ParamMap params){
    TokenCacheObj token = TokenThreadLocal.getTokenObjNonNull();
    if(token instanceof ExpertToken e){
      params.put("expertId", e.getExpertId());
    }else if(UserToken.class.equals(token.getClass())){
      params.put("userId", token.objId().toString());
    }
    return getDao().statistics(params);
  }

  public static void main(String[] args) {
    System.out.println(SecretHelper.decrypt("2ab53f72047db171026e04899db4b05d4a4e2ee5bd22a9dbe307ba2c61fb92a572e39a5471bb979aa6ebca8420e2c3bb", null, null));
  }
}