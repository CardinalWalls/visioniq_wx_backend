package com.wk.vpac.main.service.api.region;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.wk.vpac.common.token.user.UserToken;
import com.wk.vpac.database.dao.region.CommunityDao;
import com.wk.vpac.database.dao.user.UserBaseInfoDao;
import com.wk.vpac.domain.region.Community;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Community Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class CommunityService extends AbstractJpaService<Community, String, CommunityDao> {
  private final UserBaseInfoDao userBaseInfoDao;

  public DataPage<Map<String, Object>> page(Map<String, String> params){
    return getDao().page(params);
  }

  public Community save(Map<String, String> params){
    String id = ConvertUtil.convert(params.get("id"), "");
    Community community;
    if(StringUtils.isBlank(id)){
      community = ConvertUtil.populate(new Community(), params, false, "id");
    }else{
      Community exists = getDao().findById(id).orElseThrow(()->new IllegalArgumentException("未找到数据"));
      community = ConvertUtil.populate(exists, params, false, "id");
    }
    return TransactionActivation.of(()->saveAndFlush(community)).start();
  }

//  public Community bind(String id){
//    Community community = findById(id);
//    Assert.isTrue(community != null && community.getValid(), ()->"未找到社区信息");
//    TransactionActivation.start(()->{
//      userBaseInfoDao.bindCommunityId(TokenThreadLocal.getTokenObjNonNull(UserToken.class).getUserId(), id);
//      userBaseInfoDao.bindExpertId(TokenThreadLocal.getTokenObjNonNull(UserToken.class).getUserId(), community.getExpertId());
//      return null;
//    });
//    return community;
//  }

  public CommunityService(UserBaseInfoDao userBaseInfoDao) {
    this.userBaseInfoDao = userBaseInfoDao;
  }
}