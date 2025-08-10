package com.wk.vpac.database.dao.user;

import com.base.components.common.dto.page.DataPage;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.JpaHelper;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.wk.vpac.domain.user.Gallery;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Gallery DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2020-06-30
 */
@Repository
public interface GalleryDao extends GenericJpaDao<Gallery, String> {

  default DataPage page(Map<String, String> params){
    String querySql = "SELECT " + JpaHelper.findColumns(Gallery.class, "t") + " FROM mjc_gallery t WHERE 1=1";
    String countSql = "SELECT COUNT(1) FROM mjc_gallery t WHERE 1=1";
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    sqlBuilder.addWhereEq("t","user_id",params.get("userId"));
    jakarta.persistence.Query count = sqlBuilder.bindQuerySql(countSql).build(JpaHelper.getEntityManager());
    jakarta.persistence.Query query = JpaHelper.setMapResult(
      sqlBuilder.bindQuerySql(querySql).orderBy("t.create_time desc").build(JpaHelper.getEntityManager()));
    return JpaHelper.pageByQueryUnlimited(count, query, params);
  }

}

