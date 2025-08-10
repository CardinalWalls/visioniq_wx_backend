KindEditor.plugin('diy_video', function (K) {
  var self = this, name = 'diy_video';
  self.clickToolbar(name, function () {
    if(self.fullscreenMode){
      alert("全屏窗口不支持视频插件，请退出全屏再试！");
      return;
    }
    attachmentVod.totalCount = 1;
    attachmentVod.cleanList();
    if(attachmentVod.checkCountOnInit()){
      $("#_attachmentVod_mediaType").html(attachmentVod.mediaType.join("、"));
      attachmentVod.file[0].value = null;
      attachmentVod.callback = function (array) {
        var html = attachmentVod.toViewHtml(array);
        self.insertHtml(html);
        attachmentVod.modal.modal("hide");
        attachmentVod.open = false;
      };
      attachmentVod.tableEl.bootstrapTable("removeAll");
      attachmentVod.thirdTableEl.bootstrapTable("removeAll");
      attachmentVod.thirdUrl.val("");
      attachmentVod.modal.modal();
      attachmentVod.open = true;
    }
  });
});