/*
 * Copyright (c) 2017.  mj.he800.com Inc. All rights reserved.
 */
var tp = {
  openModal:function (type) {
    attachment.clear("imgJson");
    $("#typeModal").modal({
      backdrop: "static"
    });
    $('#type_form').find("input").val("");
    $('#type_form').find("select").val("");
    if(type == 'edit'){
      var selected = tableList.obj.bootstrapTable("getSelections");
      if(selected.length == 1){
        $('#name').val(selected[0].name);
        $('#remark').val(selected[0].remark);
        $('#id').val(selected[0].id);
        $('#sortNo').val(selected[0].sortNo);
        $('#status').val(selected[0].status);
        $('#typeCode').val(selected[0].typeCode);
        $('#isOrdinary').val(selected[0].isOrdinary);
        $('#groupName').val(selected[0].groupName);
        $('#targetLink').val(selected[0].targetLink);
        attachment.setArray("imgJson",JSON.parse(selected[0].imgJson));
      }
    }

  },
  delete:function () {
    layer.confirm("确定要删除该分类吗？", {
      btn: ['确定', '取消'],
      shade: 0.3
    }, function () {
      var selected = tableList.obj.bootstrapTable("getSelections");
      var l = layer.load(1, {shade: 0.3});
      $.post(ctx + "/admin/news/type/delete", {id: selected[0].id}, function (rs) {
        layer.close(l);
        if (rs.success) {
          layer.msg(rs.message, {icon: 1});
          location.reload();
        } else {
          layer.alert(rs.message, {icon: 2});
        }
      }, "json");
    });
  },
  search:function () {
    tableList.obj.bootstrapTable("refresh");
    tableList.init();
  },
  typeSave:function () {
    if (!$("#type_form").valid()) {
      return;
    }
    var typeName = $("#name").val();
    var remark = $("#remark").val();
    var id = $("#id").val();
    var _org_select_ = $("#_org_select_").val();

    // var l = layer.load(1, {shade: 0.3});
    $.post(ctx + "/admin/news/type", {
      id: id,
      name: typeName,
      _org_select_: _org_select_,
      sortNo: $("#sortNo").val(),
      status: $("#status").val(),
      typeCode: $("#typeCode").val(),
      isOrdinary: $("#isOrdinary").val(),
      imgJson: $("#imgJson").val(),
      remark: remark,
      groupName:$("#groupName").val(),
      targetLink:$("#targetLink").val(),
    }, function (rs) {
      // layer.close(l);
      if (rs.success) {
        layer.msg(rs.message, {icon: 1});
        $("#typeModal").modal("hide");
        tp.search();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    }, "json");
  }

};
function hideCol() {
  tableList.obj.bootstrapTable('hideColumn', 'id');
}
var tableList = {
  obj: $("#table_list"),
  init: function (searchArgs) {
    tableList.obj.bootstrapTable({
      url: ctx + "/admin/news/type",
      onLoadSuccess: function (data) {
        hideCol();
      },
      //数据列
      columns: [{
        checkbox: true
      }, {
        title: "id",
        field: "id"
      },  {
        title: "序号",
        field: "sortNo"
      }, {
        title: "分类名称",
        field: "name"
      }, {
        title: "编码",
        field: "typeCode"
      },{
        title: "分组名称",
        field: "groupName"
      },{
        title: "图片",
        field: "imgJson",
        formatter: function (value, row) {
          if(value&&value!=''){
            var array = JSON.parse(value);
            var html = "";
            for (var i = 0; i < array.length; i++) {
              html += attachment.toViewHtml(array[i].url) + " ";
            }
            return html;
          }
        }
      },{
        title: "是否展示在主列表里面",
        field: "isOrdinary",
        formatter: function (value, row) {
          if(value==0){
            return "否";
          }
          if(value==1){
            return "是";
          }
        }
      }, {
        title: "状态",
        field: "status",
        formatter: function (value, row) {
          if(value==0){
            return "禁用";
          }
          if(value==1){
            return "启用";
          }
        }
      },{
        title: "备注",
        field: "remark"
      }]
    });
  }
};

$(function () {
  tableList.init({});
});