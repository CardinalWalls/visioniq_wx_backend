package com.wk.vpac.main.controller.admin.sys;

import com.base.components.common.constants.Valid;
import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.google.common.collect.Lists;
import com.wk.vpac.common.constants.sys.DictionaryDataType;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.sys.Dictionary;
import com.wk.vpac.main.service.admin.sys.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Dictionary Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-07-02
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class DictionaryController {

  @Autowired
  private DictionaryService dictionaryService;

  /**
   * 页面
   *
   * @param request
   * @param response
   * @param modelMap
   *
   * @return
   */
  @GetMapping({"/sys/dictionary/index"})
  public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
    List<String> dataType = Lists.newArrayList();
    for (DictionaryDataType item : DictionaryDataType.values()) {
      dataType.add(item.toString().trim());
    }
    modelMap.put("dataType", dataType);
    return "sys/dictionary/index";
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
  @GetMapping(value = "/sys/dictionary/page")
  public ResponseEntity page(@RequestParam Map<String, String> params) {
    ConditionGroup<Dictionary> conditionGroup = ConditionGroup.build();
    String dictCode = ConvertUtil.convertNullable(params.get("searchDictCode"), String.class);
    String dictName = ConvertUtil.convertNullable(params.get("searchDictName"), String.class);
    String dataType = ConvertUtil.convertNullable(params.get("searchDataType"), String.class);
    Integer status = ConvertUtil.convertNullable(params.get("searchStatus"), Integer.class);
    Integer canEdit = ConvertUtil.convertNullable(params.get("searchCanEdit"), Integer.class);
    if (StringUtils.isNotEmpty(dictCode)) {
      conditionGroup.addCondition("dictCode", ConditionEnum.OPERATE_RIGHT_LIKE, dictCode);
    }
    if (StringUtils.isNotEmpty(dictName)) {
      conditionGroup.addCondition("dictName", ConditionEnum.OPERATE_RIGHT_LIKE, dictName);
    }
    if (StringUtils.isNotEmpty(dataType)) {
      conditionGroup.addCondition("dataType", ConditionEnum.OPERATE_EQUAL, dataType);
    }
    if (status != null && Valid.parseVal(status) != null) {
      conditionGroup.addCondition("status", ConditionEnum.OPERATE_EQUAL, status);
    }
    if (canEdit != null && canEdit != -1) {
      conditionGroup.addCondition("canEdit", ConditionEnum.OPERATE_EQUAL, canEdit);
    }
    return new ResponseEntity<>(DataPage.from(dictionaryService.findAll(conditionGroup, Pages.Helper
      .pageable(params, Sort.by(Sort.Direction.ASC, "orderNo")))), HttpStatus.OK);
  }

  /**
   * 新增修改
   *
   * @param params -
   *
   * @return smsTemplate
   */
  @PostMapping(value = "/sys/dictionary/saveOrUpdate")
  public ResponseEntity save(@RequestParam Map<String, String> params) {
    dictionaryService.saveOrUpdate(params);
    return ResponseEntity.ok(JsonResult.success());
  }

  /**
   * 删除
   *
   * @param params -
   *
   * @return -
   */
  @PostMapping(value = "/sys/dictionary/delete")
  public ResponseEntity delete(@RequestParam Map<String, String> params) {
    String[] idsList = StringUtils.split(params.get("ids"), ",");
    dictionaryService.delete(Lists.newArrayList(idsList));
    return ResponseEntity.ok(JsonResult.success());
  }

  /**
   * 刷新字典缓存
   * @return
   */
  @PostMapping(value = "/sys/dictionary/refresh-cache")
  public ResponseEntity refreshDictionaryCache() {
    dictionaryService.refreshDictionaryCache();
    return ResponseEntity.ok(JsonResult.success());
  }

}
