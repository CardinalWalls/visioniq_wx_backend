package com.wk.vpac.database.dao.sys;

import com.base.components.common.dto.page.DataPage;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * AttachmentAuthExtendDao DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-03-27
 */
@Repository
public interface AttachmentAuthExtendDao {

  DataPage<Map> searchCollectData(Integer fileType, String userId, int pageNum, int pageSize);
}

