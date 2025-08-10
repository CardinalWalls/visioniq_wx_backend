
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
        $this.val(selected[0][$this.attr("name")]);
      });
    } else {
      $("#code_form .form-control").each(function () {
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
      url: ctx + "/admin/dynamicform/form/data?code="+formCode,
      //数据列
      columns: [{
        checkbox: true
      }, {
        title: "联系电话",
        field: "phone"
      }, {
        title: "联系人",
        field: "userName"
      }, {
        title: "事故地点",
        field: "address"
      }, {
        title: "事故原因",
        field: "reason"
      }, {
        title: "时间",
        field: "createTime"
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