package com.wk.vpac.main.controller.api.operations.news;

import com.base.components.common.doc.Scope;
import com.base.components.common.doc.annotation.ApiExtension;
import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.annotation.RequestModel;
import com.base.components.common.doc.annotation.ReturnModel;
import com.base.components.common.doc.annotation.Token;
import com.base.components.common.doc.type.ArrayType;
import com.base.components.common.doc.type.ObjectType;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.token.TokenCacheObj;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.operations.news.News;
import com.wk.vpac.domain.operations.news.NewsType;
import com.wk.vpac.domain.sys.KeywordLink;
import com.wk.vpac.main.service.api.operation.news.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * News Controller
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-08-07
 */
@RestController
@RequestMapping(GatewayPath.API)
@Tag(name = "文章模块")
public class NewsController {

  private final NewsService newsService;

  public NewsController(NewsService newsService) {
    this.newsService = newsService;
  }


  @Operation(summary = "分页列表")
  @ApiExtension(author = "蒋文", update = "2019-06-21 12:59",token = @Token(require = false))
  @RequestModel({
    @Param(name = "pageNum", value = "当前页数; 默认1", dataType = Integer.class),
    @Param(name = "pageSize", value = "每页行数; 默认30", dataType = Integer.class),
    @Param(name = "startTime", value = "时间之后"),
    @Param(name = "endTime", value = "时间之前"),
    @Param(name = "typeId", value = "分类ID，多个逗号隔开"),
    @Param(name = "typeCode", value = "分类编码，多个逗号隔开"),
    @Param(name = "typeGroupName", value = "分类分组名称，多个逗号隔开"),
    @Param(name = "title", value = "标题"),
    @Param(name = "isRecommend", value = "是否推荐（空-全部，0-不推荐，1-推荐）"),
    @Param(name = "searchKey", value = "全文搜索关键词"),
    @Param(name = "tagsCode", value = "标签编码，多个以英文逗号隔开"),
    @Param(name = "regionId", value = "业务地区id，多个以英文逗号隔开"),
    @Param(name = "regionName", value = "业务地区名称，多个以英文逗号隔开"),
    @Param(name = "regionNews", value = "是否是业务地区文章（1=是，0=否）"),
    @Param(name = "expertId", value = "平台专家ID"),
  })
  @ReturnModel(baseModel = DataPage.class, value = {
    @Param(name = "list[]", value = "结果集", dataType = ArrayType.class, genericType = News.class),
    @Param(name = "list[].tags", value = "所属标签"),
    @Param(name = "list[].typeName", value = "分类名称"),
    @Param(name = "list[].typeGroupName", value = "分类分组名称"),
    @Param(name = "list[].typeCode", value = "分类编码"),
    @Param(name = "list[].regionId", value = "业务地区id（英文逗号隔开）"),
    @Param(name = "list[].regionName", value = "业务地区名称（英文逗号隔开）"),
    @Param(name = "list[].visitCount", value = "访问次数"),
    @Param(name = "list[].collectCount", value = "收藏次数"),
    @Param(name = "list[].praiseCount", value = "点赞次数"),
    @Param(name = "list[].evaluateCount", value = "评价数"),
    @Param(name = "list[].userCollect", value = "当前用户是否收藏; 0=否，1=是", dataType = Integer.class),
    @Param(name = "list[].userPraise", value = "当前用户是否点赞; 0=否，1=是", dataType = Integer.class),
    @Param(name = "list[].userEvaluate", value = "当前用户是否评论; 0=否，1=是", dataType = Integer.class),
    @Param(name = "list[].expertName", value = "专家名称"),
    @Param(name = "list[].expertProfile", value = "专家简介"),
    @Param(name = "list[].expertJobPosition", value = "专家职位"),
    @Param(name = "list[].expertJobTitle", value = "专家职称"),
    @Param(name = "list[].expertAvatar", value = "专家头像"),
    @Param(name = "list[].expertBusinessAreaIds", value = "专家擅长领域ID（多个以,隔开）"),
    @Param(name = "list[].expertBusinessAreaNames", value = "专家擅长领域名字（多个以,隔开）"),
  })
  @GetMapping(value = "/news/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity page(@RequestParam Map<String, String> params) {
    TokenCacheObj tokenObj = TokenThreadLocal.getTokenObj();
    if(tokenObj != null){
      params.put("userId", tokenObj.objId().toString());
    }
    return new ResponseEntity<>(newsService.pageList(params), HttpStatus.OK);
  }

