package com.wk.vpac.main.service.admin.user;

import com.wk.vpac.domain.user.UserVisualFunctionReport;
import com.wk.vpac.database.dao.user.UserVisualFunctionReportDao;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.exception.Exceptions;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * UserVisualFunctionReport Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class UserVisualFunctionReportService extends AbstractJpaService<UserVisualFunctionReport, String, UserVisualFunctionReportDao> {

  public DataPage<RowMap> page(PageParamMap params){
    return getDao().page(params);
  }

  public UserVisualFunctionReport save(ParamMap params){
    String id = params.getStrTrimOrEmpty("id");
    UserVisualFunctionReport userVisualFunctionReport;
    if(StringUtils.isBlank(id)){
      userVisualFunctionReport = params.populate(new UserVisualFunctionReport(), false, "id");
    }else{
      UserVisualFunctionReport exists = getDao().findById(id).orElseThrow(Exceptions.of(()->"未找到数据"));
      userVisualFunctionReport = params.populate(exists, false, "id");
    }
    return TransactionActivation.of(()->saveAndFlush(userVisualFunctionReport)).start();
  }

}