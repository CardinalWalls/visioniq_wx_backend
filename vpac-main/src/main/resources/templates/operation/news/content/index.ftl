<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
<link href="${ctx!}/admin/pub/js/attachment/gallery/attachmentGallery.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${ctx!}/admin/pub/plugin/select2ztree/select2.min.css">
<link rel="stylesheet" type="text/css" href="${ctx!}/admin/pub/plugin/select2ztree/bootstrapStyle.css">

<style>
  #newsContentModal img {
    max-width: 100%;
  }
  .form-group{
    margin-right: 5px !important;
  }
</style>
<script>
  var detailLink = "${detailLink}";
</script>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <label for="startTime">起止时间</label>
          <input type="text" placeholder="请选择开始时间" id="startTime" name="startTime" class="form-control layer-date"
                 autocomplete="off">
        </div>
        <div class="form-group">
          <label for="endTime">至</label>
          <input type="text" placeholder="请选择结束时间" id="endTime" name="endTime" class="form-control layer-date"
                 autocomplete="off">
        </div>
        <div class="form-group">
          <label for="title" class="sr-only">标题</label>
          <input type="text" placeholder="标题" name="title" id="title" class="form-control">
        </div>

        <div class="form-group">
          <label for="typeIdSearch" class="sr-only">内容分类</label>
          <select id="typeIdSearch" name="typeId" class="form-control">
            <option value="">全部</option>
          </select>
        </div>

        <div class="form-group">
          <label for="status" class="sr-only">状态</label>
          <select id="status" name="status" class="form-control">
            <option value="">未删除状态</option>
            <option value="0">草稿</option>
            <option value="1">已发布</option>
            <option value="2">已删除</option>
          </select>
        </div>
        <div class="form-group">
          <label for="recommend" class="sr-only">是否推荐</label>
          <select name="recommend" class="form-control">
            <option value="">- 是否推荐 -</option>
            <option value="1">推荐</option>
            <option value="0">未推荐</option>
          </select>
        </div>

<#--        <div class="form-group">-->
<#--          <label for="vipView" class="sr-only">是否VIP</label>-->
<#--          <select name="vipView" class="form-control">-->
<#--            <option value="">是否VIP</option>-->
<#--            <option value="1">VIP</option>-->
<#--            <option value="0">普通</option>-->
<#--          </select>-->
<#--        </div>-->

<#--        <div class="form-group">-->
<#--          <label for="templateType" class="sr-only">模板类型</label>-->
<#--          <select name="templateType" class="form-control">-->
<#--            <option value="">模板类型</option>-->
<#--            <option value="1">红头</option>-->
<#--            <option value="0">普通</option>-->
<#--          </select>-->
<#--        </div>-->

<#--        <div class="form-group">-->
<#--          <label for="regionNews" class="sr-only">地区政策</label>-->
<#--          <select name="regionNews" class="form-control">-->
<#--            <option value="">是否是地区政策</option>-->
<#--            <option value="1">是</option>-->
<#--            <option value="0">否</option>-->
<#--          </select>-->
<#--        </div>-->

<#--        <div class="form-group">-->
<#--          <label for="hasExpert" class="sr-only">专家解读</label>-->
<#--          <select name="hasExpert" class="form-control">-->
<#--            <option value="">是否解读</option>-->
<#--            <option value="1">有解读</option>-->
<#--            <option value="0">无解读</option>-->
<#--          </select>-->
<#--        </div>-->

<#--        <div class="form-group">-->
<#--          <label for="hasRed" class="sr-only">红头设置</label>-->
<#--          <select name="hasRed" class="form-control">-->
<#--            <option value="">红头设置</option>-->
<#--            <option value="1">已设置</option>-->
<#--            <option value="0">未设置</option>-->
<#--          </select>-->
<#--        </div>-->

        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
    <#if RequestParameters["title"]??>
      <script type="application/javascript">
        $("#title").val('${RequestParameters["title"]}');
      </script>
    </#if>
    <div class="table-toolbar">
      <button onclick="tp.openModal('add')" type="button" class="btn btn-info" style="">
        <i class="fa fa-plus"></i>
        新增
      </button>
      <button onclick="tp.openModal('edit')" type="button" class="btn btn-warning">
        <i class="fa fa-edit"></i>
        修改
      </button>
      <button onclick="tp.delete()" type="button" class="btn btn-danger">
        <i class="fa fa-remove"></i>
        删除
      </button>
