package com.wk.vpac.main.service.admin.sys;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.google.common.collect.Sets;
import com.wk.vpac.database.dao.sys.KeywordLinkDao;
import com.wk.vpac.domain.sys.KeywordLink;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * KeywordLink Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class KeywordLinkService extends AbstractJpaService<KeywordLink, String, KeywordLinkDao> {

  public DataPage page(Map<String, String> params){
    return getDao().page(params);
  }

  @Transactional(rollbackFor = Exception.class)
  public KeywordLink save(Map<String, String> params){
    String id = ConvertUtil.convert(params.get("id"), "");
    KeywordLink keywordLink;
    if(StringUtils.isBlank(id)){
      keywordLink = ConvertUtil.populate(new KeywordLink(), params);
    }else{
      keywordLink = ConvertUtil.populate(findById(id), params);
    }
    return saveAndFlush(keywordLink);
  }

  public Pair<String, Set<KeywordLink>> replaceKeywords(String src){
    String after = src;
    Set<KeywordLink> sets = null;
    if(StringUtils.isNotBlank(src)){
      for (KeywordLink keywordLink : findAll()) {
        String temp = after.replace(keywordLink.getKeyword(),
                                    String.format("<a class=\"keyword\" target=\"_blank\" href=\"%s\">%s</a>",
                                                  keywordLink.getUrlLink(), keywordLink.getKeyword()));
        if(temp.length() != after.length()){
          after = temp;
          if(sets == null){
            sets = Sets.newHashSet();
          }
          sets.add(keywordLink);
        }
      }
    }
    return Pair.of(after, sets == null ? Collections.emptySet() : sets);
  }
}