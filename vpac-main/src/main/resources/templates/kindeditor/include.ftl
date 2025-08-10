<#-- 加载编辑器的容器 -->
<textarea id="${(keditor_id == null)?string('keditor_content', keditor_id)}"></textarea>


<#--自定义多图上传-->
<link rel="stylesheet" type="text/css"
      href="${ctx!}/admin/pub/plugin/kindeditor/plugins/multi_image/diyUpload/css/webuploader.css">
<link rel="stylesheet" type="text/css"
      href="${ctx!}/admin/pub/plugin/kindeditor/plugins/multi_image/diyUpload/css/diyUpload.css">
<script charset="utf-8"
        src="${ctx!}/admin/pub/plugin/kindeditor/plugins/multi_image/diyUpload/js/webuploader.html5only.min.js"></script>
<script charset="utf-8"
        src="${ctx!}/admin/pub/plugin/kindeditor/plugins/multi_image/diyUpload/js/diyUpload.js"></script>
<#--HTML压缩-->
<script charset="utf-8" src="${ctx!}/admin/pub/plugin/minify/htmlminifier.min.js"></script>
<#-- 编辑器源码文件 -->
<script charset="utf-8" src="${ctx!}/admin/pub/plugin/kindeditor/kindeditor-all.js"></script>
<script charset="utf-8" src="${ctx!}/admin/pub/plugin/kindeditor/lang/zh-CN.js"></script>


<#-- 实例化编辑器 -->
<script type="text/javascript">
  ${(keditor_id == null)?string('ke', keditor_id)} =
    KindEditor.create("#${(keditor_id == null)?string('keditor_content', keditor_id)}", {
      ctx: ctx,
      filterMode: false,
      minWidth: 0,
      minHeight: 0,
      width: "100%",
      height: "${(keditor_height == null)?string('300px', keditor_height+'px')}",
      uploadJson: ctx + "/admin/attachment/uploadAtta/kindeditor",
      uploadMultiImgUrl: ctx + "/admin/attachment/uploadAtta",
      uploadWeb: ctx + "/admin/attachment/uploadAttaForWebUrl",
      items: ['source', '|', 'undo', 'redo', '|', 'preview', 'print', 'code',
        'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
        'superscript', 'clearhtml', 'quickformat', '|', '', 'mobile', 'fullscreen', '/',
        'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'template', 'image', "multi_image",
        // 'diy_video',
        'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
        'anchor', 'link', 'unlink','crawling'],
      imageSizeLimit: "50MB",
      imageUploadLimit: 25,
      afterCreate: function () {
        var editorObj = this;
        var doc = editorObj.edit.doc;
        var body = $(doc.body);
        body.bind('paste', function (event) {
          setTimeout(function () {
            awaitCircle(function () {
              return body.find(".__kindeditor_paste__").length === 0;
            }, function () {
              // 处理bug
              var useless = body.find(".__kindeditor_paste__");
              if (useless) {
                useless.removeAttr("style");
                useless.removeClass("__kindeditor_paste__");
              }
              var imgs = body.find("img");
              $.each(imgs, function (index, item) {
                var layerindex = layer.load(1, {
                  shade: [0.3, '#fff'],
                  content: "转存中",
                  success: function (layero) {
                    layero.find('.layui-layer-content').css({
                      'padding-top': '39px',
                      'width': '120px',
                      'margin-left': '-60px'
                    });
                  }
                });
                var _that = $(this);
                var imgSrc = decodeURIComponent(_that.attr("src"));
                if (imgSrc.indexOf("file://") > -1) {
                  // 上传本地图片，暂无
                  layer.close(layerindex);
                } else if (imgSrc.indexOf("data:") > -1) {
                  var blob = dataURLtoBlob(imgSrc);
                  // 上传粘贴板中的截图到服务器
                  var form = document.imgForm;
                  var formData = new FormData(form);
                  formData.append("file", blob);
                  $.ajax({
                    type: "POST",
                    url: editorObj.uploadMultiImgUrl,
                    data: formData,
                    dataType: "json",
                    async: false,
                    processData: false,
                    contentType: false,
                    success: function (res) {
                      layer.close(layerindex);
                      if (res.success) {
                        _that.attr('src', res.data.url);
                        _that.attr('data-ke-src', res.data.url);
                        _that.attr('alt', res.data.name);
                      }
                    },
                    fail: function (err) {
                      layer.close(layerindex);
                    }
                  });
                } else if (imgSrc.indexOf("/pub/") === -1) {
                  // ajax异步上传其它网络图片
                  $.postPayload(editorObj.uploadWeb, {url: imgSrc}, function (res) {
                    layer.close(layerindex);
                    console.log(_that, res);
                    if (res.success) {
                      // 重置图片
                      _that.attr('src', res.data.url);
                      _that.attr('data-ke-src', res.data.url);
                      _that.attr('alt', res.data.name);
                    }
                  },function (err) {
                    layer.close(layerindex);
                  })
                } else {
                  layer.close(layerindex);
                }
              });
            }, 10, 1000);
          }, 1);
        });
      }
    });

  // base64转blob
  function dataURLtoBlob(dataurl) {
    var arr = dataurl.split(','),
      mime = arr[0].match(/:(.*?);/)[1],
      bstr = atob(arr[1]),
      n = bstr.length,
      u8arr = new Uint8Array(n);
    while (n--) {
      u8arr[n] = bstr.charCodeAt(n);
    }
    return new Blob([u8arr], {type: mime});
  }

  var awaitCircle = function (breakFunc, overFunc, loopMs, maxWaitMs) {
    loopMs = loopMs === undefined ? 10 : loopMs;
    maxWaitMs = maxWaitMs === undefined ? 1000 : maxWaitMs;
    var maxRunCount = arguments[4] === undefined ? (maxWaitMs / loopMs) : arguments[4];
    maxRunCount--;
    if (breakFunc()) {
      overFunc();
    } else {
      if (maxRunCount <= 0) {
        return;
      }
      setTimeout(function () {
        awaitCircle(breakFunc, overFunc, loopMs, maxWaitMs, maxRunCount);
      }, loopMs);
    }
  };
</script>
