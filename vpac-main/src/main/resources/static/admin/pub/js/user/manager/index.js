
var pi = {
  getSelectedRow: function () {
    var selectedAll = tableList.obj.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
    }
    return selectedRow;
  },
  search: function () {
    tableList.obj.bootstrapTable("refresh");
  },
  clearCondition: function () {
    document.conditionForm.reset();
    pi.search();
  },
  detail: function (id) {
    $("#detailModal").modal({
      backdrop: "static"
    });
    $("#detailModal").css("padding", "0px");
    var t_height = $(document).height();
    var h_height = $(".modal-header").height();
    $("#detailIframe").css("height", t_height - h_height - 35);
    $("#detailIframe").attr("src", ctx + "/admin/user/manager/detail?id=" + id);
  },
  showIntegral:function () {
    var selectedRow = pi.getSelectedRow();
    if(!selectedRow){
      layer.msg("请选择赠送用户",{icon:2});
      return false;
    }
    /*if(!selectedRow.parentUserNickName){
      layer.alert("当前选择成交客户无推荐人",{icon:1});
      return false;
    }*/
    $("#integral_form")[0].reset();
    $("#give_user_id").val(selectedRow.id);
    $("#giveIntegralModal").modal();
  },
  saveIntegral:function () {
    if (!$("#integral_form").valid()) {
      return;
    }
    var params = $("#integral_form").serializeJSON();
    var selectedRow = pi.getSelectedRow();
    var i0 = layer.confirm("确定赠送用户【"+selectedRow.userNickName+"】积分："+params.integral+"？", {
      btn: ['确定', '取消'], shade: 0.3
    }, function () {
      $.postPayload(ctx + "/admin/user/integral/give",
        params,
        function (rs) {
          layer.closeAll();
          if (rs.success) {
            layer.msg(rs.message, {icon: 1});
            $("#giveIntegralModal").modal("hide");
            pi.search();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        }, "json"
      );
    });
  },
  openRecharge:function(){
    var selectedRow = pi.getSelectedRow();
    if(!selectedRow){
      layer.msg("请选择充值用户",{icon:2});
      return false;
    }
    var f = $("#recharge_form");
    f[0].reset();
    f.find("input[name='userId']").val(selectedRow.id);
    attachment.clear("rechargeAttachment");
    $("#rechargeModal").modal();
  },
  saveRecharge:function () {
    if (!$("#recharge_form").valid()) {
      return;
    }
    var params = $("#recharge_form").serializeJSON();
    layer.load(1, {shade: 0.3});
    $.postPayload(ctx + "/admin/user/recharge", params,
      function (rs) {
        layer.closeAll();
        if (rs.success) {
          $("#rechargeModal").modal("hide");
          layer.msg(rs.message, {icon: 1});
          pi.search();
        } else {
          layer.alert(rs.message, {icon: 2});
        }
      }, "json"
    );
  }
};

