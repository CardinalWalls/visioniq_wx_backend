package com.wk.vpac.main.controller.api.user;

import com.base.components.common.constants.sys.Pages;
import com.base.components.common.doc.Scope;
import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestBodyModel;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.doc.annotation.Token;
import com.base.components.common.doc.type.ArrayType;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.util.SetsHelper;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.token.TokenConstant;
import com.wk.vpac.domain.user.UserIntervene;
import com.wk.vpac.main.service.api.user.UserInterveneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * UserIntervene Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2024-06-11
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "视力干预模块")
public class UserInterveneController {

  private final UserInterveneService userInterveneService;

  public UserInterveneController(UserInterveneService userInterveneService){
    this.userInterveneService = userInterveneService;
  }

  @Operation(summary="视力干预-分页")
  @ApiExtension(author = "code generator", update = "2024-06-11 11:16", token = @Token(name = TokenConstant.EXPERT_TOKEN_KEY))
  @RequestModel({
    @Param(name = "userArchiveId", value = "用户档案ID", required = true),
    @Param(name = "pushed", value = "是否已推送", dataType = Boolean.class),
    @Param(name = Pages.PAGE_NUM_VAR_NAME, value = "当前页数; 默认" + Pages.PAGE_NUM, dataType = Integer.class),
    @Param(name = Pages.PAGE_SIZE_VAR_NAME, value = "每页行数; 默认" + Pages.PAGE_SIZE, dataType = Integer.class)
  })
  @ReturnModel(baseModel = DataPage.class, value = {
    @Param(name = "list[]", value = "数据列表", dataType = ArrayType.class, genericType = UserIntervene.class)
  })
  @GetMapping(value = "/user/userIntervene/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(PageParamMap params) {
    return new ResponseEntity<>(userInterveneService.page(params), HttpStatus.OK);
  }

  @Operation(summary="视力干预-新增")
  @ApiExtension(author = "code generator", update = "2024-06-11 11:16", token = @Token(name = TokenConstant.EXPERT_TOKEN_KEY))
  @RequestBodyModel({
    @Param(name = "type", value = "干预类型；0=正常、1=远视储备不足、2=轻度-中度近视、3=中度-高度近视、4=超高度近视", required = true, dataType = Integer.class),
    @Param(name = "scheme", value = "干预措施", required = true),
    @Param(name = "remark", value = "备注"),
    @Param(name = "userArchiveId", value = "用户档案ID", required = true),
  })
  @PostMapping(value = "/user/userIntervene", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserIntervene> save(@RequestBody ParamMap params) {
    return new ResponseEntity<>(userInterveneService.save(params), HttpStatus.CREATED);
  }

  @Operation(summary="视力干预-修改")
  @ApiExtension(author = "code generator", update = "2024-06-11 11:16", token = @Token(name = TokenConstant.EXPERT_TOKEN_KEY))
  @RequestBodyModel({
    @Param(name = "id", value = "ID", required = true),
    @Param(name = "type", value = "干预类型；0=正常、1=远视储备不足、2=轻度-中度近视、3=中度-高度近视、4=超高度近视", required = true, dataType = Integer.class),
    @Param(name = "scheme", value = "干预措施", required = true),
    @Param(name = "remark", value = "备注"),
  })
  @PutMapping(value = "/user/userIntervene", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserIntervene> update(@RequestBody ParamMap params) {
    return new ResponseEntity<>(userInterveneService.save(params), HttpStatus.CREATED);
  }

  @Operation(summary="视力干预-推送给用户")
  @ApiExtension(author = "code generator", update = "2024-06-11 11:16", token = @Token(name = TokenConstant.EXPERT_TOKEN_KEY))
  @RequestBodyModel({
    @Param(name = "id", value = "ID", required = true),
  })
  @PutMapping(value = "/user/userIntervene/push", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserIntervene> push(@RequestBody ParamMap params) {
    return new ResponseEntity<>(userInterveneService.push(params.hasText("id", ()->"请选择需要推送的记录")), HttpStatus.CREATED);
  }

  @Operation(summary="视力干预-删除")
  @ApiExtension(author = "code generator", update = "2024-06-11 11:16", token = @Token(name = TokenConstant.EXPERT_TOKEN_KEY))
  @RequestModel({
    @Param(name = "ids", value = "ID多个以逗号分隔", required = true, requestScope = Scope.PATH)
  })
  @DeleteMapping(value = "/user/userIntervene/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> delete(@PathVariable("ids") String ids) {
    Set<String> idSet = SetsHelper.toStringSets(ids, ",");
    userInterveneService.delete(idSet);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
