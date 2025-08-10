var userVisualFunctionReport = {
  add: function () {
    //userVisualFunctionReport.modalEl.find("form [name=id]").val("");
    //userVisualFunctionReport.modalEl.find("form input.form-control").val("");
    userVisualFunctionReport.modalEl.find("form")[0].reset();
    userVisualFunctionReport.modalEl.find(".save-btn").show();
    userVisualFunctionReport.modal("新增");
  },
  edit:function(read){
    var selectedRow = userVisualFunctionReport.getSelectedRow();
    if (selectedRow) {
      userVisualFunctionReport.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        var v = selectedRow[$this.attr("name")];
        if(v !== undefined){
          $this.val(v + "");
        }
      });
      if(read){
        userVisualFunctionReport.modalEl.find(".save-btn").hide();
        userVisualFunctionReport.modal("查看");
      }else{
        userVisualFunctionReport.modalEl.find(".save-btn").show();
        userVisualFunctionReport.modal("修改");
      }
    }
  },
  modal: function (title) {
    if (title) {
      userVisualFunctionReport.modalEl.find(".modal-title").html(title);
      userVisualFunctionReport.modalEl.modal();
    } else {
      userVisualFunctionReport.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = userVisualFunctionReport.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.msg("请选择一行数据", {icon: 2});
    return null;
  },
  refresh: function () {
    userVisualFunctionReport.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = userVisualFunctionReport.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/user/userVisualFunctionReport/save", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        userVisualFunctionReport.modal(null);
        layer.msg(rs.message, {icon: 1});
        userVisualFunctionReport.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  del: function () {
    var selectedRow = userVisualFunctionReport.getSelectedRow();
    if (selectedRow) {
      layer.confirm("确定删除该数据？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        layer.load(1, {shade: 0.3});
        $.deletePayload(ctx + "/admin/user/userVisualFunctionReport/del", {id: selectedRow.id}, function (rs) {
          layer.closeAll();
          if (rs.success) {
            layer.msg(rs.message, {icon: 1});
            userVisualFunctionReport.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    }
  },
  tableEl: $("#userVisualFunctionReport_table_list"),
  modalEl: $("#userVisualFunctionReport_modal"),
  initTable: function () {
    userVisualFunctionReport.tableEl.bootstrapTable({
      url: ctx + "/admin/user/userVisualFunctionReport/page",
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
          title: "检查日期",
          field: "inspectDate"
        },
        {
          title: "主视眼",
          field: "dominantEye"
        },
        {
          title: "遮盖实验",
          field: "coverTest"
        },
        {
          title: "诉求",
          field: "appeal"
        },
        {
          title: "左眼球镜",
          field: "leftSphere"
        },
        {
          title: "右眼球镜",
          field: "rightSphere"
        },
        {
          title: "左眼柱镜",
          field: "leftCylinder"
        },
        {
          title: "右眼柱镜",
          field: "rightCylinder"
        },
        {
          title: "左眼轴位",
          field: "leftAxial"
        },
        {
          title: "右眼轴位",
          field: "rightAxial"
        },
        {
          title: "左眼最佳矫正视力",
          field: "leftBcva"
        },
        {
          title: "右眼最佳矫正视力",
          field: "rightBcva"
        },
        {
          title: "左眼针孔视力",
          field: "leftPhva"
        },
        {
          title: "右眼针孔视力",
          field: "rightPhva"
        },
        {
          title: "近附加",
          field: "adds"
        },
        {
          title: "远重影",
          field: "farGhost"
        },
        {
          title: "近重影",
          field: "nearGhost"
        },
        {
          title: "视远不清",
          field: "farClearly"
        },
        {
          title: "视近不清",
          field: "nearClearly"
        },
        {
          title: "眼痛",
          field: "ophthalmalgia"
        },
        {
          title: "头痛",
          field: "headache"
        },
        {
          title: "串字跳行",
          field: "skipLines"
        },
        {
          title: "疲劳",
          field: "fatigue"
        },
        {
          title: "其它症状",
          field: "otherSymptoms"
        },
        {
          title: "立体视",
          field: "stereopsis"
        },
        {
          title: "worth 4 dot",
          field: "worth4Dot"
        },
        {
          title: "远距水平隐斜",
          field: "dlp"
        },
        {
          title: "远距垂直隐斜",
          field: "dvp"
        },
        {
          title: "近距水平隐斜",
          field: "nlp"
        },
        {
          title: "近距垂直隐斜",
          field: "nvp"
        },
        {
          title: "AC/A",
          field: "acA"
        },
        {
          title: "调节反应",
          field: "bcc"
        },
        {
          title: "负相对调节",
          field: "nra"
        },
        {
          title: "正相对调节",
          field: "pra"
        },
        {
          title: "调节灵敏度",
          field: "af"
        },
        {
          title: "调节幅度",
          field: "amp"
        },
        {
          title: "集合近点",
          field: "npc"
        },
        {
          title: "远融像范围BI",
          field: "farFusionRangeBi"
        },
        {
          title: "远融像范围BO",
          field: "farFusionRangeBo"
        },
        {
          title: "近融像范围BI",
          field: "nearFusionRangeBi"
        },
        {
          title: "近融像范围BO",
          field: "nearFusionRangeBo"
        },
        {
          title: "备注",
          field: "remark"
        },
        {
          title: "缓解眼镜",
          field: "reliefGlasses"
        },
        {
          title: "训练内容",
          field: "trainingContent"
        },
        {
          title: "训练方式",
          field: "trainingStyle"
        },
        {
          title: "家庭训练器械",
          field: "homeTrainingEquipment"
        },
        {
          title: "复查时间",
          field: "reviewTime"
        },
        {
          title: "视光师",
          field: "optometrists"
        }
      ]
    });
  }
};
$(function () {
  userVisualFunctionReport.initTable();
  $(".layer-date").each(function(){
    laydate.render({elem:this, type:'datetime'});
  });
});