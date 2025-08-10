<#macro appTree app>
  <li>
    <#if app.type == 0 && app.children?? && app.children?size gt 0>
      <a href="javascript:void(0)" title="${app.description}">
        <i class="${app.iconClass}"></i>
        <span class="nav-label">${app.name}</span><span class="fa arrow"></span>
      </a>
    </#if>
    <#if app.type == 1>
      <a class="J_menuItem" href="${ctx!}${app.targetUrl}"
         id="${app.id}"><i class="${app.iconClass}"></i>
        <span class="nav-label">${app.name}</span>
      </a>
    </#if>
    <#if app.children?? && app.children?size gt 0>
      <ul class="nav nav-second-level">
        <#list app.children as child>
            <@appTree app=child />
        </#list>
      </ul>
    </#if>
  </li>
</#macro>