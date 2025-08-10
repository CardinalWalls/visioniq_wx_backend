package com.wk.vpac.main.controller.api.tags;

import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.dto.page.DataPage;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.tags.Tags;
import com.wk.vpac.main.service.admin.tags.TagsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * TagsController
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2020/5/18 0018 16:30
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "标签模块")
public class TagsController {

  @Autowired
  private TagsService tagsService;

  @Operation(summary = "标签分页列表")
  @ApiExtension(author = "蒋文", update = "2019-06-21 11:47")
  @RequestModel({
    @Param(name = "typeCode", value = "标签分类编码")
  })
  @ReturnModel(baseModel = DataPage.class, value = {
    @Param(name = "list[]", value = "类型名称",dataType = Tags.class,genericType = ArrayNode.class),
    @Param(name = "list[].typeName", value = "类型名称"),
    @Param(name = "list[].typeCode", value = "类型编码"),
  })
  @GetMapping(value = "/public/tags/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity tagsList(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(tagsService.page(params), HttpStatus.OK);
  }

}
