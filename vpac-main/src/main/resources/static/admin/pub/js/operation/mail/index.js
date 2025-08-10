var htmlMinify = require('html-minifier').minify;
$(function () {

  $("#table_list").bootstrapTable({
    url: ctx + "/admin/operation/mail/pages",
    //数据列
    columns: [{
      checkbox: true
    }, {
      title: "标题",
      field: "subject"
    }, {
      title: "数量",
      field: "count"
    }, {
      title: "创建时间",
      field: "createTime"
    }, {
      title: "状态",
      field: "status",
      formatter: function (value) {
        switch (value) {
          case 0 :
            return '未发送';
          case 1 :
            return '已发送';
        }
      }
    }, {
      title: "备注",
      width: "30%",
      field: "remark"
    }, {
      title: "操作",
      formatter: function (value, row, index) {
        var send = "<button class=\"btn btn-primary btn-xs\" onclick=\"sendMail('" + row.id + "')\"><i class='fa fa-send'></i>发送</button>";
        return "<button class=\"btn btn-success btn-xs\" onclick=\"viewContent('" + row.id + "')\">查看详情</button>&nbsp;&nbsp;" + send;
      }
    }]
  });
  $.defaultLayDate('#startDate','#endDate','date');
});

function refreshGrid() {
  $("#table_list").bootstrapTable("refresh");
}

function clearCondition() {
  document.form[0].reset();
  $("#table_list").bootstrapTable("refresh");
}

function viewContent(id) {
  $.get(ctx + "/admin/operation/mail/" + id, "", function (re) {
    if(re.success){
      $("#id").val(re.data.id);
      $("#subject").val(re.data.subject);
      $("#addressee").val(re.data.addressee);
      $("#remarks").val(re.data.remarks);
      keditor.html(re.data.content);
      $("#createMailModal").modal();
    }else{
      layer.alert(re.message, {icon: 2});
    }
  })
}

function createMail() {
  $("#mail_form")[0].reset();
  $("#createMailModal").modal();
}

function saveMail() {
  var l = layer.load(1, {shade: 0.3});
  var params = $("#mail_form").serializeJSON();
  params.editorValue = htmlMinify(keditor.html(), {
    collapseWhitespace: true, conservativeCollapse: true
  });
  $.post(ctx + "/admin/operation/mail", params, function (rs) {
    layer.close(l);
    if (rs.success) {
      refreshGrid();
      layer.msg(rs.message, {icon: 1});
      $("#createMailModal").modal("hide");
    } else {
      layer.alert(rs.message, {icon: 2});
    }
  }, "json");
}

function saveMailBox() {
  var l = layer.load(1, {shade: 0.3});
  $.post(ctx + "/admin/operation/mail/server", $("#mailbox_form").serialize(), function (rs) {
    layer.close(l);
    if (rs.success) {
      layer.msg(rs.message, {icon: 1});
      $("#mailBoxModal").modal("hide");
    } else {
      layer.alert(rs.message, {icon: 2});
    }
  }, "json");
}

function configServer() {
  $("#mailbox_form")[0].reset();

  $.get(ctx + "/admin/operation/mail/server", {}, function (re) {
    if (re.success) {
      if(re.data){
        $("#box_id").val(re.data.id);
        $("#address").val(re.data.address);
        $("#smtp").val(re.data.smtp);
        $("#password").val(re.data.password);
        $("#sign").val(re.data.sign);
      }
      $("#mailBoxModal").modal();
    } else {
      layer.alert(re.message, {icon: 2});
    }
  }, "json");
}

function sendMail(id) {
  var l = layer.load(1, {shade: 0.3});
  $.post(ctx + "/admin/operation/mail/send", {id: id}, function (rs) {
    layer.close(l);
    if (rs.success) {
      layer.msg("发送完成", {icon: 1});
    } else {
      layer.alert(rs.message, {icon: 2});
    }
  }, "json");

}