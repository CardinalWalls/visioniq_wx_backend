/*
img:  注意  ！！！！！！！！atta加了隐藏的！！！！！！！
          <div class="form-group" id="avatarForm">
            <label for="avatar" class="control-label">头像：</label>
            <button type="button" class="btn-success" style="position: relative;right: 0" onclick="attachment.getAtta('avatar').addAtta()"><i
              class="fa fa-upload"></i>更换头像
            </button>
            <div class="form-group" id="attas" style="display: none;">
            </div>
            <div>
              <img class="img-circle" id="image" style="width:90px" src="/admin/img/avatar.jpg">
            </div>
            <input type="file" style="display: none" dir="" id="file" name="file" enctype="multipart/form-data"/>
          </div>
single和mulit:
          <div class="form-group" id="imgsForm">
            <label for="imgs" class="control-label">首页照片：</label>
            <button type="button" class="btn-success " style="position: relative;right: 0" onclick="attachment.getAtta('imgs').addAtta()"><i
              class="fa fa-upload"></i>添加照片
            </button>
            <div class="form-group" id="attas">
            </div>
            <input type="file" style="display: none" dir="" id="file" name="file" enctype="multipart/form-data"/>
          </div>

同一页面多个上传表单，表单种类如上，
div需要添加你的 表单名字+Form 作为id

attachment.regist('avatar', 'img')里的avatar就是这个表单的名字
要操作某个表单时，通过attachment.getAtta('avatar').操作就行，比原来的多了个.getAtta('avatar')

 */
var attachment = {
  pattern: '',
  name: '',
  data: new Map(),
  ossurl: ctx+'/api/oss/redirect?file=',
  getAtta: function (name) {
    attachment.name = name;
    attachment.pattern = attachment.data[name];
    return this;
  },
  regist: function (name, pattern) {
    attachment.name = name;
    attachment.pattern = pattern;
    attachment.data[name] = pattern;
    //上传文件事件
    $("#" + attachment.name + "Form :file").change(function () {
      if (attachment.pattern === 'single' && $("#" + attachment.name + "Form #attas .attaDiv ").length >= 1) {
        layer.alert("只能选择一个");
        return;
      }
      if (attachment.pattern === 'img' && $("#" + attachment.name + "Form #attas .attaDiv ").length >= 1) {
        $("#" + attachment.name + "Form #attas").html('');
      }
      if ($(this).val() == null || $(this).val() == undefined || $(this).val() == "") {
        return;
      }
      var loadIndex = layer.load();
      var formData = new FormData();
      formData.append("file", $(this)[0].files[0]);
      $.ajax({
        url: ctx + "/admin/attachment/uploadAtta",
        type: "POST",
        data: formData,
        /**
         *必须false才会自动加上正确的Content-Type
         */
        contentType: false,
        /**
         * 必须false才会避开jQuery对 formdata 的默认处理
         * XMLHttpRequest会对 formdata 进行正确的处理
         */
        processData: false,
        success: function (res) {
          if (res.success) {
            layer.msg(res.message, {time: 1000});
            if (attachment.pattern === 'img') {
              $('#' + attachment.name + 'Form #image').attr('src', res.data.url)
            }
            // 用于区分的id
            var rnd="";
            for(var i=0;i<10;i++)
              rnd+=Math.floor(Math.random()*10);
            $("#" + attachment.name + "Form #attas").append(
                '<div class="attaDiv" style="margin: 3px" id="attaDiv_'+rnd+'">' +
                '<label class="attaName col-sm-3">' + res.data.name + '</label>' +
                '<label class="attaSize col-sm-2">' + res.data.size + 'k</label>' +
                '<input type="text" class="attaRemark col-sm-4" placeholder="附件说明"/>' +
                '&nbsp;' + '&nbsp;' +
                '<a href="' + res.data.url + '" download="" class="attaUrl" target="_blank">下载</a>' +
                '&nbsp;' + '&nbsp;' +
                '<button type="button" class="btn-danger"  onclick="attachment.getAtta(\'' + attachment.name + '\').deleteAtta(this)"><i class="fa fa-trash-o"></i>删除</button>' +
                '</div>'
            );
            $("#" + attachment.name + "Form #photos").append(
                '<img class="img-circle" style="width: 90px;height: 90px" src="'+res.data.url+'" ><a href="#" onclick="attachment.deletePre(this, \''+rnd+'\')"><i class="fa fa-trash-o "></i></a></img>'
            );
          }
          else {
            layer.alert(res.message, {icon: 2});
          }
          layer.close(loadIndex);
        },
        error: function () {
          layer.close(loadIndex);
          layer.alert("异常", {icon: 2});
        }
      });
    });
  },
  deleteAtta: function (target) {
    $(target).parent().remove();
  },
  addAtta: function () {
    $("#" + attachment.name + "Form :file").click();
  },
  emptyAtta: function () {
    $("#" + attachment.name + "Form #attas").html('');
  },
  imgclear: function (imgsrc) {
    $('#' + attachment.name + 'Form #image').attr('src', imgsrc ? imgsrc : '/admin/img/avatar.jpg');
  },
  // 用作读取已有图片显示出来
  appendAtta: function (e) {
    $("#" + attachment.name + "Form #attas").append(
        '<div class="attaDiv" style="margin: 3px" id="attaDiv_'+e.id+'">' +
        '<input id="id" type="hidden" value="'+e.id+'">' +
        '<label class="attaName col-sm-3">' + e.name + '</label>' +
        '<label class="attaSize col-sm-2">' + e.size + '</label>' +
        '<input type="text" class="attaRemark col-sm-4" value="' + e.remark + '" placeholder="附件说明"/>' +
        '&nbsp;' + '&nbsp;' +
        '<a href="'+attachment.ossurl+ e.url + '" download="" class="attaUrl" target="_blank">下载</a>' +
        '&nbsp;' + '&nbsp;' +
        '<button type="button" class="btn-danger"  onclick="attachment.getAtta(\'' + attachment.name + '\').deleteAtta(this)"><i class="fa fa-trash-o"></i>删除</button>' +
        '</input>'
    );
  },
  appendImg: function (id, uri) {
    $("#" + attachment.name + "Form #photos").append(
        '<img class="img-circle" style="width: 90px;height: 90px" src="'+attachment.ossurl+uri+'" ><a href="#" onclick="attachment.deletePre(this, \''+id+'\')"><i class="fa fa-trash-o "></i></a></img>'
    );
  },
  getAttaParams: function () {
    var attas = new Array();
    $("#" + attachment.name + "Form #attas").find("div").each(function () {
      var id = $(this).find("#id").eq(0).val();
      var attaName = $(this).find(".attaName").eq(0).html();
      var attaRemark = $(this).find(".attaRemark").eq(0).val();
      var attaUrl = $(this).find(".attaUrl").eq(0).attr("href");
      var attaSize = $(this).find(".attaSize").eq(0).html();
      var persistent = $(this).find(".persistentValue").eq(0).val();
      var atta = {
        id: id,
        attaName: attaName,
        attaRemark: attaRemark,
        attaUrl: attaUrl,
        attaSize: attaSize,
        persistent: persistent
      }
      attas.push(atta);
    });
    return attas;
  },
  setImg: function (imgurl) {
    $('#'+attachment.name+'Form #image').attr('src', attachment.ossurl+imgurl)
  },
  deletePre: function (e, id) {
    $(e).prev('.img-circle').remove();
    $(e).remove();
    $('#attaDiv_' + id).remove();
  },
  clear: function () {
    $('.attaDiv').remove();
    $('#photos').html('');
  }
}