<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <label for="startDate">起止时间</label>
          <input type="text" placeholder="创建开始时间" id="startDate" name="startDate" class="form-control layer-date" autocomplete="off">
        </div>
        <div class="form-group">
          <label for="endDate">至</label>
          <input type="text" placeholder="创建结束时间" id="endDate" name="endDate" class="form-control layer-date" autocomplete="off">
        </div>
        <div class="form-group">
          <select class="form-control" name="searchGroupId" title="分组" id="searchGroupId">
            <option value="">全部分组</option>

          </select>
        </div>
        <div class="form-group">
          <input type="text" placeholder="位置编码" name="positionCode" class="form-control" title="位置编码">
        </div>
        <div class="form-group">
          <input type="text" placeholder="说明" name="searchDescri" class="form-control" title="说明">
        </div>
        <div class="form-group">
          <select name="searchStatus" class="form-control" title="状态">
            <option value="-1">全部状态</option>
            <option value="0">禁用</option>
            <option value="1">启用</option>
          </select>
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i></button>
      </div>
    </form>
    <div class="table-toolbar">
      <button onclick="ad.addOrEdit('add')" type="button" class="btn btn-info">
        <i class="fa fa-plus"></i>
        创建广告
      </button>
      <button onclick="ad.addOrEdit('edit')" type="button" class="btn btn-warning">
        <i class="fa fa-edit"></i>
        编辑
      </button>
      <button onclick="ad.delete()" type="button" class="btn btn-danger">
        <i class="fa fa-remove"></i>
        删除
      </button>
      <button onclick="ad.updateUrls()" type="button" class="btn btn-warning">
        <i class="fa fa-edit"></i>
        批量修改链接
      </button>
    </div>
    <table id="table_list" class="table-striped"></table>
  </div>
</div>
<div class="modal" id="adModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
          class="sr-only">关闭</span>
        </button>
        <h4 class="modal-title">
          <b id="modal_title">广告</b>
        </h4>
      </div>
      <div class="modal-body">
        <form class="panel-body form-horizontal" id="ad_form">
          <input type="hidden" class="form-control" name="id" id="id"/>
          <div class="form-group">
            <div>
              <select class="form-control chosen-select" data-placeholder="分组" id="groupId" name="groupId"
                      required="required">

              </select>
            </div>
          </div>
          <div class="form-group">
            <div>
              <input type="text" class="form-control" placeholder="广告说明" id="descri" name="descri"
                     required="required"/>
            </div>
          </div>
          <div class="form-group">
            <div>
              <input type="text" class="form-control" placeholder="位置编码" id="positionCode" name="positionCode"
                     required="required"/>
              <#--<span id="bannerCodeMsg"></span>-->
              <#--<br>-->
              <#--<span id="mobileCodeMsg"></span>-->
            </div>
          </div>
          <div class="form-group">
            <div>
              <button type="button" class="btn btn-success" onclick="attachment.uploadFile(1, 'img')"><i
                class="fa fa-upload"></i>上传图片
              </button>
              <input type="hidden" class="form-control" required  name="img" id="img" customImageStyle="max-width:80px;max-height:80px"/>
            </div>
          </div>
          <div class="form-group">
            <div class=" form-inline">
              <input type="text" placeholder="宽" id="width" name="width" class="form-control"">
              &nbsp;*&nbsp;
              <input type="text" placeholder="高" id="height" name="height" class="form-control">
            </div>
          </div>
          <div class="form-group">
            <div>
              <input type="text" placeholder="生效时间" autocomplete="off" required
                     id="beginTime" name="beginTime" class="form-control layer-date">
              &nbsp;~&nbsp;
              <input type="text" placeholder="失效时间" autocomplete="off" required
                     id="endTime" name="endTime" class="form-control layer-date">
            </div>
          </div>
          <div class="form-group">
            <div>
              <input type="text" class="form-control" placeholder="链接地址" id="url" name="url"/>
            </div>
          </div>
          <div class="form-group">
            <div>
              <input type="text" class="form-control" placeholder="备注" id="remark" name="remark"/>
            </div>
          </div>
          <div class="form-group">
            <div>
              <input type="number" placeholder="排序号" name="orderNo" id="orderNo" class="form-control">
            </div>
          </div>
          <div class="form-group">
            <div>
              <select class="form-control" name="status" id="status" required="required">
                <option value="1" selected>启用</option>
                <option value="0">禁用</option>
              </select>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="dealSaveBtn" onclick="ad.dealSave()"><i
          class="fa fa-save"></i>提交
        </button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>
      </div>
    </div>
  </div>

</div>


<div class="modal" id="urlsModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg" style="width: 600px;">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
          class="sr-only">关闭</span>
        </button>
        <h4 class="modal-title">
          <b id="modal_title">批量修改链接</b>
        </h4>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <div>
            <input type="text" class="form-control" placeholder="原链接地址" id="oldUrl" name="oldUrl"/>
          </div>
        </div>
        <div class="form-group">
          <div>
            <input type="text" class="form-control" placeholder="新链接地址" id="newUrl" name="newUrl"/>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary block m-b" style="width: 50px; float: right;" onclick="ad.urlsSave()">确定</button>
      </div>
    </div>
  </div>
</div>



  <script src="${ctx!}/admin/pub/js/attachment/attachmentTool.js"></script>
<script src="${ctx!}/admin/pub/js/sys/dictionary/data/util.js"></script>
<script src="${ctx!}/admin/pub/js/operation/banner/info/index.js"></script>
  <script src="${ctx!}/admin/pub/js/tags/main/util.js"></script>
</@bodyContent>