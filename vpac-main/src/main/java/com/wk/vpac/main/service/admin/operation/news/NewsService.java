package com.wk.vpac.main.service.admin.operation.news;

import com.base.components.common.constants.Valid;
import com.base.components.common.constants.sys.Dates;
import com.base.components.common.constants.sys.Pages;
import com.base.components.common.dto.io.FileDTO;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.oss.OssServiceClient;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wk.vpac.common.constants.news.NewsStatus;
import com.wk.vpac.database.dao.operations.news.NewsDao;
import com.wk.vpac.database.dao.operations.news.NewsTypeDao;
import com.wk.vpac.database.util.CodeUtil;
import com.wk.vpac.domain.operations.news.News;
import com.wk.vpac.domain.operations.news.NewsType;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * NewsService
 *
 * @author <a href="lijian_jie@163.com">LiJian</a>
 * @version 1.0.0, 2018-03-27 13:53
 */
@Service
public class NewsService extends AbstractJpaService<News, String, NewsDao> {
  private final NewsTypeDao typeDao;
  private final AttachmentService attachmentService;
  private final OssServiceClient ossServiceClient;

  public NewsService(NewsTypeDao typeDao, AttachmentService attachmentService, OssServiceClient ossServiceClient) {
    this.typeDao = typeDao;
    this.attachmentService = attachmentService;
    this.ossServiceClient = ossServiceClient;
  }

  public DataPage<Map<String, Object>> queryNewsPage(Map<String, String> params) {
    return getDao().queryNewsPage(params);
  }

  /**
   * 搜索新闻
   *
   * @param params
   *
   * @return
   */
  public DataPage searchNews(Map<String, String> params) {
    String keywords = params.get("keywords");
    ConditionGroup<News> condition = ConditionGroup.build().addCondition("status", ConditionEnum.OPERATE_EQUAL,
                                                                         NewsStatus.issue.getVal()
    );
    if (StringUtils.isNotBlank(keywords)) {
      condition.addCondition("title", ConditionEnum.OPERATE_EQUAL, keywords);
    }
    Pageable pageable = Pages.Helper.pageable(params, Sort.by(Sort.Direction.DESC, "publishTime"));
    return DataPage.from(getDao().findAll(condition, pageable));
  }

  /**
   * 新闻栏目查询
   *
   * @param params
   *
   * @return
   */
  public DataPage queryNewsTypes(Map<String, String> params) {
    String status = params.get("status");
    String type = params.get("type");
    ConditionGroup conditionGroup = ConditionGroup.build();
    if (StringUtils.isNotEmpty(status)) {
      conditionGroup.addCondition("status", ConditionEnum.OPERATE_EQUAL, status);
    }
    if (StringUtils.isNotEmpty(type)) {
      conditionGroup.addCondition("type", ConditionEnum.OPERATE_EQUAL, type);
    }
    return DataPage
      .from(typeDao.findAll(conditionGroup, Pages.Helper.pageable(params, Sort.by(Sort.Direction.ASC, "sortNo"))));
  }

  /**
   * 查询热点新闻 按点击次数降序
   *
   * @param params
   *
   * @return
   */
  public DataPage queryHotNews(Map<String, String> params) {
    ConditionGroup<News> condition = ConditionGroup.build().addCondition("status", ConditionEnum.OPERATE_EQUAL,
                                                                         NewsStatus.issue.getVal()
    );
    Pageable pageable = Pages.Helper.pageable(params, Sort.by(Sort.Direction.DESC, "readCount"));
    return DataPage.from(getDao().findAll(condition, pageable));
  }

  /**
   * 查询推荐新闻
   *
   * @param params
   *
   * @return
   */
  public DataPage queryRecommendNews(Map<String, String> params) {
    ConditionGroup<News> condition = ConditionGroup.build().addCondition("recommend", ConditionEnum.OPERATE_EQUAL,
                                                                         Valid.TRUE.getVal()
    );
    Pageable pageable = Pages.Helper.pageable(params, Sort.by(Sort.Direction.DESC, "publishTime", "readCount"));
    return DataPage.from(getDao().findAll(condition, pageable));
  }

