package com.wk.vpac.main.controller.admin.sys;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.SetsHelper;
import com.google.common.collect.Maps;
import com.wk.vpac.common.constants.sys.ChatAutoReplyType;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.main.service.admin.sys.SysAutoReplyService;
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

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * SysAutoReply Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-09-11
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class SysAutoReplyController {

  private final SysAutoReplyService sysAutoReplyService;

  public SysAutoReplyController(SysAutoReplyService sysAutoReplyService){
    this.sysAutoReplyService = sysAutoReplyService;
  }

  @GetMapping(value = "/sys/sysAutoReply/index")
  public String index(ModelMap model) throws IOException {
    Map<String, String> types = Maps.newLinkedHashMap();
    for (ChatAutoReplyType value : ChatAutoReplyType.values()) {
      if(value.isAutoReply()){
        types.put(String.valueOf(value.getVal()), value.getDesc());
      }
    }
    model.put("types", types);
    model.put("typesJson", JsonUtils.toString(types));
    return "sys/sysAutoReply/index";
  }

  @GetMapping(value = "/sys/sysAutoReply/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(PageParamMap params) {
    return new ResponseEntity<>(sysAutoReplyService.page(params), HttpStatus.OK);
  }

  @PostMapping(value = "/sys/sysAutoReply/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> save(@RequestBody ParamMap params) {
    return new ResponseEntity<>(JsonResult.success(sysAutoReplyService.save(params)), HttpStatus.OK);
  }

  @DeleteMapping(value = "/sys/sysAutoReply/del", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@RequestBody ParamMap params) {
    Set<String> ids = SetsHelper.toStringSets(params.getStrTrim("id"), ",");
    int count = ids.isEmpty() ? 0 : sysAutoReplyService.deleteInBatch(ids);
    return new ResponseEntity<>(JsonResult.success(count), HttpStatus.OK);
  }
}
