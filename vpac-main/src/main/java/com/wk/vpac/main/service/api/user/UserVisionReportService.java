package com.wk.vpac.main.service.api.user;

import com.wk.vpac.domain.user.UserVisionReport;
import com.wk.vpac.database.dao.user.UserVisionReportDao;
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
 * UserVisionReport Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class UserVisionReportService extends AbstractJpaService<UserVisionReport, String, UserVisionReportDao> {

  public DataPage<RowMap> page(PageParamMap params){
    return getDao().page(params);
  }

  public UserVisionReport save(ParamMap params){
    String id = params.getStrTrimOrEmpty("id");
    UserVisionReport userVisionReport;
    if(StringUtils.isBlank(id)){
      userVisionReport = params.populate(new UserVisionReport(), false, "id");
    }else{
      UserVisionReport exists = getDao().findById(id).orElseThrow(Exceptions.of(()->"未找到数据"));
      userVisionReport = params.populate(exists, false, "id");
    }
    return TransactionActivation.of(()->saveAndFlush(userVisionReport)).start();
  }

}