  public News getNewsInfo(String id) {
    Optional<News> newsOptional = getDao().findById(id);
    Assert.isTrue(newsOptional.isPresent(), "news is not present");
    News news = newsOptional.get();
    return news;
  }

  @Transactional(rollbackFor = Exception.class)
  public void addNewsType(Map<String, String> params) {
    NewsType newsType = ConvertUtil.populate(new NewsType(), params);
    // 编号唯一性检测
    long count = CodeUtil.codeCount(typeDao, "typeCode", newsType.getTypeCode());
    Assert.isTrue(count == 0, "编码重复，请重新输入");
    newsType.setCreateTime(new Date());
    // 持久化图片
    String imgJson = newsType.getImgJson();
    if (StringUtils.isNotEmpty(imgJson)) {
      try {
        ArrayNode imgArr = JsonUtils.reader(imgJson, ArrayNode.class);
        ArrayNode persistent = attachmentService
          .persistent(imgArr, false, TokenThreadLocal.getTokenObjNonNull().objId().toString());
        newsType.setImgJson(persistent == null ? "[]" : persistent.toString());
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      newsType.setImgJson("[]");
    }
    typeDao.save(newsType);
  }

  @Transactional(rollbackFor = Exception.class)
  public void updateNewsType(Map<String, String> params) {
    NewsType newsType = ConvertUtil.populate(new NewsType(), params);
    NewsType type = typeDao.findById(newsType.getId()).orElseThrow(() -> new IllegalArgumentException("未找到新闻类型"));
    // 编号唯一性检测
    if (!type.getTypeCode().equals(newsType.getTypeCode())) {
      long count = CodeUtil.codeCount(typeDao, "typeCode", newsType.getTypeCode());
      Assert.isTrue(count == 0, "编码重复，请重新输入");
    }
    type.setRemark(newsType.getRemark());
    type.setName(newsType.getName());
    type.setSortNo(newsType.getSortNo());
    type.setTypeCode(newsType.getTypeCode());
    type.setStatus(newsType.getStatus());
    type.setIsOrdinary(newsType.getIsOrdinary());
    type.setGroupName(newsType.getGroupName());
    type.setTargetLink(newsType.getTargetLink());
    // 持久化图片
    String imgJson = newsType.getImgJson();
    if (StringUtils.isNotEmpty(imgJson)) {
      try {
        ArrayNode imgArr = JsonUtils.reader(imgJson, ArrayNode.class);
        ArrayNode persistent = attachmentService
          .persistent(imgArr, false, TokenThreadLocal.getTokenObjNonNull().objId().toString());
        type.setImgJson(persistent == null ? "[]" : persistent.toString());
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      type.setImgJson("[]");
    }
    typeDao.save(type);
  }


  @Transactional(rollbackFor = Exception.class)
  public void delete(String id) {
    typeDao.deleteById(id);
  }

  @Transactional(rollbackFor = Exception.class)
  public void addNews(Map<String, String> params) {
    String id = params.get("id");
    String typeId = ConvertUtil.checkNotNull(params, "typeId", "未选择分类", String.class);
    String title = ConvertUtil.checkNotNull(params, "edittitle", "请填写标题", String.class);
    String authorName = params.get("authorName");
    String publishTime = params.get("publishTime");
    String keyword = params.get("keyword");
    int status = Integer.parseInt(StringUtils.isBlank(params.get("formstatus")) ? "0" : params.get("formstatus"));
    String content = ConvertUtil.checkNotNull(params, "editorValue", "请填写文章内容", String.class);
    String summary = params.get("summary") == null ? "" : params.get("summary");
    String theSource = params.get("theSource") == null ? "" : params.get("theSource");
    String expertId = params.get("expertId") == null ? "" : params.get("expertId");
    News news = new News();
    news.setId(id == null ? "" : id);
    news.setTypeId(typeId);
    news.setTitle(title);
    news.setKeyword(keyword);
    Integer recommend = ConvertUtil.convert(params.get("recommend"), 0);
    Integer vipView = ConvertUtil.convert(params.get("vipView"), 0);
    Integer templateType = ConvertUtil.convert(params.get("templateType"), 0);
    Integer regionNews = ConvertUtil.convert(params.get("regionNews"), 0);
    Integer displayType = ConvertUtil.convert(params.get("displayType"), 1);
    Integer detailType = ConvertUtil.convert(params.get("detailType"), 1);
    BigDecimal price = ConvertUtil.convert(params.get("price"), BigDecimal.ZERO);

    news.setIsRecommend(recommend);
    news.setVipView(vipView);
    news.setTemplateType(templateType);
    news.setRegionNews(regionNews);
    news.setDisplayType(displayType);
    news.setDetailType(detailType);
    news.setPrice(price);
    if (StringUtils.isNotBlank(publishTime)) {
      news.setPublishTime(DateTime.parse(publishTime, Dates.DATE_TIME_FORMATTER).toDate());
    } else if (StringUtils.isBlank(publishTime) && Valid.TRUE.getVal() == status) {
      news.setPublishTime(new Date());
    }
    // 附件持久化
    news.setImg(attachmentService.persistent(params.get("img"), ""));
    news.setImgs(attachmentService.persistentArray(params.get("imgs")).toString());

//    attachmentService.dealUeditorVedio("", content);
    news.setSummary(summary);
    news.setTheSource(theSource);
    news.setExpertId(expertId);
    news
      .setContent(attachmentService.dealUeditorImg(TokenThreadLocal.getTokenObjNonNull().objId().toString(), content));
    news.setCreateTime(new Date());
    news.setStatus(status);
    news.setAuthorName(authorName);
    news.setAuthorId(TokenThreadLocal.getTokenObjNonNull().objId().toString());
    news.setSubtitle(params.getOrDefault("subtitle", ""));
    news.setOtherAttr(params.getOrDefault("otherAttr", ""));
    news = getDao().save(news);
    updateUserMappingAndVote(news, params.get("userIds"));
  }

  @Transactional(rollbackFor = Exception.class)
  public void updateNews(Map<String, String> params) {
    String id = ConvertUtil.checkNotNull(params, "id", String.class);
    String title = ConvertUtil.checkNotNull(params, "edittitle", "请填写标题", String.class);
    String typeId = ConvertUtil.checkNotNull(params, "typeId", "未选择分类", String.class);
    String authorName = params.get("authorName");
    String publishTime = params.get("publishTime");
    String keyword = params.get("keyword");
    int status = Integer.parseInt(StringUtils.isBlank(params.get("formstatus")) ? "0" : params.get("formstatus"));
    String content = ConvertUtil.checkNotNull(params, "editorValue", "请填写文章内容", String.class);
    String summary = params.get("summary") == null ? "" : params.get("summary");
    String theSource = params.get("theSource") == null ? "" : params.get("theSource");
    String expertId = params.get("expertId") == null ? "" : params.get("expertId");
    News news = getDao().findById(id).orElseThrow(() -> new IllegalArgumentException("未找到新闻"));
    String oldContent = news.getContent();
//    attachmentService.dealUeditorVedio(oldContent, content);
    news.setTypeId(typeId);
    news.setTitle(title);
    news.setKeyword(keyword);
    news
      .setContent(attachmentService.dealUeditorImg(TokenThreadLocal.getTokenObjNonNull().objId().toString(), content));
    Integer recommend = ConvertUtil.convert(params.get("recommend"), 0);
    Integer vipView = ConvertUtil.convert(params.get("vipView"), 0);
    Integer templateType = ConvertUtil.convert(params.get("templateType"), 0);
    Integer regionNews = ConvertUtil.convert(params.get("regionNews"), 0);
    Integer displayType = ConvertUtil.convert(params.get("displayType"), 1);
    Integer detailType = ConvertUtil.convert(params.get("detailType"), 1);
    BigDecimal price = ConvertUtil.convert(params.get("price"), BigDecimal.ZERO);
    news.setIsRecommend(recommend);
    news.setVipView(vipView);
    news.setTemplateType(templateType);
    news.setRegionNews(regionNews);
    news.setDisplayType(displayType);
    news.setDetailType(detailType);
    news.setPrice(price);
    if (StringUtils.isNotBlank(publishTime)) {
      news.setPublishTime(DateTime.parse(publishTime, Dates.DATE_TIME_FORMATTER).toDate());
    } else if (StringUtils.isBlank(publishTime) && Valid.TRUE.getVal() == status) {
      news.setPublishTime(new Date());
    }
    // 附件持久化
    news.setImg(attachmentService.persistent(params.get("img"), ""));
    news.setImgs(attachmentService.persistentArray(params.get("imgs")).toString());
    news.setTheSource(theSource);
    news.setExpertId(expertId);
    news.setSummary(summary);
    news.setStatus(status);
    news.setAuthorName(authorName);
    news.setAuthorId(TokenThreadLocal.getTokenObjNonNull().objId().toString());
    news.setSubtitle(params.getOrDefault("subtitle", ""));
    news.setOtherAttr(params.getOrDefault("otherAttr", ""));
    getDao().save(news);
    updateUserMappingAndVote(news, params.get("userIds"));
  }

  private void updateUserMappingAndVote(News news, String userIds) {
    // 关联人员
    //    if (StringUtils.isNotBlank(userIds)) {
    //      String[] array = StringUtils.split(userIds, ",");
    //      if (array != null) {
    //        for (String userId : array) {
    //          MediaUserMapping mp = new MediaUserMapping();
    //          mp.setMediaId(news.getId());
    //          mp.setUserId(userId);
    //          mediaUserMappingDao.save(mp);
    //        }
    //        news.setVote(StringUtils.isNotBlank(news.getMatchStageId()) && array.length == 1);
    //      }
    //    } else {
    //    }
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteNews(String id) {
    getDao().changeStatus(id, NewsStatus.delete.getVal());
  }

  @Transactional(rollbackFor = Exception.class)
  public void online(String id) {
    int count = getDao().online(id, NewsStatus.issue.getVal(), new Date());
    Assert.isTrue(count == 1, "未找到文章");
  }

  @Transactional(rollbackFor = Exception.class)
  public void recommend(String id) {
    getDao().recommend(id, Valid.TRUE.getVal());
  }




  private String getWxImage(String imageUrl, String userId) {
    try {
      String suffix = getImageSuffix(imageUrl);
      imageUrl = imageUrl.replace("?wx_fmt=" + suffix, "");
      HttpGet get = new HttpGet(imageUrl);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
        HttpResponse response = httpclient.execute(get);
        response.getEntity().writeTo(out);
      }
      if (out.size() == 0) {
        return "";
      }
      FileDTO fileDTO = new FileDTO();
      fileDTO.setFileName(System.currentTimeMillis() + "." + suffix);
      fileDTO.setUserId(userId);
      fileDTO.setTempFile(Boolean.FALSE);
      fileDTO.setPrivateFile(false);
      fileDTO.setUploadBytes(out.toByteArray());
      ObjectNode objectNode = ossServiceClient.serverUploadFile(fileDTO);
      String url = objectNode.get("url").asText();
      url = URLDecoder.decode(url, "utf-8");
      if (url.indexOf("?") > 0) {
        return url.substring(url.indexOf("/pub"), url.indexOf("?"));
      } else {
        return url.substring(url.indexOf("/pub"));
      }
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  private String getImageSuffix(String attr) {
    if (attr.endsWith("wx_fmt=png")) {
      return "png";
    } else if (attr.endsWith("wx_fmt=jpeg")) {
      return "jpeg";
    } else if (attr.endsWith("wx_fmt=gif")) {
      return "gif";
    } else {
      return "";
    }
  }

}
