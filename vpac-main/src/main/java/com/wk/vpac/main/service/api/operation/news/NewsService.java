package com.wk.vpac.main.service.api.operation.news;

import com.base.components.common.constants.Valid;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.database.jpa.util.JpaHelper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wk.vpac.common.constants.news.NewsStatus;
import com.wk.vpac.database.dao.operations.news.NewsDao;
import com.wk.vpac.database.dao.operations.news.NewsTypeDao;
import com.wk.vpac.domain.operations.news.News;
import com.wk.vpac.domain.operations.news.NewsType;
import com.wk.vpac.domain.sys.KeywordLink;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import com.wk.vpac.main.service.admin.sys.KeywordLinkService;
import jakarta.persistence.Query;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * News Service
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
@RefreshScope
public class NewsService extends AbstractJpaService<News, String, NewsDao> {

  private final NewsTypeDao newsTypeDao;
  private final AttachmentService attachmentService;
  private final KeywordLinkService keywordLinkService;

  public NewsService(NewsTypeDao newsTypeDao, AttachmentService attachmentService,
                     KeywordLinkService keywordLinkService) {
    this.newsTypeDao = newsTypeDao;
    this.attachmentService = attachmentService;
    this.keywordLinkService = keywordLinkService;
  }

  public Pair<String, Set<KeywordLink>> replaceKeywords(String content){
    return keywordLinkService.replaceKeywords(content);
  }

  /**
   * 详情
   *
   * @return
   */
  public Map<String, Object> detail(Map<String, String> params) {
    String id = ConvertUtil.checkNotNull(params, "id", "id不能为空", String.class);
    News news = findById(id);
    Assert.notNull(news, "未找到栏目！");
    params.put("status", NewsStatus.issue.getVal() + "");
    params.put("id", id);
    DataPage<Map<String, Object>> dataPage = getDao().queryNewsPage(params);
    if (dataPage.getTotal() > 0) {
      Map<String, Object> res = dataPage.getList().get(0);
      if(res != null){
        String content = ConvertUtil.convert(res.get("content"), "");
        if(org.apache.commons.lang3.StringUtils.isNotBlank(content)){
          content = attachmentService.displayUeditorImgWithHost(content);
          Pair<String, Set<KeywordLink>> keyword = replaceKeywords(content);
          res.put("keywords", keyword.getSecond());
          if(ConvertUtil.convert(params.get("replaceKeyword"), true)){
            content = keyword.getFirst();
          }
          res.put("content", content);
        }
      }
//      // 专家信息 & 专家解读
//      String expertId = res.get("expertId") == null ? "" : String.valueOf(res.get("expertId"));
//      if (!StringUtils.isEmpty(expertId)) {
//        Expert expert = expertService.findById(expertId);
//        res.put("expert", expert);
//        NewsExplanation newsExplanation = JpaHelper.detach(newsExplanationService.findOne(
//          ConditionGroup.build().addCondition("expertId", ConditionEnum.OPERATE_EQUAL, expertId)
//                        .addCondition("newsId", ConditionEnum.OPERATE_EQUAL, id)
//                        .addCondition("status", ConditionEnum.OPERATE_EQUAL, 1)));
//        if (newsExplanation != null) {
//          newsExplanation
//            .setArticleContent(attachmentService.displayUeditorImgWithHost(newsExplanation.getArticleContent()));
//          res.put("newsExplanation", newsExplanation);
//        }else{
//          res.remove("expert");
//          res.remove("expertId");
//        }
//      }
//      // 红头信息
//      NewsOther newsOther = JpaHelper.detach(
//        newsOtherDao.findOne(ConditionGroup.build().addCondition("newsId", ConditionEnum.OPERATE_EQUAL, id))
//                    .orElse(null));
//      res.put("newsOther", newsOther);
//      // 相关新闻 todo
      return res;
    }
    return null;
  }

  /**
   * 分页查询
   *
   * @return
   */
  public DataPage pageList(Map<String, String> params) {
    params.put("status", String.valueOf(NewsStatus.issue.getVal()));
    return getDao().queryNewsPage(params);
  }

  /**
   * 新闻分类
   *
   * @return
   */
  public List<NewsType> types(Map<String, String> params) {
    ConditionGroup<NewsType> conditionGroup = ConditionGroup.build();
    String isOrdinary = params.get("isOrdinary");
    if (!StringUtils.isEmpty(isOrdinary)) {
      conditionGroup.addCondition("isOrdinary", ConditionEnum.OPERATE_EQUAL, isOrdinary);
    }
    String typeType = params.get("typeType");
    if(!StringUtils.isEmpty(typeType)){
      conditionGroup.addCondition("type", ConditionEnum.OPERATE_EQUAL, typeType);
    }
    conditionGroup.addCondition("groupName", ConditionEnum.OPERATE_EQUAL, params.get("groupName"), true);

    List<NewsType> all =  newsTypeDao.findAll(
      conditionGroup.addCondition("status", ConditionEnum.OPERATE_EQUAL, Valid.TRUE.getVal()),
      Sort.by(Sort.Direction.ASC, "sortNo"));

    return all;
  }

  public ObjectNode prevAndNext(String id, String searchKey, boolean refType){
    ObjectNode node = JsonUtils.createObjectNode();
    if(org.apache.commons.lang3.StringUtils.isNotBlank(id)){
      List<Map<String, Object>> list = JpaHelper.setMapResult(getEntityManager().createNativeQuery(
        "SELECT publish_time publishTime, type_id typeId FROM base_news WHERE id = :id")
                                    .setParameter("id", id)).getResultList();
      if(!list.isEmpty()){
        Map<String, Object> current = list.get(0);
        Object publishTime = current.get("publishTime");
        Object typeId = current.get("typeId");
        if(publishTime != null && typeId != null){
          String ref = "";
          if (refType){
            ref += "AND type_id = :typeId ";
          }
          if(org.apache.commons.lang3.StringUtils.isNotBlank(searchKey)){
            ref += " AND MATCH (`title`, `content`, `summary`, `author_name`, `keyword`) AGAINST (:searchKey) ";
          }
          Query query = JpaHelper.setMapResult(
            getEntityManager().createNativeQuery("(SELECT id, title, publish_time publishTime, 0 type FROM base_news "
                                                   + "WHERE status = 1 "+ref+" AND (publish_time > :orderField OR (publish_time > :orderField AND id > :id)) ORDER BY publish_time ASC LIMIT 1) "
                                                   + "UNION ALL (SELECT id, title, publish_time publishTime, 1 type FROM base_news "
                                                   + "WHERE status = 1 "+ref+" AND (publish_time < :orderField OR (publish_time < :orderField AND id < :id)) ORDER BY publish_time DESC LIMIT 1)")
                              .setParameter("orderField", publishTime).setParameter("id", id), map->{
              Integer type = ConvertUtil.convert(map.get("type"), Integer.class);
              String pre = null;
              if (Objects.equals(type, 0)) {
                pre = "prev";
              }else if (Objects.equals(type, 1)) {
                pre = "next";
              }
              if(pre != null){
                node.put(pre + "Id", map.get("id").toString());
                node.put(pre + "Title", map.get("title").toString());
                node.putPOJO(pre + "PublishTime", map.get("publishTime"));
              }
            });
          if(refType){
            query.setParameter("typeId", typeId);
          }
          if(org.apache.commons.lang3.StringUtils.isNotBlank(searchKey)){
            query.setParameter("searchKey", searchKey);
          }
          query.getResultList();
        }
      }
    }
    return node;
  }
}