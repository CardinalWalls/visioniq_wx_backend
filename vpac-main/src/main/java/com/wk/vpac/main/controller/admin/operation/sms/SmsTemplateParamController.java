package com.wk.vpac.main.controller.admin.operation.sms;

import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.page.DataPage;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.sys.SmsTemplateParam;
import com.wk.vpac.main.service.admin.operation.sms.SmsTemplateParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * SmsTemplateUsuallyParam Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-22
 */
@RestController
@RequestMapping(GatewayPath.ADMIN)
public class SmsTemplateParamController {

  @Autowired
  private SmsTemplateParamService smsTemplateParamService;

  /**
   * 分页
   * @param params -
   * <p> pageNum        - Nullable   - Int - 当前页
   * <p> pageSize       - Nullable   - Int - 每页记录数
   * 
   * @return DataPage
   */
  @GetMapping(value = "/smsTemplate/param/page")
  public ResponseEntity page(@RequestParam Map<String, String> params) {
    ConditionGroup<SmsTemplateParam> conditionGroup = ConditionGroup.build();
    // 条件: e.g. conditionGroup.addCondition("id", ConditionEnum.OPERATE_EQUAL,params.get("id"))
    return new ResponseEntity<>(DataPage.from(smsTemplateParamService.findAll(
      Pages.Helper.pageable(params, Sort.by(Sort.Direction.ASC, "id")))), HttpStatus.OK);
  }


}
