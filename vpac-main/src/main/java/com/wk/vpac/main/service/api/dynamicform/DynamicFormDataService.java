package com.wk.vpac.main.service.api.dynamicform;

import com.base.components.cache.Cache;
import com.base.components.cache.CacheManager;
import com.base.components.common.constants.Valid;
import com.base.components.common.dto.dictionary.DictionaryNode;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.exception.auth.AuthException;
import com.base.components.common.exception.business.BusinessException;
import com.base.components.common.service.cache.DictionaryCacheService;
import com.base.components.common.token.TokenCacheObj;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.IPUtils;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.Logs;
import com.base.components.common.util.ServletContextHolder;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.database.jpa.util.JpaHelper;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.base.components.oss.OssService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.wk.vpac.cache.CacheName;
import com.wk.vpac.cache.dictionary.ReceiveSmsAdminUser;
import com.wk.vpac.common.constants.sms.SmsTemplateId;
import com.wk.vpac.common.service.sms.SmsService;
import com.wk.vpac.database.dao.dynamicform.DynamicDataCodeDao;
import com.wk.vpac.database.dao.dynamicform.DynamicDataColumnsDao;
import com.wk.vpac.database.dao.dynamicform.DynamicFormDataDao;
import com.wk.vpac.domain.dynamicform.DynamicDataCode;
import com.wk.vpac.domain.dynamicform.DynamicDataColumns;
import com.wk.vpac.domain.dynamicform.DynamicFormData;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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

