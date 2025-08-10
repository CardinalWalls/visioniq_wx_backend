package com.wk.vpac.main.service.admin.dynamicform;

import com.base.components.common.constants.Valid;
import com.base.components.common.constants.sys.Dates;
import com.base.components.common.constants.sys.PageHelper;
import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wk.vpac.database.dao.dynamicform.DynamicDataChildDao;
import com.wk.vpac.database.dao.dynamicform.DynamicDataCodeDao;
import com.wk.vpac.database.dao.dynamicform.DynamicDataColumnsDao;
import com.wk.vpac.database.dao.dynamicform.DynamicFormDataDao;
import com.wk.vpac.database.dao.user.UserBaseInfoDao;
import com.wk.vpac.domain.dynamicform.DynamicDataChild;
import com.wk.vpac.domain.dynamicform.DynamicDataCode;
import com.wk.vpac.domain.dynamicform.DynamicDataColumns;
import com.wk.vpac.domain.dynamicform.DynamicFormData;
import com.wk.vpac.domain.user.UserBaseInfo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DynamicFormData Service
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class DynamicFormDataService extends AbstractJpaService<DynamicFormData, String, DynamicFormDataDao> {
  @Autowired
  private DynamicDataColumnsDao dynamicDataColumnsDao;
  @Autowired
  private DynamicDataChildDao dynamicDataChildDao;
  @Autowired
  private DynamicDataCodeDao dynamicDataCodeDao;
  @Autowired
  private UserBaseInfoDao userBaseInfoDao;

  public DataPage page(Map<String, String> params) {
    String code = params.get("code");
    String title = params.get("title");
    List<String> types = dynamicDataChildDao.groupTypeByCode(code);
    ConditionGroup<DynamicDataColumns> build = ConditionGroup.build();
    build.addCondition("code", ConditionEnum.OPERATE_EQUAL, code);
    Optional<DynamicDataColumns> columns = dynamicDataColumnsDao.findOne(build);
    DynamicDataColumns dataColumns = columns.orElseThrow(() -> new IllegalArgumentException("未找到数据结构！"));
    Set<String> columnsSet = Sets.newHashSet();
    ArrayNode nodes;
    try {
      nodes = JsonUtils.reader(dataColumns.getJsonData(), ArrayNode.class);
      for (JsonNode column : nodes) {
        String field = column.get("field").textValue();
        columnsSet.add(field);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("json数据异常");
    }
    DataPage<Map> dataPage = getDao().page(code, title, params, types, columnsSet);
    List<Map> list = dataPage.getList();
    List<Map> collect = list.stream().map(val -> {
      Map<String, Object> map = Maps.newHashMap();
      map.put("id", val.get("id"));
      map.put("title", val.get("title"));
      map.put("userId", val.get("userId"));
      map.put("userPhone", val.get("userPhone"));
      Object userNickName = val.get("userNickName");
      map.put("userName", userNickName == null || StringUtils.isBlank(userNickName.toString()) ? val.get("wxName") : userNickName);
      Object userAvatar = val.get("userAvatar");
      map.put("userAvatar", userAvatar == null || StringUtils.isBlank(userAvatar.toString()) ? val.get("wxImg") : userAvatar);
      map.put("title", val.get("title"));
      map.put("updateTime", val.get("updateTime"));
      map.put("createTime", val.get("createTime"));
      map.put("remark", val.get("remark"));
      map.put("result", val.get("result"));
      map.put("_status", val.get("status"));
      // count统计放入map
      Set set = val.keySet();
      for (Object key : set) {
        if (key.toString().startsWith("count_")) {
          map.put(key.toString(), val.get(key));
        }
      }
      try {
        ObjectNode data = JsonUtils.reader((String) val.get("jsonData"), ObjectNode.class);
        //        for (JsonNode column : nodes) {
        //          String field = column.get("field").textValue();
        //          map.put(field, data.get(field));
        //        }
        for (String column : columnsSet) {
          map.put("jsonData_"+column, data.get(column));
        }
      } catch (IOException e) {
        throw new IllegalArgumentException("json数据异常");
      }
      return map;
    }).collect(Collectors.toList());
    DataPage<Map> page = new DataPage<>();
    page.setList(collect);
    page.setPageSize(dataPage.getPageSize());
    page.setPages(dataPage.getPages());
    page.setPageNum(dataPage.getPageNum());
    page.setTotal(dataPage.getTotal());
    return page;
  }

  /**
   * DynamicFormData新增和修改
   *
   * @param dynamicFormData -
   *
   * @return -
   */
  @Transactional(rollbackFor = Exception.class)
  public DynamicFormData saveOrUpdate(DynamicFormData dynamicFormData) {
    return getDao().saveAndFlush(dynamicFormData);
  }

  public List<Map> listCode(Map<String, String> params) {
    return dynamicDataCodeDao.listCode();
  }

  public ArrayNode columns(Map<String, String> params) {
    String code = params.get("code");
    Optional<DynamicDataColumns> one = dynamicDataColumnsDao.findOne(
      ConditionGroup.build().addCondition("code", ConditionEnum.OPERATE_EQUAL, code));
    DynamicDataColumns dynamicDataColumns = one.orElseThrow(() -> new IllegalArgumentException("未找到对应的数据结构！"));
    ArrayNode result = JsonUtils.createArrayNode();
    try {
      result.add(JsonUtils.createObjectNode().put("field", "userInfo").put("title", "平台用户"));
      result.add(JsonUtils.createObjectNode().put("field", "title").put("title", "标题"));
      ArrayNode columns = JsonUtils.reader(dynamicDataColumns.getJsonData(), ArrayNode.class);
      for (JsonNode column : columns) {
        ObjectNode jsonNodes = JsonUtils.toObjectNode(column);
        jsonNodes.put("field","jsonData_"+jsonNodes.path("field").asText());
        result.add(jsonNodes);
      }
      result.add(JsonUtils.createObjectNode().put("field", "updateTime").put("title", "更新时间"));
      result.add(JsonUtils.createObjectNode().put("field", "createTime").put("title", "创建时间"));
      result.add(JsonUtils.createObjectNode().put("field", "remark").put("title", "后台备注"));
      result.add(JsonUtils.createObjectNode().put("field", "result").put("title", "处理结果"));
    } catch (IOException e) {
      throw new IllegalArgumentException("json数据异常");
    }
    return result;
  }

  public List<String> type(Map<String, String> params) {
    String code = ConvertUtil.checkNotNull(params, "code", String.class);
    return dynamicDataChildDao.groupTypeByCode(code);
  }

  public List<Map> child(Map<String, String> params) {
    String id = ConvertUtil.checkNotNull(params, "id", String.class);
    String type = ConvertUtil.checkNotNull(params, "type", String.class);
    List<DynamicDataChild> childs = dynamicDataChildDao.findAll(
      ConditionGroup.build().addCondition("refId", ConditionEnum.OPERATE_EQUAL, id)
                  .addCondition("type", ConditionEnum.OPERATE_EQUAL, type));
    List<Map> list = Lists.newArrayList();
    for (DynamicDataChild child : childs) {
      try {
        ObjectNode reader = JsonUtils.reader(child.getJsonData(), ObjectNode.class);
        Iterator<String> iterator = reader.fieldNames();
        Map<String, Object> map = Maps.newHashMap();
        while (iterator.hasNext()) {
          String key = iterator.next();
          map.put(key, reader.get(key).textValue());
        }
        map.put("id", child.getId());
        list.add(map);
      } catch (IOException e) {
        throw new IllegalArgumentException("json数据异常");
      }
    }
    return list;
  }

  public ArrayNode childColumns(Map<String, String> params) {
    String code = ConvertUtil.checkNotNull(params, "code", String.class);
    String type = ConvertUtil.checkNotNull(params, "type", String.class);
    DynamicDataChild child = dynamicDataChildDao.findFirstByCodeAndType(code, type)
                                                .orElseThrow(() -> new IllegalArgumentException("为找到数据！"));
    ArrayNode arr = JsonUtils.mapper.createArrayNode();
    try {
      ObjectNode reader = JsonUtils.reader(child.getJsonData(), ObjectNode.class);
      Iterator<String> iterator = reader.fieldNames();
      while (iterator.hasNext()) {
        ObjectNode node = JsonUtils.mapper.createObjectNode();
        String key = iterator.next();
        node.put("field", key);
        node.put("title", key);
        arr.add(node);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("json数据异常");
    }
    return arr;
  }

  public DataPage pageCode(Map<String, String> params) {
    Page<Map> codes = dynamicDataCodeDao.pageCode(Pages.Helper.pageable(params, null));
    return DataPage.from(codes);
  }

  @Transactional(rollbackFor = Exception.class)
  public JsonResult saveOrUpdateCode(String id, String code, String title, String beginTime, String endTime,
                                     String remark, boolean captcha, boolean userAuth, boolean notifyWxAdmin,
                                     int notifyWxUser, String notifyWxUserLink, int status) {
    if (StringUtils.isBlank(id)) {
      if (!exists(code)) {
        DynamicDataCode dataCode = new DynamicDataCode();
        dataCode.setId(id);
        dataCode.setCode(code);
        dataCode.setTitle(title);
        dataCode.setBeginTime(DateTime.parse(beginTime, Dates.DATE_TIME_FORMATTER).toDate());
        dataCode.setEndTime(DateTime.parse(endTime, Dates.DATE_TIME_FORMATTER).toDate());
        dataCode.setRemark(remark);
        dataCode.setStatus(status);
        dataCode.setCaptcha(captcha);
        dataCode.setUserAuth(userAuth);
        dataCode.setNotifyWxAdmin(notifyWxAdmin);
        dataCode.setNotifyWxUser(notifyWxUser);
        dataCode.setNotifyWxUserLink(notifyWxUserLink);
        dynamicDataCodeDao.saveAndFlush(dataCode);
      } else {
        return JsonResult.fail("code已存在！");
      }
    } else {
      Assert.isTrue(0 == dynamicDataCodeDao.count(ConditionGroup.build()
                                             .addCondition("code", ConditionEnum.OPERATE_EQUAL, code)
                                              .addCondition("id", ConditionEnum.OPERATE_UNEQUAL, id)), "code已存在！");
      DynamicDataCode dataCode = dynamicDataCodeDao.findById(id)
                                                   .orElseThrow(() -> new IllegalArgumentException("未找到数据!"));
      dataCode.setTitle(title);
      dataCode.setBeginTime(DateTime.parse(beginTime, Dates.DATE_TIME_FORMATTER).toDate());
      dataCode.setEndTime(DateTime.parse(endTime, Dates.DATE_TIME_FORMATTER).toDate());
      dataCode.setRemark(remark);
      dataCode.setStatus(status);
      dataCode.setCaptcha(captcha);
      dataCode.setUserAuth(userAuth);
      dataCode.setNotifyWxAdmin(notifyWxAdmin);
      dataCode.setNotifyWxUser(notifyWxUser);
      dataCode.setNotifyWxUserLink(notifyWxUserLink);
    }
    return JsonResult.success();
  }

  private boolean exists(String code) {
    return dynamicDataCodeDao.count(ConditionGroup.build().addCondition("code", ConditionEnum.OPERATE_EQUAL, code)) > 0;
  }

  public DataPage pageColumns(Map<String, String> params) {
    return DataPage.from(dynamicDataColumnsDao.findAll(Pages.Helper.pageable(params, null)
    ));
  }

  @Transactional(rollbackFor = Exception.class)
  public JsonResult saveOrUpdateColumns(String id, String code, String jsonData) {
    Assert.isTrue(exists(code), "未找到表单编号");
    if (StringUtils.isBlank(id)) {
      Assert.isTrue(0 == dynamicDataColumnsDao.count(ConditionGroup.build()
                                                                   .addCondition("code", ConditionEnum.OPERATE_EQUAL, code)), "code已存在！");
      DynamicDataColumns columns = new DynamicDataColumns();
      columns.setCode(code);
      columns.setJsonData(jsonData);
      columns.setStatus(Valid.TRUE.getVal());
      dynamicDataColumnsDao.save(columns);
    } else {
      Assert.isTrue(0 == dynamicDataColumnsDao.count(ConditionGroup.build()
                                                                .addCondition("code", ConditionEnum.OPERATE_EQUAL, code)
                                                                .addCondition("id", ConditionEnum.OPERATE_UNEQUAL, id)), "code已存在！");
      DynamicDataColumns columns = dynamicDataColumnsDao.findById(id)
                                                        .orElseThrow(() -> new IllegalArgumentException("未找到数据！"));
      columns.setJsonData(jsonData);
      columns.setCode(code);
      dynamicDataColumnsDao.save(columns);
    }
    return JsonResult.success();
  }

  public DataPage queryFromData(Map<String, String> params) {
    ConditionGroup<DynamicFormData> build = ConditionGroup.build().addCondition("code", ConditionEnum.OPERATE_EQUAL, params.get("code"));
    Page<DynamicFormData> data = getDao()
      .findAll(build, PageHelper.INSTANCE.pageable(params, Sort.by(Sort.Direction.DESC, "createTime")));
    DataPage<Object> empty = new DataPage<>();

    LinkedList<Object> objects = Lists.newLinkedList();
    data.getContent().forEach(dynamicFormData -> {
      String jsonData = dynamicFormData.getJsonData();
      try {
        ObjectNode reader = JsonUtils.reader(jsonData, ObjectNode.class);
        reader.put("id", dynamicFormData.getId());
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        reader.put("createTime", dateTimeFormatter.format(dynamicFormData.getCreateTime()));
        reader.put("status", dynamicFormData.getStatus());
        objects.add(reader);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    empty.setList(objects);
    empty.setTotal(data.getTotalElements());
    empty.setPageNum(data.getNumber());
    empty.setPageSize(data.getTotalPages());
    return empty;
  }

  @Transactional(rollbackFor = Exception.class)
  public int updateStatus(String id, int status, String result){
    int count = getDao().updateStatus(id, status, StringUtils.isBlank(result) ? "" : result);
    if(count > 0){
      DynamicFormData data = findById(id);
      //发送消息
      if(data != null){
        String userId = data.getUserId();
        if(StringUtils.isNotBlank(userId)){
          DynamicDataCode code = dynamicDataCodeDao.findByCode(data.getCode());
          if(code != null && code.getNotifyWxUser() != null && code.getNotifyWxUser() > 0){
            UserBaseInfo user = userBaseInfoDao.findById(userId).orElse(null);
            if(user != null){
              Map<String, String> params = Maps.newHashMap();
              if(code.getNotifyWxUser() == 1 && StringUtils.isNotBlank(user.getMpOpenId())){
//                params.put("first", "业务状态已更新");
//                params.put("keyword1", data.getTitle());
//                params.put("keyword2", (data.getStatus() == 2?"已处理":"已取消") + (StringUtils.isBlank(data.getResult())?"":"；" + data.getResult()));
//                sendWxMp(user.getMpOpenId(), checkUrl(org.getMobileWebSite() + code.getNotifyWxUserLink(), id), params);
              }
              else if(code.getNotifyWxUser() == 2 && StringUtils.isNotBlank(user.getMiniOpenId())){
//                params.put("thing1", data.getTitle());
//                params.put("phrase2", (data.getStatus() == 2?"已处理":"已取消") + (StringUtils.isBlank(data.getResult())?"":"；" + data.getResult()));
//                sendWxMa(user.getMiniOpenId(), checkUrl(code.getNotifyWxUserLink(), id), params);
              }
            }
          }
        }
      }
    }
    return count;
  }
  @Transactional(rollbackFor = Exception.class)
  public int updateRemark(String id, String remark){
    return getDao().updateRemark(id, remark);
  }


//  private void sendWxMp(String mpOpenId, String url, Map<String, String> params){
//    String mpTemplateId = multiOrgContext.getWeChatTemplateId(orgId, WxMsgTemplate.BUSINESS_NOTIFICATION);
//    if(StringUtils.isNotBlank(mpTemplateId)){
//      params.put("orgId", orgId);
//      weChatService.sendWxMessage(mpOpenId, mpTemplateId, params, url);
//    }
//  }
//  private void sendWxMa(String maOpenId, String url, Map<String, String> params){
//    String maTemplateId = multiOrgContext.getWxMaTemplateId(orgId, WxMaMsgTemplate.BUSINESS_NOTIFICATION);
//    if(StringUtils.isNotBlank(maTemplateId)){
//      params.put("orgId", orgId);
//      weChatService.sendMaSubscribeMessage(orgId, maOpenId, maTemplateId, url, params);
//    }
//  }

  private String checkUrl(String url, String id){
    return url.replace("{id}", id);
  }
}