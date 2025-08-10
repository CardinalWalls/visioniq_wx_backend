<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>

<@headContent/>
<link href="${ctx!}/admin/pub/js/attachment/gallery/attachmentGallery.css" rel="stylesheet">
<@bodyContent>

<div class="panel panel-default">
  <div class="panel-body table-content">
<#--    <form class="table-search">-->
<#--      <div class="form-inline">-->
<#--        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>-->
<#--        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>-->
<#--        </button>-->
<#--      </div>-->
<#--    </form>-->
    <div class="table-toolbar">
      <button onclick="tp.openModal('add')" type="button" class="btn btn-info">
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
    </div>
    <table id="table_list" class="table-striped"></table>
  </div>
</div>


<div class="modal" id="typeModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
          class="sr-only">关闭</span>
        </button>
        <h4 class="modal-title">
          <b id="modal_title">分类</b>
        </h4>
      </div>
      <div class="modal-body">
        <form class="panel-body form-horizontal" id="type_form">
          <input type="hidden" class="form-control" name="id" id="id"/>
          <div class="form-group">
            <label for="sortNo" class="control-label">排序号：</label>
            <div>
              <input type="number" class="form-control" id="sortNo" name="sortNo" value="1" required/>
            </div>
          </div>
          <div class="form-group">
            <label for="name" class="control-label">分类名称：</label>
            <div>
              <input type="text" class="form-control" id="name" name="name" required/>
            </div>
          </div>
          <div class="form-group">
            <label for="typeCode" class="control-label">分类编码：</label>
            <div>
              <input type="text" class="form-control" id="typeCode" name="typeCode" required/>
            </div>
          </div>

          <div class="form-group">
            <label for="type" class="control-label">分组名称：</label>
            <div>
              <input type="text" name="groupName" id="groupName" class="form-control" />
            </div>
          </div>

          <div class="form-group">
            <label for="avatar" class="control-label">分类图片：</label>
            <button type="button" class="btn btn-success" onclick="attachment.uploadFile(5, 'imgJson')"><i
              class="fa fa-upload"></i>本地上传
            </button>
            <button type="button" class="btn btn-success" onclick="attachmentGallery.openGallery(5,'imgJson')">
              图库选择
            </button>
            <input type="hidden" class="form-control" required name="imgJson" id="imgJson"
                   customImageStyle="max-width:80px;max-height:80px"/>
          </div>

          <div class="form-group">
            <label for="isOrdinary" class="control-label">是否展示在主列表里面：</label>
            <div>
              <select name="isOrdinary" id="isOrdinary" class="form-control" required>
                <option value="1">是</option>
                <option value="0">否</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <label class="control-label">状态：</label>
            <div>
              <select name="status" id="status" class="form-control" required>
                <option value="1">启用</option>
                <option value="0">禁用</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <label for="remark" class="control-label">备注：</label>
            <div>
              <input type="text" class="form-control" id="remark" name="remark"/>
            </div>
          </div>
          <div class="form-group">
            <label for="targetLink" class="control-label">跳转地址：</label>
            <div>
              <input type="text" class="form-control" id="targetLink" name="targetLink"/>
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


<script src="${ctx!}/admin/pub/js/operation/news/type/index.js"></script>
  <script src="${ctx!}/admin/pub/js/attachment/attachmentTool.js"></script>
    <script src="${ctx!}/admin/pub/js/attachment/gallery/attachmentGallery.js"></script>

</@bodyContent>