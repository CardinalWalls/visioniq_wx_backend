<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="renderer" content="webkit">
  <title>${systemName}</title>
  <meta name="keywords" content="">
  <meta name="description" content="">
  <!--[if lt IE 9]>
  <meta http-equiv="refresh" content="0;ie.html"/>
  <![endif]-->
  <link rel="shortcut icon" href="${ctx!}/admin/favicon.ico">
  <link href="${ctx!}/admin/css/bootstrap.min.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/font-awesome.min.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/animate.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/style.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/index.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/theme.css" rel="stylesheet">
  <link href="${ctx!}/admin/css/plugins/mousewheel/jquery.mCustomScrollbar.min.css" rel="stylesheet">
  <script type="text/javascript">
    var ctx = "${ctx!}";
    <#-- 如果当前窗口不是顶层窗口，则为临时登录iframe登录成功的窗口，将本窗口iframe（authFailToLoginFrame）删除 -->
    if (window.top !== window.self) {
      window.top.document.getElementById("authFailToLoginFrame").remove();
    }
  </script>
</head>

<body class="fixed-sidebar full-height-layout parent">
<div id="_parent_cover_"></div>
<div id="wrapper">
  <!--左侧导航开始-->
  <nav class="navbar-default navbar-static-side" role="navigation">
    <div class="nav-close"><i class="fa fa-times-circle"></i>
    </div>
    <div class="sidebar-collapse">
      <ul class="nav" id="side-menu" style="display:none">
<#--        <li class="logo-slogan-context">-->
<#--          <div class="logo-slogan">-->
<#--            <h4>${systemName}</h4>-->
<#--          </div>-->
<#--          <div class="logo-slogan-min">-->
<#--            <img style="max-height:39px;max-width:68px" src="/api/oss/redirect?file=${logo}" onerror="this.src='/admin/img/logo.png'"/>-->
<#--          </div>-->
<#--        </li>-->
        <li class="nav-header">
          <div class="dropdown profile-element">
            <a data-toggle="dropdown" class="dropdown-toggle" href="#" style="background:none" id="_user_info_toggle_">
              <span class="font-bold text-fff">
                <i class="fa fa-user user-i"></i>
                <span id="index_member_name" class="m-l-xs">${memberToken.memberName!memberToken.account}</span>　
                <b class="caret"></b>
               </span>
            </a>
            <ul class="dropdown-menu fadeInRight m-t-xs text-black shadow" style="width:218px;left:-18px">
              <li><a class="J_menuItem" href="${ctx!}/admin/information">
                <i class="fa fa-edit"></i>
                修改资料</a>
              </li>
              <li class="divider"></li>
              <li><a href="/admin/logout" onclick="$.cookie('_opened_tabs_', '')">
                <i class="fa fa-power-off"></i>
                安全退出</a>
              </li>
            </ul>
          </div>
        </li>
        <li class="line dk"></li>
        <li>
          <a href='javascript:$("#content-nav a:first").tab("show");index.tabScroll("0px")'>
            <i class="fa fa-home"></i>
            <span class="nav-label">主 页</span>
          </a>
        </li>
        <li class="line dk"></li>
        <#include "${_ROOT_PATH_}/macro/apptree.ftl">
        <#list appMenu as app>
          <@appTree app=app />
        </#list>
      </ul>
    </div>
  </nav>
  <!--左侧导航结束-->
  <!--右侧部分开始-->
  <div id="page-wrapper" class="dashbard-1">
    <div class="nav-top navbar-static-top">
      <table>
        <tr>
          <th>
            <div class="navbar-minimalize-div">
              <button class="navbar-minimalize btn btn-white resize-btn" style="background-color:#FFF0" href="#"><i class="fa fa-bars"></i></button>
            </div>
          </th>
          <th>
            <div id="nav_tab_scroll">
              <ul class="nav nav-tabs" role="tablist" id="content-nav">
                <li data-href='${ctx!}/admin/home'
                    role="presentation" class="m-l-sm active" onclick="index.clickTab(this);index.homeOnClick();" id="_nav_home_tab_" >
                  <a href="#tab_home" aria-controls="tab_home" role="tab" data-toggle="tab"><i
                    class="fa fa-home"></i>主 页</a>
                  <button type='button' class='close main-if-refresh' onclick='index.refreshWindow("tab_home")'><i
                    class='fa fa-refresh'></i></button>
                </li>
              </ul>
            </div>
          </th>
          <th>
            <div class="nav-opt-div">
              <button title="全屏显示" class="btn btn-white resize-btn" style="background-color:#FFF0" onclick="$('body').fullScreen();"><i
                class="fa fa-arrows-alt"></i></button>
              <div class="dropdown inline">
                <a class="btn btn-white resize-btn" style="background-color:#FFF0" onclick="$(this).parent().addClass('open')">
                  <span class="font-bold">
                    <i class="fa fa-cog"></i>
                  </span>
                </a>
                <ul class="dropdown-menu fadeInRight text-black text-center shadow" onmouseleave="$(this).parent().removeClass('open')"
                    style="margin-left:-49px">
                  <li class="text-center">
                    <div>主题颜色</div>
                    <div class="theme-skin">
                      <label data-id="vip" style="color:#CDA992"></label>
                      <label data-id="purple" style="color:#b096fd"></label>
                      <label data-id="blue" style="color:#00ADFF"></label>
                      <label data-id="pink" style="color:#f32d8d"></label>
                      <label data-id="green" style="color:#00e85f"></label>
