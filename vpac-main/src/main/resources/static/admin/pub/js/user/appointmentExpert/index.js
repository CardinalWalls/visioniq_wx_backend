var appointmentExpert = {
  add: function () {
    //appointmentExpert.modalEl.find("form [name=id]").val("");
    //appointmentExpert.modalEl.find("form input.form-control").val("");
    appointmentExpert.modalEl.find("form")[0].reset();
    appointmentExpert.modalEl.find(".save-btn").show();
    appointmentExpert.modal("新增");
  },
  edit:function(read){
    var selectedRow = appointmentExpert.getSelectedRow();
    if (selectedRow) {
      appointmentExpert.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        var v = selectedRow[$this.attr("name")];
        if(v !== undefined){
          $this.val(v + "");
        }
      });
      if(read){
        appointmentExpert.modalEl.find(".save-btn").hide();
        appointmentExpert.modal("查看");
      }else{
        appointmentExpert.modalEl.find(".save-btn").show();
        appointmentExpert.modal("修改");
      }
    }
  },
  modal: function (title) {
    if (title) {
      appointmentExpert.modalEl.find(".modal-title").html(title);
      appointmentExpert.modalEl.modal();
    } else {
      appointmentExpert.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = appointmentExpert.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.msg("请选择一行数据", {icon: 2});
    return null;
  },
  refresh: function () {
    appointmentExpert.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = appointmentExpert.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/user/appointmentExpert/save", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        appointmentExpert.modal(null);
        layer.msg(rs.message, {icon: 1});
        appointmentExpert.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  del: function () {
    var selectedRow = appointmentExpert.getSelectedRow();
    if (selectedRow) {
      layer.confirm("确定删除该数据？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        layer.load(1, {shade: 0.3});
        $.deletePayload(ctx + "/admin/user/appointmentExpert/del", {id: selectedRow.id}, function (rs) {
          layer.closeAll();
          if (rs.success) {
            layer.msg(rs.message, {icon: 1});
            appointmentExpert.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    }
  },
  tableEl: $("#appointmentExpert_table_list"),
  modalEl: $("#appointmentExpert_modal"),
  initTable: function () {
    appointmentExpert.tableEl.bootstrapTable({
      url: ctx + "/admin/user/appointmentExpert/page",
      columns: [{
        checkbox: true
      },
      {
        title: "专家-姓名",
        field: "expertName"
      },
      {
        title: "专家-职称",
        field: "expertTitle"
      },
      {
        title: "专家-职位",
        field: "expertJobPosition"
      },
      {
        title: "专家-医院",
        field: "expertHospital"
      },
      {
        title: "专家-科室",
        field: "expertDepartment"
      },
      {
        title: "父母-手机",
        field: "userPhone"
      },
      {
        title: "档案-姓名",
        field: "userName"
      },
      {
        title: "档案-身份证",
        field: "userIdCard"
      },
      {
        title: "档案-性别",
        field: "userGender",
        formatter: function (value, row, index) {
          return value === 1? "男" : (value === 2 ? "女": "");
        }
      },
      {
        title: "档案-生日",
        field: "userBirth"
      },
      {
        title: "预约时间",
        field: "targetTime"
      }
      ]
    });
  }
};
$(function () {
  appointmentExpert.initTable();
  $(".layer-date").each(function(){
    laydate.render({elem:this, type:'date'});
  });
});