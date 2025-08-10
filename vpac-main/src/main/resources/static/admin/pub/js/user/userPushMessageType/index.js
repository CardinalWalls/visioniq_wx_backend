var userPushMessageType = {
  add: function () {
    //userPushMessageType.modalEl.find("form [name=id]").val("");
    //userPushMessageType.modalEl.find("form input.form-control").val("");
    userPushMessageType.modalEl.find("form")[0].reset();
    userPushMessageType.modalEl.find(".save-btn").show();
    userPushMessageType.modal("新增");
  },
  edit:function(read){
    var selectedRow = userPushMessageType.getSelectedRow();
    if (selectedRow) {
      userPushMessageType.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        var v = selectedRow[$this.attr("name")];
        if(v !== undefined){
          $this.val(v + "");
        }
      });
      if(read){
        userPushMessageType.modalEl.find(".save-btn").hide();
        userPushMessageType.modal("查看");
      }else{
        userPushMessageType.modalEl.find(".save-btn").show();
        userPushMessageType.modal("修改");
      }
    }
  },
  modal: function (title) {
    if (title) {
      userPushMessageType.modalEl.find(".modal-title").html(title);
      userPushMessageType.modalEl.modal();
    } else {
      userPushMessageType.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = userPushMessageType.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.msg("请选择一行数据", {icon: 2});
    return null;
  },
  refresh: function () {
    userPushMessageType.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = userPushMessageType.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/user/userPushMessageType/save", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        userPushMessageType.modal(null);
        layer.msg(rs.message, {icon: 1});
        userPushMessageType.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  del: function () {
    var selectedRow = userPushMessageType.getSelectedRow();
    if (selectedRow) {
      layer.confirm("确定删除该数据？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        layer.load(1, {shade: 0.3});
        $.deletePayload(ctx + "/admin/user/userPushMessageType/del", {id: selectedRow.id}, function (rs) {
          layer.closeAll();
          if (rs.success) {
            layer.msg(rs.message, {icon: 1});
            userPushMessageType.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    }
  },
  tableEl: $("#userPushMessageType_table_list"),
  modalEl: $("#userPushMessageType_modal"),
  initTable: function () {
    userPushMessageType.tableEl.bootstrapTable({
      url: ctx + "/admin/user/userPushMessageType/page",
      columns: [{
          checkbox: true
        },
        {
          title: "分类名称",
          field: "name"
        },
        {
          title: "排序",
          field: "sortNo",
          align: "right"
        },
        {
          title: "是否有效",
          field: "valid",
          formatter:function (value, row) {
            return value ? "有效":"无效";
          }
        }
      ]
    });
  }
};
$(function () {
  userPushMessageType.initTable();
});