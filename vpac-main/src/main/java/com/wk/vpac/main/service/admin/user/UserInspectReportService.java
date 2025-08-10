package com.wk.vpac.main.service.admin.user;

import com.base.components.common.constants.sys.Dates;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.PageParamMap;
import com.base.components.common.dto.sys.ParamMap;
import com.base.components.common.dto.sys.RowMap;
import com.base.components.common.exception.Exceptions;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.ExcelTool;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.ValidatorUtil;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.transaction.TransactionActivation;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wk.vpac.database.dao.user.UserInspectReportDao;
import com.wk.vpac.domain.user.UserInspectReport;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.BufferedInputStream;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;


/**
 * UserInspectReport Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class UserInspectReportService extends AbstractJpaService<UserInspectReport, String, UserInspectReportDao> {
  private static final Map<Integer, BiConsumer<UserInspectReport, String>> IMPORT_HANDLER = Maps.newLinkedHashMap();
  static {
    IMPORT_HANDLER.put(0, (report, v) -> {
      Assert.isTrue(ValidatorUtil.isIDCard(v), "身份证号不正确");
      report.setIdcard(v.toUpperCase());
    });
    IMPORT_HANDLER.put(1, UserInspectReport::setName);
    IMPORT_HANDLER.put(2, (report, v) -> {
      v = StringUtils.substring(v, 0, Dates.DATE_FORMATTER_PATTERN.length());
      DateTime date;
      try {
        date = ConvertUtil.dateNonNull(v, "检查日期", Dates.DATE_FORMATTER);
      } catch (Exception ignore) {
        date = ConvertUtil.dateNonNull(v, "检查日期", DateTimeFormat.forPattern("yyyy.MM.dd"));
      }
      report.setInspectDate(date.toString(Dates.DATE_FORMATTER));
    });
    IMPORT_HANDLER.put(3, (report, v) -> {
      report.setSchool(v);
      report.setHospital(v);
    });
    IMPORT_HANDLER.put(4, UserInspectReport::setClassName);
    //右眼裸眼
    IMPORT_HANDLER.put(5, UserInspectReport::setRightVisual);
    //左眼裸眼
    IMPORT_HANDLER.put(6, UserInspectReport::setLeftVisual);
    //右眼球镜
    IMPORT_HANDLER.put(7, UserInspectReport::setRightDiopterS);
    //右眼柱镜
    IMPORT_HANDLER.put(8, UserInspectReport::setRightDiopterC);
    //右眼轴位
    IMPORT_HANDLER.put(9, UserInspectReport::setRightShaftPosition);
    //左眼球镜
    IMPORT_HANDLER.put(10, UserInspectReport::setLeftDiopterS);
    //左眼柱镜
    IMPORT_HANDLER.put(11, UserInspectReport::setLeftDiopterC);
    //左眼轴位
    IMPORT_HANDLER.put(12, UserInspectReport::setLeftShaftPosition);
    IMPORT_HANDLER.put(13, (report, v) -> report.setGender("1".equals(v) || "男".equals(v) ? 1 : 2));
  }
  public DataPage<RowMap> page(PageParamMap params){
    return getDao().page(params);
  }

  public UserInspectReport save(ParamMap params){
    String id = params.getStrTrimOrEmpty("id");
    UserInspectReport userInspectReport;
    Date now = new Date();
    if(StringUtils.isBlank(id)){
      userInspectReport = params.populate(new UserInspectReport(), false, "id");
      userInspectReport.setCreateTime(now);
    }else{
      UserInspectReport exists = getDao().findById(id).orElseThrow(Exceptions.of(()->"未找到数据"));
      userInspectReport = params.populate(exists, false, "id");
    }
    userInspectReport.setUpdateTime(now);
    if(userInspectReport.getHeight() == null){
      userInspectReport.setHeight(0);
    }
    return TransactionActivation.start(()->saveAndFlush(userInspectReport));
  }

  public int importNumDelete(String importNum){
    return TransactionActivation.start(()->getDao().deleteByImportNum(importNum));
  }
  public ObjectNode importData(String fileUrl, String fileName, int startRow){
    URL url;
    try {
      url = URI.create(fileUrl).toURL();
    } catch (Exception e) {
      throw new IllegalArgumentException("导入数据错误", e);
    }
    List<String> errors = Lists.newArrayList();
    List<UserInspectReport> list = Lists.newArrayList();
    DateTime now = DateTime.now();
    String importNum = now.toString("yyyyMMddHHmmss");
    boolean isXlsx = fileName.endsWith(".xlsx") || url.toString().endsWith(".xlsx");
    try(BufferedInputStream bis = new BufferedInputStream(url.openConnection().getInputStream())) {
      ExcelTool.readExcel(bis, startRow, isXlsx, 5000, (row, data, e) -> {
        if(data != null){
          UserInspectReport report = new UserInspectReport();
          for (Map.Entry<Integer, BiConsumer<UserInspectReport, String>> entry : IMPORT_HANDLER.entrySet()) {
            try {
              entry.getValue().accept(report, getVal(data, entry.getKey()));
            } catch (Exception ex) {
              errors.add((row.getRowNum() + 1) + "行：" + ex.getMessage());
              report = null;
              break;
            }
          }
          if(report != null){
            report.setImportNum(importNum);
            report.setCreateTime(now.toDate());
            report.setUpdateTime(report.getCreateTime());
            list.add(report);
          }
        }
        return null;
      });
    } catch (Exception e) {
      if(e.getCause() instanceof NotOLE2FileException){
        throw new IllegalArgumentException("导入的文件格式错误，请将文件另存为“.xls”或“.xlsx”格式");
      }
      throw new IllegalArgumentException("导入数据错误（"+e.getCause()+"）", e);
    }
    TransactionActivation.start(()-> getDao().saveAll(list));
    return JsonUtils.createObjectNode().put("add", list.size()).putPOJO("errors", errors);
  }
  private String getVal(String[] data, int index){
    try {
      return StringUtils.trimToEmpty(data[index]);
    } catch (Exception ignore) {
      return "";
    }
  }
}