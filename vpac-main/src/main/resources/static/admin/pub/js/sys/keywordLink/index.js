var keywordLink = {
  add: function () {
    //keywordLink.modalEl.find("form [name=id]").val("");
    //keywordLink.modalEl.find("form input.form-control").val("");
    keywordLink.modalEl.find("form")[0].reset();
    keywordLink.modalEl.find(".save-btn").show();
    keywordLink.modal("新增");
  },
  edit:function(read){
    var selectedRow = keywordLink.getSelectedRow();
    if (selectedRow) {
      keywordLink.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        var v = selectedRow[$this.attr("name")];
        if(v !== undefined){
          $this.val(v + "");
        }
      });
      if(read){
        keywordLink.modalEl.find(".save-btn").hide();
        keywordLink.modal("查看");
      }else{
        keywordLink.modalEl.find(".save-btn").show();
        keywordLink.modal("修改");
      }
    }
  },
  modal: function (title) {
    if (title) {
      keywordLink.modalEl.find(".modal-title").html(title);
      keywordLink.modalEl.modal();
    } else {
      keywordLink.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = keywordLink.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.msg("请选择一行数据", {icon: 2});
    return null;
  },
  refresh: function () {
    keywordLink.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = keywordLink.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/sys/keywordLink/save", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        keywordLink.modal(null);
        layer.msg(rs.message, {icon: 1});
        keywordLink.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  del: function () {
    var selectedRow = keywordLink.getSelectedRow();
    if (selectedRow) {
      layer.confirm("确定删除该数据？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        layer.load(1, {shade: 0.3});
        $.deletePayload(ctx + "/admin/sys/keywordLink/del", {id: selectedRow.id}, function (rs) {
          layer.closeAll();
          if (rs.success) {
            layer.msg(rs.message, {icon: 1});
            keywordLink.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    }
  },
  tableEl: $("#keywordLink_table_list"),
  modalEl: $("#keywordLink_modal"),
  initTable: function () {
    keywordLink.tableEl.bootstrapTable({
      url: ctx + "/admin/sys/keywordLink/page",
      columns: [{
        checkbox: true
      },
      {
        title: "关键词",
        field: "keyword"
      },
      {
        title: "跳转链接",
        field: "urlLink"
      }
      ]
    });
  }
};
$(function () {
  keywordLink.initTable();
});