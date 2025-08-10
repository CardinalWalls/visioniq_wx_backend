var tp = {
  autoRefreshLimit: 60,
  autoRefreshStop: false,
  curId: '',
  search: function () {
    tableList.init();
  },
  searchChild: function () {
    var type = $('#type').val();
    if (!type) {
      layer.alert("请选择一种类型");
      return;
    }
    childList.init(type);
  },
  clearCondition: function () {
    document.searchForm.reset();
    tp.search();
  },
  openChildModal: function (id, type) {
    childList.obj.bootstrapTable('destroy');
    tp.curId = id;
    $("#childModal").modal();
    var l = layer.load(1, {shade: 0.3});
    $.get(ctx + "/admin/dynamicform/type?code=" + tableList.curCode, function (rs) {
      layer.close(l);
      var parse = JSON.parse(rs);
      $('option[name="customOption"]').remove();
      for (var i = 0; i < parse.length; i++) {
        $("#type").append('<option value="' + parse[i] + '" name="customOption">' + parse[i] + '</option>');
      }
      if (type) {
        childList.init(type);
      }
    }, "text");
  },
  changeStatus:function (id, status) {
    layer.prompt({
      title: "设置为：" + (status===0?"已取消":"已处理") + " > 请填写处理结果",
      formType: 2,
      allowBlank:true
    }, function (result) {
      $.putPayload(ctx + "/admin/dynamicform/data/status", {id:id, status:status, result:result.trim()},function (rs) {
        layer.msg(rs.message, {icon: 1});
        tableList.obj.bootstrapTable('refresh');
      });
    });
  },
  clickAutoRefreshBtn:function (e, play) {
    if(play === 0){
      tp.autoRefreshStop = true;
      $(e).replaceWith('<button onclick="tp.clickAutoRefreshBtn(this,1)" id="autoRefreshBtn" title="继续刷新" class="btn btn-info btn-xs"><i class="fa fa-play"></i></button>');
    }
    else{
      tp.autoRefreshStop = false;
      $(e).replaceWith('<button onclick="tp.clickAutoRefreshBtn(this,0)" id="autoRefreshBtn" title="暂停刷新" class="btn btn-warning btn-xs"><i class="fa fa-pause"></i></button>');
    }
  }

};

var tableList = {
  obj: $("#table_list"),
  params: [],
  curCode: '',
  init: function () {
    var params = $("#searchForm").serializeJSON();
    tableList.curCode = params.code;
    $.ajax({
      type: "GET",
      url: "/admin/dynamicform/columns",
      contentType: "application/json;charset=utf-8",
      dataType: "json",
      data: params,
      json: 'callback',
      success: function (json) {
        if(json.exception){
          return;
        }
        if (code !== tableList.curCode) {
          tableList.curCode = code;
          $('div[name="otherqueryform"]').remove();
          for (var index in json) {
            var column = json[index];
            // if(column.field === 'title'){
            //   column.visible = false;
            //   continue;
            // }
            if(column.attachment){
              column.formatter = function (value, row, index) {
                if(value){
                  var array = JSON.parse(value);
                  var html = "";
                  for (var i = 0; i < array.length; i++) {
                    html += attachment.toViewHtml(array[i].url) + " ";
                  }
                  return html;
                }
              };
            }
            else{
              var prefix = "jsonData_";
              if(column.field.indexOf(prefix) === 0){
                var name = column.field.substr(prefix.length);
                var existsParams = params[name];
                // 添加搜索条件
                $('#otherquery').append('<div class="form-group" name="otherqueryform">\n' +
                  '          <label for="title" class="sr-only">' + column.title + '</label>\n' +
                  '          <input value="' + (existsParams === undefined ? "" : existsParams) + '" type="text" placeholder="' + column.title + '" name="' + name + '" id="' + column.field + '" class="form-control">\n' +
                  '        </div>');
              }
              if(column.require){
                column.title = "<b style='color:red'>* </b>" + column.title;
              }
              if(column.field === "userInfo"){
                column.formatter = function (value, row, index) {
                  if(row.userId){
                    var img = attachment.toViewHtml(row.userAvatar ? row.userAvatar : location.origin+"/admin/img/avatar.png", null, "m-r");
                    var name = "<span>" + row.userName + "</span>";
                    return "<div style='white-space:nowrap'>" + img + "<span class='m-r-xs'>"+row.userPhone+"</span>" + name + "</div>";
                  }
                }
              }
              else if(column.field === "remark"){
                column.editable = {
                  type: 'text',
                  escape: false,
                  mode: "popup",
                  emptytext:"点击修改",
                  emptyclass:"editable-text",
                  title: '设置备注信息'
                }
              }
            }
          }
        }

        json.push({
          title: "附属信息",
          field: "deal",
          visible: false,
          formatter: function (value, row, index) {
            var str = '';
            for (var key in row) {
              if (key.indexOf('count_') === 0) {
                var keystr = key.replace('count_', '');
                str += '<button class="btn btn-info btn-xs" type="button" onclick="tp.openChildModal(\'' + row.id + '\', \'' + keystr + '\')">' + keystr + '(' + row[key] + ')</button> &nbsp;&nbsp;';
              }
            }
            return str;
          }
        });
        json.push({
          title: "状态",
          field: "_status",
          formatter: function (value, row, index) {
            var html = "";
            if(value === 2){
              html += "<label class='m-r-xs text-success'>已处理</label>";
            }
            else if(value === 1){
              html += "<label class='m-r-xs text-info'>未处理</label>";
            }
            else{
              html += "<label class='m-r-xs text-warning'>已取消</label>";
            }
            return html;
          }
        });
        json.push({
          title: "处理",
          field: "_opt",
          formatter: function (value, row, index) {
            var html = "<div style='min-width:55px'>";
            if(row._status === 2){
              html += "<button class='btn btn-xs btn-warning m-r-xs' title='设为已取消' onclick=\"tp.changeStatus('"+row.id+"', 0)\"><i class='fa fa-times-circle'></i></button>";
            }
            else if(row._status === 1){
              html += "<button class='btn btn-xs btn-success m-r-xs' title='设为已处理' onclick=\"tp.changeStatus('"+row.id+"', 2)\"><i class='fa fa-check'></i></button><button class='btn btn-xs btn-warning' title='设为已取消' onclick=\"tp.changeStatus('"+row.id+"', 0)\"><i class='fa fa-times-circle'></i></button>";
            }
            else{
              html += "<button class='btn btn-xs btn-success' title='设为已处理' onclick=\"tp.changeStatus('"+row.id+"', 2)\"><i class='fa fa-check'></i></button>";
            }
            return html + "</div>";
          }
        });
        tableList.obj.bootstrapTable('destroy').bootstrapTable({
          url: ctx + "/admin/dynamicform/page",
          autoRefresh:false,
          onLoadSuccess:function(data){
            if(tp.autoRefresh){
              tp.autoRefresh = false;
              var rows = data.rows;
              for (var i = 0; i < rows.length; i++) {
                if(rows[i]._status === 1){
                  $("#newItemAudio")[0].play();
                  return;
                }
              }
            }
          },
          onEditableSave:function (field, row, rowIndex, oldValue, $el) {
            $.putPayload(ctx + "/admin/dynamicform/data/remark", {id:row.id, remark:row.remark},function (rs) {
              layer.msg("操作成功");
              tp.obj.bootstrapTable('updateRow', {index: row.id, row: row});
            });
          },
          //数据列
          columns: json
        });
      }
    });

  }
};