var tableList = {
  updateLevel:function(id){
    if (id) {
      var row = tableList.obj.bootstrapTable('getRowByUniqueId', id);
      if(row){
        var content = [];
        content.push('<form >');
        content.push('<div class="form-group"><label>用户名称：'+row.userNickName+'</label></div>');
        content.push('<div class="form-group"><label>用户手机：'+row.phone+'</label></div>');
        content.push('<div class="form-group"><label>用户等级：</label>');
        content.push('<div><input type="number" class="form-control" id="updateLevel" value="'+row.level+'" /></div>');
        content.push('</div>');
        content.push('</form>');
        layer.open({
          title:"修改用户等级",
          content: content.join(''),
          yes: function(index, layero){
            var params = {userId:id, level:$("#updateLevel").val()};
            $.putPayload(ctx + "/admin/user/level", params, function (rs) {
              if (rs.success) {
                layer.msg("操作成功", {icon: 1});
                pi.search();
              } else {
                layer.alert(rs.message, {icon: 2});
              }
            });
          }
        });
      }
    }
  },
  updateParent:function(id){
    if (id) {
      var row = tableList.obj.bootstrapTable('getRowByUniqueId', id);
      if(row){
        var content = [];
        content.push('<form >');
        content.push('<div class="form-group"><label class="text-danger">修改会将此用户变更成为新推荐人的下级客户</label></div>');
        content.push('<div class="form-group"><label>用户名称：'+row.userNickName+'</label></div>');
        content.push('<div class="form-group"><label>用户手机：'+row.phone+'</label></div>');
        content.push('<div class="form-group"><label>推荐人名称：'+(row.parentUserNickName?row.parentUserNickName:"")+'</label></div>');
        content.push('<div class="form-group"><label>推荐人手机：</label>');
        content.push('<div><input type="number" class="form-control" id="updateParentUserPhone" value="'+(row.parentUserPhone?row.parentUserPhone:"")+'" /></div>');
        content.push('</div>');
        content.push('</form>');
        layer.open({
          title:"修改用户推荐人",
          content: content.join(''),
          yes: function(index, layero){
            var params = {userId:id, parentPhone:$("#updateParentUserPhone").val()};
            $.putPayload(ctx + "/admin/user/parent", params, function (rs) {
              if (rs.success) {
                layer.msg("操作成功", {icon: 1});
                pi.search();
              } else {
                layer.alert(rs.message, {icon: 2});
              }
            });
          }
        });
      }
    }
  },
  obj: $("#table_list"),
  init: function () {
    tableList.obj.bootstrapTable({
      url: ctx + "/admin/user/manager/listpage",
      //数据列
      columns: [{
        checkbox:true
      },{
        title: "头像",
        field: "wxImg",
        align:"center",
        formatter: function (value, row, index) {
          var url = "/admin/img/avatar.jpg";
          if (row.avatar) {
            url = row.avatar;
          } else if (row.wxImg) {
            url = row.wxImg;
          }
          return '<img class="img-circle" id="avatar" style="width:30px" src="' + url + '"/>'
        }
      },{
        title: "昵称",
        field: "userNickName"
      }, {
        title: "微信名",
        field: "wxName"
      }, {
        title:"性别",
        field:"gender",
        formatter:function (value) {
          if(value === 1){
            return "男";
          }else if(value === 2){
            return "女";
          } else {
            return "未填写";
          }
        }
      }, {
        title: "地区",
        field: "registRegion"
      }, {
        title: "手机号",
        field: "phone"
      }, {
        title: "注册时间",
        field: "registTime"
      },{
        title:"推荐人",
        field:"parentUserNickName"
      },{
        title:"推荐人手机号",
        field:"parentUserPhone",
        formatter:function (value, row) {
          return "<button class='btn btn-xs btn-info m-r-xs' title='修改' onclick=\"tableList.updateParent('"+row.id+"')\"><i class='fa fa-edit'></i></button>"
            + (value ? value :"");
        }
      },{
        title:"等级",
        field:"level",
        formatter:function (value, row) {
          return "<button class='btn btn-xs btn-info m-r-xs' title='修改' onclick=\"tableList.updateLevel('"+row.id+"')\"><i class='fa fa-edit'></i></button>"
          + value;
        }
      },{
        title:"积分",
        field:"integral"
      },{
        title:"余额",
        field:"balance"
      },{
        title:"收益余额",
        field:"distribution"
      },{
        title:"备注",
        field:"remark",
        visible:false
      }/*, {
        title: "操作",
        field: "deal",
        formatter: function (value, row, index) {
          return '<button onclick="pi.detail(\'' + row.id + '\')" type="button" class="btn btn-success btn-xs"><i class="fa fa-search"></i>查看详情</button>';
        }
      }*/]
    });
  }
};

$(function () {
  window.onresize = function () {
    $("#detailModal").css("padding", "0px");
  };
  $.defaultLayDate('#registTimeStart','#registTimeEnd','datetime');
  tableList.init({});

});