/**
 * 文件上传工具
 */
;(function ($) {
  "use strict"
  var _global;
  if ($) {
    // 添加上传组件
    if (!$("#attachmentUtilForm")[0]) {
      $("body").append("<form id='attachmentUtilForm' name='attachmentUtilForm' method='post'><input type='file' style='display: none' dir='' id='attachmentUtilFile' name='file' enctype='multipart/form-data'/></form>");
    }
    // 上传配置
    var attachmentUtil = {
      /**
       * 上传文件方法
       * @param functionOrImgId 回调函数
       */
      uploadFile: function (callback) {
        $("#attachmentUtilFile").click();
        var objEvt = $._data($("#attachmentUtilFile")[0], "events");
        if (objEvt && objEvt["change"]) {
          return;
        }
        //上传文件事件
        $("#attachmentUtilFile").change(function () {
          var loadIndex = layer.load(1, {
            shade: [0.1, '#ccc']
          });
          if ($(this).val() == null || $(this).val() == undefined || $(this).val() == "") {
            return;
          }
          var option = {
            url: ctx + "/admin/attachment/uploadAtta",
            type: 'POST',
            dataType: "json",
            clearForm: false,
            restForm: false,
            async: false,
            success: function (res) {
              if (callback && callback instanceof Function) {
                callback(res);
              } else {
                console.log("attachmentUtil无默认回调方法");
              }
              layer.close(loadIndex);
              document.attachmentUtilForm.reset();
            },
            error: function () {
              layer.close(loadIndex);
              layer.alert("异常", {icon: 2});
              document.attachmentUtilForm.reset();
            }
          };
          $("#attachmentUtilForm").ajaxSubmit(option);
        });
      },
    }
  } else {
    throw "jquery is not found!";
  }

  // 将对象暴露给全局对象
  _global = (function () {
    return this || (0, eval)('this');
  }());
  if (typeof module !== "undefined" && module.exports) {
    module.exports = attachmentUtil;
  } else if (typeof define === "function" && define.amd) {
    define(function () {
      return attachmentUtil;
    });
  } else {
    !('attachmentUtil' in _global) && (_global.attachmentUtil = attachmentUtil);
  }
})(window.jQuery);