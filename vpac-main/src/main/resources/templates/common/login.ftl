<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <title>${systemName}</title>
  <meta name="keywords" content="">
  <meta name="description" content="">
  <link rel="shortcut icon" href="${ctx!}/admin/favicon.ico">
  <link href="${ctx!}/admin/css/bootstrap.min.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/font-awesome.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/animate.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/style.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/login.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/gradient-theme.css" rel="stylesheet">
  <!--[if lt IE 9]>
  <meta http-equiv="refresh" content="0;ie.html"/>
  <![endif]-->
  <script>
    <#-- 当前窗口 未用参数标识为顶层窗口（topWindow=1），且不是顶层时，强制刷新顶层窗口location = 当前窗口location -->
    if ("${topWindow}" !== "1" && window.top !== window.self) {
      window.top.location = window.location;
    }
  </script>
</head>

<body class="signin" style="height:100%">
<div id="canvas" class="gradient"></div>
<div class="form-top"></div>
<div class="signinpanel">
  <div class="row">
    <form method="post" action="${ctx!}/admin/login" id="frm" class="shadow" style="border-radius:15px;" onsubmit="return false;">
      <input type="hidden" name="topWindow" value="${topWindow}" />
      <div class="text-center m-b-lg title-div" style="background:#ffffff00 !important;border-bottom: 1px solid #abababcc;">
        ${loginTitle}
      </div>
      <div class="login-group m-t-md">
        <label class="uname"></label>
        <input style="display:none">
        <input type="text" class="form-control" name="username" id="username" placeholder="用户名" autocomplete="off"/>
      </div>
      <div class="login-group">
        <label class="pword"></label>
        <input style="display:none">
        <input type="password" class="form-control" name="password" id="password" placeholder="密码" autocomplete="off"/>
      </div>
      <div class="login-group" style="padding-left: 25px;display:none" id="captchaDiv">
        <input style="width: calc(90% - 110px);" type="text" class="form-control" name="captchaCode" id="captchaCode"
               placeholder="验证码" autocomplete="off"/>
        <img title="看不清？点击换一张" style="height:45px;width:130px;margin-top:-14px;cursor:pointer" id="captchaImg" />
        <div>
          <label id="captchaCode-error" class="error" for="captchaCode"></label>
        </div>
        <input name="captchaPreCode" type="hidden" id="captchaPreCode" value="${captchaPreCode}"/>
      </div>
      <button onclick="login.loginSubmit();return false;" id="submit_button"
        class="subscribe_cta g-btn g-btn-primary g-btn-centred g-btn-fullWidth m-t-sm">
        <div class="g-btn_wrap">
          <div class="t-shadow m-t-n-xxs" style="z-index:1">登&nbsp;&nbsp;&nbsp;&nbsp;录</div>
          <div class="g-btn_background"></div>
        </div>
      </button>

    </form>


  </div>
<#--<div class="signup-footer">-->
<#--<div class="pull-left">-->
<#--</div>-->
<#--</div>-->
<input style="display: none" type="checkbox" checked id="rememberMe" />
</div>
<div id="footer" class="t-shadow">
${copyright} <a href="https://beian.miit.gov.cn" target="_blank" class="m-l-sm">渝ICP备2022014319号-1</a>
</div>
<!-- 全局js -->
<script src="${ctx!}/admin/js/jquery.min.js"></script>
<script src="${ctx!}/admin/js/plugins/jquery-treegrid-master/js/jquery.cookie.js"></script>
<script src="${ctx!}/admin/js/theme.js"></script>
<script src="${ctx!}/admin/js/bootstrap.min.js"></script>

<!-- 自定义js -->
<script src="${ctx!}/admin/js/content.js"></script>
<script src="${ctx!}/admin/js/jquery.payload.js"></script>

