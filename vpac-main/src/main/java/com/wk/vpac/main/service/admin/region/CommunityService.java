package com.wk.vpac.main.service.admin.region;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.wk.vpac.database.dao.region.CommunityDao;
import com.wk.vpac.domain.region.Community;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;

/**
 * Community Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class CommunityService extends AbstractJpaService<Community, String, CommunityDao> {

  public DataPage<Map<String, Object>> page(Map<String, String> params){
    return getDao().page(params);
  }

  public Community save(Map<String, String> params){
    String id = ConvertUtil.convert(params.get("id"), "");
    Community community;
    Date now = new Date();
    if(StringUtils.isBlank(id)){
      community = ConvertUtil.populate(new Community(), params, false, "id");
      community.setCreateTime(now);
    }else{
      Community exists = getDao().findById(id).orElseThrow(()->new IllegalArgumentException("未找到数据"));
      community = ConvertUtil.populate(exists, params, false, "id");
    }
    Assert.hasText(community.getExpertId(), ()->"请选择一个专家");
    community.setUpdateTime(now);
    return TransactionActivation.of(()->saveAndFlush(community)).start();
  }

}