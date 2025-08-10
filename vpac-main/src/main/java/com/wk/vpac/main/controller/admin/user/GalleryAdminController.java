package com.wk.vpac.main.controller.admin.user;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.google.common.collect.Sets;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.user.Gallery;
import com.wk.vpac.main.service.admin.user.GalleryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Gallery Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-06-30
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class GalleryAdminController {

  @Autowired
  private GalleryService galleryService;


  @GetMapping(value = "/user/gallery/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity page(@RequestParam Map<String, String> params) {
    String userId = TokenThreadLocal.getTokenObjNonNull().objId().toString();
    params.put("userId",userId);
    return new ResponseEntity<>(galleryService.page(params), HttpStatus.OK);
  }

  @GetMapping(value = "/user/gallery/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Gallery> findById(@PathVariable String id) {
    return new ResponseEntity<>(galleryService.findById(id), HttpStatus.OK);
  }

  @PostMapping(value = "/user/gallery/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity save(@RequestBody Map<String, String> params) {
    String userId = TokenThreadLocal.getTokenObjNonNull().objId().toString();
    params.put("userId",userId);
    return new ResponseEntity<>(JsonResult.success(galleryService.save(params)), HttpStatus.OK);
  }

  @PostMapping(value = "/user/gallery/del", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity delete(@RequestBody Map<String,String> params) {
    String deleteId = ConvertUtil.checkNotNull(params, "id", "删除id不能为空", String.class);
    String[] idsArray = StringUtils.split(deleteId, ",");
    int count = 0;
    if(idsArray != null && idsArray.length > 0){
      count = galleryService.deleteInBatch(Sets.newHashSet(idsArray));
    }

    return new ResponseEntity<>(JsonResult.success(count), HttpStatus.OK);
  }
}
