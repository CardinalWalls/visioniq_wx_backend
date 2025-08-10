<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>

<@headContent/>
<@bodyContent>

<div class="panel panel-default">
  <div class="panel-body table-content">
<#--    <form class="table-search">-->
<#--      <div class="form-inline">-->
<#--        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>-->
<#--        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i></button>-->
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
    </div>
    <table id="table_list" class="table-striped"></table>
  </div>
</div>

<div class="modal" id="columnsModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
          class="sr-only">关闭</span>
        </button>
        <h4 class="modal-title">
          <b id="modal_title">字段数据</b>
        </h4>
      </div>
      <div class="modal-body">
        <form class="panel-body form-horizontal" id="columns_form">
          <input type="hidden" class="form-control" name="id" id="id"/>
          <div class="form-group">
            <label for="code" class="control-label">表单编号：</label>
            <div>
              <input type="text" class="form-control" id="code" name="code" required/>
            </div>
          </div>
          <div class="form-group" id="jsonData_group">
            <label for="jsonData" class="control-label">字段映射：</label>
            <button class="btn btn-success btn-xs m-b-xs" type="button" onclick="tp.addColumns()">增加字段</button>
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



<script src="${ctx!}/admin/pub/js/dynamicform/columnsindex.js"></script>
</@bodyContent>