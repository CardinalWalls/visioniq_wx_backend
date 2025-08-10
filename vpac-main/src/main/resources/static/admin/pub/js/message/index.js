
var ms = {
  searching:false,
  clearSearch:function(){
    $("#search_form .form-control").val("");
    ms.search('table_list');
  },
  search:function(table){
    ms.searching = true;
    $('#' + table).bootstrapTable('refresh');
  }
};
$(function(){
  $("#table_list").bootstrapTable({
    url: ctx + "/admin/message/page",
    //数据列
    columns: [{
      title: "接收用户",
      field: "userNickName"
    }, {
      title: "用户类型",
      field: "userTypeName"
    }, {
      title: "手机号",
      field: "phone"
    }, {
      title: "消息内容",
      field: "content"
    }, {
      title: "发送时间",
      field: "createTime"
    }, {
      title: "读取时间",
      field: "knowTime"
    }, {
      title: "状态",
      field: "status",
      formatter: function (value, row, index) {
        return "<label class='label label-"+(value === 1?"info":"warning")+"'>"+(value === 1?"已读":"未读")+"</label>"
      }
    }]
  });

});