  @Operation(summary = "根据ID查询详情")
  @ApiExtension(author = "蒋文", update = "2019-06-21 12:59",token = @Token(require = false))
  @RequestModel({
    @Param(name = "id", value = "文章id", required = true, requestScope = Scope.PATH),
    @Param(name = "replaceKeyword", value = "是否将内容中的关键词替换为链接(默认true)", dataType = Boolean.class)
  })
  @ReturnModel(baseModel = News.class, value = {
    @Param(name = " tags", value = "所属标签"),
    @Param(name = " typeName", value = "分类名称"),
    @Param(name = " typeGroupName", value = "分类分组名称"),
    @Param(name = " typeCode", value = "分类编码"),
    @Param(name = " regionId", value = "业务地区id（英文逗号隔开）"),
    @Param(name = " regionName", value = "业务地区名称（英文逗号隔开）"),
    @Param(name = " visitCount", value = "访问次数"),
    @Param(name = " collectCount", value = "收藏次数"),
    @Param(name = " praiseCount", value = "点赞次数"),
    @Param(name = " evaluateCount", value = "评价数"),
    @Param(name = " userCollect", value = "当前用户是否收藏; 0=否，1=是", dataType = Integer.class),
    @Param(name = " userPraise", value = "当前用户是否点赞; 0=否，1=是", dataType = Integer.class),
    @Param(name = " userEvaluate", value = "当前用户是否评论; 0=否，1=是", dataType = Integer.class),
    @Param(name = " expertName", value = "专家名称"),
    @Param(name = " expertProfile", value = "专家简介"),
    @Param(name = " expertJobPosition", value = "专家职位"),
    @Param(name = " expertJobTitle", value = "专家职称"),
    @Param(name = " expertAvatar", value = "专家头像"),
    @Param(name = " expertBusinessAreaIds", value = "专家擅长领域ID（多个以,隔开）"),
    @Param(name = " expertBusinessAreaNames", value = "专家擅长领域名字（多个以,隔开）"),
    @Param(name = " keywords[]", value = "内容关键词", dataType = ArrayType.class, genericType = KeywordLink.class)
  })
  @GetMapping(value = "/news/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map> findDetailAndReadById(@PathVariable String id,@RequestParam Map<String, String> params) {
    params.put("id", id);
    TokenCacheObj tokenObj = TokenThreadLocal.getTokenObj();
    if(tokenObj != null){
      params.put("userId", tokenObj.objId().toString());
    }
    Map<String, Object> news = newsService.detail(params);
    return new ResponseEntity<>(news, HttpStatus.OK);
  }


  @Operation(summary = "新闻分类")
  @ApiExtension(author = "蒋文", update = "2019-06-21 11:47")
  @RequestModel({
    @Param(name = "isOrdinary", value = "是否是普通的，1-是，展示在主列表里面，0-否"),
    @Param(name = "typeType", value = "分类类别，1=税法政策，2=平台自有"),
    @Param(name = "groupName", value = "类型分组名称"),
  })
  @ReturnModel(isCollection = true, baseModel = NewsType.class)
  @GetMapping(value = "/news/type", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity types(@RequestParam Map<String, String> params) {
    return new ResponseEntity<>(newsService.types(params), HttpStatus.OK);
  }

  @Operation(summary = "新闻关联文章（上、下一篇）", description = "默认根据发布时间排序")
  @ApiExtension(author = "李赓", update = "2019-06-21 11:47")
  @RequestModel({
    @Param(name = "id", value = "当前文章ID", requestScope = Scope.PATH),
    @Param(name = "refType", value = "是否根据当前文章分类类别筛选, 默认false", dataType = Boolean.class),
    @Param(name = "searchKey", value = "全文搜索关键词")
  })
  @ReturnModel(baseModel = ObjectType.class, value = {
    @Param(name = "prevId", value = "上一篇ID"),
    @Param(name = "prevTitle", value = "上一篇标题"),
    @Param(name = "prevPublishTime", value = "上一篇发布时间"),
    @Param(name = "nextId", value = "下一篇ID"),
    @Param(name = "nextTitle", value = "下一篇标题"),
    @Param(name = "nextPublishTime", value = "下一篇发布时间"),
  })
  @GetMapping(value = "/news/prevAndNext/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity prevAndNext(@PathVariable String id, @RequestParam Map<String, String> params) {
    return new ResponseEntity<>(newsService.prevAndNext(id, params.get("searchKey"), ConvertUtil.convert(params.get("refType"), false)), HttpStatus.OK);
  }

}
