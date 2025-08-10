/*
    // html元素
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
      </div>
    </div>


    //dom加载完毕后
    regionSelect.init(callback);
    //初始化元素
    regionSelect.build(select, defaultValue);
    //修改时，选中
    regionSelect.selectVal(select, valueId);
 */
var regionSelect = {
  initialized:false,
  defaultSelectValues: [],
  data: null,
  initText:{'city-select-provincial':'- 选择省级 -', 'city-select-city':'- 选择市级 -', 'city-select-district':'- 选择区级 -'},
  mappingPre:{'city-select-city':'city-select-provincial', 'city-select-district':'city-select-city'},
  mappingNext:{'city-select-provincial':'city-select-city', 'city-select-city':'city-select-district'},
  'city-select-provincial': [],
  'city-select-city': [],
  'city-select-district': [],
  getName:function (value, level){
    let arr = regionSelect[level === 1 ? "city-select-provincial" : (level === 2 ? "city-select-city" : "city-select-district")];
    for (let i = 0; i < arr.length; i++) {
      let r = arr[i];
      if(r.id === value){
        return r.name;
      }
    }
    return "";
  },
  checkInit:function (){
    if(!regionSelect.initialized){
      alert("regionSelect not initialized data yet");
    }
  },
  selectVal: function (select, selectVal) {
    regionSelect.checkInit();
    var $this = $(select);
    var context = $this.parents(".city-select-context");
    var level = _regionSelectFindClass($this);
    if(level){
      var data = [];
      var current = level;
      var id = selectVal;
      for(;;){
        var arr = regionSelect[current].filter(function (e) {
          return e.id === id;
        });
        var pre = regionSelect.mappingPre[current];
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
    regionSelect.checkInit();
    var ctx = $(context);
    if(ctx){
      var selects = ctx.find('.city-select-city,.city-select-district');
      _regionSelectInitSelect(selects);
      if(regionSelect.defaultSelectValues.length > 0){
        for(var i in regionSelect.defaultSelectValues){
          var obj = regionSelect.defaultSelectValues[i];
          regionSelect.selectVal($(obj.select), obj.value);
        }
      }
    }
  },
  /** 初始化数据 */
  initData:function (callback) {
    if(!regionSelect.initialized){
      $.ajax({
        type: 'GET',
        url: "/admin/region/all",
        dataType: 'json',
        async:false,
        success: function (rs){
          if (rs.success) {
            regionSelect.data = rs.data;
            _regionSelectInitLevel();
            regionSelect.initialized = true;
            if(callback){
              callback();
            }
          } else {
            console.log("地区选择器初始化失败");
          }
        },
        error: function (e){
          console.log("地区选择器初始化失败");
        }
      });
      // $.get("/admin/region/all", function (rs) {
      //   if (rs.success) {
      //     regionSelect.data = rs.data;
      //     _regionSelectInitLevel();
      //     regionSelect.initialized = true;
      //     if (finishEventFunc) {
      //       finishEventFunc();
      //     }
      //   } else {
      //     console.log("地区选择器初始化失败");
      //   }
      // });
    }
  },
  /** 初始化dom元素 */
  build:function (selectEl, defaultValue){
    regionSelect.checkInit();
    if(selectEl){
      var $this = $(selectEl);
      var context = $this.parents(".city-select-context");
      var provincial = context.find('.city-select-provincial').change(function () {
        var $this = $(this);
        _regionSelectSelectChange($this, context.find('.city-select-city,.city-select-district'));
      });
      var city = context.find('.city-select-city').change(function () {
        var $this = $(this);
        _regionSelectSelectChange($this, context.find('.city-select-district'));
      });
      var selects = $(provincial, city);
      _regionSelectInitSelect(selects);
      defaultValue = defaultValue === null || defaultValue === undefined ? "" : defaultValue;
      var contains = false;
      for(var i in regionSelect.defaultSelectValues){
        var obj = regionSelect.defaultSelectValues[i];
        if(obj === selectEl){
          contains = true;
          obj.value = defaultValue;
          break;
        }
      }
      if(!contains){
        _regionSelectAppendOption(regionSelect.data.filter(function (e) {
          return e.parentId === 'ROOT';
        }), provincial);
        _regionSelectInitLevel();
        regionSelect.defaultSelectValues.push({select: selectEl, value: defaultValue});
      }
      regionSelect.selectVal(selectEl, defaultValue);
    }
  },
};

function _regionSelectInitLevel() {
  regionSelect['city-select-provincial'] = regionSelect.data.filter(function (e) {
    return e.parentId === 'ROOT';
  });
  regionSelect['city-select-city'] = regionSelect.data.filter(function (e) {
    var provincial = regionSelect['city-select-provincial'];
    for(var i in provincial){
      if (provincial[i].id === e.parentId) {
        return true;
      }
    }
    return false;
  });
  regionSelect['city-select-district'] = regionSelect.data.filter(function (e) {
    var city = regionSelect['city-select-city'];
    for(var i in city){
      if (city[i].id === e.parentId) {
        return true;
      }
    }
    return false;
  });
}
function _regionSelectAppendOption(list, selects) {
  _regionSelectInitSelect(selects);
  for (var i in list){
    selects.append('<option value="'+list[i].id+'">'+list[i].name+'</option>');
  }
}
function _regionSelectInitSelect(selects) {
  selects.each(function(){
    var $this = $(this);
    var t = regionSelect.initText[_regionSelectFindClass($this)];
    $this.html('<option value="">'+(t?t:'- 请选择 -')+'</option>');
  });
}
function _regionSelectSelectChange(event, _regionSelectInitSelects){
  var id = event.val();
  var level = _regionSelectFindClass(event);
  if(level){
    var ops = regionSelect[regionSelect.mappingNext[level]].filter(function (e) {
      return e.parentId === id;
    });
    _regionSelectInitSelect(_regionSelectInitSelects);
    _regionSelectAppendOption(ops, _regionSelectInitSelects.eq(0));
  }
}

function _regionSelectFindClass(select) {
  var cl = select.attr("class").split(" ");
  for(var i in cl){
    var c = cl[i];
    if(c.indexOf("city-select-") >= 0){
      return c;
    }
  }
  return null;
}