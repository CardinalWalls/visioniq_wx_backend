<!DOCTYPE html>
<html>

<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">


  <title>404-页面未找到</title>
  <meta name="keywords" content="">
  <meta name="description" content="">

  <link href="${ctx!}/admin/css/bootstrap.min.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/font-awesome.css" rel="stylesheet">

  <link href="${ctx!}/admin/css/animate.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/style.css" rel="stylesheet">

</head>

<body class="gray-bg">


<div class="middle-box text-center animated fadeInDown">
  <h1>404</h1>
  <h3 class="font-bold">Page Not Found</h3>
  <h3 class="font-bold">页面未找到</h3>
  <div class="error-desc">
    <div class="error-desc">
      <div id="btn_div">
      </div>
    </div>
  </div>
</div>

<!-- 全局js -->
<script src="${ctx!}/admin/js/jquery.min.js"></script>
<script src="${ctx!}/admin/js/bootstrap.min.js"></script>

<script>
  $(function(){
    if(window.parent === window){
      $("#btn_div").append("<a href='javascript:history.back();' class='btn btn-primary m-t'>" +
        "<i class='fa fa-reply'></i> 返回</a>");
    }else{
      $("#btn_div").append("<a href=\"javascript:window.parent.index.closeNav(\'"+location.pathname+"\');\" " +
        "class='btn btn-primary m-t'><i class='fa fa-close'></i> 关闭</a>");
    }
  });
</script>
</body>

</html>
