package com.wk.vpac.database.dao.user;


import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.ValidatorUtil;
import com.base.components.database.jpa.common.result.ResultConsumer;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import com.base.components.database.jpa.util.SelectPageHelper;
import com.wk.vpac.common.constants.UserType;
import com.wk.vpac.domain.user.Expert;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Expert DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2023-02-15
 */
@Repository
public interface ExpertDao extends GenericJpaDao<Expert, String> {

  default DataPage<Map<String, Object>> page(Map<String, String> params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder.create("wk_expert", "t");
    sqlBuilder.joinTable("LEFT JOIN base_user_base_info u ON u.id = t.user_id", "u");
    sqlBuilder.joinTable("LEFT JOIN base_user_base_info ru ON ru.user_type = "+ UserType.USER.getCode()+" AND ru.expert_id = t.id", "ru");
    sqlBuilder.select(findColumns(Expert.class, "t") + ",u.phone, u.real_name name, u.avatar, u.gender, COUNT(ru.id) refCount");
    sqlBuilder.addWhereLike("u", "real_name", params.get("name"));
    sqlBuilder.addWhereEq("u", "phone", params.get("phone"));
    sqlBuilder.addWhereLike("t", "title", params.get("title"));
    sqlBuilder.addWhereLike("t", "job_position", params.get("jobPosition"));
    sqlBuilder.addWhereLike("t", "hospital", params.get("hospital"));
    sqlBuilder.addWhereEq("t", "status", ConvertUtil.convert(params.get("status"), Integer.class));
    sqlBuilder.addWhereEq("t", "id", params.get("id"));
    SelectPageHelper.query(sqlBuilder, params, "t", "id", "u.phone", "u.real_name");
    ResultConsumer<Map<String, Object>> resultConsumer = null;
    if(ConvertUtil.convert(params.get("hidePhone"), false)){
      resultConsumer = row-> row.put("phone", ValidatorUtil.phoneInsensitive(row.getOrDefault("phone", "").toString()));
    }
    sqlBuilder.groupBy("t.id");
    if(!sqlBuilder.containsParamsKey("id")){
      return sqlBuilder.orderBy("t.id DESC").toPage(getEntityManager()).resultConsumer(resultConsumer).queryUnlimited(params);
    }
    return DataPage.nonPage(setMapResult(sqlBuilder.build(getEntityManager()), resultConsumer).getResultList());
  }

  Expert findByUserId(@Param("userId") String userId);
  @Query(value = "SELECT user_id FROM wk_expert WHERE id = :id", nativeQuery = true)
  String findUserIdById(@Param("id") String id);

  default String matching(String regionId){
    String[] id = new String[1];
    toRowResult(getEntityManager().createNativeQuery(
      "SELECT ((CASE WHEN region_id = :regionId THEN 0 ELSE 1 END) + RAND()) rd, id FROM wk_expert WHERE status = 1 ORDER BY rd"),
                 row->{
      if(id[0] == null){
        id[0] = row.getStr("id");
      }
    }).setMaxResults(1).setParameter("regionId", regionId).getResultList();
    Assert.hasText(id[0], ()->"系统未正确匹配您的专属医生，请联系平台客服");
    return id[0];
  }

  @Modifying
  @Query(value = "UPDATE wk_expert SET view_count = view_count + :addCount WHERE id = :id", nativeQuery = true)
  void incrementViewCount(@Param("id") String id, @Param("addCount") int addCount);

  @Query(value = "SELECT COUNT(1) FROM wk_user_archive WHERE expert_id = :id", nativeQuery = true)
  int countBind(@Param("id") String id);
}

