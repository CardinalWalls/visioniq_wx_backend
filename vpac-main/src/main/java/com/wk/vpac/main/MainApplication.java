

package com.wk.vpac.main;

import com.base.components.common.boot.BannerPrinter;
import com.base.components.common.boot.ServerPort;
import com.base.components.common.boot.SpringBootApplicationRunner;
import com.base.components.common.boot.UniqueBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.wk.vpac.**", nameGenerator = UniqueBeanNameGenerator.class)
public class MainApplication {

  public static void main(String[] args) {
    //0606 国际爱眼日
    SpringBootApplicationRunner.run(MainApplication.class, ServerPort.set(60606), BannerPrinter.create(), args);
  }
}
