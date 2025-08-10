
var pi = {
  addOrEdit: function (type) {
    if (type == "edit") {
      var selectedRow = pi.getSelectedRow();
      if (selectedRow == null) {
        layer.alert("请选择一行", {icon: 1});
        return;
      }

      pi.openModal();
      if (selectedRow) {
        for (var i in selectedRow) {
          var input = $('#dictionary_form').find("#" + i);
          input.val(selectedRow[i]);
        }
      }
      $('#dictionary_form').valid();
    }
    if (type == "add") {
      pi.openModal();
    }
  },
  openModal: function () {
    $("#dictionaryModal").modal({
      backdrop: "static"
    });
    $('#dictionaryModal').on('hidden.bs.modal', function () {
//取消验证文字
      $('#dictionary_form').validate().resetForm();
      document.dictionaryForm.reset();
    });
    $('#dictionary_form').find("input").val("");
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
    $("#searchDictId").val(searchDictId);
    tableList.obj.bootstrapTable('refresh');
    tableList.init();
  },
  clearCondition: function () {
    document.forms[1].reset();
    pi.search();
  },
  dealSave: function () {
    if (!$("#dictionary_form").valid()) {
      return;
    }

    var l = layer.load(1, {shade: 0.3});
    var params = $("#dictionary_form").serializeJSON();
    params.dictId = searchDictId;
    $.post(ctx + "/admin/sys/dictionary/data/saveOrUpdate", params, function (rs) {
      layer.close(l);
      if (rs.success) {
        layer.msg(rs.message, {icon: 1});
        $("#dictionaryModal").modal("hide");
        pi.search();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    }, "json");
  },
  delete: function () {
    var selectedRow = pi.getSelectedRow();
    if (selectedRow == null) {
      layer.alert("请选择一行", {icon: 1});
      return;
    }
    var index = layer.confirm("确定要删除：" + selectedRow.dataName + "吗？", {
      btn: ['确定', '取消'],
      shade: 0.3
    }, function () {
      var l = layer.load(1, {shade: 0.3});
      $.post(ctx + "/admin/sys/dictionary/data/delete", {ids: selectedRow.id}, function (rs) {
        layer.close(l);
        layer.close(index);
        if (rs.success) {
          layer.msg(rs.message, {icon: 1});
          pi.search();
        } else {
          layer.alert(rs.message, {icon: 2});
        }
      }, "json");
    });
  },
};

var tableList = {
  obj: $("#table_list"),
  init: function (searchArgs) {
    tableList.obj.bootstrapTable({
      url: ctx + "/admin/sys/dictionary/data/page",
      //数据列
      columns: [{
        checkbox: true
      }, {
        title: "数据key",
        field: "dataKey"
      }, {
        title: "数据value",
        field: "dataValue"
      }, {
        title: "数据名称",
        field: "dataName"
      }, {
        title: "备注",
        field: "remarks"
      }, {
        title: "状态",
        field: "status",
        formatter: function (value, row, index) {
          switch (value) {
            case 0:
              return '禁用';
              break;
            case 1:
              return "启用";
              break;
          }
        }
      }, {
        title: "排序号",
        field: "orderNo"
      },

      ]
    });
  }
};

var searchDictId;

function init(dictId,dictName) {
  searchDictId = dictId;
  $("#dictName").html(dictName);
  pi.clearCondition();
}





