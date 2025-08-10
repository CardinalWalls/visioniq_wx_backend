<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>

<@headContent/>
<style>
  #otherquery .form-group{
    margin-right:3px;
  }
  .editable-text{
    color:#BBB;
    font-style:italic;
  }
  .editable-click{
    border:0 !important;
  }
</style>
<@bodyContent>
<input type="hidden" id="paramStatus" value="${RequestParameters["status"]}"/>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search" id="searchForm">
      <div class="form-inline">
          <div class="form-group">
            <select style="font-weight:bold;${(RequestParameters['code'] != null)?string('display:none','')}" id="code" name="code" class="form-control" notAutoRefresh="true" title="编号" onchange="tp.search()">
            </select>
          </div>

        <div class="form-group">
          <select name="status" id="searchStatus" class="form-control" notAutoRefresh="true"  onchange="tp.search()">
            <option value="">选择状态</option>
            <option value="1">未处理</option>
            <option value="2">已处理</option>
            <option value="0">已取消</option>
          </select>
        </div>
        <div class="form-group">
          <input name="userPhone" class="form-control" notAutoRefresh="true" placeholder="平台用户手机号" />
        </div>
        <div class="form-group">
          <input name="userNickName" class="form-control" notAutoRefresh="true" placeholder="平台用户昵称" />
        </div>
        <div class="form-group" id="otherquery"></div>
        <button type="button" class="btn btn-info" onclick="tp.search()" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white " onclick="$('#searchForm .form-control:not(#code)').val('');tp.search()"title="清空条件"><i class="fa fa-recycle"></i></button>
      </div>
    </form>
    <div class="table-toolbar">
      <button onclick="tp.clickAutoRefreshBtn(this,0)" id="autoRefreshBtn" title="暂停刷新" class="btn btn-warning btn-xs"><i class="fa fa-pause"></i></button>
      <label class="m-l-xs">刷新数据（秒）：<span id="autoRefreshTime"></span></label> /
      <input type="text" value="60" title="刷新间隔" style="width:30px" onkeyup="tp.autoRefreshLimit = this.value <= 0 ? 50 : this.value * 1;" />
    </div>
    <table id="table_list" class="table-striped"></table>
  </div>
</div>


<div class="modal" id="childModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
          class="sr-only">关闭</span>
        </button>
        <h4 class="modal-title">
          <b id="modal_title">附属信息</b>
        </h4>
      </div>
      <div class="modal-body">

        <div class="table-content">
          <form class="table-search">
            <div class="form-inline">
              <select id="type" name="type" class="form-control">
                <option value="">请选择附属信息类型</option>
              </select>

              <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
              <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i></button>
            </div>
          </form>
          <div class="table-toolbar">
          </div>
          <table id="child_list" class="table-striped"></table>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>
      </div>
    </div>
  </div>
</div>


<script src="${ctx!}/admin/pub/js/attachment/attachmentTool.js"></script>
<script src="${ctx!}/admin/pub/js/dynamicform/index.js?v=1.1"></script>
</@bodyContent>