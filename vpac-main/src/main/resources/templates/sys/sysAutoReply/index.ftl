<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
  <script>
    var types = ${typesJson};
  </script>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <input type="text" placeholder="关键词" name="keyword" class="form-control" title="关键词">
        </div>
        <div class="form-group">
          <select name="type" class="form-control" title="关键词">
            <option value="">- 分类 -</option>
            <#list types?keys as key>
              <option value="${key}">${types[key]}</option>
            </#list>
          </select>
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
      <div class="table-toolbar">
        <button onclick="sysAutoReply.edit(true)" type="button" class="btn btn-white">
          <i class="fa fa-list-alt"></i>
          查看
        </button>
        <button onclick="sysAutoReply.add()" type="button" class="btn btn-info">
          <i class="fa fa-plus"></i>
          新增
        </button>
        <button onclick="sysAutoReply.edit()" type="button" class="btn btn-warning">
          <i class="fa fa-edit"></i>
          修改
        </button>
        <button onclick="sysAutoReply.del()" type="button" class="btn btn-danger">
          <i class="fa fa-remove"></i>
          删除
        </button>
      </div>
    <table id="sysAutoReply_table_list" class="table-striped"></table>
  </div>
</div>
<div class="modal" id="sysAutoReply_modal" tabindex="-1" role="dialog" aria-hidden="true">
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
            <label class="control-label">分类 <b class="text-danger">*</b>：</label>
            <div>
              <select name="type" class="form-control" title="分类" required>
                <#list types?keys as key>
                  <option value="${key}">${types[key]}</option>
                </#list>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">关键词（多个用逗号隔开）：</label>
            <div>
              <input type="text" class="form-control" name="keyword"/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">匹配关键词时的最大文字长度：</label>
            <div>
              <input type="text" class="form-control" name="matchLengthMax" initValue="6" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">回复文字内容 <b class="text-danger">*</b>：</label>
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
          <div class="form-group">
            <label class="control-label">排序 <b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="sortNo" initValue="0" required/>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary save-btn" onclick="sysAutoReply.save()"><i class="fa fa-save"></i>提交</button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</button>
      </div>
    </div>
  </div>
</div>
<script src="${ctx!}/admin/pub/js/sys/sysAutoReply/index.js"></script>

</@bodyContent>