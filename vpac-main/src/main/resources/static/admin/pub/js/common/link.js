var link = {
  gotoOrder:function (orderCode) {
    if(orderCode){
      link.goto(ctx + "/admin/work/workOrder/index?orderCode=" + orderCode);
    }
  },
  gotoReceives:function (id, userPhone) {
    if(id || userPhone){
      var href = ctx + "/admin/work/workOrderReceives/index?_&";
      if(id){
        href += "&orderId=" + id;
      }
      if(userPhone){
        href += "&userPhone=" + userPhone;
      }
      link.goto(href);
    }
  },
  gotoOrderLogs:function (orderId, receiveId) {
    if(orderId || receiveId){
      var href = ctx + "/admin/work/workOrderLogs/index?_&";
      if(receiveId){
        href += "&orderReceiveId=" + receiveId;
      }
      if(orderId){
        href += "&orderId=" + orderId;
      }
      link.goto(href);
    }
  },
  gotoMerchant:function (orgCode) {
    if(orgCode){
      link.goto(ctx + "/admin/company/comMerchant/index?orgCode=" + orgCode);
    }
  },
  goto:function (href){
    if(top.index){
      top.index.showNav(href, true);
    }else{
      window.open(href);
    }
  }
}