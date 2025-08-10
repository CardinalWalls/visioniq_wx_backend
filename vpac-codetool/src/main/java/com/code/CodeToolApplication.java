package com.code;

import com.base.components.common.boot.SpringBootApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.code"})
public class CodeToolApplication {

	public static void main(String[] args) {
		SpringBootApplicationRunner.run(CodeToolApplication.class, args);
	}

}
