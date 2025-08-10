KindEditor.plugin('template', function (K) {
  var self = this, name = 'template';
  var html = [
    '<iframe src="' + self.ctx + '/admin/news/editorTemplate/editorTemplate" style="border: 0;height: 600px;width: 100%;" id="editorTemplateIfr"></iframe>'
  ].join('');
  self.clickToolbar(name, function () {
    self.createDialog({
      name: name,
      width: 450,
      height: 640,
      title: self.lang(name),
      body: html,
      yesBtn: {
        name: self.lang('yes'),
        click: function (e) {
          var editorTemplate = $("#editorTemplateIfr")[0].contentWindow.editorTemplate;
          var selectRow = editorTemplate.getSelectedRow();
          if (selectRow) {
            self.insertHtml(selectRow.content);
            // 模板人气增加1
            $.postPayload(self.ctx + '/admin/sys/editorTemplate/use', {id: selectRow.id}, function (rs) {
            })
          }
          self.hideDialog().focus();
        }
      }
    });
  });

});
