package com.wk.vpac.main.controller.api.dynamicform;

import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestBodyModel;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.doc.annotation.Token;
import com.base.components.common.doc.type.ArrayType;
import com.base.components.common.doc.validation.EL;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.log.system.SystemLogger;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.dynamicform.DynamicFormData;
import com.wk.vpac.main.service.api.dynamicform.DynamicFormDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * DynamicFormData Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-15
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "通用接口")
public class DynamicFormDataController {

  @Autowired
  private DynamicFormDataService dynamicFormDataService;

  @Operation(summary = "动态表单-查询分页", description =  "只能查询登录用户提交的信息")
  @ApiExtension(author = "李赓", update = "2021-03-12 08:48:12", token = @Token)
  @RequestModel({
    @Param(name = "code", value = "表单编号"),
    @Param(name = "jsonData_${表单字段}", value = "需要查询的表单字段")
  })
  @ReturnModel(baseModel = DataPage.class, value = {
    @Param(name = "list[]", value = "集合泛型实体对象", dataType = ArrayType.class, genericType = DynamicFormData.class),
    @Param(name = "list[].remark", hidden = true),
  })
  @GetMapping(value = "/dynamicFormData/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity page(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(dynamicFormDataService.page(params), HttpStatus.CREATED);
  }

  @SystemLogger
  @Operation(summary = "动态表单-添加数据")
  @ApiExtension(author = "李赓", update = "2021-03-12 08:48:12", token = @Token(require = false))
  @RequestBodyModel({
    @Param(name = "_captchaPreCode_",value = "验证码前置preCode，见/api/edge/captcha/normal"),
    @Param(name = "_captchaCode_",value = "验证码"),
    @Param(name = "title", value = "业务标题，为空时默认为原表单标题"),
    @Param(name = "code",value = "表单编号", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "jsonData" ,value = "表单数据（附件字段样式：[{\"name\":\"aaa.jpg\", \"url\":\"/pub/aaa/aaa.jpg\"}]）", required = true, checkEL = EL.NOT_BLANK)
  })
  @ReturnModel(baseModel = DynamicFormData.class)
  @PostMapping(value = "/dynamicFormData/pub", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity save(@RequestBody Map<String, String> params) {
    return new ResponseEntity<>(dynamicFormDataService.save(params), HttpStatus.CREATED);
  }

  @SystemLogger
  @Operation(summary = "动态表单-修改数据")
  @ApiExtension(author = "李赓", update = "2021-03-12 08:48:12", token = @Token(require = false))
  @RequestBodyModel({
    @Param(name = "id",value = "表单ID", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "title", value = "业务标题，为空时默认为原表单标题"),
    @Param(name = "jsonData" ,value = "表单数据", required = true, checkEL = EL.NOT_BLANK)
  })
  @ReturnModel(baseModel = DynamicFormData.class)
  @PutMapping(value = "/dynamicFormData", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity update(@RequestBody Map<String, String> params) {
    return new ResponseEntity<>(dynamicFormDataService.save(params), HttpStatus.CREATED);
  }

}
