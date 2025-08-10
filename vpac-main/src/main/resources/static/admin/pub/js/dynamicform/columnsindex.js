
var tp = {
  curId: '',
  curIndex: 0,
  search: function () {
    tableList.obj.bootstrapTable("refresh");
  },
  clearCondition: function () {
    document.searchForm.reset();
    tp.search();
  },
  openModal: function (type) {
    $('#jsonData_group .input-daterange').remove();
    if(type == 'edit'){
      var selected = tableList.obj.bootstrapTable("getSelections");
      if(selected.length < 1){
        layer.alert('请选择一行数据');
        return;
      }
      $("#columnsModal").modal({
        backdrop: "static"
      });
      $("#columns_form #code").val(selected[0]['code']);
      $("#columns_form #id").val(selected[0]['id']);
      var jsonData = JSON.parse(selected[0]['jsonData'])
      for(var index in jsonData){
        var cur = jsonData[index];
        tp.appendDiv(index, cur['field'],cur['title'], cur['require'], cur['unique'], cur['attachment']);
      }
    } else {
      $("#columnsModal").modal({
        backdrop: "static"
      });
      $("#columns_form #code").val('');
      $("#columns_form #id").val('');
      tp.appendDiv(0, '','');
    }
  },
  uniqueAndRequireClick:function(el){
    var name = el.name;
    if(name === 'require' && !el.checked){
      $(el).parents(".input-daterange").find("input[name='unique']")[0].checked = false;
    }
    if(name === 'unique' && el.checked){
      $(el).parents(".input-daterange").find("input[name='require']")[0].checked = true;
    }
  },
  appendDiv: function (index, englishVal, ChineseVal, require, unique, attachment) {
    var id = 'jsonDataDiv_'+index;
    $('#jsonData_group').append(
      '           <div class="input-daterange input-group m-b" id="'+id+'">\n' +
      '              <input type="text" class="input-sm form-control" name="english" placeholder="字段英文名(无下划线)" value="'+englishVal+'">\n' +
      '              <span class="input-group-addon">→</span>\n' +
      '              <input type="text" class="input-sm form-control" name="chinese" placeholder="字段中文名" value="'+ChineseVal+'">\n' +
      '<span class="input-group-addon"><input type="checkbox" onclick="tp.uniqueAndRequireClick(this)" name="require" '+(require?"checked":"")+' style="vertical-align:bottom"> 必填</span>'+
      '<span class="input-group-addon"><input type="checkbox" onclick="tp.uniqueAndRequireClick(this)" name="unique" '+(unique?"checked":"")+' style="vertical-align:bottom"> 唯一</span>'+
      '<span class="input-group-addon"><input type="checkbox" name="attachment" '+(attachment?"checked":"")+' style="vertical-align:bottom"> 附件</span>'+
      '              <span class="input-group-addon cursor-pointer" style="background-color:red;color:white" onclick="tp.removeDiv(\''+id+'\')"> \n' +
      '                 <span class="fa fa-minus"></span>\n' +
      '              </span>\n' +
      '            </div>'
    );
    tp.curIndex = index;
  },
  typeSave: function () {
    if (!$("#columns_form").valid()) {
      return;
    }
    var l = layer.load(1, {shade: 0.3});
    var code = $('#columns_form #code').val();
    var id = $('#columns_form #id').val();
    var arr = new Array();
    var map = {};
    $('#columns_form .input-daterange').each(function () {
      var id = $(this).attr('id');
      var english = $('#'+id+' input[name="english"]').val().trim();
      var chinese = $('#'+id+' input[name="chinese"]').val().trim();
      if(english !== '' && chinese !== '' && map[english] !== true){
        map[english] = true;
        arr.push({
          field: english,
          title: chinese,
          require: $('#'+id+' input[name="require"]')[0].checked,
          unique: $('#'+id+' input[name="unique"]')[0].checked,
          attachment: $('#'+id+' input[name="attachment"]')[0].checked,
        });
      }
    });
    $.post(ctx + "/admin/dynamicform/columns", {id: id,code: code, jsonData: JSON.stringify(arr)}, function (rs) {
      layer.close(l);
      if (rs.success) {
        $("#columnsModal").modal("hide");
        tp.search();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    }, "json");
  },
  removeDiv: function (id) {
    if(id === 'jsonDataDiv_0'){
      $('#'+id).find('input').each(function(){$(this).val('')})
    } else {
      $('#'+id).remove();
    }
  },
  addColumns: function () {
    tp.curIndex = parseInt(tp.curIndex) + 1;
    tp.appendDiv(tp.curIndex, '','');
  }
};

var tableList = {
  obj: $("#table_list"),
  init: function (searchArgs) {
    tableList.obj.bootstrapTable({
      url: ctx + "/admin/dynamicform/columns/page" ,
      //数据列
      columns: [{
        checkbox: true
      }, {
        title: "编号",
        field: "code"
      }, {
        title: "字段数据",
        field: "jsonData",
        formatter: function (value, row, index) {
          var str = '';
          var jsonData = JSON.parse(value);
          for(var index in jsonData){
            var item = jsonData[index];
            str += '<span title="'+(item.require?"必填 ":"")+(item.unique?" 唯一":"")+'" class="label label-'+(item.unique?"warning":"primary")+'">'
              +(item.require?"<b style='color:red'>* </b>":"")+item.field + "：" +item.title+'</span> &nbsp;&nbsp;';
          }
          return str;
        }
      }
      ]
    });
  }
};

$(function () {
  tableList.init({});
});