

package com.wk.vpac.gateway;

import com.base.components.common.boot.BannerPrinter;
import com.base.components.common.boot.ServerPort;
import com.base.components.common.boot.SpringBootApplicationRunner;
import com.base.components.common.boot.UniqueBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * GatewayApplication
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2022-06-28 16:28
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.wk.vpac.**"}, nameGenerator = UniqueBeanNameGenerator.class)
public class GatewayApplication {
  public static void main(String[] args) {
    SpringBootApplicationRunner.run(GatewayApplication.class, ServerPort.set(10010),
                                    BannerPrinter.create(), args);
  }
}
