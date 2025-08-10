;(function (window) {
  /** 授权状态：true=成功授权，false=拒绝，null=可重试 */
  var auth = null;
  window.browserNotify = {
    /**
     * 发送通知
     * @param title  -  标题
     * @param body   -  内容
     * @param icon   -  图标URL
     * @param id     -  唯一ID，频繁通知时，会替换之前未关闭的同ID的通知窗口，为空时默认为 window.location.pathname
     * @param failCallback - nullable 授权失败时的回调函数
     **/
    send:function (title, body, icon, id, failCallback) {
      if (auth === true) {
        sendNotify(title, body, icon);
      }
      //重新授权
      else if(auth === null){
        requestAuth(function (auth) {
          if(auth){
            sendNotify(title, body, icon, id);
          }else{
            doCallback(failCallback);
          }
        });
      }else{
        doCallback(failCallback);
      }
    },
    /**
     * 获取授权
     * @param callback - nullable 回调函数，执行时传入参数：[0]boolean = 是否授权成功
     **/
    checkAuth:function (callback) {
      if(auth === null){
        requestAuth(callback);
      }else{
        doCallback(callback, auth === true);
      }
    },
    /**
     * 每秒循环检测，每经过 loopSeconds 秒（默认60秒）就执行 triggerFunc
     **/
    loopCheck:function (triggerFunc, loopSeconds) {
      loopSeconds = loopSeconds ? loopSeconds : 60;
      var seconds = loopSeconds;
      setInterval(function () {
        if(seconds <= 0){
          triggerFunc();
          seconds = loopSeconds;
        }
        seconds--;
      }, 1000);
    }
  };

  var requestAuth = function (callback) {
    try {
      Notification.requestPermission(function (permission) {
        console.log("Notification permission = " + permission);
        if(permission === "granted"){
          auth = true;
        }else if (permission === "denied"){
          auth = false;
        }else{
          auth = null;
        }
        doCallback(callback, auth === true);
      });
    } catch (e) {
      doCallback(callback, false);
    }
  };

  var sendNotify = function (title, body, icon, id) {
    var _notify = new Notification(title, {body: body, icon: icon, requireInteraction: false,
      tag: window.location.pathname, renotify:true, timestamp: new Date().getTime()});
    _notify.onclick = function (ev) {
      if(window !== window.top && window.top.index && window.top.index.showNav){
        window.top.index.showNav(window.location.pathname);
      }
      window.top.focus();
    };
    window.onbeforeunload = function (ev) {
      _notify.close();
    };
  };

  var doCallback = function (callback, arg) {
    if(typeof(callback) === "function"){
      callback(arg);
    }
  };

})(window);