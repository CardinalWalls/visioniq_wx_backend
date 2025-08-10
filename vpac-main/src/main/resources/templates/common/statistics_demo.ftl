<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<script type="application/javascript" src="${ctx!}/admin/js/plugins/echarts/echarts.min.js"></script>
<script type="application/javascript" src="${ctx!}/admin/pub/js/common/static_theme.js"></script>
<style>
  .panel,.ibox{
    margin-bottom:10px !important;
  }
  .padder-v{
    padding:0;
    margin-bottom:10px;
  }
  .padder-v div:first-child{
    word-break:break-word;
  }
  @media (max-width:512px){
    .padder-v div:first-child{
      font-size:18px;
    }
  }
  .handler{
    cursor: pointer;
    color: #fff;
    transition: background 1s;
    padding:0 !important;
  }
  .handler.on-warning{
    background: #ff6645 !important;
  }
  .handler-content{
    /* 一排6个提醒框 */
    width:calc(100% / 4 - 20px);
    margin-left:10px;
    margin-right:10px;
    float:left;
  }
</style>
<@headContent/>

<@bodyContent>
  <div class="wrapper wrapper-content animated fadeInRight">
    <div class="text-center ibox" style="margin-top:-25px;">
      <h3>
        <img style="height:60px;vertical-align:top;margin-right:10px;" src="${ctx!}/admin/img/logo.png" onerror="this.remove()"/>
        <label style="font-size:33px;margin-top:12px;color:#000;">${systemName}</label>
      </h3>
    </div>

    <div class="row row-sm text-center">
