var baseUserBaseInfo = {
  add: function () {
    baseUserBaseInfo.modalEl.find("form")[0].reset();
    baseUserBaseInfo.modalEl.find(".save-btn").show();
    $("#userType").removeAttr("disabled").html(
      `<option value="1" selected>普通用户</option>
      <option value="3">客服运营</option>`
    );
    $("#phone").removeAttr("disabled");
    $("#pwdLabel").html("登录密码：");
    baseUserBaseInfo.modal("新增");
  },
  edit:function(read){
    var selectedRow = baseUserBaseInfo.getSelectedRow();
    if (selectedRow) {
      if(selectedRow.userType === 2){
        $("#userType").attr("disabled", "disabled").html(`<option value="2">专家</option>`);
      }else{
        $("#userType").removeAttr("disabled").html(
          `<option value="1">普通用户</option>
          <option value="3">客服运营</option>`
        );
      }
      baseUserBaseInfo.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        var v = selectedRow[$this.attr("name")];
        if(v !== undefined){
          $this.val(v + "");
        }
      });
      $("#expertId").selectPageClear().val(selectedRow.expertId).selectPageRefresh();
      $("#phone").attr("disabled", "disabled");
      $("#pwd").val("");
      if(read){
        $("#pwdLabel").html("登录密码：");
        baseUserBaseInfo.modalEl.find(".save-btn").hide();
        baseUserBaseInfo.modal("查看");
      }else{
        $("#pwdLabel").html("修改密码：");
        baseUserBaseInfo.modalEl.find(".save-btn").show();
        baseUserBaseInfo.modal("修改");
      }
    }
  },
  modal: function (title) {
    if (title) {
      baseUserBaseInfo.modalEl.find(".modal-title").html(title);
      baseUserBaseInfo.modalEl.modal();
    } else {
      baseUserBaseInfo.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = baseUserBaseInfo.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.msg("请选择一行数据", {icon: 2});
    return null;
  },
  refresh: function () {
    baseUserBaseInfo.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = baseUserBaseInfo.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/user/userBaseInfo/save", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        baseUserBaseInfo.modal(null);
        layer.msg(rs.message, {icon: 1});
        baseUserBaseInfo.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
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
    $.postPayload(ctx + "/admin/user/userBaseInfo/import", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        var errors = rs.data.errors;
        layer.alert("导入完成，新增：" + rs.data.add + ", 更新：" + rs.data.update
          + (errors.length > 0 ? "<div class='text-danger'><div><b>错误信息：</b></div>"
            + errors.map(function (s){return "<div>" + s + "</div>"}).join("") + "</div>":""), {icon: 1});
        baseUserBaseInfo.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  tableEl: $("#baseUserBaseInfo_table_list"),
  modalEl: $("#baseUserBaseInfo_modal"),
  initTable: function () {
    baseUserBaseInfo.tableEl.bootstrapTable({
      url: ctx + "/admin/user/userBaseInfo/page",
      columns: [{
          checkbox: true
        },
        {
          title: "ID",
          field: "id",
          visible:false
        },
        {
          title: "手机号",
          field: "phone"
        },
        {
          title: "昵称",
          field: "userNickName"
        },
        {
          title: "姓名",
          field: "realName"
        },
        {
          title: "性别",
          field: "gender",
          formatter:function (value) {
            if(value === 1){
              return "男";
            }else if(value === 2){
              return "女";
            } else {
              return "未填写";
            }
          }
        },
        {
          title: "生日",
          field: "birth"
        },
        {
          title: "类别",
          field: "userTypeId",
          formatter:function (value, row) {
            return `<label class="label label-${row.userType === 2 ? 'primary':(row.userType === 3 ? 'success' : 'default')}">${userTypes[value]}</label>`;
          }
        },
        {
          title: "身份证",
          field: "idCard"
        },
        {
          title: "街道",
          field: "streetName",
          formatter:function (value, row) {
            return row.userType === 1 ? value : "";
          }
        },
        {
          title: "社区",
          field: "communityName",
          formatter:function (value, row) {
            return row.userType === 1 ? value : "";
          }
        },
        {
          title: "专家",
          field: "expertName",
          formatter:function (value, row) {
            if(row.userType === 1){
              var v = value;
              if(v){
                if(row.expertPhone){
                  v += " | " + row.expertPhone;
                }
                if(row.expertHospital){
                  v += " | " + row.expertHospital;
                }
                if(row.expertDepartment){
                  v += " | " + row.expertDepartment;
                }
              }
              return v;
            }
          }
        },
        {
          title: "备注",
          field: "remark"
        },
        {
          title: "状态",
          field: "status",
          formatter:function (value, row) {
            return value === 1 ? "正常" : "冻结";
          }
        },
        {
          title: "注册时间",
          field: "registTime"
        }
      ]
    });
  }
};
$(function () {
  $.selectPage($("#expertId,#searchExpertId"), "/admin/user/userBaseInfo/expert/page", {formatItem : function (data){
      return `${data.name} | ${data.phone}${data.hospital?" | "+data.hospital:""}${data.department?" | "+data.department:""}`;
    }});
  $("#searchPhone").val(getQueryValue("phone"));
  baseUserBaseInfo.initTable();
  $(".layer-date").each(function(){
    laydate.render({elem:this, type:'datetime'});
  });
});