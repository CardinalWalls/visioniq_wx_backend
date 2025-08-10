<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>401-登录失效</title>
  <meta name="keywords" content="">
  <meta name="description" content="">
  <link href="${ctx!}/admin/css/bootstrap.min.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/font-awesome.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/animate.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/style.css" rel="stylesheet">
</head>
<body class="gray-bg">
<div class="middle-box text-center animated fadeInDown">
  <h1>401</h1>
  <h3 class="font-bold">UNAUTHORIZED</h3>
  <h3 class="font-bold">登录信息已失效，请重新登录！</h3>
  <div class="error-desc">
    <a href="javascript:toLogin()" id="backBtn" class="btn btn-primary m-t">返回登录 <span id="countDownSpan"></span></a>
  </div>
</div>

<!-- 全局js -->
<script src="${ctx!}/admin/js/jquery.min.js"></script>
<script src="${ctx!}/admin/js/bootstrap.min.js"></script>
<script>
  var _stop_ = false;
  $(function(){
    $(window).keydown(function (e) {
      if (e.keyCode === 13) {
        toLogin();
      }
      return true;
    });
    var countDownSpan = $("#countDownSpan");
    var countDownToLogin = function (s) {
      if(_stop_){
        return;
      }
      if(s === 0){
        toLogin();
      }else{
        countDownSpan.html("（" + s + "s）");
        setTimeout(function () {
          countDownToLogin(s-1);
        }, 1000);
      }
    };
    countDownToLogin(6);
  });
  function toLogin() {
    _stop_ = true;
    $("#countDownSpan").html("");
    //如果当前页是在iframe中，在top window中打开一个新的iframe临时登录页
    if(window.top.index && window.top.location !== window.location ){
      window.top.index.openLoginFrame();
      $("#backBtn").hide();
      //检查是否登录成功
      checkFrameLogin();
    }else{
      window.parent.location.href='${ctx!}/admin/login';
    }
  }
  function checkFrameLogin() {
    if(window.top.document.getElementById("authFailToLoginFrame") == null){
      window.location.reload();
    }else{
      setTimeout(function () {
        checkFrameLogin();
      }, 300);
    }
  }
</script>
</body>
</html>