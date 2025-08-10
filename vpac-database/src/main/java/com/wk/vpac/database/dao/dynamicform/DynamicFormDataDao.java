package com.wk.vpac.database.dao.dynamicform;

import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.wk.vpac.domain.dynamicform.DynamicFormData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DynamicFormData DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-15
 */
@Repository
public interface DynamicFormDataDao extends GenericJpaDao<DynamicFormData, String> {

  @Modifying
  @Query(value = "UPDATE base_dynamic_form_data SET status = :status, result = :result, update_time = NOW() WHERE id=:id", nativeQuery = true)
  int updateStatus(@Param("id") String id, @Param("status") int status, @Param("result") String result);

  @Modifying
  @Query(value = "UPDATE base_dynamic_form_data SET remark = :remark, update_time = NOW() WHERE id=:id", nativeQuery = true)
  int updateRemark(@Param("id") String id, @Param("remark") String remark);

  default DataPage<Map> page(String code, String title, Map<String, String> params, List<String> types, Set<String> columnsSet) {
    int pageNum = Pages.Helper.pageNum(params.get("pageNum"));
    int pageSize = Pages.Helper.pageSize(params.get("pageSize"));
    String colm = "";
    String maxCol = "";
    for (String type : types) {
      colm += ",ifnull(ch.`" + type + "`,0) `count_"+type+"`";
      maxCol += " ,MAX( CASE t.type WHEN '"+type+"' THEN t.count ELSE 0 END ) '"+type+"'";
    }
    String querySql = "SELECT d.id, d.`code`, d.title, d.json_data jsonData,d.create_time createTime, d.update_time updateTime, d.status, "
      + "u.phone userPhone, u.user_nick_name userNickName, u.avatar userAvatar, u.wx_name wxName, u.wx_img wxImg, u.id userId, d.remark, d.result "
      + colm
      + " FROM  base_dynamic_form_data d left join base_user_base_info u ON u.id = d.user_id left join  ( SELECT t.ref_id"
      + maxCol
      + " FROM ( SELECT c.ref_id,c.type, count( 1 ) count FROM base_dynamic_data_child c  GROUP BY c.ref_id,c.type ) t  "
      + " GROUP BY  t.ref_id) ch  ON d.id=ch.ref_id WHERE  1 = 1";
    String countSql = "select count(1) from base_dynamic_form_data d left join base_user_base_info u ON u.id = d.user_id where 1=1";

    NativeSQLBuilder nativeSQLBuilder = NativeSQLBuilder.defaultAnd();
    nativeSQLBuilder.and().addWhere("d", "code", StringUtils.isBlank(code) ? "" : code, ConditionEnum.OPERATE_EQUAL);
    if (StringUtils.isNotBlank(title)) {
      nativeSQLBuilder.and().addWhere("d", "title", title, ConditionEnum.OPERATE_RIGHT_LIKE);
    }
    Integer status = ConvertUtil.convert(params.get("status"), Integer.class);
    if(status != null){
      nativeSQLBuilder.and().addWhere("d", "status", status, ConditionEnum.OPERATE_EQUAL);
    }
    nativeSQLBuilder.linkAnd().addWhereEq("u", "phone", params.get("userPhone"), true);
    String userNickName = params.get("userNickName");
    if(StringUtils.isNotBlank(userNickName)){
      nativeSQLBuilder.andGroupStart().addWhereRightLike("u", "user_nick_name", userNickName)
                      .or()
                      .addWhereRightLike("u", "wx_name", userNickName).groupEnd();
    }

    columnsSet.forEach(c -> {
      String v = params.get(c);
      if(StringUtils.isNotBlank(v)){
        nativeSQLBuilder.and()
                        .addWhere("d", "json_data->'$." + c + "'", v,
                                  ConditionEnum.OPERATE_LIKE, "json_" + c);
      }
    });

    nativeSQLBuilder.bindQuerySql(querySql).orderBy(" d.create_time desc");
    jakarta.persistence.Query listQuery = setMapResult(nativeSQLBuilder.build(getEntityManager()));
    nativeSQLBuilder.bindQuerySql(countSql);
    jakarta.persistence.Query countQuery = nativeSQLBuilder.build(getEntityManager());
    return pageByQuery(countQuery, listQuery, pageNum, pageSize);
  }
}

