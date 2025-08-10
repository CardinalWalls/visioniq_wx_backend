package com.wk.vpac.main.controller.admin.sys;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.util.ConvertUtil;
import com.google.common.collect.Sets;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.main.service.admin.sys.KeywordLinkService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * KeywordLink Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2022-09-29
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class KeywordLinkController {

  @Autowired
  private KeywordLinkService keywordLinkService;

  @GetMapping(value = "/sys/keywordLink/index")
  public String index(ModelMap model) {
    return "sys/keywordLink/index";
  }

  @GetMapping(value = "/sys/keywordLink/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity page(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(keywordLinkService.page(params), HttpStatus.OK);
  }

  @PostMapping(value = "/sys/keywordLink/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity save(@RequestBody Map<String, String> params) {
    return new ResponseEntity<>(JsonResult.success(keywordLinkService.save(params)), HttpStatus.OK);
  }

  @DeleteMapping(value = "/sys/keywordLink/del", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity delete(@RequestBody Map<String,String> params) {
    String deleteId = ConvertUtil.checkNotNull(params, "id", "删除id不能为空", String.class);
    String[] idsArray = StringUtils.split(deleteId, ",");
    int count = 0;
    if(idsArray != null && idsArray.length > 0){
      count = keywordLinkService.deleteInBatch(Sets.newHashSet(idsArray));
    }
    return new ResponseEntity<>(JsonResult.success(count), HttpStatus.OK);
  }
}
