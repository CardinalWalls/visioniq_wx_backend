var tagsType = {
  add: function () {
    attachment.clear("imgJson");
    tagsType.modalEl.find("form [name=id]").val("");
    tagsType.modal("新增");
  },
  edit:function(){
    attachment.clear("imgJson");
    var selectedRow = tagsType.getSelectedRow();
    if (selectedRow) {
        tagsType.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        $this.val(selectedRow[$this.attr("name")]);
      });
      attachment.setArray("imgJson",JSON.parse(selectedRow.imgJson));
      tagsType.modal("修改");
    }
  },
  modal: function (title) {
    if (title) {
      tagsType.modalEl.find(".modal-title").html(title);
      tagsType.modalEl.modal();
    } else {
      tagsType.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = tagsType.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.alert("请选择一行数据", {icon: 1});
    return null;
  },
  refresh: function () {
    tagsType.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = tagsType.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    var l = layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/tags/tagsType/save", params, function (rs) {
      layer.close(l);
      if (rs.success) {
        tagsType.modal(null);
        layer.alert(rs.message, {icon: 1});
        tagsType.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  del: function () {
    var selectedRow = tagsType.getSelectedRow();
    if (selectedRow) {
      var i0 = layer.confirm("确定删除该数据？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        var i1 = layer.load(1, {shade: 0.3});
        $.deletePayload(ctx + "/admin/tags/tagsType/del", {id: selectedRow.id}, function (rs) {
          layer.close(i0);
          layer.close(i1);
          if (rs.success) {
            layer.alert(rs.message, {icon: 1});
            tagsType.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    }
  },
  tableEl: $("#tagsType_table_list"),
  modalEl: $("#tagsType_modal"),
  initTable: function () {
    tagsType.tableEl.bootstrapTable({
      url: ctx + "/admin/tags/tagsType/page",
      columns: [{
        checkbox: true
      },
      {
        title: "分组名称",
        field: "name"
      },
      {
        title: "分组编码",
        field: "typeCode"
      },{
          title: "图片",
          field: "imgJson",
          formatter: function (value, row) {
            if(value&&value!=''){
              var array = JSON.parse(value);
              var html = "";
              for (var i = 0; i < array.length; i++) {
                html += attachment.toViewHtml(array[i].url) + " ";
              }
              return html;
            }
          }
        },
        {
          title: "排序号",
          field: "sortNo"
        },
        {
          title: "状态",
          field: "status",
          formatter: function (value, row, index) {
            return value === 0 ? '禁用' : '启用'
          }
        },
      ]
    });
  }
};
$(function () {
  tagsType.initTable();
});