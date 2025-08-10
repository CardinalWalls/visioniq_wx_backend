package com.wk.vpac.main.service.admin.sys;

import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.wk.vpac.common.constants.sys.DictionaryDataType;
import com.wk.vpac.database.dao.sys.DictionaryDao;
import com.wk.vpac.database.dao.sys.DictionaryDataDao;
import com.wk.vpac.database.service.dictionary.DictionaryRefreshService;
import com.wk.vpac.domain.sys.Dictionary;
import com.wk.vpac.domain.sys.DictionaryData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Dictionary Service
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class DictionaryService extends AbstractJpaService<Dictionary, String, DictionaryDao> {
  @Autowired
  private DictionaryDataDao dictionaryDataDao;
  @Autowired
  private DictionaryRefreshService dictionaryRefreshService;

  /**
   * Dictionary新增和修改
   *
   * @param params
   *
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  public Dictionary saveOrUpdate(Map<String, String> params) {
    Dictionary dictionary = ConvertUtil.populate(new Dictionary(), params);
    dictionary.setDictCode(dictionary.getDictCode() != null ? dictionary.getDictCode().trim() : "");
    dictionary.setDictName(dictionary.getDictName() != null ? dictionary.getDictName().trim() : "");
    if (StringUtils.isEmpty(dictionary.getId())) {
      int countSame = getDao().countSameInsert(dictionary.getDictCode(), dictionary.getDictName());
      Assert.isTrue(countSame == 0, "已存在同code或者同name的记录");
      Integer no = getDao().maxOrderNo();
      if (no == null) {
        no = 0;
      } else {
        no = no + 1;
      }
      dictionary.setOrderNo(no);
    } else {
      //判断是否有重复记录
      int countSame = getDao().countSameUpdate(dictionary.getId(), dictionary.getDictCode(), dictionary.getDictName());
      Assert.isTrue(countSame == 0, "已存在同code或者同name的记录");
      //判断类型
      if (DictionaryDataType.SINGLE.toString().equals(dictionary.getDataType())) {
        long c = dictionaryDataDao
          .count(ConditionGroup.build().addCondition("dictId", ConditionEnum.OPERATE_EQUAL, dictionary.getId()));
        Assert.isTrue(c <= 1, DictionaryDataType.SINGLE.toString() + "类型只能添加一个数据，已存在多个数据！");
      }

    }
    getDao().save(dictionary);
    refreshDictionaryCache();
    return dictionary;
  }

  /**
   * Dictionary删除
   *
   * @param ids
   */
  @Transactional(rollbackFor = Exception.class)
  public void delete(Iterable<String> ids) {
    List<Dictionary> list = getDao().findAllById(ids);
    if (!CollectionUtils.isEmpty(list)) {
      getDao().deleteInBatch(list);
    }
    List<DictionaryData> listData = dictionaryDataDao
      .findAll(ConditionGroup.build().addCondition("dictId", ConditionEnum.OPERATE_IN, (Serializable) ids));
    if (!CollectionUtils.isEmpty(listData)) {
      dictionaryDataDao.deleteInBatch(listData);
    }
    refreshDictionaryCache();
  }

  /**
   * 刷新字典缓存
   */
  public void refreshDictionaryCache() {
    dictionaryRefreshService.refreshDictionary();
  }


}