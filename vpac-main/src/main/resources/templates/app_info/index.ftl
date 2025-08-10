<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
<@bodyContent>
<link rel="stylesheet" type="text/css" href="${ctx!}/admin/pub/css/app_info/app_info.css" />
  <div class="col-xs-12 col-sm-6">
    <div class="panel panel-default">
      <div class="panel-heading">
        <h4>应用菜单</h4>
      </div>
      <div class="panel-body">
        <div class="row">
          <div class="col-xs-12">
            <div id="app_tree" class="app-tree"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="col-xs-12 col-sm-6">
    <div class="panel panel-default">
      <div class="panel-heading text-center">
        <button onclick="ap.add(0)" type="button" class="btn btn-success" id="add_dir_btn" disabled>
          <i class="fa fa-plus"></i>
          新增目录
        </button>
        <button onclick="ap.add(1)" type="button" class="btn btn-info" id="add_app_btn" disabled>
          <i class="fa fa-plus"></i>
          新增菜单页面
        </button>
        <button onclick="ap.add(2)" type="button" class="btn btn-primary" id="add_op_btn" disabled>
          <i class="fa fa-plus"></i>
          新增功能资源
        </button>
        <button onclick="ap.del()" type="button" id='delete_btn' class="btn btn-danger">
          <i class="fa fa-trash-o"></i>
          删除
        </button>
      </div>
      <form class="panel-body m-h" id="app_form" onsubmit="return false;">
        <input type="hidden" name="parentId" id="parentId"/>
        <div class="form-group">
          <label for="id" class="control-label">ID：</label>
          <div>
            <input type="text" class="form-control" id="id" readonly="readonly"/>
          </div>
        </div>
        <div class="form-group">
          <label for="name" class="control-label">名称：</label>
          <div>
            <input type="text" class="form-control" name="name" id="name" required="required"/>
          </div>
        </div>
        <div class="form-group">
          <label for="description" class="control-label">描述：</label>
          <div>
            <input type="text" class="form-control" name="description" id="description" />
          </div>
        </div>
        <div class="form-group">
          <label for="iconClass" class="control-label">图标：</label>
          <div>
            <input type="text" class="form-control" name="iconClass" id="iconClass" onclick="ap.openIcon(this)" />
          </div>
        </div>
        <div class="form-group">
          <label for="targetUrl" class="control-label">跳转路径：</label>
          <div>
            <input type="text" class="form-control" name="targetUrl" id="targetUrl" />
          </div>
        </div>
        <div class="form-group">
          <label for="authUrlPrefix" class="control-label">授权资源URI（Spring MVC 语法），不能使用根路径：</label>
          <a onclick="ap.addAuthItem()" class="btn btn-success btn-xs" id="addAuthItemBtn" disabled><i class="fa fa-plus"></i>添加授权URI</a>
          <div class="form-inline" id="authUrlPrefixDiv">
          </div>
        </div>
        <div class="form-group">
          <label for="directory" class="control-label">菜单类别：</label>
          <input type="hidden" name="type" id="type" />
          <span id="typeName"></span>
        </div>
        <div class="form-group">
          <label for="status" class="control-label">是否启用：</label>
          <input type="checkbox" id="status"/>
        </div>
        <div class="form-group" align="center">
          <button type="button" class="btn btn-primary" id="sub_btn" onclick="ap.formSubmit()" disabled>
            <i class="fa fa-save"></i>
            保存
          </button>
        </div>
      </form>
    </div>
  </div>

<script src="${ctx!}/admin/pub/js/app_info/index.js"></script>
<script src="${ctx!}/admin/pub/plugin/icons/selector.icons.js"></script>
  <#include "${_ROOT_PATH_}/macro/fancytree.ftl">
  <@fancytree/>
</@bodyContent>