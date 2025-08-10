package com.wk.vpac.main.controller.api.user;

import com.base.components.common.constants.sys.Pages;
import com.base.components.common.doc.Scope;
import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.doc.annotation.Token;
import com.base.components.common.doc.type.ArrayType;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.user.UserVisionReport;
import com.wk.vpac.main.service.api.user.UserVisionReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserVisionReport Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "视力报告")
public class UserVisionReportController {

  private final UserVisionReportService userVisionReportService;

  public UserVisionReportController(UserVisionReportService userVisionReportService){
    this.userVisionReportService = userVisionReportService;
  }

  @Operation(summary="视力报告-分页")
  @ApiExtension(author = "code generator", update = "2023-09-11 15:29", token = @Token)
  @RequestModel({
    @Param(name = Pages.PAGE_NUM_VAR_NAME, value = "当前页数; 默认" + Pages.PAGE_NUM, dataType = Integer.class),
    @Param(name = Pages.PAGE_SIZE_VAR_NAME, value = "每页行数; 默认" + Pages.PAGE_SIZE, dataType = Integer.class)
  })
  @ReturnModel(baseModel = DataPage.class, value = {
    @Param(name = "list[]", value = "数据列表", dataType = ArrayType.class, genericType = UserVisionReport.class)
  })
  @GetMapping(value = "/user/userVisionReport/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(PageParamMap params) {
    return new ResponseEntity<>(userVisionReportService.page(params), HttpStatus.OK);
  }

  @Operation(summary="视力报告-根据ID查询")
  @ApiExtension(author = "code generator", update = "2023-09-11 15:29", token = @Token)
  @RequestModel({
    @Param(name = "id", value = "ID", required = true, requestScope = Scope.PATH)
  })
  @GetMapping(value = "/user/userVisionReport/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserVisionReport> findById(@PathVariable("id") String id) {
    return new ResponseEntity<>(userVisionReportService.findById(id), HttpStatus.OK);
  }

//  @Operation(summary="UserVisionReport新增")
//  @ApiExtension(author = "code generator", update = "2023-09-11 15:29")
//  @RequestBodyModel({
//    @Param(name = "aaa", value = "aaa字段", required = true, dataType = Integer.class),
//    @Param(name = "bbb", value = "bbb字段", required = true)
//  })
//  @PostMapping(value = "/user/userVisionReport", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<UserVisionReport> save(@RequestBody ParamMap params) {
//    return new ResponseEntity<>(userVisionReportService.save(params), HttpStatus.CREATED);
//  }

//  @Operation(summary="UserVisionReport修改")
//  @ApiExtension(author = "code generator", update = "2023-09-11 15:29")
//  @RequestBodyModel({
//    @Param(name = "id", value = "ID", required = true),
//    @Param(name = "aaa", value = "aaa字段", dataType = Integer.class),
//    @Param(name = "bbb", value = "bbb字段")
//  })
//  @PutMapping(value = "/user/userVisionReport", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<UserVisionReport> update(@RequestBody ParamMap params) {
//    return new ResponseEntity<>(userVisionReportService.save(params), HttpStatus.CREATED);
//  }

//  @Operation(summary="UserVisionReport删除")
//  @ApiExtension(author = "code generator", update = "2023-09-11 15:29")
//  @RequestModel({
//    @Param(name = "ids", value = "ID多个以逗号分隔", required = true, requestScope = Scope.PATH)
//  })
//  @DeleteMapping(value = "/user/userVisionReport/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<Integer> delete(@PathVariable("ids") String ids) {
//    Set<String> idSet = SetsHelper.toStringSets(ids, ",");
//    int count = idSet.isEmpty() ? 0 : userVisionReportService.deleteInBatch(idSet);
//    return new ResponseEntity<>(count, HttpStatus.CREATED);
//  }
}
