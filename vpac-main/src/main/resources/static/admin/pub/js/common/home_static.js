function showMore() {
  $("#more_button").hide();
  $("div[name=more_analysis]").show();
  initMore();
}
function initMore() {
  var myChart5 = echarts.init(document.getElementById('main5'), 'essos');
  var myChart6 = echarts.init(document.getElementById('main6'), 'essos');
  var myChart7 = echarts.init(document.getElementById('main7'), 'essos');
  var myChart8 = echarts.init(document.getElementById('main8'), 'essos');

  myChart5.showLoading();
  myChart6.showLoading();
  myChart7.showLoading();
  myChart8.showLoading();

  $.ajax(ctx + "/admin/home/ma/visit/distribution", {
    method: "GET",
    contentType: 'application/json;charset=utf-8',
    dataType: "json",
    success: function (res) {
      var data = res.data;
      var option5 = {
        title: {
          top: '15px',
          left: '25px',
          text: '访问时长'
        },
        tooltip: {
          trigger: 'axis'
        },
        toolbox: {
          top: '15px',
          right: '25px',
          feature: {
            saveAsImage: {}
          }
        },
        grid: {
          left: '10px',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: data.visitTimeName
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: data.visitTimeVal,
          type: 'bar'
        }]
      };
      myChart5.hideLoading();
      myChart5.setOption(option5);
      var option6 = {
        title: {
          top: '15px',
          left: '25px',
          text: '访问深度'
        },
        tooltip: {
          trigger: 'axis'
        },
        toolbox: {
          top: '15px',
          right: '25px',
          feature: {
            saveAsImage: {}
          }
        },
        grid: {
          left: '10px',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: data.accessDepthName
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: data.accessDepthVal,
          type: 'bar'
        }]
      };
      myChart6.hideLoading();
      myChart6.setOption(option6);

    }
  });
  $.ajax(ctx + "/admin/home/ma/visit/portrait", {
    method: "GET",
    contentType: 'application/json;charset=utf-8',
    dataType: "json",
    success: function (res) {
      var data = res.data;
      var option7 = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        title: {
          top: '15px',
          left: '25px',
          text: '年龄分布'
        },
        toolbox: {
          top: '15px',
          right: '25px',
          feature: {
            saveAsImage: {}
          }
        },
        legend: {
          top: '20px',
          orient: 'horizontal',
          data: data.ageNames
        },
        series: [
          {
            name: '年龄分布',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '30',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: true
            },
            data: data.ageValue
          }
        ]
      };
      myChart7.hideLoading();
      myChart7.setOption(option7);
      var option8 = {
        title : {
          top: '15px',
          text: '访问地区分布',
          x:'center'
        },
        tooltip : {
          trigger: 'item'
        },
        dataRange: {
          left: '35px',
          min: 0,
          max: data.provinceMax === undefined ? 1000 : data.provinceMax,
          x: 'left',
          y: 'bottom',
          text:['高','低'],
          calculable : true,
          inRange: {
            color: ['#ffffff', '#0090bc']
          }
        },
        toolbox: {
          show: true,
          orient : 'vertical',
          y: 'center',
          right: '35px',
          feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            restore : {show: true},
            saveAsImage : {show: true}
          }
        },
        series :{
            name: '访问量',
            type: 'map',
            mapType: 'china',
            roam: false,
            itemStyle:{
              normal:{label:{show:true}},
              emphasis:{
                label:{show:true},
                areaColor: "#BCBCDD"
              }
            },
            data: data.provinceValue === undefined ? [] : data.provinceValue
          }
      };
      myChart8.hideLoading();
      myChart8.setOption(option8);
    }
  });
}
// 基于准备好的dom，初始化echarts实例

