<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>

<@headContent/>
<@bodyContent>

<div class="panel panel-default">
  <div class="panel-body table-content">
<#--    <form class="table-search">-->
<#--      <div class="form-inline">-->
<#--        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>-->
<#--        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i></button>-->
<#--      </div>-->
<#--    </form>-->
    <div class="table-toolbar">
      <button onclick="tp.openModal('add')" type="button" class="btn btn-info">
        <i class="fa fa-plus"></i>
        新增
      </button>
      <button onclick="tp.openModal('edit')" type="button" class="btn btn-warning">
        <i class="fa fa-edit"></i>
        修改
      </button>
    </div>
    <table id="table_list" class="table-striped"></table>
  </div>
</div>



<div class="modal" id="codeModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
          class="sr-only">关闭</span>
        </button>
        <h4 class="modal-title">
          <b id="modal_title">表单</b>
        </h4>
      </div>
      <div class="modal-body">
        <form class="panel-body form-horizontal" id="code_form">
          <input type="hidden" class="form-control" name="id" id="id"/>
          <div class="form-group">
            <label for="code" class="control-label">表单编号(注意：表单编号生成后无法修改！)：</label>
            <div>
              <input type="text" class="form-control" id="code" name="code" required/>
            </div>
          </div>
          <div class="form-group">
            <label for="title" class="control-label">标题：</label>
            <div>
              <input type="text" class="form-control" id="title" name="title" required/>
            </div>
          </div>
          <div class="form-group">
            <label for="beginTime" class="control-label">开始时间：</label>
            <div>
              <input type="text" class="form-control layer-date" autocomplete="off" id="beginTime" name="beginTime" required/>
            </div>
          </div>
          <div class="form-group">
            <label for="endTime" class="control-label">截止时间：</label>
            <div>
              <input type="text" class="form-control layer-date" autocomplete="off" id="endTime" name="endTime" required/>
            </div>
          </div>
          <div class="col-xs-6" style="margin-left:-15px">
            <div class="form-group">
              <label for="status" class="control-label">状态：</label>
              <div>
                <select class="form-control" id="status" name="status"  required>
                  <option value="1" selected>启用</option>
                  <option value="0">禁用</option>
                </select>
              </div>
            </div>
          </div>
          <div class="col-xs-6" style="margin-left:15px">
            <div class="form-group">
              <label for="captcha" class="control-label">是否使用验证码：</label>
              <div>
                <select class="form-control" id="captcha" name="captcha"  required>
                  <option value="false" selected>否</option>
                  <option value="true">是</option>
                </select>
              </div>
            </div>
          </div>
          <div class="col-xs-6" style="margin-left:-15px">
            <div class="form-group">
              <label for="userAuth" class="control-label">是否必须登录：</label>
              <div>
                <select class="form-control" id="userAuth" name="userAuth"  required>
                  <option value="false" selected>否</option>
                  <option value="true">是</option>
                </select>
              </div>
            </div>
          </div>
          <div class="col-xs-6" style="margin-left:15px">
            <div class="form-group">
              <label for="notifyWxAdmin" class="control-label">是否通知微信管理员：</label>
              <div>
                <select class="form-control" id="notifyWxAdmin" name="notifyWxAdmin"  required>
                  <option value="false" selected>否</option>
                  <option value="true">是</option>
                </select>
              </div>
            </div>
          </div>
          <div class="col-xs-4" style="margin-left:-15px">
            <div class="form-group">
              <label for="notifyWxUser" class="control-label">消息通知到用户：</label>
              <div>
                <select class="form-control" id="notifyWxUser" name="notifyWxUser"  required>
                  <option value="0" selected>不通知</option>
                  <option value="1">公众号</option>
                  <option value="2">小程序</option>
                </select>
              </div>
            </div>
          </div>
          <div class="col-xs-8" style="margin-left:15px">
            <div class="form-group">
              <label for="notifyWxUserLink" class="control-label">消息通知到用户链接（以/开头，可用占位符{id}）：</label>
              <div>
                <input class="form-control" id="notifyWxUserLink" name="notifyWxUserLink" />
              </div>
            </div>
          </div>
          <div class="form-group">
            <label for="remark" class="control-label">备注：</label>
            <div>
              <textarea class="form-control" id="remark" name="remark"></textarea>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="dealSaveBtn" onclick="tp.typeSave()"><i
          class="fa fa-save"></i>提交
        </button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>
      </div>
    </div>
  </div>
</div>



<script src="${ctx!}/admin/pub/js/dynamicform/codeindex.js"></script>
</@bodyContent>