package com.wk.vpac.main.service.admin.sys;

import com.base.components.common.constants.msgqueue.Channel;
import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.service.message.ChannelMessageSender;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.wk.vpac.database.dao.msgqueue.SysMessageEventDao;
import com.wk.vpac.database.dao.msgqueue.SysMessageEventErrorDao;
import com.wk.vpac.database.dao.sys.SysMessageEventHandleDao;
import com.wk.vpac.domain.msgqueue.SysMessageEvent;
import com.wk.vpac.domain.msgqueue.SysMessageEventError;
import com.wk.vpac.domain.msgqueue.SysMessageEventHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * MessageEventService
 *
 * @author <a href="lijian_jie@163.com">LiJian</a>
 * @version 1.0.0, 2018-05-23 14:45
 */
@Service
public class MessageEventService extends AbstractJpaService<SysMessageEvent, String, SysMessageEventDao>{
  @Autowired
  private SysMessageEventHandleDao sysMessageEventHandleDao;
  @Autowired
  private SysMessageEventErrorDao eventErrorDao;
  @Autowired
  private ChannelMessageSender channelMessageSender;

  public DataPage queryMessageEvent(Map<String, String> params) {
    return getDao().page(params);
  }

  public SysMessageEventError getErrorInfo(String messageId) {
    ConditionGroup<SysMessageEventError> build = ConditionGroup.build()
      .addCondition("messageId",ConditionEnum.OPERATE_EQUAL,messageId);
    return eventErrorDao.findOne(build).orElse(null);
  }

  public DataPage pageHandle(String eventId, Map<String, String> params) {
    return DataPage.from(sysMessageEventHandleDao.findAll(
      ConditionGroup.build().addCondition("eventId", ConditionEnum.OPERATE_EQUAL, eventId),
      Pages.Helper.pageable(params, null)
    ));
  }

  @Transactional(rollbackFor = Exception.class)
  public int deleteEvents(Map<String, String> params){
    List<String> ids = getDao().findIds(params);
    if(!ids.isEmpty()){
      sysMessageEventHandleDao.deleteByEventIds(ids);
      return deleteInBatch(ids);
    }
    return 0;
  }

  public void retryHandler(Map<String, String> params){
    String id = ConvertUtil.checkNotNull(params, "id", String.class);
    SysMessageEventHandle handler = sysMessageEventHandleDao.findById(id)
                                                          .orElseThrow(() -> new IllegalArgumentException("未找到数据"));
    SysMessageEvent event = findById(handler.getEventId());
    Assert.notNull(event, "未找到数据");
    Channel channel = Channel.parse(event.getChannel());
    Assert.notNull(channel, "channel parse error > " + event.getChannel());
    channelMessageSender.retryHandler(channel, handler);
  }
}
