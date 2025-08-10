package com.wk.vpac.main.service.admin.operation.sms;

import com.wk.vpac.domain.sys.SmsSendRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * SmsUsuallyService
 *
 * @author <a href="lijian_jie@163.com">LiJian</a>
 * @version 1.0.0, 2018-06-25 11:51
 */
@Service
//@RefreshScope
@EnableConfigurationProperties(SmsUsuallyParamService.BaseSmsParams.class)
public class SmsUsuallyParamService {

  @Autowired
  private Map<String, SmsUsuallyParamInterface> paramInterface;
  @Autowired
  private BaseSmsParams baseSmsParams;


  public Map<String, Object> execute(SmsSendRecord record,String type,List<String> code,Map<String, Object> extra) {
    return paramInterface.get(baseSmsParams.getMapping().get(type)).loadParamValues(record,code,extra);
  }


  @ConfigurationProperties("base.admin.sms.param")
  public static class BaseSmsParams{
    /**
     * 业务类型与参数接口实现关系映射
     */
    private Map<String,String> mapping;

    public Map<String, String> getMapping() {
      return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
      this.mapping = mapping;
    }
  }
}
