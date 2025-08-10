<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
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
    <table id="table_list"></table>
  </div>
</div>

<div class="modal" id="tagModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
          class="sr-only">关闭</span>
        </button>
        <h4 class="modal-title">
          <b id="modal_title">标签管理</b>
        </h4>
      </div>
      <div class="modal-body">
        <form class="panel-body form-horizontal" id="tag_form" name="tag_form">
          <input type="hidden" class="form-control" name="id" id="id"/>

          <div class="form-group">
            <label for="name" class="control-label">标签名称：</label>
            <div>
              <input type="text" class="form-control" id="tagName" name="tagName" required/>
            </div>
          </div>
          <div class="form-group">
            <label for="name" class="control-label">分组名称：</label>
            <div>
              <input type="text" class="form-control" id="groupName" name="groupName" required/>
            </div>
          </div>
          <#--<div class="form-group" id="logoForm">-->
            <#--<label for="main" class="control-label">logo：</label>-->
            <#--<button type="button" class="btn-success " style="position: relative;right: 0"-->
                    <#--onclick="attachment.getAtta('logo').addAtta()"><i-->
              <#--class="fa fa-upload"></i>添加图片-->
            <#--</button>-->
            <#--<div class="form-group" id="attas" style="display: none;">-->
            <#--</div>-->
            <#--<div>-->
              <#--<img class="img-rounded" id="image" style="width:90px" src="/admin/img/avatar.jpg">-->
            <#--</div>-->
            <#--<input type="file" style="display: none" dir="" id="file" name="file" enctype="multipart/form-data"/>-->
          <#--</div>-->
          <div class="form-group">
            <label for="name" class="control-label">是否用于查询条件：</label>
            <select class="form-control" id="useCondition" name="useCondition">
              <option value="false">否</option>
              <option value="true" selected>是</option>
            </select>
          </div>
          <div class="form-group">
            <label for="detail" class="control-label">备注：</label>
            <div>
    <textarea class="form-control" rows="5" placeholder="填写备注..." id="remark" name="remark"
    ></textarea>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="dealSaveBtn" onclick="tp.typeSave()"><i
          class="fa fa-save"></i>提交
        </button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消
        </button>
      </div>
    </div>
  </div>
</div>


<script src="${ctx!}/admin/pub/js/sys/tag/index.js"></script>
<script src="${ctx!}/admin/pub/js/attachment/attachment_mulit.js"></script>
</@bodyContent>