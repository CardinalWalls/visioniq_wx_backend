package com.wk.vpac.main.service.api.user;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.wk.vpac.common.token.user.UserToken;
import com.wk.vpac.database.dao.user.AppointmentExpertDao;
import com.wk.vpac.database.dao.user.ExpertDao;
import com.wk.vpac.domain.user.AppointmentExpert;
import com.wk.vpac.domain.user.Expert;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * AppointmentExpert Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class AppointmentExpertService extends AbstractJpaService<AppointmentExpert, String, AppointmentExpertDao> {
  private final ExpertDao expertDao;
  private final UserArchiveService userArchiveService;

  public DataPage<Map<String, Object>> page(Map<String, String> params){
    return getDao().page(params);
  }

  public AppointmentExpert save(String expertId, String userArchiveId, String targetTime){
    userArchiveService.checkArchive(userArchiveId);
    Assert.hasText(expertId, ()->"请选择需要预约的专家");
    DateTime date = ConvertUtil.dateNonNull(targetTime, "预约时间", Dates.DATE_FORMATTER).secondOfDay().withMaximumValue();
    if(date.isBeforeNow()){
      throw new IllegalArgumentException("不能预约今天之前的时间");
    }
    Expert expert = expertDao.findById(expertId).orElseThrow(() -> new IllegalArgumentException("未找到专家信息"));
    if (StringUtils.isNotBlank(expert.getAppointmentWeekLimit())) {
      if (expert.getAppointmentWeekLimit().contains(String.valueOf(date.getDayOfWeek()))) {
        throw new IllegalArgumentException("专家未开启预约，预约日期：" + targetTime);
      }
    }
    if (getDao().existsByUserArchiveIdAndExpertIdAndTargetTime(userArchiveId, expertId, targetTime)) {
      throw new IllegalArgumentException("您已预约成功，请勿重复操作");
    }

    AppointmentExpert appointment = new AppointmentExpert();
    appointment.setCreateTime(new Date());
    appointment.setUserArchiveId(userArchiveId);
    appointment.setExpertId(expertId);
    appointment.setTargetTime(targetTime);
    return TransactionActivation.of(()->saveAndFlush(appointment)).start();
  }

  public int delete(Collection<String> ids){
    if(!ids.isEmpty()){
      String userId = TokenThreadLocal.getTokenObjNonNull(UserToken.class).getUserId();
      return TransactionActivation.of(()->getDao().deleteByUserIdAndIdIn(userId, ids)).start();
    }
    return 0;
  }

  public AppointmentExpertService(ExpertDao expertDao, UserArchiveService userArchiveService) {
    this.expertDao = expertDao;
    this.userArchiveService = userArchiveService;
  }
}