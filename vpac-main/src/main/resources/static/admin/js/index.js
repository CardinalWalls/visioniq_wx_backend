var index = {
  isIOS:false,
  navTabScroll:$("#nav_tab_scroll"),
  closeWindow: function (e, id, callback) {
    var li = $(e).parents("li");
    var a = li.prev().find("a:eq(0)");
    a.trigger("click");
    var iframe = $("#" + id);
    //触发关闭事件
    try {
      iframe[0].contentWindow.dispatchEvent(new Event("beforeunload"));
    } catch (e) {}
    iframe.remove();
    li.remove();
    if(callback){
      callback();
    }else{
      index.tabScroll();
    }
    index.cacheOpenedTabs();
  },
  refreshWindow: function (id, newHref) {
    var e = document.getElementById(id);
    if (newHref) {
      e.src = newHref;
    } else {
      newHref = e.src;
      e.src = newHref;
    }
  },
  closeNav: function (href, notShowPrev) {
    $("#content-nav li").each(function () {
      var li = $(this);
      var h = li.data("href");
      if (h) {
        if (href.split("?")[0] === h.split("?")[0]) {
          if (notShowPrev) {
            $(li.find("a").attr("href")).remove();
            li.remove();
          } else {
            li.find(".main-if-close").trigger("click");
          }
          return false;
        }
      }
    });
    index.tabScroll();
    index.cacheOpenedTabs();
  },
  showNav: function (href, refresh) {
    var opened = false;
    var uri = href.split("?")[0];
    var hasPower = false;
    $("#content-nav li").each(function () {
      var li = $(this);
      var h = li.data("href");
      if (h) {
        if (uri === h.split("?")[0]) {
          var a = li.find(">a");
          if (refresh) {
            index.refreshWindow(a.attr("aria-controls"), href)
          }
          a.trigger("click");
          opened = true;
          hasPower = true;
          return false;
        }
      }
    });
    if (!opened) {
      $(".J_menuItem").each(function () {
        var $this = $(this);
        if (uri === $this.attr("href")) {
          var id = $this.attr("id");
          var ic = $this.find("i").attr("class");
          index.openNav(ic, href, id, $this.text(), refresh);
          hasPower = true;
          return false;
        }
      });
    }
    if (!hasPower) {
      alert("请申请访问权限!");
    }
  },
  showNavAnother: function (href, refresh) {
    index.closeNav(href, true);
    var uri = href.split("?")[0];
    var hasPower = false;
    $(".J_menuItem").each(function () {
      var $this = $(this);
      if (uri === $this.attr("href")) {
        var id = $this.attr("id");
        var ic = $this.find("i").attr("class");
        index.openNav(ic, href, id, $this.text(), refresh);
        hasPower = true;
        return false;
      }
    });
    if (!hasPower) {
      alert("请申请访问权限!");
    }
  },
  openNav: function (icon, href, id, text, refresh) {
    var exists = $("#tab_" + id);
    var move = "right";
    if (exists.length > 0) {
      if (refresh) {
        index.refreshWindow("tab_" + id, href);
      }
      var a = $("#content-nav a[href='#tab_" + id + "']");
      a.tab("show");
      move = a.parent().position().left + "px";
    } else {
      var nav = $("#content-nav");
      nav.find(".active").removeClass("active");
      nav.append("<li role='presentation' class='tab-nav active' onclick='index.clickTab(this)' data-toggle='context' data-target='#context-menu' data-href='" + href + "'>" +
        "<a href='#tab_" + id + "' aria-controls='tab_" + id + "' role='tab' data-toggle='tab'>" +
        "<i class='" + icon + "'></i>" + text.trim() + "</a>" +
        "<button type='button' class='close main-if-close' onclick='index.closeWindow(this, \"tab_" + id + "\")'><span>&times;</span></button>" +
        "<button type='button' class='close main-if-refresh' onclick='index.refreshWindow(\"tab_" + id + "\")'><i class='fa fa-refresh'></i></button>" +
        "<button class='close main-if-new' onclick='index.closeWindow(this, \"tab_" + id + "\")'>" +
        "<a class='no-padding' style='color:#000' href='"+href+"' target='_blank'><i class='fa fa-external-link'></i></a></button>" +
        "</li>");
      var content = $("#content-main");
      content.find(".active").removeClass("active");
      content.append("<iframe role='tabpanel' class='tab-pane active' id='tab_" + id + "' width='100%' " +
        "height='"+(index.isIOS?"150%":"100%")+"' scrolling='"+(index.isIOS?"no":"yes")+"' " +
        "src='" + href + "' frameborder='0' seamless></iframe>");
    }
    index.tabScroll(move);
    index.initContextMenu(id);
    // index.monitorEnter('tab_' + id);
  },
  initContextMenu: function (id) {
    var iframe = document.getElementById('tab_' + id);
    iframe.onload = function () {
      try {
        iframe.contentDocument.onclick = function () {
          $(".tab-nav").contextmenu('closemenu');
        };
      } catch (e) {
      }
    };
    index.cacheOpenedTabs(id);
  },
  // 输入框输完信息点击回车查询监听
  monitorEnter: function (id) {
    var iframe = document.getElementById(id);
    iframe.onload = function () {
      var searchForm = $("#" + id).contents().find("form").first();
      searchForm.submit(function () {
        return false;
      });
      var searchBtn = null;
      searchForm.find("button").each(function () {
        if ($(this).html().indexOf("查询") > -1) {
          searchBtn = $(this);
          return false;
        }
      });
      if (searchBtn == null) {
        return false;
      }
      try {
        var objEvtKd = $._data(searchForm.find("input.form-control")[0], 'events');
        if (!objEvtKd || !objEvtKd['keydown']) {
          searchForm.find("input.form-control").keydown(function (e) {
            if (e.keyCode === 13) {
              searchBtn.click();
            }
          });
        }
      } catch (e) {
        console.log("no search input");
      }
      try {
        var objEvtCg = $._data(searchForm.find("select")[0], 'events');
        if (!objEvtCg || !objEvtCg['change']) {
          searchForm.find("select").change(function () {
            searchBtn.click();
          })
        }
      } catch (e) {
        console.log("no search select");
      }
      // 日期插件text不自动填充
      searchForm.find(".layer-date").attr("autocomplete","off");
    }
  },
  currentUserAuth: null,
  //获取当前用户权限信息
  getCurrentUserAuth: function () {
    if (!index.currentUserAuth) {
      $.ajax({
        url: "/admin/user/auth/info",
        method: "GET",
        async: false,
        success: function (res) {
          index.currentUserAuth = res.data;
        },
        type: "json"
      });
    }
    return index.currentUserAuth;
  },
  tabScroll:function(scrollTo){
    index.navTabScroll.find(".mCSB_container").width("auto");
    index.navTabScroll.mCustomScrollbar("update");
    if(scrollTo){
      index.navTabScroll.mCustomScrollbar("scrollTo",scrollTo);
    }
  },
  clickTab:function(e){
    var $this = $(e);
    index.tabScroll($this.position().left + "px");
    index.cacheOpenedTabs($this.children("a").attr("aria-controls").substr(4));
  },
  SmoothlyMenu:function(){
    if (!$('body').hasClass('mini-navbar')) {
      $('#side-menu').hide();
      setTimeout(function () {
        $('#side-menu').fadeIn(300);
      }, 100);
    } else if ($('body').hasClass('fixed-sidebar')) {
      $('#side-menu').hide();
      setTimeout(function () {
        $('#side-menu').fadeIn(300);
      }, 300);
    } else {
      $('#side-menu').removeAttr('style');
    }
  },
  topSystemInfoToggle:function (show) {
    var body = $(parent.window.document.body);
    var i = $(".top-system-info-toggle").find("i");
    if(show){
      body.removeClass("top-system-info-hidden");
      i.attr("class", "fa fa-toggle-on");
      $.cookie("top-system-info-hidden", false, {expires: 7, path:'/admin'}); //7天
    }else{
      body.addClass("top-system-info-hidden");
      i.attr("class", "fa fa-toggle-off");
      $.cookie("top-system-info-hidden", true, {expires: 7, path:'/admin'}); //7天
    }
  },
  //登录失效时，打开临时的登录窗口
  openLoginFrame:function () {
    var f =$("#authFailToLoginFrame");
    if(f.length === 0){
      f = $('<iframe id="authFailToLoginFrame" style="position:absolute;top:0;z-index:99999;display:none" ' +
        '        width="100%" height="100%" frameborder="0" seamless></iframe>').appendTo("body");
    }
    //登录页，参数标识当前窗口，为顶层窗口（topWindow=1），不然会被强制刷新顶层窗口
    f.attr("src", ctx + "/admin/login?topWindow=1");
    f.show();
  },
  //记录点击的窗口
  cacheOpenedTabs:function(id){
    try {
      // 只记录选中的那一个
      $.cookie("_opened_tabs_",
        id? "#" + id : ("#" + $("#content-nav>li.active>a").attr("aria-controls").substr(4)));
    } catch (e) {}
  },
  //刷新整个页面后，打开之前记录的窗口
  initCachedOpenedTabs:function () {
    var ids = $.cookie("_opened_tabs_");
    if(ids){
      $(decodeURIComponent(ids)).trigger("click");
    }
  },
  //刷新整个页面后，打开参数target目标路径窗口
  openedTargetTab:function () {
    var target = $.cookie("_target_");
    if(target){
      $.removeCookie("_target_");
      $(".J_menuItem[href='"+decodeURIComponent(target)+"']").trigger("click");
    }
  },
  homeOnClick:function () {
    var tab = $("#_nav_home_tab_");
    if (tab.data("next_click_refresh") === "true") {
      index.refreshWindow("tab_home");
    }
  },
  homeOnLoad:function () {
    var tab = $("#_nav_home_tab_");
    if (!tab.hasClass("active")) {
      tab.data("next_click_refresh", "true");
    }else{
      tab.removeData("next_click_refresh");
    }
  }
};
$(function () {
  //主页 iframe ， ios浏览器兼容性处理
  if (/(iPhone|iPad|iPod|iOS|Mac)/i.test(navigator.userAgent)) {
    $("body").addClass("ios-webkit");
    $("#tab_home").attr("scrolling", "no");
    index.isIOS = true;
    var checkIframeHeight = function () {
      $("iframe").each(function () {
        var iframeWin = this.contentWindow || this.contentDocument.parentWindow;
        if (iframeWin.document.body) {
          var height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
          var percent =  $.type(this.height) === "string" && this.height.indexOf("%") > 0;
          if(this.scrollHeight < height || percent){
            this.height = height + 100;
          }
        }
      });
      setTimeout(checkIframeHeight, 1000);
    };
    checkIframeHeight();
  }

  $(".J_menuItem").on('click', function () {
    if (window.event && window.event.ctrlKey) {
      return true;
    }
    var $this = $(this);
    var id = $this.attr("id");
    var ic = $this.find("i").attr("class");
    var href = $this.attr("href");
    index.openNav(ic, href, id, $this.text(), false);
    return false;
  });
  $("#side-menu .dropdown-menu").mouseleave(function () {
    $("#side-menu .dropdown").removeClass("open");
  });
  // 右键点击菜单详细事件
  $(".tab-nav").contextmenu();
  $(".dropdown-menu-other").click(function () {
    var id = $("#context-menu").attr("data-id");
    $(".tab-nav").each(function () {
      var a = $(this).find("a");
      if (a.attr("aria-controls") != id) {
        index.closeWindow(a, a.attr("aria-controls"));
      }
    });
  });
  $(".dropdown-menu-all").click(function () {
    $(".tab-nav").each(function () {
      var a = $(this).find("a");
      index.closeWindow(a, a.attr("aria-controls"), function () {});
    });
    $("#content-nav a").trigger("click");
    index.tabScroll();
  });
  $(window).keydown(function (e) {
    if (e.keyCode === 116) {
      if (window.event.ctrlKey) {
        return true;
      }
      var id = $("#content-nav .active a").attr("aria-controls");
      if(id){
        index.refreshWindow(id);
      }
      return false;
    }
    return true;
  });
  //标签栏滚动
  index.navTabScroll.mCustomScrollbar({axis:"x",theme:"light-thin",scrollInertia:200});

  //固定菜单栏
  $('.sidebar-collapse').slimScroll({
    height: '100%',
    railOpacity: 0.9,
    alwaysVisible: false
  });
  // 菜单切换
  $('.navbar-minimalize').click(function (e) {
    if (e && e.stopPropagation) {
      e.stopPropagation();
    }
    else {
      window.event.cancelBubble = true;
    }
    $("body").toggleClass("mini-navbar");
    index.SmoothlyMenu();
  });
  $('.full-height-scroll').slimScroll({
    height: '100%'
  });

  $('#side-menu>li').click(function () {
    if ($('body').hasClass('mini-navbar')) {
      $('.navbar-minimalize:eq(0)').trigger('click');
    }
  });
  $('#side-menu>li a.J_menuItem').click(function () {
    if ($(window).width() < 769) {
      $('.navbar-minimalize:eq(0)').trigger('click');
    }
  });

  $('.nav-close').click(function () {
    $('.navbar-minimalize:eq(0)').trigger('click');
  });
  // MetsiMenu
  $('#side-menu').metisMenu().show();
  $("#_parent_cover_").fadeOut(700);

  //top-system-info-toggle
  $(".top-system-info-toggle").on("click", function () {
    index.topSystemInfoToggle(!$(this).find("i").hasClass("fa-toggle-on"));
  });
  var topSystemInfoHidden = $.cookie('top-system-info-hidden');
  if(topSystemInfoHidden === undefined){
    index.topSystemInfoToggle(!$(".top-system-info-toggle i").hasClass("fa-toggle-off"));
  }else{
    index.topSystemInfoToggle(!(topSystemInfoHidden === 'true'));
  }

  index.initCachedOpenedTabs();
  index.openedTargetTab();
});
$(window).bind("load resize", function () {
  if ($(this).width() < 1000) {
    $('body').addClass('mini-navbar');
    $('.navbar-static-side').fadeIn();
  }
});