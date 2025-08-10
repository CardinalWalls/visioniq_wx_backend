package com.wk.vpac.main.service.admin.user;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.wk.vpac.database.dao.user.AppointmentExpertDao;
import com.wk.vpac.domain.user.AppointmentExpert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * AppointmentExpert Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class AppointmentExpertService extends AbstractJpaService<AppointmentExpert, String, AppointmentExpertDao> {

  public DataPage<Map<String, Object>> page(Map<String, String> params){
    return getDao().page(params);
  }

  public AppointmentExpert save(Map<String, String> params){
    String id = ConvertUtil.convert(params.get("id"), "");
    AppointmentExpert appointmentExpert;
    if(StringUtils.isBlank(id)){
      appointmentExpert = ConvertUtil.populate(new AppointmentExpert(), params, false, "id");
    }else{
      AppointmentExpert exists = getDao().findById(id).orElseThrow(()->new IllegalArgumentException("未找到数据"));
      appointmentExpert = ConvertUtil.populate(exists, params, false, "id");
    }
    return TransactionActivation.of(()->saveAndFlush(appointmentExpert)).start();
  }

}