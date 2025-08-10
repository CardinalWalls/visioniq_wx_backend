(function($){
  var BootstrapTable = $.fn.bootstrapTable.Constructor;
  var _initToolbar = BootstrapTable.prototype.initToolbar;
  BootstrapTable.prototype.initToolbar = function () {
    _initToolbar.apply(this, Array.prototype.slice.apply(arguments));
    toolbarInit(this);
    sortInit(this);
  };
  var _Constructor = $.fn.bootstrapTable;
  var customBootstrapTable = function () {
    var argArray = Array.prototype.slice.apply(arguments);
    if(argArray.length > 0 && $.type(argArray[0]) === "object" && !this.data("bootstrap.table") && argArray[0].columns){
      argArray[0] = extendOptions(argArray[0], this);
    }
    return _Constructor.apply(this, argArray);
  };
  customBootstrapTable.Constructor = BootstrapTable;
  customBootstrapTable.theme = _Constructor.theme;
  customBootstrapTable.VERSION = _Constructor.VERSION;
  customBootstrapTable.defaults = _Constructor.defaults;
  customBootstrapTable.columnDefaults = _Constructor.columnDefaults;
  customBootstrapTable.events = _Constructor.events;
  customBootstrapTable.locales = _Constructor.locales;
  customBootstrapTable.methods = _Constructor.methods;
  customBootstrapTable.utils = _Constructor.utils;
  // BOOTSTRAP TABLE INIT，见 bootstrap-table.js 最后
  $.fn.bootstrapTable = customBootstrapTable;

  var extendOptions = function(opt, table){
    opt = $.extend(true, {}, simpleTableDefault, opt);
    opt.queryParams = chainFunc(table, simpleTableDefault.queryParams, opt.queryParams);
    opt.onColumnSwitch = compositeFunc(table, simpleTableDefault.onColumnSwitch, opt.onColumnSwitch);
    //代理自定义的 responseHandler 方法
    var _responseHandler = opt.responseHandler;
    opt.responseHandler = function(res){
      return simpleTableDefault.responseHandlerProxy.call(table, res, _responseHandler)
    };

    bootstrapTableExtPreInit(table, opt);
    var multiSortCount = {count:0};
    countSortableColumns(opt.columns, multiSortCount);
    if(multiSortCount.count < 2){
      opt.showMultiSort = false;
    }

    fixedColumnsCheck(opt);
    reorderableColumnsAddClass(table, opt);
    optionColumnsCheck(opt, table);
    return opt;
  };
  var bootstrapTableExtPreInit = function(table, opt){
    var id = table.attr("id");
    var content = table.parents(".table-content:eq(0)");
    content.data("table-id", id);
    opt.toolbar = content.find(".table-toolbar");
    var searchEl = content.find(".table-search").on("keydown", function (event) {
      if(event.keyCode === 13){
        $("#"+id).bootstrapTable('refresh');
      }
    }).on("submit",function(){return false;});
    searchEl.find("select").on("change", function () {
      var notAutoRefresh = $(this).attr("notAutoRefresh");
      if(!notAutoRefresh){
        $("#" + id).bootstrapTable('refresh');
      }
    });
    table.data("table-search-form", searchEl);
    table.data("table-content", content);
    content.find(".table-search-submit").on("click",function(){
      $("#"+id).bootstrapTable('refresh');
    });
    //重置search form
    content.find(".table-search-reset").on("click",function(){
      var form = table.data("table-search-form");
      var orgSelected = form.find(".-org-select-hidden-");
      var orgSelectedId = orgSelected.val();
      form[0].reset();
      if(orgSelected.length > 0){
        try {
          orgSelected.val(orgSelectedId).orgRefresh();
        } catch (e) {}
      }
      //清理和更新 selectPage 插件的 input
      form.find("input.sp_hidden").each(function (){
        var $this = $(this);
        var initVal = $this.attr("initValue");
        if(initVal === undefined){
          $this.val("");
        }else{
          $this.val(initVal);
        }
        try {
          if ($this.val() === '') {
            $this.selectPageClear();
          } else {
            $this.selectPageRefresh();
          }
        } catch (e) {}
      });
      $("#"+id).bootstrapTable('refresh');
    });
    //初始化就默认调整表格高度（固定表头）
    if(opt.initFixedHeight){
      var height = getFixedHeight(searchEl.height());
      table.data('fixedHeight', 'true');
      opt["height"] = height;
    }
  };
  var countSortableColumns = function (columns, obj) {
    if(obj.count > 0){
      return;
    }
    for(var i = 0; i < columns.length; i++){
      var c = columns[i];
      if($.type(c) === "array"){
        countSortableColumns(c, obj);
      }else if(c.sortable){
        obj.count++;
      }
    }
  };
  var compositeFunc = function (jqEl, fun0, fun1) {
    return $.proxy(function (arg0, arg1, arg2, arg3, arg4, arg5) {
      $.proxy(fun0, jqEl)(arg0, arg1, arg2, arg3, arg4, arg5);
      if(fun0 !== fun1 && fun1){
        $.proxy(fun1, jqEl)(arg0, arg1, arg2, arg3, arg4, arg5);
      }
    }, jqEl);
  };
  var chainFunc = function (jqEl, fun0, fun1) {
    return $.proxy(function (data) {
      data = $.proxy(fun0, jqEl)(data);
      if(fun0 !== fun1 && fun1){
        data = $.proxy(fun1, jqEl)(data);
      }
      return data;
    }, jqEl);
  };
  var getFixedHeight = function (searchFromHeight) {
    return $("body").height() - (100 + searchFromHeight);
  };
  var toolbarInit = function (table) {
    var content = table.$el.data("table-content");
    var rightTool = content.find(".fixed-table-toolbar .columns.columns-right");
    if(rightTool.length > 0){
      if(table.options['miniToolbar']){
        rightTool.addClass("toolbar-hide");
      }
      if(rightTool.children(".tool-bar-fixed-height-btn").length === 0){
        $("<button class='btn btn-default tool-bar-fixed-height-btn' title='调整表格高度（固定表头）'><i class='fa fa-arrows-v'></i></button>")
          .appendTo(rightTool).on("click",function () {
          var height = getFixedHeight(table.$el.data("table-search-form").height());
          if(content.find(".bootstrap-table").height() > height){
            table.$el.data('fixedHeight', 'true');
            table.$el.bootstrapTable('refreshOptions', {height:height})
          }else if('true' === table.$el.data('fixedHeight')) {
            table.$el.removeData('fixedHeight');
            table.$el.bootstrapTable('refreshOptions', {height: 0})
          }
        });
      }
      if(rightTool.children(".tool-bar-config-btn").length === 0){
        $("<button class='btn btn-default tool-bar-config-btn' title='更多功能'><i class='fa fa-outdent'></i></button>")
          .appendTo(rightTool).on("click",function () {
          var p = $(this).parent();
          if(p.children(":first").is(":visible")){
            p.removeClass("toolbar-show");
            p.addClass("toolbar-hide");
          }else{
            p.removeClass("toolbar-hide");
            p.addClass("toolbar-show");
          }

        });
      }
      if(table.options['showColumns']){
        var ls;
        try {
          ls = localStorage;
        } catch (e) {}
        if(ls){
          var ul = rightTool.find(".keep-open>ul.dropdown-menu");
          if(ul.length === 1){
            var cacheKey = buildCacheKey(table.$el.attr("id"));
            var dcv = table.$el.data("_defaultColumnVisible_");
            var inputs = ul.find("input").on("click", function (){
              var f = this.getAttribute("data-field");
              if(f){
                var cache = getLocalStorageObj(ls, cacheKey);
                cache[f] = this.checked ? 1 : 0;
                ls.setItem(cacheKey, JSON.stringify(cache));
              }
            });
            var btn = $('<button class="btn btn-white btn-xs" style="margin:6px"><i class="fa fa-retweet"></i> 恢复默认列显示</button>').on("click", function (e){
              ls.removeItem(cacheKey);
              for (let i = 0; i < inputs.length; i++) {
                var input = inputs[i];
                var f = input.getAttribute("data-field");
                if(f){
                  var v = dcv[f] === 1;
                  input.checked = v;
                  table.$el.bootstrapTable(v ? 'showColumn':'hideColumn', f);
                }
              }
              e.stopImmediatePropagation();
            });

            ul.prepend($('<li class="dropdown-item-marker reset-columns text-center" role="menuitem"></li>').append(btn));
          }
        }
      }
      if(!table.options['showRightTool']){
        rightTool.hide();
      }
    }
  };
  var sortInit = function (table) {
    table.$container.off("click", ".th-inner").on("click", ".th-inner", function(event){
      if($(this).hasClass("sortable")){
        table.options.unSorted = "true";
        if(table.options.showMultiSort){
          table.$toolbar.find(".multi-sort").trigger("click");
        }
        else{
          if($(this).hasClass("desc")){
            $(this).removeClass("desc");
            table.options.sortName = null;
            table.options.sortOrder = null;
            table.refresh();
          }else{
            table.onSort(event);
          }
        }
      }
    });
    if(table.options.showMultiSort){
      if(table.options.sortPriority && table.options.sortPriority.length > 0){
        var sortMap = {};
        for(var i = 0 ; i < table.options.sortPriority.length; i++){
          sortMap[table.options.sortPriority[i].sortName] = table.options.sortPriority[i].orderBy;
          table.addLevel(i, table.options.sortPriority[i]);
        }
        table.$el.find("th>.sortable").each(function () {
          var $this = $(this);
          var order = sortMap[$this.parent().data("field")];
          if(order){
            $this.addClass(order);
          }
        });
      }
      $('#sortModal_' + table.$el.attr("id")).off('show.bs.modal').on('show.bs.modal', function () {
        var $this = $(this);
        if($this.data("init") !== "true"){
          if(table.options.sortPriority && table.options.sortPriority.length > 0){
            $this.find("tbody").empty();
            for(var i = 0 ; i < table.options.sortPriority.length; i++){
              table.addLevel(i, table.options.sortPriority[i]);
            }
            table.setButtonStates();
          }
          $this.data("init", "true");
        }
      });
    }else{
      table.$toolbar.find(".multi-sort").remove();
    }
  };
  var buildSortField = function (table) {
    var sortField = null;
    if(table){
      if(table.options.unSorted){
        sortField = "_NOT_SORT_";
      }
      if(table.options.showMultiSort){
        var array = table.options.sortPriority;
        if(array){
          var val = [];
          for(var i = 0; i < array.length; i++){
            val.push(array[i].sortName + (array[i].sortOrder?" " + array[i].sortOrder:""));
          }
          sortField = val.join(",");
        }
      }else{
        if(table.options.sortName){
          sortField = table.options.sortName + (table.options.sortOrder?" " + table.options.sortOrder:"");
        }
      }
    }
    return sortField;
  };
  var getLocalStorageObj = function(ls, key){
    var c = ls.getItem(key);
    if(c){
      try {
        return JSON.parse(c);
      } catch (e) {
      }
    }
    return {};
  };
  var columnsEach = function (obj, func){
    if ($.type(obj) === "array") {
      for (var i = 0; i < obj.length; i++) {
        columnsEach(obj[i], func);
      }
    }
    else{
      func(obj);
    }
  };
  var buildCacheKey = function (id){
    return location.pathname + "#" + id;
  }
  var optionColumnsCheck = function (opt, table){
    //自定义列显示
    var cache;
    try {
      cache = getLocalStorageObj(localStorage, buildCacheKey(table.attr("id")));
    } catch (e) {
      cache = {};
    }
    var dcv = {}; //默认可见的column.visible
    columnsEach(opt.columns, function (c){
      if(c.field){
        if(c.visible === undefined || c.visible){
          dcv[c.field] = 1;
        }
        var v = cache[c.field];
        if(v !== undefined){
          c.visible = v === 1;
        }
        if(!c.formatter){
          //优化显示（超过行数显示“...”）未设置 formatter 的 TD，样式参考 overflow-div
          if(!$.overflowPopover){
            $.overflowPopover = function (el, opt){
              if(el){
                if(el.clientWidth < el.scrollWidth || el.clientHeight < el.scrollHeight){
                  var $el = $(el);
                  if(!$el.data('bs.popover')){
                    $el.popover($.extend(true, {
                      trigger: "hover",
                      placement: "top",
                      delay: {hide: 100},
                    }, opt))
                      .on('shown.bs.popover', function (event) {
                        var that = this;
                        $(this).parent().find('div.popover').on('mouseenter', function () {
                          $(that).attr('in', true);
                        }).on('mouseleave', function () {
                          $(that).removeAttr('in');
                          $(that).popover('hide');
                        });
                      }).on('hide.bs.popover', function (event) {
                      if ($(this).attr('in')) {
                        event.preventDefault();
                      }
                    });
                  }
                  $el.popover("toggle");
                }
              }
            }
          }
          c.formatter = function (value, row, index, field){
            return value === undefined || value.toString().trim().length === 0 ? ""
              : ("<div tabindex='0' class='overflow-div' onclick=\"$.overflowPopover(this, {content:$(this).text()})\">" + value + "</div>");
          }
        }
      }
    });
    table.data("_defaultColumnVisible_", dcv);
  };
  /** 解决因为表格加载图片时间不一致，导致浮动列高度和表格高度不对齐的问题 */
  var fixedColumnsCheck = function (opt) {
    if(!window.bootstrapTablefixedColumnsCheckRunning && opt.fixedColumns && (opt.fixedNumber > 0 || opt.fixedRightNumber > 0)){
      window.bootstrapTablefixedColumnsCheckRunning = true;
      var checkFunc = function(){
        setTimeout(function () {
          $(".fixed-columns,.fixed-columns-right").each(function () {
            var $this = $(this);
            if($this.attr("style") === undefined || ($this.width() > 0 && $this.parent().height() - $this.height() > 30)){
              $(window).trigger("resize");
            }
          });
          checkFunc();
        }, 1000);
      };
      checkFunc();
    }
  };
  /** 可拖动列开启时，在 table 上加 class = reorderabls-columns-thead */
  var reorderableColumnsAddClass = function (table, opt) {
    if(opt && opt.reorderableColumns){
      if(!table.hasClass("reorderabls-columns-thead")){
        table.addClass("reorderabls-columns-thead");
      }
    }
  };
  var simpleTableDefault = {
    //data中存储复选框value的键值，一般为主键列
    idField: "id",
    //从data中获取每行数据唯一标识的属性名，一般为主键列，如使用方法 getRowByUniqueId
    uniqueId:"id",
    //表格名称，一般用于导出时
    name:'',
    undefinedText:"",
    //toolbar是否在加载完成后默认为缩小状态
    miniToolbar: true,
    //是否显示右侧 toolbar 按钮
    showRightTool: true,
    //是否开启多字段排序
    showMultiSort:true,
    //是否开启列自定义显示
    showColumns: true,
    //是否开启刷新
    showRefresh: true,
    //是否开启样式切换
    showToggle: true,
    //是否开启自动刷新
    autoRefresh: true,
    //自动刷新开关状态
    autoRefreshStatus:false,
    //自动刷新间隔秒
    autoRefreshInterval: 300,
    //是否开启数据导出
    showExport: true,
    //初始化就默认调整表格高度（固定表头）
    initFixedHeight: false,
    //导出方式，basic：当前页，all：全部（会较大的pageSize请求一次数据）
    exportDataType: 'basic',
    exportTypes: ['excel', 'doc', 'json', 'xml', 'csv', 'txt', 'sql'],
    //导出时是否需要添加标题表头，字符串或function(){return "标题表头"}
    exportHeaderName:null,
    exportOptions:{
      mso:{
        //读取td上的style
        styles:["text-align", "font-weight", "font-size"],
        //能转数字的内容转为数字格式，其它的转文本格式
        onMsoNumberFormat:function(cell, row, col) {
          var src = cell.innerText;
          if(src.length > 0){
            var val = src;
            var hasComma = val.indexOf(",") >= 0;
            if(hasComma){
              val = val.replaceAll(",", "");
            }
            if(!isNaN(val)){
              if(hasComma){
                var val2 = val.replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
                //如果加入逗号的数字，与原值不相等，则转文本
                if(val2 !== src){
                  return "\\@";
                }
              }
              var digit = 0;
              var sp = val.split(".");
              if(sp.length === 2){
                digit = digit.toFixed(sp[1].length);
                return "" + digit;
              }
            }
          }
          //默认转文本
          return "\\@";
        }
      },
      //导出文件名，字符串或方法
      fileName: function () {
        var n = '';
        try {
          n = $("#"+ $(window.event.target || window.event.srcElement).parents(".table-content").data("table-id"))
            .bootstrapTable("getOptions").name;
        } catch (e) {}
        if(!n){
          n = $("title").text();
        }
        if(!n){
          n = "TableExport";
        }
        try {
          n += "_" + new Date().format("yyyyMMdd-HHmmss");
        } catch (e) {}
        return n;
      },
      onTableExportBegin: function (){
        var el = $("#"+ $(window.event.target || window.event.srcElement).parents(".table-content").data("table-id"));
        var opt = el.bootstrapTable("getOptions");
        var exportHeaderName = opt.exportHeaderName;
        var name = '';
        if(typeof exportHeaderName === 'string'){
          name = exportHeaderName;
        }
        else if(typeof  exportHeaderName === 'function'){
          name = exportHeaderName();
        }
        if(name){
          var columns = 0;
          el.find("thead tr").each(function (){
            var th = 0;
            $(this).find("th").each(function (){
              var colspan = $(this).attr("colspan") * 1;
              colspan = colspan ? colspan : 1;
              th += colspan;
            });
            columns = Math.max(columns, th);
          });
          var index = 0;
          //存在不导出列的情况
          if(opt.exportOptions.ignoreColumn && opt.exportOptions.ignoreColumn.length > 0){
            for (var i = 0; i < columns; i++) {
              if (opt.exportOptions.ignoreColumn.indexOf(i) < 0) {
                index = i;
                break;
              }
            }
          }
          var tr = $("<tr id='__export_temp_header__'>");
          for (var i = 0; i < index + 1; i++) {
            if(i === index){
              tr.append("<th colspan='"+(columns-index)+"' style='text-align:center;font-size:17px'>" +
                "<div style='height:0.01px;overflow:hidden'>" + name+ "</div></th>")
            }else{
              tr.append("<th><div style='height:0.01px'></div></th>")
            }
          }
          el.find("thead").prepend(tr);
        }
      },
      onTableExportEnd: function (){
        $("#__export_temp_header__").remove();
      }
    },
    //是否显示全选，需要单选为false
    checkboxHeader: false,
    //点击行就选择
    clickToSelect: true,
    //单选
    singleSelect: true,
    //启动分页
    pagination: true,
    showJumpTo: true,
    //每页显示的记录数
    pageSize: window.innerHeight > 860 ? 15 : 10,
    //当前第几页
    pageNumber: 1,
    //记录数可选列表
    pageList: [10, 15, 20, 30, 50, 100, 500, 1000, 3000, 5000],
    //是否启用查询，已自定义，不需要设置为true
    search: false,
    //列浮动，是否开启
    fixedColumns: true,
    //列浮动，浮动左侧列数
    fixedNumber: 1,
    //列浮动，浮动右侧列数
    fixedRightNumber: 0,
    method: "GET",
    //表示服务端请求
    sidePagination: "server",
    //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
    //设置为limit可以获取limit, offset, search, sort, order
    queryParamsType: "undefined",
    loadingFontSize: "2rem",
    //拖动列是否开启
    reorderableColumns:true,
    //拖动列拖动时，跟随拖动效果的行数
    maxMovingRows: 50,
    //解决拖动列结束后，列浮动偶尔失效的问题
    onReorderColumn:function(e){
      if(window.bootstrapTablefixedColumnsCheckRunning){
        setTimeout(function () {
          $(window).trigger("resize");
        }, 1000);
      }
    },
    contentType: "application/json",
    onColumnSwitch:function(field, checked){
      if(checked){
        sortInit(this.data("bootstrap.table"));
      }
    },
    queryParams: function (params) {
      params.pageNum = params.pageNumber;
      var newParams = params;
      var search = this.data("table-search-form").serializeJSON();
      if(search){
        newParams = $.extend(newParams, search);
      }
      var sortField = buildSortField(this.data("bootstrap.table"));
      if(sortField){
        newParams.sortField = sortField;
      }
      return newParams;
    },
    responseHandlerProxy:function(res, responseHandler){
      if(res.sortField){
        var table = this.data("bootstrap.table");
        var arr = res.sortField.split(",");
        if(table.options.showMultiSort){
          table.options.sortPriority = [];
          for(var i = 0; i < arr.length; i++){
            var order = arr[i].split(" ");
            table.options.sortPriority.push({sortName:order[0], sortOrder:order.length>1?order[1].toLowerCase():"asc"});
          }
        }else{
          if(arr.length>0){
            var order = arr[0].split(" ");
            table.options.sortName = order[0];
            table.options.sortOrder = order.length>1?order[1].toLowerCase():"asc";
          }
        }
      }
      //返回值为异常信息，结合 bootstrap-table-zh-CN.js 中的 formatNoMatches 来显示信息
      if(res.exception && res.exceptionId && res.error){
        this.data("bootstrap-error-msg", "<b style='color:red'>请求数据异常： " + res.message + "</b>");
        return {
          "rows": [],
          "total": 0
        };
      }
      //如果定义了 responseHandler 则调用原方法
      if(responseHandler){
        return responseHandler.call(this, res);
      }
      return {
        "rows": res.list,
        "total": res.total
      };
    }
  };
})(jQuery);