<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>

<@headContent/>
<@bodyContent>

<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">

        <div class="form-group">
          <select class="form-control" name="userTypeId" onchange="ms.search('table_list')">
            <option value="" selected>人员类别 - 全部</option>
            <#list userTypes as userType>
              <option value="${userType.id}">人员类别 - ${userType.userTypeName}</option>
            </#list>
          </select>
        </div>
        <div class="form-group">
          <select class="form-control" name="status" onchange="ms.search('table_list')">
            <option value="">状态 - 全部</option>
            <option value="1">状态 - 已读</option>
            <#if "${RequestParameters['status']}"?? && "${RequestParameters['status']}"=="0">
              <option value="0" selected>状态 - 未读</option>
            <#else>
              <option value="0">状态 - 未读</option>
            </#if>
          </select>
        </div>
        <div class="form-group">
          <input placeholder="姓名"  name="userNickName" class="form-control search-input">
        </div>
        <div class="form-group">
          <input placeholder="手机号"  name="phone" class="form-control search-input">
        </div>
        <div class="form-group">
          <input placeholder="信息内容"  name="content" class="form-control search-input">
        </div>

        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i></button>

      </div>
    </form>
    <div class="table-toolbar">
      <button type="button" class="btn btn-white" onclick="ms.del()">
        <i class="fa fa-close"></i>
        删除
      </button>
    </div>
    <table id="table_list" class="table-striped"></table>
  </div>
</div>



<script src="${ctx!}/admin/pub/js/message/index.js"></script>
</@bodyContent>