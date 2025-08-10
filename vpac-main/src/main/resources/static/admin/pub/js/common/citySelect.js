/*
    <div class="form-group">
      <label for="categoryName" class="control-label">所属地区：</label>
      <div class="row col-xs-12 city-select-context">
        <div  class="inline">
          <select class="form-control city-select-provincial" name="xxx">
          </select>
        </div>&nbsp;
        <div class="inline">
          <select class="form-control city-select-city" name="xxx">
          </select>
        </div>&nbsp;
        <div class="inline">
          <select class="form-control city-select-district" name="xxx" required>
          </select>
        </div>
        <div class="inline">
          <select class="form-control city-select-tiny" name="xxx">
          </select>
        </div>
      </div>
    </div>

    //修改时，选中
    citySelect.selectVal(select, valueId);
 */
var citySelect = {
  defaultSelectValues: [],
  data: null,
  initText:{'city-select-provincial':'- 选择省级 -', 'city-select-city':'- 选择市级 -', 'city-select-district':'- 选择区级 -', 'city-select-tiny':'- 选择地区 -'},
  mappingPre:{'city-select-city':'city-select-provincial', 'city-select-district':'city-select-city', 'city-select-tiny':'city-select-district'},
  mappingNext:{'city-select-provincial':'city-select-city', 'city-select-city':'city-select-district', 'city-select-district':'city-select-tiny'},
  'city-select-provincial': [],
  'city-select-city': [],
  'city-select-district': [],
  'city-select-tiny': [],
  selectVal: function (select, selectVal) {
    var $this = $(select);
    var context = $this.parents(".city-select-context");
    var level = _citySelectFindClass($this);
    if(level){
      var data = [];
      var current = level;
      var id = selectVal;
      for(;;){
        var arr = citySelect[current].filter(function (e) {
          return e.id === id;
        });
        var pre = citySelect.mappingPre[current];
        if(arr[0]){
          data.push({name:current, arr:arr});
          id = arr[0].parentId;
        }
        if(pre){
          current = pre;
        }else{
          break;
        }
      }
      data.reverse();
      for(var i in data){
        var f = data[i];
        context.find("." + f.name).val(!f.arr[0]?null:f.arr[0].id).trigger("change");
      }
    }
  },
  resetVal:function(context){
    var ctx = $(context);
    if(ctx){
      var selects = ctx.find('.city-select-city,.city-select-district,.city-select-tiny');
      _citySelectInitSelect(selects);
      if(citySelect.defaultSelectValues.length > 0){
        for(var i in citySelect.defaultSelectValues){
          var obj = citySelect.defaultSelectValues[i];
          citySelect.selectVal($(obj.select), obj.value);
        }
      }
    }
  },
  /** defaultSelectValue = {select:'#id',value:'xxx'} 或数组 */
  init:function (defaultSelectValue, finishEventFunc) {
    if(defaultSelectValue){
      if(defaultSelectValue instanceof Array){
        for(var i in defaultSelectValue){
          citySelect.defaultSelectValues.push(defaultSelectValue[i]);
        }
      }else{
        citySelect.defaultSelectValues.push(defaultSelectValue);
      }
    }
    var provincial = $('.city-select-provincial').change(function () {
      var $this = $(this);
      var context = $this.parents(".city-select-context");
      _citySelectSelectChange($this, context.find('.city-select-city,.city-select-district,.city-select-tiny'));
    });
    var city = $('.city-select-city').change(function () {
      var $this = $(this);
      var context = $this.parents(".city-select-context");
      _citySelectSelectChange($this, context.find('.city-select-district,.city-select-tiny'));
    });
    var district = $('.city-select-district').change(function () {
      var $this = $(this);
      var context = $this.parents(".city-select-context");
      _citySelectSelectChange($this, context.find('.city-select-tiny'));
    });
    var selects = $(provincial, city, district);
    _citySelectInitSelect(selects);
    $.get("/admin/region/all", function (rs) {
      if (rs.success) {
        citySelect.data = rs.data;
        _citySelectAppendOption(citySelect.data.filter(function (e) {
          return e.parentId === 'ROOT';
        }), provincial);
        _citySelectInitLevel();
        if (finishEventFunc) {
          finishEventFunc();
        }
        if(citySelect.defaultSelectValues.length > 0){
          for(var i in citySelect.defaultSelectValues){
            var obj = citySelect.defaultSelectValues[i];
            citySelect.selectVal($(obj.select), obj.value);
          }
        }
      } else {
        console.log("地区选择器初始化失败");
      }
    });
    $.get("/admin/region/tiny/all", function (rs) {
      if (rs.success) {
        citySelect['city-select-tiny'] = rs.data;
      } else {
        console.log("地区选择器初始化失败");
      }
    });
  }
};

function _citySelectInitLevel() {
  citySelect['city-select-provincial'] = citySelect.data.filter(function (e) {
    return e.parentId === 'ROOT';
  });
  citySelect['city-select-city'] = citySelect.data.filter(function (e) {
    var provincial = citySelect['city-select-provincial'];
    for(var i in provincial){
      if (provincial[i].id === e.parentId) {
        return true;
      }
    }
    return false;
  });
  citySelect['city-select-district'] = citySelect.data.filter(function (e) {
    var city = citySelect['city-select-city'];
    for(var i in city){
      if (city[i].id === e.parentId) {
        return true;
      }
    }
    return false;
  });
}
function _citySelectAppendOption(list, selects) {
  _citySelectInitSelect(selects);
  for (var i in list){
    selects.append('<option value="'+list[i].id+'">'+list[i].name+'</option>');
  }
}
function _citySelectInitSelect(selects) {
  selects.each(function(){
    var $this = $(this);
    var t = citySelect.initText[_citySelectFindClass($this)];
    $this.html('<option value="">'+(t?t:'- 请选择 -')+'</option>');
  });
}
function _citySelectSelectChange(event, _citySelectInitSelects){
  var id = event.val();
  var level = _citySelectFindClass(event);
  if(level){
    var ops = citySelect[citySelect.mappingNext[level]].filter(function (e) {
      return e.parentId === id;
    });
    _citySelectInitSelect(_citySelectInitSelects);
    _citySelectAppendOption(ops, _citySelectInitSelects.eq(0));
  }
}

function _citySelectFindClass(select) {
  var cl = select.attr("class").split(" ");
  for(var i in cl){
    var c = cl[i];
    if(c.indexOf("city-select-") >= 0){
      return c;
    }
  }
  return null;
}