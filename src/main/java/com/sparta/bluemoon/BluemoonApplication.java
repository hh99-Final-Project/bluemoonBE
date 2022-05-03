package com.sparta.bluemoon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BluemoonApplication {

	public static void main(String[] args) {
		SpringApplication.run(BluemoonApplication.class, args);
	}

}