$(function () {
  var myChart1 = echarts.init(document.getElementById('main1'), 'essos');
  var myChart2 = echarts.init(document.getElementById('main2'), 'essos');
  var myChart3 = echarts.init(document.getElementById('main3'), 'essos');
  var myChart4 = echarts.init(document.getElementById('main4'), 'essos');

  myChart1.showLoading();
  myChart2.showLoading();
  myChart3.showLoading();
  myChart4.showLoading();
  $.ajax(ctx + "/admin/home/total", {
    method: "GET",
    contentType: 'application/json;charset=utf-8',
    dataType: "json",
    success: function (res) {
      var data = res.data;
      var vipIncomeTotal = data.vipIncomeTotal?data.vipIncomeTotal:0;
      $("#vip_income_total").html("￥"+formatNumber(vipIncomeTotal ,2));
      var questionTotal = data.questionTotal?data.questionTotal:0;
      $("#question_total").html("￥"+(formatNumber(questionTotal ,2)));
      $("#user_total").html(data.userTotal);
      $("#vip_total").html(data.vipTotal);
    }
  });


  //小程序概况
  $.ajax(ctx + "/admin/home/ma/summary", {
    method: "GET",
    contentType: 'application/json;charset=utf-8',
    dataType: "json",
    success: function (res) {
      $("#more_button").show();
      var data = res.data;
      var option1 = {
        title: {
          top: '15px',
          left: '25px',
          text: '小程序概况'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          top: '20px',
          data: ['累计用户数', '转发次数', '转发人数']
        },
        grid: {
          left: '20px',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          top: '15px',
          right: '25px',
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: data.date.reverse()
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '累计用户数',
            type: 'line',
            data: data.visitTotal.reverse()
          },
          {
            name: '转发次数',
            type: 'line',
            data: data.sharePv.reverse()
          },
          {
            name: '转发人数',
            type: 'line',
            data: data.shareUv.reverse()
          }
        ]
      };
      myChart1.setOption(option1);
      myChart1.hideLoading();
    }
  });
  $.ajax(ctx + "/admin/home/ma/visit", {
    method: "GET",
    contentType: 'application/json;charset=utf-8',
    dataType: "json",
    success: function (res) {
      var data = res.data;
      var option2 = {
        title: {
          top: '15px',
          left: '25px',
          text: '小程序访问趋势'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          top: '20px',
          data: ['打开次数', '访问次数', '访问人数', '新用户数']
        },
        grid: {
          left: '35px',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          top: '15px',
          right: '25px',
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: data.date.reverse()
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '打开次数',
            type: 'line',
            data: data.sessionCnt.reverse()
          },
          {
            name: '访问次数',
            type: 'line',
            data: data.visitPv.reverse()
          },
          {
            name: '访问人数',
            type: 'line',
            data: data.visitUv.reverse()
          },
          {
            name: '新用户数',
            type: 'line',
            data: data.visitUvNew.reverse()
          }
        ]
      };
      myChart2.hideLoading();
      myChart2.setOption(option2);

      var option3 = {
        title: {
          top: '15px',
          left: '25px',
          text: '停留时长趋势'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          top: '20px',
          data: ['次均停留时长', '人均停留时长', "平均访问深度"]
        },
        grid: {
          left: '35px',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          top: '15px',
          right: '25px',
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: data.date
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '次均停留时长',
            type: 'line',
            data: data.stayTimeSession.reverse()
          },
          {
            name: '人均停留时长',
            type: 'line',
            data: data.stayTimeUv.reverse()
          },
          {
            name: '平均访问深度',
            type: 'line',
            data: data.visitDepth.reverse()
          }
        ]
      };
      myChart3.hideLoading();
      myChart3.setOption(option3);
    }
  });
  $.ajax(ctx + "/admin/home/ma/visit/distribution", {
    method: "GET",
    contentType: 'application/json;charset=utf-8',
    dataType: "json",
    success: function (res) {
      var data = res.data;
      var option4 = {
        title: {
          top: '15px',
          left: '25px',
          text: '来源分布'
        },
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '10px',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          top: '15px',
          right: '25px',
          feature: {
            saveAsImage: {}
          }
        },
        legend: {
          top: '20px',
          data: ['小程序历史列表', '搜索', '会话', '二维码']
        },
        xAxis: {
          type: 'category',
          data: data.accessSource
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: data.accessSourceVal,
          type: 'bar'
        }]
      };
      myChart4.hideLoading();
      myChart4.setOption(option4);
    }
  });
});