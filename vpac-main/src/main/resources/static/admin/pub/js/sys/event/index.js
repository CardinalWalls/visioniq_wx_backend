var eventHandle = {
  searching:false,
  eventId:'-',
  obj: $("#event_handle"),
  init: function (args) {
    eventHandle.eventId = args.eventId;
    $("#choose_event_channel").html(args.eventChannel);
    $("#choose_event_time").html(args.eventCreateTime);
    if(eventHandle.obj.data("bootstrap.table")){
      eventHandle.obj.bootstrapTable("refresh");
      return;
    }
    eventHandle.obj.bootstrapTable({
      url: ctx + "/admin/event/handle/page",
      queryParams: function (params) {
        params.eventId = eventHandle.eventId;
        return params;
      },
      onClickCell:function(field, value, row, el){
        if(value && field === "errorStack"){
          layer.open({
            title: "["+ row.errorTime + "]",
            type: 1,
            skin: 'layui-layer-rim',
            area: ['90%', '90%'],
            content: '<pre class="text-danger">'+value+'</pre>'
          });
        }
      },
      //数据列
      columns: [{
        title: "监听者",
        field: "listenerId"
      }, {
        title: "监听者class",
        field: "listenerClass"
      }, {
        title: "是否完成",
        field: "hasDone",
        formatter: function (value, row, index) {
          var h = "<div style='white-space:nowrap'>";
          switch (value) {
            case -1 : h += "<span class='label label-info'>执行中</span>";break;
            case 0 : h += "<span class='label label-info'>未开始</span>";break;
            case 1: h += "<span class='label label-success'>已完成</span>";break;
            case 2 : h += "<span class='label label-danger status-item'>异常</span><button onclick=\"eventHandle.retry('"+row.id+"')\" class='btn btn-primary btn-xs m-l-xs'><i class='fa fa-refresh'></i> 重试</button>";break;
            case 3 : h += "<span class='label label-success' title='由其它监听器处理'>其它</span>";break;
            case 4: h += "<span class='label label-success'>重试完成</span>";break;
            case 5 : h += "<span class='label label-danger status-item'>重试异常</span><button onclick=\"eventHandle.retry('"+row.id+"')\" class='btn btn-primary btn-xs m-l-xs'><i class='fa fa-refresh'></i> 重试</button>";break;
          }
          return h + "</div>";
        }
      }, {
        title: "完成host",
        field: "doneHost"
      }, {
        title: "完成服务",
        field: "doneServer"
      }, {
        title: "完成时间",
        field: "doneTime"
      }, {
        title: "异常时间",
        field: "errorTime"
      }, {
        title: "异常信息",
        field: "errorStack",
        formatter: function (value, row, index) {
          return value && value.length > 50 ? "<a href=\"javascript:;\">" + value.substr(0, 50) + "</a>": value;
        }
      }, {
        title: "备注",
        field: "remark"
      }]
    });
  },
  refreshGrid:function () {
    eventHandle.searching = true;
    $("#table_list").bootstrapTable("refresh");
    eventHandle.searching = false;
  },
  del:function(){
    layer.confirm('是否要删除该查询结果的全部消息事件？', {
      btn: ['是','否'],
      shade: 0.3
    }, function(){
      var index = layer.load(1);
      $.post(ctx + "/admin/event/del", {
        hasDone: $("#hasDone").val(),
        startDate: $("#startDate").val(),
        endDate: $("#endDate").val(),
        channel: $("#channel").val()
      }, function(res){
        layer.close(index);
        if(res.success){
          layer.alert(res.message + " - Count = " + res.data, {icon: 1});
          $('#table_list').bootstrapTable('refresh');
          eventHandle.init({eventId:'-', eventChannel:'', eventCreateTime:''});
        }
        else{
          layer.alert(res.message, {icon: 2});
        }
      });
    });
  },
  retry:function(id){
    layer.confirm('是否要重新执行此事件处理记录？', {
      btn: ['是','否'],
      shade: 0.3
    }, function(){
      var index = layer.load(1);
      $.post(ctx + "/admin/event/retry", {
        id: id
      }, function(res){
        layer.close(index);
        if(res.success){
          layer.alert(res.message, {icon: 1});
          setTimeout(function () {
            eventHandle.obj.bootstrapTable("refresh");
            $("#table_list").bootstrapTable("refresh");
          },1000);
        }
        else{
          layer.alert(res.message, {icon: 2});
        }
      });
    });
  },
  eventInfoJsonCheck:false
};
$(function () {
  if(messageChannels){
    var c =  $("#channel");
    for(var i in messageChannels){
      var arr = i.split(".");
      c.append("<option value='"+i+"'>"+ arr[arr.length-1] + " - " +messageChannels[i]+"</option>");
    }
  }
  var increment = function(array, i, html){
    var node = array[i];
    node = node ? node : {};
    node.count = node.count === undefined ? 1 : node.count + 1;
    node.html = html;
    array[i] = node;
  };
  $("#table_list").bootstrapTable({
    url: ctx + "/admin/event/list",
    onCheck: function (row) {
      eventHandle.init({eventId:row.id, eventChannel:row.channel, eventCreateTime:row.createTime});
    },
    //数据列
    columns: [{
      checkbox: true
    }, {
      field: "channel",
      title: "事件",
      width: "25%"
    }, {
      field: "channelInfo",
      title: "事件名",
      formatter: function (value, row, index) {
        var arr = row.channel.split("@")[0];
        var typeArr = arr.split(".");
        var name = messageChannels[arr];
        return typeArr[typeArr.length-1] + (name?" - " + name:"");
      }
    }, {
      field: "handleStatus",
      title: "处理状态",
      formatter: function (value, row, index) {
        if(value){
          var arr = value.split(",");
          var val = [];
          for(var i in arr){
            var s = arr[i];
            switch (s) {
              case "'-1'" : increment(val, 0, "<span class='label label-info status-item'>执行中 %s</span>");break;
              case "'0'" : increment(val, 1,"<span class='label label-info status-item'>未开始 %s</span>");break;
              case "'1'" : increment(val, 2,"<span class='label label-success status-item'>已完成 %s</span>");break;
              case "'3'" : increment(val, 3,"<span class='label label-success status-item' title='由其它监听器处理'>其它 %s</span>");break;
              case "'4'" : increment(val, 4,"<span class='label label-success status-item' >重试完成 %s</span>");break;
              case "'2'" : increment(val, 5,"<span class='label label-danger status-item'>异常 %s</span>");break;
              case "'5'" : increment(val, 6,"<span class='label label-danger status-item'>重试异常 %s</span>");break;
            }
          }
          var str = "";
          for (var i = 0; i < val.length; i++) {
            var node = val[i];
            if(node){
              str += node.html.replace("%s", node.count);
            }
          }
          return str;
        }
        return "无监听";
      }
    }, {
      field: "createTime",
      title: "触发时间"
    }, {
      field: "eventInfoJson",
      title: "eventInfoJson <input type='checkbox' style='vertical-align:middle' id='eventInfoJson_check' onclick=\"$(\'.event-info-json\').toggleClass('in');eventHandle.eventInfoJsonCheck=this.checked\" />",
      width: "35%",
      formatter: function (value, row, index) {
        return '<a data-toggle="collapse" data-target="#'+row.id+'_eventInfoJson">' +
          '<i class="fa fa-search pull-left m-r-xs"></i></a><div class="collapse '+(eventHandle.eventInfoJsonCheck?'in':'')+' event-info-json" id="'+row.id+'_eventInfoJson">'+value+'</div>';
      }
    }, {
      field: "remark",
      title: "备注"
    }]
  });
  var start = {
    elem: '#startDate',
    type: 'date',
    done: function (date) {
      end.min = date; //开始日选好后，重置结束日的最小日期
      end.start = date //将结束日的初始值设定为开始日
    }
  };
  var end = {
    elem: '#endDate',
    type: 'date',
    done: function (date) {
      start.max = date; //结束日选好后，重置开始日的最大日期
    }
  };
  laydate.render(start);
  laydate.render(end);
});