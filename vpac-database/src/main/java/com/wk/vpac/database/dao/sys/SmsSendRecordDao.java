package com.wk.vpac.database.dao.sys;


import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.wk.vpac.domain.sys.SmsSendRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * SmsSendRecord DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-22
 */
@Repository
public interface SmsSendRecordDao extends GenericJpaDao<SmsSendRecord, String> {

  /**
   * 统计未发送的短信记录
   * @param id
   * @return
   */
  @Query(value = "select count(1) from base_sms_send_record where sms_id=:smsId and status in  (0,2)",nativeQuery = true)
  long countUnSendRecord(@Param("smsId") String id);

  @Query(value="delete from base_sms_send_record where sms_id =:smsId",nativeQuery = true)
  @Modifying
  void deleteRecords(@Param("smsId") String smsId);

  default DataPage<Map> pageRecord(Map<String, String> params) {
    int pageNum = Pages.Helper.pageNum(params.get("pageNum"));
    int pageSize = Pages.Helper.pageSize(params.get("pageSize"));
    String querySql = "select  "
      + "a.id,a.sms_id as smsId,a.template_id as templateId,a.template_name as templateName,a.user_id as userId,a.phone,a.status,a.send_time as sendTime,a.create_time as createTime,a.batch,a.user_name as userName,a.remarks, "
      + "b.last_login_time as lastLoginTime  " + "from base_sms_send_record a  " + "left join base_user_base_info b  "
      + "on a.user_id=b.id  " + "where 1=1 ";
    String countSql = "select  " + "count(1)" + "from base_sms_send_record a  " + "left join base_user_base_info b  "
      + "on a.user_id=b.id  " + "where 1=1 ";

    NativeSQLBuilder sqlBuilder = new NativeSQLBuilder();
    //短信id
    String smsId = ConvertUtil.convertNullable(params.get("smsId"), String.class);
    if (StringUtils.isNotEmpty(smsId)) {
      sqlBuilder.and().addWhere("a", "sms_id", smsId, ConditionEnum.OPERATE_EQUAL);
    }
    //手机号
    String phone = ConvertUtil.convertNullable(params.get("searchPhone"), String.class);
    if (StringUtils.isNotEmpty(phone)) {
      sqlBuilder.and().addWhere("a", "phone", phone, ConditionEnum.OPERATE_RIGHT_LIKE);
    }
    //用户名
    String userName = ConvertUtil.convertNullable(params.get("searchUserName"), String.class);
    if (StringUtils.isNotEmpty(userName)) {
      sqlBuilder.and().addWhere("a", "user_name", userName, ConditionEnum.OPERATE_RIGHT_LIKE);
    }
    //状态
    Integer status = ConvertUtil.convertNullable(params.get("searchStatus"), Integer.class);
    if (status != null && status != -1) {
      sqlBuilder.and().addWhere("a", "status", status, ConditionEnum.OPERATE_EQUAL);
    }
    String lastLoginStartDate = ConvertUtil.convertNullable(params.get("lastLoginStartDate"), String.class);
    if (StringUtils.isNotEmpty(lastLoginStartDate)) {
      sqlBuilder.and().addWhere("b", "last_login_time", lastLoginStartDate, ConditionEnum.OPERATE_GREATER_EQUAL,"lastLoginStartDate");
    }
    String lastLoginEndDate = ConvertUtil.convertNullable(params.get("lastLoginEndDate"), String.class);
    if (StringUtils.isNotEmpty(lastLoginEndDate)) {
      sqlBuilder.and().addWhere("b", "last_login_time", lastLoginEndDate, ConditionEnum.OPERATE_LESS_EQUAL,"lastLoginEndDate");
    }

    sqlBuilder.bindQuerySql(querySql).orderBy(" a.send_time desc ");
    jakarta.persistence.Query listQuery = setMapResult(sqlBuilder.build(getEntityManager()));
    sqlBuilder.bindQuerySql(countSql);
    jakarta.persistence.Query countQuery = sqlBuilder.build(getEntityManager());

    return pageByQuery(countQuery, listQuery, pageNum, pageSize);
  }
}

