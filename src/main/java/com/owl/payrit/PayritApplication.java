package com.owl.payrit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PayritApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayritApplication.class, args);
	}

}
