package com.wk.vpac.main.service.admin.user;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.wk.vpac.database.dao.user.HealthyFormDao;
import com.wk.vpac.domain.user.HealthyForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * HealthyForm Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class HealthyFormService extends AbstractJpaService<HealthyForm, String, HealthyFormDao> {

  public DataPage<Map<String, Object>> page(Map<String, String> params){
    return getDao().page(params);
  }

  public HealthyForm save(Map<String, String> params){
    String id = ConvertUtil.convert(params.get("id"), "");
    HealthyForm healthyForm;
    Date now = new Date();
    if(StringUtils.isBlank(id)){
      healthyForm = ConvertUtil.populate(new HealthyForm(), params, false, "id");
      healthyForm.setCreateTime(now);
    }else{
      HealthyForm exists = getDao().findById(id).orElseThrow(()->new IllegalArgumentException("未找到数据"));
      healthyForm = ConvertUtil.populate(exists, params, false, "id");
    }
    healthyForm.setUpdateTime(now);
    return TransactionActivation.of(()->saveAndFlush(healthyForm)).start();
  }

}