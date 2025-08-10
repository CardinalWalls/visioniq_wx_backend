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
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.util.SetsHelper;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.user.UserVisualFunctionReport;
import com.wk.vpac.main.service.api.user.UserVisualFunctionReportService;
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

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * UserVisualFunctionReportController
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @since 2024-05-09 09:58
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "用户档案-视功能检查报告")
public class UserVisualFunctionReportController {

  private final UserVisualFunctionReportService userVisualFunctionReportService;

  public UserVisualFunctionReportController(UserVisualFunctionReportService userVisualFunctionReportService){
    this.userVisualFunctionReportService = userVisualFunctionReportService;
  }

  @Operation(summary="视功能检查报告-分页")
  @ApiExtension(author = "code generator", update = "2024-05-09 10:00:56", token = @Token)
  @RequestModel({
    @Param(name = "userArchiveId", value = "档案ID", required = true),
    @Param(name = Pages.PAGE_NUM_VAR_NAME, value = "当前页数; 默认" + Pages.PAGE_NUM, dataType = Integer.class),
    @Param(name = Pages.PAGE_SIZE_VAR_NAME, value = "每页行数; 默认" + Pages.PAGE_SIZE, dataType = Integer.class)
  })
  @ReturnModel(baseModel = DataPage.class, value = {
    @Param(name = "list[]", value = "数据列表", dataType = ArrayType.class, genericType = UserVisualFunctionReport.class)
  })
  @GetMapping(value = "/user/userVisualFunctionReport/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(PageParamMap params) {
    return new ResponseEntity<>(userVisualFunctionReportService.page(params), HttpStatus.OK);
  }

  @Operation(summary="视功能检查报告-根据ID查询")
  @ApiExtension(author = "code generator", update = "2024-05-09 10:00:56", token = @Token)
  @RequestModel({
    @Param(name = "id", value = "ID", required = true, requestScope = Scope.PATH)
  })
  @ReturnModel(baseModel = UserVisualFunctionReport.class)
  @GetMapping(value = "/user/userVisualFunctionReport/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findById(@PathVariable("id") String id) {
    PageParamMap paramMap = new PageParamMap();
    paramMap.put("id", id);
    List<RowMap> list = userVisualFunctionReportService.page(paramMap).getList();
    return new ResponseEntity<>(list.isEmpty() ? Collections.emptyMap() : list.getFirst(), HttpStatus.OK);
  }

  @Operation(summary="视功能检查报告-新增")
  @ApiExtension(author = "code generator", update = "2024-05-09 10:00:56", token = @Token)
  @RequestBodyModel(baseModel = UserVisualFunctionReport.class, value = {
    @Param(name = "id", hidden = true),
    @Param(name = "createTime", hidden = true),
    @Param(name = "updateTime", hidden = true),
    @Param(name = "userArchiveId", value = "档案ID", required = true),
  })
  @PostMapping(value = "/user/userVisualFunctionReport", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserVisualFunctionReport> save(@RequestBody ParamMap params) {
    return new ResponseEntity<>(userVisualFunctionReportService.save(params), HttpStatus.CREATED);
  }

  @Operation(summary="视功能检查报告-修改")
  @ApiExtension(author = "code generator", update = "2024-05-09 10:00:56", token = @Token)
  @RequestBodyModel(baseModel = UserVisualFunctionReport.class, value = {
    @Param(name = "id", value = "报告ID", required = true),
    @Param(name = "createTime", hidden = true),
    @Param(name = "updateTime", hidden = true),
    @Param(name = "userArchiveId", hidden = true),
  })
  @PutMapping(value = "/user/userVisualFunctionReport", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserVisualFunctionReport> update(@RequestBody ParamMap params) {
    return new ResponseEntity<>(userVisualFunctionReportService.save(params), HttpStatus.CREATED);
  }

  @Operation(summary="视功能检查报告-删除")
  @ApiExtension(author = "code generator", update = "2024-05-09 10:00:56", token = @Token)
  @RequestModel({
    @Param(name = "ids", value = "ID多个以逗号分隔", required = true, requestScope = Scope.PATH)
  })
  @DeleteMapping(value = "/user/userVisualFunctionReport/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@PathVariable("ids") String ids) {
    Set<String> idSet = SetsHelper.toStringSets(ids, ",");
    userVisualFunctionReportService.delete(idSet);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
