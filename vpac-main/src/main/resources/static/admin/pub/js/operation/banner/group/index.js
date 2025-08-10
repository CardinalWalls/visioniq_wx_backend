/*
 * Copyright (c) 2017.  mj.he800.com Inc. All rights reserved.
 */
var ad = {
  openModal: function (type) {
    if (type == "edit") {
      var selectedRow = ad.getSelectedRow();
      if (selectedRow == null) {
        layer.alert("请选择一行", {icon: 1});
        return;
      }
      $("#groupModal").modal({
        backdrop: "static"
      });
      $('#group_form').find("input").val("");
      if (selectedRow) {
        for (var i in selectedRow) {
          var input = $('#group_form').find("#" + i);
          input.val(selectedRow[i]);
        }
      }
      $('#group_form').valid();
    }
    if(type=="add"){
      $("#groupModal").modal({
        backdrop: "static"
      });
      $('#group_form').find("input").val("");
    }
  },
  getSelectedRow: function () {
    var selectedAll = tableList.obj.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
    }
    return selectedRow;
  },
  delete: function () {
    var selectedRow = ad.getSelectedRow();
    if (selectedRow == null) {
      layer.alert("请选择一行", {icon: 1});
      return;
    }
    var index = layer.confirm("确定要删除：" + selectedRow.name + "吗？", {
      btn: ['确定', '取消'],
      shade: 0.3
    }, function () {
      var l = layer.load(1, {shade: 0.3});
      $.post(ctx + "/admin/operation/banner/group/delete", {id: selectedRow.id}, function (rs) {
        layer.close(l);
        layer.close(index);
        if (rs.success) {
          layer.msg(rs.message, {icon: 1});
          tableList.obj.bootstrapTable("refresh");
        } else {
          layer.alert(rs.message, {icon: 2});
        }
      }, "json");
    });
  },
  groupSave: function () {
    if (!$("#group_form").valid()) {
      return;
    }
    var action = ctx + "/admin/operation/banner/group/saveOrUpdate";
    var postData = $("#group_form").serializeJSON();
    postData._org_select_ = $("#_org_select_").val();
    var l = layer.load(1, {shade: 0.3});
    $.post(action, postData, function (rs) {
      layer.close(l);
      if (rs.success) {
        $("#groupModal").modal("hide");
        tableList.obj.bootstrapTable("refresh");
        layer.alert(rs.message, {icon: 1});
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    }, "json");
  }

};

var tableList = {
  obj: $("#table_list"),
  init: function (searchArgs) {
    tableList.obj.bootstrapTable({
      url: ctx + "/admin/operation/banner/group/listpage",
      //数据列
      columns: [{
        checkbox: true
      },{
        title: "分类名称",
        field: "name",
        formatter: function (value, row) {
          if(row.status == 0){
            return '<s>'+ row.name +'</s>';
          }else{
            return row.name;
          }
        }
      }, {
        title: "编码",
        field: "typeCode"
      },
        {
        title: "创建时间",
        field: "createTime"
      },{
        title: "状态",
        field: "status",
        formatter: function (value, row, index) {
          return value === 0 ? '禁用' : '启用'
        }
      }]
    });
  }
};

$(function () {
  tableList.init({});
});