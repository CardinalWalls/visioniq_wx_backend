package com.wk.vpac.main.controller.api.region;

import com.base.components.common.doc.Scope;
import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.sys.Region;
import com.wk.vpac.main.service.api.region.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * RegionController
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2018/3/30 0030 16:23
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "通用接口")
public class RegionController {

  @Autowired
  RegionService regionService;


  @Operation(summary="查询地区子级列表")
  @ApiExtension(author = "李赓", update = "2019-01-02 14:44")
  @RequestModel(@Param(value = "父节点ID, 为空时查询根节点", name = "id"))
  @ReturnModel(baseModel = Region.class, isCollection = true)
  @GetMapping(value = "/region/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getRegionByParentId(@RequestParam Map<String, String> params) {
    String id = params.get("id");
    List<Region> res = regionService.getRegionByParentId(id);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @Operation(summary = "查询地区父级所有数据",
    description =  "查询已选地区每级所有其它数据，用于select级联，返回二维数组：[ [一级],[二级],[三级] ] ")
  @ApiExtension(author = "李赓", update = "2019-01-02 14:44")
  @RequestModel(@Param(value = "已选中地区ID", name = "id", requestScope = Scope.PATH))
  @ReturnModel(isCollection = true, value = {
    @Param(name = "id", value = "地区ID"),
    @Param(name = "name", value = "地区名称"),
    @Param(name = "selected", value = "已选中; 1=是、0=否", dataType = Integer.class),
  })
  @GetMapping(value = "/region/selected/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity findSelected(@PathVariable("id") String id) {
    return new ResponseEntity<>(regionService.findSelected(id), HttpStatus.OK);
  }


  @Operation(summary = "查询地区根据[省、市、区]名称")
  @ApiExtension(author = "李赓", update = "2019-01-02 14:44")
  @RequestModel({
    @Param(name = "name", value = "地区名", required = true),
    @Param(name = "level", value = "级别; 1=省，2=市，3=区", dataType = Integer.class, required = true)
  })
  @ReturnModel(isCollection = true, value = {
    @Param(name = "regionId", value = "regionId", required = true),
    @Param(name = "regionName", value = "regionName", required = true),
    @Param(name = "namePath", value = "namePath", required = true)
  })
  @GetMapping(value = "/region/by-name-level", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getRegionByNameAndLevel(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(regionService.getRegionByNameAndLevel(params), HttpStatus.OK);
  }


  @Operation(summary = "查询地区根据[省、市、区]名称")
  @ApiExtension(author = "李赓", update = "2019-01-02 14:44")
  @RequestModel({
    @Param(name = "province", value = "省", required = true),
    @Param(name = "city", value = "市", required = true),
    @Param(name = "district", value = "区", required = true)
  })
  @ReturnModel({
    @Param(name = "regionId", value = "regionId", required = true),
    @Param(name = "regionName", value = "regionName", required = true),
    @Param(name = "namePath", value = "namePath", required = true)
  })
  @GetMapping(value = "/region/by-full-name", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getRegionByName(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(regionService.getRegionByName(params), HttpStatus.OK);
  }

  @Operation(summary="查询所有市级（二级）列表")
  @ApiExtension(author = "蒋文", update = "2019-01-02 14:44")
  @RequestModel({})
  @ReturnModel(baseModel = Region.class, isCollection = true)
  @GetMapping(value = "/region/city", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getAllCity(@RequestParam Map<String, String> params) {
    List<Region> res = regionService.getAllCity();
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

}
