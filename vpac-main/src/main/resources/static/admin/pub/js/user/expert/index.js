var expert = {
  add: function () {
    //expert.modalEl.find("form [name=id]").val("");
    //expert.modalEl.find("form input.form-control").val("");
    $("#addDiv").show();
    $("#editDiv").hide();
    attachment.clear("avatar");
    expert.modalEl.find("form")[0].reset();
    expert.modalEl.find(".save-btn").show();
    citySelect.selectVal("#districtId","500105");
    expert.modal("新增");
  },
  edit:function(read){
    var selectedRow = expert.getSelectedRow();
    if (selectedRow) {
      expert.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        var v = selectedRow[$this.attr("name")];
        if(v !== undefined){
          $this.val(v + "");
        }
      });
      citySelect.selectVal("#districtId",selectedRow.regionId);
      $("#addDiv").hide();
      $("#editDiv").show();
      expert.appointmentWeekLimitClick(selectedRow.appointmentWeekLimit);
      $("#userId").selectPageClear().val(selectedRow.userId).selectPageRefresh();
      if(read){
        attachment.setSingle("avatar", selectedRow.avatar, null, true);
        attachment.setArray("workCard", JSON.parse(selectedRow.workCard), null, true);
        expert.modalEl.find(".save-btn").hide();
        expert.modal("查看");
      }else{
        attachment.setSingle("avatar", selectedRow.avatar);
        attachment.setArray("workCard", JSON.parse(selectedRow.workCard));
        expert.modalEl.find(".save-btn").show();
        expert.modal("修改");
      }
    }
  },
  modal: function (title) {
    if (title) {
      expert.modalEl.find(".modal-title").html(title);
      expert.modalEl.modal();
    } else {
      expert.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = expert.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.msg("请选择一行数据", {icon: 2});
    return null;
  },
  refresh: function () {
    expert.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = expert.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    params.appointmentWeekLimit = $("#appointmentWeekLimit input").get().map(function(e){return e.checked?e.dataset["value"]:""}).join("");
    $.postPayload(ctx + "/admin/user/expert/save", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        expert.modal(null);
        layer.msg(rs.message, {icon: 1});
        expert.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  qrCode: function (id) {
    if (!id) {
      return;
    }
    layer.load(1, {shade: 0.3});
    $.postPayload(ctx + "/admin/user/expert/qrCode", {id: id}, function (rs) {
      layer.closeAll();
      if (rs.success) {
        console.log(rs.data);
        layer.msg(rs.message, {icon: 1});
        expert.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  del: function () {
    var selectedRow = expert.getSelectedRow();
    if (selectedRow) {
      layer.confirm("确定删除该数据？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        layer.load(1, {shade: 0.3});
        $.deletePayload(ctx + "/admin/user/expert/del", {id: selectedRow.id}, function (rs) {
          layer.closeAll();
          if (rs.success) {
            layer.msg(rs.message, {icon: 1});
            expert.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    }
  },
  appointmentWeekLimitClick:function (week){
    $("#appointmentWeekLimit input").each(function (e){
      if(week !== undefined){
        this.checked = week.indexOf(this.dataset["value"]) >= 0;
      }
      if(this.checked){
        $(this).parent().addClass("text-disabled");
      }
      else{
        $(this).parent().removeClass("text-disabled");
      }
    });
  },
  audit:function (id){
    layer.confirm("确定审核通过该专家？", {
      btn: ['是','否'],
      shade: 0.3
    }, function(){
      layer.load(1, {shade: 0.3});
      $.postPayload(ctx + "/admin/user/expert/audit", {id: id}, function(res){
        layer.closeAll();
        if(res.success){
          layer.msg(res.message, {icon: 1});
          expert.refresh();
        }
        else{
          layer.alert(res.message, {icon: 2});
        }
      });
    });
  },
  tableEl: $("#expert_table_list"),
  modalEl: $("#expert_modal"),
  initTable: function () {
    expert.tableEl.bootstrapTable({
      url: ctx + "/admin/user/expert/page",
      columns: [{
          checkbox: true
        },
        {
          title: "绑定人数",
          field: "refCount",
          align:"right"
        },
        {
          title: "头像",
          field: "avatar",
          formatter: function (value, row, index) {
            return attachment.toViewHtml(value, "max-height:50px");
          }
        },
        {
          title: "姓名",
          field: "name"
        },
        {
          title: "性别",
          field: "gender",
          formatter: function (value, row, index) {
            return 1 === value?"男":(2===value?"女":"未设置");
          }
        },
        {
          title: "手机号",
          field: "phone",
          formatter: function (value, row, index) {
            return "<a onclick=\"expert.gotoUser('"+value+"')\">"+value+"</a>";
          }
        },
        {
          title: "职称",
          field: "title"
        },
        {
          title: "地区",
          field: "regionName"
        },
        {
          title: "职位",
          field: "jobPosition"
        },
        {
          title: "医院",
          field: "hospital"
        },
        {
          title: "科室",
          field: "department"
        },
        {
          title: "等级",
          field: "level"
        },
        {
          title: "擅长专业",
          field: "proficient"
        },
        {
          title: "个人简介",
          field: "profile"
        },
        {
          title: "预约限制(星期)",
          field: "appointmentWeekLimit"
        },
        {
          title: "状态",
          field: "status",
          formatter: function (value, row, index) {
            if (value === 1){
              return "有效";
            }
            else if(value === 0){
              return "<span class='text-danger'>无效</span>";
            }
            else{
              return `<button class='btn btn-info btn-xs' onclick="expert.audit('${row.id}')"><i class='fa fa-edit'></i> 待审核</button>`;
            }
          }
        },
        {
          title: "工作证件",
          field: "workCard",
          formatter: function (value, row, index) {
            if(value){
              var array = JSON.parse(value);
              var html = "";
              for (var i = 0; i < array.length; i++) {
                html += attachment.toViewHtml(array[i].url, "max-height:50px") + " ";
              }
              return "<div class='text-nowrap'>"+html+"</div>";
            }
            return "";
          }
        },
        {
          title: "二维码",
          field: "qrCode",
          align: "center",
          formatter: function (value, row, index) {
            let img = "";
            if(value){
              img = attachment.toViewHtml(value, "max-width:50px;max-height:50px");
            }
            return `<div style="width:75px">${img}<button class="btn btn-xs btn-white" onclick="expert.qrCode('${row.id}')" title="重新生成新的二维码"><i class="fa fa-plus"></i></button></div>`;
          }
        },
        {
          title: "创建时间",
          field: "createTime"
        },
        {
          title: "修改时间",
          field: "updateTime"
        }
      ]
    });
  },
  gotoUser:function (phone) {
    if(phone){
      if(top.index){
        top.index.showNav(ctx + "/admin/user/userBaseInfo/index?phone=" + phone, true);
      }else{
        window.open(ctx + "/admin/user/userBaseInfo/index?phone=" + phone);
      }
    }
  },
};
$(function () {
  expert.initTable();
  citySelect.init([{select:"#districtId", value:"500105"}], function () {
  });

  var opt = {
    searchField:"searchKey",
    keyField : 'id',
    multiple: false,
    eAjaxSuccess : function(res){
      return {
        list:res.list,
        totalRow:res.total
      };
    }
  };
  $("#userId").each(function (){
    var $this = $(this);
    $this.selectPage($.extend({}, opt, {
      data: ctx+"/admin/user/list/page",
      formatItem:function(data){
        var v = data.phone;
        if(data.name){
          v +=" | " + data.name;
        }
        if(data.realName){
          v +=" | " + data.realName;
        }
        return v;
      }
    }));
  });
});