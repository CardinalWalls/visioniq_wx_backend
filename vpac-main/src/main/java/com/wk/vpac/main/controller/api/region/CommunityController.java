package com.wk.vpac.main.controller.api.region;

import com.wk.vpac.main.service.api.region.CommunityService;

/**
 * Community Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-02-15
 */
//@RestController
//@RequestMapping(GatewayPath.API)
//@Tag(name = "社区模块")
public class CommunityController {

  private final CommunityService communityService;

  public CommunityController(CommunityService communityService){
    this.communityService = communityService;
  }

//  @Operation(summary="社区-分页")
//  @ApiExtension(author = "code generator", update = "2023-02-15 17:05")
//  @RequestModel({
//    @Param(name = "id", value = "社区ID，查单个时使用"),
//    @Param(name = "name", value = "社区名称"),
//    @Param(name = "streetName", value = "街道名称"),
//    @Param(name = "expertName", value = "专家名称"),
//    @Param(name = "pageNum", value = "当前页数; 默认1", dataType = Integer.class),
//    @Param(name = "pageSize", value = "每页行数; 默认30", dataType = Integer.class)
//  })
//  @ReturnModel(baseModel = DataPage.class, value = {
//    @Param(name = "list[]", value = "数据列表", dataType = ArrayType.class, genericType = Community.class),
//    @Param(name = "list[].regionName", value = "地区名称"),
//    @Param(name = "list[].expertName", value = "专家姓名"),
//    @Param(name = "list[].expertHospital", value = "专家的医院"),
//    @Param(name = "list[].expertDepartment", value = "专家的科室"),
//  })
//  @GetMapping(value = "/region/community/page", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<?> page(@RequestParam Map<String, String> params) {
//    params.put("valid", String.valueOf(true));
//    return new ResponseEntity<>(communityService.page(params), HttpStatus.OK);
//  }

//  @Operation(summary="社区-用户绑定")
//  @ApiExtension(author = "code generator", update = "2023-02-15 17:05", token = @Token)
//  @RequestModel({
//    @Param(name = "communityId", value = "社区ID", requestScope = Scope.PATH, required = true),
//  })
//  @PutMapping(value = "/region/community/bind/{communityId}", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<?> bind(@PathVariable("communityId") String communityId) {
//    return new ResponseEntity<>(communityService.bind(communityId), HttpStatus.OK);
//  }

}
