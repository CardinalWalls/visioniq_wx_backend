var userInspectReport = {
  add: function () {
    //userInspectReport.modalEl.find("form [name=id]").val("");
    //userInspectReport.modalEl.find("form input.form-control").val("");
    userInspectReport.modalEl.find("form")[0].reset();
    attachment.clear("fileArray", "otherFileArray");
    userInspectReport.modalEl.find(".save-btn").show();
    userInspectReport.modal("新增");
  },
  edit:function(read, id){
    var selectedRow = id ? userInspectReport.tableEl.bootstrapTable('getRowByUniqueId', id) : userInspectReport.getSelectedRow();
    if (selectedRow) {
      userInspectReport.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        var v = selectedRow[$this.attr("name")];
        if(v !== undefined){
          $this.val(v + "");
        }
      });
      attachment.setArray("fileArray", JSON.parse(selectedRow.fileArray));
      attachment.setArray("otherFileArray", JSON.parse(selectedRow.otherFileArray));
      if(read){
        userInspectReport.modalEl.find(".save-btn").hide();
        userInspectReport.modal("查看");
      }else{
        userInspectReport.modalEl.find(".save-btn").show();
        userInspectReport.modal("修改");
      }
    }
  },
  modal: function (title) {
    if (title) {
      userInspectReport.modalEl.find(".modal-title").html(title);
      userInspectReport.modalEl.modal();
    } else {
      userInspectReport.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = userInspectReport.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length === 1) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.msg("请选择一行数据", {icon: 2});
    return null;
  },
  refresh: function () {
    userInspectReport.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = userInspectReport.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/user/userInspectReport/save", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        userInspectReport.modal(null);
        layer.msg(rs.message, {icon: 1});
        userInspectReport.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  del: function () {
    var selectedRow = userInspectReport.tableEl.bootstrapTable("getSelections")
    if (selectedRow.length > 0) {
      var ids = selectedRow.map(r=>r.id).join(",");
      layer.confirm("确定删除这些数据？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        layer.load(1, {shade: 0.3});
        $.deletePayload(ctx + "/admin/user/userInspectReport/del", {id: ids}, function (rs) {
          layer.closeAll();
          if (rs.success) {
            layer.msg("删除数量：" + rs.data, {icon: 1});
            userInspectReport.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    }
  },
  showImportData:function(){
    var modal = $("#import_modal");
    modal.find("form")[0].reset();
    attachment.clear("dataFile");
    modal.modal();
  },
  importData:function(){
    var f = $("#import_modal form");
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/user/userInspectReport/import", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        var errors = rs.data.errors;
        layer.alert("导入完成，新增：" + rs.data.add
          + (errors.length > 0 ? "<div class='text-danger'><div><b>错误信息：</b></div>"
            + errors.map(function (s){return "<div>" + s + "</div>"}).join("") + "</div>":""), {icon: 1});
        userInspectReport.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  importNumDelete:function (){
    let html = `<form ><div class="form-group"><label>导入批次号：</label><div><input class="form-control" id="importNum_" required/></div></div></form>`;
    layer.open({
      title:"导入批次删除",
      content: html,
      yes: function(index, layero){
        $.deletePayload(ctx + "/admin/user/userInspectReport/importNum/del", {importNum:$("#importNum_").val()}, function (rs) {
          if (rs.success) {
            layer.msg("已删除条数：" + rs.data, {icon: 1});
            userInspectReport.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      }
    });
  },
  tableEl: $("#userInspectReport_table_list"),
  modalEl: $("#userInspectReport_modal"),
  initTable: function () {
    userInspectReport.tableEl.bootstrapTable({
      url: ctx + "/admin/user/userInspectReport/page",
      singleSelect: false,
      checkboxHeader: true,
      columns: [{
          checkbox: true
        },
        {
          title: "身份证",
          field: "idcard",
          formatter: function (value, row, index) {
            return `<a class="cursor-pointer" title="查看" onclick="userInspectReport.edit(true, '${row.id}')">${value}</a>`
          }
        },
        {
          title: "姓名",
          field: "name",
          formatter: function (value, row, index) {
            return `<div style='min-width:40px'>${value}</div>`
          }
        },
        {
          title: "性别",
          field: "gender",
          formatter: function (value, row, index) {
            if (value === 1) {
              return "男";
            } else if (value === 2){
              return "女"
            }
            return "";
          }
        },
        {
          title: "导入批次",
          field: "importNum"
        },
        {
          title: "检查日期",
          field: "inspectDate"
        },
        {
          title: "学校",
          field: "school",
          formatter: function (value, row, index) {
            return `<div style='white-space:pre'>${value}</div>`
          }
        },
        {
          title: "班级",
          field: "className"
        },
        {
          title: "右眼裸眼(RV)",
          field: "rightVisual"
        },
        {
          title: "右眼球镜(R/S)",
          field: "rightDiopterS"
        },
        {
          title: "右眼柱镜(R/C)",
          field: "rightDiopterC"
        },
        {
          title: "右眼轴位(R/A)",
          field: "rightShaftPosition"
        },
        {
          title: "左眼裸眼(LV)",
          field: "leftVisual"
        },
        {
          title: "左眼球镜(L/S)",
          field: "leftDiopterS"
        },
        {
          title: "左眼柱镜(L/C)",
          field: "leftDiopterC"
        },
        {
          title: "左眼轴位(L/A)",
          field: "leftShaftPosition"
        },
        {
          title: "左眼眼轴",
          field: "leftAxis"
        },
        {
          title: "左眼曲率半径",
          field: "leftCurvatureRadius"
        },
        {
          title: "右眼眼轴",
          field: "rightAxis"
        },
        {
          title: "右眼曲率半径",
          field: "rightCurvatureRadius"
        },
        {
          title: "检查单据",
          field: "fileArray",
          formatter: function (value, row, index) {
            let html = "";
            if(value){
              try {
                let array = JSON.parse(value);
                for (let i = 0; i < array.length; i++) {
                  html += attachment.toViewHtml(array[i].url) + " ";
                }
              } catch (e) {
              }
              return html;
            }
          }
        },
        {
          title: "其它单据",
          field: "otherFileArray",
          formatter: function (value, row, index) {
            let html = "";
            if(value){
              try {
                let array = JSON.parse(value);
                for (let i = 0; i < array.length; i++) {
                  html += attachment.toViewHtml(array[i].url) + " ";
                }
              } catch (e) {
              }
              return html;
            }
          }
        },
        {
          title: "校外用眼时长",
          field: "outSchoolHours"
        },
        {
          title: "坐姿不正确",
          field: "incorrectSittin"
        },
        {
          title: "佩戴眼镜不正确",
          field: "incorrectGlasses"
        },
        {
          title: "过敏情况",
          field: "allergy"
        },
        {
          title: "眼镜种类",
          field: "glassesType"
        },
        {
          title: "其它措施",
          field: "otherSolution"
        },
        {
          title: "户外用眼时长",
          field: "outdoorsHours"
        },
        {
          title: "其它说明",
          field: "otherDescription"
        },
        {
          title: "户外用眼时长说明",
          field: "outdoorsHoursExplain"
        },
        {
          title: "眼镜种类说明",
          field: "glassesTypeExplain"
        },
        {
          title: "其它措施说明",
          field: "otherSolutionExplain"
        },
        {
          title: "检测医院",
          field: "hospital"
        },
        {
          title: "当前身高",
          field: "height"
        },
        {
          title: "档案姓名",
          field: "archiveName"
        },
        {
          title: "档案性别",
          field: "archiveGender",
          formatter: function (value, row, index) {
            if (value === 1) {
              return "男";
            } else if (value === 2){
              return "女"
            }
            return "";
          }
        },
        {
          title: "档案学校",
          field: "archiveSchool"
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
          title: "关联账号",
          field: "accountPhone"
        }
      ]
    });
  }
};
$(function () {
  $.defaultLayDate("#inspectDateS", "#inspectDateE", "date");
  userInspectReport.initTable();
  $(".layer-date").each(function(){
    laydate.render({elem:this, type:'date'});
  });
});