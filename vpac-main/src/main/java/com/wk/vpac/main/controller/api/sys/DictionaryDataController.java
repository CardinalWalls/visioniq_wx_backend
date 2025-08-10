package com.wk.vpac.main.controller.api.sys;

import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.doc.validation.EL;
import com.base.components.common.dto.dictionary.DictionaryNode;
import com.base.components.common.service.cache.DictionaryCacheService;
import com.base.components.common.util.ConvertUtil;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.sys.DictionaryData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * DictionaryData Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-07-02
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "通用接口")
public class DictionaryDataController {

  @Autowired
  private DictionaryCacheService dictionaryCacheService;
  @Operation(summary="字典-列表查询")
  @ApiExtension(author = "李赓", update = "2019-01-09 09:29")
  @RequestModel({
    @Param(name = "dictCode", value = "字典编号(不区分大小写)", required = true, checkEL = EL.NOT_BLANK),
  })
  @ReturnModel(baseModel = DictionaryData.class, isCollection = true)
  @GetMapping(value = "/dictionary/data/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity dictionaryDataList(@RequestParam Map<String, String> params) {
    String dictCode = ConvertUtil.checkNotNull(params, "dictCode", "字典编号不能为空", String.class);
    return new ResponseEntity<>(dictionaryCacheService.list(dictCode), HttpStatus.OK);
  }

  @Operation(summary="字典-单个查询")
  @ApiExtension(author = "李赓", update = "2019-01-09 09:29")
  @RequestModel({
    @Param(name = "dictCode", value = "字典编号(不区分大小写)", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "dataKey", value = "数据key，当字典类型为SINGLE时，可以不填；当字典类型为LIST时，取对应dataKey的数据，不填则取列表第一个")
  })
  @ReturnModel(baseModel = DictionaryData.class)
  @GetMapping(value = "/dictionary/data/single", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity dictionaryDataSingle(@RequestParam Map<String, String> params) {
    String dictCode = ConvertUtil.checkNotNull(params, "dictCode", "字典编号不能为空", String.class);
    String dataKey = params.get("dataKey");
    dictCode = dictCode.trim();
    DictionaryNode single = null;
    try {
      single = dictionaryCacheService.single(dictCode);
    } catch (Exception ignore) {
    }
    try {
      if(single == null){
        List<DictionaryNode> list = dictionaryCacheService.list(dictCode);
        if(StringUtils.isNotBlank(dataKey)){
          for (DictionaryNode dictionaryNode : list) {
            if(dataKey.equals(dictionaryNode.getDataKey())){
              single = dictionaryNode;
              break;
            }
          }
        }
        else if(!list.isEmpty()){
          single = list.get(0);
        }
      }
    } catch (Exception ignore) {
    }
    return new ResponseEntity<>(single, HttpStatus.OK);
  }

}
