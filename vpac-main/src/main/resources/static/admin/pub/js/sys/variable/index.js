var tp = {
  selectPromotionId: null,
  addOrEdit: function (type) {
    var form = $('#data_form').resetForm();
    if (type == "edit") {
      var selectedRow = tp.getSelectedRow();
      if (selectedRow == null) {
        layer.alert("请选择一行", {icon: 1});
        return;
      }
      tp.openModal();
      if (selectedRow) {
        for (var i in selectedRow) {
          var input = form.find(".form-control[name='" + i + "']");
          var v = selectedRow[i];
          if(i === "jsonData"){
            try {
              v = JSON.stringify(JSON.parse(selectedRow[i]), null, 4);
            } catch (e) {
            }
          }
          input.val(v);
        }
      }
      if (selectedRow.avatar) {
        attachment.setSingle('attas', selectedRow.avatar);
      }
    }
    if (type == "add") {
      attachment.clear('attas');
      tp.openModal();
    }
  },
  openModal: function () {
    $("#dataModal").modal();
  },
  getSelectedRow: function () {
    var selectedAll = tableList.obj.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
    }
    return selectedRow;
  },
  search: function () {
    tableList.obj.bootstrapTable('refresh');
  },
  dealSave: function () {
    if (!$("#data_form").valid()) {
      return;
    }
    var l = layer.load(1, {shade: 0.3});
    var params = $("#data_form").serializeJSON();
    params._org_select_=$("#_org_select_").val();
    try {
      params.jsonData = JSON.stringify(JSON.parse(params.jsonData));
    } catch (e) {
    }
    $.post(ctx + "/admin/sys/variable", params, function (rs) {
      layer.close(l);
      if (rs.success) {
        layer.msg(rs.message, {icon: 1});
        $("#dataModal").modal("hide");
        tp.search();
        layer.msg(rs.message, {time:2000,icon: 1});
      } else {
        layer.msg(rs.message, {time:2000,icon: 2});
      }
    }, "json");
  },
  post: function (selectedRow, str, url) {
    var index = layer.confirm("确定要" + str + "吗？", {
      btn: ['确定', '取消'],
      shade: 0.3
    }, function () {
      var l = layer.load(1, {shade: 0.3});
      $.post(ctx + url, {id: selectedRow.id}, function (rs) {
        layer.close(l);
        layer.close(index);
        if (rs.success) {
          layer.msg(rs.message, {time:2000,icon: 1});
          tp.search();
        } else {
          layer.msg(rs.message, {time:2000,icon: 2});
        }
      }, "json");
    });
  },
  delete: function () {
    var selectedRow = tp.getSelectedRow();
    if (selectedRow == null) {
      layer.msg("请选择一行", {time:2000,icon: 2});
      return;
    }
    tp.post(selectedRow, "删除", "/admin/sys/variable/delete");
  }
};


var tableList = {
  obj: $("#table_list"),
  init: function () {
    tableList.obj.bootstrapTable({
      url: ctx + "/admin/sys/variable",
      //数据列
      columns: [{
        checkbox: true
      }, {
        title: "类型",
        field: "type",
        sortable: true
      }, {
        title: "附件",
        field: "avatar",
        formatter: function (value, row, index) {
          return attachment.toViewHtml(row.avatar);
        }
      }, {
        title: "关联项",
        field: "refId"
      }, {
        title: "排序号",
        field: "orderNo",
        sortable: true
      }, {
        title: "描述",
        field: "remark"
      }, {
        title: "内容",
        field: "jsonData",
        formatter: function (value, row, index) {
          var v = value;
          try {
            v = JSON.stringify(JSON.parse(value), null, 4);
          } catch (e) {}
          return '<div class="width-col mCS-dir-rtl">'+v+'</div>';
        }
      }]
    });
  }
};

$(function () {
  tableList.init();
});