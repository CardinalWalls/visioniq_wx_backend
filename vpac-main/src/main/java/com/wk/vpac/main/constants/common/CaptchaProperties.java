package com.wk.vpac.main.constants.common;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * CaptchaProperties
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2022-09-30 15:14
 */
@ConfigurationProperties("base.captcha")
public class CaptchaProperties {
  /** 验证码参数 {@link Config} */
  private Properties props = new Properties();

  public DefaultKaptcha build() {
    Config config = new Config(props);
    DefaultKaptcha kp = new DefaultKaptcha();
    kp.setConfig(config);
    return kp;
  }

  public Properties getProps() {
    return props;
  }

  public void setProps(Properties props) {
    this.props = props;
  }
}
