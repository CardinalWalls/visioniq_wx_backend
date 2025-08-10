package com.wk.vpac.main.service.api.user;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.exception.Exceptions;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.wk.vpac.database.dao.user.UserInspectReportDao;
import com.wk.vpac.domain.user.UserArchive;
import com.wk.vpac.domain.user.UserInspectReport;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import com.wk.vpac.main.service.aliyun.OcrService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * UserInspectReport Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class UserInspectReportService extends AbstractJpaService<UserInspectReport, String, UserInspectReportDao> {
  private static final Logger logger = LoggerFactory.getLogger(UserInspectReportService.class);
  private static final Pattern NUMBER_PATTERN = Pattern.compile("(-?\\d+(\\.\\d+)?)|(-?\\.\\d+)");
  private final UserArchiveService userArchiveService;
  private final AttachmentService attachmentService;
  private final OcrService ocrService;

  public UserInspectReportService(UserArchiveService userArchiveService, AttachmentService attachmentService,
                                  OcrService ocrService) {
    this.userArchiveService = userArchiveService;
    this.attachmentService = attachmentService;
    this.ocrService = ocrService;
  }

  public DataPage<RowMap> page(PageParamMap params){
    if(StringUtils.isBlank(params.getStr("id"))){
      userArchiveService.checkArchive(params.getStrOrEmpty("userArchiveId"));
    }
    return getDao().page(params);
  }

  public UserInspectReport save(ParamMap params){
    String id = params.getStrTrimOrEmpty("id");
    params.dateNullable("inspectDate", Dates.DATE_FORMATTER_PATTERN, e->"检查日期" + e);
    Date now = new Date();
    UserInspectReport report;
    if(StringUtils.isBlank(id)){
      String userArchiveId = params.hasText("userArchiveId", () -> "请选择档案");
      Assert.isTrue(userArchiveService.existsById(userArchiveId), ()->"未找到档案数据");
      userArchiveService.checkArchive(userArchiveId);
      UserArchive archive = userArchiveService.findById(userArchiveId);
      Assert.notNull(archive, ()->"未找到档案数据");
      Assert.isTrue(!getDao().existsByUserArchiveIdAndInspectDate(archive.getId(), params.getStr("inspectDate")),
                    ()->"检查日期已存在，请勿重复录入");
      report = params.populate(new UserInspectReport(), false, "id");
//      report.setUserArchiveId(userArchiveId);
      report.setIdcard(archive.getIdcard());
      report.setCreateTime(now);
    }else{
      params.removes( "idCard");
      UserInspectReport exists = getDao().findById(id).orElseThrow(Exceptions.of(()->"未找到数据"));
//      userArchiveService.findArchive(TokenThreadLocal.getTokenObjNonNull(), exists.getIdcard());
      String userArchiveId = exists.getUserArchiveId();
      if(StringUtils.isBlank(userArchiveId)){
        userArchiveId = params.hasText("userArchiveId", ()->"请选择档案");
      }
      params.put("userArchiveId", userArchiveId);
      Assert.isTrue(!getDao().existsByUserArchiveIdAndInspectDateAndIdNot(userArchiveId,
                                                                          params.getStr("inspectDate"),
                                                                          exists.getId()),
                    ()->"检查日期已存在，请勿重复录入");
      report = params.populate(exists, false, "id");
    }
    report.setUpdateTime(now);
    report.setFileArray(attachmentService.persistentArray(params.getStr("fileArray")).toString());
    report.setOtherFileArray(attachmentService.persistentArray(params.getStr("otherFileArray")).toString());
    return TransactionActivation.of(()->saveAndFlush(report)).start();
  }


  @Transactional(rollbackFor = Exception.class)
  public void delete(Set<String> ids){
    TokenThreadLocal.getTokenObjNonNull();
    if(!CollectionUtils.isEmpty(ids)){
      getDao().deleteByIdIn(ids);
    }
  }
  public ObjectNode read(String imageUrl)  {
    return doRead(ocrService.readExcelImage(imageUrl));
  }
  private ObjectNode doRead(String data)  {
    if (logger.isDebugEnabled()) {
      logger.debug("OCR image content：{}", data);
    }
    ObjectNode node;
    try {
      node = JsonUtils.reader(data, ObjectNode.class);
    } catch (IOException e) {
      throw new IllegalStateException("读取内容异常，请重试", e);
    }
    JsonNode infos = node.path("prism_wordsInfo");
    String leftAxis = null;
    String rightAxis = null;
    String leftCurvatureRadius = "";
    String rightCurvatureRadius = "";
    String leftK1 = "";
    String leftK2 = "";
    String rightK1 = "";
    String rightK2 = "";
    boolean nextCurvatureRadius = false;
    int i = 0;
    for (JsonNode info : infos) {
      //    String name = null;
      String value = info.path("word").textValue();
      //      if(StringUtils.contains(value, "姓名")){
      //        name = value;
      //      }
      //      else
      if(StringUtils.contains(value, "复合AL")){
        if(rightAxis == null){
          rightAxis = readFirstNumbers(value);
        }else{
          leftAxis = readFirstNumbers(value);
        }
      }
      else if(StringUtils.contains(value, "K1")){
        nextCurvatureRadius = true;
        i++;
        if(i == 1 || i == 3){
          rightK1 = readFirstNumbers(value.replaceFirst("K1", ""));
        }
        else{
          leftK1 = readFirstNumbers(value.replaceFirst("K1", ""));
        }
        continue;
      }
      else if(StringUtils.contains(value, "K2")){
        nextCurvatureRadius = true;
        i++;
        if(i == 1 || i == 3){
          rightK2 = readFirstNumbers(value.replaceFirst("K2", ""));
        }
        else{
          leftK2 = readFirstNumbers(value.replaceFirst("K2", ""));
        }
        continue;
      }
      if(nextCurvatureRadius){
        if(!value.contains("mm")){
          continue;
        }
        nextCurvatureRadius = false;
        if(i == 1 || i == 3){
          rightCurvatureRadius += value;
        }
        else{
          leftCurvatureRadius += value;
          if(i == 4){
            break;
          }
        }
      }
    }
    Assert.isTrue(leftK1 != null && rightK1 != null, ()->"图片识别失败，请重新识别");
    List<String> leftCurvatureRadiusList = readNumbers(leftCurvatureRadius);
    List<String> rightCurvatureRadiusList = readNumbers(rightCurvatureRadius);
    logger.info("ocr image => \nleftAxis: {}, rightAxis: {}, leftCurvatureRadius: {}, rightCurvatureRadius: {}, "
                  + "leftK1: {}, leftK2: {}, rightK1: {}, rightK2: {}",
                leftAxis, rightAxis, leftCurvatureRadiusList, rightCurvatureRadiusList, leftK1, leftK2, rightK1, rightK2);
    return JsonUtils.createObjectNode().put("leftAxis", ConvertUtil.convert(leftAxis, BigDecimal.class))
                    .put("rightAxis", ConvertUtil.convert(rightAxis, BigDecimal.class))
//                    .put("leftCurvatureRadius", computeCurvatureRadius(ConvertUtil.convert(index(leftCurvatureRadiusList, 0), BigDecimal.class),
//                                                    ConvertUtil.convert(index(leftCurvatureRadiusList, 1), BigDecimal.class)))
//                    .put("rightCurvatureRadius", computeCurvatureRadius(ConvertUtil.convert(index(rightCurvatureRadiusList, 0), BigDecimal.class),
//                                                     ConvertUtil.convert(index(rightCurvatureRadiusList, 1), BigDecimal.class)))
                    .put("leftCurvatureRadius", computeCurvatureRadius(ConvertUtil.convert(leftK1, BigDecimal.class),
                                                    ConvertUtil.convert(leftK2, BigDecimal.class)))
                    .put("rightCurvatureRadius", computeCurvatureRadius(ConvertUtil.convert(rightK1, BigDecimal.class),
                                                     ConvertUtil.convert(rightK2, BigDecimal.class)))
                    .put("leftK1", ConvertUtil.convert(leftK1, BigDecimal.class))
                    .put("leftK2", ConvertUtil.convert(leftK2, BigDecimal.class))
                    .put("rightK1", ConvertUtil.convert(rightK1, BigDecimal.class))
                    .put("rightK2", ConvertUtil.convert(rightK2, BigDecimal.class));
  }

  private BigDecimal computeCurvatureRadius(BigDecimal k1, BigDecimal k2){
    if(k1 == null || k2 == null){
      return null;
    }
    //337.5 / ((k1 + k2) / 2 )
    return new BigDecimal("337.5").divide(k1.add(k2).divide(BigDecimal.TWO), 2, RoundingMode.HALF_UP);
  }

  private <E> E index(List<E> collection, int index){
    if (collection != null && !collection.isEmpty()) {
      try {
        return collection.get(index);
      } catch (Exception ignore) {
        return collection.getFirst();
      }
    }
    return null;
  }

  @SuppressWarnings("all")
  private BigDecimal avg(BigDecimal a, BigDecimal b){
    if(a == null){
      return b;
    }
    if (b == null){
      return a;
    }
    return a.add(b).divide(BigDecimal.TWO);
  }

  private static String readFirstNumbers(String str){
    if(str == null){
      return null;
    }
    Matcher matcher = NUMBER_PATTERN.matcher(str);
    if (matcher.find()) {
      return matcher.group();
    }
    return null;
  }
  private static List<String> readNumbers(String str){
    if(str == null){
      return Collections.emptyList();
    }
    Matcher matcher = NUMBER_PATTERN.matcher(str);
    List<String> matches = Lists.newArrayList();
    while (matcher.find()) {
      matches.add(matcher.group());
    }
    return matches;
  }
}