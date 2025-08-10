/*
 * Copyright (c) 2017.  mj.he800.com Inc. All rights reserved.
 */
var ad = {
  addOrEdit: function (type) {
    attachment.clear("img");
    if (type == "edit") {
      var selectedRow = ad.getSelectedRow();
      if (selectedRow == null) {
        layer.alert("请选择一行", {icon: 1});
        return;
      }
      ad.openModal();
      if (selectedRow) {
        for (var i in selectedRow) {
          var input = $('#ad_form').find("#" + i);
          input.val(selectedRow[i]);
          if (i == 'groupId') {
            $("#groupId").val(selectedRow[i]);
            $("#groupId").trigger("chosen:updated");
          }
        }
        attachment.setSingle("img", selectedRow.img);
      }
      $('#ad_form').valid();
    }
    if (type == "add") {
      ad.openModal();
    }
  },
  updateUrls: function () {
    $("#urlsModal").modal('show');
  },
  urlsSave: function () {
    var oldUrl = $("#oldUrl").val();
    var newUrl = $("#newUrl").val();
    $.ajax({
      url: ctx + "/admin/operation/banner/info/urls",
      type: "PUT",
      data: {
        oldUrl: oldUrl,
        newUrl: newUrl,
        _org_select_ : $("#_org_select_").val()
      },
      dataType: "json",
      success: function (res) {
        if(res.success){
          $("#urlsModal").modal('hide');
          $("#oldUrl").val('');
          $("#newUrl").val('');
          tableList.tableEl.bootstrapTable('refresh');
        }else{
          layer.alert(res.message);
        }
      }
    })

  },
  openModal: function () {
    $("#adModal").modal({
      backdrop: "static"
    });
    $('#adModal').on('hidden.bs.modal', function () {
      //取消验证文字
      $('#ad_form').validate().resetForm();
    });
    $('#ad_form').find("input").val("");
    $("#width").val(0);
    $("#height").val(0);
  },
  getSelectedRow: function () {
    var selectedAll = tableList.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
    }
    return selectedRow;
  },
  search: function () {
    var searchArgs = $("#search_form").serializeJSON();
    tableList.init(searchArgs);
  },
  clearCondition: function () {
    document.searchForm.reset();
    ad.search();
  },
  dealSave: function () {
    if (!$("#ad_form").valid()) {
      return;
    }
    var l = layer.load(1, {shade: 0.3});
    var params = $("#ad_form").serializeJSON();
    params._org_select_ = $("#_org_select_").val();
    $.post(ctx + "/admin/operation/banner/info/saveOrUpdate", params, function (rs) {
      layer.close(l);
      if (rs.success) {
        layer.msg(rs.message, {icon: 1});
        $("#adModal").modal("hide");
        tableList.tableEl.bootstrapTable('refresh');
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    }, "json");
  },
  delete: function () {
    var selectedRow = ad.getSelectedRow();
    if (selectedRow == null) {
      layer.alert("请选择一行", {icon: 1});
      return;
    }
    var index = layer.confirm("确定要删除：" + selectedRow.descri + "吗？", {
      btn: ['确定', '取消'],
      shade: 0.3
    }, function () {
      var l = layer.load(1, {shade: 0.3});
      $.post(ctx + "/admin/operation/banner/info/delete", {id: selectedRow.id}, function (rs) {
        layer.close(l);
        layer.close(index);
        if (rs.success) {
          layer.msg(rs.message, {icon: 1});
          tableList.tableEl.bootstrapTable("refresh");
        } else {
          layer.alert(rs.message, {icon: 2});
        }
      }, "json");
    });
  },

  deleteAtta: function (target) {
    $(target).parent().remove();
  }

};

var tableList = {
  getSelectedRow: ad.getSelectedRow,
  refresh: function () {
    tableList.tableEl.bootstrapTable('refresh');
  },
  tableEl: $("#table_list"),
  init: function (searchArgs) {
    tableList.tableEl.bootstrapTable({
      url: ctx + "/admin/operation/banner/info/listpage",
      //数据列
      columns: [{
        checkbox: true
      }, {
        title: "分组",
        field: "groupName"
      }, {
        title: "广告说明",
        field: "descri"
      }, {
        title: "位置编码",
        field: "positionCode"
      }, {
        title: "标签",
        field: "tagName"
      }, {
        title: "图片",
        field: "img",
        formatter: function (value, row, index) {
          return attachment.toViewHtml(value);
        }
      }, {
        title: "尺寸",
        field: "size",
        formatter: function (value, row, index) {
          return row.width + "*" + row.height;
        }
      }, {
        title: "链接",
        field: "url",
        formatter: function (value, row, index) {
          return '<a target="_blank" href="' + value + '">' + value + '</a>';
        }
      }, {
        title: "点击数",
        field: "clickCount"
      }, {
        title: "创建时间",
        field: "createTime",
        sortable: true
      }, {
        title: "生效时间",
        field: "beginTime",
        formatter: function (value, row, index) {
          if (value) {
            return value.substring(0, 10);
          }
        }
      }, {
        title: "失效时间",
        field: "endTime",
        formatter: function (value, row, index) {
          if (value) {
            return value.substring(0, 10);
          }
        }
      }, {
        title: "备注",
        field: "remark"
      }, {
        title: "排序号",
        field: "orderNo"
      }, {
        title: "状态",
        field: "status",
        formatter: function (value, row, index) {
          switch (value) {
            case 0:
              return '禁用';
              break;
            case 1:
              return "启用";
              break;
          }
        }
      }]
    });
  }
};


function initPlugins() {
  $.defaultLayDate('#startDate','#endDate','datetime');
  $.defaultLayDate('#beginTime','#endTime','datetime');
}


$(function () {
  tableList.init({});
  initPlugins();

  changeType();
  $("#_org_select_").change(function () {
    changeType();
  });

  $("#groupId").change(function () {
    var typeCode = $(this).find("option:selected").attr("typeCode");
    $("#positionCode").val(typeCode);
  });

  $(".table-search-reset").click(function () {
    changeType();
    $("#tagsId").selectPageClear();
  });
  tagsUtil.initElement(tableList, "ADVERTISEMENT");
});

function changeType() {
  $.getJSON("/admin/operation/banner/group/list", {
    _org_select_: $("#_org_select_").val()
  }, function (re) {
    $("select#searchGroupId").empty();
    $("select#groupId").empty();
    $("select#searchGroupId").append("<option value=''>全部分组</option>");
    $("select#groupId").append("<option value=''>选择分组</option>");
    for (var i = 0; i < re.length; i++) {
      $("select#searchGroupId").append("<option value='" + re[i].id +
        "'>" + re[i].name + "</option>");
      $("select#groupId").append("<option typeCode='" + re[i].typeCode + "' value='" + re[i].id +
        "'>" + re[i].name + "</option>");
    }
  });
}