package com.wk.vpac.main.controller.api.operations;

import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.dto.page.DataPage;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.operations.Advertisement;
import com.wk.vpac.main.service.api.operation.AdvertisementService;
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
 * AdvertisementController
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2018/4/10 0010 14:45
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "广告模块")
public class AdvertisementController {


  @Autowired
  private AdvertisementService advertisementService;


  @Operation(summary="广告列表查询")
  @ApiExtension(author = "李赓", update = "2019-01-14 09:43")
  @RequestModel({
    @Param(name = "positionCode", value = "位置编码"),
    @Param(name = "tagsCode", value = "标签编码，多个以英文逗号隔开"),
  })
  @ReturnModel(baseModel = Advertisement.class, isCollection = true, value = {
    @Param(name = "groupName", value = "分组名称", required = true)
  })
  @GetMapping(value = "/advertisement/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity list(@RequestParam Map<String, String> params) {
    DataPage dp = advertisementService.list(params);
    return new ResponseEntity<>(dp.getList(), HttpStatus.OK);
  }

}
