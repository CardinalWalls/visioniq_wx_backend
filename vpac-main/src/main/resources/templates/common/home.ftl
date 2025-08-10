<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<script type="application/javascript" src="${ctx!}/admin/pub/js/common/static_theme.js"></script>
<@headContent/>

<@bodyContent>
  <div class="wrapper wrapper-content animated fadeInRight">
    <div class="text-center ibox" style="margin-top:-25px;">
      <h3>
        <img style="height:60px;vertical-align:top;margin-right:10px;" src="${ctx!}/admin/img/logo.png" onerror="this.remove()"/>
        <label style="font-size:33px;margin-top:12px;color:#000;">${systemName}</label>
      </h3>
    </div>
  </div>
</@bodyContent>