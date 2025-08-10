package com.wk.vpac.main.service.admin.operation.advertisement;

import com.base.components.common.constants.sys.Pages;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.wk.vpac.database.dao.operations.AdvertisementGroupDao;
import com.wk.vpac.domain.operations.AdvertisementGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * AdvertisementGroupService
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2018/4/8 0008 11:24
 */
@Service
public class AdvertisementGroupService extends
    AbstractJpaService<AdvertisementGroup, String, AdvertisementGroupDao> {

  /**
   * 分页查询
   *
   * @return
   */
  public Page<AdvertisementGroup> listPage(Map<String, String> params) {
    ConditionGroup<AdvertisementGroup> cg = ConditionGroup.build();
    return getDao().findAll(cg, Pages.Helper.pageable(params, Sort.by(Sort.Direction.DESC, "createTime")));
  }


  /**
   * 新增
   *
   * @param ag
   */
  @Transactional(rollbackFor = Exception.class)
  public void add(AdvertisementGroup ag) {
    ag.setCreateTime(new Date());
    getDao().save(ag);
  }

  /**
   * 修改
   *
   * @param ag
   */
  @Transactional(rollbackFor = Exception.class)
  public void update(AdvertisementGroup ag) {
    Optional<AdvertisementGroup> op = getDao().findById(ag.getId());
    Assert.isTrue(op.isPresent(), "未查到分组信息");
    AdvertisementGroup newpg = op.get();
    newpg.setStatus(ag.getStatus());
    newpg.setName(ag.getName());
    newpg.setTypeCode(ag.getTypeCode());
    getDao().save(newpg);
  }

  /**
   * 删除
   *
   * @param params
   */
  @Transactional(rollbackFor = Exception.class)
  public void delete(Map<String, String> params) {
    String id = ConvertUtil.checkNotNull(params, "id", "id不能为空", String.class);
    Optional<AdvertisementGroup> op = getDao().findById(id);
    Assert.isTrue(op.isPresent(), "未查到分组信息");
    AdvertisementGroup newpg = op.get();
    getDao().delete(newpg);
  }


}
