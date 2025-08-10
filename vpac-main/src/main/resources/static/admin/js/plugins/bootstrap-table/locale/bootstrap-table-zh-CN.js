/**
 * Bootstrap Table Chinese translation
 * Author: Zhixin Wen<wenzhixin2010@gmail.com>
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['zh-CN'] = {
        formatLoadingMessage: function () {
            return '正在努力地加载数据中，请稍候……';
        },
        formatRecordsPerPage: function (pageNumber) {
            return '每页显示 ' + pageNumber + ' 条记录';
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return '显示第 ' + pageFrom + ' 到第 ' + pageTo + ' 条记录，总共 ' + totalRows + ' 条记录';
        },
        formatSearch: function () {
            return '搜索';
        },
        formatNoMatches: function () {
            try {
                var m = this.tableContent.prevObject.data("bootstrap-error-msg");
                if(m){
                    this.tableContent.prevObject.removeData("bootstrap-error-msg");
                    return m;
                }
            } catch (e) {}
            return '没有找到匹配的记录';
        },
        formatPaginationSwitch: function () {
            return '隐藏/显示分页';
        },
        formatRefresh: function () {
            return '刷新';
        },
        formatToggle: function () {
            return '切换样式';
        },
        formatColumns: function () {
            return '列显示';
        },
        formatExport:function(){
            return '导出数据';
        },
        formatMultipleSort:function(){
            return '多列排序';
        },
        formatAddLevel:function(){
            return '添加排序列至尾端';
        },
        formatDeleteLevel:function(){
            return '去除尾端排序列';
        },
        formatColumn:function(){
            return '排序列';
        },
        formatOrder: function() {
            return '排序方向';
        },
        formatSortBy: function() {
            return '首列';
        },
        formatThenBy: function() {
            return '依次列';
        },
        formatSort: function() {
            return '排序';
        },
        formatCancel: function() {
            return '取消';
        },
        formatDuplicateAlertTitle: function() {
            return '检查到重复的列! ';
        },
        formatDuplicateAlertDescription: function() {
            return '请删除或更改重复的列.';
        },
        formatSortOrders: function() {
            return {
              asc: '升序',
              desc: '降序'
            };
        },
        formatAutoRefresh: function() {
          return '自动刷新'+(this.autoRefreshInterval?'('+this.autoRefreshInterval+'秒)':'');
        }

    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['zh-CN']);

})(jQuery);
