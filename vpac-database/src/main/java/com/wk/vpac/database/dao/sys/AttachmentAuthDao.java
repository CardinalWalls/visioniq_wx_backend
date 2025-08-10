package com.wk.vpac.database.dao.sys;

import com.base.components.common.dto.page.DataPage;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.wk.vpac.domain.sys.AttachmentAuth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * AttachmentAuth DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-03-27
 */
@Repository
public interface AttachmentAuthDao extends GenericJpaDao<AttachmentAuth, String> {

  @Query(value = "select user_id as userId, url from base_attachment_auth where attachment_id = :attachmentId",
    nativeQuery = true)
  List<Map<String, String>> findAuthList(@Param("attachmentId") String attachmentId);

  List<String> findUserIdById(String id);

  default DataPage<Map> searchCollectData(Integer fileType, String userId, int pageNum, int pageSize) {

    String querySql = "select a.id,a.file_type as fileType,a.url,a.remark from base_attachment a left join base_attachment_auth aa on a.id = "
      + " aa.attachment_id where 1=1 ";

    String countSql = "select count(1) from base_attachment a left join base_attachment_auth aa on a.id = aa.attachment_id where 1=1 ";

    NativeSQLBuilder sqlBuilder = new NativeSQLBuilder();
    if(fileType != null){
      sqlBuilder.and().addWhere("a", "file_type", fileType, ConditionEnum.OPERATE_EQUAL);
    }
    sqlBuilder.and().addWhere("aa", "user_id", userId, ConditionEnum.OPERATE_EQUAL);

    sqlBuilder.bindQuerySql(querySql).orderBy(" a.id");
    jakarta.persistence.Query listQuery = setMapResult(sqlBuilder.build(getEntityManager()));
    sqlBuilder.bindQuerySql(countSql);
    jakarta.persistence.Query countQuery = sqlBuilder.build(getEntityManager());

    return pageByQuery(countQuery, listQuery, pageNum, pageSize);
  }
}

