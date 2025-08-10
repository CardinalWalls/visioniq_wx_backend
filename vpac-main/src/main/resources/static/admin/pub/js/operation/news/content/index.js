var htmlMinify = require('html-minifier').minify;
var tp = {
  openModal: function (type) {
    attachment.clear("img", "imgs");
    var selected = tableList.obj.bootstrapTable("getSelections");
    if (selected.length != 1 && type === 'edit') {
      layer.msg("请选择一行数据");
      return;
    }
    // 解决模态框中编辑器无法在文本框输入值的问题
    $('#typeModal').on('shown.bs.modal', function () {
      $(document).off('focusin.modal');
    });
    $("#typeModal").modal({
      backdrop: "static"
    });
    if (type === 'edit') {
      $.get(ctx + "/admin/news/" + selected[0].id, "", function (re) {
        attachment.setSingle("img", re.img);
        attachment.setArray("imgs", JSON.parse(re.imgs));
        $("#id").val(selected[0].id);
        $("#summary").val(re.summary);
        $("#typeId").val(re.typeId);
        $("#edittitle").val(re.title);
        $("#subtitle").val(re.subtitle);
        $("#otherAttr").val(re.otherAttr);
        $("#publishTime").val(re.publishTime);
        $("#readCount").val(re.readCount);
        $("#likeCount").val(re.likeCount);
        $("#formstatus").val(re.status);
        $("#authorName").val(re.authorName);
        $("#keyword").val(re.keyword);
        $("#vipView").val(re.vipView);
        $("#theSource").val(re.theSource);
        $("#price").val(re.price);
        if (re.templateType) {
          $("#templateType").val(re.templateType);
        } else {
          $("#templateType").val(0);
        }
        if (re.regionNews) {
          $("#regionNews").val(re.regionNews);
        } else {
          $("#regionNews").val(0);
        }
        if (re.isRecommend) {
          $("#recommend").val(re.isRecommend);
        } else {
          $("#recommend").val(0);
        }
        if (re.displayType) {
          $("#displayType").val(re.displayType);
        } else {
          $("#displayType").val(1);
        }
        if (re.detailType) {
          $("#detailType").val(re.detailType);
        } else {
          $("#detailType").val(1);
        }

        keditor.html(re.content);
        $("select#typeId").change();
      })
    } else {
      $("#typeId").val("");
      $("#id").val("");
      $("#summary").val("");
      $("#edittitle").val("");
      $("#publishTime").val(formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
      $("#readCount").val("100");
      $("#likeCount").val("100");
      $("#recommend").val(0);
      $("#authorName").val('');
      $("#keyword").val('');
      $("#vipView").val("0");
      $("#templateType").val(0);
      $("#regionNews").val(0);
      $("#displayType").val(1);
      $("#detailType").val(1);
      $("#theSource").val('');
      $("#price").val(0);

      keditor.html("");
    }
  },
  delete: function () {
    layer.confirm("确定要删除该新闻吗？", {
      btn: ['确定', '取消'],
      shade: 0.3
    }, function (index) {
      var selected = tableList.obj.bootstrapTable("getSelections");
      var l = layer.load(1, {shade: 0.3});
      $.post(ctx + "/admin/news/delete", {id: selected[0].id, _org_select_: $("#_org_select_").val()}, function (rs) {
        layer.close(l);
        layer.close(index);
        if (rs.success) {
          layer.msg(rs.message, {icon: 1});
          tp.search();
        } else {
          layer.alert(rs.message, {icon: 2});
        }
      }, "json");
    });
  },
  search: function () {
    var searchArgs = $("#search_form").serializeJSON();
    tableList.obj.bootstrapTable("refresh");
    tableList.init(searchArgs);
  },
  clearCondition: function () {
    document.searchForm.reset();
    tp.search();
  },
  typeSave: function () {
    if (!$("#news_form").valid()) {
      return;
    }
    var l = layer.load(1, {shade: 0.3});
    var params = $("#news_form").serializeJSON();
    params.editorValue = htmlMinify(keditor.html(), {
      collapseWhitespace: true, conservativeCollapse: true
    });
    params._org_select_ = $("#_org_select_").val();
    // params.userIds = media_select.getSelectPlayerUserIds().join(",");
    $.post(ctx + "/admin/news", params, function (rs) {
      layer.close(l);
      if (rs.success) {
        layer.msg(rs.message, {icon: 1});
        $("#typeModal").modal("hide");
        tp.search();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    }, "json");
  },
  online: function () {
    var selected = tableList.obj.bootstrapTable("getSelections");
    if (selected.length != 1) {
      layer.msg("请选择一条新闻");
      return;
    }
    layer.confirm("确定要上线该新闻吗，这会更新发布时间？", {
      btn: ['确定', '取消'],
      shade: 0.3
    }, function (index) {
      var l = layer.load(1, {shade: 0.3});
      $.post(ctx + "/admin/news/online", {id: selected[0].id, _org_select_: $("#_org_select_").val()}, function (rs) {
        layer.close(l);
        layer.close(index);
        if (rs.success) {
          layer.msg(rs.message, {icon: 1});
          tp.search();
        } else {
          layer.alert(rs.message, {icon: 2});
        }
      }, "json");
    });
  },
  recommend: function () {
    var selected = tableList.obj.bootstrapTable("getSelections");
    if (selected.length != 1) {
      layer.msg("请选择一条新闻");
      return;
    }
    if (selected[0].recommend == 1) {
      layer.msg("该新闻已设置为推荐新闻");
      return;
    }
    layer.confirm("确定设置该新闻为推荐新闻？", {
      btn: ['确定', '取消'],
      shade: 0.3
    }, function (index) {
      var l = layer.load(1, {shade: 0.3});
      $.post(ctx + "/admin/news/recommend", {
        id: selected[0].id,
        _org_select_: $("#_org_select_").val()
      }, function (rs) {
        layer.close(index);
        layer.close(l);
        if (rs.success) {
          layer.msg(rs.message, {icon: 1});
          $("#table_list").bootstrapTable("refresh");
        } else {
          layer.alert(rs.message, {icon: 2});
        }
      }, "json");
    });
  },
  addAtta: function () {
    $("#" + attachment.attasId).empty();
    attachment.uploadFile();
  },
  deleteAtta: function (target) {
    $(target).parent().remove();
  },
  openNewsContent: function (newsId) {
    $("#newsContentModal").modal();
    var l = layer.load(1, {shade: 0.3});
    $.get(ctx + "/admin/news/" + newsId, function (rs) {
      layer.close(l);
      $("#newsDetail div.detail").each(function () {
        var that = $(this);
        var attr = that.attr("data-rel");
        if (attr === 'img') {
          var img = rs[attr];
          if (img) {
            that.html('<img alt="封面" src="' + attachment.redirectApiUri + "?file=" + encodeURIComponent(img) + '" style="max-height: 100px;max-width: 300px;"/>');
          } else {
            that.html("");
          }
        } else {
          that.html(rs[attr]);
        }
      });
      $("#newsLink").html(newsId ? "/pages/news/detail/detail?id=" + newsId : "");
    }, "json");
  },
  viewSourceContent: function (url) {
    window.open(url, "_blank");
  },
  getSelectedRow: function () {
    var selected = tableList.obj.bootstrapTable("getSelections");
    if (selected && selected.length > 0) {
      return selected[0];
    }
    layer.msg("请选择一条新闻");
    return null;
  },

  refresh: function () {
    tableList.obj.bootstrapTable('refresh');
  },
  other: function () {
    var selectedRow = tp.getSelectedRow();
    if (selectedRow) {
      if (!selectedRow.templateType || selectedRow.templateType == 0) {
        layer.msg("该文章模板类型不是红头，不用设置红头信息！");
        return;
      }
      $("#other_modal .modal-title").html("【" + selectedRow.title + "】红头信息");
      $("#other_modal input[name='newsId']").val(selectedRow.id);
      $("#other_modal").modal();
      $.get(ctx + "/admin/news/other?newsId=" + selectedRow.id, function (res) {
        $("#other_modal").find('form .form-control').each(function () {
          var $this = $(this);
          $this.val(res[$this.attr("name")]);
        });
      })
    }
  },

  otherSave: function () {
    if (!$("#otherForm").valid()) {
      return;
    }
    var l = layer.load(1, {shade: 0.3});
    var params = $("#otherForm").serializeJSON();
    $.post(ctx + "/admin/news/other", params, function (rs) {
      layer.close(l);
      if (rs.success) {
        layer.msg(rs.message, {icon: 1});
        $("#other_modal").modal("hide");
        tp.search();
      } else {
        layer.alert(rs.message, {icon: 2});
      }
    }, "json");
  },
  tableEl: $("#table_list"),
};


var tableList = {
  obj: $("#table_list"),
  init: function (searchArgs) {
    tableList.obj.bootstrapTable({
      url: ctx + "/admin/news/page",
      //数据列
      columns: [{
          checkbox: true
        }, {
          title: "id",
          field: "id",
          visible:false
        }, {
          title: "分类",
          field: "typeName"
        }, {
          title: "封面图",
          field: "img",
          formatter: function (value, row) {
            return attachment.toViewHtml(value);
          }
        }, {
          title: "标题",
          field: "title",
          formatter: function (value, row) {
            if(row.targetLink){
              return value + "<a target='_blank' class='pull-right' href='"+row.targetLink.replace("{id}", row.id)+"' title='跳转到文章页面'><i class='fa fa-external-link'></i></a>";
            }
            else if(detailLink){
              return value + "<a target='_blank' class='pull-right' href='"+detailLink.replace("{id}", row.id)+"' title='跳转到文章页面'><i class='fa fa-external-link'></i></a>";
            }
            return value;
          }
        }, {
          title: "发布时间",
          field: "publishTime"
        },
        {
          title: "摘要",
          field: "summary",
          visible:false
        }, {
          title: "是否推荐",
          field: "isRecommend",
          formatter: function (value, row, index) {
            if (value == 1) {
              return "推荐";
            } else {
              return "未推荐";
            }
          }
        // }, {
        //   title: "是否VIP",
        //   field: "vipView",
        //   visible:false,
        //   formatter: function (value, row, index) {
        //     if (value == 1) {
        //       return "VIP可看";
        //     } else {
        //       return "普通";
        //     }
        //   }
        // }, {
        //   title: "关联地区",
        //   field: "regionName"
        }, {
          title: "标签",
          field: "tags"
        },  {
          title: "价格",
          field: "price",
          visible:false
        }, {
          title: "状态",
          field: "status",
          formatter: function (value, row, index) {
            if (value === 0) {
              return '草稿';
            } else if (value === 1) {
              return '已发布';
            } else if (value === 2) {
              return '已删除';
            }
          }
        }, {
          title: "操作",
          field: "deal",
          formatter: function (value, row, index) {
            return '<button class="btn btn-success btn-xs" type="button" onclick="tp.openNewsContent(\'' + row.id + '\')">文章详情</button>';
          }
        }
      ]
    });
  }
};
var wxArticle = {
  obj: $("#wx_article_table_list"),
  refresh: function (orgId) {
    $("#wx_article_table_list").bootstrapTable("refresh", {
      url: ctx + "/admin/news/wx/article/sync?orgId=" + orgId
    })
  },
  init: function () {
    wxArticle.obj.bootstrapTable({
      pagination: false,
      singleSelect: false,
      checkboxHeader: true,
      //数据列
      columns: [{
        checkbox: true
      }, {
        title: "标题",
        field: "title"
      }, {
        title: "缩略图",
        field: "thumbUrl",
        formatter: function (value, row) {
          //return attachment.toViewHtml(value.substr(0,value.indexOf("?")));
          return attachment.toViewHtml(value);
        }
      }, {
        title: "文章地址",
        field: "url",
        formatter: function (value, row) {
          return '<button class="btn btn-success btn-xs" type="button" onclick="tp.viewSourceContent(\'' + value + '\')"><i class="fa fa-search"></i>查看详情</button>';
        }
      }
      ]
    });
  }
};

function initPlugins() {
  $.defaultLayDate('#startTime', '#endTime', 'datetime');
  laydate.render({
    elem: '#publishTime',
    type: 'datetime'
  });
  laydate.render({
    elem: '#orgWrittenTime',
    type: 'datetime'
  });
  laydate.render({
    elem: '#orgPublishTime',
    type: 'datetime'
  });

  initType();
  $("#_org_select_").change(function () {
    initType();
  });

}

function initType() {
  $.getJSON("/admin/news/type?status=1", {
    _org_select_: $("#_org_select_").val()
  }, function (re) {
    var s1 = $("select#typeIdSearch").empty();
    var s2 = $("select#typeId").empty();
    s1.append("<option value=''>- 全部类型 -</option>");
    s2.append("<option value=''>- 选择类型 -</option>");
    for (var i = 0; i < re.list.length; i++) {
      var n = re.list[i].name;
      var groupName = re.list[i].groupName;
      if(groupName){
        n += "（"+groupName+"）";
      }
      n = "<option value='"+re.list[i].id+"' title='"+re.list[i].typeCode+"'>" + n +"</option>";
      s1.append(n);
      s2.append(n);
    }
  });
  changeType();
}

function changeType() {
  // $("select#typeId").change(function () {
  //   var typeType = $(this).find("option:selected").attr("type");
  //   if (typeType == 1) {
  //     $(".detailTypeDiv").show();
  //     $(".cloudOwnNo").show();
  //     $(".cloudOwn").hide();
  //   }
  //   if (typeType == 2) {
  //     $("#detailType").val(2);
  //     $(".detailTypeDiv").hide();
  //     $(".cloudOwnNo").hide();
  //     $(".cloudOwn").show();
  //   }
  // });
}

$(function () {
  tableList.init({});
  wxArticle.init();
  initPlugins();
  // media_select.init();

  tagsUtil.initElement(tp, "NEWS");
  // regionRefTool.initElement(tp);
  $(".table-search-reset").click(function () {
    $("#tagsId").selectPageClear();
  });
});

function insertBr(id) {
  insertText(document.getElementById(id), "<br>");
}

function clearBr(id) {
  var old = $("#" + id).val();
  $("#" + id).val(old.replace(new RegExp("<br>", 'g'), ""));
}

//textarea光标位置插入值
function insertText(obj, str) {
  if (document.selection) {
    var sel = document.selection.createRange();
    sel.text = str;
  } else if (typeof obj.selectionStart === 'number' && typeof obj.selectionEnd === 'number') {
    var startPos = obj.selectionStart,
      endPos = obj.selectionEnd,
      cursorPos = startPos,
      tmpStr = obj.value;
    obj.value = tmpStr.substring(0, startPos) + str + tmpStr.substring(endPos, tmpStr.length);
    cursorPos += str.length;
    obj.selectionStart = obj.selectionEnd = cursorPos;
  } else {
    obj.value += str;
  }
}


// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
function formatDate(date, fmt) {
  var o = {
    "M+": date.getMonth() + 1,
    "d+": date.getDate(),
    "H+": date.getHours(),
    "m+": date.getMinutes(),
    "s+": date.getSeconds(),
    "S+": date.getMilliseconds()
  };
  //因为date.getFullYear()出来的结果是number类型的,所以为了让结果变成字符串型，下面有两种方法：
  if (/(y+)/.test(fmt)) {
    //第一种：利用字符串连接符“+”给date.getFullYear()+""，加一个空字符串便可以将number类型转换成字符串。
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
  }
  for (var k in o) {
    if (new RegExp("(" + k + ")").test(fmt)) {
      //第二种：使用String()类型进行强制数据类型转换String(date.getFullYear())，这种更容易理解。
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(String(o[k]).length)));
    }
  }
  return fmt;
}