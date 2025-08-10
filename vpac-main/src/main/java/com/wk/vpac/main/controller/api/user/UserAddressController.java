package com.wk.vpac.main.controller.api.user;

import com.base.components.common.doc.Scope;
import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestBodyModel;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.Token;
import com.base.components.common.doc.validation.EL;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.user.UserAddress;
import com.wk.vpac.main.service.api.user.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * UserAddress Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-12-26
 */
@SystemLogger
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "用户中心-地址管理")
public class UserAddressController {

  @Autowired
  private UserAddressService userAddressService;

  @Operation(summary="地址列表", description = "查询当前用户的用户地址列表")
  @ApiExtension(author = "李赓", update = "2019-01-03 13:59", token = @Token)
  @GetMapping(value = "/user/address/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UserAddress>> findList(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(userAddressService.findList(), HttpStatus.OK);
  }

  @Operation(summary="地址查询", description = "查询单个地址")
  @ApiExtension(author = "李赓", update = "2019-01-03 13:59", token = @Token)
  @RequestModel(@Param(name = "id", value = "地址ID", required = true, requestScope = Scope.PATH, checkEL = EL.NOT_BLANK))
  @GetMapping(value = "/user/address/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserAddress> findOne(@PathVariable("id") String id) {
    return new ResponseEntity<>(userAddressService.findById(id), HttpStatus.OK);
  }

  @Operation(summary="地址新增", description = "新增用户地址")
  @ApiExtension(author = "李赓", update = "2019-01-09 09:45", token = @Token)
  @RequestBodyModel({
    @Param(name = "regionId", value = "地区ID", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "regionIds", value = "地区id json数组字符串", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "regionName", value = "地区省市区名称"),
    @Param(name = "receiveName", value = "收件人", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "receivePhone", value = "收件人手机", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "address", value = "详细地址", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "valid", value = "是否默认 否0,是1", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "longitude", value = "定位经度"),
    @Param(name = "latitude", value = "定位纬度"),
    @Param(name = "fullAddress", value = "地址全称")
  })
  @PostMapping(value = "/user/address", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserAddress> save(@RequestBody Map<String, String> params) {
    UserAddress userAddress = ConvertUtil.populate(new UserAddress(), params);
    if (StringUtils.isBlank(userAddress.getLatitude())) {
      userAddress.setLatitude("0.0");
    }
    if (StringUtils.isBlank(userAddress.getLongitude())) {
      userAddress.setLongitude("0.0");
    }
    if (userAddress.getFullAddress() == null) {
      userAddress.setFullAddress("");
    }
    return new ResponseEntity<>(userAddressService.add(userAddress), HttpStatus.CREATED);
  }
  @Operation(summary="地址修改", description = "地址修改")
  @ApiExtension(author = "李建", update = "2019-09-24 09:45", token = @Token)
  @RequestBodyModel({
    @Param(name = "id", value = "地址id", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "regionId", value = "地区ID", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "regionIds", value = "地区id json数组字符串", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "regionName", value = "地区省市区名称"),
    @Param(name = "receiveName", value = "收件人", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "receivePhone", value = "收件人手机", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "address", value = "详细地址", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "valid", value = "是否默认 否0,是1", required = true, checkEL = EL.NOT_BLANK),
    @Param(name = "longitude", value = "定位经度"),
    @Param(name = "latitude", value = "定位纬度"),
    @Param(name = "fullAddress", value = "地址全称")
  })
  @PutMapping(value = "/user/address", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserAddress> update(@RequestBody Map<String, String> params) {
    UserAddress userAddress = ConvertUtil.populate(new UserAddress(), params);
    userAddress.setUserId(TokenThreadLocal.getTokenObjNonNull().objId().toString());
    if (StringUtils.isBlank(userAddress.getLatitude())) {
      userAddress.setLatitude("0.0");
    }
    if (StringUtils.isBlank(userAddress.getLongitude())) {
      userAddress.setLongitude("0.0");
    }
    if (userAddress.getFullAddress() == null) {
      userAddress.setFullAddress("");
    }
    return new ResponseEntity<>(userAddressService.updateAddress(userAddress), HttpStatus.OK);
  }

  @Operation(summary="地址设置为默认使用")
  @ApiExtension(author = "李赓", update = "2019-01-09 09:45", token = @Token)
  @RequestModel(@Param(name = "id", value = "地址ID", required = true, requestScope = Scope.PATH, checkEL = EL.NOT_BLANK))
  @PutMapping(value = "/user/address/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserAddress> setValid(@PathVariable("id") String id) {
    userAddressService.validTrue(TokenThreadLocal.getTokenObjNonNull().objId().toString(), id);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Operation(summary="地址删除", description = "删除单个地址")
  @ApiExtension(author = "李赓", update = "2019-01-03 13:59", token = @Token)
  @RequestModel(@Param(name = "id", value = "地址ID", required = true, requestScope = Scope.PATH, checkEL = EL.NOT_BLANK))
  @DeleteMapping(value = "/user/address/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> deleteOne(@PathVariable("id") String id) {
    userAddressService.deleteById(id);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
