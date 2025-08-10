<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>

<@headContent/>
<@bodyContent>
<style type="text/css">
  #table_list {table-layout:fixed;word-break:break-all}
  .status-item{display:inline-block;margin-right:3px;margin-bottom:5px}
</style>
<script>
  var messageChannels = ${messageChannels!'{}'};
</script>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <label for="startDate" class="sr-only">开始时间</label>
          <input type="text" style="background-color:#FFF" placeholder="请选择开始时间" id="startDate" name="startDate" class="form-control layer-date" readonly>
        </div>
        <div class="form-group">
          <label for="endDate" class="sr-only">结束时间</label>
          <input type="text" style="background-color:#FFF" placeholder="请选择结束时间" id="endDate" name="endDate" class="form-control layer-date" readonly>
        </div>
        <div class="form-group">
          <select class="form-control" id="hasDone" name="hasDone" onchange="eventHandle.refreshGrid()">
            <option value="">全部</option>
            <option value="-1">执行中</option>
            <option value="0">未开始</option>
            <option value="1">已完成</option>
            <option value="2">异常</option>
            <option value="3">其它</option>
          <#-- Integer.MIN -->
            <option value="-2147483648">无监听</option>
          </select>
        </div>
        <div class="form-group">
          <select class="form-control" id="channel" name="channel" onchange="eventHandle.refreshGrid()">
            <option value="">全部</option>
          </select>
        </div>

        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i></button>
      </div>
    </form>
    <div class="table-toolbar">
      <button type="button" class="btn btn-danger" onclick="eventHandle.del()"><i class="fa fa-close"></i> 删除</button>
    </div>
    <table id="table_list" class="table-striped"></table>
  </div>

  <div class="panel-body">
    <div class="row">
      <div class="col-xs-12"><table id="table_list"></table></div>
    </div>
    <div class="row">
      <div class="col-xs-12">
        <div class="panel panel-default">
          <div class="panel-heading">
            监听事件处理列表 - <span class="label label-primary" id="choose_event_channel"></span> - <span class="label label-primary" id="choose_event_time"></span>
          </div>
          <div class="panel-body">
            <div class="table-content">
            <#--<form class="table-search">-->
            <#--<div class="form-inline">-->
            <#--<button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>-->
            <#--<button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i></button>-->
            <#--</div>-->
            <#--</form>-->
              <div class="table-toolbar">
              </div>
              <table id="event_handle" class="table-striped"></table>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>

</div>



<script src="${ctx!}/admin/pub/js/sys/event/index.js?v=1.0"></script>
</@bodyContent>