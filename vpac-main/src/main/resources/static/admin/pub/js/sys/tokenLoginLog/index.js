var tokenLoginLog = {
  refresh: function () {
    tokenLoginLog.tableEl.bootstrapTable('refresh');
  },
  tableEl: $("#tokenLoginLog_table_list"),
  modalEl: $("#tokenLoginLog_modal"),
  tokenEl: $("#requestToken"),
  requestLogObjChecked: false,
  requestLogUaChecked: false,
  initTable: function () {
    tokenLoginLog.tableEl.bootstrapTable({
      url: ctx + "/admin/sys/tokenLoginLog/page",
      pageSize:10,
      onClickCell:function(field, value, row, el){
        tokenLoginLog.tokenEl.val(row.token);
        tokenRequestLog.refresh();
      },
      columns: [{
        checkbox: true
      },
      {
        title: "userId",
        field: "userId",
        visible:false
      },
      {
        title: "token",
        field: "token",
        visible:false
      },
      {
        title: "登录对象信息 <input type='checkbox' title='展开' style='vertical-align:middle' onclick=\"$(\'.token-info-json\').toggleClass('show');tokenLoginLog.requestLogObjChecked=this.checked\" />",
        field: "tokenObjJson",
        formatter: function (value, row, index) {
          var name = "";
          if(row.tokenObjName){
            name = row.tokenObjName + "<br/>";
          }
          var v = value;
          try {
            v = JSON.stringify(JSON.parse(value), null, 2);
          } catch (e) {}
          return '<div class="token-info-json width-col json-col name-col '+(tokenLoginLog.requestLogObjChecked?"show":"")+'">'+ name + v +'</div>';
        }
      },
      {
        title: "用户类型",
        field: "tokenType"
      },
      {
        title: "登录IP",
        field: "loginIp"
      },
      {
        title: "登录地区",
        field: "loginLocation"
      },
      {
        title: "应用服务器",
        field: "serverHost"
      },
      {
        title: "登录时间",
        field: "createTime"
      },
      {
        title: "操作接口次数",
        field: "requestCount"
      },
      {
        title: "用户代理 <input type='checkbox' title='展开' style='vertical-align:middle' onclick=\"$(\'.ua-info-col\').toggleClass('show');tokenLoginLog.requestLogUaChecked=this.checked\" />",
        field: "userAgent",
        formatter: function (value, row, index) {
          return '<div class="ua-info-col width-col '+(tokenLoginLog.requestLogUaChecked?"show":"")+'">'+value+'</div>';
        }
      }
      ]
    });
  }
};
$(function () {
  tokenLoginLog.initTable();
  $.defaultLayDate('#loginStart','#loginEnd','datetime');
  var select = $("#loginTokenType");
  var array = _tokenTypes_;
  for (var i = 0; i < array.length; i++) {
    var t = array[i];
    select.append("<option value='"+t+"'>"+t+"</option>");
  }
});