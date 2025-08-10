/**
 * 相册工具
 * 先引用attachmentTool.js、attachmentGallery.css
 */
;(function ($) {
  "use strict"
  var _global;
  if ($ && layer && attachment) {
    // 上传配置
    var attachmentGallery = {
      chooseTab: "",
      openGallery: function (attachmentCount,fieldId, callBackFunction) {
        layer.open({
          type: 1,
          id: "attachmentGalleryLayer",
          title: "选择图库图片",
          btn: ['确认', '取消'],
          skin: 'layui-layer-rim',
          area: ['630px', '550px'], //宽高
          content: '<ul class="nav nav-tabs" role="tablist">' +
            '<li role="presentation" class="active"><a href="#attachmentGallery" role="tab" data-toggle="tab">图库</a>' +
            '</li>' +
            '<li role="presentation"><a href="#attachmentWebUrl" role="tab" data-toggle="tab">网络图片</a></li>' +
            '<div class="pull-right gallery-btn">' +
            '<button class="btn btn-info btn-xs" onclick="attachmentGallery.galleryUpload()">上传</button>' +
            '<button class="btn btn-default btn-xs" onclick="attachmentGallery.galleryDelete()">删除</button>' +
            '</div>' +
            '</ul>' +
            '<div class="tab-content">' +
            '<div role="tabpanel" class="tab-pane active clear" id="attachmentGallery">' +
            '</div>' +
            '<div role="tabpanel" class="tab-pane" id="attachmentWebUrl">' +
            '<div>' +
            '图片地址：' +
            '<input id="attachmentImgWebUrl" placeholder="请输入网络图片地址"/>' +
            '<button class="btn btn-info btn-sm" onclick="attachmentGallery.uploadWebUrl()">添加</button>' +
            '</div>' +
            '<div id="attachmentWebUrlPreview">' +
            '</div>' +
            '</div>' +
            '</div>',
          yes: function (index, layero) {
            var url = attachmentGallery.choose();
            if (url && url != '') {
              layer.close(index);
              if (callBackFunction && callBackFunction instanceof Function) {
                callBackFunction(url);
              } else {
                attachment.defaultCallback({url: url, name: "图库图片.jpg"}, attachmentCount, fieldId);
              }
            }
          },
        });
        attachmentGallery.changeTab();
        $('a[href="#attachmentGallery"]').click();
      },

      choose: function () {
        var url;
        if (attachmentGallery.chooseTab == 'gallery') {
          url = attachmentGallery.selectedGallery().url;
        }
        if (attachmentGallery.chooseTab == 'webUrl') {
          url = $("#webImg").attr("src");
        }
        if (!url || url == '') {
          layer.alert("无图片确认！");
          return false;
        }
        return url;
      },

      uploadWebUrl: function () {
        var url = $("#attachmentImgWebUrl").val();
        if (url && url != '') {
          var l = layer.load(1, {
            shade: [0.1, '#ccc']
          });
          $.postPayload(ctx + "/admin/attachment/uploadAttaForWebUrl", {url: url}, function (res) {
            layer.close(l);
            if (res.success) {
              $("#attachmentWebUrlPreview").html('<img id="webImg" style="max-width: 100px;max-height: 100px;" src="' + res.data.url + '"/>')
            } else {
              layer.alert("该图片无法转存！");
            }
          })
        }
      },

      galleryUpload: function () {
        attachment.uploadFileOnly(1, function (data) {
          var loading = layer.load(1, {
            shade: [0.1, '#ccc']
          });
          $.postPayload(ctx + "/admin/user/gallery/save", {url: data.url}, function (res) {
            layer.close(loading);
            attachmentGallery.galleryAll();
          })
        });
      },

      galleryAll: function () {
        $.get(ctx + "/admin/user/gallery/page", {pageSize: 150}, function (res) {
          var html = "";
          if (res.total > 0) {
            $.each(res.list, function (index, item) {
              html += '<div class="gallery_item"><div class="img_div"><img id="' + item.id + '" src="' + attachment.redirectApiUri + '?file=' + item.url + '"></div><div class="checkbox_div"><input type="checkbox"class="checkbox"></div></div>'
            });
          }
          $("#attachmentGallery").html(html);
          $(".gallery_item").click(function (e) {
            var status = $(this).find("input").is(":checked");
            $(".gallery_item").find("input").prop("checked", false);
            $(this).find("input").prop("checked", !status);
            isChoose($(this).find("input"));
          });
          $("input.checkbox").click(function (e) {
            e.stopPropagation();
            var status = $(this).is(":checked");
            $(".gallery_item").find("input").prop("checked", false);
            $(this).prop("checked", status);
            isChoose($(this));
          });
          $(".img_div").mouseover(function () {
            $(".img_div").removeClass("gallery_active");
            $(this).addClass("gallery_active");
          }).mouseout(function () {
            $(this).removeClass("gallery_active");
          });

          function isChoose(checkbox) {
            $(".img_div").removeClass("img_active");
            var status = checkbox.is(":checked");
            if (status) {
              checkbox.parent().parent().find(".img_div").addClass("img_active");
            }
          }
        });
      },

      galleryDelete: function () {
        var id = attachmentGallery.selectedGallery().id;
        if (id) {
          var l = layer.load(1, {
            shade: [0.1, '#ccc']
          });
          $.postPayload(ctx + "/admin/user/gallery/del", {id: id}, function (res) {
            layer.close(l);
            attachmentGallery.galleryAll();
          });
        }
      },

      selectedGallery: function () {
        var res = {};
        var checked = $(".gallery_item").find("input:checkbox:checked");
        if (checked) {
          res = {
            id: checked.parent().parent().find("img").attr("id"),
            url: checked.parent().parent().find("img").attr("src")
          }
        }
        return res;
      },

      changeTab: function () {
        $('a[data-toggle="tab"]').on('click', function (e) {
          var href = $(this).attr("href");
          if (href == '#attachmentGallery') {
            $(".gallery-btn").show();
            attachmentGallery.chooseTab = 'gallery';
            $("#attachmentGallery").empty();
            attachmentGallery.galleryAll();
          }
          if (href == '#attachmentWebUrl') {
            $(".gallery-btn").hide();
            attachmentGallery.chooseTab = 'webUrl';
          }
        });
      }

    }
  } else {
    throw "jquery or layer or attachment is not found!";
  }

  // 将对象暴露给全局对象
  _global = (function () {
    return this || (0, eval)('this');
  }());
  if (typeof module !== "undefined" && module.exports) {
    module.exports = attachmentGallery;
  } else if (typeof define === "function" && define.amd) {
    define(function () {
      return attachmentGallery;
    });
  } else {
    !('attachmentGallery' in _global) && (_global.attachmentGallery = attachmentGallery);
  }
})(window.jQuery);