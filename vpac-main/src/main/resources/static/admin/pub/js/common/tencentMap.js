var tencentMap = {
  geocoder: null,
  // markList: [],
  marker: null,
  map: null,
  resetZoom:10,
  detailZoom:15,
  //GEO信息加载完成事件
  geoCompleteEvent:null,
  //点一次地图就直接查询GEO信息
  queryGeo:false,
  /** 初始化 */
  loadScript: function () {
    var script = document.createElement("script");
    script.type = "text/javascript";
    script.src = "https://map.qq.com/api/js?v=2.exp&key=3LYBZ-MVWKO-YXWWM-SRO5U-XLKPJ-QQB6I&callback=tencentMapInit";
    document.body.appendChild(script);
  },
  codeAddress: function () {
    var address = document.getElementById("mapAddressSearch").value;
    //通过getLocation();方法获取位置信息值
    tencentMap.geocoder.getLocation(address);
  },
  resetMap: function () {
    tencentMap.map.panTo(new qq.maps.LatLng(29.560019,106.585579));
    tencentMap.deleteOverlays();
    tencentMap.map.setZoom(tencentMap.resetZoom);
  },
  deleteOverlays: function() {
    if (tencentMap.marker) {
      if(tencentMap.marker.addressInfo){
        tencentMap.marker.addressInfo.close();
      }
      tencentMap.marker.setMap(null);
    }
    tencentMap.marker = null;
  },
  setLatAndLng: function (lat, lng) {
    $('#latitude').val(lat);
    $('#longitude').val(lng);
  },
  mapPositioning: function () {
    // var myLatlng = new qq.maps.LatLng($('#latitude').val(),$('#longitude').val());
    tencentMap.addMarker(parseFloat($('#latitude').val()),parseFloat($('#longitude').val()));
    // 设置地图显示级别
    tencentMap.map.setZoom(tencentMap.detailZoom);
  },
  addMarker: function (lat, lng) {
    tencentMap.deleteOverlays();
    tencentMap.setLatAndLng(lat, lng);
    var position = new qq.maps.LatLng(lat, lng);
    tencentMap.map.panTo(position);
    tencentMap.marker = new qq.maps.Marker({
      position: position,
      map: tencentMap.map,
      title:"点击查看详细地址"
    });
    tencentMap.marker.locationMarker = true;
    qq.maps.event.addListener(tencentMap.marker, 'click', function() {
      tencentMap.geocoder.getAddress(position);
    });
    if(tencentMap.queryGeo){
      tencentMap.geocoder.getAddress(position);
    }
  },
  addAddressMarker: function (result) {
    // var openNow = false;
    // if(tencentMap.marker && tencentMap.marker.locationMarker){
    //   openNow = true;
    // }
    tencentMap.deleteOverlays();
    tencentMap.setLatAndLng(result.detail.location.lat, result.detail.location.lng);
    tencentMap.map.panTo(result.detail.location);
    tencentMap.marker = new qq.maps.Marker({
      position: result.detail.location,
      map: tencentMap.map,
      title:"点击查看详细地址"
    });
    tencentMap.marker.addressInfo = new qq.maps.InfoWindow({
      map: tencentMap.map,
      position: result.detail.location,
      content: tencentMap.buildAddress(result.detail)
    });
    qq.maps.event.addListener(tencentMap.marker, 'click', function() {
      tencentMap.marker.addressInfo.open();
    });
    // if(openNow){
    //   tencentMap.marker.addressInfo.open();
    // }
  },
  buildAddress:function(detail){
    // var ad = detail.address;
    var adc =
      // orBlank(detail.addressComponents.country) +
      orBlank(detail.addressComponents.province) +
      orBlank(detail.addressComponents.city) +
      orBlank(detail.addressComponents.district) +
      orBlank(detail.addressComponents.town) +
      orBlank(detail.addressComponents.village) +
      orBlank(detail.addressComponents.street) +
      orBlank(detail.addressComponents.streetNumber);
    // if(ad === adc){
    //   return '<div>'+ ad + '</div>';
    // }else{
    //   return '<div>'+ ad + '</div><div>'+ adc + '</div>';
    // }
    return adc;
  }
};

// $(function () {
//   tencentMap.loadScript();
// });
function tencentMapInit() {
  var myLatlng = new qq.maps.LatLng(29.560019,106.585579);
  var myOptions = {
    zoom: tencentMap.resetZoom,
    center: myLatlng,
    mapTypeId: qq.maps.MapTypeId.ROADMAP
  };
  tencentMap.map = new qq.maps.Map(document.getElementById("container"), myOptions);

  //绑定单击事件添加参数
  qq.maps.event.addListener(tencentMap.map, 'click', function(event) {
    tencentMap.addMarker(event.latLng.getLat(),event.latLng.getLng());
  });

  tencentMap.geocoder = new qq.maps.Geocoder({
    complete : function(result){
      tencentMap.addAddressMarker(result);
      if($.type(tencentMap.geoCompleteEvent) === 'function'){
        tencentMap.geoCompleteEvent(result.detail);
      }
    }
  });
  $('#mapAddressSearch').keydown(function(e) {
    if (e.keyCode === 13) {
      tencentMap.codeAddress();
    }
  });
}
function orBlank(s) {
  return s ? s : '';
}