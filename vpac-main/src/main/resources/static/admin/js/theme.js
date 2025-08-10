(function(window, $){
  $(function(){
    var theme = {
      chooseThemeSkin:function(skin){
        $(".theme-skin label").each(function(){
          var $this = $(this).empty();
          if($this.data("id") === skin){
            $this.append('<i class="fa fa-check-square"></i>');
          }else{
            $this.append('<i class="fa fa-square"></i>');
          }
        });
        $.cookie("theme_skin", skin, {expires: 7, path:'/admin'}); //7天
        var body = $("body");
        var bc = body.attr("class");
        var arr = bc ? body.attr("class").split(" ") : [];
        for(var i = 0 ; i < arr.length; i++){
          var v = arr[i];
          if(v.indexOf("theme-skin-") >=0){
            delete arr[i];
            break;
          }
        }
        body.attr("class", arr.join(" ")).addClass("theme-skin-" + skin);
      }
    };
    var skin = $.cookie("theme_skin");
    theme.chooseThemeSkin(skin ? skin : "blue");
    $(".theme-skin label").on("click", function () {
      theme.chooseThemeSkin($(this).data("id"));
    });
    if(window.parent !== window){
      $("body").addClass("child");
    }else{
      $("body").addClass("parent");
    }
  });
})(window, jQuery);