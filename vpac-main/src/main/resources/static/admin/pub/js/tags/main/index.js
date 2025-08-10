var tags = {
  add: function () {
    attachment.clear("imgJson");
    tags.modalEl.find("form [name=id]").val("");
    tags.modal("新增");
  },
  edit:function(){
    attachment.clear("imgJson");
    var selectedRow = tags.getSelectedRow();
    if (selectedRow) {
        tags.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        $this.val(selectedRow[$this.attr("name")]);
      });
      $("#typeIdForTags").val(selectedRow["typeId"]).selectPageClear();
      $("#typeIdForTags").val(selectedRow["typeId"]).selectPageRefresh();
      attachment.setArray("imgJson",JSON.parse(selectedRow.imgJson));
      tags.modal("修改");
    }
  },
  modal: function (title) {
    if (title) {
      tags.modalEl.find(".modal-title").html(title);
      tags.modalEl.modal();
    } else {
      tags.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = tags.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.alert("请选择一行数据", {icon: 1});
    return null;
  },
  refresh: function () {
    tags.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = tags.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    var l = layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/tags/main/save", params, function (rs) {
      layer.close(l);
      if (rs.success) {
        tags.modal(null);
        layer.msg(rs.message, {icon: 1});
        tags.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  del: function () {
    var selectedRow = tags.getSelectedRow();
    if (selectedRow) {
      var i0 = layer.confirm("确定删除该数据？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        var i1 = layer.load(1, {shade: 0.3});
        $.deletePayload(ctx + "/admin/tags/main/del", {id: selectedRow.id}, function (rs) {
          layer.close(i0);
          layer.close(i1);
          if (rs.success) {
            layer.msg(rs.message, {icon: 1});
            tags.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    }
  },
  tableEl: $("#tags_table_list"),
  modalEl: $("#tags_modal"),
  initTable: function () {
    tags.tableEl.bootstrapTable({
      url: ctx + "/admin/tags/main/page",
      columns: [{
        checkbox: true
      },
        {
          title: "排序号",
          field: "sortNo"
        },
      {
        title: "标签名",
        field: "tagName"
      },
        {
          title: "标签编码",
          field: "tagCode"
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
        },
      {
        title: "类型",
        field: "typeName"
      },
        {
          title: "类型编码",
          field: "typeCode"
        },
        {
          title: "创建时间",
          field: "createTime"
        },
        {
          title: "状态",
          field: "status",
          formatter: function (value, row, index) {
            return value === 0 ? '禁用' : '启用'
          }
        },
      ]
    });
  }
};
$(function () {
  tags.initTable();
  $(".layer-date").each(function(){
    laydate.render({elem:this, type:'datetime'});
  });
  $.selectPageInit(false, $('#typeId,#typeIdForTags'), '/admin/tags/tagsType/simplePage');
});