<#--      <span style="padding: 0 5px;">|</span>-->
<#--      <button onclick="tp.other()" type="button" class="btn btn-info" style="">-->
<#--        红头设置-->
<#--      </button>-->
<#--      <button onclick="tp.explanation()" type="button" class="btn btn-info" style="">-->
<#--        解读设置-->
<#--      </button>-->
    </div>
    <table id="table_list" class="table-striped"></table>
  </div>
</div>



<div class="modal" id="typeModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
          class="sr-only">关闭</span>
        </button>
        <h4 class="modal-title">
          <b id="modal_title">编辑新闻</b>
        </h4>
      </div>
      <div class="modal-body">
        <form class="panel-body form-horizontal" id="news_form">
          <input type="hidden" class="form-control" name="id" id="id"/>

          <div class="row">
            <div class="form-group col-xs-12">
              <label for="typeId" class="control-label">新闻分类：</label>
              <div>
                <select class="form-control" name="typeId" id="typeId" required="required">
                </select>
              </div>
            </div>
<#--            <div class="form-group col-xs-6">-->
<#--              <label for="vipView" class="control-label">是否VIP：</label>-->
<#--              <div>-->
<#--                <select id="vipView" name="vipView" class="form-control">-->
<#--                  <option value="0" selected="selected">普通</option>-->
<#--                  <option value="1">VIP</option>-->
<#--                </select>-->
<#--              </div>-->
<#--            </div>-->
          </div>

          <div class="form-group">
            <label for="edittitle" class="control-label">标题：</label>
            <button type="button" class="btn btn-xs btn-primary" style="margin-bottom: 5px"
                    onclick="insertBr('edittitle')">插入换行符
            </button>
            <button type="button" class="btn btn-xs btn-default" style="margin-bottom: 5px"
                    onclick="clearBr('edittitle')">清除换行符
            </button>
            <div>
              <textarea type="text" rows="1" class="form-control" id="edittitle" name="edittitle" required></textarea>
            </div>
          </div>
          <div class="form-group">
            <label for="subtitle" class="control-label">副标题：</label>
            <button type="button" class="btn btn-xs btn-primary" style="margin-bottom: 5px"
                    onclick="insertBr('subtitle')">插入换行符
            </button>
            <button type="button" class="btn btn-xs btn-default" style="margin-bottom: 5px"
                    onclick="clearBr('subtitle')">清除换行符
            </button>
            <div>
              <textarea type="text" rows="1" class="form-control" id="subtitle" name="subtitle" ></textarea>
            </div>
          </div>
          <div class="form-group">
            <label for="newscontent" class="control-label">内容：</label>
            <div>
              <#assign keditor_id="keditor"/>
              <#assign keditor_height="300"/>
              <#include "${_ROOT_PATH_}/kindeditor/include.ftl"/>
            </div>
          </div>

          <div class="row">
            <div class="form-group col-xs-6">
              <label for="avatar" class="control-label">新闻封面：</label>
              <button type="button" class="btn btn-success" onclick="attachment.uploadFile(1, 'img')"><i
                class="fa fa-upload"></i>本地上传
              </button>
              <button type="button" class="btn btn-success" onclick="attachmentGallery.openGallery(1,'img')">
                图库选择
              </button>
              <input type="hidden" class="form-control" required name="img" id="img"
                     customImageStyle="max-width:80px;max-height:80px"/>
            </div>
            <div class="form-group col-xs-6">
              <label for="avatar" class="control-label">其它多图：</label>
              <button type="button" class="btn btn-success" onclick="attachment.uploadFile(10, 'imgs')"><i
                    class="fa fa-upload"></i>本地上传
              </button>
              <button type="button" class="btn btn-success" onclick="attachmentGallery.openGallery(10,'imgs')">
                图库选择
              </button>
              <input type="hidden" class="form-control" name="imgs" id="imgs"
                     customImageStyle="max-width:80px;max-height:80px"/>
            </div>
          </div>

          <div class="row">
            <div class="form-group col-xs-6">
              <label for="summary" class="control-label">摘要：</label>
              <button type="button" class="btn btn-xs btn-primary" style="margin-bottom: 5px"
                      onclick="insertBr('summary')">插入换行符
              </button>
              <button type="button" class="btn btn-xs btn-default" style="margin-bottom: 5px"
                      onclick="clearBr('summary')">清除换行符
              </button>
              <div>
                <textarea class="form-control" id="summary" rows="3" name="summary"></textarea>
              </div>
            </div>
            <div class="form-group col-xs-6 m-t-xs">
              <label for="summary" class="control-label">其它属性：</label>
              <div>
                <textarea class="form-control" id="otherAttr" rows="3" name="otherAttr"></textarea>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="form-group col-xs-6">
              <label for="displayType" class="control-label">列表展示类型：</label>
              <div>
                <select id="displayType" name="displayType" class="form-control">
                  <option value="1">文左图右</option>
                  <option value="2">文右图左</option>
                  <option value="3">文上图下</option>
                  <option value="4">文下图上</option>
                </select>
              </div>
            </div>
            <div class="form-group col-xs-6">
              <label for="publishTime" class="control-label">发布时间：</label>
              <div>
                <input type="text" class="form-control layer-date" id="publishTime" name="publishTime"
                       autocomplete="off"/>
              </div>
            </div>
