<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
    <style>
      .text-disabled{
        color: #ed5565;
        text-decoration: line-through;
        font-style: italic;
      }
    </style>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <input type="text" placeholder="姓名" name="name" class="form-control" title="姓名">
        </div>
        <div class="form-group">
          <input type="text" placeholder="手机号" name="phone" class="form-control" title="手机号">
        </div>
        <div class="form-group">
          <input type="text" placeholder="职称" name="title" class="form-control" title="职称">
        </div>
        <div class="form-group">
          <input type="text" placeholder="职位" name="jobPosition" class="form-control" title="职位">
        </div>
        <div class="form-group">
          <input type="text" placeholder="医院" name="hospital" class="form-control" title="医院">
        </div>
        <div class="form-group">
          <select class="form-control" name="status" title="状态">
            <option value="">- 状态 -</option>
            <option value="1">有效</option>
            <option value="0">无效</option>
            <option value="-1">待审核</option>
          </select>
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
      <div class="table-toolbar">
        <button onclick="expert.edit(true)" type="button" class="btn btn-white">
          <i class="fa fa-list-alt"></i>
          查看
        </button>
        <button onclick="expert.add()" type="button" class="btn btn-info">
          <i class="fa fa-plus"></i>
          新增
        </button>
        <button onclick="expert.edit()" type="button" class="btn btn-warning">
          <i class="fa fa-edit"></i>
          修改
        </button>
      </div>
    <table id="expert_table_list" class="table-striped"></table>
  </div>
</div>
<div class="modal" id="expert_modal" tabindex="-1" role="dialog" aria-hidden="true">
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
          <div class="form-group form-line-l col-xs-12">
            <label class="control-label">头像：</label>
            <button type="button" class="btn btn-white btn-xs save-btn" onclick="attachment.uploadFile(1, 'avatar')"><i
                  class="fa fa-upload"></i> 上传
            </button>
            <div>
              <input type="hidden" class="form-control attachment-field" name="avatar" id="avatar" customImageStyle="max-height:80px"/>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-4">
            <label class="control-label">姓名<b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="name" required/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">性别<b class="text-danger">*</b>：</label>
            <div>
              <select class="form-control" name="gender">
                <option value="1">男</option>
                <option value="2">女</option>
              </select>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">状态<b class="text-danger">*</b>：</label>
            <div>
              <select class="form-control" name="status">
                <option value="1">有效</option>
                <option value="0">无效</option>
              </select>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-12" id="addDiv">
            <label class="control-label">手机号<b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="phone" required/>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-12" id="editDiv">
            <label class="control-label">关联登录账号<b class="text-danger">*</b><span class="text-danger">（更换后，新的登录账号的姓名等信息会覆盖）</span>：</label>
            <div>
              <input type="text" class="form-control" name="userId" id="userId" required/>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-4">
            <label class="control-label">职称：</label>
            <div>
              <input type="text" class="form-control" name="title" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">职位：</label>
            <div>
              <input type="text" class="form-control" name="jobPosition" />
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">等级：</label>
            <div>
              <input type="text" class="form-control" name="level" />
            </div>
          </div>
          <div class="form-group form-line-l col-xs-8">
            <label class="control-label">医院<b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="hospital" required/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-4">
            <label class="control-label">科室<b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="department" required/>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-12">
            <label for="categoryName" class="control-label">所属地区：</label>
            <div class="city-select-context">
              <div class="inline">
                <select class="form-control city-select-provincial" name="provincialId" id="provincialId">
                </select>
              </div>
              <div class="inline">
                <select class="form-control city-select-city" name="cityId" id="cityId">
                </select>
              </div>
              <div class="inline">
                <select class="form-control city-select-district" name="regionId" id="districtId" required>
                </select>
              </div>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-12">
            <label class="control-label">擅长专业：</label>
            <div>
              <input class="form-control" name="proficient" />
            </div>
          </div>
          <div class="form-group form-line-l col-xs-12">
            <label class="control-label">个人简介：</label>
            <div>
              <textarea class="form-control" name="profile" rows="3"></textarea>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-12">
            <label class="control-label">预约限制（星期）：</label>
            <div id="appointmentWeekLimit" onclick="expert.appointmentWeekLimitClick()">
              <label class="m-r"><input type="checkbox" data-value="1"> 一</label>
              <label class="m-r"><input type="checkbox" data-value="2"> 二</label>
              <label class="m-r"><input type="checkbox" data-value="3"> 三</label>
              <label class="m-r"><input type="checkbox" data-value="4"> 四</label>
              <label class="m-r"><input type="checkbox" data-value="5"> 五</label>
              <label class="m-r"><input type="checkbox" data-value="6"> 六</label>
              <label class="m-r"><input type="checkbox" data-value="7"> 日</label>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-12">
            <label class="control-label">工作证件：</label>
            <button type="button" class="btn btn-white btn-xs save-btn" onclick="attachment.uploadFile(5, 'workCard')"><i
                  class="fa fa-upload"></i> 上传
            </button>
            <div>
              <input type="hidden" class="form-control attachment-field" name="workCard" id="workCard" customImageStyle="max-height:80px"/>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary save-btn" onclick="expert.save()"><i class="fa fa-save"></i>提交</button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</button>
      </div>
    </div>
  </div>
</div>
<script src="${ctx!}/admin/pub/js/common/citySelect.js"></script>
<script src="${ctx!}/admin/pub/js/attachment/attachmentTool.js"></script>
<script src="${ctx!}/admin/pub/js/user/expert/index.js"></script>

</@bodyContent>