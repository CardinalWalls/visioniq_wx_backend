var community = {
  add: function () {
    //community.modalEl.find("form [name=id]").val("");
    //community.modalEl.find("form input.form-control").val("");
    community.modalEl.find("form")[0].reset();
    community.modalEl.find(".save-btn").show();
    citySelect.init([{select:"#regionId", value:"500112"}]);
    community.modal("新增");
  },
  edit:function(read){
    var selectedRow = community.getSelectedRow();
    if (selectedRow) {
      community.modalEl.find('form .form-control').each(function () {
        var $this = $(this);
        var v = selectedRow[$this.attr("name")];
        if(v !== undefined){
          $this.val(v + "");
        }
      });
      tencentMap.mapPositioning();
      citySelect.init([{select:"#regionId", value:selectedRow.regionId}]);
      $("#expertId").selectPageClear().val(selectedRow.expertId).selectPageRefresh();
      if(read){
        community.modalEl.find(".save-btn").hide();
        community.modal("查看");
      }else{
        community.modalEl.find(".save-btn").show();
        community.modal("修改");
      }
    }
  },
  modal: function (title) {
    if (title) {
      community.modalEl.find(".modal-title").html(title);
      community.modalEl.modal();
    } else {
      community.modalEl.modal("hide");
    }
  },
  getSelectedRow: function () {
    var selectedAll = community.tableEl.bootstrapTable("getSelections");
    var selectedRow = null;
    if (selectedAll.length > 0) {
      selectedRow = selectedAll[0];
      return selectedRow;
    }
    layer.msg("请选择一行数据", {icon: 2});
    return null;
  },
  refresh: function () {
    community.tableEl.bootstrapTable('refresh');
  },
  save: function () {
    var f = community.modalEl.find('form');
    if (!f.valid()) {
      return;
    }
    layer.load(1, {shade: 0.3});
    var params = f.serializeJSON();
    $.postPayload(ctx + "/admin/region/community/save", params, function (rs) {
      layer.closeAll();
      if (rs.success) {
        community.modal(null);
        layer.msg(rs.message, {icon: 1});
        community.refresh();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    });
  },
  // del: function () {
  //   var selectedRow = community.getSelectedRow();
  //   if (selectedRow) {
  //     layer.confirm("确定删除该数据？", {
  //       btn: ['确定', '取消'], shade: 0.3
  //     }, function () {
  //       layer.load(1, {shade: 0.3});
  //       $.deletePayload(ctx + "/admin/region/community/del", {id: selectedRow.id}, function (rs) {
  //         layer.closeAll();
  //         if (rs.success) {
  //           layer.msg(rs.message, {icon: 1});
  //           community.refresh();
  //         } else {
  //           layer.alert(rs.message, {icon: 2});
  //         }
  //       });
  //     });
  //   }
  // },
  tableEl: $("#community_table_list"),
  modalEl: $("#community_modal"),
  initTable: function () {
    community.tableEl.bootstrapTable({
      url: ctx + "/admin/region/community/page",
      columns: [{
          checkbox: true
        },
        {
          title: "社区名称",
          field: "name"
        },
        {
          title: "街道名称",
          field: "streetName"
        },
        {
          title: "地区",
          field: "regionName"
        },
        {
          title: "专家",
          field: "expertName",
          formatter: function (value, row, index) {
            var v = value;
            if(row.expertHospital){
              v += " | " + row.expertHospital;
            }
            if(row.expertDepartment){
              v += " | " + row.expertDepartment;
            }
            return v;
          }
        },
        {
          title: "详细地址",
          field: "address"
        },
        {
          title: "排序",
          field: "sortNo"
        },
        {
          title: "状态",
          field: "valid",
          formatter: function (value, row, index) {
            return value ? "有效" : "<span class='text-danger'>无效</span>";
          }
        },
        {
          title: "创建时间",
          field: "createTime"
        },
        {
          title: "修改时间",
          field: "updateTime"
        }
      ]
    });
  }
};
$(function () {
  var opt = {
    searchField:"searchKey",
    keyField : 'id',
    multiple: false,
    eAjaxSuccess : function(res){
      return {
        list:res.list,
        totalRow:res.total
      };
    }
  };
  $("#expertId").each(function (){
    var $this = $(this);
    $this.selectPage($.extend({}, opt, {
      data: ctx+"/admin/user/expert/page",
      formatItem:function(data){
        var v = data.name;
        if(data.hospital){
          v += " | " + data.hospital;
        }
        if(data.department){
          v += " | " + data.department;
        }
        return v;
      }
    }));
  });
  community.initTable();
  tencentMap.resetZoom = 13;
  tencentMap.detailZoom = 16;
  tencentMap.queryGeo = true;
  tencentMap.geoCompleteEvent = function (geo) {
    if(geo && geo.addressComponents){
      var $address = $("#address");
      var buildAddress = tencentMap.buildAddress(geo);
      console.log(geo);
      var v = $address.val(buildAddress);
      if(v.length === 0){
        $address.val(buildAddress);
      }
      $.get("/api/region/by-full-name", {"province":geo.addressComponents.province, "city":geo.addressComponents.city, "district":geo.addressComponents.district},function (re) {
        if(re && re.regionId){
          citySelect.selectVal("#regionId", re.regionId);
        }
      },"json");
    }
  };
  tencentMap.loadScript();
});