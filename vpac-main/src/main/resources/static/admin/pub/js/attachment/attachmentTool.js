/**
 * 文件上传工具
 * 代码示例: input 无需使用 type="hidden"
 <div class="form-group">
 <label class="control-label">图片：</label>
 <button type="button" class="btn btn-success btn-xs" onclick="attachment.uploadFile(1, 'img')">
 <i class="fa fa-upload"></i>上传图片
 </button>
 <input class="form-control attachment-field" accept=".pdf" required  name="img" id="img" customImageStyle="max-width:80px;max-height:80px"/>
 </div>
 */
;(function ($) {
  "use strict"
  var _global;
  if ($) {
    // 添加上传组件
    if (!$("#_attachmentToolForm_")[0]) {
      $("body").append("<form id='_attachmentToolForm_' name='_attachmentToolForm_' method='post'>" +
        "<input type='hidden' id='_attachmentToolCount_'>" +
        "<input type='hidden' id='_attachmentToolFieldId_'>" +
        "<input type='hidden' id='_attachmentToolStyle_'>" +
        "<input type='file' style='display:none' dir='' id='_attachmentToolFile_' name='file' enctype='multipart/form-data' multiple/>" +
        "</form>");
    }
    // 添加拖拽组件
    try{
      Sortable
    }catch(e){
      var sortableJs= document.createElement("script");
      sortableJs.setAttribute("type", "text/javascript");
      sortableJs.setAttribute("src", ctx + "/admin/pub/js/attachment/Sortable.min.js");
      document.body.appendChild(sortableJs);
    }

    // 上传配置
    var attachment = {
      redirectApiUri: "/api/oss/redirect",

      /**
       *  attachmentCount = 附件数量上限，callbackFunction = 上传成功后的回调函数
       **/
      uploadFileOnly: function (attachmentCount, callbackFunction, callbackFunctionAgain) {
        var file = $("#_attachmentToolFile_");
        if (attachmentCount > 1) {
          file.attr("multiple", "multiple");
        } else {
          file.removeAttrs("multiple");
        }
        file.click();
        var objEvt = $._data(file[0], "events");
        if (objEvt && objEvt["change"]) {
          return;
        }
        //上传文件事件
        file.change(function () {
          var $this = $(this);
          var v = $this.val();
          if (v === null || v === undefined || v === "") {
            return;
          }
          var accept = $this.attr("accept");
          if(accept){
            accept = accept.toLowerCase();
            for (var i = 0; i < this.files.length; i++) {
              var f = this.files[i];
              var _i = f.name.lastIndexOf(".");
              if(_i>=0 && accept.indexOf(f.name.substring(_i + 1)) < 0){
                layer.alert("上传文件限制类型：" + accept, {icon: 2});
                return;
              }
            }
          }
          if (!attachment.checkCountOnChange(this.files.length)) {
            return;
          }
          var loadIndex = layer.load(1, {shade: 0.3});
          var form = $("#_attachmentToolForm_");
          setTimeout(function () {
            form.ajaxSubmit({
              url: ctx + "/admin/attachment/uploadAtta",
              type: 'POST',
              dataType: "json",
              clearForm: false,
              restForm: false,
              async: false,
              success: function (res) {
                layer.close(loadIndex);
                if (res.success) {
                  if (callbackFunction && callbackFunction instanceof Function) {
                    callbackFunction(res.data,callbackFunctionAgain);
                  } else {
                    throw "无上传回调！";
                  }
                } else {
                  layer.alert(res.message, {icon: 2});
                }
                form[0].reset();
              },
              error: function () {
                layer.close(loadIndex);
                layer.alert("异常", {icon: 2});
                form[0].reset();
              }
            });
          }, 1);
        });
      },

      /**
       *  attachmentCount = 附件数量上限，fieldId=字段元素ID(一般用隐藏input)，
       *  注：附件元素css样式: 使用 在隐藏fieldId元素上使用customImageStyle定义样式，
       *      返回格式： [{attaName: xxx, attaRemark: xxx, attaUrl: xxx, attaSize: xxx}]
       **/
      uploadFile: function (attachmentCount, fieldId, callbackFun) {
        var field = $("#" + fieldId);
        if (field.length !== 1) {
          layer.alert("未找到文件字段的表单元素#" + fieldId, {icon: 2});
          return;
        }
        attachmentCount = attachmentCount < 1 ? 1 : attachmentCount;
        if (!attachment.checkCountOnInit(attachmentCount, field)) {
          return;
        }
        $("#_attachmentToolForm_")[0].reset();
        $("#_attachmentToolCount_").val(attachmentCount);
        $("#_attachmentToolFieldId_").val(fieldId);
        $("#_attachmentToolStyle_").val(field.attr("customImageStyle"));
        var accept = field.attr("accept");
        if(accept){
          $("#_attachmentToolFile_").attr("accept", accept);
        }else{
          $("#_attachmentToolFile_").removeAttr("accept");
        }
        attachment.uploadFileOnly(attachmentCount, function (data,callbackFunctionAgain) {
          attachment.defaultCallback(
            data,
            $("#_attachmentToolCount_").val(),
            $("#_attachmentToolFieldId_").val(),
            $("#_attachmentToolStyle_").val(),
            false,
            callbackFunctionAgain
          );
        },callbackFun);
      },
      /**
       * 设置多个附件的字段，
       * fieldId            - 非空 - input-file元素ID
       * attachmentArray    - 非空 - ["url", "url"]或[{name:xx, url:xxx}, {name:xx, url:xxx}]
       * attachmentStyle    - 可空 - css样式
       * readonly           - 可空 - 是否为隐藏操作按钮，默认 false
       */
      setArray: function (fieldId, attachmentArray, attachmentStyle, readonly) {
        attachment.clear(fieldId);
        var array = [];
        for (var i = 0; i < attachmentArray.length; i++) {
          var a = attachmentArray[i];
          if (a == null) {
            continue;
          }
          if ($.type(a) === "string" && a !== "") {
            array.push({
              name: a.substring(a.lastIndexOf("/") + 1),
              url: a
            });
          } else if ($.type(a) === "object" && a.url !== "") {
            array.push({
              name: a.name === undefined ? a.attaName : a.name,
              url: a.url === undefined ? a.attaUrl : a.url
            });
          }
        }
        if (array.length > 0) {
          // for (var j = 0; j < array.length; j++) {
          //   attachment.defaultCallback(array[j], array.length, fieldId);
          // }
          attachment.defaultCallback(array, array.length, fieldId, attachmentStyle, readonly);
        }
      },
      /**
       * 设置单个附件的字段，
       * fieldId            - 非空 - input-file元素ID
       * attachmentSingle   - 非空 - url字符或{name:xx, url:xxx}
       * attachmentStyle    - 可空 - css样式
       * readonly           - 可空 - 是否为隐藏操作按钮，默认 false
       */
      setSingle: function (fieldId, attachmentSingle, attachmentStyle, readonly) {
        attachment.clear(fieldId);
        if (attachmentSingle == null) {
          return;
        }
        var obj;
        if ($.type(attachmentSingle) === "string" && attachmentSingle !== "") {
          obj = {
            name: attachmentSingle.substring(attachmentSingle.lastIndexOf("/") + 1),
            url: attachmentSingle
          };
        } else if ($.type(attachmentSingle) === "object" && attachmentSingle.url !== "") {
          obj = {
            name: attachmentSingle.name,
            url: attachmentSingle.url
          };
        }
        if (obj) {
          attachment.defaultCallback(obj, 1, fieldId, attachmentStyle, readonly);
        }
      },
      /**将附件uri转为页面 html **/
      toViewHtml: function (fileUri, htmlStyle, htmlClass) {
        if (fileUri) {
          if (fileUri.indexOf("http") === 0 || fileUri.indexOf("https") === 0) {
            return '<img class="cursor-pointer ' + (htmlClass ? htmlClass : 'img-rounded') + '" ' +
              'style="' + (htmlStyle ? htmlStyle : 'max-width:50px;max-height:50px') + '" onclick="window.open($(this).attr(\'src\'))" ' +
              'src="' + fileUri + '" onerror="attachment.imgLoadError(this)" />'
          }
          var name = fileUri.substr(fileUri.lastIndexOf("/") + 1);
          if (attachment.isImg(name) || name.indexOf(".") < 0) {
            return '<img class="cursor-pointer ' + (htmlClass ? htmlClass : 'img-rounded') + '" title="'+name+'&#10;点击下载" ' +
              'style="' + (htmlStyle ? htmlStyle : 'max-width:50px;max-height:50px') + '" onclick="window.open($(this).attr(\'src\'))" ' +
              'src="' + attachment.redirectApiUri + '?file=' + encodeURIComponent(fileUri) + '" onerror="attachment.imgLoadError(this)" />'
          }
          else if (attachment.isAudio(name)) {
            var uri = attachment.redirectApiUri + '?file=' + encodeURIComponent(fileUri);
            return '<audio controls class="' + (htmlClass ? htmlClass : '') + '" style="' + (htmlStyle ? htmlStyle : '') + '" src="' + uri + '" title="' + name + '">' +
              '<a href="' + uri + '" target="_blank">下载' + name + '</a></audio>';
          }
          else {
            return '<a href="' + attachment.redirectApiUri + '?file=' + encodeURIComponent(fileUri) + '" target="_blank"><b>' + name + '</b></a>';
          }
        }
        return "";
      },
      defaultCallback: function (data, attachmentCount, fieldId, attachmentStyle, readonly,callbackFun) {
        attachmentCount = attachmentCount < 1 ? 1 : attachmentCount;
        var fieldEl = $("#" + fieldId);
        var divEl = $("#" + fieldId + "_attachment_div");
        if (divEl.length === 0) {
          divEl = $("<div id='" + fieldId + "_attachment_div'></div>");
          fieldEl.after(divEl);
        }
        var itemArray = divEl.find(".attachment-item");
        var index = itemArray.length;
        var delCount = (attachmentCount - (data instanceof Array ? data.length - 1 : 0));
        while (index >= delCount) {
          index--;
          attachment.del(itemArray.eq(index), fieldEl.attr("id"));
        }

        var sortFunction = function () {
          // 可托拽
          var originDivEl = document.getElementById(divEl.attr("id"));
          if (originDivEl) {
            new Sortable(originDivEl, {
              swapThreshold: 1,
              animation: 150,
              draggable: ".attachment-div",
              onUpdate: function (evt) {
                var oldIndex = evt.oldIndex;
                var newIndex = evt.newIndex;
                var fieldElVal = fieldEl.val();
                var newJson = JSON.parse(fieldElVal);
                if (oldIndex > newIndex) {
                  newJson.splice(newIndex, 0, newJson[oldIndex]);
                  newJson.splice(oldIndex + 1, 1)
                } else {
                  newJson.splice(newIndex + 1, 0, newJson[oldIndex]);
                  newJson.splice(oldIndex, 1)
                }
                fieldEl.val(JSON.stringify(newJson));
              }
            });
          }
        };

        var val = fieldEl.val();
        var json = [];
        if (val) {
          json = JSON.parse(val);
        }
        attachmentStyle = attachmentStyle ? attachmentStyle : fieldEl.attr("customImageStyle");
        fieldEl.data("loading", true);
        if (fieldEl.attr("disabled") || fieldEl.attr("readonly") || readonly) {
          readonly = true;
        }
        if (data instanceof Array) {
          for (var x = 0; x < data.length && x < attachmentCount; x++) {
            var file = data[x];
            attachment.buildViewHtml(file, divEl, attachmentStyle, fieldEl, index, readonly, sortFunction);
            json[index] = buildJsonItem(file.name, file.url, file.remark, file.size, file.duration);
            index++;
          }
        } else {
          attachment.buildViewHtml(data, divEl, attachmentStyle, fieldEl, index, readonly, sortFunction);
          json[index] = buildJsonItem(data.name, data.url, data.remark, data.size, data.duration);
        }

        fieldEl.val(JSON.stringify(json));
        fieldEl.data("loading", false);
        if(callbackFun && callbackFun instanceof Function){
          callbackFun(fieldId,JSON.stringify(json));
        }
      },
      buildViewHtml: function (data, divEl, attachmentStyle, fieldEl, index, readonly, sortFunction) {
        var delDisplay = "";
        if (readonly) {
          delDisplay = "display:none";
        }
        var url = data.url.indexOf("/") === 0
          ? (data.url.indexOf(attachment.redirectApiUri) === 0 ? data.url : attachment.redirectApiUri + "?file=" + encodeURIComponent(data.url))
          : data.url;
        attachmentStyle = attachmentStyle ? attachmentStyle : "";
        var html = "<div class='attachment-div' data-url='"+data.url+"' style='display:inline-block' title='"+data.name+"&#10;点击下载"+(readonly?"":"，可拖拽排序")+"'>";
        var elId = fieldEl.attr("id");
        var isImg = attachment.isImg(data.name) || data.name.indexOf(".") < 0;
        if (isImg) {
          html += "<a href='" + url + "' target='_blank' class='attachment-item'><img style='" + attachmentStyle + "' src='" + url + "' onerror=\"attachment.imgLoadError(this)\" class='img-thumbnail'/></a>";
        }
        else if (attachment.isAudio(data.name)) {
          html += "<div class='attachment-item' style='display:inline-block;vertical-align:middle'><audio controls style='" + attachmentStyle + "' src='" + url + "' onloadedmetadata=\"attachment.loadDuration(this,'" + elId + "','" + index + "')\" ><a href='" + url + "' target='_blank'>下载" + data.name + "</a> </audio></div>";
        }
        else {
          html += "<a href='" + url + "' target='_blank' class='attachment-item'><b style='" + attachmentStyle + "'>" + data.name + "</b></a>";
        }
        html += "<button style='" + delDisplay + "' title='删除' type='button' class='btn btn-xs btn-danger m-l-xs m-r-sm "+(isImg?"attachment-img-del-btn":"attachment-btn")+"' onclick=\"attachment.del(this,\'" + elId + "\')\"><i class='fa fa-trash-o'></i></button>";
        if(isImg){
          html += "<button style='" + delDisplay + "' title='下载' type='button' class='btn btn-xs btn-white m-l-xs m-r-sm attachment-img-open-btn' onclick=\"window.open('"+url+"')\"><i class='fa fa-search'></i></button>";
        }
        divEl.append(html + "</div>");
        if(!readonly && sortFunction){
          sortFunction();
        }
      },
      del: function (e, fieldId) {
        var el = $("#" + fieldId);
        var div = $(e).hasClass("attachment-div") ? $(e) : $(e).parents(".attachment-div");
        var index = div.prevAll().length;
        var array = JSON.parse(el.val());
        if(index >= 0 && array[index].attaUrl === div.data("url")){
          array.splice(index, 1);
          el.val(JSON.stringify(array));
          div.remove();
        }
      },
      clear: function (fieldId) {
        for (var i = 0; i < arguments.length; i++) {
          $("#" + arguments[i]).val("");
          $("#" + arguments[i] + "_attachment_div").empty();
        }
      },
      isImg: function (fileName) {
        var i = fileName.lastIndexOf(".");
        if (i <= 0) {
          return false;
        }
        var suffix = fileName.substring(i);
        return /\.(gif|jpg|jpeg|png|bmp|ico|blob)$/.test(suffix.toLowerCase());
      },
      isAudio: function (fileName) {
        var i = fileName.lastIndexOf(".");
        if (i <= 0) {
          return false;
        }
        var suffix = fileName.substring(i);
        return /\.(mp3|m4a|wav|aac|ogg|wma|flac)$/.test(suffix.toLowerCase());
      },
      checkCountOnChange: function (fileCount) {
        var totalCount = parseInt($("#_attachmentToolCount_").val());
        if (totalCount === 1 && fileCount === 1) {
          return true;
        }
        var arrayStr = $("#" + $("#_attachmentToolFieldId_").val()).val();
        var arrayLength = arrayStr ? JSON.parse(arrayStr).length : 0;
        var limit = totalCount - arrayLength;
        if (limit < fileCount) {
          layer.msg("选择文件个数超过限制，已上传：" + arrayLength + " / " + totalCount + " 个，<br/>还能上传：" + limit + " 个", {
            icon: 2,
            time: 4000
          });
          return false;
        }
        return true;
      },
      checkCountOnInit: function (totalCount, fieldEl) {
        if (totalCount === 1) {
          return true;
        }
        var arrayStr = fieldEl.val();
        var arrayLength = arrayStr ? JSON.parse(arrayStr).length : 0;
        if (totalCount <= arrayLength) {
          layer.msg("上传个数限制已满：" + totalCount + " 个，请先删除后再上传", {icon: 2, time: 4000});
          return false;
        }
        return true;
      },
      loadDuration: function (audio, elId, index) {
        if (elId && audio) {
          try {
            var el = $("#" + elId);
            if (el.length === 1) {
              awaitCircle(function () {
                return !el.data("loading");
              }, 10, function () {
                var array = JSON.parse(el.val());
                var node = array[index];
                var d = audio.duration;
                if (d) {
                  var df = d.toFixed(2);
                  if (node && node.duration === undefined && df > 0) {
                    node.duration = df;
                    el.val(JSON.stringify(array));
                  }
                }
              }, 100);
            }
          } catch (error) {
          }
        }
      },
      imgLoadError:function (e) {
        if(e.src !== "/admin/img/img-404.png"){
          e.src = "/admin/img/img-404.png";
          e.title = "图片未找到";
          $(e).parents(".attachment-div").find(".attachment-img-open-btn").remove();
        }
      }
    }
  } else {
    throw "jquery is not found!";
  }

  var buildJsonItem = function (name, url, remark, size, duration) {
    var item = {
      attaName: name,
      attaRemark: remark,
      attaUrl: url,
      attaSize: size
    };
    if (duration !== undefined) {
      item.duration = duration;
    }
    return item;
  };

  var awaitCircle = function (breakFunc, loopMs, overFunc, maxRunCount) {
    //默认最大1.5s
    maxRunCount = maxRunCount === undefined ? (1500 / loopMs) : maxRunCount;
    maxRunCount--;
    if (breakFunc()) {
      overFunc();
    } else {
      if (maxRunCount <= 0) {
        return;
      }
      setTimeout(function () {
        awaitCircle(breakFunc, loopMs, overFunc, maxRunCount);
      }, loopMs);
    }
  };

  // 将对象暴露给全局对象
  _global = (function () {
    return this || (0, eval)('this');
  }());
  if (typeof module !== "undefined" && module.exports) {
    module.exports = attachment;
  } else if (typeof define === "function" && define.amd) {
    define(function () {
      return attachment;
    });
  } else {
    !('attachment' in _global) && (_global.attachment = attachment);
  }
})(window.jQuery);