<#--      <div class="col-xs-12 text-left"><b style="font-size:18px">综合数据</b></div>-->
      <div class="col-xs-3">
        <div class="panel padder-v item bg-primary cursor-pointer" title="只统计“已支付”和“支付中”" style="margin-right: -7px;"
             onclick="window.top.index.showNav(ctx + '/admin/company/comRemuneration/index?status=1,2', true)">
          <div class="h2 text-info font-thin h1" style="color: white"><span>￥</span><span class="data-element money" id="payedRemuneration">0</span></div>
          <span class="text-fff text-xs" >x1</span>
          <div class="m-b-sm"></div>
        </div>
      </div>
      <div class="col-xs-3">
        <div class="panel padder-v item bg-primary cursor-pointer" title="只统工单状态在含“接单中”之后且有已审核接单人的" style="background-color: #688cf9;margin-left: -2px;margin-right: -5px;"
             onclick="window.top.index.showNav(ctx + '/admin/work/workOrderReceives/index?auditStatus=1', true)">
          <div class="h2 text-fff font-thin h1"><span>￥</span><span class="data-element money" id="totalCommission">0</span></div>
          <span class="text-fff text-xs">x2</span>
          <div class="m-b-sm"></div>
        </div>
      </div>
      <div class="col-xs-3">
        <div class="panel padder-v item bg-info cursor-pointer" style="margin-left:-5px;margin-right: -2px;"
             onclick="window.top.index.showNav(ctx + '/admin/user/userBaseInfo/index', true)">
          <div class="h2 font-thin data-element" id="userType_1" style="color: white">0</div>
          <span class="text-fff text-xs">x3</span>
          <div class="m-b-sm"></div>
        </div>
      </div>
      <div class="col-xs-3">
        <div class="panel padder-v item bg-warning cursor-pointer" style="background-color:#da9db3;margin-left: -7px;"
             onclick="window.top.index.showNav(ctx + '/admin/company/comMerchant/index', true)">
          <div class="h2 text-fff font-thin h1 data-element" id="userType_2">0</div>
          <span class="text-fff text-xs">x4</span>
          <div class="m-b-sm"></div>
        </div>
      </div>
    </div>

    <div class="row row-sm text-center" style="padding: 0 5px">
      <div class="handler-content">
        <div class="panel padder-v item handler" style="background:#92aeec" title="只统计“待支付”和“支付失败”"
             onclick="window.top.index.showNav(ctx + '/admin/company/comRemuneration/index?status=0,-1', true)">
          <div class="h3 font-thin h1" style="margin:6px"><span>￥</span><span class="data-element money" id="todoRemuneration">0</span></div>
          <span class="text-xs">x5</span>
          <div class="m-b-sm"></div>
        </div>
      </div>
      <div class="handler-content">
        <div class="panel padder-v item handler" style="background:#92aeec" title="待审核的接单人员"
             onclick="window.top.index.showNav(ctx + '/admin/work/workOrderReceives/index?auditStatus=0', true)">
          <div class="h3 font-thin h1" style="margin:6px" id="todoReceives">0</div>
          <span class="text-xs">x6</span>
          <div class="m-b-sm"></div>
        </div>
      </div>
      <div class="handler-content">
        <div class="panel padder-v item handler" style="background:#92aeec" title="已审核的接单人员"
             onclick="window.top.index.showNav(ctx + '/admin/work/workOrderReceives/index?auditStatus=1', true)">
          <div class="h3 font-thin h1" style="margin:6px" id="acceptReceives">0</div>
          <span class="text-xs">x7</span>
          <div class="m-b-sm"></div>
        </div>
      </div>
      <div class="handler-content">
        <div class="panel padder-v item handler" style="background:#92aeec" title="工单状态在含“接单中”之后的"
             onclick="window.top.index.showNav(ctx + '/admin/work/workOrder/index?status=2,3,4,5', true)">
          <div class="h3 font-thin h1" style="margin:6px" id="totalOrder">0</div>
          <span class="text-xs">x8</span>
          <div class="m-b-sm"></div>
        </div>
      </div>
    </div>

    <div class="row row-sm text-center" style="padding: 0 5px">
      <div class="handler-content">
        <div class="panel padder-v item handler" style="background:#92aeec"
             onclick="window.top.index.showNav(ctx + '/admin/work/workOrder/index?status=2', true)">
          <div class="h3 font-thin h1" style="margin:6px" id="orderStatus_2">0</div>
          <span class="text-xs">c1</span>
          <div class="m-b-sm"></div>
        </div>
      </div>
      <div class="handler-content">
        <div class="panel padder-v item handler" style="background:#a4a7f3"
             onclick="window.top.index.showNav(ctx + '/admin/work/workOrder/index?status=3', true)">
          <div class="h3 font-thin h1" style="margin:6px" id="orderStatus_3">0</div>
          <span class="text-xs">c2</span>
          <div class="m-b-sm"></div>
        </div>
      </div>
      <div class="handler-content">
        <div class="panel padder-v item handler" style="background:#ada4f3"
             onclick="window.top.index.showNav(ctx + '/admin/work/workOrder/index?status=4', true)">
          <div class="h3 font-thin" style="margin:6px;" id="orderStatus_4">0</div>
          <span class="text-xs">c3</span>
          <div class="m-b-sm"></div>
        </div>
      </div>
      <div class="handler-content">
        <div class="panel padder-v item handler" style="background:#a593ed"
             onclick="window.top.index.showNav(ctx + '/admin/work/workOrder/index?status=5', true)">
          <div class="h3 font-thin" style="margin:6px;" id="orderStatus_5">0</div>
          <span class="text-xs">c4</span>
          <div class="m-b-sm"></div>
        </div>
      </div>
    </div>


    <div class="ibox">
      <div class="ibox-content">
        <div id="main" style="width:100%;height:220px;z-index:100"></div>
        <div style="margin-top:-35px;margin-bottom:-10px;position:relative;z-index:200">
          统计方式：
          <select id="remunerationUnitChartByDays" class="form-control chart-param" style="width:80px;display:inline-block">
            <option value="true">每天</option>
            <option value="false">每月</option>
          </select>
        </div>
      </div>
    </div>
    <div class="ibox">
      <div class="ibox-content">
        <div id="main1" style="width: 100%;height:220px;z-index:100"></div>
        <div style="margin-top:-35px;margin-bottom:-10px;position:relative;z-index:200">
          统计方式：
          <select id="remunerationCumulativeChartByDays" class="form-control chart-param" style="width:80px;display:inline-block">
            <option value="true">每天</option>
            <option value="false">每月</option>
          </select>
        </div>
      </div>
    </div>
  </div>

  <!-- Sparkline -->
  <script src="/admin/js/plugins/sparkline/jquery.sparkline.min.js"></script>
  <!-- Peity -->
  <script src="/admin/js/plugins/peity/jquery.peity.min.js"></script>
  <!-- 自定义js -->
  <script src="/admin/js/content.js?v=1.0.0"></script>
  <!-- peity demo data -->
  <script src="/admin/js/demo/peity-demo.js"></script>
  <script>
    $(document).ready(function () {


      $("#sparkline1").sparkline([34, 43, 43, 35, 44, 32, 44, 52], {
        type: 'line',
        width: '100%',
        height: '60',
        lineColor: '#1ab394',
        fillColor: "#ffffff"
      });

      $("#sparkline2").sparkline([24, 43, 43, 55, 44, 62, 44, 72], {
        type: 'line',
        width: '100%',
        height: '60',
        lineColor: '#1ab394',
        fillColor: "#ffffff"
      });

      $("#sparkline3").sparkline([74, 43, 23, 55, 54, 32, 24, 12], {
        type: 'line',
        width: '100%',
        height: '60',
        lineColor: '#ed5565',
        fillColor: "#ffffff"
      });

      $("#sparkline4").sparkline([24, 43, 33, 55, 64, 72, 44, 22], {
        type: 'line',
        width: '100%',
        height: '60',
        lineColor: '#ed5565',
        fillColor: "#ffffff"
      });

      $("#sparkline5").sparkline([1, 4], {
        type: 'pie',
        height: '140',
        sliceColors: ['#1ab394', '#F5F5F5']
      });

      $("#sparkline6").sparkline([5, 3], {
        type: 'pie',
        height: '140',
        sliceColors: ['#1ab394', '#F5F5F5']
      });

      $("#sparkline7").sparkline([2, 2], {
        type: 'pie',
        height: '140',
        sliceColors: ['#ed5565', '#F5F5F5']
      });

      $("#sparkline8").sparkline([2, 3], {
        type: 'pie',
        height: '140',
        sliceColors: ['#ed5565', '#F5F5F5']
      });


    });
  </script>
  <script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'), 'essos');
    var myChart1 = echarts.init(document.getElementById('main1'), 'essos');
    $(function(){
      var queryChartData = function (){
        $.get(ctx + "/admin/home/statics/chart", {
          remunerationUnitChartByDays:$("#remunerationUnitChartByDays").val(),
          remunerationCumulativeChartByDays:$("#remunerationCumulativeChartByDays").val(),
        }, function (res) {
          var data = res.data;
          if(res.success){
            buildDaysChart(data.remunerationUnitChart, "已发劳务报酬金额统计", {"v": "金额(元)"}, myChart);
            buildDaysChart(data.remunerationCumulativeChart, "已发劳务报酬金额增长趋势", {"v": "金额(元)"}, myChart1);
          }
        }, "json");
      };
      var queryData = function(){
        $.get(ctx + "/admin/home/statics", function (res) {
          var data = res.data;
          if(res.success){
            for (var k in data) {
              var v = data[k];
              buildNumber(k, v);
            }
          }
        }, "json");
        queryChartData();
      };
      queryData();
      $(".chart-param").on("change",function (){
        queryChartData();
      });
      setInterval(queryData, 300000);
      setInterval(function(){
        $(".handler").each(function () {
          var $this = $(this);
          if($this.hasClass("todo")){
            $this.toggleClass("on-warning");
          }
          else{
            $this.removeClass("on-warning");
          }
        });
      }, 1000);
    });
    var buildNumber= function(id, number){
      var e = $("#" + id);
      if(e.length > 0){
        number = 1 * (number?number:0);
        e.html(e.hasClass("money") ? number.toFixed(2) : number.toFixed(0));
        if(id === 'todoRemuneration' || id === 'todoReceives'){
          if(number > 0){
            e.parents(".handler").addClass("todo");
          }
        }
      }
    };
    var buildDaysChart = function (list, chartName, valueMap, chart) {
      var x = [];
      var yMap = {};
      for (const vKey in valueMap) {
        yMap[vKey] = {name:valueMap[vKey], value:[]};
      }
      for (let i = 0; i < list.length; i++) {
        var item = list[i];
        x.push(item["unitTime"]);
        for (const vKey in yMap) {
          var array = yMap[vKey].value;
          var v = item[vKey];
          array.push(v === undefined ? 0 : v);
        }
      }
      var series = [];
      var legend = [];
      for (const vKey in yMap) {
        var val = yMap[vKey];
        legend.push(val.name);
        series.push({
          name: val.name,
          type: 'line',
          data: val.value
        });
      }
      var option = {
        title: {
          text: chartName
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data:legend
        },
        xAxis: {
          data: x
        },
        yAxis: {},
        series: series
      };
      chart.setOption(option);
    };
  </script>
</@bodyContent>