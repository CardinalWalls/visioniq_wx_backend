/**
 * 关联地区工具
 */
;(function ($) {
  "use strict"
  var _global;
  if ($) {
    try {
      citySelect;
    }catch (e) {
      document.write("<script src='" + ctx + "/admin/pub/js/common/citySelect.js'>" + "<" + "/script>");
    }

    var regionRefTool = {
      regionAll: [],
      btnName:"关联地区",
      removeSelectedRegionRef: function (id) {
        $("#refRegionLabel_" + id).remove();
      },
      regionRef: function () {
        var selectedRow = regionRefTool.area.getSelectedRow();
        if (selectedRow) {

          $("#region_ref_modal").find(".modal-title").html(regionRefTool.btnName + "【" + (selectedRow.title ? selectedRow.title : (selectedRow.name ? selectedRow.name : selectedRow.id)) + "】");
          $("#region_ref_modal input[name='id']").val(selectedRow.id);
          $('#__regionRefFormDiv__').empty();
          // 查询已关联地区
          $.get(ctx + "/admin/region/ref/list", {refId: selectedRow.id}, function (res) {
            $.each(res, function (index, item) {
              regionRefTool.addSpan(item.regionId, item.regionName, item.level);
            })
          });

          // 解决模态框中编辑器无法在文本框输入值的问题
          $('#region_ref_modal').on('shown.bs.modal', function () {
            $(document).off('focusin.modal');
          });
          $("#region_ref_modal").modal();
        }
      },
      /**
       * @param area 作用域
       */
      initElement: function (area, btnName) {
        regionRefTool.area = area;
        if(btnName){
          regionRefTool.btnName = btnName;
        }
        var modal = '<div class="modal" id="region_ref_modal" tabindex="-1" role="dialog" aria-hidden="true">\n' +
          '  <div class="modal-dialog">\n' +
          '    <div class="modal-content">\n' +
          '      <div class="modal-header">\n' +
          '        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span\n' +
          '          class="sr-only">关闭</span>\n' +
          '        </button>\n' +
          '        <h4 class="modal-title"></h4>\n' +
          '      </div>\n' +
          '      <div class="modal-body">\n' +
          '        <form class="panel-body form-horizontal" id="regionRefForm">\n' +
          '          <input type="hidden" name="id">\n' +
          '          <div class="form-group">\n' +
          '            <label class="control-label">已关联的地区：</label>\n' +
          '            <div id="__regionRefFormDiv__" style="margin-top: 10px;">\n' +
          '            </div>\n' +
          '          </div>\n' +
          '          <div class="form-group">\n' +
          '            <label class="control-label">选择地区：</label>\n' +
        '              <div class="city-select-context">\n' +
          '              <div class="inline">\n' +
          '                <select class="form-control city-select-provincial" >\n' +
          '                </select>\n' +
          '              </div>&nbsp;\n' +
          '              <div class="inline">\n' +
          '                <select class="form-control city-select-city" >\n' +
          '                </select>\n' +
          '              </div>&nbsp;\n' +
          '              <div class="inline">\n' +
          '                <select class="form-control city-select-district" id="_refRegionId_" >\n' +
          '                </select>\n' +
          '              </div>\n' +
          '              <div class="inline">\n' +
          '                <button class="btn btn-info btn-sm"  style="vertical-align:unset" id="__refRegionBtn__"><i class="fa fa-plus"></i></button>' +
          '              </div>\n' +
          '            </div>\n' +
          '          </div>\n' +
          '        </form>\n' +
          '      </div>\n' +
          '      <div class="modal-footer">\n' +
          '        <button type="button" class="btn btn-primary" onclick="regionRefTool.regionRefSave()"><i class="fa fa-save"></i>提交\n' +
          '        </button>\n' +
          '        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>\n' +
          '      </div>\n' +
          '    </div>\n' +
          '  </div>\n' +
          '</div>';
        $("body").append(modal);
        $("#__refRegionBtn__").on("click", function () {
          var el = $("#_refRegionId_");
          var value = el.val();
          if(value){
            var p = el.parents(".city-select-context");
            var name = p.find(".city-select-provincial option:selected").text() + "-" +
              p.find(".city-select-city option:selected").text() + "-" +
              p.find(".city-select-district option:selected").text();
            if ($('#refRegionLabel_' + value).length <= 0) {
              regionRefTool.addSpan(value, name);
            }
          }else{
            layer.msg("请选择区级地区");
          }
          return false;
        });

        var button = '<span style="padding: 0 5px;">|</span>\n' +
          '<button onclick="regionRefTool.regionRef()" type="button" class="btn btn-primary">' +
          '<i class="fa fa-map-marker"></i> ' + regionRefTool.btnName + '</button>';

        area.tableEl.parents(".table-content").find('.table-toolbar').append(button);

        citySelect.init({select:"#_refRegionId_",value:"500103"});

      },

      addSpan: function (value, name) {
        $('#__regionRefFormDiv__').append("<label id='refRegionLabel_" + value + "' data-value='" + value + "' data-name='" + name + "' class=\"label label-primary  m-r-xs\" style='display:inline-block'>" + name + " <a class=\"text-fff\" style='margin-left: 5px;' href=\"javascript:regionRefTool.removeSelectedRegionRef('" + value + "')\">X</a></label>");
      },

      regionRefSave: function () {
        var dataLabel = $("#__regionRefFormDiv__").find("label");
        var data = [];
        var id = $("#region_ref_modal input[name='id']").val();
        if (dataLabel) {
          dataLabel.each(function () {
            data.push({
              regionId: $(this).attr("data-value"),
              level: 3,
              regionName: $(this).attr("data-name")
            });
          });
        }

        var i0 = layer.confirm("确定要打关联地区吗？", {
          btn: ['确定', '取消'], shade: 0.3
        }, function () {
          var i1 = layer.load(1, {shade: 0.3});
          $.putPayload(ctx + "/admin/region/ref", {refId: id, data: JSON.stringify(data)}, function (rs) {
            layer.close(i0);
            layer.close(i1);
            if (rs.success) {
              layer.alert(rs.message, {icon: 1});
              $("#region_ref_modal").modal("hide");
              regionRefTool.area.refresh();
            } else {
              layer.alert(rs.message, {icon: 2});
            }
          });
        });
      }

    }
  } else {
    throw "jquery or ctx is not found!";
  }

  // 将对象暴露给全局对象
  _global = (function () {
    return this || (0, eval)('this');
  }());
  if (typeof module !== "undefined" && module.exports) {
    module.exports = regionRefTool;
  } else if (typeof define === "function" && define.amd) {
    define(function () {
      return regionRefTool;
    });
  } else {
    !('regionRefTool' in _global) && (_global.regionRefTool = regionRefTool);
  }
})(window.jQuery);