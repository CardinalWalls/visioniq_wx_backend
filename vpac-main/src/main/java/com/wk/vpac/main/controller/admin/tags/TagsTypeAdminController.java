package com.wk.vpac.main.controller.admin.tags;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.util.ConvertUtil;
import com.google.common.collect.Sets;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.tags.TagsType;
import com.wk.vpac.main.service.admin.tags.TagsTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * TagsType Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-04-30
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class TagsTypeAdminController {

  @Autowired
  private TagsTypeService tagsTypeService;

  @GetMapping(value = "/tags/tagsType/index")
  public String index(ModelMap model) {
    return "tags/tagsType/index";
  }

  @GetMapping(value = "/tags/tagsType/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity page(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(tagsTypeService.page(params), HttpStatus.OK);
  }

  @GetMapping(value = "/tags/tagsType/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TagsType> findById(@PathVariable String id) {
    return new ResponseEntity<>(tagsTypeService.findById(id), HttpStatus.OK);
  }

  @PostMapping(value = "/tags/tagsType/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity save(@RequestBody Map<String, String> params) {
    return new ResponseEntity<>(JsonResult.success(tagsTypeService.save(params)), HttpStatus.OK);
  }

  @DeleteMapping(value = "/tags/tagsType/del", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity delete(@RequestBody Map<String,String> params) {
    String deleteId = ConvertUtil.checkNotNull(params, "id", "删除id不能为空", String.class);
    String[] idsArray = StringUtils.split(deleteId, ",");
    int count = 0;
    if(idsArray != null && idsArray.length > 0){
      count = tagsTypeService.deleteInBatch(Sets.newHashSet(idsArray));
    }
    return new ResponseEntity<>(JsonResult.success(count), HttpStatus.OK);
  }

  @GetMapping(value = "/tags/tagsType/simplePage", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity simplePage(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(tagsTypeService.simplePage(params), HttpStatus.OK);
  }

}
