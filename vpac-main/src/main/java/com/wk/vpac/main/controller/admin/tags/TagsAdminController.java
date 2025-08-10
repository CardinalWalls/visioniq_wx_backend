package com.wk.vpac.main.controller.admin.tags;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Sets;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.tags.Tags;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import com.wk.vpac.main.service.admin.tags.TagsService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Map;

/**
 * Tags Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-01-03
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class TagsAdminController {

  @Autowired
  private TagsService tagsService;
  @Autowired
  private AttachmentService attachmentService;


  @GetMapping(value = "/tags/main/index")
  public String index(ModelMap model) {
    return "tags/main/index";
  }

  @GetMapping(value = "/tags/main/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity page(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(tagsService.page(params), HttpStatus.OK);
  }

  @GetMapping(value = "/tags/main/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Tags> findById(@PathVariable String id) {
    return new ResponseEntity<>(tagsService.findById(id), HttpStatus.OK);
  }

  @PostMapping(value = "/tags/main/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity save(@RequestBody Map<String, String> params) {
    String id = ConvertUtil.convert(params.get("id"), "");
    Tags tags ;
    if(StringUtils.isBlank(id)){
      tags = ConvertUtil.populate(new Tags(), params);
      tags.setCreateTime(new Date());
    }else{
      tags = ConvertUtil.populate(tagsService.findById(id), params);
    }

    // 持久化图片
    String imgJson = tags.getImgJson();
    if(StringUtils.isNotEmpty(imgJson)){
      try {
        ArrayNode imgArr = JsonUtils.reader(imgJson, ArrayNode.class);
        ArrayNode persistent = attachmentService
          .persistent(imgArr, false, TokenThreadLocal.getTokenObjNonNull().objId().toString());
        tags.setImgJson(persistent==null?"[]":persistent.toString());
      }catch (Exception e){
        e.printStackTrace();
      }
    }else{
      tags.setImgJson("[]");
    }

    tags = tagsService.saveAndFlush(tags);

    return new ResponseEntity<>(JsonResult.success(tags), HttpStatus.OK);
  }

  @DeleteMapping(value = "/tags/main/del", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity delete(@RequestBody Map<String,String> params) {
    String deleteId = ConvertUtil.checkNotNull(params, "id", "删除id不能为空", String.class);
    String[] idsArray = StringUtils.split(deleteId, ",");
    int count = 0;
    if(idsArray != null && idsArray.length > 0){
      count = tagsService.deleteInBatch(Sets.newHashSet(idsArray));
    }
    return new ResponseEntity<>(JsonResult.success(count), HttpStatus.OK);
  }


  @GetMapping(value = "/tags/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity tagsPage(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(tagsService.simplePage(params), HttpStatus.OK);
  }


  @PutMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity tags(@RequestBody Map<String, String> params) {
    tagsService.tags(params);
    return new ResponseEntity<>(JsonResult.success(), HttpStatus.OK);
  }

}
