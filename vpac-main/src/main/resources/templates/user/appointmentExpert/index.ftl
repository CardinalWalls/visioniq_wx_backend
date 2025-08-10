<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <input type="text" placeholder="预约时间开始" name="targetTimeStart" class="form-control layer-date" title="预约时间开始">
        </div>
        <div class="form-group">
          <input type="text" placeholder="预约时间截止" name="targetTimeEnd" class="form-control layer-date" title="预约时间截止">
        </div>
        <div class="form-group">
          <input type="text" placeholder="专家姓名" name="expertName" class="form-control" title="专家姓名">
        </div>
        <div class="form-group">
          <input type="text" placeholder="档案姓名" name="userName" class="form-control" title="档案姓名">
        </div>
        <div class="form-group">
          <input type="text" placeholder="父母手机" name="parentPhone" class="form-control" title="父母手机">
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
      <div class="table-toolbar">
<#--        <button onclick="appointmentExpert.edit(true)" type="button" class="btn btn-white">-->
<#--          <i class="fa fa-list-alt"></i>-->
<#--          查看-->
<#--        </button>-->
<#--        <button onclick="appointmentExpert.add()" type="button" class="btn btn-info">-->
<#--          <i class="fa fa-plus"></i>-->
<#--          新增-->
<#--        </button>-->
<#--        <button onclick="appointmentExpert.edit()" type="button" class="btn btn-warning">-->
<#--          <i class="fa fa-edit"></i>-->
<#--          修改-->
<#--        </button>-->
<#--        <button onclick="appointmentExpert.del()" type="button" class="btn btn-danger">-->
<#--          <i class="fa fa-remove"></i>-->
<#--          删除-->
<#--        </button>-->
      </div>
    <table id="appointmentExpert_table_list" class="table-striped"></table>
  </div>
</div>
<div class="modal" id="appointmentExpert_modal" tabindex="-1" role="dialog" aria-hidden="true">
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
            <label class="control-label">创建时间：</label>
            <div>
              <input type="text" class="form-control layer-date" name="createTime" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">用户ID：</label>
            <div>
              <input type="text" class="form-control" name="userId" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">专家ID：</label>
            <div>
              <input type="text" class="form-control" name="expertId" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">预约时间：</label>
            <div>
              <input type="text" class="form-control" name="targetTime" required/>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary save-btn" onclick="appointmentExpert.save()"><i class="fa fa-save"></i>提交</button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</button>
      </div>
    </div>
  </div>
</div>
<script src="${ctx!}/admin/pub/js/user/appointmentExpert/index.js"></script>

</@bodyContent>