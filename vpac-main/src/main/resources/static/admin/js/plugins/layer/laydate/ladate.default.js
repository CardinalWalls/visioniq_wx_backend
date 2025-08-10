(function ($) {
  if(laydate){
    $.defaultLayDate = function (startSelect, endSelect, type) {
      var start = {
        elem: startSelect,
        type: type,
        done: function (value, date, endDate) {
          var d = $.defaultLayDate.min;
          if (JSON.stringify(date) !== "{}") {
            d = {
              year: date.year,
              month: date.month - 1,
              date: date.date,
              hours: date.hours,
              minutes: date.minutes,
              seconds: date.seconds
            };
          }
          $end.config.min = d;
          $end.config.value = d;
        }
      };
      var end = {
        elem: endSelect,
        type: type,
        done: function (value, date, endDate) {
          var d = $.defaultLayDate.max;
          if (JSON.stringify(date) !== "{}") {
            d = {
              year: date.year,
              month: date.month - 1,
              date: date.date,
              hours: date.hours,
              minutes: date.minutes,
              seconds: date.seconds
            };
          }
          $start.config.max = d;
        }
      };
      var $start = laydate.render(start);
      var $end = laydate.render(end);
    };
  }else{
    $.defaultLayDate = function (startSelect, endSelect, type) {
    }
  }
  $.defaultLayDate.max = {
    year: 2099,
    month: 1,
    date: 1,
    hours: 1,
    minutes: 1,
    seconds: 1
  };
  $.defaultLayDate.min = {
    year: 1970,
    month: 1,
    date: 1,
    hours: 1,
    minutes: 1,
    seconds: 1
  };
})(jQuery, laydate);