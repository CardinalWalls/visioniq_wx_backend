/**
 * 关联地区工具
 */
;(function ($) {
  "use strict"
  var _global;
  if ($) {
    document.write("<script src='" + ctx + "/admin/pub/plugin/select2ztree/jquery-migrate-1.2.1.js'>" + "<" + "/script>");
    document.write("<script src='" + ctx + "/admin/pub/plugin/select2ztree/jquery.ztree.all.js'>" + "<" + "/script>");
    document.write("<script src='" + ctx + "/admin/pub/plugin/select2ztree/jquery.ztree.exhide.min.js'>" + "<" + "/script>");
    document.write("<script src='" + ctx + "/admin/pub/plugin/select2ztree/select2.min.js'>" + "<" + "/script>");
    document.write("<script src='" + ctx + "/admin/pub/plugin/select2ztree/select2ztree.js'>" + "<" + "/script>");
    document.write("<script src='" + ctx + "/admin/pub/plugin/select2ztree/ztree.vague.js'>" + "<" + "/script>");

    var regionRefTool = {
      regionAll: [],
      removeSelectedRegionRef: function (id) {
        $("#label_" + id).remove();
      },
      regionRef: function () {
        var selectedRow = regionRefTool.area.getSelectedRow();
        if (selectedRow) {

          $("#region_ref_modal").find(".modal-title").html("关联地区【" + (selectedRow.title ? selectedRow.title : (selectedRow.name ? selectedRow.name : selectedRow.id)) + "】");
          $("#region_ref_modal input[name='id']").val(selectedRow.id);
          $('#regionRefFormDiv').empty();
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
      initElement: function (area) {
        regionRefTool.area = area;
        var modal = '<div class="modal" id="region_ref_modal" tabindex="-1" role="dialog" aria-hidden="true">\n' +
          '  <div class="modal-dialog">\n' +
          '    <div class="modal-content">\n' +
          '      <div class="modal-header">\n' +
          '        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span\n' +
          '          class="sr-only">关闭</span>\n' +
          '        </button>\n' +
          '        <h4 class="modal-title">\n' +
          '          关联地区\n' +
          '        </h4>\n' +
          '      </div>\n' +
          '      <div class="modal-body">\n' +
          '        <form class="panel-body form-horizontal" id="regionRefForm">\n' +
          '          <input type="hidden" name="id">\n' +
          '          <div class="form-group">\n' +
          '            <label class="control-label">已关联的地区(\n' +
          '              <label style="width: 10px;height: 10px;margin: 0;border: 0;padding: 0;" class="label-danger"></label>国\n' +
          '              <label style="width: 10px;height: 10px;margin: 0;border: 0;padding: 0;" class="label-success"></label>省\n' +
          '              <label style="width: 10px;height: 10px;margin: 0;border: 0;padding: 0;" class="label-info"></label>市\n' +
          '              <label style="width: 10px;height: 10px;margin: 0;border: 0;padding: 0;" class="label-warning"></label>区\n' +
          '              )：\n' +
          '            </label>\n' +
          '            <div id="regionRefFormDiv" style="margin-top: 10px;">\n' +
          '            </div>\n' +
          '          </div>\n' +
          '          <div class="form-group">\n' +
          '            <label class="control-label">添加关联：</label>\n' +
          '            <div>\n' +
          '              <select id="regionRefSelect" style="width: 100%;"></select>\n' +
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

        var condition = '<div class="form-group">\n' +
          '          <input type="text" name="regionName" placeholder="地区" class="form-control" >\n' +
          '        </div>&nbsp;';
        area.tableEl.parents(".table-content").find('.table-search-submit').before(condition);

        var button = '<span style="padding: 0 5px;">|</span>\n' +
          '      <button onclick="regionRefTool.regionRef()" type="button" class="btn btn-warning">\n' +
          '        <i class="fa fa-map-marker"></i> 关联地区\n' +
          '      </button>';

        area.tableEl.parents(".table-content").find('.table-toolbar').append(button);

        var setting = {
          view: {
            dblClickExpand: false
          },
          data: {
            simpleData: {
              enable: true,
              idKey: "id",
              pIdKey: "parentId",
              rootPId: "ROOT"
            }
          },
        };
        $.get(ctx + "/admin/region/all", {selfId: ""}, function (res) {
          regionRefTool.regionAll = res.data;
          $('#regionRefSelect').select2ztree({
            textField: 'name',
            valueField: "id",
            ztree: {
              setting: setting,
              zNodes: res.data
            }
          });
          $("#regionRefSelect").change(function () {
            var value = $(this).attr("data-value");
            var name = $(this).attr("data-name");
            var level = $(this).attr("data-level");
            level = parseInt(level);
            if ($('#label_' + value).length <= 0) {
              regionRefTool.addSpan(value, name, level);
            }
          });
        });

      },

      addSpan: function (value, name, level) {
        $('#regionRefFormDiv').append("<label id='label_" + value + "' data-value='" + value + "' data-name='" + name + "' data-level='" + level + "' class=\"label label-" + (level == 0 ? "danger" : level == 1 ? "success" : level == 2 ? "info" : "warning") + " m-r-xs\" style='display:inline-block'>" + name + " <a class=\"text-fff\" style='margin-left: 5px;' href=\"javascript:regionRefTool.removeSelectedRegionRef('" + value + "')\">X</a></label>");
      },

      regionRefSave: function () {
        var dataLabel = $("#regionRefFormDiv").find("label");
        var data = [];
        var id = $("#region_ref_modal input[name='id']").val();
        if (dataLabel) {
          dataLabel.each(function () {
            data.push({
              regionId: $(this).attr("data-value"),
              level: $(this).attr("data-level"),
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