<#--            <div class="form-group col-xs-6 detailTypeDiv">-->
<#--              <label for="detailType" class="control-label">详情展示类型：</label>-->
<#--              <div>-->
<#--                <select id="detailType" name="detailType" class="form-control">-->
<#--                  <option value="1">红色五星</option>-->
<#--                  <option value="2">普通文章</option>-->
<#--                </select>-->
<#--              </div>-->
<#--            </div>-->
          </div>


          <div class="row">
            <div class="form-group col-xs-6">
              <label for="keyword" class="control-label">关键字：</label>
              <div>
                <input type="text" class="form-control" id="keyword" name="keyword"/>
              </div>
            </div>
            <div class="form-group col-xs-6">
              <label for="authorName" class="control-label">作者姓名：</label>
              <div>
                <input type="text" class="form-control" id="authorName" name="authorName"/>
              </div>
            </div>
<#--            <div class="form-group col-xs-6">-->
<#--              <label for="expertId" class="control-label">专家作者：</label>-->
<#--              <input name="expertId" class="form-control" id="expertId">-->
<#--            </div>-->
<#--          </div>-->

<#--          <div class="row">-->
<#--            <div class="form-group col-xs-6">-->
<#--              <label for="price" class="control-label">价格：</label>-->
<#--              <div>-->
<#--                <input type="text" class="form-control" id="price" name="price" required/>-->
<#--              </div>-->
<#--            </div>-->
<#--            -->
          </div>


          <div class="row">
            <div class="form-group col-xs-4">
              <label for="recommend" class="control-label">推荐新闻：</label>
              <div>
                <select id="recommend" name="recommend" class="form-control">
                  <option value="0" selected="selected">否</option>
                  <option value="1">是</option>
                </select>
              </div>
            </div>
            <div class="form-group col-xs-4">
              <label for="theSource" class="control-label">来源：</label>
              <div>
                <input type="text" class="form-control" id="theSource" name="theSource"/>
              </div>
            </div>
            <div class="form-group col-xs-4">
              <label for="formstatus" class="control-label">状态: </label>
              <div>
                <select class="form-control" name="formstatus" id="formstatus" required="required">
                  <option value="1">已发布</option>
                  <option value="0" selected>草稿</option>
                </select>
              </div>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="dealSaveBtn" onclick="tp.typeSave()"><i
          class="fa fa-save"></i>提交
        </button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>
      </div>
    </div>
  </div>
