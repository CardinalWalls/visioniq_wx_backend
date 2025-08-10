package com.wk.vpac.main.service.admin.operation.sms;

import com.base.components.common.dto.page.DataPage;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.wk.vpac.database.dao.sys.SmsSendRecordDao;
import com.wk.vpac.domain.sys.SmsSendRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * SmsSendRecord Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class SmsSendRecordService extends AbstractJpaService<SmsSendRecord, String, SmsSendRecordDao> {

  /**
   * SmsSendRecord新增和修改
   *
   * @param smsSendRecord
   *
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  public SmsSendRecord saveOrUpdate(SmsSendRecord smsSendRecord) {
    return getDao().saveAndFlush(smsSendRecord);
  }

  /**
   * SmsSendRecord删除
   * @param ids
   */
  @Transactional(rollbackFor = Exception.class)
  public void delete(Iterable<String> ids) {
    List<SmsSendRecord> list = getDao().findAllById(ids);
    if(!CollectionUtils.isEmpty(list)){
      getDao().deleteInBatch(list);
    }
  }


  /**
   * 分页查询
   * @param params -
   * <p> pageNum                     - Nullable   - Int - 当前页
   * <p> pageSize                    - Nullable   - Int - 每页记录数
   * <p> smsId                       - Nullable   - Str - 短信id
   * <p> searchPhone                 - Nullable   - Str - 电话
   * <p> searchUserName              - Nullable   - Str - 用户名
   * <p> searchStatus                - Nullable   - Int - 状态
   *
   * @return
   */
  public DataPage pageRecord(Map<String,String> params){
    return getDao().pageRecord(params);
  }




}