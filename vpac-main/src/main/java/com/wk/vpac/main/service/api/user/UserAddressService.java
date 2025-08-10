package com.wk.vpac.main.service.api.user;

import com.base.components.common.token.TokenThreadLocal;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.wk.vpac.database.dao.user.UserAddressDao;
import com.wk.vpac.domain.user.UserAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UserAddress Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class UserAddressService extends AbstractJpaService<UserAddress, String, UserAddressDao> {

  @Transactional(rollbackFor = Exception.class)
  public UserAddress add(UserAddress userAddress) {
    String userId = TokenThreadLocal.getTokenObjNonNull().objId().toString();
    userAddress.setId(null);
    userAddress.setUserId(userId);
    UserAddress result = getDao().saveAndFlush(userAddress);
    if(userAddress.getValid()){
      validTrue(userId,result.getId());
    }
    return result;
  }

  /**
   * 查询当前用户使用的地址
   */
  public List<UserAddress> findList(){
    return getDao().findByUserId(TokenThreadLocal.getTokenObjNonNull().objId().toString());
  }

  @Transactional(rollbackFor = Exception.class)
  public void validTrue(String userId, String id) {
    getDao().updateDisable(userId);
    getDao().updateValid(id, true);
  }
  @Transactional(rollbackFor = Exception.class)
  public UserAddress updateAddress(UserAddress userAddress) {
    saveAndFlush(userAddress);
    if(userAddress.getValid()){
      validTrue(userAddress.getUserId(),userAddress.getId());
    }
    return userAddress;
  }
}