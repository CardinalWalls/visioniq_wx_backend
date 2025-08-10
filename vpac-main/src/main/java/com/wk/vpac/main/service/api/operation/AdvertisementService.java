package com.wk.vpac.main.service.api.operation;

import com.base.components.common.constants.Valid;
import com.base.components.common.dto.page.DataPage;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.wk.vpac.database.dao.operations.AdvertisementDao;
import com.wk.vpac.domain.operations.Advertisement;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * AdvertisementService
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2018/4/10 0010 14:47
 */
@Service
public class AdvertisementService extends AbstractJpaService<Advertisement, String, AdvertisementDao> {
  public DataPage list(Map<String, String> params) {
    params.put("searchStatus", Valid.TRUE.getVal() + "");
    params.put("pageNum", "1");
    params.put("pageSize", "100");
    return getDao().list(params);
  }
}
