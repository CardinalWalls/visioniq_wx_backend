<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>

<@headContent/>
<style>
  .width-col{
    word-break: break-all;
    font-size: 15px;
    white-space:pre;
    overflow:auto;
    max-height:150px;
    line-height: 19px;
  }
</style>
<@bodyContent>

  <div class="panel panel-default">
    <div class="panel-body table-content">
      <form class="table-search">
        <div class="form-inline">
          <div class="form-group">
            <input type="number" placeholder="类型" name="type" class="form-control">
          </div>
          <div class="form-group">
            <input type="text" placeholder="关联项" name="refId" class="form-control">
          </div>
          <div class="form-group">
            <input type="text" placeholder="描述" name="remark" class="form-control">
          </div>
          <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
          <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i></button>
        </div>
      </form>
      <div class="table-toolbar">
        <button onclick="tp.addOrEdit('add')" type="button" class="btn btn-info">
          <i class="fa fa-plus"></i>
          创建
        </button>
        <button onclick="tp.addOrEdit('edit')" type="button" class="btn btn-warning">
          <i class="fa fa-edit"></i>
          编辑
        </button>
        <button onclick="tp.delete()" type="button" class="btn btn-danger">
          <i class="fa fa-trash-o"></i>
          删除
        </button>

      </div>
      <table id="table_list" class="table-striped"></table>
    </div>
  </div>


  <div class="modal" id="dataModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                class="sr-only">关闭</span>
          </button>
          <h4 class="modal-title">
            <b id="modal_title">动态数据</b>
          </h4>
        </div>
        <div class="modal-body">
          <form class="panel-body form-horizontal" id="data_form">
            <input type="hidden" class="form-control" name="id" id="id"/>
            <div class="form-group">
              <label for="type" class="control-label">类型：</label>
              <div>
                <input type="number" class="form-control" placeholder="类型" name="type" value="0" required="required" />
              </div>
            </div>
            <div class="form-group">
              <label for="logo" class="control-label">附件：</label>
              <button type="button" class="btn btn-success" onclick="attachment.uploadFile(1, 'attas')">
                <i class="fa fa-upload"></i>上传附件
              </button>
              <input type="hidden" class="form-control"  name="attas" id="attas" customImageStyle="max-width:80px;max-height:80px"/>
            </div>
            <div class="form-group">
              <label for="status" class="control-label">关联项：</label>
              <div>
                <input type="text" class="form-control" placeholder="关联项" name="refId" required="required"/>
              </div>
            </div>
            <div class="form-group">
              <label for="status" class="control-label">排序号：</label>
              <div>
                <input type="number" class="form-control" placeholder="排序号" name="orderNo" required="required"/>
              </div>
            </div>
            <div class="form-group">
              <label for="status" class="control-label">描述：</label>
              <div>
                <input type="text" class="form-control" placeholder="描述"  name="remark" required="required"/>
              </div>
            </div>
            <div class="form-group">
              <label for="status" class="control-label">内容：</label><br>
              <label for="status" class="control-label" style="color:red;">注意：数据以{"name":"tec","sex":"男"}的方式拼接</label>
              <div>
              <textarea class="form-control" placeholder="内容" rows="15" name="jsonData"
                        required="required"></textarea>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" id="dealSaveBtn" onclick="tp.dealSave()"><i
                class="fa fa-save"></i>提交
          </button>
          <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>
        </div>
      </div>
    </div>

  </div>

  <script src="${ctx!}/admin/js/plugins/bootstrap-table/table.js"></script>
  <script src="${ctx!}/admin/pub/js/sys/variable/index.js"></script>
  <script src="${ctx!}/admin/pub/js/attachment/attachmentTool.js"></script>
</@bodyContent>