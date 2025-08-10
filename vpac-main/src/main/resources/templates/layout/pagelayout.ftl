<#--普通应用页面引用-->
<#macro headContent>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${(_APP_NAME_ == null)? string(RequestParameters['_APP_NAME_'], _APP_NAME_)}</title>
  <meta name="keywords" content="">
  <meta name="description" content="">
  <link rel="shortcut icon" href="${ctx!}/admin/favicon.ico">
  <link href="${ctx!}/admin/css/bootstrap.min.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/font-awesome.min.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/plugins/bootstrap-table/bootstrap-table-tree-column.css" rel="stylesheet">
  <link href="${ctx!}/admin/js/plugins/bootstrap-table/fixed-columns/bootstrap-table-fixed-columns.min.css" rel="stylesheet">
  <link href="${ctx!}/admin/js/plugins/bootstrap-editable/bootstrap-editable.css" rel="stylesheet"/>
  <link href="${ctx!}/admin/css/animate.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/style.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/theme.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/plugins/switch/bootstrap-switch.min.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/plugins/selectpage/selectpage.css" type="text/css" rel="stylesheet">
  <link href="${ctx!}/admin/js/plugins/dragtable/dragtable.css" type="text/css" rel="stylesheet">
  <script type="text/javascript">
    var ctx = "${ctx!}";
  </script>
  <!--jquery-->
  <script src="${ctx!}/admin/js/jquery.min.js"></script>
  <script src="${ctx!}/admin/js/plugins/jquery-treegrid-master/js/jquery.cookie.js"></script>
  <script src="${ctx!}/admin/js/theme.js"></script>
  <script src="${ctx!}/admin/js/jquery.serializejson.min.js"></script>
  <script src="${ctx!}/admin/js/jquery.form.min.js"></script>
  <script src="${ctx!}/admin/js/jquery-ui.min.js"></script>
  <script src="${ctx!}/admin/js/plugins/dragtable/jquery.dragtable.js"></script>
  <!--bootstrap-->
  <script src="${ctx!}/admin/js/bootstrap.min.js"></script>
  <!-- Bootstrap table -->
  <script src="${ctx!}/admin/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
  <script src="${ctx!}/admin/js/plugins/bootstrap-table/tableExport.js"></script>
  <script src="${ctx!}/admin/js/plugins/bootstrap-table/bootstrap-table-export.js"></script>
  <script src="${ctx!}/admin/js/plugins/bootstrap-table/bootstrap-table-tree-column.min.js"></script>
  <script src="${ctx!}/admin/js/plugins/bootstrap-editable/bootstrap-editable.min.js"></script>
  <script src="${ctx!}/admin/js/plugins/bootstrap-editable/bootstrap-table-editable.min.js"></script>
<#--<script src="${ctx!}/admin/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js"></script>-->
  <script src="${ctx!}/admin/js/plugins/bootstrap-table/bootstrap-table-multiple-sort.min.js"></script>
  <script src="${ctx!}/admin/js/plugins/bootstrap-table/bootstrap-table-auto-refresh.min.js"></script>
  <script src="${ctx!}/admin/js/plugins/bootstrap-table/fixed-columns/bootstrap-table-fixed-columns.min.js"></script>
  <script src="${ctx!}/admin/js/plugins/bootstrap-table/bootstrap-table-reorder-columns.min.js"></script>
<#--Bootstrap table 本地化-放最后-->
  <script src="${ctx!}/admin/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
  <script src="${ctx!}/admin/js/plugins/bootstrap-table/bootstrap-table-init.js"></script>
  <!-- Peity -->
  <script src="${ctx!}/admin/js/plugins/peity/jquery.peity.min.js"></script>
  <!-- layer -->
  <script src="${ctx!}/admin/js/plugins/layer/layer.min.js"></script>
  <script src="${ctx!}/admin/js/plugins/layer/extend/layer.ext.js"></script>
  <script src="${ctx!}/admin/js/plugins/layer/laydate/laydate.5.0.9.js"></script>
  <script src="${ctx!}/admin/js/plugins/layer/laydate/ladate.default.js"></script>
  <!-- 自定义js -->
  <script src="${ctx!}/admin/js/content.js"></script>
  <script src="${ctx!}/admin/js/jquery.payload.js"></script>
  <script src="${ctx!}/admin/js/plugins/selectpage/selectpage.js"></script>
  <script src="${ctx!}/admin/pub/js/attachment/Sortable.min.js"></script>
  <!--jquery validate-->
  <script src="${ctx!}/admin/js/plugins/validate/jquery.validate.min.js"></script>
  <script src="${ctx!}/admin/js/plugins/validate/messages_zh.min.js"></script>
  <script src="${ctx!}/admin/js/plugins/validate/jquery.validate.init.js"></script>
  <#nested/>
</head>
</#macro>
<#macro bodyContent>
<body>
  <div class="wrapper wrapper-content">
  <#nested/>
  </div>
</body>
</html>
</#macro>