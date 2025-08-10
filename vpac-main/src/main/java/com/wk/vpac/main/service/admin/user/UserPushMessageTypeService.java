package com.wk.vpac.main.service.admin.user;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.exception.Exceptions;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.wk.vpac.database.dao.user.UserPushMessageDao;
import com.wk.vpac.database.dao.user.UserPushMessageTypeDao;
import com.wk.vpac.domain.user.UserPushMessageType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


/**
 * UserPushMessageType Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class UserPushMessageTypeService extends AbstractJpaService<UserPushMessageType, String, UserPushMessageTypeDao> {
  private final UserPushMessageDao userPushMessageDao;

  public UserPushMessageTypeService(UserPushMessageDao userPushMessageDao) {
    this.userPushMessageDao = userPushMessageDao;
  }


  public DataPage<RowMap> page(PageParamMap params){
    return getDao().page(params);
  }

  public UserPushMessageType save(ParamMap params){
    String id = params.getStrTrimOrEmpty("id");
    String name = params.hasText("name", () -> "请填写分类名称").trim();
    UserPushMessageType userPushMessageType;
    if(StringUtils.isBlank(id)){
      if (count(ConditionGroup.build().addCondition("name", ConditionEnum.OPERATE_EQUAL, name)) > 0) {
        throw Exceptions.of("该分类名称已存在");
      }
      userPushMessageType = params.populate(new UserPushMessageType(), false, "id");
    }else{
      if (count(ConditionGroup.build().addCondition("name", ConditionEnum.OPERATE_EQUAL, name)
                              .addCondition("id", ConditionEnum.OPERATE_UNEQUAL, id)) > 0) {
        throw Exceptions.of("该分类名称已存在");
      }
      UserPushMessageType exists = getDao().findById(id).orElseThrow(Exceptions.of(()->"未找到数据"));
      userPushMessageType = params.populate(exists, false, "id");
    }
    return TransactionActivation.of(()->saveAndFlush(userPushMessageType)).start();
  }

  @Transactional(rollbackFor = Exception.class)
  public void delete(Collection<String> ids){
    for (String id : ids) {
      if (userPushMessageDao.existsByTypeId(id)) {
        throw Exceptions.of("该类型下已有推送消息，不能删除");
      }
      deleteById(id);
    }
  }
}