//  @Autowired
//  private DynamicDataChildDao dynamicDataChildDao;
  @Autowired
  private DynamicDataCodeDao dynamicDataCodeDao;
  @Autowired
  private DynamicDataColumnsDao dynamicDataColumnsDao;
  @Autowired
  private CacheManager cacheManager;
  @SuppressWarnings("all")
  @Autowired
  private OssService ossService;
  @Autowired
  private SmsService smsService;
  @Autowired
  private DictionaryCacheService dictionaryCacheService;

  public DataPage page(Map<String, String> params){
    String code = params.get("code");
    if(StringUtils.isBlank(code)){
      return DataPage.getEmpty();
    }
    String querySql = "SELECT " + JpaHelper.findColumns(DynamicFormData.class, "t", "remark") + " FROM base_dynamic_form_data t WHERE 1=1";
    String countSql = "SELECT COUNT(1) FROM base_dynamic_form_data t WHERE 1=1";
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    sqlBuilder.addWhereEq("t", "code", code);
    sqlBuilder.addWhereEq("t", "user_id", TokenThreadLocal.getTokenObjNonNull().objId().toString());
    String prefix = "jsonData_";
    for (Map.Entry<String, String> entry : params.entrySet()) {
      String key = entry.getKey();
      int i = key.indexOf(prefix);
      if(i == 0){
        String value = entry.getValue();
        if(StringUtils.isNotBlank(value)){
          String field = key.substring(prefix.length());
          sqlBuilder.addWhere(" AND t.json_data->'$."+field+"' = :jsonData_"+field);
          sqlBuilder.setParameter("jsonData_"+field, entry.getValue());
        }
      }
    }
    Query count = sqlBuilder.bindQuerySql(countSql).build(JpaHelper.getEntityManager());
    Query query = JpaHelper.setMapResult(
      sqlBuilder.bindQuerySql(querySql).orderBy("t.create_time DESC").build(JpaHelper.getEntityManager()));
    return JpaHelper.pageByQuery(count, query, params);
  }

  @Transactional(rollbackFor = Exception.class)
  public DynamicFormData save(Map<String, String> params) {
    HttpServletRequest request = ServletContextHolder.getRequestNonNull();
    String userId = "";
    TokenCacheObj tokenObj = TokenThreadLocal.getTokenObj();
    if(tokenObj != null){
      userId = tokenObj.objId().toString();
    }
    params.remove("userId");
    String id = ConvertUtil.convert(params.get("id"), String.class);
    DateTime now = DateTime.now();
    DynamicFormData dynamicFormData;
    if (StringUtils.isBlank(id)) {
      dynamicFormData = ConvertUtil.populate(new DynamicFormData(), params);
      dynamicFormData.setId(null);
      dynamicFormData.setUserId(userId);
      dynamicFormData.setStatus(Valid.TRUE.getVal());
      dynamicFormData.setCreateTime(now.toDate());
      dynamicFormData.setUpdateTime(null);
    } else {
      dynamicFormData = ConvertUtil.populate(findById(id), params);
      if(StringUtils.isNotBlank(dynamicFormData.getUserId())){
        Assert.isTrue(dynamicFormData.getUserId().equals(userId), "没有修改权限");
      }
      dynamicFormData.setUpdateTime(now.toDate());
    }

    DynamicDataCode dynamicDataCode = dynamicDataCodeDao.existsCode(
      dynamicFormData.getCode(), now.toDate()).orElseThrow(() -> new BusinessException("未找到数据（" + dynamicFormData.getCode() + "）"));
    //检查是否需要登录
    if(dynamicDataCode.getUserAuth() && StringUtils.isBlank(userId)){
      throw new AuthException();
    }
    String captchaPreCode = params.get("_captchaPreCode_");
    limitCheck(dynamicDataCode.getCaptcha(), captchaPreCode, params.get("_captchaCode_"), request);
    DynamicDataColumns dynamicDataColumns = dynamicDataColumnsDao.findByCode(dynamicDataCode.getCode());
    Assert.notNull(dynamicDataColumns, "未找到表单数据");
    ObjectNode jsonData = transformWithColumnRule(dynamicDataColumns, dynamicFormData);
    dynamicFormData.setJsonData(jsonData.toString());
    String title = params.get("title");
    dynamicFormData.setTitle(StringUtils.isBlank(title) ? dynamicDataCode.getTitle() : title);
    DynamicFormData formData = getDao().save(dynamicFormData);
//    ArrayNode arr = (ArrayNode) jsonData.get("arr");
//    if (arr != null) {
//      for (JsonNode jsonNode : arr) {
//        String name = jsonNode.get("name").textValue();
//        ArrayNode childData = (ArrayNode) jsonNode.get("data");
//        for (JsonNode node : childData) {
//          DynamicDataChild child = new DynamicDataChild();
//          child.setJsonData(node.toString());
//          child.setRefId(formData.getId());
//          child.setStatus(Valid.TRUE.getVal());
//          child.setType(name);
//          child.setCode(formData.getCode());
//          child.setExtendId(orgId);
//          dynamicDataChildDao.save(child);
//        }
//      }
//    }
    //需要验证码，移除验证码
    if(dynamicDataCode.getCaptcha()){
      Cache cache = cacheManager.getCache(CacheName.NORMAL_CAPTCHA_IMG);
      cache.evict(captchaPreCode);
    }
    else{
      //缓存本次调用IP
      limitIp(request);
    }
    //发送微信通知管理员，first=收到新提交的业务数据，keyword1=title，keyword2=请进入后台系统处理
    if(dynamicDataCode.getNotifyWxAdmin()){
//      wxNotifyService.sendBizWxNotify("收到新提交的业务数据", NotifyBizType.DYNAMIC_FORM, orgId,
//                                        dynamicDataCode.getTitle(), "请进入后台系统处理", "", "");
    }
    if("100".equals(dynamicDataCode.getCode())){
      //发短信给管理员
      String adminPhones = dictionaryCacheService.list(ReceiveSmsAdminUser.class).stream()
                                                 .map(DictionaryNode::getDataKey)
                                                 .collect(Collectors.joining(","));
      if(StringUtils.isNotBlank(adminPhones)){
        smsService.sendSmsAsync(adminPhones, null, SmsTemplateId.ALiSMS_TodoToAdmin, ImmutableMap.of("name", "意见反馈"));
      }
    }
    return formData;
  }

  private ObjectNode transformWithColumnRule(DynamicDataColumns dynamicDataColumns, DynamicFormData dynamicFormData){
    Map<String, String> uniqueMap = Maps.newHashMap();
    Map<String, String> attachmentMap = Maps.newHashMap();
    StringBuilder uniqueTitle = new StringBuilder();
    ArrayNode columns;
    try {
      columns = JsonUtils.reader(dynamicDataColumns.getJsonData(), ArrayNode.class);
    } catch (Exception e) {
      throw new BusinessException("表单字段配置错误", e);
    }
    ObjectNode data;
    try {
      data = JsonUtils.reader(dynamicFormData.getJsonData(), ObjectNode.class);
    } catch (Exception e) {
      throw new BusinessException("表单数据错误", e);
    }
    ObjectNode stringData = JsonUtils.createObjectNode();
    for (JsonNode node : columns) {
      String field = node.path("field").textValue();
      String value = data.path(field).asText();
      value = "null".equalsIgnoreCase(value) ? "" : value;
      if(StringUtils.isNotBlank(field)){
        String title = node.path("title").textValue();
        title = StringUtils.isBlank(title) ? field : title;
        stringData.put(field, value);
        boolean attachment = node.path("attachment").booleanValue();
        if (node.path("require").booleanValue()) {
          checkRequire(title, value, attachment);
        }
        if (node.path("unique").booleanValue()) {
          checkRequire(title, value, attachment);
          uniqueMap.put(field, value);
          if(uniqueTitle.length() > 0){
            uniqueTitle.append(", ");
          }
          uniqueTitle.append(title);
        }
        if (attachment) {
          if(value.length() > 2){
            attachmentMap.put(field, value);
          }else{
            stringData.put(field, "[]");
          }
        }
      }
    }
    checkUnique(dynamicFormData.getId(), uniqueMap, uniqueTitle);
    Map<String, String> persistent = persistentAttachment(attachmentMap);
    for (Map.Entry<String, String> entry : persistent.entrySet()) {
      stringData.put(entry.getKey(), entry.getValue());
    }
    return stringData;
  }

  private void checkRequire(String title, String value, boolean attachment){
    if(attachment){
      Assert.isTrue(StringUtils.isNotBlank(value) && value.length() > 2, "请上传：" + title);
    }
    else{
      Assert.isTrue(StringUtils.isNotBlank(value), "请填写：" + title);
    }
  }

  private void checkUnique(String updateId, Map<String, String> uniqueMap, StringBuilder uniqueTitle){
    StringBuilder sb = new StringBuilder();
    boolean hasId = StringUtils.isNotBlank(updateId);
    if(hasId){
      sb.append(" AND id != :id ");
    }
    if(uniqueMap.size() > 0){
      for (Map.Entry<String, String> entry : uniqueMap.entrySet()) {
        sb.append(" AND json_data->'$.").append(entry.getKey()).append("'=:json_").append(entry.getKey());
      }
      Query query = getEntityManager().createNativeQuery("SELECT COUNT(1) FROM base_dynamic_form_data WHERE 1=1 " + sb);
      for (Map.Entry<String, String> entry : uniqueMap.entrySet()) {
        query.setParameter("json_"+entry.getKey(), entry.getValue());
      }
      if(hasId){
        query.setParameter("id", updateId);
      }
      int count = ConvertUtil.convert(query.getSingleResult(), 0);
      Assert.isTrue(count == 0, "请勿提交重复数据：" + uniqueTitle);
    }
  }

  /** 检测验证码或ip请求频繁限制 */
  private void limitCheck(boolean captcha, String captchaPreCode, String captchaCode, HttpServletRequest request){
    if(captcha){
      Assert.isTrue(StringUtils.isNoneBlank(captchaPreCode, captchaCode), "请填写验证码");
      Cache cache = cacheManager.getCache(CacheName.NORMAL_CAPTCHA_IMG);
      Assert.isTrue(captchaCode.equalsIgnoreCase(cache.get(captchaPreCode, String.class)), "验证码错误");
    }
    else{
      Cache cache = cacheManager.getDefaultCache();
      String realIp = IPUtils.getRealIp(request);
      if(StringUtils.isNotBlank(realIp)){
        Assert.isTrue(cache.get(buildIpLimitCacheKey(realIp)).get() == null, "请求过于频繁，请稍后再试");
      }
    }
  }

  private String buildIpLimitCacheKey(String ip){
    return "DYNAMIC_FORM_IP_LIMIT-" + ip;
  }

  private void limitIp(HttpServletRequest request){
    Cache cache = cacheManager.getDefaultCache();
    String realIp = IPUtils.getRealIp(request);
    if(StringUtils.isNotBlank(realIp)){
      String key = buildIpLimitCacheKey(realIp);
      cache.put(key, 1);
      cache.expire(key, 20, TimeUnit.SECONDS);
    }
  }

  private Map<String, String> persistentAttachment(Map<String, String> attachmentMap){
    Map<String, String> persistent = Maps.newHashMap();
    for (Map.Entry<String, String> entry : attachmentMap.entrySet()) {
      String val = "[]";
      try {
        ArrayNode array = JsonUtils.reader(entry.getValue(), ArrayNode.class);
        val = ossService.persistent("dynamicForm", null, array).toString();
      } catch (Exception e) {
        Logs.get().error("", e);
      }
      persistent.put(entry.getKey(), val);
    }
    return persistent;
  }

}