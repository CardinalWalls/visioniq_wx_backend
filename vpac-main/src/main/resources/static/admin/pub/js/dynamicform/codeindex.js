
var tp = {
  curId: '',
  search: function () {
    tableList.obj.bootstrapTable("refresh");
  },
  clearCondition: function () {
    document.searchForm.reset();
    tp.search();
  },
  openModal: function (type) {
    $("#codeModal").modal({
      backdrop: "static"
    });
    if(type == 'edit'){
      var selected = tableList.obj.bootstrapTable("getSelections");
      $("#code_form .form-control").each(function () {
        var $this = $(this);
        $this.val(selected[0][$this.attr("name")] + "");
      });
    } else {
      $("#code_form input.form-control").each(function () {
        var $this = $(this);
        $this.val('');
      });
    }
  },
  typeSave: function () {
    if (!$("#code_form").valid()) {
      return;
    }
    var l = layer.load(1, {shade: 0.3});
    var params = $("#code_form").serializeJSON();
    $.post(ctx + "/admin/dynamicform/code", params, function (rs) {
      layer.close(l);
      if (rs.success) {
        $("#codeModal").modal("hide");
        tp.search();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    }, "json");
  },
  showData: function (code) {

  }
};

var tableList = {
  obj: $("#table_list"),
  init: function (searchArgs) {
    tableList.obj.bootstrapTable({
      url: ctx + "/admin/dynamicform/code" ,
      //数据列
      columns: [{
        checkbox: true
      }, {
        title: "编号",
        field: "code"
      }, {
        title: "标题",
        field: "title"
      }, {
        title: "开始日期",
        field: "beginTime"
      }, {
        title: "截止日期",
        field: "endTime"
      }, {
        title: "状态",
        field: "status",
        formatter: function (value, row, index) {
          return value === 1 ? '启用' : '禁用';
        }
      }, {
        title: "使用验证码",
        field: "captcha",
        formatter: function (value, row, index) {
          return value ? '是' : '否';
        }
      }, {
        title: "需要登录",
        field: "userAuth",
        formatter: function (value, row, index) {
          return value ? '是' : '否';
        }
      }, {
        title: "是否通知微信管理员",
        field: "notifyWxAdmin",
        formatter: function (value, row, index) {
          return value ? '是' : '否';
        }
      }, {
        title: "消息通知到用户",
        field: "notifyWxUser",
        formatter: function (value, row, index) {
          if(value === 0){
            return "否"
          }
          return "<a href='"+(row.notifyWxUserLink?row.notifyWxUserLink:"#")+"' target='_blank'>"+(value === 1?"公众号":"小程序")+"</a>";
        }
      }, {
        title: "备注",
        field: "remark"
      }, {
        title: "已收集数量",
        field: "count",
        formatter: function (value, row, index) {
          return '<button class="btn btn-info btn-xs" type="button" onclick="parent.index.showNav(\'/admin/dynamicform/index?code='+row.code+'\', true)">'+value+'</button>';
        }
      }
      ]
    });
  }
};
function initPlugins() {
  $.defaultLayDate('#beginTime','#endTime','datetime');
}

$(function () {
  tableList.init({});
  initPlugins();
});