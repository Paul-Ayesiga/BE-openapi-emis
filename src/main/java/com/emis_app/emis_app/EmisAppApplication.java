package com.emis_app.emis_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EmisAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmisAppApplication.class, args);
	}

}
