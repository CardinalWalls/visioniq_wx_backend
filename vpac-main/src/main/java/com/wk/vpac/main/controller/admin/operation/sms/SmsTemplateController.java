package com.wk.vpac.main.controller.admin.operation.sms;

import com.base.components.common.constants.Valid;
import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wk.vpac.common.constants.sms.SmsParamType;
import com.wk.vpac.common.constants.sms.SmsTemplateUsuallyParam;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.sys.SmsTemplate;
import com.wk.vpac.domain.sys.SmsTemplateParam;
import com.wk.vpac.main.service.admin.operation.sms.SmsTemplateParamService;
import com.wk.vpac.main.service.admin.operation.sms.SmsTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * SmsTemplate Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-22
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class SmsTemplateController {

  @Autowired
  private SmsTemplateService smsTemplateService;
  @Autowired
  private SmsTemplateParamService smsTemplateParamService;


  /**
   * 页面
   *
   * @param request
   * @param response
   * @param modelMap
   *
   * @return
   */
  @GetMapping({"/operation/sms/template/index"})
  public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
    Map<String, List<Map<String, String>>> res = Maps.newHashMap();
    for (SmsParamType smsParamType : SmsParamType.values()) {
      List<Map<String, String>> list = Lists.newArrayList();
      for (SmsTemplateUsuallyParam item : smsParamType.getParams()) {
        Map<String, String> map = Maps.newHashMap();
        map.put(item.getCode(), item.getDesc());
        list.add(map);
      }
      res.put(smsParamType.getCode(), list);
    }
    modelMap.put("paramMap", res);
    return "operation/sms/template/index";
  }

  /**
   * 分页
   *
   * @param params -
   * <p> pageNum        - Nullable   - Int - 当前页
   * <p> pageSize       - Nullable   - Int - 每页记录数
   *
   * @return DataPage
   */
  @GetMapping(value = "/operation/sms/template/page")
  public ResponseEntity page(@RequestParam Map<String, String> params) {
    ConditionGroup<SmsTemplate> conditionGroup = ConditionGroup.build();
    String typeName = ConvertUtil.convertNullable(params.get("searchTypeName"), String.class);
    String tplName = ConvertUtil.convertNullable(params.get("searchTplName"), String.class);
    String content = ConvertUtil.convertNullable(params.get("searchContent"), String.class);
    Integer status = ConvertUtil.convertNullable(params.get("searchStatus"), Integer.class);
    if (StringUtils.isNotEmpty(typeName)) {
      conditionGroup.addCondition("typeName", ConditionEnum.OPERATE_EQUAL, typeName);
    }
    if (StringUtils.isNotEmpty(tplName)) {
      conditionGroup.addCondition("tplName", ConditionEnum.OPERATE_RIGHT_LIKE, tplName);
    }
    if (StringUtils.isNotEmpty(content)) {
      conditionGroup.addCondition("content", ConditionEnum.OPERATE_LIKE, content);
    }
    if (status != null && Valid.parseVal(status) != null) {
      conditionGroup.addCondition("status", ConditionEnum.OPERATE_EQUAL, status);
    }
    return new ResponseEntity<>(DataPage.from(smsTemplateService.findAll(conditionGroup, Pages.Helper
      .pageable(params, Sort.by(Sort.Direction.DESC, "createTime")))), HttpStatus.OK);
  }

  /**
   * 根据ID查询
   *
   * @param id -
   *
   * @return smsTemplate
   */
  @GetMapping(value = "/operation/sms/template/{id}")
  public ResponseEntity findById(@PathVariable("id") String id) {
    return new ResponseEntity<>(smsTemplateService.findById(id), HttpStatus.OK);
  }

  /**
   * 新增修改
   *
   * @param params -
   *
   * @return smsTemplate
   */
  @PostMapping(value = "/operation/sms/template/saveOrUpdate")
  public ResponseEntity save(@RequestParam Map<String, String> params) throws IOException {
    smsTemplateService.saveOrUpdate(params);
    return ResponseEntity.ok(JsonResult.success());
  }


  /**
   * 删除
   *
   * @param ids -
   *
   * @return -
   */
  @PostMapping(value = "/operation/sms/template/{ids}")
  public ResponseEntity delete(@PathVariable("ids") String ids) {
    String[] idsList = StringUtils.split(ids, ",");
    smsTemplateService.delete(Lists.newArrayList(idsList));
    return ResponseEntity.ok(JsonResult.success());
  }

  /**
   * 删除
   *
   * @param params -
   *
   * @return -
   */
  @PostMapping(value = "/operation/sms/template/delete")
  public ResponseEntity delete(@RequestParam Map<String, String> params) {
    String[] idsList = StringUtils.split(params.get("ids"), ",");
    smsTemplateService.delete(Lists.newArrayList(idsList));
    return ResponseEntity.ok(JsonResult.success());
  }

  /**
   * 根据模板ID查询模板参数
   *
   * @param params
   *
   * @return smsTemplate
   */
  @GetMapping(value = "/operation/sms/template/param")
  public ResponseEntity findParamByTlpId(@RequestParam Map<String, String> params) {
    String templateId = ConvertUtil.checkNotNull(params, "templateId", "模板id不能为空", String.class);
    List<SmsTemplateParam> list = smsTemplateParamService
      .findAll(ConditionGroup.build().addCondition("templateId", ConditionEnum.OPERATE_EQUAL, templateId));
    return new ResponseEntity<>(list, HttpStatus.OK);
  }


}
