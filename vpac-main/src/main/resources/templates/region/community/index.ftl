<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<#include "${_ROOT_PATH_}/region/regionSelect.ftl">
<@headContent/>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <input type="text" placeholder="街道名称" name="streetName" class="form-control" title="街道名称">
        </div>
        <div class="form-group">
          <input type="text" placeholder="社区名称" name="name" class="form-control" title="社区名称">
        </div>
        <div class="form-group">
          <input type="text" placeholder="专家" name="expertName" class="form-control" title="专家">
        </div>
        <div class="form-group">
          <select class="form-control" name="valid">
            <option value="">- 状态 -</option>
            <option value="true">有效</option>
            <option value="false">无效</option>
          </select>
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
      <div class="table-toolbar">
        <button onclick="community.edit(true)" type="button" class="btn btn-white">
          <i class="fa fa-list-alt"></i>
          查看
        </button>
        <button onclick="community.add()" type="button" class="btn btn-info">
          <i class="fa fa-plus"></i>
          新增
        </button>
        <button onclick="community.edit()" type="button" class="btn btn-warning">
          <i class="fa fa-edit"></i>
          修改
        </button>
      </div>
    <table id="community_table_list" class="table-striped"></table>
  </div>
</div>
<div class="modal" id="community_modal" tabindex="-1" role="dialog" aria-hidden="true">
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
          <div class="form-group form-line-l col-xs-5">
            <label class="control-label">社区名称<b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="name" required/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-5">
            <label class="control-label">街道名称<b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="streetName" required/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-2">
            <label class="control-label">是否有效<b class="text-danger">*</b>：</label>
            <div>
              <select class="form-control" name="valid">
                <option value="true">有效</option>
                <option value="false">无效</option>
              </select>
            </div>
          </div>
          <div class="form-group form-line-l col-xs-10">
            <label class="control-label">专家<b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="expertId" id="expertId" required/>
            </div>
          </div>
          <div class="form-group form-line-r col-xs-2">
            <label class="control-label">排序<b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="sortNo" initValue="1" required/>
            </div>
          </div>

          <div class="form-group">
            <label class="control-label">选择位置：</label>
              <#include "${_ROOT_PATH_}/common/tencentMap.ftl">
          </div>

          <div class="form-group">
            <label class="control-label">详细地址<b class="text-danger">*</b>：</label>
            <div>
              <input type="text" class="form-control" name="address" id="address" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">所在地区<b class="text-danger">*</b>：</label>
            <div>
              <div class="row col-xs-12 city-select-context">
                <div  class="inline">
                  <select class="form-control city-select-provincial">
                  </select>
                </div>&nbsp;
                <div class="inline">
                  <select class="form-control city-select-city" >
                  </select>
                </div>&nbsp;
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
        <button type="button" class="btn btn-primary save-btn" onclick="community.save()"><i class="fa fa-save"></i>提交</button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</button>
      </div>
    </div>
  </div>
</div>
<script src="${ctx!}/admin/pub/js/common/citySelect.js"></script>
<script src="${ctx!}/admin/pub/js/common/tencentMap.js"></script>
<script src="${ctx!}/admin/pub/js/region/community/index.js"></script>

</@bodyContent>