<#--                      <label data-id="orange" style="color:#ff9838"></label>-->
                    </div>
                  </li>
<#--                  <li class="text-center">-->
<#--                    <div>-->
<#--                      <label class="top-system-info-toggle"><i class="fa fa-toggle-on"></i> 菜单栏顶部信息</label>-->
<#--                    </div>-->
<#--                  </li>-->
                </ul>
              </div>
              <div class="dropdown inline">
                <a data-toggle="dropdown" class="dropdown-toggle btn btn-white resize-btn" style="background:none;margin:6px 0">
                  <span class="font-bold">
                    <i class="fa fa-user user-i"></i>
                  </span>
                </a>
                <ul class="dropdown-menu fadeInRight text-black shadow" onmouseleave="$(this).parent().removeClass('open')"
                    style="margin-left:-99px">
                  <li>
                    <a class="J_menuItem" href="${ctx!}/admin/information" style="text-align:center">
                      <i class="fa fa-edit"></i>
                      <b>修改资料</b>
                    </a>
                  </li>
                  <li>
                    <a href="/admin/logout" style="text-align:center" onclick="$.cookie('_opened_tabs_', '')">
                      <i class="fa fa-power-off"></i>
                      <b>安全退出</b>
                    </a>
                  </li>
                </ul>
              </div>
            </div>
          </th>
        </tr>
      </table>
    </div>
    <div class="row J_mainContent tab-content" id="content-main">
      <iframe src="${ctx!}/admin/home" onload="index.homeOnLoad()"
              role="tabpanel" class="tab-pane active" id="tab_home"
              width="100%" height="100%" frameborder="0" seamless></iframe>
    </div>
  </div>
  <!--右侧部分结束-->
  <!--右侧右键菜单开始-->
  <div id="context-menu">
    <ul class="dropdown-menu" role="menu" onmouseleave="$('#context-menu').removeClass('open')">
      <li><a tabindex="-1" class="dropdown-menu-other">关闭其它</a></li>
      <li><a tabindex="-1" class="dropdown-menu-all">关闭所有</a></li>
    </ul>
  </div>
  <!--右侧右键菜单结束-->
</div>
<!-- 全局js -->
<script src="${ctx!}/admin/js/jquery.min.js"></script>
<script src="${ctx!}/admin/js/plugins/jquery-treegrid-master/js/jquery.cookie.js"></script>
<script src="${ctx!}/admin/js/theme.js"></script>
<script src="${ctx!}/admin/js/bootstrap.min.js"></script>
<script src="${ctx!}/admin/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="${ctx!}/admin/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="${ctx!}/admin/js/plugins/layer/layer.min.js"></script>
<script src="${ctx!}/admin/js/bootstrap-contextmenu.js"></script>

<!-- 自定义js -->
<script src="${ctx!}/admin/js/index.js"></script>
<script src="${ctx!}/admin/js/jquery.payload.js"></script>
<script src="${ctx!}/admin/js/jquery.fullscreen.js"></script>
<script src="${ctx!}/admin/js/plugins/mousewheel/jquery.mCustomScrollbar.concat.min.js"></script>

<!-- 第三方插件 -->
<script src="${ctx!}/admin/js/plugins/pace/pace.min.js"></script>
</body>
</html>