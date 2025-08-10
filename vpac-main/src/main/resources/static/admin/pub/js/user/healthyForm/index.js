var healthyForm = {
  add: function () {
    //healthyForm.modalEl.find("form [name=id]").val("");
    //healthyForm.modalEl.find("form input.form-control").val("");
    healthyForm.modalEl.find("form")[0].reset();
    healthyForm.modalEl.find(".save-btn").show();
    healthyForm.modal("新增");
  },
  edit:function(read){
    var selectedRow = healthyForm.getSelectedRow();
    if (selectedRow) {
      healthyForm.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        var v = selectedRow[$this.attr("name")];
        if(v !== undefined){
          $this.val(v + "");
        }
      });
      if(read){
        healthyForm.modalEl.find(".save-btn").hide();
        healthyForm.modal("查看");
      }else{
        healthyForm.modalEl.find(".save-btn").show();
        healthyForm.modal("修改");
      }
    }
  },
  modal: function (title) {
    if (title) {
      healthyForm.modalEl.find(".modal-title").html(title);
      healthyForm.modalEl.modal();
    } else {
      healthyForm.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = healthyForm.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.msg("请选择一行数据", {icon: 2});
    return null;
  },
  refresh: function () {
    healthyForm.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = healthyForm.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/user/healthyForm/save", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        healthyForm.modal(null);
        layer.msg(rs.message, {icon: 1});
        healthyForm.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  del: function () {
    var selectedRow = healthyForm.getSelectedRow();
    if (selectedRow) {
      layer.confirm("确定删除该数据？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        layer.load(1, {shade: 0.3});
        $.deletePayload(ctx + "/admin/user/healthyForm/del", {id: selectedRow.id}, function (rs) {
          layer.closeAll();
          if (rs.success) {
            layer.msg(rs.message, {icon: 1});
            healthyForm.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    }
  },
  tableEl: $("#healthyForm_table_list"),
  modalEl: $("#healthyForm_modal"),
  initTable: function () {
    healthyForm.tableEl.bootstrapTable({
      url: ctx + "/admin/user/healthyForm/page",
      columns: [{
          checkbox: true
        },
        {
          title: "userId",
          field: "userId",
          visible:false
        },
        {
          title: "姓名",
          field: "name"
        },
        {
          title: "性别",
          field: "gender"
        },
        {
          title: "年龄",
          field: "age"
        },
        {
          title: "身高cm",
          field: "height"
        },
        {
          title: "体重kg",
          field: "weight"
        },
        {
          title: "BMI",
          field: "bmi"
        },
        {
          title: "近一个月体重",
          field: "weightOneMonth"
        },
        {
          title: "近二个月体重",
          field: "weightTwoMonth"
        },
        {
          title: "近三个月体重",
          field: "weightThreeMonth"
        },
        {
          title: "近四个月体重",
          field: "weightFourMonth"
        },
        {
          title: "近五个月体重",
          field: "weightFiveMonth"
        },
        {
          title: "近六个月体重",
          field: "weightSixMonth"
        },
        {
          title: "基础疾病",
          field: "diseases"
        },
        {
          title: "早餐主食",
          field: "breakfastMain"
        },
        {
          title: "早餐蔬菜",
          field: "breakfastVegetable"
        },
        {
          title: "早餐肉类",
          field: "breakfastMeat"
        },
        {
          title: "早餐豆类",
          field: "breakfastBean"
        },
        {
          title: "早餐奶类",
          field: "breakfastMilk"
        },
        {
          title: "早餐水果",
          field: "breakfastFruits"
        },
        {
          title: "午餐主食",
          field: "lunchMain"
        },
        {
          title: "午餐蔬菜",
          field: "lunchVegetable"
        },
        {
          title: "午餐肉类",
          field: "lunchMeat"
        },
        {
          title: "午餐豆类",
          field: "lunchBean"
        },
        {
          title: "午餐奶类",
          field: "lunchMilk"
        },
        {
          title: "午餐水果",
          field: "lunchFruits"
        },
        {
          title: "晚餐主食",
          field: "dinnerMain"
        },
        {
          title: "晚餐蔬菜",
          field: "dinnerVegetable"
        },
        {
          title: "晚餐肉类",
          field: "dinnerMeat"
        },
        {
          title: "晚餐豆类",
          field: "dinnerBean"
        },
        {
          title: "晚餐奶类",
          field: "dinnerMilk"
        },
        {
          title: "晚餐水果",
          field: "dinnerFruits"
        },
        {
          title: "保健品",
          field: "healthProducts"
        },
        {
          title: "体力活动",
          field: "physicalLabour"
        },
        {
          title: "运动方式",
          field: "sportInfo"
        },
        {
          title: "运动频率",
          field: "sportFrequency"
        },
        {
          title: "大便情况",
          field: "shitInfo"
        },
        {
          title: "睡眠情况",
          field: "sleepInfo"
        },
        {
          title: "更新时间",
          field: "updateTime"
        },
        {
          title: "创建时间",
          field: "createTime"
        }
      ]
    });
  }
};
$(function () {
  healthyForm.initTable();
  $(".layer-date").each(function(){
    laydate.render({elem:this, type:'datetime'});
  });
});