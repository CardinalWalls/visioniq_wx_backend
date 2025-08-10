package com.wk.vpac.main.controller.admin.sys;

import com.base.components.common.util.JsonUtils;
import com.google.common.collect.Lists;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.token.TokenType;
import com.wk.vpac.main.service.admin.sys.TokenLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * TokenLoginLog Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-09-02
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class TokenLoginLogController {

  @Autowired
  private TokenLoginLogService tokenLoginLogService;

  @GetMapping(value = "/sys/tokenLoginLog/index")
  public String index(ModelMap model) {
    List<String> tokenTypes = Lists.newArrayList();
    for (TokenType value : TokenType.values()) {
      tokenTypes.add(value.getTypeClass().getSimpleName());
    }
    try {
      model.put("tokenTypes", JsonUtils.toString(tokenTypes));
    } catch (Exception ignore) {
      model.put("tokenTypes", "[]");
    }
    return "sys/tokenLoginLog/index";
  }

  @GetMapping(value = "/sys/tokenLoginLog/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity page(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(tokenLoginLogService.page(params), HttpStatus.OK);
  }

  @GetMapping(value = "/sys/tokenRequestLog/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity requestPage(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(tokenLoginLogService.requestPage(params), HttpStatus.OK);
  }

}
