// var $parentNode = window.parent.document;

// function $childNode(name) {
//     return window.frames[name]
// }

// // tooltips
// $('.tooltip-demo').tooltip({
//     selector: "[data-toggle=tooltip]",
//     container: "body"
// });

// // 使用animation.css修改Bootstrap Modal
// $('.modal').appendTo("body");

// $("[data-toggle=popover]").popover();


//判断当前页面是否在iframe中
if (top == this && location.pathname.indexOf("login") < 0) {
    var gohome = '<div class="gohome"><a class="animated bounceInUp" href="/admin/index" title="返回首页"><i class="fa fa-home"></i></a></div>';
    $('body').append(gohome);
}

//animation.css
function animationHover(element, animation) {
    element = $(element);
    element.hover(
        function () {
            element.addClass('animated ' + animation);
        },
        function () {
            //动画完成之前移除class
            window.setTimeout(function () {
                element.removeClass('animated ' + animation);
            }, 2000);
        });
}

//拖动面板
function WinMove() {
    var element = "[class*=col]";
    var handle = ".ibox-title";
    var connect = "[class*=col]";
    $(element).sortable({
            handle: handle,
            connectWith: connect,
            tolerance: 'pointer',
            forcePlaceholderSize: true,
            opacity: 0.8,
        })
        .disableSelection();
};
var toNum = function (value, scale) {
  var v = value * 1;
  var s = isNaN(scale) || scale < 0 ? 2 : scale;
  try {
    v = (v).toFixed(s);
  } catch (e) {
  }
  return isNaN(v) ? (0).toFixed(s) : v;
};
var getQueryValue = function (key) {
  var query = window.location.search.substring(1);
  var vars = query.split("&");
  for (var i = 0; i < vars.length; i++) {
    var pair = vars[i].split("=");
    if (pair.length === 2 && pair[0] === key) {
      return window.decodeURIComponent(pair[1]);
    }
  }
  return '';
}
Date.prototype.format = function (format) //author: meizz
{
  var o = {
    "M+": this.getMonth() + 1, //month
    "d+": this.getDate(),    //day
    "H+": this.getHours(),   //hour
    "m+": this.getMinutes(), //minute
    "s+": this.getSeconds(), //second
    "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
    "S": this.getMilliseconds() //millisecond
  }
  if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
    (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o) if (new RegExp("(" + k + ")").test(format))
    format = format.replace(RegExp.$1,
      RegExp.$1.length == 1 ? o[k] :
        ("00" + o[k]).substr(("" + o[k]).length));
  return format;
}
$(function(){
  $(window).keydown(function (e) {
    if (e.keyCode === 116) {
      if (window.event.ctrlKey) {
        return true;
      }
      window.location.reload();
      return false;
    }
    return true;
  })
});
