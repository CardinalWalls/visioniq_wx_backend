var userPushMessage = {
  conditionDiv:$("#conditionDiv"),
  add: function () {
    $("#publishTimeText").hide().find("span").html('');
    userPushMessage.modalEl.find("form")[0].reset();
    conditionTool.resetCondition(userPushMessage.conditionDiv);
    userPushMessage.refreshUserArchive();
    $("#typeId").selectPageClear();
    userPushMessage.modalEl.find(".save-btn").show();
    userPushMessage.modal("新增");
  },
  edit:function(read){
    var selectedRow = userPushMessage.getSelectedRow();
    if (selectedRow) {
      userPushMessage.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        var v = selectedRow[$this.attr("name")];
        if(v !== undefined){
          $this.val(v + "");
        }
      });
      if(selectedRow.publishTime){
        $("#publishTimeText").show().find("span").html(selectedRow.publishTime);
      }else{
        $("#publishTimeText").hide().find("span").html('');
      }
      if(selectedRow.conditionJson){
        conditionTool.resetCondition(userPushMessage.conditionDiv, JSON.parse(selectedRow.conditionJson));
      }
      $("#typeId").val(selectedRow.typeId).selectPageRefresh();
      userPushMessage.refreshUserArchive();
      if(read){
        userPushMessage.modalEl.find(".save-btn").hide();
        userPushMessage.conditionDiv.find("button").hide();
        userPushMessage.conditionDiv.find(".form-control").attr("disabled", "disabled");
        userPushMessage.modal("查看");
      }else{
        if(selectedRow.publishTime){
          layer.msg("此消息已发布，不能再修改", {icon: 2});
        }else{
          userPushMessage.modalEl.find(".save-btn").show();
          userPushMessage.conditionDiv.find("button").show();
          userPushMessage.conditionDiv.find(".form-control").removeAttr("disabled");
          userPushMessage.modal("修改");
        }
      }
    }
  },
  modal: function (title) {
    if (title) {
      userPushMessage.modalEl.find(".modal-title").html(title);
      userPushMessage.modalEl.modal();
    } else {
      userPushMessage.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = userPushMessage.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.msg("请选择一行数据", {icon: 2});
    return null;
  },
  refresh: function () {
    userPushMessage.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = userPushMessage.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    params.conditionJson = JSON.stringify(conditionTool.loadConditionJson(userPushMessage.conditionDiv));
    $.postPayload(ctx + "/admin/user/userPushMessage/save", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        userPushMessage.modal(null);
        layer.msg(rs.message, {icon: 1});
        userPushMessage.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  publish:function (id){
    if(id){
      layer.confirm("确定发布此消息？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        layer.load(1, {shade: 0.3});
        $.putPayload(ctx + "/admin/user/userPushMessage/publish/" + id, {}, function (rs) {
          layer.closeAll();
          if (rs.success) {
            layer.msg("发布成功，接收消息人数：" + rs.data, {icon: 1});
            userPushMessage.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    }
  },
  del: function () {
    var selectedRow = userPushMessage.getSelectedRow();
    if (selectedRow) {
      layer.confirm("确定删除该数据？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        layer.load(1, {shade: 0.3});
        $.deletePayload(ctx + "/admin/user/userPushMessage/del", {id: selectedRow.id}, function (rs) {
          layer.closeAll();
          if (rs.success) {
            layer.msg(rs.message, {icon: 1});
            userPushMessage.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    }
  },
  tableEl: $("#userPushMessage_table_list"),
  modalEl: $("#userPushMessage_modal"),
  initTable: function () {
    userPushMessage.tableEl.bootstrapTable({
      url: ctx + "/admin/user/userPushMessage/page",
      columns: [{
          checkbox: true
        },
        {
          title: "分类名称",
          field: "typeName"
        },
        {
          title: "文字内容",
          field: "content"
        },
        {
          title: "链接地址",
          field: "url"
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
          title: "过滤条件",
          field: "conditionJson",
          formatter:function (value, row) {
            return conditionTool.conditionJsonToView(value);
          }
        },
        {
          title: "发布时间",
          field: "publishTime",
          formatter:function (value, row) {
            return value ? value : `<button class="btn btn-xs btn-info" onclick="userPushMessage.publish('${row.id}')"><i class="fa fa-send-o"></i> 发布</button>`;
          }
        },
        {
          title: "接收消息人数",
          field: "publishCount",
          align: "right"
        },
        {
          title: "操作人",
          field: "operatorName"
        }
      ]
    });
  },
  refreshUserArchive:function (){
    $("#userArchive_table_list").bootstrapTable("refresh");
  },
  initUserArchiveTable: function () {
    $("#userArchive_table_list").bootstrapTable({
      url: ctx + "/admin/user/userPushMessage/userArchive/page",
      queryParams:function (params){
        params.conditionJson = JSON.stringify(conditionTool.loadConditionJson(userPushMessage.conditionDiv));
        params.publishTime = $("#publishTimeText span").text();
        return params;
      },
      pageSize:5,
      columns: [
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
          title: "修改时间",
          field: "updateTime",
          visible:false
        }
      ]
    });
  }
};
$(function () {
  $.selectPage($("#typeId,#searchTypeId"), ctx + "/admin/user/userPushMessage/type/page", {formatItem : function (data){
    return `${data.name}${data.valid?"":"（无效）"}`;
  }});
  regionSelect.initData();
  userPushMessage.initTable();
  conditionTool.initContext(userPushMessage.conditionDiv, conditionFields);
  $(".layer-date").each(function(){
    laydate.render({elem:this, type:'datetime'});
  });
  userPushMessage.initUserArchiveTable();
});