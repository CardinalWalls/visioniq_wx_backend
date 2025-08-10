package com.wk.vpac.main.controller.admin.region;

import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.dto.tree.TreeNodeData;
import com.base.components.common.dto.tree.TreeParam;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.util.ConvertUtil;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.sys.Region;
import com.wk.vpac.domain.sys.RegionTiny;
import com.wk.vpac.main.service.admin.region.RegionNodeBuilder;
import com.wk.vpac.main.service.admin.region.RegionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * RegionController
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2018/3/22 0022 16:31
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class RegionController {

  @Autowired
  RegionService regionService;

  @GetMapping(value = {"/region/index"})
  public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
    return "region/index";
  }

  @GetMapping(value = {"/region/tree"})
  @ResponseBody
  public ResponseEntity tree(HttpServletRequest request, HttpServletResponse response) {
    List<TreeNodeData<Region>> treeNodeData = regionService.loadTreeNodes(new TreeParam(request));
    return ResponseEntity.ok(treeNodeData);
  }

  @GetMapping(value = {"/region/member/tree"})
  @ResponseBody
  public ResponseEntity memberCountTree(HttpServletRequest request, HttpServletResponse response) {
    List<TreeNodeData<Region>> treeNodeData = regionService
      .loadTreeNodes(new TreeParam(request), RegionService.CountType.MEMBER);
    return ResponseEntity.ok(treeNodeData);
  }

  @PostMapping("/region/saveOrUpdate")
  @ResponseBody
  public ResponseEntity saveOrUpdate(Region region) {
    Region rs;
    if (StringUtils.isNotBlank(region.getId())) {
      rs = regionService.updateBaseInfo(region);
    } else {
      rs = regionService.addRegion(region);
    }
    return ResponseEntity.ok(JsonResult.success(RegionNodeBuilder.buildNode(rs, null)));
  }

  @PostMapping("/region/delete")
  @ResponseBody
  public ResponseEntity delete(String id) {
    regionService.delete(id);
    return ResponseEntity.ok(JsonResult.success());
  }

  @GetMapping(value = {"/region/all"})
  @ResponseBody
  public ResponseEntity citySelect(HttpServletRequest request, HttpServletResponse response) {
    return ResponseEntity.ok(JsonResult.success(regionService.allBycitySelect()));
  }

  @GetMapping(value = {"/region/tiny/all"})
  @ResponseBody
  public ResponseEntity tinyByCitySelect(HttpServletRequest request, HttpServletResponse response) {
    return ResponseEntity.ok(JsonResult.success(regionService.allTinyBycitySelect()));
  }

  @GetMapping(value = {"/region/tiny/page"})
  @ResponseBody
  public ResponseEntity pageRegionTiny(@RequestParam Map<String, String> params) {
    return ResponseEntity.ok(regionService.pageRegionTiny(ConvertUtil.convert(params.get("regionId"), ""), Pages.Helper
      .pageable(params, Sort.by(Direction.ASC, "orderNo"))));
  }

  @PostMapping(value = {"/region/tiny/saveOrUpdate"})
  @ResponseBody
  public ResponseEntity saveOrUpdate(RegionTiny tiny) {
    if (StringUtils.isNotBlank(tiny.getId())) {
      regionService.tinyUpdate(tiny);
    } else {
      regionService.tinySave(tiny);
    }
    return ResponseEntity.ok(JsonResult.success());
  }

  @PostMapping(value = {"/region/tiny/delete"})
  @ResponseBody
  public ResponseEntity tinyDelete(@RequestParam Map<String, String> params) {
    regionService.tinyDelete(ConvertUtil.checkNotNull(params, "id", String.class));
    return ResponseEntity.ok(JsonResult.success());
  }

  @PostMapping("/region/doMove")
  @ResponseBody
  public ResponseEntity doMove(String srcId, String targetId, String hitMode) {
    regionService.updateToMove(srcId, targetId, hitMode);
    return ResponseEntity.ok(JsonResult.success());
  }

  @PutMapping(value = "/region/ref", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity ref(@RequestBody Map<String, String> params) {
    regionService.regionRef(params);
    return new ResponseEntity<>(JsonResult.success(), HttpStatus.OK);
  }

  @GetMapping(value = {"/region/ref/list"})
  @ResponseBody
  public ResponseEntity refList(@RequestParam Map<String, String> params) {
    return ResponseEntity.ok(regionService.regionRefList(params));
  }

}
