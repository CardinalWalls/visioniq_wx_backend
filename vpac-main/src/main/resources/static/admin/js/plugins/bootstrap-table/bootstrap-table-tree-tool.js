/**
 * 属性表结构工具类
 * 参考 bootstrap-table-init.js 构建表格；
 *
 * ------- 名词解释 -------
 * 懒加载: 查询下级节点
 * 业务查询参数：如 name、phone 等，业务参数查询时，会查询出满足条件的节点，并查询这些节点的所有父级，以构建完整的树形结构，懒加载时不会带上这些参数；
 * 全局查询参数：如 valid 等，这些参数会直接过滤每次的查询结果，包括懒加载的查询，使用 class="tree-base-arg" 来标记；
 *
 * 树形表格参数：
      //树形表格父级字段
      parentIdField: 'parentId'
      //树形表格层级显示字段
      treeShowField: 'name'
      //是否为懒加载
      treeLazyLoad: true
 *
 * 后台查询返回的列表中必要的字段：id、parentId、leftVal、rightVal、treeKind；可选字段：parentLeftVal、parentRightVal
 *
 **/
var tableTreeTool = {
  //上下移动按钮
  moveBtn : function (row, beforeClickStr, afterClickStr) {
    var s = "";
    if(beforeClickStr && (row.leftVal === undefined || row.parentLeftVal === undefined || row.leftVal - row.parentLeftVal > 1)){
      s += "<button class='btn btn-white btn-xs' title='上移' onclick=\""+beforeClickStr.replaceAll("\"", "'")+"\"><i class='fa fa-angle-up'></i></button>";
    }
    if(afterClickStr && (row.parentRightVal === undefined || row.rightVal === undefined || row.parentRightVal - row.rightVal > 1)){
      s += "<button class='btn btn-white btn-xs' title='下移' onclick=\""+afterClickStr.replaceAll("\"", "'")+"\"><i class='fa fa-angle-down'></i></button>";
    }
    return s === "" ? s : "<div class='tree-move-div'>"+s+"</div>";
  },
  //懒加载按钮
  expandBtn:function(tableEl, row){
    var btn = "";
    if(row.rightVal - row.leftVal > 1){
      row._hasChildren = true;
    }
    if(row._hasChildren){
      var table;
      if(tableEl.selector){
        table = '$(\''+tableEl.selector+'\')';
      }else{
        throw "tableEl is not a jQuery Element !";
      }
      //如果有子节点，说明不需要加载
      if(tableTreeTool.existsChildren(row.id, tableEl.bootstrapTable("getData"))){
        row._expand = true;
        row._lazyLoaded = true;
      }
      else{
        row._expand = false;
        row._lazyLoaded = false;
      }
      btn = ' <a class="expand-btn" onclick="tableTreeTool.expand('+table+', \''+row.id+'\')" ><i class="fa fa-chevron-'+(row._expand?"up":"down")+'"></i></a>';
    }
    return btn;
  },
  //上下移动成功之后
  moveUpAndDownSuccess: function (tableEl, id) {
    tableEl.bootstrapTable('uncheckAll');
    var row = tableEl.bootstrapTable('getRowByUniqueId', id);
    if(row && row.parentId !== undefined){
      var treeKind = row.treeKind;
      var parentId = row.parentId;
      var children = tableTreeTool.findAllChildrenIds(parentId, tableEl.bootstrapTable("getData"));
      for (var i = 0; i < children.length; i++) {
        tableEl.bootstrapTable('removeByUniqueId', children[i]);
      }
      tableTreeTool.lazyLoad(tableEl, parentId, treeKind, function () {
        tableEl.bootstrapTable("checkBy", {field:"id", values:id});
      });
    }else{
      tableEl.bootstrapTable('refresh');
    }
  },
  existsChildren:function(parentId, rows){
    for (var i = 0; i < rows.length; i++) {
      var row = rows[i];
      if(parentId === row.parentId){
        return true;
      }
    }
    return false;
  },
  findChildrenIds:function(parentId, rows, findArray){
    findArray = findArray === undefined ? [] : findArray;
    for (var i = 0; i < rows.length; i++) {
      var row = rows[i];
      if(parentId === row.parentId){
        findArray.push(row.id);
        if(row._hasChildren && row._expand){
          tableTreeTool.findChildrenIds(row.id, rows, findArray);
        }
      }
    }
    return findArray;
  },
  findAllChildrenIds:function(parentId, rows, findArray){
    findArray = findArray === undefined ? [] : findArray;
    for (var i = 0; i < rows.length; i++) {
      var row = rows[i];
      if(parentId === row.parentId){
        findArray.push(row.id);
        if(row._hasChildren){
          tableTreeTool.findAllChildrenIds(row.id, rows, findArray);
        }
      }
    }
    return findArray;
  },
  expand:function (tableEl, id) {
    if(tableTreeTool.loading){
      return;
    }
    var select = tableEl.bootstrapTable('getRowByUniqueId', id);
    if(!select){
      return;
    }
    tableTreeTool.loading = true;
    var doExpand = function (show) {
      $("tr[data-uniqueid='"+select.id+"'] .expand-btn").find("i").attr("class", show ? "fa fa-chevron-up":"fa fa-chevron-down");
      var children = tableTreeTool.findChildrenIds(select.id, tableEl.bootstrapTable("getData"));
      if(children.length > 0){
        for (var i = 0; i < children.length; i++) {
          var tr = $("tr[data-uniqueid='"+children[i]+"']");
          if(show){
            tr.show();
          }else{
            tr.hide();
          }
        }
      }
    };
    if(select._expand){
      select._expand = false;
      doExpand(false);
    }
    else{
      select._expand = true;
      //未加载
      if (!select._lazyLoaded) {
        tableTreeTool.lazyLoad(tableEl, select.id, select.treeKind, function () {
          select._lazyLoaded = true;
          $("tr[data-uniqueid='"+select.id+"'] .expand-btn").find("i").attr("class", "fa fa-chevron-up");
        });
      }
      //已加载
      else{
        doExpand(true);
      }
    }
    tableTreeTool.loading = false;
  },
  lazyLoad:function(tableEl, parentId, treeKind, callback){
    var param = {};
    var baseArg = tableEl.data("table-search-form").find(".tree-base-arg");
    if(baseArg){
      $.each(baseArg, function () {
        var el = $(this);
        param[el.attr("name")] = el.val();
      });
    }
    param.parentId = parentId;
    param.treeKind = treeKind;
    $.get(tableEl.data("bootstrap.table").options.url, param, function(rs){
      tableEl.bootstrapTable('append', rs.list);
      if(callback){
        callback();
      }
    },"json");
  }
};
(function () {
  $(function () {
    $("head").append("<style>" +
      ".tree-move-div{"+
        "float:right;"+
        "height:22px;"+
        "width:20px;"+
        "display:flex;"+
        "flex-direction:column;"+
        "justify-content:center;"+
      "}"+
      ".tree-move-div .btn{"+
        "height:11px;"+
        "flex-direction:column;"+
        "justify-content:center;"+
      "}</style>");
  });
})();