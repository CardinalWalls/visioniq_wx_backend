(function($){
  if($){
    $.postPayload = function(url, jsonData, success, error, returnType){
      $.payload(url, "POST", jsonData, success, error, returnType);
    };
    $.putPayload = function(url, jsonData, success, error, returnType){
      $.payload(url, "PUT", jsonData, success, error, returnType);
    };
    $.deletePayload = function(url, jsonData, success, error, returnType){
      $.payload(url, "DELETE", jsonData, success, error, returnType);
    };
    $.payload = function(url, method, jsonData, success, error, returnType){
      var data = (jsonData ? ($.type(jsonData) === "object" ? JSON.stringify(jsonData) : jsonData.toString()) : "{}");
      $.ajax(url, {
        method : method,
        contentType:'application/json;charset=utf-8',
        data: data,
        dataType: (returnType ? returnType : "json"),
        success : function (rs) {
          if(success){
            success(rs);
          }
        },
        error: function (e) {
          if(error){
            error(e);
          }
        }
      });
    };
    $(function () {
      $("form").each(function(){
        var $this = $(this);
        this.resetNative = this.reset;
        this.reset = function(){
          this.resetNative();
          $this.find("input:hidden").each(function () {
            var input = $(this);
            //表单初始值
            var initVal = input.attr("initValue");
            if(initVal === undefined){
              input.val("");
            }else{
              input.val(initVal);
            }
          });
          $this.trigger("reseted");
        }
      });
    });
  }
})(window.jQuery);