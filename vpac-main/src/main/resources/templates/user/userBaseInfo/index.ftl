<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
<script>
  var userTypes = ${userTypes};
</script>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <input type="text" placeholder="手机号" name="phone" id="searchPhone" class="form-control" title="手机号">
        </div>
        <div class="form-group">
          <input type="text" placeholder="姓名" name="userNickName" class="form-control" title="姓名">
        </div>
        <div class="form-group">
          <input type="text" placeholder="身份证" name="idCard" class="form-control" title="身份证">
        </div>
        <div class="form-group">
          <input type="text" placeholder="街道" name="streetName" class="form-control" title="街道">
        </div>
        <div class="form-group">
          <input type="text" placeholder="社区" name="communityName" class="form-control" title="社区">
        </div>
        <div class="form-group">
          <input type="text" placeholder="绑定专家名称" name="expertName" class="form-control" title="绑定专家名称">
        </div>
        <div class="form-group">
          <input type="text" placeholder="绑定专家手机号" name="expertPhone" class="form-control" title="绑定专家手机号">
        </div>
        <div class="form-group">
          <select name="userTypeId" class="form-control" title="身份证">
            <option value="">- 用户类别 -</option>
            <#list userTypeMap?keys as key>
              <option value="${key}">${userTypeMap[key]}</option>
            </#list>
          </select>
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
      <div class="table-toolbar">
        <button onclick="baseUserBaseInfo.edit(true)" type="button" class="btn btn-white">
          <i class="fa fa-list-alt"></i>
          查看
        </button>
        <button onclick="baseUserBaseInfo.add()" type="button" class="btn btn-info">
          <i class="fa fa-plus"></i>
          新增
        </button>
        <button onclick="baseUserBaseInfo.edit()" type="button" class="btn btn-warning">
          <i class="fa fa-edit"></i>
          修改
        </button>
<#--        <button onclick="baseUserBaseInfo.showImportData()" type="button" class="btn btn-success">-->
<#--          <i class="fa fa-plus"></i>-->
<#--          批量导入-->
<#--        </button>-->
      </div>
    <table id="baseUserBaseInfo_table_list" class="table-striped"></table>
  </div>
</div>
<div class="modal" id="baseUserBaseInfo_modal" tabindex="-1" role="dialog" aria-hidden="true">
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
          <div class="form-group form-line-l col-xs-4">
            <label class="control-label">姓名<b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="userNickName" required/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">手机号<b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="phone" id="phone" required/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4" title="新账号默认密码登录手机号后6位">
            <label class="control-label" id="pwdLabel">登录密码：</label>
            <div>
              <input type="text" class="form-control" name="pwd" id="pwd"/>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-4">
            <label class="control-label">身份证：</label>
            <div>
              <input type="text" class="form-control" name="idCard" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">真实姓名：</label>
            <div>
              <input type="text" class="form-control" name="realName" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">性别：</label>
            <div>
              <select class="form-control" name="gender">
                <option value="1">男</option>
                <option value="2">女</option>
                <option value="0">未选择</option>
              </select>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-4">
            <label class="control-label">备注：</label>
            <div>
              <input type="text" class="form-control" name="remark"/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">状态：</label>
            <div>
              <select class="form-control" name="status">
                <option value="1">正常</option>
                <option value="0">冻结</option>
              </select>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">用户类型：</label>
            <div>
              <select class="form-control" name="userType" id="userType">
              </select>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-12">
            <label class="control-label">绑定专家（会同步修改此账号的所有档案）：</label>
            <div>
              <input class="form-control" name="expertId" id="expertId" />
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary save-btn" onclick="baseUserBaseInfo.save()"><i class="fa fa-save"></i>提交</button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</button>
      </div>
    </div>
  </div>
</div>

<div class="modal" id="import_modal" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                class="sr-only">关闭</span>
          </button>
          <h4 class="modal-title">
            批量导入个人用户
          </h4>
        </div>
        <div class="modal-body">
          <form class="panel-body form-horizontal">
            <div class="form-group">
              <label class="control-label text-info"><i class="fa fa-info-circle"></i> 导入用户的默认密码为手机号后6位</label>
            </div>
            <div class="form-group">
              <label class="control-label">数据开始行数<b class="text-danger">*</b>：</label>
              <div>
                <input type="text" class="form-control" name="startRow" initValue="2" required/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label">导入模式<b class="text-danger">*</b>：</label>
              <div>
                <select class="form-control" name="toUpdate" required>
                  <option value="false">只新增不覆盖</option>
                  <option value="true">覆盖更新</option>
                </select>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label">导入模板文件<b class="text-danger">*</b>：</label>
              <button type="button" class="btn btn-success" onclick="attachment.uploadFile(1, 'dataFile')">
                <i class="fa fa-upload"></i> 上传
              </button>
              <a class="m-l" href="/admin/pub/js/user/userBaseInfo/import_user.xlsx" target="_blank" download="导入个人用户模板.xlsx">
                <b>下载导入模板<span class="text-danger">（请勿修改模板列顺序）</span></b>
              </a>
              <div>
                <input class="form-control attachment-field" name="dataFile" id="dataFile" required/>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" onclick="baseUserBaseInfo.importData()">
            <i class="fa fa-save"></i> 保存
          </button>
          <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i> 关闭</button>
        </div>
      </div>
    </div>
</div>
<script src="${ctx!}/admin/pub/js/attachment/attachmentTool.js"></script>
<script src="${ctx!}/admin/pub/js/user/userBaseInfo/index.js"></script>

</@bodyContent>