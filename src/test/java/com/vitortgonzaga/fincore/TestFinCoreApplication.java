package com.vitortgonzaga.fincore;

import org.springframework.boot.SpringApplication;

public class TestFinCoreApplication {

	static void main(String[] args) {
		SpringApplication.from(FinCoreApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