<!-- jQuery Validation plugin javascript-->
<script src="${ctx!}/admin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${ctx!}/admin/js/plugins/validate/messages_zh.min.js"></script>
<script src="${ctx!}/admin/js/plugins/layer/layer.min.js"></script>
<script src="${ctx!}/admin/js/plugins/layer/extend/layer.ext.js"></script>
<script src="${ctx!}/admin/js/gradient-button.js"></script>
<script type="text/javascript">
  var captchaPreCode = '${captchaPreCode}';
  $(function () {
    if(captchaPreCode){
      $("#captchaDiv").show();
      $("#captchaImg").on("click", function () {
        this.src = "/api/edge/captcha/normal?type=0&captchaPreCode="+captchaPreCode + "&random=" + Math.random();
      }).trigger("click");
    }
    else{
      $("#captchaDiv").hide();
    }
    $("#username").val($.cookie("rememberMeA"));
    $("#password").val($.cookie("rememberMeP"));
    // 在键盘按下并释放及提交后验证提交表单
    $("#frm").validate({
      rules: {
        username: {
          required: true,
          minlength: 2
        },
        password: {
          required: true,
          minlength: 5
        },
        captchaCode: {
          required: true
        }
      },
      messages: {
        username: {
          required: "请输入用户名",
          minlength: "用户名必需由两个字母组成"
        },
        password: {
          required: "请输入密码",
          minlength: "密码长度不能小于 6 个字母"
        },
        captchaCode: {
          required: "请输入验证码"
        }
      },
      submitHandler: function (form) {
        if(!login.waiting){
          login.waiting = true;
          if(login.isEnter){
            login.fire();
            setTimeout(function () {
              form.submit();
              login.isEnter = false;
            }, 350);
          }else{
            $("#submit_button").attr("disabled", true);
            form.submit();
          }
        }
      }
    });
    var errorMsg = '${errorMsg!}';
    if (errorMsg) {
      layer.alert(errorMsg, {icon: 2});
    }else{
      $("#captchaCode").focus();
    }
    document.onkeydown = function (event) {
      var e = event || window.event || arguments.callee.caller.arguments[0];
      if (e && e.keyCode === 13) { // enter
        layer.close(1);
        if (errorMsg) {
          errorMsg = null;
          $("#username").focus();
        }
      }
    };

    $(window).keydown(function (e) {
      if (e.keyCode === 13) {
        if(!login.isEnter){
          login.isEnter = true;
          login.loginSubmit();
        }
      }
    });
    $("#keepPwdCheckBox").css({color:$(".g-btn_wrap").css("background-color")});
    $("#keepPwd,#keepPwdCheckBox").on("click", function () {
      var el = $("#rememberMe")[0];
      if(el.checked){
        el.checked = false;
        $("#keepPwdCheckBox i").attr("class", "fa fa-square");
      }
      else{
        el.checked = true;
        $("#keepPwdCheckBox i").attr("class", "fa fa-check-square");
      }
    });
  });
  var login = {
    isEnter:false,
    waiting:false,
    loginSubmit:function () {
      var rememberMe = $("#rememberMe").get(0).checked;
      if(rememberMe){
        $.cookie("rememberMeA", $("#username").val(), {expires: 30, path:'/admin'});
        $.cookie("rememberMeP", $("#password").val(), {expires: 30, path:'/admin'});
      }
      $('#frm').submit();
    },
    fire:function () {
      var btn = $("#submit_button");
      btn.find(".g-btn_background").css(
        {"z-index":0,"width":"225%","height":"800px", "top":btn.height()/2 + "px","left": btn.width()/2 + "px"});
    }
  };
</script>
<#-- 每天更换 -->
<#if (.now?string("d")?number) % 2 == 0>
  <script src="${ctx!}/admin/pub/js/common/login/three.min.js"></script>
  <script src="${ctx!}/admin/pub/js/common/login/projector.js"></script>
  <script src="${ctx!}/admin/pub/js/common/login/canvas-renderer.js"></script>
  <script src="${ctx!}/admin/pub/js/common/login/3d-lines-animation.js"></script>
<#else>
  <script src="${ctx!}/admin/pub/js/common/login/RibbonsEffect.js"></script>
</#if>
</body>

</html>
