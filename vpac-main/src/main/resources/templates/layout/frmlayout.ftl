<#--弹出的表单页面引用-->
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
  <link href="${ctx!}/admin/css/plugins/chosen/chosen.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/bootstrap.min.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/font-awesome.min.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/animate.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/style.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/theme.css" rel="stylesheet">
  <script type="text/javascript">
    var ctx = "${ctx!}";
  </script>
  <!--jquery-->
  <script src="${ctx!}/admin/js/jquery.min.js"></script>
  <script src="${ctx!}/admin/js/plugins/jquery-treegrid-master/js/jquery.cookie.js"></script>
  <script src="${ctx!}/admin/js/theme.js"></script>
  <script src="${ctx!}/admin/js/jquery.serializejson.min.js"></script>
  <script src="${ctx!}/admin/js/jquery.form.min.js"></script>

  <!-- Chosen -->
  <script src="${ctx!}/admin/js/plugins/chosen/chosen.jquery.js"></script>
  <!--bootstrap-->
  <script src="${ctx!}/admin/js/bootstrap.min.js"></script>
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