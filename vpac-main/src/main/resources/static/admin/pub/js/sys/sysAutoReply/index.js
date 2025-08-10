var sysAutoReply = {
  add: function () {
    //sysAutoReply.modalEl.find("form [name=id]").val("");
    //sysAutoReply.modalEl.find("form input.form-control").val("");
    sysAutoReply.modalEl.find("form")[0].reset();
    sysAutoReply.modalEl.find(".save-btn").show();
    sysAutoReply.modal("新增");
  },
  edit:function(read){
    var selectedRow = sysAutoReply.getSelectedRow();
    if (selectedRow) {
      sysAutoReply.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        var v = selectedRow[$this.attr("name")];
        if(v !== undefined){
          $this.val(v + "");
        }
      });
      if(read){
        sysAutoReply.modalEl.find(".save-btn").hide();
        sysAutoReply.modal("查看");
      }else{
        sysAutoReply.modalEl.find(".save-btn").show();
        sysAutoReply.modal("修改");
      }
    }
  },
  modal: function (title) {
    if (title) {
      sysAutoReply.modalEl.find(".modal-title").html(title);
      sysAutoReply.modalEl.modal();
    } else {
      sysAutoReply.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = sysAutoReply.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.msg("请选择一行数据", {icon: 2});
    return null;
  },
  refresh: function () {
    sysAutoReply.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = sysAutoReply.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/sys/sysAutoReply/save", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        sysAutoReply.modal(null);
        layer.msg(rs.message, {icon: 1});
        sysAutoReply.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  del: function () {
    var selectedRow = sysAutoReply.getSelectedRow();
    if (selectedRow) {
      layer.confirm("确定删除该数据？", {
        btn: ['确定', '取消'], shade: 0.3
      }, function () {
        layer.load(1, {shade: 0.3});
        $.deletePayload(ctx + "/admin/sys/sysAutoReply/del", {id: selectedRow.id}, function (rs) {
          layer.closeAll();
          if (rs.success) {
            layer.msg(rs.message, {icon: 1});
            sysAutoReply.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      });
    }
  },
  tableEl: $("#sysAutoReply_table_list"),
  modalEl: $("#sysAutoReply_modal"),
  initTable: function () {
    sysAutoReply.tableEl.bootstrapTable({
      url: ctx + "/admin/sys/sysAutoReply/page",
      columns: [{
          checkbox: true
        },
        {
          title: "分类",
          field: "type",
          formatter:function (value, row) {
            return types[value];
          }
        },
        {
          title: "关键词",
          field: "keyword"
        },
        {
          title: "匹配关键词时的最大文字长度",
          field: "matchLengthMax",
          align: "right"
        },
        {
          title: "回复内容",
          field: "content"
        },
        {
          title: "链接地址",
          field: "url"
        },
        {
          title: "排序",
          field: "sortNo",
          align: "right"
        },
        {
          title: "创建时间",
          field: "createTime"
        },
        {
          title: "修改时间",
          field: "updateTime"
        },
      ]
    });
  }
};
$(function () {
  sysAutoReply.initTable();
  $(".layer-date").each(function(){
    laydate.render({elem:this, type:'datetime'});
  });
});