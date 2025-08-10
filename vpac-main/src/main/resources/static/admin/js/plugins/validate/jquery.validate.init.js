(function ($, validator) {
  if(validator){
    validator.defaults = $.validator.defaults ? $.validator.defaults : {};
    validator.defaults.errorPlacement = function (error, element) {
      var context = element.parents(".valid-group:eq(0)");
      if(context.length === 0){
        context = element.parents(".form-group:eq(0)");
      }
      var label = context.find(".control-label");
      if(label.length > 0){
        label.after(error);
      }else{
        element.after(error);
      }
    };
  }
})(jQuery, jQuery.validator);