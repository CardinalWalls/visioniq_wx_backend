package com.wk.vpac.main.controller.admin.sys;

/**
 * DictionaryDataController
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2018/7/2 0002 14:55
 */
import com.base.components.common.constants.Valid;
import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.dictionary.DictionaryNode;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.service.cache.DictionaryCacheService;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.google.common.collect.Lists;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.sys.DictionaryData;
import com.wk.vpac.main.service.admin.sys.DictionaryDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
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
public class DictionaryDataController {

  @Autowired
  private DictionaryDataService dictionaryDataService;
  @Autowired
  private DictionaryCacheService dictionaryCacheService;

  /**
   * 页面
   *
   * @param request
   * @param response
   * @param modelMap
   *
   * @return
   */
  @GetMapping({"/sys/dictionary/data/index"})
  public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
    return "sys/dictionary/data/index";
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
  @GetMapping(value = "/sys/dictionary/data/page")
  public ResponseEntity page(@RequestParam Map<String, String> params) {
    ConditionGroup<DictionaryData> conditionGroup = ConditionGroup.build();
    String dictId = ConvertUtil.convertNullable(params.get("searchDictId"), String.class);
    String dataName = ConvertUtil.convertNullable(params.get("searchDataName"), String.class);
    Integer status = ConvertUtil.convertNullable(params.get("searchStatus"), Integer.class);
    Assert.notNull(dictId, "字典id不能为空");
    conditionGroup.addCondition("dictId", ConditionEnum.OPERATE_EQUAL, dictId);
    if (StringUtils.isNotEmpty(dataName)) {
      conditionGroup.addCondition("dataName", ConditionEnum.OPERATE_RIGHT_LIKE, dataName);
    }
    if (status != null && Valid.parseVal(status) != null) {
      conditionGroup.addCondition("status", ConditionEnum.OPERATE_EQUAL, status);
    }
    return new ResponseEntity<>(DataPage.from(dictionaryDataService.findAll(conditionGroup, Pages.Helper
      .pageable(params, Sort.by(Sort.Direction.ASC, "orderNo")))), HttpStatus.OK);
  }

  /**
   * 新增修改
   *
   * @param params -
   *
   * @return smsTemplate
   */
  @PostMapping(value = "/sys/dictionary/data/saveOrUpdate")
  public ResponseEntity save(@RequestParam Map<String, String> params) {
    dictionaryDataService.saveOrUpdate(params);
    return ResponseEntity.ok(JsonResult.success());
  }

  /**
   * 删除
   *
   * @param params -
   *
   * @return -
   */
  @PostMapping(value = "/sys/dictionary/data/delete")
  public ResponseEntity delete(@RequestParam Map<String, String> params) {
    String[] idsList = StringUtils.split(params.get("ids"), ",");
    dictionaryDataService.delete(Lists.newArrayList(idsList));
    return ResponseEntity.ok(JsonResult.success());
  }

  /**
   * 根据字典编码获取字典数据(多个值)
   *
   * @param params -
   * <p> dictCode               - NotNull  - Str  - 字典编码
   *
   * @return
   */
  @GetMapping(value = "/sys/dictionary/data/list")
  public ResponseEntity dictionaryDataList(@RequestParam Map<String, String> params) {
    String dictCode = ConvertUtil.checkNotNull(params, "dictCode", "字典编号不能为空", String.class);
    List<DictionaryNode> list = dictionaryCacheService.list(dictCode);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  /**
   * 根据字典编码获取字典数据(单个值)
   *
   * @param params -
   * <p> dictCode               - NotNull  - Str  - 字典编码
   *
   * @return
   */
  @GetMapping(value = "/sys/dictionary/data/single")
  public ResponseEntity dictionaryDataSingle(@RequestParam Map<String, String> params) {
    String dictCode = ConvertUtil.checkNotNull(params, "dictCode", "字典编号不能为空", String.class);
    DictionaryNode node = dictionaryCacheService.single(dictCode);
    return new ResponseEntity<>(node, HttpStatus.OK);
  }

}
