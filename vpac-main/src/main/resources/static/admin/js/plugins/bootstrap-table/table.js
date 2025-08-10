// 窗口改变时动态改变高度
var tableHeight = 0;
getTableHeight();

// 获取窗口高度
function getTableHeight() {
  if ($(".table-search") && !$(".table-search").is(':hidden')) {
    tableHeight = $(window).height() - $(".table-search").height() - 75;
  } else {
    tableHeight = $(window).height() - 75;
  }
}

// 监听窗口变化
$(window).resize(function () {
  changeHeight();
});

function changeHeight() {
  getTableHeight();
  var options = tableList.obj.bootstrapTable('getOptions');
  if (options.fixedHeight) {
    tableList.obj.bootstrapTable('resetView', {
      height: tableHeight
    });
  }
}

// 获取默认配置
function getDefaultOptions(searchArgs) {
  return {
    method: "GET",
    responseHandler: function (res) {
      return {
        "rows": res.list,
        "total": res.total
      };
    },
    onLoadSuccess: function () {
      tableList.obj.bootstrapTable("uncheckAll");
      addSearchBtn()
    },
    queryParams: function (params) {
      var newParams = {
        pageSize: params.pageSize,
        pageNum: params.pageNum,
        sortName: params.sortName,
        sortOrder: params.sortOrder
      };
      for (var key in searchArgs) {
        newParams[key] = searchArgs[key]
      }
      return newParams;
    },
    pageSize: 15,
    checkboxHeader: false,
    clickToSelect: true,
    singleSelect: true,
    idField: "id",
    // 快捷工具
    sortName: "id",
    sortOrder: "asc",
    toolbar: '#toolbar',
    showColumns: true,
    showRefresh: true,
    showToggle: true,
    showExport: true,
    showSearch: true,
    fixedHeight: true,
    exportDataType: 'basic',
    exportTypes: ['json', 'xml', 'csv', 'txt', 'sql', 'doc', 'excel'],
    height: tableHeight,
  };
}

// 表格配置
function tableConfig(tableId, options) {
  var tableList = {
    obj: $("#" + tableId),
    init: function (searchArgs) {
      tableList.obj.bootstrapTable('destroy');
      var defaultOptions = getDefaultOptions(searchArgs);
      for (var obj in options) {
        defaultOptions[obj] = options[obj];
      }
      tableList.obj.bootstrapTable(defaultOptions);
    }
  };
  return tableList;
}

// toolbar添加查询按钮
function addSearchBtn() {
  var toolbar = $(".fixed-table-toolbar");
  var options = tableList.obj.bootstrapTable('getOptions');
  if (options.showSearch) {
    if (toolbar) {
      var columns = toolbar.find(".columns");
      if (columns) {
        var btnGroup = columns.eq(0);
        if (btnGroup) {
          var searchBtn = $(".btn-default[name='showSearch']");
          if (searchBtn.length == 0) {
            btnGroup.prepend(
              "<button class=\"btn btn-default\" type=\"button\" name=\"showSearch\" onclick=\"searchBtnClick()\" aria-label=\"showSearch\" title=\"搜索\"><i class=\"glyphicon glyphicon-search icon-search\"></i></button>"
            );
          }
          var fixedHeightBtn = $(".btn-default[name='fixedHeightBtn']");
          if (fixedHeightBtn.length == 0) {
            btnGroup.append(
              "<button class=\"btn btn-default\" type=\"button\" name=\"fixedHeightBtn\" onclick='fixedHeightBtnClick()' aria-label=\"fixedHeightBtn\" title=\"固定表格高度\"><i class=\"glyphicon glyphicon-pushpin\"></i></button>"
            );
          }
        }
      }
    }
  }
}

// toolbar查询按钮点击事件
function searchBtnClick() {
  $('#table-search').slideToggle(200);
  setTimeout(changeHeight, 210);
}

// 是否固定表格高度
function fixedHeightBtnClick() {
  var options = tableList.obj.bootstrapTable('getOptions');
  if (options.fixedHeight) {
    tableList.obj.bootstrapTable('refreshOptions', {height: 0, fixedHeight: false})
  } else {
    tableList.obj.bootstrapTable('refreshOptions', {height: tableHeight, fixedHeight: true})
  }
}

