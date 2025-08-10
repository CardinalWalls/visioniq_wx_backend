/**
 * 阿里云视频上传工具
 * JS-SDK参考文档： https://help.aliyun.com/document_detail/52204.html?spm=a2c4g.11186623.6.1041.6d92257eE3X5vz
 * 代码示例：
 <div class="form-group">
 <label class="control-label">视频：</label>
 <button type="button" class="btn btn-success" onclick="attachmentVod.uploadFile(1, 'videoJsonArray')">
 <i class="fa fa-upload"></i>上传视频
 </button>
 <input type="hidden" class="form-control" required  name="videoJsonArray" id="videoJsonArray" customVodStyle="width:208px;height:130px" thirdVod="true"/>
 </div>
 */
;(function ($, sdk) {
  "use strict";
  var _global;
  if(!$){
    throw "jquery is not found!";
  }else if(!sdk){
    throw "AliyunUpload is not found!";
  }else {
    var status = {
      wait: 0,
      uploading: 1,
      stop: 2,
      success: 3,
      error: 4
    };
    var init = false;
    var uploader = new AliyunUpload.Vod({
      //分片大小默认1M，不能小于100K
      partSize: 1048576,
      //并行上传分片个数，默认5
      parallel: 5,
      //网络原因失败时，重新上传次数，默认为3
      retryCount: 3,
      //网络原因失败时，重新上传间隔时间，默认为2秒
      retryDuration: 2,
      // 开始上传
      onUploadstarted: function (uploadInfo) {
        var id = uploadInfo.file.name + uploadInfo.file.size;
        var row = attachmentVod.tableEl.bootstrapTable("getRowByUniqueId", id);
        refreshRow(row);
        if (uploadInfo.videoId) {
          var up = uploader.getCheckpoint(uploadInfo.file);
          //刷新上传地址和凭证
          $.ajax({
            url: ctx+"/api/oss/vod/upload/auth/refresh?videoId=" + uploadInfo.videoId,
            method: "GET",
            success: function (res) {
              if (res.success) {
                row.uploadStatus = status.uploading;
                uploader.setUploadAuthAndAddress(uploadInfo, res.uploadAuth, res.uploadAddress);
                refreshRow(row);
                updateProgress(up.loaded, id);
              } else {
                attachmentVod.updateNewFile(uploadInfo, row, id);
              }
            }
          });
        } else {
          attachmentVod.updateNewFile(uploadInfo, row, id);
        }
      },
      // 文件上传成功
      onUploadSucceed: function (uploadInfo) {
        var id = uploadInfo.file.name + uploadInfo.file.size;
        var row = attachmentVod.tableEl.bootstrapTable("getRowByUniqueId", id);
        row.uploadStatus = status.success;
        refreshRow(row);
      },
      // 文件上传失败
      onUploadFailed: function (uploadInfo, code, message) {
        errorMsg("上传失败：" + message,  uploadInfo.file.name + uploadInfo.file.size);
      },
      // 文件上传进度，单位：字节
      onUploadProgress: function (uploadInfo, totalSize, loadedPercent) {
        updateProgress(loadedPercent,  uploadInfo.file.name + uploadInfo.file.size);
      },
      // 上传凭证超时
      onUploadTokenExpired: function (uploadInfo) {
        $.ajax({
          url: ctx+"/api/oss/vod/upload/auth/refresh?videoId=" + uploadInfo.videoId,
          method: "GET",
          success: function (res) {
            uploader.resumeUploadWithAuth(res.uploadAuth);
          }
        })
      },
      //全部文件上传结束
      onUploadEnd: function (uploadInfo) {
      }
    });
    var attachmentVod = {
      open:false,
      modal:$("#_attachmentVod_modal"),
      uploadField:null,
      uploadFieldId:null,
      totalCount:1,
      canUploadCount:1,
      file:$("#_attachmentVod_file"),
      thirdUrl:$("#_attachmentVod_thirdUrl"),
      callback:undefined,
      /**
       *  attachmentCount = 附件数量上限，fieldId=字段元素ID(一般用隐藏input)，
       *  callback = 回调函数，为空时调用 attachmentVod.defaultCallback
       *  注：附件元素css样式: 使用 在隐藏fieldId元素上使用customImageStyle定义样式，
       *      返回格式： [{name: xxx, url: xxx, third: false}]， （third=是否为第三方平台资源）
       **/
      uploadFile: function (attachmentCount, fieldId, callback) {
        if(!init){
          throw "AliyunUpload init fail";
        }
        if(!attachmentVod.open){
          attachmentVod.uploadField = $("#"+fieldId);
          attachmentVod.uploadFieldId = fieldId;
          if(attachmentVod.uploadFieldId && attachmentVod.uploadField.length === 1 ){
            attachmentVod.totalCount = attachmentCount < 1 ? 1 : attachmentCount;
            if(attachmentVod.checkCountOnInit()){
              $("#_attachmentVod_mediaType").html(attachmentVod.mediaType.join("、"));
              attachmentVod.file[0].value = null;
              uploader.cleanList();
              attachmentVod.callback = callback;
              attachmentVod.tableEl.bootstrapTable("removeAll");
              attachmentVod.thirdTableEl.bootstrapTable("removeAll");
              attachmentVod.thirdUrl.val("");
              $("#_attachmentVod_thirdTableContent").hide();
              var thirdVod = attachmentVod.uploadField.attr("thirdVod");
              if(thirdVod === undefined || thirdVod === 'true'){
                $("#_attachmentVod_thirdBtn").show();
              }else{
                $("#_attachmentVod_thirdBtn").hide();
              }
              attachmentVod.modal.modal();
              attachmentVod.open = true;
            }
          }
        }
      },
      updateNewFile:function(uploadInfo, row, id){
        $.ajax({
          url: ctx+"/api/oss/vod/upload/auth?fileName=" + uploadInfo.file.name,
          method: "GET",
          success: function (res) {
            if (res.success) {
              row.uploadStatus = status.uploading;
              refreshRow(row);
              uploader.setUploadAuthAndAddress(uploadInfo, res.uploadAuth, res.uploadAddress, res.videoId);
            }
            else{
              row.uploadStatus = status.error;
              refreshRow(row);
              errorMsg("上传失败，请重试", id);
            }
          }
        });
      },
      /** 设置多个附件的字段，attachmentArray=["url", "url"]或[{url:xxx}, {url:xxx}] */
      setArray: function (fieldId, attachmentArray) {
        attachmentVod.clear(fieldId);
        var array = [];
        for(var i = 0 ; i < attachmentArray.length; i++){
          var a = attachmentArray[i];
          if($.type(a) === "string"){
            array.push({
              url : a
            });
          }else if($.type(a) === "object"){
            array.push(a);
          }
        }
        if(array.length > 0){
          var fieldEl = $("#" + fieldId);
          var divEl = $("#" + fieldId + "_attachment_div");
          if (divEl.length === 0) {
            divEl = $("<div id='" + fieldId + "_attachment_div'></div>");
            fieldEl.after(divEl);
          }
          for(var j = 0 ; j < array.length; j++){
            attachmentVod.buildViewHtml(array[j], divEl, '', fieldEl);
          }
          fieldEl.val(JSON.stringify(attachmentArray));
        }
      },
      /**将附件 视频附件Json数组转为页面 html **/
      toViewHtml:function(dataArray, style, imgClass){
        var html = "";
        for (var i = 0; i < dataArray.length; i++) {
          var item = dataArray[i];
          if(item.iframe === false){
            html += "<video src='" + dataArray[i].url + "' style='"+(style ? style:"")+"' class='m-r-xs attachment-vod "+imgClass+"' controls>您的浏览器不兼容视频播放</video>"
          }else{
            html += "<iframe src='" + dataArray[i].url + "' style='"+(style ? style:"")+"' class='m-r-xs attachment-vod "+imgClass+"' frameborder=0 allowFullScreen='true'></iframe>"
          }
        }
        return html;
      },
      cleanList:function(){
        uploader.cleanList();
      },
      clear: function (fieldId) {
        for (var i = 0; i < arguments.length; i++) {
          $("#" + arguments[i]).val("");
          $("#" + arguments[i] + "_attachment_div").empty();
        }
      },
      checkCountOnChange:function(fileCount){
        var canUpCount = attachmentVod.canUploadCount - uploader.listFiles().length - attachmentVod.thirdTableEl.bootstrapTable("getData").length;
        if(fileCount > canUpCount ){
          layer.msg("选择文件个数超过限制，<br/>已上传或已选择：" + (attachmentVod.totalCount - canUpCount)
            + " 个，<br/>还能选择：" + canUpCount + " 个", {icon: 2, time: 4000});
          return false;
        }
        return true;
      },
      checkCountOnInit:function(){
        var fieldLength = 0;
        try {
          fieldLength = JSON.parse(attachmentVod.uploadField.val()).length;
        } catch (e) {}
        attachmentVod.canUploadCount = attachmentVod.totalCount - fieldLength;
        $("#_attachmentVod_existsCount").html(fieldLength);
        $("#_attachmentVod_totalCount").html(attachmentVod.totalCount);
        if(attachmentVod.canUploadCount <= 0){
          layer.msg("上传个数限制已满："+attachmentVod.totalCount+" 个，请先删除后再上传", {icon: 2, time: 4000});
          return false;
        }
        return true;
      },
      defaultCallback: function (array) {
        var attachmentCount = attachmentVod.totalCount;
        var fieldEl = attachmentVod.uploadField;
        var divEl = $("#" + attachmentVod.uploadFieldId + "_attachment_div");
        if (divEl.length === 0) {
          divEl = $("<div id='" + attachmentVod.uploadFieldId + "_attachment_div'></div>");
          fieldEl.after(divEl);
        }
        var itemArray = divEl.find(".attachment-item");
        var index = itemArray.length;
        var delCount = (attachmentCount - (array instanceof Array ? array.length - 1 : 0));
        while (index >= delCount) {
          index--;
          attachmentVod.delByIndex(itemArray.eq(index), index, fieldEl.attr("id"));
        }

        var val = fieldEl.val();
        var json = [];
        if (val) {
          json = JSON.parse(val);
        }
        var attachmentStyle = fieldEl.attr("customVodStyle");
        for(var x=0;x<array.length && x < attachmentCount;x++){
          var file = array[x];
          attachmentVod.buildViewHtml(file,divEl,attachmentStyle,fieldEl);
          json[index] = file;
          index++;
        }
        fieldEl.val(JSON.stringify(json));
      },
      buildViewHtml:function(data,divEl,attachmentStyle,fieldEl){
        var delDisplay = "";
        if(fieldEl.attr("disabled") || fieldEl.attr("readonly")){
          delDisplay = "display:none";
        }
        if(data.iframe === false){
          divEl.append(
            "<div class='attachment-div' style='display:inline-block'>" +
            "<video src='" + data.url + "' style='"+(attachmentStyle ? attachmentStyle:"")+"' class='attachment-item attachment-vod' controls >您的浏览器不兼容视频播放</video>" +
            "<button style='margin-left:2px;"+delDisplay+"' class='btn btn-xs btn-danger m-l-xs m-r-sm' " +
            "onclick=\"attachmentVod.delByUrl(this,\'" + data.url + "\',\'" + fieldEl.attr("id") + "\')\"><i class='fa fa-trash-o'></i></button>" +
            "</div>");
        }
        else{
          divEl.append(
            "<div class='attachment-div' style='display:inline-block'>" +
            "<iframe src='" + data.url + "' style='"+(attachmentStyle ? attachmentStyle:"")+"' class='attachment-item attachment-vod' frameborder=0 allowFullScreen='true'></iframe>" +
            "<button style='margin-left:2px;"+delDisplay+"' class='btn btn-xs btn-danger m-l-xs m-r-sm' " +
            "onclick=\"attachmentVod.delByUrl(this,\'" + data.url + "\',\'" + fieldEl.attr("id") + "\')\"><i class='fa fa-trash-o'></i></button>" +
            "</div>");
        }
      },
      delByIndex: function (e, index, fieldId) {
        var el = $("#" + fieldId);
        var json = JSON.parse(el.val());
        json.splice(index, 1);
        el.val(JSON.stringify(json));
        $(e).parent().remove();
      },
      delByUrl: function (e, url, fieldId) {
        var el = $("#" + fieldId);
        var json = JSON.parse(el.val());
        for (var i = 0; i < json.length; i++) {
          var node = json[i];
          if(node.url === url){
            json.splice(i, 1);
            break;
          }
        }
        el.val(JSON.stringify(json));
        $(e).parent().remove();
      },
      stop:function (id) {
        updateStatus(status.stop, id);
        uploader.stopUpload();
      },
      continue:function (id) {
        uploader.startUpload();
        updateStatus(status.uploading, id);
      },
      delFile:function (id, index) {
        try {
          uploader.stopUpload();
          uploader.deleteFile(index);
          attachmentVod.tableEl.bootstrapTable("removeByUniqueId", id);
        } catch (e) {}
      },
      addThirdUrl:function () {
        var url = attachmentVod.thirdUrl.val().trim();
        if(url && attachmentVod.checkCountOnChange(1)){
          var iframe = false;
          if(url.indexOf("<iframe") === 0){
            try {
              url = $(url).attr("src");
              iframe = true;
            } catch (e) {}
          }
          var row = attachmentVod.thirdTableEl.bootstrapTable("getRowByUniqueId", url);
          if(!row){
            var data = {
              index: attachmentVod.thirdTableEl.bootstrapTable("getData").length,
              row: {
                "id": url,
                "iframe": iframe
              }
            };
            attachmentVod.thirdTableEl.bootstrapTable("insertRow", data);
          }
        }
      }
    };

    $.get(ctx+"/api/oss/vod/config", function (rs) {
      if(rs.userId){
        uploader.options.userId = rs.userId;
        uploader.options.regionId = rs.regionId;
        var array = rs.mediaType.split(",");
        attachmentVod.mediaType = [];
        for (var i = 0; i < array.length; i++) {
          var v = array[i].trim();
          if(v){
            attachmentVod.mediaType.push(v);
          }
        }
        init = true;
      }
    },"json");
    attachmentVod.tableEl = $('#_attachmentVod_table').bootstrapTable({
      checkboxHeader: false,
      clickToSelect: false,
      singleSelect: true,
      showRefresh: false,
      pagination:false,
      //数据列
      columns: [{
        title: "文件名称",
        field: "name"
      }, {
        title: "大小",
        field: "size",
        width: "70px"
      }, {
        title: "上传进度",
        field: "uploadProgress",
        width: "150px",
        formatter: function (value, row, index) {
          var p = "0%";
          var pm = "等待上传";
          var active = "";
          if(row.uploadStatus === status.stop){
            p = getProgress(index);
          }else if(row.uploadStatus === status.uploading){
            p = getProgress(index);
            pm = p;
            active = "active";
          }else if(row.uploadStatus === status.success){
            p = "100%";
            pm = "上传完成"
          }
          return '<div class="progress m-b-n"><div name="upload_progress_' + row.id + '" style="width:' + p + '" ' +
            'class="progress-bar progress-bar-striped '+active+'"></div></div>' +
            '<div class="progress-txt" name="upload_message_' + row.id + '">' + pm + '</div>'
        }
      }, {
        title: "操作",
        field: "op",
        width: "100px",
        formatter: function (value, row, index) {
          var btn = "<div>";
          if (row.uploadStatus === status.uploading) {
            btn += '<button class="btn btn-info btn-xs" type="button" onclick="attachmentVod.stop(\''+row.id+'\')">暂停</button>';
          } else if (row.uploadStatus === status.stop) {
            btn += '<button class="btn btn-success btn-xs" type="button" onclick="attachmentVod.continue(\''+row.id+'\')">继续</button>';
          }
          return btn + '<button class="btn btn-danger btn-xs m-l-xs" type="button" onclick="attachmentVod.delFile(\''+row.id+'\', '+index+')">删除</button></div>';
        }
      }]
    });
    attachmentVod.thirdTableEl = $('#_attachmentVod_thirdTable').bootstrapTable({
      checkboxHeader: false,
      clickToSelect: false,
      singleSelect: true,
      showRefresh: false,
      pagination:false,
      //数据列
      columns: [{
        title: "视频URL地址",
        field: "id"
      }, {
        title: "操作",
        field: "op",
        width: "100px",
        formatter: function (value, row, index) {
          return '<button class="btn btn-danger btn-xs m-l-xs" type="button" ' +
            'onclick="attachmentVod.thirdTableEl.bootstrapTable(\'removeByUniqueId\', \''+row.id+'\');" >删除</button></div>';
        }
      }]
    });

    var resetAttachmentVod = function () {
      attachmentVod.totalCount = 1;
      attachmentVod.canUploadCount = 1;
      attachmentVod.uploadField = null;
      attachmentVod.uploadFieldId = null;
      attachmentVod.open = false;
    };
    var refreshRow = function(row) {
      attachmentVod.tableEl.bootstrapTable("updateByUniqueId", row.id, row);
    };
    var updateProgress = function(loadedPercent, id) {
      var p = numbro(loadedPercent === undefined ? 0 : loadedPercent).format({
        output: "percent",
        mantissa: 0
      });
      attachmentVod.tableEl.find("[name='upload_progress_" + id + "']").attr({"style": "width:" + p});
      attachmentVod.tableEl.find("[name='upload_message_" + id + "']").html(p);
    };
    var getProgress = function (index) {
      var p =uploader.listFiles()[index].loaded;
      return numbro(p === undefined ? 0 : p).format({
        output: "percent",
        mantissa: 0
      });
    };
    var errorMsg = function(msg, id) {
      var messageObj = attachmentVod.tableEl.find("[name='upload_message_" + id + "']");
      messageObj.html(msg);
      messageObj.attr({"style": "color:red"})
      try {
        uploader.stopUpload();
      } catch (e) {}
    };
    var updateStatus = function(status, id) {
      var row = attachmentVod.tableEl.bootstrapTable("getRowByUniqueId", id);
      if(row){
        row.uploadStatus = status;
        refreshRow(row);
      }
    };

    attachmentVod.file.on("change", function (event) {
      var length = event.target.files.length;
      if(attachmentVod.checkCountOnChange(length)){
        var allType = $.inArray("*", attachmentVod.mediaType) >= 0;
        for (var i = 0; i < length; i++) {
          var file = event.target.files[i];
          var files = uploader.listFiles();
          var repeat = false;
          var name =file.name;
          var id = name + file.size;
          name = name.substr(name.lastIndexOf(".")+1,name.length-1).toLowerCase();
          if(!allType && $.inArray(name, attachmentVod.mediaType) === -1){
            continue;
          }
          for (var x = 0; x < files.length; x++) {
            var f = files[x].file;
            if (id === f.name + f.size) {
              repeat = true;
              break;
            }
          }
          //判断重复
          if (!repeat) {
            uploader.addFile(file, null, null, null);
            var data = {
              index: uploader.listFiles().length,
              row: {
                "id":id,
                "uploadStatus": status.wait,
                "name": file.name,
                "size": numbro(file.size).format({
                  output: "byte",
                  base: "binary",
                  spaceSeparated: true,
                  mantissa: 1
                }).replace("iB", "")
              }
            };
            attachmentVod.tableEl.bootstrapTable("insertRow", data);
          }
        }
      }
      this.value = null;
    });

    $("#_attachmentVod_selectFile").on("click", function () {
      attachmentVod.file.click();
    });
    $("#_attachmentVod_startUpload").on("click", function () {
      uploader.startUpload();
    });
    $("#_attachmentVod_clearUpload").on("click", function () {
      uploader.stopUpload();
      uploader.cleanList();
      attachmentVod.tableEl.bootstrapTable("removeAll");
    });
    $("#_attachmentVod_close").on("click",function () {
      var l = layer.confirm("确定要取消上传吗？", {
        btn: ['确定', '取消'],
        shade: 0.3
      }, function () {
        uploader.stopUpload();
        attachmentVod.modal.modal("hide");
        resetAttachmentVod();
        layer.close(l);
      });
    });
    $("#_attachmentVod_over").on("click",function () {
      var l = layer.confirm("确定要暂停上传，并选择已完成的视频吗？", {
        btn: ['确定', '取消'],
        shade: 0.3
      }, function () {
        var l0 = layer.load(1, {shade: 0.3});
        uploader.stopUpload();
        var files = uploader.listFiles();
        var array = [];
        for (var i = 0; i < files.length; i++) {
          var file = files[i];
          //只获取上传成功的文件
          if (file.state === "Success") {
            array.push({
              id:file.videoId,
              name: file.file.name,
              url: "/api/oss/vod/play.html?id="+file.videoId,
              third: false
            })
          }
        }
        var third = attachmentVod.thirdTableEl.bootstrapTable("getData");
        for (var i = 0; i < third.length; i++) {
          var item = third[i];
          array.push({
            name: 'thirdPart',
            url: item.id,
            third: true,
            iframe: item.iframe
          })
        }

        if (attachmentVod.callback) {
          attachmentVod.callback(array);
        }else{
          attachmentVod.defaultCallback(array);
        }
        attachmentVod.modal.modal("hide");
        resetAttachmentVod();
        layer.close(l0);
        layer.close(l);
      });

    });
  }

  // 将对象暴露给全局对象
  _global = (function () {
    return this || (0, eval)('this');
  }());
  if (typeof module !== "undefined" && module.exports) {
    module.exports = attachmentVod;
  } else if (typeof define === "function" && define.amd) {
    define(function () {
      return attachmentVod;
    });
  } else {
    !('attachmentVod' in _global) && (_global.attachmentVod = attachmentVod);
  }
})(window.jQuery, AliyunUpload);