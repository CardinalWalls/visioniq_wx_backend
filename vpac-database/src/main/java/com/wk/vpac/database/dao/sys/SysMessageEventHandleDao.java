package com.wk.vpac.database.dao.sys;

import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.wk.vpac.domain.msgqueue.SysMessageEventHandle;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SysMessageEventHandleDao的Dao接口
 *
 * @author : code tools
 * @version : 2.0
 * @date : 2018-08-08 09:29:13
 */
@Repository
public interface SysMessageEventHandleDao extends GenericJpaDao<SysMessageEventHandle, String> {

  @Query("delete from SysMessageEventHandle where eventId in (:eventIds)")
  @Modifying
  int deleteByEventIds(@Param("eventIds") List<String> ids);

}
