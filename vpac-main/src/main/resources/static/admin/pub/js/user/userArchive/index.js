var userArchive = {
  // add: function () {
  //   userArchive.modalEl.find("form")[0].reset();
  //   userArchive.modalEl.find(".save-btn").show();
  //   userArchive.modal("新增");
  // },
  edit:function(read){
    var selectedRow = userArchive.getSelectedRow();
    if (selectedRow) {
      userArchive.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        var v = selectedRow[$this.attr("name")];
        if(v !== undefined){
          $this.val(v + "");
        }
      });
      regionSelect.selectVal("#regionId", selectedRow.regionId);
      $("#expertId").selectPageClear().val(selectedRow.expertId).selectPageRefresh();
      $("#userPhone").val(selectedRow.parentPhone);
      if(read){
        userArchive.modalEl.find(".save-btn").hide();
        userArchive.modal("查看");
      }else{
        userArchive.modalEl.find(".save-btn").show();
        userArchive.modal("修改");
      }
    }
  },
  modal: function (title) {
    if (title) {
      userArchive.modalEl.find(".modal-title").html(title);
      userArchive.modalEl.modal();
    } else {
      userArchive.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = userArchive.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.msg("请选择一行数据", {icon: 2});
    return null;
  },
  refresh: function () {
    userArchive.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = userArchive.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/user/userArchive/save", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        userArchive.modal(null);
        layer.msg(rs.message, {icon: 1});
        userArchive.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  // del: function () {
  //   var selectedRow = userArchive.getSelectedRow();
  //   if (selectedRow) {
  //     layer.confirm("确定删除该数据？", {
  //       btn: ['确定', '取消'], shade: 0.3
  //     }, function () {
  //       layer.load(1, {shade: 0.3});
  //       $.deletePayload(ctx + "/admin/user/userArchive/del", {id: selectedRow.id}, function (rs) {
  //         layer.closeAll();
  //         if (rs.success) {
  //           layer.msg(rs.message, {icon: 1});
  //           userArchive.refresh();
  //         } else {
  //           layer.alert(rs.message, {icon: 2});
  //         }
  //       });
  //     });
  //   }
  // },
  tableEl: $("#userArchive_table_list"),
  modalEl: $("#userArchive_modal"),
  initTable: function () {
    userArchive.tableEl.bootstrapTable({
      url: ctx + "/admin/user/userArchive/page",
      columns: [{
          checkbox: true
        },
        {
          title: "姓名",
          field: "name"
        },
        {
          title: "性别",
          field: "gender",
          formatter:function (value, row) {
            return value === 1 ? "男" : "女";
          }
        },
        {
          title: "出生日期",
          field: "birth"
        },
        {
          title: "父母手机号",
          field: "parentPhone"
        },
        {
          title: "所属专家",
          field: "expertName",
          formatter:function (value, row) {
            if (!row.expertPhone){
              return "";
            }
            return `<label class="label label-default">${row.expertPhone}-${value}</label>`;
          }
        },
        {
          title: "地区",
          field: "regionName"
        },
        {
          title: "身份证",
          field: "idcard"
        },
        {
          title: "双亲是否近视",
          field: "parentsMyopia",
          formatter:function (value, row) {
            if(value === 1){
              return "父亲"
            }
            if(value === 2){
              return "母亲"
            }
            if(value === 3){
              return "父母"
            }
            return "无";
          }
        },
        {
          title: "风险等级",
          field: "riskLevel",
          formatter:function (value, row) {
            switch (value){
              case 1 : return "低风险";
              case 2 : return "中风险";
              case 3 : return "高风险";
              default: return "";
            }
          }
        },
        {
          title: "创建时间",
          field: "createTime"
        },
        {
          title: "备注",
          field: "remark"
        },
        {
          title: "修改时间",
          field: "updateTime",
        }
      ]
    });
  }
};
$(function () {
  userArchive.initTable();
  $.selectPage($("#expertId,#searchExpertId"), "/admin/user/userBaseInfo/expert/page", {formatItem : function (data){
    return `${data.name} | ${data.phone}${data.hospital?" | "+data.hospital:""}${data.department?" | "+data.department:""}`;
  }});
  regionSelect.initData(function (){
    regionSelect.build("#regionId");
  });
  $(".layer-date").each(function(){
    laydate.render({elem:this, type:'date'});
  });
});