<div>
  <div class="input-group m-b-xs">
    <input type="text" id="mapAddressSearch" class="form-control" placeholder="地址搜索；例：中国重庆市渝中区解放碑" >
    <span class="input-group-btn">
        <button class="btn btn-default" type="button" onclick="tencentMap.codeAddress();return false;" style="font-size:17px"><i class="fa fa-search"></i></button>
      </span>
  </div>
</div>
<div id="container" style="height:300px"></div>
<input type="hidden" class="form-control" id="latitude" name="latitude" required/>
<input type="hidden" class="form-control" id="longitude" name="longitude" required/>