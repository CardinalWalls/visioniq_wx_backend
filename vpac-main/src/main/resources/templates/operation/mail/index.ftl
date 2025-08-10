<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>

<@headContent/>
<@bodyContent>

<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <label for="startDate" >开始时间</label>
          <input type="text" placeholder="请选择开始时间" id="startDate" name="startDate" class="form-control layer-date">
        </div>
        <div class="form-group">
          <label for="endDate" >结束时间</label>
          <input type="text" placeholder="请选择结束时间" id="endDate" name="endDate" class="form-control layer-date">
        </div>
        <div class="form-group">
          <label for="keywords" >关键词</label>
          <input type="text" placeholder="请输入查询关键词" id="keywords" name="keywords" class="form-control">
        </div>
        <div class="form-group">
          <label for="withdrawStatus" >状态</label>
          <select id="status" name="status" class="form-control">
            <option value="">全部状态</option>
            <option value="0">未发送</option>
            <option value="1">已发送</option>
          </select>
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i></button>
      </div>
    </form>
    <div class="table-toolbar">
      <button onclick="createMail()" type="button" class="btn btn-info">
        <i class="fa fa-plus"></i>
        创建邮件
      </button>
      <button onclick="configServer()" type="button" class="btn btn-warning">
        <i class="fa fa-edit"></i>
        服务器设置
      </button>
    </div>
    <table id="table_list" class="table-striped"></table>
  </div>
</div>

  <div class="modal" id="mailBoxModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
          </button>
          <h4 class="modal-title">
            <i class="fa fa-group"></i>
            <b id="modal_title"></b>配置邮件服务器
          </h4>
        </div>

        <div class="modal-body">
          <form class="panel-body form-horizontal" id="mailbox_form">
            <input type="hidden" class="form-control" name="id" id="box_id"/>
            <div class="form-group">
              <label for="address" >邮箱地址</label>
              <input type="text" placeholder="邮箱地址" id="address" name="address" class="form-control">
            </div>
            <div class="form-group">
              <label for="smtp" >smtp服务器</label>
              <input type="text" placeholder="smtp服务器" id="smtp" name="smtp" class="form-control">
            </div>
            <div class="form-group">
              <label for="password" >密码</label>
              <input type="text" placeholder="密码" id="password" name="password" class="form-control"/>
            </div>
            <div class="form-group">
              <label for="sign" >签名</label>
              <input type="text" placeholder="签名" id="sign" name="sign" class="form-control"/>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" id="dealSaveBtn" onclick="saveMailBox()"><i
                class="fa fa-save"></i>提交
          </button>
          <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>
        </div>
      </div>
    </div>
  </div>


  <div class="modal" id="createMailModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
        </button>
        <h4 class="modal-title">
          <i class="fa fa-group"></i>
          <b id="modal_title"></b>创建/修改邮件
        </h4>
      </div>
      <div class="modal-body">
        <form class="panel-body form-horizontal" id="mail_form">
          <input type="hidden" class="form-control" name="id" id="id"/>
          <div class="form-group">
            <label for="subject" >邮件主题</label>
            <input type="text" placeholder="邮件主题" id="subject" name="subject" class="form-control">
          </div>
          <div class="form-group">
            <label for="addressee" >收件人</label>
            <textarea placeholder="收件人(多个以逗号分隔)" id="addressee" name="addressee" class="form-control"></textarea>
          </div>
          <div class="form-group">
            <label for="remarks" >备注</label>
            <input type="text" placeholder="备注" id="remark" name="remark" class="form-control"/>
          </div>
          <div class="form-group">
            <label for="content" >内容：</label>
            <div class="col-xs-12">
              <#assign keditor_id="keditor"/>
              <#assign keditor_height="400px"/>
              <#include "${_ROOT_PATH_}/kindeditor/include.ftl"/>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="dealSaveBtn" onclick="saveMail()"><i class="fa fa-save"></i>提交
        </button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>
      </div>
    </div>
  </div>
</div>






<script src="${ctx!}/admin/pub/js/operation/mail/index.js"></script>
</@bodyContent>