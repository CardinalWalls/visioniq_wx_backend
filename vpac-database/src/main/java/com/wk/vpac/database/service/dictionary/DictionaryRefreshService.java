package com.wk.vpac.database.service.dictionary;

import com.base.components.common.constants.Valid;
import com.base.components.common.dto.dictionary.DictionaryNode;
import com.base.components.common.service.cache.DictionaryCacheService;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.google.common.collect.Lists;
import com.wk.vpac.common.constants.sys.DictionaryDataType;
import com.wk.vpac.database.dao.sys.DictionaryDao;
import com.wk.vpac.database.dao.sys.DictionaryDataDao;
import com.wk.vpac.domain.sys.Dictionary;
import com.wk.vpac.domain.sys.DictionaryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DictionaryRefreshService
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2019-06-07 22:50
 */
@Service
@SuppressWarnings("all")
public class DictionaryRefreshService {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  @Autowired
  private DictionaryDao dictionaryDao;
  @Autowired
  private DictionaryDataDao dictionaryDataDao;
  @Autowired(required = false)
  private DictionaryCacheService dictionaryCacheService;

  public void refreshDictionary(){
    if(dictionaryCacheService == null){
      return;
    }
    List<Dictionary> listDict = dictionaryDao
      .findAll(ConditionGroup.build().addCondition("status", ConditionEnum.OPERATE_EQUAL, Valid.TRUE.getVal()));
    if (listDict != null) {
      List<DictionaryData> dictData = dictionaryDataDao
        .findAll(ConditionGroup.build().addCondition("status", ConditionEnum.OPERATE_EQUAL, Valid.TRUE.getVal()),
                 Sort.by(Sort.Direction.ASC, "dictId", "orderNo"));
      Map<String, List> dataMap = dictData.stream().collect(
        Collectors.groupingBy(DictionaryData::getDictId, HashMap::new, Collectors.toCollection(ArrayList::new)));
      listDict.forEach(dictionary -> {
        List data = dataMap.get(dictionary.getId());
        if(DictionaryDataType.LIST.name().equalsIgnoreCase(dictionary.getDataType())){
          dictionaryCacheService.rebuildList(dictionary.getDictCode(), data == null ? Lists.newArrayList() : data);
        }else{
          dictionaryCacheService.rebuildSingle(dictionary.getDictCode(),
                                               data == null || data.isEmpty() ? null : (DictionaryNode) data.get(0));
        }
      });
      logger.info("dictionary refresh to cache, size("+listDict.size()+")");
    }
  }
}
