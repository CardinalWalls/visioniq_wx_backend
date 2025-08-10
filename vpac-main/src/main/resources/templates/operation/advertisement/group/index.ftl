<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>

<@headContent/>
<@bodyContent>

<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>

    <div class="table-toolbar">
      <button onclick="ad.openModal('add')" type="button" class="btn btn-info">
        <i class="fa fa-plus"></i>
        新增
      </button>
      <button onclick="ad.openModal('edit')" type="button" class="btn btn-warning">
        <i class="fa fa-edit"></i>
        修改
      </button>
      <button onclick="ad.delete()" type="button" class="btn btn-danger">
        <i class="fa fa-remove"></i>
        删除
      </button>
    </div>
    <table id="table_list" class="table-striped"></table>
  </div>
</div>


<div class="modal" id="groupModal" tabindex="-1" role="dialog" aria-hidden="true">
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
        <form class="panel-body form-horizontal" id="group_form">
          <input type="hidden" class="form-control" name="id" id="id"/>
          <div class="form-group">
            <label for="name" class="control-label">名称：</label>
            <div>
              <input type="text" class="form-control" id="name" name="name" required/>
            </div>
          </div>
          <div class="form-group">
            <label for="typeCode" class="control-label">编码：</label>
            <div>
              <input type="text" class="form-control" id="typeCode" name="typeCode" required/>
            </div>
          </div>
          <div class="form-group">
            <label for="status" class="control-label">状态：</label>
            <div>
              <select class="form-control" name="status" id="status" required="required">
                <option value="1" selected>启用</option>
                <option value="0">禁用</option>
              </select>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="dealSaveBtn" onclick="ad.groupSave()"><i class="fa fa-save"></i>提交</button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>
      </div>
    </div>
  </div>
</div>




<script src="${ctx!}/admin/pub/js/operation/banner/group/index.js"></script>

</@bodyContent>