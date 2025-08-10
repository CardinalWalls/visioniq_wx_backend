package com.wk.vpac.database.dao.chat;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.util.DynamicSqlBuilder;
import com.base.components.database.jpa.util.JpaHelper;
import com.wk.vpac.domain.chat.ChatMessage;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * ChatMessage DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2021-07-08
 */
@Repository
public interface ChatMessageDao extends GenericJpaDao<ChatMessage, String> {

  default DataPage<Map<String, Object>> page(Map<String, String> params){
    DynamicSqlBuilder sqlBuilder = DynamicSqlBuilder
      .create("wk_chat_message", "t")
      .joinTable("LEFT JOIN base_user_base_info u ON u.id = t.send_user_id", "u")
      .joinTable("LEFT JOIN wk_expert e ON e.user_id = u.id", "e","u")
      .select(JpaHelper.findColumns(ChatMessage.class, "t") + ", u.user_nick_name sendUserNickName, "
                + "u.real_name sendUserRealName, u.avatar sendUserAvatar, u.user_type sendUserType, "
                + "e.job_position expertJobPosition, e.title expertTitle, e.hospital expertHospital");
    sqlBuilder.addWhereEq("t", "group_id", params.get("groupId"), false);
    String userId = params.get("userId");
    if (StringUtils.isNotBlank(userId)) {
      sqlBuilder.addWhere("AND EXISTS (SELECT 1 FROM wk_chat_message_group_user gu WHERE gu.group_id = t.group_id AND gu.user_id = :userId)",
                             "userId", userId);
    }
    DateTime ge = ConvertUtil.dateNullable(params.get("createTimeGe"), "发送时间", Dates.DATE_TIME_FORMATTER);
    if(ge != null){
      sqlBuilder.addWhereGeEq("t", "create_time", ge.toDate());
    }
    DateTime le = ConvertUtil.dateNullable(params.get("createTimeLe"), "发送时间", Dates.DATE_TIME_FORMATTER);
    if(le != null){
      sqlBuilder.addWhereLeEq("t", "create_time", le.toDate());
    }
    String sortType = Sort.Direction.ASC.name().equalsIgnoreCase(params.get("sortType"))
                      ?Sort.Direction.ASC.name():Sort.Direction.DESC.name();
    return sqlBuilder.orderBy("t.id " + sortType).toPage(getEntityManager()).queryUnlimited(params);
  }
}

