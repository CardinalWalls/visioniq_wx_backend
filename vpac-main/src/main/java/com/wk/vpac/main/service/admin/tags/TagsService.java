package com.wk.vpac.main.service.admin.tags;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.wk.vpac.database.dao.tags.TagsDao;
import com.wk.vpac.database.dao.tags.TagsRefDao;
import com.wk.vpac.domain.tags.Tags;
import com.wk.vpac.domain.tags.TagsRef;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Tags Service
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class TagsService extends AbstractJpaService<Tags, String, TagsDao> {

  @Autowired
  private TagsRefDao tagsRefDao;

  public DataPage<Map<String, Object>> simplePage(Map<String, String> params) {
    return getDao().simplePage(params);
  }


  public DataPage<Map<String, Object>> page(Map<String, String> params) {
    return getDao().page(params);
  }


  @Transactional(rollbackFor = Exception.class)
  public void tags(Map<String, String> params) {
    String id = ConvertUtil.checkNotNull(params, "id", "关联id不能为空", String.class);
    String tagsId = params.get("tagsId");

    List<TagsRef> oldList = tagsRefDao
      .findAll(ConditionGroup.build().addCondition("refId", ConditionEnum.OPERATE_EQUAL, id));
    if (oldList != null) {
      tagsRefDao.deleteAll(oldList);
    }
    if (StringUtils.isNotEmpty(tagsId)) {
      String[] tagsArr = tagsId.split(",");
      Date date = new Date();
      for (int i = 0; i < tagsArr.length; i++) {
        TagsRef tagsRef = new TagsRef();
        tagsRef.setCreateTime(date);
        tagsRef.setRefId(id);
        tagsRef.setTagsId(tagsArr[i]);
        tagsRefDao.save(tagsRef);
      }
    }
  }

}