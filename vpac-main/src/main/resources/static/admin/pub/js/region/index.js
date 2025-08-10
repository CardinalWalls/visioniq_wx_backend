

var mb = {
  selected: null, //选中节点
  isUpdate: false,
  initTree: function (param) { //初始化tree
    mb.tree = $("#region_tree").fancytree({
      extensions: ["dnd", "glyph", "clones"],
      source: {
        async: true,
        url: ctx + "/admin/region/tree",
        data: param
      },
      lazyLoad: function (event, data) {
        mb.treeParam["nodeId"] = data.node.refKey;
        data.result = {
          url: ctx + "/admin/region/tree",
          data: mb.treeParam
        };
      },
      click: function (event, data) {
        data.node.setActive(true);
        mb.selected = data.node;
        mb.clickNode(data.node);
        regionTinyTable.obj.bootstrapTable('refresh', {query: {regionId:data.node.refKey}});
      },
      dnd: {
        autoExpandMS: 500,
        focusOnClick: true,
        preventVoidMoves: true, // Prevent dropping nodes 'before self', etc.
        preventRecursiveMoves: true, // Prevent dropping nodes on own descendants
        dragStart: function (node, data) {
          return !!node.data.parentId;
        },
        dragEnter: function (node, data) {
          return true;
        },
        dragOver: function (node, data) {
          if (node.isDescendantOf(data)) {
            return false;
          }
        },
        dragDrop: function (node, data) {
          if (node.data.parentId) {
            mb.doMove(node, data.otherNode, data.hitMode);
          } else {
            layer.alert("目标节点不能为根节点！", {icon:2});
          }
        }
      }
    });
    mb.tree.fancytree("getTree").setFocus(true);
    mb.tree.fancytree("getTree").visit(function(node){
      node.setExpanded();
    });
  },
  clickNode: function (node) { //点击节点
    if (node) {
      mb.isUpdate = true;
      mb.updateForm(node);
      $("#add_region_btn").removeAttrs("disabled");
    }
  },
  updateForm: function (node) { //更新表单
    if (node.data) {
      $('#region_form').find("input").val("");
      for (var i in node.data) {
        var input = $('#region_form').find("#" + i);
        if (i == "status") {
          $('#region_form #status')[0].checked = (node.data[i] == 0);
        } else {
          input.val(node.data[i]);
        }
      }
    }
    if (node.parent) {
      $("#parentName").val(node.parent.data.name);
    }

  },
  regionTinySubmit: function () { //提交表单
    if(!$("#region_tiny_form").valid()){
      return;
    }
    var action = ctx + "/admin/region/tiny/saveOrUpdate";
    var checked = $("#region_tiny_form #status")[0].checked;
    var postData = $("#region_tiny_form").serializeJSON();
    postData.status = checked ? 0 : 1;
    var l = layer.load(1, {shade: 0.3});
    $.post(action, postData, function (rs) {
      layer.close(l);
      if (rs.success) {
        mb.refreshTinyTable();
        $('#regionTinyModal').modal("hide");
        layer.alert(rs.message, {icon: 1});
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    }, "json");
  },
  regionSubmit: function () { //提交表单
    if(!$("#region_form").valid()){
      return;
    }
    var action = ctx + "/admin/region/saveOrUpdate";
    var checked = $("#region_form #status")[0].checked;
    var postData = $("#region_form").serialize() + "&status=" + (checked ? 0 : 1);
    if (mb.isUpdate) {
      postData += "&id=" + $("#region_form #id").val();
    }
    var l = layer.load(1, {shade: 0.3});
    $.post(action, postData, function (rs) {
      layer.close(l);
      if (rs.success) {
        layer.alert(rs.message, {icon: 1});
        location.reload();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    }, "json");
  },
  refreshTinyTable: function() {
    regionTinyTable.obj.bootstrapTable('refresh');
  },
  add_region: function (isRoot) {
    mb.isUpdate = false;
    if (isRoot) {
      $('#region_form').find("input").val("").removeAttr("disabled");
      $("#region_form #id").val("添加根地区");
    } else {
      if (mb.selected) {
        $('#region_form').find("input").val("").removeAttr("disabled");
        $("#parentId").val(mb.selected.refKey);
        $("#parentName").val(mb.selected.data.name);
        $("#region_form #id").val("添加节点地区");
      } else {
        layer.alert("请填选择一个节点!", {icon: 2});
      }
    }
  },
  addRegionTiny: function (type) {
      if (mb.selected) {
        if (mb.selected.data.left === 0) {
          layer.msg("请选择末级节点", {time: 1000});
          return;
        }
        document.region_tiny_form.reset();
        $('#region_tiny_form #status')[0].checked = false;
        if (type === 'edit') {
          var selected = regionTinyTable.obj.bootstrapTable("getSelections");
          for (var i in selected[0]) {
            var input = $('#region_tiny_form').find("#" + i);
            if (i == "status") {
              $('#region_tiny_form #status')[0].checked = (selected[0][i].status === 0);
            } else {
              input.val(selected[0][i]);
            }
          }
        }
        $("#regionId").val(mb.selected.refKey);
        $("#regionName").val(mb.selected.data.name);
        $('#regionTinyModal').modal({
          backdrop: "static"
        });
      } else {
        layer.alert("请填选择一个末级节点!", {icon: 2});
      }
  },
  delete_region: function () {
    if (mb.selected) {
      layer.confirm("确定要删除：" + mb.selected.title + "，及其所有子节点吗？", {
        btn: ['确定', '取消'],
        shade: 0.3
      }, function () {
        var l = layer.load(1, {shade: 0.3});
        $.post(ctx + "/admin/region/delete", {id: mb.selected.data.id}, function (rs) {
          layer.close(l);
          if (rs.success) {
            var parent = mb.selected.parent;
            mb.selected.remove();
            $(parent.ul).trigger("click");
            layer.alert(rs.message, {icon: 1});
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        }, "json");
      });
    } else {
      layer.alert("请填选择一个节点!", {icon: 2});
    }
  },
  deleteRegionTiny: function () {
    if (mb.selected) {
      layer.confirm("确定要删除吗？", {
        btn: ['确定', '取消'],
        shade: 0.3
      }, function () {
        var l = layer.load(1, {shade: 0.3});
        var selected = regionTinyTable.obj.bootstrapTable("getSelections");
        $.post(ctx + "/admin/region/tiny/delete", {id: selected[0].id}, function (rs) {
          layer.close(l);
          if (rs.success) {
            mb.refreshTinyTable();
            layer.alert(rs.message, {icon: 1});
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        }, "json");
      });
    } else {
      layer.alert("请填选择一个节点!", {icon: 2});
    }
  },
  doMove: function (target, src, hitMode) {
    if ("over" === hitMode && target.data.level >= src.data.level) {
      layer.alert("不能将 [" + src.title + "] 移动到 [" + target.title + "] 里面 ！", {icon:2});
    }
    else if("over" !== hitMode && target.data.level !== src.data.level){
      layer.alert("不能将 [" + src.title + "] 移动到 [" + target.title + "] "+(hitMode === "after" ? "后面" : "前面")+" ！", {icon:2});
    }else {
      layer.confirm("确定要将 [" + src.title + "] 移动到 [" + target.title + "] " + (hitMode === "over" ? "里面" : (hitMode === "after" ? "后面" : "前面")) + " 吗？", {
        btn: ['确定','取消'],
        shade: 0.3
      }, function(){
        var l = layer.load(1,{shade:0.3});
        $.post(ctx + "/admin/region/doMove", {
          srcId: src.refKey,
          targetId: target.refKey,
          hitMode: hitMode
        }, function (rs) {
          layer.close(l);
          if (rs.success) {
            src.moveTo(target, hitMode);
            layer.alert(rs.message, {icon:1});
          } else {
            layer.alert(rs.message, {icon:2});
          }
        }, "json");
      });
    }
  }
};
var regionTinyTable = {
  obj: $('#regionTinyTable'),
  init: function (searchArgs) {
    regionTinyTable.obj.bootstrapTable({
      method: "GET",
      url: ctx + "/admin/region/tiny/page",
      responseHandler: function (res) {
        return {
          "rows": res.list,
          "total": res.total
        };
      },
      onLoadSuccess: function () {
        regionTinyTable.obj.bootstrapTable("uncheckAll");
      },
      queryParams: function (params) {
        params.regionId = mb.selected ? mb.selected.refKey : "";
        return params;
      },
      pageSize: 5,
      checkboxHeader: false,
      clickToSelect: true,
      singleSelect: true,
      idField: "id",
      //数据列
      columns: [{
        checkbox: true
      }, {
        title: "名称",
        field: "name"
      }, {
        title: "备注",
        field: "remark"
      }, {
        title: "状态",
        field: "status",
        formatter: function (value, row, index) {
          if (value === 1){
            return '启用';
          } else {
            return '禁用';
          }
        }
      }]
    });
  }
}
$(function () {
  mb.treeParam = {};
  mb.initTree(mb.treeParam);
  regionTinyTable.init();
  $("#region_tree ul").height(window.innerHeight * 2 / 3);
});