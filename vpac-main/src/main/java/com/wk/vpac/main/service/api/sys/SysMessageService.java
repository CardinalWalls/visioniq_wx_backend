package com.wk.vpac.main.service.api.sys;

import com.base.components.common.constants.Valid;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.wk.vpac.common.constants.KnowStatus;
import com.wk.vpac.database.dao.sys.SysMessageDao;
import com.wk.vpac.domain.sys.SysMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Optional;

/**
 * 系统消息service
 *
 * @author <a href="tecyun@foxmail.com">Huangyunyang</a>
 * @version 1.0.0, 2018/3/20 0020 15:23
 */
@Service
public class SysMessageService extends AbstractJpaService<SysMessage, String, SysMessageDao> {

  public int countUnReadMessage(String userId) {
    ConditionGroup<SysMessage> condition = ConditionGroup.build()
                                                         .addCondition("userId", ConditionEnum.OPERATE_EQUAL, userId)
                                                         .addCondition("status", ConditionEnum.OPERATE_EQUAL,
                                                                       KnowStatus.UNREAD.getStatus()
                                                         ).addCondition("delFlag", ConditionEnum.OPERATE_EQUAL,
                                                                        Valid.TRUE.getVal());

    return (int) getDao().count(condition);
  }

  @Transactional(rollbackFor = Exception.class)
  public void updateReadStatus(String msgId) {
    Optional<SysMessage> messageOptional = getDao().findById(msgId);
    Assert.isTrue(messageOptional.isPresent(), "message is not resent");
    SysMessage sysMessage = messageOptional.get();
    sysMessage.setStatus(Valid.TRUE.getVal());
    sysMessage.setKnowTime(new Date());
    getDao().saveAndFlush(sysMessage);
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteMessage(String id) {
    getDao().deleteById(id);
  }

  @Transactional(rollbackFor = Exception.class)
  public void readAllMessage(String userId) {
    getDao().updateMessageReadStatus(userId);
  }
}
