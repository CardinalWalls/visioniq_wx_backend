

package com.wk.vpac.main.service.admin.message;

import com.base.components.common.dto.page.DataPage;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.wk.vpac.common.constants.KnowStatus;
import com.wk.vpac.database.dao.sys.SysMessageDao;
import com.wk.vpac.domain.sys.SysMessage;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 系统消息service
 *
 * @author <a href="tecyun@foxmail.com">Huangyunyang</a>
 * @version 1.0.0, 2018/3/20 0020 15:23
 */
@Service
public class SysMessageService extends AbstractJpaService<SysMessage, String, SysMessageDao> {

  public long countUnReadMessage() {
    return getDao().countByUnread(KnowStatus.UNREAD.getStatus());
  }

  public DataPage<Map<String,Object>> findDetailPage(Map<String,String> searchParam){
    return getDao().findDetailPage(searchParam);
  }
}
