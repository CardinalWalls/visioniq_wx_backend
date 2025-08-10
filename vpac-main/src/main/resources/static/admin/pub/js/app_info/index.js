

var ap = {
  memberType: null, //表单类别:0 = dept, 1 = person
  selected: null, //选中节点
  isUpdate:false,
  initTree: function (param) { //初始化tree
    ap.tree = $("#app_tree").fancytree({
      extensions: ["dnd", "glyph", "clones"],
      source: {
        async: true,
        url: ctx + "/admin/appInfo/tree",
        data: param
      },
      lazyLoad: function (event, data) {
        ap.treeParam["nodeId"] = data.node.refKey;
        data.result = {
          url: ctx + "/admin/appInfo/tree",
          data: ap.treeParam
        };
      },
      click: function (event, data) {
        data.node.setActive(true);
        ap.selected = data.node;
        ap.clickNode(data.node);
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
            ap.doMove(node, data.otherNode, data.hitMode);
          } else {
            layer.alert("目标节点不能为根节点！", {icon:2});
          }
        }
      }
    });
    ap.tree.fancytree("getTree").setFocus(true);
    $("#app_tree ul").css({"max-height": window.innerHeight * 4/5});
  },
  doMove: function (target, src, hitMode) {
    if (ap.checkMove(target, src, hitMode)) {
      layer.confirm("确定要将 [" + src.title + "] 移动到 [" + target.title + "] " + (hitMode == "over" ? "里面" : (hitMode == "after" ? "后面" : "前面")) + " 吗？", {
        btn: ['确定','取消'],
        shade: 0.3
      }, function(){
        var l = layer.load(1,{shade:0.3});
        $.post(ctx + "/admin/appInfo/doMove", {
          srcId: src.refKey,
          targetId: target.refKey,
          hitMode: hitMode
        }, function (rs) {
          layer.close(l);
          if (rs.success) {
            src.moveTo(target, hitMode);
            layer.msg(rs.message, {icon:1});
          } else {
            layer.alert(rs.message, {icon:2});
          }
        }, "json");
      });
    }
  },
  checkMove:function(target, src, hitMode){
    var err = "不能将 [" + src.title + "] 移动到 [" + target.title + "] ！";
    var check = true;
    if("over" === hitMode){
      if(target.data.type === 2 || (target.data.type !== 1 && src.data.type === 2)){
        check = false;
      }
    }else{
      if((target.data.type !== 2 && src.data.type === 2) || (target.data.type === 2 && src.data.type !== 2)){
        check = false;
      }
    }
    if(!check){
      layer.alert(err, {icon:2});
    }
    return check;
  },
  clickNode: function (node) { //点击节点
    if (node) {
      ap.memberType = node.data.memberType;
      ap.isUpdate = true;
      ap.updateForm(node.data);
    }
  },
  updateForm: function (json) { //更新表单
    if (json) {
      $('#app_form').find("input").val("");
      $("#authUrlPrefixDiv").empty();
      for (var i in json) {
        var input = $('#app_form').find("#"+i);
        if (i === "status") {
          input[0].checked = (json[i] === 1);
        }
        else if(i === 'type'){
          ap.type(json[i]);
        }else {
          if (i === 'authUrlPrefix'){
            ap.buildAuthDiv(json[i]);
          } else {
            input.val(json[i]);
          }
        }
      }

      ap.setDisable(['add_dir_btn', 'add_app_btn'], json['type'] === 2);
      ap.setDisable(['add_op_btn', 'targetUrl'], json['type'] !== 1);
      ap.setDisable(['addAuthItemBtn'], json['type'] === 0);
      ap.setDisable(['sub_btn'], json.parentId === '');
    }
  },
  formSubmit: function () { //提交表单
    if (!ap.selected) {
      layer.alert("请填选择一个节点!", {icon:2});
      return;
    }
    if(!$("#name").val()){
      layer.alert("请填写名称!", {icon:2});
      return;
    }
    var type = $("#type").val();
    var authPath = null;
    if (type !== '0') {
      authPath = ap.checkAuthPath();
      if(!authPath){
        layer.alert("未设置授权路径!", {icon:2});
        return;
      }
      if(type === '1' && !$("#targetUrl").val()){
        layer.alert("未设置跳转路径!", {icon:2});
        return;
      }
    }
    var action = ctx + "/admin/appInfo/saveOrUpdate";
    var status = $("#status")[0].checked;
    var postData = $("#app_form").serializeJSON();
    postData.authUrlPrefix = authPath;
    postData.status= status?1:0;
    if(ap.isUpdate){
      postData.id=$("#id").val();
    }else{
      if(ap.selected.isLazy()){
        ap.selected.load(true);
      }
    }
    var l = layer.load(1,{shade:0.3});
    $.post(action, postData, function (rs) {
      layer.close(l);
      if (rs.success) {
        if (ap.isUpdate) {
          ap.selected.setTitle(rs.data.data.name);
          ap.selected.extraClasses = status ? ap.selected.extraClasses.replace("ui-state-disabled", "")
                                            : ap.selected.extraClasses + " ui-state-disabled";
          ap.selected.data = $.extend(ap.selected.data, rs.data.data);
          ap.selected.render();
        } else { //add
          ap.selected.addChildren(rs.data);
          ap.selected.setExpanded(true);
        }
        layer.alert(rs.message, {icon:1});
      } else {
        layer.alert(rs.message, {icon:2});
      }
    }, "json");
  },
  add: function (type) {
    ap.isUpdate = false;
    var t = "功能资源";
    if (ap.selected) {
      ap.setDisable(['sub_btn'], false);
      $('#app_form').find("input").val("").removeAttr("disabled");
      ap.memberType = type;
      $("#authUrlPrefixDiv").empty();
      if (type === 0) {
        t = "目录";
        ap.setDisable(['targetUrl', 'addAuthItemBtn'], true);
      } else if (type === 1) {
        t = "菜单页面";
        ap.addAuthItem();
        ap.setDisable(['targetUrl', 'addAuthItemBtn'], false);
      } else{
        $("#app_form input[name='iconClass']").val("fa fa-info-circle");
        ap.addAuthItem();
        ap.setDisable(['targetUrl'], true);
        ap.setDisable(['addAuthItemBtn'], false);
      }
      ap.type(type);
      $("#parentId").val(ap.selected.refKey);
      $("#id").val(ap.selected.title + " — 添加“" + t + "”");
    } else {
      layer.alert("请填选择一个节点!", {icon:2});
    }
  },
  del: function () {
    if (ap.selected) {
      if(!ap.selected.data.parentId){
        layer.alert("不能删除根节点!", {icon:2});
        return;
      }
      layer.confirm("确定要删除：" + ap.selected.title + "，及其所有子节点吗？", {
        btn: ['确定','取消'],
        shade: 0.3
      }, function(){
        var l = layer.load(1,{shade:0.3});
        $.post(ctx + "/admin/appInfo/delete", {id: ap.selected.refKey}, function (rs) {
          layer.close(l);
          if (rs.success) {
            var parent = ap.selected.parent;
            ap.selected.remove();
            $(parent.ul).trigger("click");
            layer.alert(rs.message, {icon:1});
          } else {
            layer.alert(rs.message, {icon:2});
          }
        }, "json");
      });
    } else {
      layer.alert("请填选择一个节点!", {icon:2});
    }
  },
  setDisable:function(ids, isDisable){
    var items = $("#"+ids.join(",#"));
    if(isDisable){
      items.attr("disabled", true);
    }else{
      items.removeAttr("disabled");
    }
  },
  openIcon:function selectIcon(obj) {
    $.selector.icons(function (value) {
      $(obj).val(value);
    });
  },
  buildAuthDiv:function(authStr){
    $("#authUrlPrefixDiv").empty();
    if(authStr){
      var arr = authStr.split(",");
      for(var i = 0 ; i < arr.length; i++){
        var s = arr[i];
        if(s.indexOf(":") >= 0){
          var split = s.split(":");
          ap.addAuthItem(split[0], split[1]);
        }else{
          ap.addAuthItem("*", s);
        }
      }
    }
  },
  addAuthItem:function(method, uri){
    var div = $("#authUrlPrefixDiv");
    div.append(
      "<div class='m-b-xs'><select class='form-control' style='width:20%'>" +
      "<option value='*' "+(method === '*'?"selected":"")+">AllMethod</option>" +
      "<option value='GET' "+(method === 'GET'?"selected":"")+">GET</option>" +
      "<option value='POST' "+(method === 'POST'?"selected":"")+">POST</option>" +
      "<option value='PUT' "+(method === 'PUT'?"selected":"")+">PUT</option>" +
      "<option value='DELETE' "+(method === 'DELETE'?"selected":"")+">DELETE</option>" +
      "</select>" +
      "<input style='width:70%' class='form-control' placeholder='授权URI' value='"+(uri?uri:"")+"' />" +
      "<button onclick='ap.delAuthItem(this)' class='btn btn-danger btn-xs'><i class='fa fa-remove'></i></button>" +
      "</div>"
    );
  },
  delAuthItem:function(e){
    $(e).parents("div").eq(0).remove();
  },
  type:function(t){
    $("#typeName").html(t===0?"<label class='label label-info'><i class='fa fa-folder-open-o'></i> 目录</label>"
      :(t===1?"<label class='label label-primary'><i class='fa fa-file-text-o'></i> 菜单页面</label>"
        :"<label class='label label-warning'><i class='fa fa-pencil'></i> 功能资源</label>"));
    $("#type").val(t);
  },
  checkAuthPath:function(){
    var path = [];
    $("#authUrlPrefixDiv div").each(function () {
      var div = $(this);
      path.push(div.find("select").val() + ":" + div.find("input").val());
    });
    return path.join(",");
  }
};
$(function () {
  ap.treeParam = {};
  ap.initTree(ap.treeParam);
});