package com.wk.vpac.main.service.admin.sys;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.exception.Exceptions;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.wk.vpac.database.dao.sys.SysAutoReplyDao;
import com.wk.vpac.domain.sys.SysAutoReply;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * SysAutoReply Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class SysAutoReplyService extends AbstractJpaService<SysAutoReply, String, SysAutoReplyDao> {

  public DataPage<RowMap> page(PageParamMap params){
    return getDao().page(params);
  }

  public SysAutoReply save(ParamMap params){
    String id = params.getStrTrimOrEmpty("id");
//    params.hasText("keyword", ()->"请填写关键词");
    params.hasText("content", ()->"请填写回复内容");
    Date now = new Date();
    SysAutoReply sysAutoReply;
    if(StringUtils.isBlank(id)){
      sysAutoReply = params.populate(new SysAutoReply(), false, "id");
      sysAutoReply.setCreateTime(now);
    }else{
      SysAutoReply exists = getDao().findById(id).orElseThrow(Exceptions.of(()->"未找到数据"));
      sysAutoReply = params.populate(exists, false, "id");
    }
    sysAutoReply.setUpdateTime(now);
    return TransactionActivation.of(()->saveAndFlush(sysAutoReply)).start();
  }

}