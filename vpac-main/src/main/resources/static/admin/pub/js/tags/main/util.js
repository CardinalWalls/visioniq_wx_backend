
;(function () {
  "use strict"
  var _global;
  var tagsUtil = {
    area: new Object(),
    tags: function () {
      var selectedRow = tagsUtil.area.getSelectedRow();
      if (selectedRow) {
        $("#tags_modal").find(".modal-title").html("打标签【" + (selectedRow.title?selectedRow.title:(selectedRow.name?selectedRow.name:selectedRow.id)) + "】");
        $("#tags_modal input[name='id']").val(selectedRow.id);
        $("#tagsIdForTags").val(selectedRow["tagsId"]).selectPageClear();
        $("#tagsIdForTags").val(selectedRow["tagsId"]).selectPageRefresh();
        $("#tags_modal").modal();
      }
    },

    tagsSave: function(){
      var i0 = layer.confirm("确定要打标签吗？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        var i1 = layer.load(1, {shade: 0.3});
        $.putPayload(ctx + "/admin/tags", $("#tags_form").serializeJSON(), function (rs) {
          layer.close(i0);
          layer.close(i1);
          if (rs.success) {
            layer.msg(rs.message, {icon: 1});
            $("#tags_modal").modal("hide");
            tagsUtil.area.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    },
    // 加载标签
    /**
     *
     * @param area 标签作用域
     * @param typeCode 标签类型编码
     */
    initElement: function (area,typeCode) {
      tagsUtil.area = area;
      var modal = '  <div class="modal" id="tags_modal" tabindex="-1" role="dialog" aria-hidden="true">\n' +
        '    <div class="modal-dialog" style="margin-top: 20%;">\n' +
        '      <div class="modal-content">\n' +
        '        <div class="modal-header">\n' +
        '          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span\n' +
        '            class="sr-only">关闭</span>\n' +
        '          </button>\n' +
        '          <h4 class="modal-title">\n' +
        '            打标签\n' +
        '          </h4>\n' +
        '        </div>\n' +
        '        <div class="modal-body">\n' +
        '          <form class="panel-body form-horizontal" id="tags_form">\n' +
        '            <input type="hidden" class="form-control" name="id"/>\n' +
        '            <div class="form-group">\n' +
        '              <input type="text" placeholder="选择标签" name="tagsId" id="tagsIdForTags" class="form-control">\n' +
        '            </div>\n' +
        '          </form>\n' +
        '        </div>\n' +
        '        <div class="modal-footer">\n' +
        '          <button type="button" class="btn btn-primary" onclick="tagsUtil.tagsSave()"><i class="fa fa-save"></i>提交\n' +
        '          </button>\n' +
        '          <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>\n' +
        '        </div>\n' +
        '      </div>\n' +
        '    </div>\n' +
        '  </div>';
      $("body").append(modal);

      var condition = '<div class="form-group">\n' +
        '          <input type="text" placeholder="选择标签" name="tagsId" id="tagsId" class="form-control" title="标签">\n' +
        '        </div>&nbsp;';
      area.tableEl.parents(".table-content").find('.table-search-submit').before(condition);

      var button = '<span style="padding: 0 5px;">|</span>\n' +
        '      <button onclick="tagsUtil.tags()" type="button" class="btn btn-warning">\n' +
        '        打标签\n' +
        '      </button>';

      area.tableEl.parents(".table-content").find('.table-toolbar').append(button);

      $.selectPageInit(true, $('#tagsId,#tagsIdForTags'), '/admin/tags/page?typeCode=' + typeCode);

    }

  };

  if (!$) {
    throw "jquery is not found!";
  }
  // 最后将插件对象暴露给全局对象
  _global = (function () {
    return this || (0, eval)('this');
  }());
  if (typeof module !== "undefined" && module.exports) {
    module.exports = tagsUtil;
  } else if (typeof define === "function" && define.amd) {
    define(function () {
      return tagsUtil;
    });
  } else {
    !('tagsUtil' in _global) && (_global.tagsUtil = tagsUtil);
  }
}());




