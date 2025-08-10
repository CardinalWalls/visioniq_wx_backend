<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>

<@headContent/>
<@bodyContent>

<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <input type="hidden" id="registRegion" name="registRegion"/>
        <input type="hidden" id="regionId" name="regionId"/>
        <div class="form-group">
            <span class="m-r-lg">
              注册时间 <i class="fa fa-question-circle" title="为提升效率，跨度超过90天的查询，将不提供时间排序" style="color:#23b7e5"></i> ：
              <input type="text" placeholder="请选择开始时间" id="registTimeStart"
                     name="registTimeStart" class="form-control layer-date" style="background:#fff" readonly> -
              <input type="text" placeholder="请选择结束时间" id="registTimeEnd"
                     name="registTimeEnd" class="form-control layer-date" style="background:#fff" readonly>
            </span>
        </div>
        <div class="form-group">
          <label for="searchPhone" class="sr-only">手机号</label>
          <input type="text" placeholder="手机号" name="searchPhone" class="form-control">
        </div>
        <div class="form-group">
          <label for="searchUserNickName" class="sr-only">用户姓名</label>
          <input type="text" placeholder="用户姓名" name="searchUserNickName"
                 class="form-control">
        </div>
        <div class="form-group">
          <label for="searchUserNickName" class="sr-only">推荐人名称</label>
          <input type="text" placeholder="推荐人名称" name="parentUserName"
                 class="form-control">
        </div>
        <div class="form-group">
          <label for="searchUserNickName" class="sr-only">推荐人手机号</label>
          <input type="text" placeholder="推荐人手机号" name="parentUserPhone"
                 class="form-control">
        </div>

        <div class="form-group">
          <label for="dataSource" class="sr-only">来源</label>
          <select class="form-control" name="dataSource">
            <option value="">全部（不含机器人）</option>
            <option value="0">PC</option>
            <option value="1">MOBILE</option>
            <option value="2">存量数据</option>
            <option value="3">机器人</option>
          </select>
        </div>

        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
    <div class="table-toolbar">
      <#if superRole==1>
        <button onclick="pi.showIntegral()" type="button" class="btn btn-info">
          <i class="fa fa-plus"></i>
          赠送积分
        </button>
      </#if>
      <button onclick="pi.openRecharge();" type="button" class="btn btn-success">
        <i class="fa fa-plus"></i>
        余额充值
      </button>
    </div>
    <table id="table_list" class="table-striped"></table>
  </div>
</div>

  <div class="modal" id="giveIntegralModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
            class="sr-only">关闭</span>
          </button>
          <h4 class="modal-title">
            <b id="modal_title">赠送积分</b>
          </h4>
        </div>
        <div class="modal-body">
          <form class="panel-body form-horizontal" id="integral_form" name="integral_form">
            <input type="hidden" class="form-control" name="userId" id="give_user_id"/>
            <div class="form-group">
              <label for="name" class="control-label">赠送来源：</label>
              <div>
                <select class="form-control" name="srcType">
                  <option value="0">活动奖励</option>
                  <#--<option value="5">企业服务</option>-->
                </select>
              </div>
            </div>
            <div class="form-group">
              <label for="name" class="control-label">积分数量：</label>
              <div>
                <input type="number" class="form-control" name="integral" required/>
              </div>
            </div>
            <div class="form-group">
              <label for="name" class="control-label">备注：</label>
              <div>
                <input type="text" class="form-control" name="remark"/>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" id="dealSaveBtn" onclick="pi.saveIntegral()"><i
            class="fa fa-save"></i>提交
          </button>
          <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消
          </button>
        </div>
      </div>
    </div>
  </div>

  <div class="modal" id="detailModal" tabindex="-1" role="dialog" style="padding: 0;border: 0;margin: 0;"
       aria-hidden="true">
    <div class="modal-dialog" style="width: 100%;height: 100%;padding: 0;border: 0;margin: 0;">
      <div class="modal-content" style="width: 100%;height: 100%;padding: 0;border: 0;margin: 0;">
        <div class="modal-header" style="border: 0;margin: 0;">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
            class="sr-only">关闭</span>
          </button>
          <h4 class="modal-title">
            <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-chevron-left"></i> 返回用户列表
            </button>
          </h4>
        </div>
        <iframe id="detailIframe" src=""
                style="width: 100%;border: 0;margin: 0;padding: 0;z-index: 999"></iframe>
      </div>
    </div>
  </div>


  <div class="modal" id="rechargeModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                class="sr-only">关闭</span>
          </button>
          <h4 class="modal-title">
            <b id="modal_title">用户充值</b>
          </h4>
        </div>
        <div class="modal-body">
          <form class="panel-body form-horizontal" id="recharge_form" name="recharge_form">
            <input type="hidden" class="form-control" name="userId" />
            <div class="form-group">
              <label for="name" class="control-label">充值金额：</label>
              <div>
                <input type="number" class="form-control" name="amount" required/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label">凭证截图：</label>
              <button type="button" class="btn btn-success btn-xs" onclick="attachment.uploadFile(10, 'rechargeAttachment')">
                <i class="fa fa-upload"></i>上传
              </button>
              <input class="form-control attachment-field" name="rechargeAttachment" id="rechargeAttachment"
                     customImageStyle="max-width:80px;" required/>
            </div>
            <div class="form-group">
              <label for="name" class="control-label">备注：</label>
              <div>
                <input type="text" class="form-control" name="remark"/>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" id="dealSaveBtn" onclick="pi.saveRecharge()"><i
                class="fa fa-save"></i>提交
          </button>
          <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消
          </button>
        </div>
      </div>
    </div>
  </div>
  <script src="${ctx!}/admin/pub/js/attachment/attachmentTool.js"></script>
  <script src="${ctx!}/admin/pub/js/user/manager/index.js"></script>
</@bodyContent>