var childList = {
  obj: $("#child_list"),
  init: function (type) {
    $.ajax({
      type: "GET",
      url: "/admin/dynamicform/child/columns?code=" + tableList.curCode + "&type=" + type,
      contentType: "application/json;charset=utf-8",
      dataType: "json",
      json: 'callback',
      success: function (json) {
        childList.obj.bootstrapTable('destroy').bootstrapTable({
          url: ctx + "/admin/dynamicform/child?id=" + tp.curId + "&type=" + type,
          //数据列
          columns: json
        });
      }
    });

  }
};

$(function () {
  var paramStatus = $("#paramStatus").val();
  console.log(paramStatus);
  if(paramStatus != null){
    $("#searchStatus").val(paramStatus);
  }
  tp.search();

  // 监听机构选择
  changeCodes();

  startAutoRefreshTime(tp.autoRefreshLimit);
});

function startAutoRefreshTime(seconds) {
  if(tp.autoRefreshStop){
    seconds++;
  }
  $("#autoRefreshTime").html(seconds);
  if(seconds > tp.autoRefreshLimit){
    seconds = tp.autoRefreshLimit;
  }
  if(seconds <= 0){
    tp.autoRefresh = true;
    tableList.init();
    seconds = tp.autoRefreshLimit + 1;
  }
  setTimeout(function () {
    startAutoRefreshTime(seconds - 1);
  }, 1000);
}

function changeCodes() {
  var curCode = getQueryVariable("code");
  $.get(ctx + "/admin/dynamicform/codes", {}, function (res) {
    var html = "<option value=''>请选择编号</option>";
    var curCodeEx = false;
    $.each(res, function (index, value) {
      if (curCode == value.code) {
        html += "<option selected value='" + value.code + "'>" + value.title + "</option>"
        curCodeEx = true;
      } else {
        html += "<option value='" + value.code + "'>" + value.title + "</option>"
      }
    });
    $("#code").html(html);
    if(curCodeEx){
      tp.search();
    }
  });
}

function getQueryVariable(variable) {
  var query = window.location.search.substring(1);
  var vars = query.split("&");
  for (var i = 0; i < vars.length; i++) {
    var pair = vars[i].split("=");
    if (pair[0] == variable) {
      return pair[1];
    }
  }
  return (false);
}