<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
<link href="${ctx!}/admin/pub/js/attachment/gallery/attachmentGallery.css" rel="stylesheet">
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <input type="text" placeholder="标签名" name="tagName" class="form-control" title="标签名">
        </div>
        <div class="form-group">
          <input type="text" placeholder="标签编码" name="tagCode" class="form-control" title="标签编码">
        </div>
        <div class="form-group">
          <input type="text" placeholder="选择类型" name="typeId" id="typeId" class="form-control">
        </div>
        <div class="form-group">
          <select placeholder="状态" name="status" class="form-control" title="状态">
            <option value="">状态</option>
            <option value="1">启用</option>
            <option value="0">禁用</option>
          </select>
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
      <div class="table-toolbar">
        <button onclick="tags.add()" type="button" class="btn btn-info">
          <i class="fa fa-plus"></i>
          新增
        </button>
        <button onclick="tags.edit()" type="button" class="btn btn-warning">
          <i class="fa fa-edit"></i>
          修改
        </button>
        <button onclick="tags.del()" type="button" class="btn btn-danger">
          <i class="fa fa-remove"></i>
          删除
        </button>
      </div>
    <table id="tags_table_list" class="table-striped"></table>
  </div>
</div>
<div class="modal" id="tags_modal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
              class="sr-only">关闭</span>
        </button>
        <h4 class="modal-title">
        </h4>
      </div>
      <div class="modal-body">
        <form class="panel-body form-horizontal">
          <input type="hidden" class="form-control" name="id"/>
          <div class="form-group">
            <label class="control-label">类型</label>
            <div>
              <input type="text" placeholder="选择类型" name="typeId" id="typeIdForTags" class="form-control">
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">标签名：</label>
            <div>
              <input type="text" class="form-control" name="tagName" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">标签编码：</label>
            <div>
              <input type="text" class="form-control" name="tagCode" required/>
            </div>
          </div>
          <div class="form-group">
            <label for="avatar" class="control-label">图片：</label>
            <button type="button" class="btn btn-success" onclick="attachment.uploadFile(10, 'imgJson')"><i
              class="fa fa-upload"></i>本地上传
            </button>
            <button type="button" class="btn btn-success" onclick="attachmentGallery.openGallery(5,'imgJson')">
              图库选择
            </button>
            <input type="hidden" class="form-control" required name="imgJson" id="imgJson"
                   customImageStyle="max-width:80px;max-height:80px"/>
          </div>
          <div class="form-group">
            <label class="control-label">排序号（等于0不显示在接口列表）：</label>
            <div>
              <input type="number" class="form-control" name="sortNo" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">状态：</label>
            <div>
              <select name="status" required class="form-control">
                <option value="1">启用</option>
                <option value="0">禁用</option>
              </select>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" onclick="tags.save()"><i class="fa fa-save"></i>提交</button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>
      </div>
    </div>
  </div>
</div>
<script src="${ctx!}/admin/pub/js/tags/main/index.js"></script>
  <script src="${ctx!}/admin/pub/js/attachment/attachmentTool.js"></script>
    <script src="${ctx!}/admin/pub/js/attachment/gallery/attachmentGallery.js"></script>
</@bodyContent>