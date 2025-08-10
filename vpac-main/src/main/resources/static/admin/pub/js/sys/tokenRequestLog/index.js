var tokenRequestLog = {
  doGroup: 0,
  groupInit:false,
  refresh: function () {
    tokenRequestLog.tableEl.bootstrapTable('refresh');
  },
  changeGroup:function(group){
    if(group === 1){
      tokenRequestLog.doGroup = 1;
      $("#notGroupBtn").attr("class", "btn btn-white");
      $("#doGroupBtn").attr("class", "btn btn-success");
      tokenRequestLog.hideColumn(["uri", "method", "apiName", "requestCount", "errorIp"]);
    }
    else{
      tokenRequestLog.doGroup = 0;
      $("#doGroupBtn").attr("class", "btn btn-white");
      $("#notGroupBtn").attr("class", "btn btn-success");
      tokenRequestLog.showColumn(["token", "errorIp", "requestCount", "errorIp"]);
    }
    tokenRequestLog.refresh();
  },
  hideColumn:function(showColumns){
    tokenRequestLog.tableEl.bootstrapTable('hideAllColumns');
    for (var i = 0; i < showColumns.length; i++) {
      tokenRequestLog.tableEl.bootstrapTable('showColumn', showColumns[i]);
    }
  },
  showColumn:function(hideColumns){
    tokenRequestLog.tableEl.bootstrapTable('showAllColumns');
    for (var i = 0; i < hideColumns.length; i++) {
      tokenRequestLog.tableEl.bootstrapTable('hideColumn', hideColumns[i]);
    }
  },
  tableEl: $("#tokenRequestLog_table_list"),
  modalEl: $("#tokenRequestLog_modal"),
  toggleParamJsonCol:function(el){
    var checked = el.checked;
    tokenRequestLog.requestLogParamsChecked = checked;
    if(checked){
      $('.param-info-json').addClass('show');
    }else{
      $('.param-info-json').removeClass('show').scrollTop(0);
    }
  },
  requestLogParamsChecked:false,
  initTable: function () {
    tokenRequestLog.tableEl.bootstrapTable({
      url: ctx + "/admin/sys/tokenRequestLog/page",
      queryParams: function (params) {
        params.doGroup = tokenRequestLog.doGroup;
        return params;
      },
      showMultiSort:false,
      columns: [
      {
        title: "登录的token",
        field: "token",
        visible:false
      },
      {
        title: "token名称",
        field: "tokenObjName"
      },
      {
        title: "Http方法",
        field: "method"
      },
      {
        title: "API地址",
        field: "uri",
        sortable: true
      },
      {
        title: "API名称",
        field: "apiName",
        sortable: true
      },
      {
        title: "请求次数",
        field: "requestCount",
        visible:false,
        sortable: true
      },
      {
        title: "请求时间",
        field: "createTime",
        sortable: true
      },
      {
        title: "请求IP",
        field: "ip",
        formatter: function (value, row, index) {
          if(row.sameIp === 1){
            return value;
          }else{
            return "<b class='text-danger' title='IP地区异常，与登录IP地区不一致'>"+value+"</b>";
          }
        }
      },
      {
        title: "请求地区",
        field: "ipLocation",
        formatter: function (value, row, index) {
          if(row.sameIp === 1){
            return value;
          }else{
            return "<b class='text-danger' title='IP地区异常，与登录IP地区不一致'>"+value+"</b>";
          }
        }
      },
      {
        title: "异常IP",
        field: "errorIp",
        visible:false,
        sortable: true,
        formatter: function (value, row, index) {
          if(value === 0){
            return "-";
          }else{
            return "<b class='text-danger' title='IP地区异常，与登录IP地区不一致'>"+value+"次</b>";
          }
        }
      },
      {
        title: "应用服务器",
        field: "serverHost"
      },
      {
        title: "请求参数 <input type='checkbox' title='展开' style='vertical-align:middle' onclick=\"tokenRequestLog.toggleParamJsonCol(this)\" />",
        field: "params",
        formatter: function (value, row, index) {
          var v = value;
          try {
            v = JSON.stringify(JSON.parse(value), null, 2);
          } catch (e) {}
          return '<div class="param-info-json width-col json-col mCS-dir-rtl '+(tokenRequestLog.requestLogParamsChecked?"show":"")+'">'+v+'</div>';
        }
      }
      ]
    });
  }
};
$(function () {
  tokenRequestLog.initTable();
  $.defaultLayDate('#requestStart','#requestEnd','datetime');
});