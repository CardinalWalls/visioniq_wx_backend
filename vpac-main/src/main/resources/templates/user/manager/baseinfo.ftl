<div style="">

<#-- 个人信息 -->
  <div style="width: 450px;height: auto;display:table;margin: 0px auto">
    <div class="col-xs-12 form-group" style="text-align: center">
      <img alt="头像" class="img-circle" id="avatar"
           style="width: 80px;height: 80px;"
           src="<#if userBaseInfo.avatar!=null>${userBaseInfo.avatar}<#elseif userBaseInfo.wxImg!=null>${userBaseInfo.wxImg}<#else>/admin/img/avatar.jpg</#if>">
    </div>
    <div class="col-xs-12 form-group">
      <div>
        <label class="col-xs-4 control-label" style="text-align: right">姓名/昵称：</label>
        <div class="col-xs-8" style="text-align: left">${userBaseInfo.userNickName}</div>
      </div>
    </div>
    <div class="col-xs-12 form-group">
      <div>
        <label class="col-xs-4 control-label" style="text-align: right">性别：</label>
        <div class="col-xs-8" style="text-align: left">
          <#if userBaseInfo.gender==1>男
          <#elseif userBaseInfo.gender==2>女
          <#else>未知
          </#if>
        </div>
      </div>
    </div>
    <div class="col-xs-12 form-group">
      <div>
        <label class="col-xs-4 control-label" style="text-align: right">生日：</label>
        <div class="col-xs-8" style="text-align: left">${userBaseInfo.birth}</div>
      </div>
    </div>
    <div class="col-xs-12 form-group">
      <div>
        <label class="col-xs-4 control-label" style="text-align: right">手机号码：</label>
        <div class="col-xs-8" style="text-align: left">${userBaseInfo.phone}</div>
      </div>
    </div>
    <div class="col-xs-12 form-group">
      <div>
        <label class="col-xs-4 control-label" style="text-align: right">电子邮箱：</label>
        <div class="col-xs-8" style="text-align: left">${userBaseInfo.userEmail}</div>
      </div>
    </div>
  </div>

</div>