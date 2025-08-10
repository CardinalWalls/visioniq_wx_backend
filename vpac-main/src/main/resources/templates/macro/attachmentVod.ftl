<#macro attachmentVod>
  <style>
    .modal.in{overflow-y:auto !important;}
  </style>
  <div class="modal fade" id="_attachmentVod_modal" tabindex="-1" data-backdrop="static" data-keyboard="false" role="dialog" aria-hidden="true">
    <div class="modal-dialog shadow">
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">
            <i class="fa fa-play-circle"></i> 视频上传
            <div class="pull-right" style="color:#999;font-style:italic">
              <i class="fa fa-exclamation-circle"></i> 支持文件格式： <span id="_attachmentVod_mediaType"></span>
            </div>
          </h4>
        </div>
        <div class="modal-body">
          <div>
            <i class="fa fa-info-circle"></i> 文件个数限制（已上传 / 总数）： <span id="_attachmentVod_existsCount"></span> / <span id="_attachmentVod_totalCount"></span>
          </div>
          <div class="table-content table-toolbar-right-hide">
            <div class="table-toolbar">
              <button type="button" class="btn btn-success" id="_attachmentVod_selectFile">
                <i class="fa fa-plus"></i>
                添加文件
              </button>
              <button type="button" class="btn btn-danger" id="_attachmentVod_clearUpload">
                <i class="fa fa-trash-o"></i>
                清空上传列表
              </button>
              <button type="button" class="btn btn-info" id="_attachmentVod_startUpload">
                <i class="fa fa-cloud-upload"></i>
                开始上传
              </button>
              <input type="file" name="file" id="_attachmentVod_file" multiple="multiple" style="display:none" />
            </div>
            <table id="_attachmentVod_table" class="table-striped table-toolbar-right-hide"></table>
          </div>
          <div class="m-t-md">
            <button type="button" class="btn btn-default btn-xs" id="_attachmentVod_thirdBtn" onclick="$('#_attachmentVod_thirdTableContent').toggle(100)">
              <i class="fa fa-youtube-play"></i> 第三方平台视频
            </button>
          </div>
          <div class="table-content table-toolbar-right-hide" id="_attachmentVod_thirdTableContent" style="display:none">
            <div class="table-toolbar">
              <div class="input-group">
                <input type="text" class="form-control" id="_attachmentVod_thirdUrl" placeholder="添加第三方带iframe标签的嵌入代码 或 能直接播放的视频URL地址">
                <span class="input-group-btn">
                  <button type="button" class="btn btn-success" title="添加" style="padding:6px 12px" onclick="attachmentVod.addThirdUrl()">
                    <i class="fa fa-plus"></i>
                  </button>
                </span>
              </div>
              <div class="m-t-xs m-b-xs" style="color:#999"> <i class="fa fa-exclamation-circle"></i> 优酷、腾讯等平台请使用带iframe标签的代码，或能直接播放的视频地址</div>
            </div>
            <table id="_attachmentVod_thirdTable" class="table-striped table-toolbar-right-hide"></table>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" id="_attachmentVod_over">
            <i class="fa fa-save"></i> 完成
          </button>
          <button type="button" class="btn btn-white" id="_attachmentVod_close"><i class="fa fa-close"></i>关闭</button>
        </div>
      </div>
    </div>
  </div>

  <script src="${ctx!}/admin/pub/js/vod/aliyun-oss-sdk-5.3.1.min.js"></script>
  <script src="${ctx!}/admin/pub/js/vod/aliyun-upload-sdk-1.5.0.min.js"></script>
  <script src="${ctx!}/admin/pub/js/vod/es6-promise.min.js"></script>
  <script src="${ctx!}/admin/pub/js/vod/util/numbro.min.js"></script>
  <script src="${ctx!}/admin/pub/js/vod/attachmentVod.js"></script>
</#macro>