</div>
</div>

  <div class="modal" id="newsContentModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg" style="width: 690px;">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
            class="sr-only">关闭</span>
          </button>
          <h4 class="modal-title">
            <b id="modal_title">新闻内容</b>
          </h4>
        </div>
        <div class="modal-body" id="newsDetail">
          <div class="row m-b">
            <div class="col-xs-12">
              <span style="font-weight: bolder">链接地址：</span>
              <div class="detail" id="newsLink"></div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-6">
              <span style="font-weight: bolder">封面图：</span>
              <div class="detail" data-rel="img"></div>
            </div>
            <div class="col-md-6">
              <span style="font-weight: bolder">标题：</span>
              <div class="detail" data-rel="title"></div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-12">
              <span style="font-weight: bolder">摘要：</span>
              <div class="detail" data-rel="summary"></div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-4">
              <span style="font-weight: bolder">关键字：</span>
              <div class="detail" data-rel="keyword"></div>
            </div>
            <div class="col-md-4">
              <span style="font-weight: bolder">作者：</span>
              <div class="detail" data-rel="authorName"></div>
            </div>
            <div class="col-md-4">
              <span style="font-weight: bolder">操作者：</span>
              <div class="detail" data-rel="nickName"></div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-4">
              <span style="font-weight: bolder">类型：</span>
              <div class="detail" data-rel="categoryName"></div>
            </div>
            <div class="col-md-4">
              <span style="font-weight: bolder">标签：</span>
              <div class="detail" data-rel="tags"></div>
            </div>
            <div class="col-md-4">
              <span style="font-weight: bolder">创建时间：</span>
              <div class="detail" data-rel="createTime"></div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-4">
              <span style="font-weight: bolder">发布时间：</span>
              <div class="detail" data-rel="publishTime"></div>
            </div>
            <div class="col-md-4">
              <span style="font-weight: bolder">浏览次数：</span>
              <div class="detail" data-rel="visitCount"></div>
            </div>
            <div class="col-md-4">
              <span style="font-weight: bolder">收藏次数：</span>
              <div class="detail" data-rel="collectCount"></div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-4">
              <span style="font-weight: bolder">关注次数：</span>
              <div class="detail" data-rel="followCount"></div>
            </div>
            <div class="col-md-4">
              <span style="font-weight: bolder">点赞次数：</span>
              <div class="detail" data-rel="praiseCount"></div>
            </div>
            <div class="col-md-4">
              <span style="font-weight: bolder">评价次数：</span>
              <div class="detail" data-rel="evaluateCount"></div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-12">
              <span style="font-weight: bolder">内容：</span>
              <hr>
              <div class="detail" data-rel="content" class="animated fadeInRight article"></div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>
        </div>
      </div>
    </div>
  </div>


<div class="modal" id="other_modal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
          class="sr-only">关闭</span>
        </button>
        <h4 class="modal-title">
          红头信息
        </h4>
      </div>
      <div class="modal-body">
        <form class="panel-body form-horizontal" id="otherForm">
          <input type="hidden" class="form-control" name="newsId"/>
          <div class="form-group">
            <label class="control-label">索引号：</label>
            <div>
              <input type="text" class="form-control" name="indexNo"/>
            </div>
          </div>

          <div class="form-group">
            <label class="control-label">发文机构：</label>
            <div>
              <input type="text" class="form-control" name="orgName"/>
            </div>
          </div>

          <div class="form-group">
            <label class="control-label">发文字号：</label>
            <div>
              <input type="text" class="form-control" name="publishNumber"/>
            </div>
          </div>

          <div class="form-group">
            <label class="control-label">主题：</label>
            <div>
              <input type="text" class="form-control" name="topic"/>
            </div>
          </div>

          <div class="form-group">
            <label class="control-label">主题词：</label>
            <div>
              <input type="text" class="form-control" name="topicWord"/>
            </div>
          </div>

          <div class="form-group">
            <label class="control-label">机构成文日期：</label>
            <div>
              <input type="text" class="form-control layer-date" id="orgWrittenTime" name="orgWrittenTime" required
                     autocomplete="off"/>
            </div>
          </div>

          <div class="form-group">
            <label class="control-label">机构发文日期：</label>
            <div>
              <input type="text" class="form-control layer-date" id="orgPublishTime" name="orgPublishTime" required
                     autocomplete="off"/>
            </div>
          </div>

        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" onclick="tp.otherSave()"><i class="fa fa-save"></i>提交
        </button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>
      </div>
    </div>
  </div>
</div>


 <script src="${ctx!}/admin/pub/js/region/regionRefTool.js"></script>
  <script src="${ctx!}/admin/pub/js/operation/news/content/index.js"></script>
  <script src="${ctx!}/admin/pub/js/attachment/attachmentTool.js"></script>
    <script src="${ctx!}/admin/pub/js/attachment/gallery/attachmentGallery.js"></script>
<script charset="utf-8" src="${ctx!}/admin/pub/plugin/kindeditor/format.js"></script>
<script src="${ctx!}/admin/pub/js/tags/main/util.js"></script>
<#--  <#include "${_ROOT_PATH_}/macro/attachmentVod.ftl"/>-->
<#--  <@attachmentVod/>-->
</@bodyContent>