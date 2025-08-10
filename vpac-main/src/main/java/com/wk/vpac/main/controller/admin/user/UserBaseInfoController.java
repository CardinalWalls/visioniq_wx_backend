package com.wk.vpac.main.controller.admin.user;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.dto.tree.TreeNodeData;
import com.base.components.common.dto.tree.TreeParam;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.token.RequireToken;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.ObjectTool;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.token.user.UserMemberToken;
import com.wk.vpac.domain.admin.SysMember;
import com.wk.vpac.domain.sys.Region;
import com.wk.vpac.domain.user.UserBaseInfo;
import com.wk.vpac.main.dto.member.AdminAuth;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import com.wk.vpac.main.service.admin.member.SysMemberService;
import com.wk.vpac.main.service.admin.region.RegionService;
import com.wk.vpac.main.service.admin.user.UserBaseInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * UserController
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2018/4/17 0017 10:20
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class UserBaseInfoController {
  @Autowired
  private UserBaseInfoService userBaseInfoService;
  @Autowired
  private AttachmentService attachmentService;
  @Autowired
  private RegionService regionService;
  @Autowired
  private SysMemberService sysMemberService;
  @Value("${base.admin.registry-user-search-days-limit:30}")
  private Integer registryUserSearchDaysLimit;


  @PostMapping(value = "/user/add",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public ResponseEntity addUser(@RequestParam Map<String, String> params,HttpServletRequest request) {
    return ResponseEntity.ok(JsonResult.success(userBaseInfoService.addUser(params,request)));
  }


  /**
   * 跳转到用户管理页面
   *
   * @param request
   * @param response
   * @param modelMap
   *
   * @return
   */
  @GetMapping(value = {"/user/manager/index"})
  public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
    modelMap.put("registTimeStart", DateTime.now().minusDays(registryUserSearchDaysLimit).withMillisOfDay(0)
                                            .toString(Dates.DATE_TIME_FORMATTER_PATTERN));
    modelMap.put("registTimeEnd",
                 DateTime.now().millisOfDay().withMaximumValue().toString(Dates.DATE_TIME_FORMATTER_PATTERN)
    );
    AdminAuth auth = sysMemberService.getCurrentAdminAuth();
    boolean superRole = auth.getSuperRole();
    modelMap.put("superRole", superRole ? 1 : 0);
    return "user/manager/index";
  }

  /**
   * 跳转到用户管理页面
   *
   * @param request
   * @param response
   * @param modelMap
   *
   * @return
   */
  @GetMapping(value = {"/user/manager/detail"})
  public String userDetail(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
    String id = request.getParameter("id");
    modelMap.put("id", id);
    modelMap.put("user", userBaseInfoService.findById(id));
    return "user/manager/detail";
  }

  /**
   * 分页查询
   *
   * @param params
   *
   * @return
   */
  @GetMapping(value = {"/user/manager/listpage"})
  @ResponseBody
  public ResponseEntity listPage(@RequestParam Map<String, String> params) throws IOException {
    return new ResponseEntity<>(userBaseInfoService.listPage(params), HttpStatus.OK);
  }


  /**
   * ajax返回用户基本详情页面
   *
   * @param userId
   * @param modelMap
   *
   * @return
   */
  @GetMapping(value = {"/user/manager/detail/base"})
  public String userDetail(String userId, ModelMap modelMap) {
    Assert.notNull(userId, "用户id不能为空");
    UserBaseInfo userBaseInfo = userBaseInfoService.findById(userId);
    userBaseInfo = ObjectTool
      .setOtherFieldsToNull(userBaseInfo, "avatar", "userNickName", "gender", "birth", "phone", "userEmail", "wxImg",
                         "wxName"
      );
    userBaseInfo.setAvatar(attachmentService.displayAtta(userBaseInfo.getAvatar()));
    modelMap.put("userBaseInfo", userBaseInfo);
    return "/user/manager/baseinfo";
  }

  /**
   * ajax返回用户服务页面
   *
   * @return
   */
  @RequireToken(UserMemberToken.class)
  @GetMapping(value = {"/user/manager/detail/service"})
  public String serviceDetail() {
    return "user/manager/serviceDetail";
  }



  /**
   * ajax返回用户基本详情页面
   *
   * @return
   */
  @GetMapping(value = {"/user/manager/detail/order"})
  public String order(String userId, ModelMap modelMap) {
    Assert.notNull(userId, "用户id不能为空");
    modelMap.put("userId", userId);
    return "user/manager/order";
  }


  @GetMapping(value = {"/user/region/tree"})
  @ResponseBody
  public ResponseEntity tree(HttpServletRequest request, HttpServletResponse response) {
    List<TreeNodeData<Region>> treeNodeData = regionService
      .loadTreeNodes(new TreeParam(request), RegionService.CountType.NORMAL_USER);
    return ResponseEntity.ok(treeNodeData);
  }


  @RequireToken(UserMemberToken.class)
  @GetMapping(value = "/user/auth/members",produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity queryOwnAuth(@RequestParam Map<String, String> params) {
    UserMemberToken tokenObj = TokenThreadLocal.getTokenObj(UserMemberToken.class);
    List<SysMember> members = sysMemberService.queryAuthMembersById(tokenObj.getId());
    return ResponseEntity.ok(JsonResult.success(members));
  }

  @RequireToken(UserMemberToken.class)
  @GetMapping(value = "/user/auth/members/regions",produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity queryOwnAuthMemberRegions(@RequestParam Map<String, String> params) {
    UserMemberToken tokenObj = TokenThreadLocal.getTokenObj(UserMemberToken.class);
    List<Region> members = sysMemberService.queryAuthMemberRegionsById(tokenObj.getId());

    return ResponseEntity.ok(JsonResult.success(members));
  }

  /**
   * 获取当前用户权限对象
   * @return
   */
  @RequireToken(UserMemberToken.class)
  @GetMapping(value = "/user/auth/info",produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity queryCurrentAuthInfo() {
    AdminAuth auth = sysMemberService.getCurrentAdminAuth();
    return ResponseEntity.ok(JsonResult.success(auth));
  }
  @RequireToken({UserMemberToken.class})
  @GetMapping(value = {"/user/list/page"})
  public ResponseEntity listUser(@RequestParam Map<String, String> params) {
    DataPage page;
    page = userBaseInfoService.querySimpleUser(params);
    return ResponseEntity.ok(page);
  }

  @PutMapping(value = "/user/parent",produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity updateParent(@RequestBody Map<String, String> params) {
    String userId = ConvertUtil.checkNotNull(params, "userId", "选择一个用户", String.class);
    String parentPhone = ConvertUtil.checkNotNull(params, "parentPhone", "请输入推荐人手机号", String.class);
    UserBaseInfo parent = userBaseInfoService.findByPhone(parentPhone);
    Assert.isTrue(!userId.equals(parent.getParentUserId()), "当前用户已经是此手机号（"+parentPhone+"）的推荐人，不能互相设置为推荐人");
    Assert.notNull(parent, "未找到推荐人：" + parentPhone);
    Assert.isTrue(!userId.equals(parent.getId()), "不能设置自己为推荐人");
    userBaseInfoService.updateParentUserId(userId, parent.getId());
    return ResponseEntity.ok(JsonResult.success());
  }
  @PutMapping(value = "/user/manager/level",produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity updateLevel(@RequestBody Map<String, String> params) {
    String userId = ConvertUtil.checkNotNull(params, "userId", "选择一个用户", String.class);
    Integer level = ConvertUtil.checkNotNull(params, "level", "请设置用户等级", Integer.class);
    userBaseInfoService.updateLevel(userId, level);
    return ResponseEntity.ok(JsonResult.success());
  }
}
