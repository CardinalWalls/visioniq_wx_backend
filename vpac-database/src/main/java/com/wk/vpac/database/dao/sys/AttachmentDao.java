

package com.wk.vpac.database.dao.sys;

import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.wk.vpac.domain.sys.Attachment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Attachment DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-03-15
 */
@Repository
public interface AttachmentDao extends GenericJpaDao<Attachment, String> {


  @Query(value = "SELECT a.id,a.url FROM base_attachment a\n"
    + "LEFT JOIN base_attachment_ref r ON r.attachment_id = a.id\n"
    + "WHERE r.ref_id=:refId AND r.ref_type=:type",nativeQuery = true)
  List<Map<String,String>> queryAttachment(@Param("refId") String refId, @Param("type") String type);
}

