<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
    <style>
      .width-col{
        max-width:400px;
        word-break: break-all;
        height:20px;
        overflow:hidden;
        font-size: 15px;
        line-height: 25px;
      }
      .width-col.name-col{
        max-width:150px;
      }
      .width-col.json-col.show{
        white-space:pre-wrap;
        max-height:150px;
        overflow-y:auto;
      }
      .width-col.show{
        max-width:none;
        height:auto;
      }
    </style>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <input type="text" placeholder="开始时间" name="startTime" id="loginStart" class="form-control layer-date" title="开始时间">
        </div>
        <div class="form-group">
          <input type="text" placeholder="结束时间" name="endTime" id="loginEnd" class="form-control layer-date" title="结束时间">
        </div>
        <div class="form-group">
          <input type="text" placeholder="userId" name="userId" class="form-control" title="userId">
        </div>
        <div class="form-group">
          <input type="text" placeholder="登录对象信息" name="tokenObjJson" class="form-control" title="登录对象信息">
        </div>
        <div class="form-group">
          <input type="text" placeholder="登录的token" name="token" class="form-control" title="登录的token">
        </div>
        <br/>
        <div class="form-group">
          <select name="tokenType" class="form-control" id="loginTokenType" title="用户类型">
            <option value="">- 用户类型 -</option>
          </select>
        </div>
        <div class="form-group">
          <input type="text" placeholder="登录IP" name="loginIp" class="form-control" title="登录IP">
        </div>
        <div class="form-group">
          <input type="text" placeholder="登录地区" name="loginLocation" class="form-control" title="登录地区">
        </div>
        <div class="form-group">
          <input type="text" placeholder="应用服务器" name="serverHost" class="form-control" title="应用服务器">
        </div>

        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
      <div class="table-toolbar">
        <label class="label label-info">登录日志</label>
      </div>
    <table id="tokenLoginLog_table_list" class="table-striped"></table>
  </div>
</div>

<div class="panel panel-default" >
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <input type="text" placeholder="开始时间" name="startTime" id="requestStart" class="form-control layer-date" title="开始时间">
        </div>
        <div class="form-group">
          <input type="text" placeholder="结束时间" name="endTime" id="requestEnd" class="form-control layer-date" title="结束时间">
        </div>
        <div class="form-group">
          <input type="text" placeholder="登录的token" name="token" id="requestToken" class="form-control" title="登录的token">
        </div>
        <div class="form-group">
          <input type="text" placeholder="请求uri" name="uri" class="form-control" title="请求uri">
        </div>
        <div class="form-group">
          <select name="method" class="form-control" title="Http方法">
            <option value="">- HTTP方法 -</option>
            <option value="POST">POST</option>
            <option value="PUT">PUT</option>
            <option value="DELETE">DELETE</option>
            <option value="GET">GET</option>
          </select>
        </div>
        <br/>
        <div class="form-group">
          <select name="sameIp" class="form-control" title="是否IP异常">
            <option value="">- 是否IP异常 -</option>
            <option value="true">正常IP</option>
            <option value="false">异常IP</option>
          </select>
        </div>
        <div class="form-group">
          <input type="text" placeholder="请求参数" name="params" class="form-control" title="请求参数">
        </div>
        <div class="form-group">
          <input type="text" placeholder="请求IP" name="ip" class="form-control" title="请求IP">
        </div>
        <div class="form-group">
          <input type="text" placeholder="请求地区" name="ipLocation" class="form-control" title="请求地区">
        </div>
        <div class="form-group">
          <input type="text" placeholder="应用服务器" name="serverHost" class="form-control" title="应用服务器">
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
    <div class="table-toolbar">
      <button class="btn btn-success" id="notGroupBtn" onclick="tokenRequestLog.changeGroup(0)">请求日志</button>
      <button class="btn btn-white" id="doGroupBtn" onclick="tokenRequestLog.changeGroup(1)">请求日志分组</button>
    </div>
    <table id="tokenRequestLog_table_list" class="table-striped"></table>
  </div>
</div>

<input type="hidden" id="tokenTypesJson" value="${tokenTypes}">
<script>
  var _tokenTypes_ = ${tokenTypes};
</script>
<script src="${ctx!}/admin/pub/js/sys/tokenLoginLog/index.js?v=1.2"></script>
<script src="${ctx!}/admin/pub/js/sys/tokenRequestLog/index.js?v=1.2"></script>

</@bodyContent>