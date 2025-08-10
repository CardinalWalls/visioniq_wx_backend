<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>

<@headContent/>
<@bodyContent>
 <div class="wrapper wrapper-content animated fadeInRight">
   <div class="row">
     <#list statisticsList as sts>
       <div class="col-sm-3">
         <div class="ibox">
           <div class="ibox-content">
             <h4>${sts.name}</h4>
             <h2 class="no-margins">${sts.val}</h2>
           </div>
         </div>
       </div>
     </#list>
   </div>
   <div class="row">
     <div class="col-xs-6">
       <div class="ibox">
         <div class="ibox-content">
           <h3>用户增长趋势</h3>
           <div id="userRegistryChart" style="height:300px"></div>
         </div>
       </div>
     </div>
     <div class="col-xs-6">
       <div class="ibox">
         <div class="ibox-content">
           <h3>订单资金增长趋势</h3>
           <div id="transactionChart" style="height:300px"></div>
         </div>
       </div>
     </div>
   </div>
 </div>

<script src="${ctx!}/admin/js/plugins/echarts/echarts.min.js"></script>
<script>
  var userRegistryChartData = JSON.parse('${(userRegistryChart!=null)?string(userRegistryChart,"{}")}');
  var transactionChartData = JSON.parse('${(transactionChart!=null)?string(transactionChart,"{}")}');
  var buildLineChart = function (divId, color, labelTitle, charData) {
    echarts.init(document.getElementById(divId)).setOption({
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: charData.x
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        name:labelTitle,
        data: charData.y,
        type: 'line',
        smooth: true,
        areaStyle: {},
        itemStyle: {
          color: color
        }
      }]
    });
  };
  $(document).ready(function () {
    buildLineChart("userRegistryChart", '#23b7e5', "注册人数", userRegistryChartData);
    buildLineChart("transactionChart", '#e53f5e', "订单总金额", transactionChartData);
  });
</script>
</@bodyContent>