package com.wk.vpac.main.controller.admin.region;

import com.base.components.common.dto.sys.JsonResult;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.main.service.admin.region.CommunityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Community Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-02-15
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class CommunityController {

  private final CommunityService communityService;

  public CommunityController(CommunityService communityService){
    this.communityService = communityService;
  }

  @GetMapping(value = "/region/community/index")
  public String index(ModelMap model) {
    return "region/community/index";
  }

  @GetMapping(value = "/region/community/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> page(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(communityService.page(params), HttpStatus.OK);
  }

  @PostMapping(value = "/region/community/save", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> save(@RequestBody Map<String, String> params) {
    return new ResponseEntity<>(JsonResult.success(communityService.save(params)), HttpStatus.OK);
  }
}
