<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
<link rel="stylesheet" type="text/css" href="${ctx!}/admin/pub/css/common/condition_tool.css" />
<script>
  var conditionFields = ${conditionFields};
</script>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <input class="form-control" name="typeId" id="searchTypeId" placeholder="类型" title="类型"/>
        </div>
        <div class="form-group">
          <input type="text" placeholder="文字内容" name="content" class="form-control" title="文字内容">
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
      <div class="table-toolbar">
        <button onclick="userPushMessage.edit(true)" type="button" class="btn btn-white">
          <i class="fa fa-list-alt"></i>
          查看
        </button>
        <button onclick="userPushMessage.add()" type="button" class="btn btn-info">
          <i class="fa fa-plus"></i>
          新增
        </button>
        <button onclick="userPushMessage.edit()" type="button" class="btn btn-warning">
          <i class="fa fa-edit"></i>
          修改
        </button>
        <button onclick="userPushMessage.del()" type="button" class="btn btn-danger">
          <i class="fa fa-remove"></i>
          删除
        </button>
      </div>
    <table id="userPushMessage_table_list" class="table-striped"></table>
  </div>
</div>
<div class="modal" id="userPushMessage_modal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
              class="sr-only">关闭</span>
        </button>
        <h4 class="modal-title">
        </h4>
      </div>
      <div class="modal-body">
        <form class="panel-body form-horizontal" onsubmit="return false;">
          <input type="hidden" class="form-control" name="id"/>
          <div class="form-group">
            <label class="control-label">分类 <b class="text-danger">*</b>：</label>
            <div>
              <input class="form-control" name="typeId" id="typeId" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">文字内容 <b class="text-danger">*</b>：</label>
            <div>
              <textarea class="form-control" name="content" required></textarea>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">链接地址：</label>
            <div>
              <input type="text" class="form-control" name="url"/>
            </div>
          </div>
          <div class="form-group" id="conditionDiv">
          </div>
          <div class="form-group">
            <div class="table-content">
              <div class="table-toolbar">
                <label class="label label-success m-r">查询结果</label>
                <label class="label label-default m-r" id="publishTimeText">数据查询截止至此消息的发布时间：<span></span></label>
                <button onclick="userPushMessage.refreshUserArchive()" type="button" class="btn btn-white" title="根据过滤条件查询">
                  <i class="fa fa-search"></i>
                </button>
              </div>
              <table id="userArchive_table_list" class="table-striped"></table>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary save-btn" onclick="userPushMessage.save()"><i class="fa fa-save"></i>提交</button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</button>
      </div>
    </div>
  </div>
</div>
<script src="${ctx!}/admin/pub/js/common/regionSelect.js"></script>
<script src="${ctx!}/admin/pub/js/common/condition_tool.js"></script>
<script src="${ctx!}/admin/pub/js/user/userPushMessage/index.js"></script>

</@bodyContent>