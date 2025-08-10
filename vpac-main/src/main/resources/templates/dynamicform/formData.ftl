<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>

<@headContent/>
    <script type="application/javascript">
      var formCode="${code}";
    </script>
<@bodyContent>

<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i></button>
      </div>
    </form>
    <div class="table-toolbar">
    </div>
    <table id="table_list" class="table-striped"></table>
  </div>
</div>






<script src="${ctx!}/admin/pub/js/dynamicform/formData.js"></script>
</@bodyContent>