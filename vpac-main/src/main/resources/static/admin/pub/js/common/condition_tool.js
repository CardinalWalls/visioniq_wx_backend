//依赖 /admin/pub/js/common/regionSelect.js 并提前调用 regionSelect.initData();
var conditionTool = {
  fields:[],
  conditionType:{
    "RISK_LEVEL": {
      operator:["="], valueHtml:function (){
        return `<select class="form-control" title="条件值" data-name="value"><option value="0">无</option><option value="1">低风险</option>
<option value="2">中风险</option><option value="3">高风险</option></select>`;
      },valueAlias:function (val){
        switch (val){
          case "1": return "低风险";
          case "2": return "中风险";
          case "3": return "高风险";
          default: return "无";
        }
      }
    },
    "GENDER": {
      operator:["="], valueHtml:function (){
        return `<select class="form-control" title="条件值" data-name="value"><option value="1">男</option><option value="2">女</option></select>`;
      },valueAlias:function (val){
        return val === "1" ? "男" : "女";
      }
    },
    "TEXT":{
      operator:["=","IN",">","<",">=","<=","!="], valueHtml:function (){
        return `<input class="form-control" type="text" data-name="value" title="条件值（匹配“包含”时，用英文逗号分隔多个值）"/>`;
      }
    },
    "INT":{
      operator:["=","IN",">","<",">=","<=","!="], valueHtml:function (){
        return `<input class="form-control" type="number" data-name="value" title="条件值（匹配“包含”时，用英文逗号分隔多个值）"/>`;
      }
    },
    "DOUBLE":{
      operator:["=","IN",">","<",">=","<=","!="], valueHtml:function (){
        return `<input class="form-control" type="number" data-name="value" title="条件值（匹配“包含”时，用英文逗号分隔多个值）"/>`;
      }
    },
    "REGION":{
      operator:["="], valueHtml:function (){
        return `<div class="city-select-context">
          <div class="inline">
            <select class="form-control city-select-provincial" name="provincialId" title="省" >
            </select>
          </div>
          <div class="inline">
            <select class="form-control city-select-city" name="cityId" title="市">
            </select>
          </div>
          <div class="inline">
            <select class="form-control city-select-district" name="districtId" title="区">
            </select>
          </div>
        </div>`;
      }, initAfter:function (conditionItem){
        regionSelect.build(conditionItem.find("select[name='districtId']")[0], "500103");
      }, loadValue:function (conditionItem){
        return conditionItem.find(".city-select-context .form-control").get().filter(function(e){return !!e.value})
          .map(function(e){return e.value}).join("-");
      }, setValue:function (conditionItem, val){
        let arr = val.split("-");
        let n = arr.length === 1 ? "provincialId" : (arr.length === 2 ? "cityId" : "districtId");
        regionSelect.selectVal(conditionItem.find(`.city-select-context .form-control[name='${n}']`)[0], arr[arr.length-1]);
      },valueAlias:function (val){
        let arr = val.split("-");
        let v = "";
        for (let i = 0; i < arr.length; i++) {
          v += (v===""?"":"-") + regionSelect.getName(arr[i], i+1);
        }
        return v;
      }
    }
  },
  initContext:function (parentDiv, fieldArray){
    if($.type(fieldArray) !== 'array'){
      alert("initContext error: $fieldArray could not null!")
    }
    parentDiv.html(`<div class="condition-context">
            <label class="control-label">过滤条件：</label>
            <button type="button" class="btn btn-info save-btn btn-xs" onclick="conditionTool.addConditionGroup($(this).parents('.condition-context'))" title="添加条件组">
              <i class="fa fa-plus"></i>
            </button>
            <label class="control-label m-l">条件组联结方式：</label>
            <select class="form-control groupJoinType allJoinType">
              <option value="AND" >且</option>
              <option value="OR" >或</option>
            </select>
            <div class="condition-groups">
            </div>
          </div>`);
    conditionTool.fields.push({context: parentDiv.find(".condition-context")[0],
      fieldOption: fieldArray.map(function (e){return `<option value="${e.column}" type="${e.type}">${e.name}</option>`;}).join("")});
  },
  resetCondition:function (context, conditionJson){
    context.find(".condition-groups").empty();
    if(conditionJson){
      context.find(".allJoinType").val(conditionJson.join);
      let groups = conditionJson.groups;
      for (let i = 0; i < groups.length; i++) {
        let group = groups[i];
        let groupEl = conditionTool.addConditionGroup(context, group.join);
        for (let j = 0; j < group.items.length; j++) {
          let item = group.items[j];
          conditionTool.addCondition(groupEl, item);
        }
      }
    }
  },
  loadConditionJson:function (context){
    let array = [];
    let groups = context.find(".condition-group");
    for (let i = 0; i < groups.length; i++) {
      let group = $(groups[i]);
      let items = group.find(".condition-item");
      if(items.length > 0){
        let itemsJson = [];
        let groupJson = {join: group.find("select[data-name='groupJoinType']").val(), items:itemsJson};
        array.push(groupJson);
        for (let j = 0; j < items.length; j++) {
          let item = $(items[j]);
          if(item.length > 0){
            let nameEL = item.find("select[data-name='name']");
            let name = nameEL.val();
            let opt = nameEL.find("option:selected");
            let typeName = opt.attr("type");
            let type = conditionTool.conditionType[typeName];
            if(type && name){
              let v = type.loadValue ? type.loadValue(item) : item.find(".form-control[data-name='value']").val();
              if(v){
                itemsJson.push({column: name, operator: item.find("select[data-name='operator']").val(),
                  value: v, type: typeName, alias: opt.text()});
              }
            }
          }
        }
      }
    }
    return {join: context.find(".allJoinType").val(), groups: array};
  },
  conditionJsonToView:function (jsonStr){
    if(jsonStr){
      try {
        let json = JSON.parse(jsonStr);
        let groups = json.groups;
        return `<div class="conditionStr">` + groups.map(function (group){
          return `<label class="label label-default">(</label>` +
            group.items.map(function (item){
              let type = conditionTool.conditionType[item.type];
              return `<label class="label label-success">${item.alias} ${item.operator === "IN"?"包含":item.operator} ${type.valueAlias?type.valueAlias(item.value):item.value}</label>`;
            }).join(`<label class="label label-primary">${group.join.toUpperCase() === 'OR' ? "或":"且"}</label>`) +
            `<label class="label label-default">)</label>`;
        }).join(`<label class="label label-primary">${json.join.toUpperCase() === 'OR' ? "或":"且"}</label>`) + `</div>`;
      } catch (e) {}
    }
    return "";
  },
  conditionFieldChange:function (el){
    let $this = $(el);
    let type = conditionTool.conditionType[$this.find("option:selected").attr("type")];
    if(type){
      let item = $this.parents(".condition-item");
      item.find("select[data-name='operator']").html(type.operator.map(function (e){
        return `<option value="${e}">${e==="IN"?"包含":e}</option>`
      }).join(""));
      item.find(".value-div").html(type.valueHtml());
      if (type.initAfter) {
        type.initAfter(item);
      }
    }
  },
  addConditionGroup:function (context, join){
    let groupsDiv = context.find(".condition-groups");
    return $(
      `<div class="condition-group panel panel-default">
        <div>
          <label class="label label-primary m-l m-r">条件组</label><label class="control-label">联结方式：</label>
          <select class="form-control groupJoinType" data-name="groupJoinType">
            <option value="AND" ${join==='and'?"selected":""}>且</option>
            <option value="OR" ${join==='or'?"selected":""}>或</option>
          </select>
          <button type="button" class="btn btn-white save-btn btn-xs m-l" onclick="conditionTool.addCondition($(this).parents('.condition-group'))" title="添加条件">
            <i class="fa fa-plus"></i>
          </button>
          <button type="button" class="btn btn-white btn-xs m-l" title="删除整个组" onclick="$(this).parents('.condition-group').remove()">
            <i class="fa fa-close"></i>
          </button>
        </div>
      </div>`
    ).appendTo(groupsDiv);
  },
  addCondition:function (group, item){
    if(item && (item.value === null || item.value === '')){
      return;
    }
    let context = group.parents(".condition-context")[0];
    let fieldOption;
    for (let i = 0; i < conditionTool.fields.length; i++) {
      if(conditionTool.fields[i].context === context){
        fieldOption = conditionTool.fields[i].fieldOption;
        break;
      }
    }
    if(!fieldOption){
      return null;
    }
    let el = $(
      `<div class="condition-item">
        <div class="col-xs-2" title="匹配字段">
          <select class="form-control" data-name="name" onchange="conditionTool.conditionFieldChange(this)">
            ${fieldOption}
          </select>
        </div>
        <div class="col-xs-1 no-padding">
          <select class="form-control text-center no-padding" data-name="operator" title="匹配符号">
          </select>
        </div>
        <div class="col-xs-8 value-div">
        </div>
        <div class="col-xs-1">
          <button type="button" class="btn btn-white btn-xs m-t-sm" title="删除此条件" onclick="$(this).parents('.condition-item').remove()"><i class="fa fa-close"></i></button>
        </div>
      </div>`
    ).appendTo(group);
    let name = el.find("select[data-name='name']");
    if(item){
      name.val(item.column);
      name.trigger("change");
      el.find("select[data-name='operator']").val(item.operator);
      let type = conditionTool.conditionType[item.type];
      if(type.setValue){
        type.setValue(el, item.value);
      }else{
        el.find(".form-control[data-name='value']").val(item.value);
      }
    }else{
      name.trigger("change");
    }
    return el;
  },
};