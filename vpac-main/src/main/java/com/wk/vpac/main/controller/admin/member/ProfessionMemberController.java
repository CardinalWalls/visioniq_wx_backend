package com.wk.vpac.main.controller.admin.member;

import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.token.RequireToken;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.token.user.UserMemberToken;
import com.wk.vpac.main.service.admin.member.SysMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * ProfessionMemberController
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2018-03-17 10:33
 */
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class ProfessionMemberController {
  @Autowired
  private SysMemberService sysMemberService;



  /**
   * 部门成员查询
   * @param params
   * <p>  userMemberName                      - Nullable   - Str - 部门名称
   * <p>  userNickName                        - Nullable   - Str - 用户名称
   * @return
   */
  @RequireToken({UserMemberToken.class})
  @GetMapping(value = {"/profession-member/coupon/user"})
  public ResponseEntity listServiceUser(@RequestParam Map<String, String> params) {
    DataPage page;
    page = sysMemberService.couponListNormalUser(params.get("normalUserPhone"), Pages.Helper.pageable(params, null));
    return ResponseEntity.ok(page);
  }
}
