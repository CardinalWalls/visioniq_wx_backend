KindEditor.plugin('crawling', function (K) {
  var self = this, name = 'crawling';
  var html = [
    '<input placeholder="请输入需要获取文章内容的链接" class="form-control" id="crawling_url"></input>'
  ].join('');
  self.clickToolbar(name, function () {
    self.createDialog({
      name: name,
      width: 450,
      height: 180,
      title: self.lang(name),
      body: html,
      yesBtn: {
        name: self.lang('yes'),
        click: function (e) {
          var url = $("#crawling_url").val();
          if (url) {
            layer.load(1, {shade: 0.3});
            $.postPayload(self.ctx + '/admin/news/wx/article/crawling', {url: url}, function (rs) {
              layer.closeAll();
              if(rs.success){
                self.hideDialog().focus();
                self.insertHtml(rs.data);
              }else{
                layer.alert(rs.message, {icon: 2});
              }
            })
          }

        }
      }
    });
  });

});
