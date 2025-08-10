<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <input type="text" placeholder="父母手机号" name="userPhone" class="form-control" title="父母手机号">
        </div>
        <div class="form-group">
          <input type="text" placeholder="专家手机号" name="expertPhone" class="form-control" title="专家手机号">
        </div>
        <div class="form-group">
          <input type="text" placeholder="姓名" name="name" class="form-control" title="姓名">
        </div>
        <div class="form-group">
          <input type="text" placeholder="身份证" name="idcard" class="form-control" title="身份证">
        </div>
        <div class="form-group">
          <select name="gender" class="form-control" title="性别">
            <option value="">- 性别 -</option>
            <option value="1">男</option>
            <option value="2">女</option>
          </select>
        </div>
        <div class="form-group">
          <select name="riskLevel" class="form-control" title="风险等级">
            <option value="">- 风险等级 -</option>
            <option value="0">无</option>
            <option value="1">低风险</option>
            <option value="2">中风险</option>
            <option value="3">高风险</option>
          </select>
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
      <div class="table-toolbar">
        <button onclick="userArchive.edit(true)" type="button" class="btn btn-white">
          <i class="fa fa-list-alt"></i>
          查看
        </button>
<#--        <button onclick="userArchive.add()" type="button" class="btn btn-info">-->
<#--          <i class="fa fa-plus"></i>-->
<#--          新增-->
<#--        </button>-->
        <button onclick="userArchive.edit()" type="button" class="btn btn-warning">
          <i class="fa fa-edit"></i>
          修改
        </button>
<#--        <button onclick="userArchive.del()" type="button" class="btn btn-danger">-->
<#--          <i class="fa fa-remove"></i>-->
<#--          删除-->
<#--        </button>-->
      </div>
    <table id="userArchive_table_list" class="table-striped"></table>
  </div>
</div>
<div class="modal" id="userArchive_modal" tabindex="-1" role="dialog" aria-hidden="true">
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
            <label class="control-label">父母手机号：</label>
            <div>
              <input type="text" class="form-control" id="userPhone" disabled/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-8">
            <label class="control-label">所属专家：</label>
            <div>
              <input class="form-control" name="expertId" id="expertId" />
            </div>
          </div>
          <div class="form-group form-line-l col-xs-4">
            <label class="control-label">姓名：</label>
            <div>
              <input type="text" class="form-control" name="name" required/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">性别：</label>
            <div>
              <select name="gender" class="form-control" title="性别" required>
                <option value="1">男</option>
                <option value="2">女</option>
              </select>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">出生日期：</label>
            <div>
              <input type="text" class="form-control layer-date" name="birth" required/>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-4">
            <label class="control-label">身份证：</label>
            <div>
              <input type="text" class="form-control" name="idcard" required/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">风险等级：</label>
            <div>
              <select name="riskLevel" class="form-control" title="风险等级">
                <option value="0">无</option>
                <option value="1">低风险</option>
                <option value="2">中风险</option>
                <option value="3">高风险</option>
              </select>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">双亲是否近视：</label>
            <div>
              <select name="parentsMyopia" class="form-control" title="双亲是否近视" required>
                <option value="0">无</option>
                <option value="1">父亲</option>
                <option value="2">母亲</option>
                <option value="3">父母</option>
              </select>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-12">
            <label class="control-label">备注：</label>
            <div>
              <input type="text" class="form-control" name="remark"/>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-12">
            <label class="control-label">所属地区：</label>
            <div>
              <div class="city-select-context">
                <div class="inline">
                  <select class="form-control city-select-provincial" >
                  </select>
                </div>
                <div class="inline">
                  <select class="form-control city-select-city" >
                  </select>
                </div>
                <div class="inline">
                  <select class="form-control city-select-district" name="regionId" id="regionId" required>
                  </select>
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary save-btn" onclick="userArchive.save()"><i class="fa fa-save"></i>提交</button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</button>
      </div>
    </div>
  </div>
</div>
<script src="${ctx!}/admin/pub/js/common/regionSelect.js"></script>
<script src="${ctx!}/admin/pub/js/user/userArchive/index.js"></script>

</@bodyContent>