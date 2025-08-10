package com.wk.vpac.main.service.admin.user;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.exception.Exceptions;
import com.base.components.common.util.ValidatorUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.wk.vpac.database.dao.sys.RegionDao;
import com.wk.vpac.database.dao.user.UserArchiveDao;
import com.wk.vpac.domain.sys.Region;
import com.wk.vpac.domain.user.UserArchive;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;


/**
 * UserArchive Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class UserArchiveService extends AbstractJpaService<UserArchive, String, UserArchiveDao> {
  private final RegionDao regionDao;

  public UserArchiveService(RegionDao regionDao) {
    this.regionDao = regionDao;
  }

  public DataPage<RowMap> page(PageParamMap params){
    params.put("admin", true);
    return getDao().page(params);
  }

  public UserArchive save(ParamMap params){
    String id = params.hasText("id", ()->"请选择一个档案数据");
    UserArchive exists = getDao().findById(id).orElseThrow(Exceptions.of(()->"未找到数据"));
    Assert.isTrue(ValidatorUtil.isIDCard(params.getStr("idcard")), ()->"请填写身份证");
    Assert.isTrue(!getDao().exists(ConditionGroup.build().addCondition("idcard", ConditionEnum.OPERATE_EQUAL, params.getStr("idcard"))
                                                 .addCondition("userId", ConditionEnum.OPERATE_EQUAL, exists.getUserId())
                                                 .addCondition("id", ConditionEnum.OPERATE_UNEQUAL, id)),
                  ()->"此身份证号您已经添加，请勿重复操作");
    Region region = regionDao.findById(params.hasText("regionId", () -> "请选择所在地区"))
                             .orElseThrow(Exceptions.of(() -> "未找到地区数据"));
    exists.setName(params.hasText("name", ()->"请填写姓名"));
    exists.setGender(params.notNull("gender", Integer.class, ()->"请选择性别"));
    exists.setBirth(params.dateTimeNotNull("birth", Dates.DATE_FORMATTER_PATTERN, e->"出生日期 " + e).toString(Dates.DATE_FORMATTER));
    exists.setIdcard(params.getStr("idcard"));
    exists.setParentsMyopia(params.get("parentsMyopia", 0));
    exists.setRegionId(region.getId());
    exists.setRegionName(region.getNamePath());
    exists.setRiskLevel(params.get("riskLevel", 0));
    exists.setUpdateTime(new Date());
    String expertId = params.getStrTrimOrEmpty("expertId");
    if (StringUtils.isNotBlank(expertId)) {
      exists.setExpertId(expertId);
    }
    return TransactionActivation.of(()->saveAndFlush(exists)).start();
  }

}