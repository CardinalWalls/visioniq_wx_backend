var userVisionReport = {
  add: function () {
    //userVisionReport.modalEl.find("form [name=id]").val("");
    //userVisionReport.modalEl.find("form input.form-control").val("");
    userVisionReport.modalEl.find("form")[0].reset();
    userVisionReport.modalEl.find(".save-btn").show();
    userVisionReport.modal("新增");
  },
  edit:function(read){
    var selectedRow = userVisionReport.getSelectedRow();
    if (selectedRow) {
      userVisionReport.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        var v = selectedRow[$this.attr("name")];
        if(v !== undefined){
          $this.val(v + "");
        }
      });
      if(read){
        userVisionReport.modalEl.find(".save-btn").hide();
        userVisionReport.modal("查看");
      }else{
        userVisionReport.modalEl.find(".save-btn").show();
        userVisionReport.modal("修改");
      }
    }
  },
  modal: function (title) {
    if (title) {
      userVisionReport.modalEl.find(".modal-title").html(title);
      userVisionReport.modalEl.modal();
    } else {
      userVisionReport.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = userVisionReport.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.msg("请选择一行数据", {icon: 2});
    return null;
  },
  refresh: function () {
    userVisionReport.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = userVisionReport.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/user/userVisionReport/save", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        userVisionReport.modal(null);
        layer.msg(rs.message, {icon: 1});
        userVisionReport.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  del: function () {
    var selectedRow = userVisionReport.getSelectedRow();
    if (selectedRow) {
      layer.confirm("确定删除该数据？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        layer.load(1, {shade: 0.3});
        $.deletePayload(ctx + "/admin/user/userVisionReport/del", {id: selectedRow.id}, function (rs) {
          layer.closeAll();
          if (rs.success) {
            layer.msg(rs.message, {icon: 1});
            userVisionReport.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    }
  },
  tableEl: $("#userVisionReport_table_list"),
  modalEl: $("#userVisionReport_modal"),
  initTable: function () {
    userVisionReport.tableEl.bootstrapTable({
      url: ctx + "/admin/user/userVisionReport/page",
      columns: [{
          checkbox: true
        },
        {
          title: "创建时间",
          field: "createTime"
        },
        {
          title: "修改时间",
          field: "updateTime"
        },
        {
          title: "用户档案ID",
          field: "userArchiveId"
        },
        {
          title: "文件地址",
          field: "fileArray"
        }
      ]
    });
  }
};
$(function () {
  userVisionReport.initTable();
  $(".layer-date").each(function(){
    laydate.render({elem:this, type:'datetime'});
  });
});