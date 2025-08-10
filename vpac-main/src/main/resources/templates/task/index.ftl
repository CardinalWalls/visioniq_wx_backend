<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>

<@headContent/>
<link rel="stylesheet" type="text/css" href="${ctx}/admin/pub/plugin/codemirror/codemirror.css">
<link rel="stylesheet" type="text/css" href="${ctx}/admin/pub/plugin/codemirror/addon/hint/show-hint.css">
<@bodyContent>
  <div class="panel panel-default">
    <div class="panel-heading">
      <b>调度作业管理</b>
    </div>
    <div class="panel-body table-content">
      <div class="row">
        <div class="col-xs-12 font-bold">
          <label>已启动的作业服务：${((taskServers?size) == 0)? string('<span class="text-danger">无服务，请启动至少一个服务','')}</label>
            <#list taskServers as taskUrl>
              <label class="label label-primary"><a href="${taskUrl}/info" target="_blank" style="color:#FFF">${taskUrl}</a></label>
            </#list>
          <br/>
        </div>
      </div>
        <#--<form class="table-search">-->
        <#--<div class="form-inline">-->
        <#--<button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i>-->
        <#--</button>-->
        <#--<button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>-->
        <#--</button>-->
        <#--</div>-->
        <#--</form>-->
      <div class="table-toolbar">
        <button class="btn btn-link" style="border:1px solid #888" type="button" onclick="taskPage.findLogs()">
          <i class="fa fa-file-text"></i> 日志
        </button>
        <button class="btn btn-info" type="button" onclick="taskPage.add()">
          <i class="fa fa-plus"></i> 新增
        </button>
        <button class="btn btn-success" type="button" onclick="taskPage.edit()">
          <i class="fa fa-edit"></i> 修改
        </button>
        <button class="btn btn-info" type="button" onclick="taskPage.trigger()">
          <i class="fa fa-rocket"></i> 执行
        </button>
        <button class="btn btn-primary" type="button" onclick="taskPage.enable()">
          <i class="fa fa-check-circle"></i> 启用
        </button>
        <button class="btn btn-warning" type="button" onclick="taskPage.disable()">
          <i class="fa fa-minus-circle"></i> 禁用
        </button>
        <button class="btn btn-danger" type="button" onclick="taskPage.rebuild()">
          <i class="fa fa-wrench"></i> 重构
        </button>
      </div>
      <table id="table_list" class="table-striped"></table>
    </div>
  </div>

  <div class="panel panel-default">
    <div class="panel-heading">
      <b>调度作业日志</b>
    </div>
    <div class="panel-body table-content">
      <form class="table-search">
        <div class="form-inline">
          <div class="form-group">
            <select class="form-control logs-search" id="logs_executeSuccess" name="executeSuccess"
                    onchange="$('#log_table').bootstrapTable('refresh');">
              <option value="">执行结果</option>
              <option value="true">成功</option>
              <option value="false">失败</option>
            </select>
          </div>
          <div class="form-group">
            <label class="control-label">ID：</label>
            <input class="form-control logs-search" id="logs_taskId" name="taskId" style="min-width:320px">
          </div>
          <div class="form-group">
            <label class="control-label">日期：</label>
            <input class="form-control layer-date logs-search" style="background-color:#FFF" placeholder="起始日期" name="timeS"
                   readonly
                   id="logs_timeS">
            <input class="form-control layer-date logs-search" style="background-color:#FFF" placeholder="截止日期"
                   readonly
                   id="logs_timeE" name="timeE">
          </div>

          <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i>
          </button>
          <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i
                class="fa fa-recycle"></i>
          </button>
        </div>
      </form>
      <div class="table-toolbar">
        <button class="btn btn-danger" type="button"
                onclick="taskPage.deleteLogs();">
          <i class="fa fa-close"></i>
        </button>
      </div>
      <table id="log_table" class="table-striped"></table>
    </div>
  </div>

  <div class="modal" id="task_modal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" style="width:80%">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                class="sr-only">关闭</span>
          </button>
          <h4 class="modal-title">
          </h4>
        </div>
        <div class="modal-body">
          <form class="panel-body form-horizontal" id="task_form">
            <input type="hidden" class="form-control" name="id"/>
            <div class="form-group col-xs-6 form-line-l">
              <label class="control-label">名称</label>
              <div>
                <input type="text" name="name" class="form-control" required>
              </div>
            </div>
            <div class="form-group col-xs-6 form-line-r">
              <label class="control-label">描述：</label>
              <div>
                <input type="text" class="form-control" name="description"/>
              </div>
            </div>
            <div class="form-group col-xs-6 form-line-l">
              <label class="control-label">时间表达式（例，每5分钟执行：0 0/5 * * * ? *）：</label>
              <div>
                <input type="text" class="form-control" name="cronExpression" required/>
              </div>
            </div>
            <div class="form-group col-xs-6 form-line-r">
              <label class="control-label">调用参数Json：</label>
              <div>
                <input type="text" class="form-control" name="argumentsMap" value="{}" />
              </div>
            </div>
            <div class="form-group">
              <label class="control-label">调用类方式：</label>
              <div>
                <select class="form-control" id="classType">
                  <option value="static" selected>调用静态类</option>
                  <option value="dynamic">编写动态类</option>
                </select>
              </div>
            </div>
            <div class="form-group" id="staticDiv">
              <label class="control-label">调用静态类（完整类名）：</label>
              <div>
                <input type="text" class="form-control" name="targetClass" id="targetClass" />
              </div>
            </div>
            <div class="form-group" id="dynamicDiv" style="display:none">
              <label class="control-label">编写动态类（勿改包名和类名）：</label>
              <div>
                <textarea id="classCode"></textarea>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" onclick="taskPage.submit()"><i class="fa fa-save"></i>提交</button>
          <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>取消</button>
        </div>
      </div>
    </div>
  </div>
  <script src="${ctx!}/admin/pub/plugin/codemirror/codemirror.js"></script>
  <script src="${ctx!}/admin/pub/plugin/codemirror/addon/edit/matchbrackets.js"></script>
  <script src="${ctx!}/admin/pub/plugin/codemirror/addon/hint/show-hint.js"></script>
  <script src="${ctx!}/admin/pub/plugin/codemirror/mode/clike/clike.js"></script>
  <script src="${ctx!}/admin/pub/plugin/codemirror/addon/display/autorefresh.js"></script>
  <!-- Page-Level Scripts -->
  <script type="text/javascript">
    var taskPage = {
      defaultCode:"package sys.task.handler;\nimport com.base.components.common.service.task.BaseTaskHandler;\nimport java.util.Map;\n\npublic class DynamicTaskHandler extends BaseTaskHandler {\n  @Override\n  public void execute(Map<String, Object> argMap) {\n    System.err.println(\"开始执行: \" + argMap);\n  }\n}",
      refresh: function () {
        $("#table_list").bootstrapTable("refresh");
      },
      select: function () {
        var select = $("#table_list").bootstrapTable("getSelections");
        if (select.length !== 1) {
          layer.alert('请选择一个调度作业');
          return null;
        }
        return select[0];
      },
      findLogs: function () {
        var select = taskPage.select();
        if (select) {
          $("#logs_taskId").val(select.id);
          $('#log_table').bootstrapTable('refresh');
        }
      },
      modal: function (title) {
        var modal = $("#task_modal");
        if (title) {
          modal.find(".modal-title").html(title);
          modal.modal({backdrop: 'static'});
        } else {
          modal.modal("hide");
        }
      },
      editTask:null,
      add: function () {
        $('#task_form')[0].reset();
        taskPage.modal("新增");
      },
      edit: function () {
        var select = taskPage.select();
        if (select) {
          $.get("${ctx!}/admin/task/find/" + select.id, function(rs){
            if(!rs || !rs.id){
              layer.alert("未找到数据，请刷新列表", {icon: 2});
              return;
            }
            taskPage.editTask = rs;
            $('#task_form .form-control').each(function () {
              var $this = $(this);
              var name = $this.attr("name");
              if(name){
                $this.val(select[name + '']);
              }
            });
            taskPage.modal("修改");
            if(select.targetClass){
              $("#classType").val("static").trigger("change");
            }else{
              $("#classType").val("dynamic").trigger("change");
            }
          });
        }
      },
      submit:function (){
        var f = $('#task_form');
        if (!f.valid()) {
          return;
        }
        var params = f.serializeJSON();
        params.targetClassScript = taskPage.codeEditor.getValue().trim();
        layer.load(1, {shade: 0.3});
        $.post("${ctx!}/admin/task/save", params, function(rs){
          layer.closeAll();
          if (rs.success) {
            taskPage.modal(null);
            layer.msg(rs.message, {icon: 1});
            taskPage.refresh();
          } else {
            layer.alert(rs.message, {icon: 2});
          }
        });
      },
      enable: function () {
        var select = taskPage.select();
        if (select) {
          layer.confirm('是否将[' + select.name + "]设为启用并同步更新到所有服务中？", {
            btn: ['是', '否'],
            shade: 0.3
          }, function () {
            var index = layer.load(1);
            $.post("${ctx!}/admin/task/enable/" + select.id, function (res) {
              layer.close(index);
              layer.alert(res.message, {icon: res.success ? 1 : 2});
              taskPage.refresh();
            });
          });
        }
      },
      disable: function () {
        var select = taskPage.select();
        if (select) {
          layer.confirm('是否将[' + select.name + "]设为禁用并同步更新到到所有服务中？", {
            btn: ['是', '否'],
            shade: 0.3
          }, function () {
            var index = layer.load(1);
            $.post("${ctx!}/admin/task/disable/" + select.id, function (res) {
              layer.close(index);
              layer.alert(res.message, {icon: res.success ? 1 : 2});
              taskPage.refresh();
            });
          });
        }
      },
      trigger: function () {
        var select = taskPage.select();
        if (select) {
          if (select.disabled) {
            layer.alert("此调度作业已经被禁用", {icon: 2});
          } else {
            layer.confirm('是否广播所有服务并立即触发执行调度作业[' + select.name + "]？", {
              btn: ['是', '否'],
              shade: 0.3
            }, function () {
              var defaultArg = select['argumentsMap'];
              layer.prompt({
                title: '触发调用的参数：',
                formType: 2,
                maxlength: 1000,
                value: defaultArg ? defaultArg : '{}',
              }, function (v) {
                var index = layer.load(1);
                $.post("${ctx!}/admin/task/trigger/" + select.id, {argumentsMap: v}, function (res) {
                  layer.close(index);
                  layer.alert(res.message + (res.success ? ("触发执行数:" + res.data) : ""), {icon: res.success ? 1 : 2});
                  taskPage.refresh();
                });
              });
            });
          }
        }
      },
      rebuild: function () {
        layer.confirm('是否要将当前数据库记录同步更新至所有调度作业服务中进行重构？', {
          btn: ['是', '否'],
          shade: 0.3
        }, function () {
          var index = layer.load(1);
          $.post("${ctx!}/admin/task/rebuild", function (res) {
            layer.close(index);
            layer.alert(res.message, {icon: res.success ? 1 : 2});
          });
        });
      },
      logsSearch: function (params) {
        return {
          pageSize: params.pageSize,
          pageNum: params.pageNum,
          taskId: $("#logs_taskId").val(),
          timeS: $("#logs_timeS").val(),
          timeE: $("#logs_timeE").val(),
          executeSuccess: $("#logs_executeSuccess").val()
        };
      },
      deleteLogs: function () {
        layer.confirm('是否要删除该查询结果的全部作业日志？', {
          btn: ['是', '否'],
          shade: 0.3
        }, function () {
          var index = layer.load(1);
          $.post("${ctx!}/admin/task/logs/delete", {
            taskId: $("#logs_taskId").val(),
            timeS: $("#logs_timeS").val(),
            timeE: $("#logs_timeE").val(),
            executeSuccess: $("#logs_executeSuccess").val()
          }, function (res) {
            layer.close(index);
            if (res.success) {
              layer.alert(res.message + " - Count = " + res.data, {icon: 1});
              $('#log_table').bootstrapTable('refresh');
            }
            else {
              layer.alert(res.message, {icon: 2});
            }
          });
        });
      }
    };
    $(document).ready(function () {
      //初始化表格,动态从服务器加载数据
      $("#table_list").bootstrapTable({
        url: "${ctx!}/admin/task/list",
        //数据列
        columns: [{
          checkbox: true
        }, {
          title: "名称",
          field: "name"
        }, {
          title: "描述",
          field: "description"
        }, {
          title: "调用Class",
          field: "targetClass",
          formatter: function (value, row, index) {
            return row.targetClassScript ? "<label class='label label-default'>动态代码</label>" : value;
          }
        }, {
          title: "时间表达式",
          field: "cronExpression"
        }, {
          title: "调用参数",
          field: "argumentsMap"
        }, {
          title: "最后执行",
          field: "lastServer"
        }, {
          title: "最后执行时间",
          field: "lastTime"
        }, {
          title: "启用",
          field: "disabled",
          formatter: function (value, row, index) {
            return value ? '否' : '是';
          }
        }, {
          title: "执行中",
          field: "locking",
          formatter: function (value, row, index) {
            return value ? '<span class="text-danger">是</span>' : '否';
          }
        }, {
          title: "创建日期",
          field: "createTime"
        }]
      });


      $("#log_table").bootstrapTable({
        url: "${ctx!}/admin/task/logs",
        onClickCell: function (field, value, row, el) {
          if (value && field === "executeExceptions") {
            layer.open({
              title: "[" + row.taskName + "]， [" + row.taskFireTime + "]， [" + row.executeServerHost + "]",
              type: 1,
              skin: 'layui-layer-rim',
              area: ['90%', '90%'],
              content: '<pre class="text-danger">' + value + '</pre>'
            });
          }
        },
        queryParams: taskPage.logsSearch,
        pageSize: 10,
        idField: "id",
        //数据列
        columns: [{
          title: "名称",
          field: "taskName"
        }, {
          title: "触发时间",
          field: "taskFireTime"
        }, {
          title: "完成时间",
          field: "executeOverTime"
        }, {
          title: "结果",
          field: "executeSuccess",
          formatter: function (value, row, index) {
            return value ? '成功' : '<span class="text-danger">失败</span>';
          }
        }, {
          title: "异常信息",
          field: "executeExceptions",
          formatter: function (value, row, index) {
            return value && value.length > 53 ? "<a href=\"javascript:;\">" + value.substr(0, 53) + "</a>" : value;
          }
        }, {
          title: "调用服务",
          field: "executeServerHost"
        }, {
          title: "执行信息",
          field: "resultMessage",
          formatter: function (value, row, index) {
            return "<div style='white-space:pre'>"+value+"</div>";
          }
        }]
      });
      $.defaultLayDate('#logs_timeS','#logs_timeE','datetime');


      taskPage.codeEditor = CodeMirror.fromTextArea(document.getElementById("classCode"), {
        lineNumbers: true,
        mode: "text/x-java",
        indentUnit: 2,
        indentWithTabs: true,
        styleActiveLine: true,
        matchBrackets: true,
        autoRefresh: true
      });

      $("#classType").on("change", function (){
        var edit = taskPage.editTask;
        var $this = $(this);
        if($this.val() === "static"){
          taskPage.codeEditor.setValue("");
          $("#targetClass").val(edit ? edit.targetClass : "");
          $("#staticDiv").show();
          $("#dynamicDiv").hide();
        }
        else{
          $("#dynamicDiv").show();
          taskPage.codeEditor.setValue(edit && edit.targetClassScript ? edit.targetClassScript : taskPage.defaultCode);
          taskPage.codeEditor.refresh();
          $("#targetClass").val("");
          $("#staticDiv").hide();
        }
      });
    });


  </script>
</@bodyContent>