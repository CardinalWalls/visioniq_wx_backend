package com.wk.vpac.main.controller.admin.operation.news;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.service.cache.DictionaryCacheService;
import com.base.components.common.token.RequireToken;
import com.base.components.common.util.ConvertUtil;
import com.google.common.collect.Maps;
import com.wk.vpac.cache.dictionary.NewsConfig;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.common.token.user.UserMemberToken;
import com.wk.vpac.domain.operations.news.News;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import com.wk.vpac.main.service.admin.operation.news.NewsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * NewsController
 *
 * @author <a href="lijian_jie@163.com">LiJian</a>
 * @version 1.0.0, 2018-03-27 13:46
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class NewsController {
  private final NewsService newsService;

  private final AttachmentService attachmentService;
  private final DictionaryCacheService dictionaryCacheService;

  public NewsController(NewsService newsService, AttachmentService attachmentService,
                        DictionaryCacheService dictionaryCacheService) {
    this.newsService = newsService;
    this.attachmentService = attachmentService;
    this.dictionaryCacheService = dictionaryCacheService;
  }



  /**
   * 跳转到类型管理页面
   *
   * @param request
   * @param response
   * @param modelMap
   *
   * @return
   */
  @GetMapping({"/news/type/index"})
  public String typeIndex(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
    return "operation/news/type/index";
  }

  /**
   * o'p
   * 跳转到内容管理页面
   *
   * @param request
   * @param response
   * @param modelMap
   *
   * @return
   */
  @GetMapping({"/news/content/index"})
  public String contentIndex(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
    String detailLink = dictionaryCacheService.listDataVal(NewsConfig.DETAIL_LINK);
    modelMap.put("detailLink", detailLink);
    return "operation/news/content/index";
  }

  /**
   * 跳转到内容编辑页面
   *
   * @param request
   * @param response
   * @param modelMap
   *
   * @return
   */
  @GetMapping({"/news/content/edit_index"})
  public String editContent(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
    request.setAttribute("contentid", request.getParameter("contentid"));
    return "operation/news/content/edit_index";
  }

  @GetMapping("/news/page")
  public ResponseEntity queryNewsPage(@RequestParam Map<String, String> params) {
    DataPage dataPage = newsService.queryNewsPage(params);
    return new ResponseEntity<>(dataPage, HttpStatus.OK);
  }

  /**
   * 搜索
   *
   * @param params <p>  keywords                      - Notnull   - Str - desc
   * <p>  pageSize                  - Notnull   - Str - desc
   * <p>  pageNum                   - Notnull   - Str - desc
   *
   * @return
   */
  @GetMapping(value = "/news/search")
  public ResponseEntity searchNews(@RequestParam Map<String, String> params) {
    DataPage dataPage = newsService.searchNews(params);
    return new ResponseEntity<>(dataPage, HttpStatus.OK);
  }

  /**
   * 获取热点新闻
   *
   * @param params
   *
   * @return
   */
  @GetMapping("/news/hot")
  public ResponseEntity queryHotNews(@RequestParam Map<String, String> params) {
    DataPage dataPage = newsService.queryHotNews(params);
    return new ResponseEntity<>(dataPage, HttpStatus.OK);
  }

  /**
   * 获取推荐新闻
   *
   * @param params
   *
   * @return
   */
  @GetMapping("/news/recommend")
  public ResponseEntity queryRecommendNews(@RequestParam Map<String, String> params) {
    DataPage dataPage = newsService.queryRecommendNews(params);
    return new ResponseEntity<>(dataPage, HttpStatus.OK);
  }

  /**
   * 获取新闻栏目
   *
   * @param params
   *
   * @return
   */
  @GetMapping("/news/type")
  @ResponseBody
  public ResponseEntity queryNewsTypes(@RequestParam Map<String, String> params) {
    DataPage types = newsService.queryNewsTypes(params);
    return ResponseEntity.ok(types);
  }

  /**
   * 新增或修改新闻分类
   *
   * @param
   *
   * @return
   */
  @RequireToken(UserMemberToken.class)
  @PostMapping("/news/type")
  @ResponseBody
  public ResponseEntity saveOrUpdate(@RequestParam Map<String, String> params) {
    if (StringUtils.isBlank(params.get("id"))) {
      newsService.addNewsType(params);
    } else {
      newsService.updateNewsType(params);
    }
    return ResponseEntity.ok(JsonResult.success());
  }

  /**
   * 新增或修改新闻
   *
   * @param params
   *
   * @return
   */
  @RequireToken(UserMemberToken.class)
  @PostMapping("/news")
  @ResponseBody
  public ResponseEntity saveOrUpdateNews(@RequestParam Map<String, String> params) {
    if (StringUtils.isBlank(params.get("id"))) {
      newsService.addNews(params);
    } else {
      newsService.updateNews(params);
    }
    return ResponseEntity.ok(JsonResult.success());
  }

  /**
   * 删除新闻分类
   *
   * @param -
   *
   * @return
   */
  @RequireToken(UserMemberToken.class)
  @PostMapping("/news/type/delete")
  @ResponseBody
  public ResponseEntity delete(@RequestParam String id) {
    newsService.delete(id);
    return ResponseEntity.ok(JsonResult.success());
  }

  /**
   * 删除新闻
   *
   * @param -
   *
   * @return
   */
  @RequireToken(UserMemberToken.class)
  @PostMapping("/news/delete")
  @ResponseBody
  public ResponseEntity deleteNews(@RequestParam String id) {
    newsService.deleteNews(id);
    return ResponseEntity.ok(JsonResult.success());
  }

  /**
   * 获取新闻详情
   *
   * @param id
   *
   * @return
   */
  @RequireToken(UserMemberToken.class)
  @GetMapping("/news/{id}")
  public ResponseEntity getNewsInfo(@PathVariable("id") String id) {
    Map<String, String> params = Maps.newHashMap();
    params.put("id", id);
    DataPage<Map<String, Object>> dataPage = newsService.queryNewsPage(params);
    Map<String, Object> detail = dataPage.getList().get(0);
    detail.put("content", attachmentService.displayUeditorImg(String.valueOf(detail.get("content"))));
    return new ResponseEntity<>(detail, HttpStatus.OK);
  }

  /**
   * 发布
   *
   * @param -
   *
   * @return
   */
  @RequireToken(UserMemberToken.class)
  @PostMapping("/news/online")
  @ResponseBody
  public ResponseEntity changeStatus(@RequestParam Map<String, String> params) {
    newsService.online(ConvertUtil.checkNotNull(params, "id", String.class));
    return ResponseEntity.ok(JsonResult.success());
  }

  /**
   * 推荐
   *
   * @param -
   *
   * @return
   */
  @RequireToken(UserMemberToken.class)
  @PostMapping("/news/recommend")
  @ResponseBody
  public ResponseEntity recommend(@RequestParam Map<String, String> params) {
    newsService.recommend(ConvertUtil.checkNotNull(params, "id", String.class));
    return ResponseEntity.ok(JsonResult.success());
  }

  /**
   * 获取新闻详情
   *
   * @param newsId
   *
   * @return
   */
  @RequireToken(UserMemberToken.class)
  @GetMapping("/news/content")
  public ResponseEntity getNewsContent(@RequestParam("newsId") String newsId) {
    News news = newsService.getNewsInfo(newsId);
    String content = attachmentService.displayUeditorImg(news.getContent());
    return new ResponseEntity<>(content, HttpStatus.OK);
  }

}
