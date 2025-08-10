
(function ($) {
  $(function(){
    $(".g-btn").on("mousemove", function (e) {
      var _this = $(this);
      _this.find(".g-btn_background").css({'left':(e.pageX - _this.offset().left) + 'px', 'top': (e.pageY - _this.offset().top)+'px'});
    });
  });
})(jQuery);
