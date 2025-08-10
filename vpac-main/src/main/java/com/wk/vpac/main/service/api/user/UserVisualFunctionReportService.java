package com.wk.vpac.main.service.api.user;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.token.TokenThreadLocal;
import com.wk.vpac.common.token.user.UserToken;
import com.wk.vpac.domain.user.UserVisualFunctionReport;
import com.wk.vpac.database.dao.user.UserVisualFunctionReportDao;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.exception.Exceptions;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Set;


/**
 * UserVisualFunctionReport Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class UserVisualFunctionReportService extends AbstractJpaService<UserVisualFunctionReport, String, UserVisualFunctionReportDao> {
  private final UserArchiveService userArchiveService;
  private final AttachmentService attachmentService;
  public UserVisualFunctionReportService(UserArchiveService userArchiveService, AttachmentService attachmentService) {
    this.userArchiveService = userArchiveService;
    this.attachmentService = attachmentService;
  }

  public DataPage<RowMap> page(PageParamMap params){
    if(StringUtils.isBlank(params.getStr("id"))){
      userArchiveService.checkArchive(params.getStrOrEmpty("userArchiveId"));
    }
    return getDao().page(params);
  }

  public UserVisualFunctionReport save(ParamMap params){
    String id = params.getStrTrimOrEmpty("id");
    params.dateNullable("inspectDate", Dates.DATE_FORMATTER_PATTERN, e->"检查日期" + e);
    Date now = new Date();
    UserVisualFunctionReport report;
    if(StringUtils.isBlank(id)){
      String userArchiveId = params.hasText("userArchiveId", () -> "请选择档案");
      Assert.isTrue(userArchiveService.existsById(userArchiveId), ()->"未找到档案数据");
      userArchiveService.checkArchive(userArchiveId);
      report = params.populate(new UserVisualFunctionReport(), false, "id");
      report.setUserArchiveId(userArchiveId);
      report.setCreateTime(now);
    }else{
      params.removes("userArchiveId");
      UserVisualFunctionReport exists = getDao().findById(id).orElseThrow(Exceptions.of(()->"未找到数据"));
      userArchiveService.checkArchive(exists.getUserArchiveId());
      report = params.populate(exists, false, "id");
    }
    report.setUpdateTime(now);
    report.setFileArray(attachmentService.persistentArray(params.getStr("fileArray")).toString());
    report.setEyegroundImg(attachmentService.persistentArray(params.getStr("eyegroundImg")).toString());
    report.setOctImg(attachmentService.persistentArray(params.getStr("octImg")).toString());
    report.setVisualFieldImg(attachmentService.persistentArray(params.getStr("visualFieldImg")).toString());
    return TransactionActivation.of(()->saveAndFlush(report)).start();
  }

  @Transactional(rollbackFor = Exception.class)
  public void delete(Set<String> ids){
    String userId = TokenThreadLocal.getTokenObjNonNull(UserToken.class).getUserId();
    if(!CollectionUtils.isEmpty(ids)){
      getDao().deleteByUserIdAndIdIn(userId, ids);
    }
  }
}