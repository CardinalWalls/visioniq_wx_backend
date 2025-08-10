var tp = {
  openModal:function (type) {
    $("#tagModal").modal({
      backdrop: "static"
    });
    document.tag_form.reset();
    attachment.getAtta("logo").imgclear();
    attachment.getAtta("logo").emptyAtta();
    $('#tag_form #id').val('');
    if(type == 'edit'){
      var selected = tableList.obj.bootstrapTable("getSelections");
      if(selected.length == 1){
        for (var i in selected[0]) {
          var input = $('#tag_form').find("#" + i);
          input.val(selected[0][i]);
        }
        // useCondition
        $('#tag_form #useCondition').val(selected[0].useCondition ? 'true' : 'false')
        // 填充logo
        attachment.getAtta("logo").setImg(selected[0].img);
      } else {
        layer.msg("请选择一行", {time: 1000});
        $("#tagModal").modal('hide');
      }
    }

  },
  search:function () {
    tableList.obj.bootstrapTable("refresh");
  },
  typeSave:function () {
    if (!$("#tag_form").valid()) {
      return;
    }
    var data = $("#tag_form").serializeJSON();
    data.logoAtta = JSON.stringify(attachment.getAtta('logo').getAttaParams());
    var l = layer.load(1, {shade: 0.3});
    $.post(ctx + "/admin/tag/save",
        data,
        function (rs) {
          layer.close(l);
          if (rs.success) {
            layer.msg(rs.message, {icon: 1});
            $("#tagModal").modal("hide");
            tp.search();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        }, "json"
    );
  }
};

var tableList = {
  obj: $("#table_list"),
  init: function () {
    tableList.obj.bootstrapTable({
      url: ctx + "/admin/tag/page",
      idField: "id",
      //数据列
      columns: [{
        checkbox: true
      // }, {
      //   title: "图片",
      //   field: "img",
      //   formatter: function (value, row, index) {
      //     if(value){
      //       return '<img class="img-rounded" style="width:30px;max-height:30px" src="'+attachment.ossurl + value +'"';
      //     }
      //     return "";
      //   }
      }, {
        title: "ID",
        field: "id"
      }, {
        title: "标签名称",
        field: "tagName"
      }, {
        title: "分类",
        field: "groupName"
      }, {
        title: "是否用于查询条件",
        field: "useCondition",
        formatter: function (value, row, index) {
          if(value){
            return "是";
          } else {
            return "否";
          }
        }
      }]
    });
  }
};

$(function () {
  tableList.init();
  // attachment.regist('logo','img');
});