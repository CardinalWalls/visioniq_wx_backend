package com.wk.vpac.main.listener.user;

import com.base.components.cache.msgqueue.service.BaseAsyncChannelMessageListener;
import com.base.components.common.constants.msgqueue.Channel;
import com.fasterxml.jackson.databind.JsonNode;
import com.wk.vpac.common.constants.msgqueue.channels.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * user
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2018/3/27 0027 10:59
 */
@Component
public class UserRegisterListener extends BaseAsyncChannelMessageListener {

//  @Autowired
//  private UserBaseInfoService userBaseInfoService;
//  @Autowired
//  private SmsService smsService;
//  @Autowired
//  private TransactionRecordService transactionRecordService;
//  @Autowired
//  private DictionaryCacheService cacheService;
//  @SuppressWarnings("unchecked")
//  private void register(JsonNode eventInfoJson){
//    String parentUserId = eventInfoJson.path("parentUserId").textValue();
//    String userId = eventInfoJson.path("userId").textValue();
//    if(StringUtils.isNoneBlank(parentUserId)){
//      //赠送推荐人积分
//      TransactionSrc promotionregist = TransactionSrc.PROMOTIONREGIST;
//      List<DictionaryNode> list = cacheService.list(IntegralPromotionRegist.class);
//      Map<String, DictionaryNode> dataMap = ConvertUtil
//        .transformToMap(list, DictionaryNode::getDataKey);
//
//      DictionaryNode dictionaryNode = dataMap.get(IntegralPromotionRegist.INTEGRALTOTAL.name());
//      int total = Integer.parseInt(dictionaryNode.getDataValue());
//      DictionaryNode day = dataMap.get(IntegralPromotionRegist.INTEGRALDAY.name());
//      int dayTotal = Integer.parseInt(day.getDataValue());
//      boolean greaterTotalIntegral = true;
//      boolean greaterDayTotal = true;
//      if(dayTotal>0){
//        DateTime now = DateTime.now();
//        DateTime start = now.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
//        DateTime end = now.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
//        //判断当日是否已达上限
//        greaterDayTotal = transactionRecordService.greaterThanTotal(dayTotal,parentUserId,TransactionSrc.PROMOTIONREGIST,start.toDate(),end.toDate());
//      }
//      if(total>0){
//        //判断总积分是否已达上限
//        greaterTotalIntegral = transactionRecordService.greaterThanTotal(total,parentUserId,TransactionSrc.PROMOTIONREGIST);
//      }
//      if(!greaterTotalIntegral && !greaterDayTotal){
//        //添加积分
//        addIntegral(parentUserId, promotionregist, dataMap,userId);
//      }
//    }
//  }
//
//  private void addIntegral(String parentUserId, TransactionSrc promotionregist,
//                           Map<String, DictionaryNode> dataMap,String userId) {
//    DictionaryNode integral = dataMap.get(IntegralPromotionRegist.INTEGRAL.name());
//    transactionRecordService.transactionIntegral(parentUserId,userId,promotionregist,false,
//                                                 promotionregist.getDesc(),Integer.parseInt(integral.getDataValue()),"",false);
//  }
//
  @Override
  public MessageChannel[] registerChannels() {
    return new MessageChannel[] {MessageChannel.USER_REGISTER_CHANNEL, MessageChannel.TEST_CHANNEL};
  }
//
  @Override
  public void onAsyncMessage(Channel messageChannel, JsonNode eventInfoJson, String remark) {
    System.out.println("messageChannel = " + messageChannel);
//    if(messageChannel instanceof MessageChannel){
//      MessageChannel channel = (MessageChannel) messageChannel;
//      if(channel == MessageChannel.USER_REGISTER_CHANNEL){
//        register(eventInfoJson);
//      }
//    }
  }
}
