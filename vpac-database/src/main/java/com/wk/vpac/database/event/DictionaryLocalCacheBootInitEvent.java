package com.wk.vpac.database.event;

import com.base.components.cache.AbstractLocalCacheBootInitEvent;
import com.base.components.cache.CacheManager;
import com.base.components.common.boot.SpringContextUtil;
import com.wk.vpac.database.service.dictionary.DictionaryRefreshService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DictionaryLocalCacheBootInitEvent
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2019-05-29 10:42
 */
public class DictionaryLocalCacheBootInitEvent extends AbstractLocalCacheBootInitEvent {
  @Autowired
  private DictionaryRefreshService dictionaryRefreshService;

  @Override
  protected void execute(CacheManager cacheManager) {
    dictionaryRefreshService.refreshDictionary();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    SpringContextUtil.addStartedEvents(this);
  }
}
