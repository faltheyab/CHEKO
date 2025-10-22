package com.faisal.cheko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the Cheko application.
 * Enables scheduling for database connection monitoring.
 */
@SpringBootApplication
@EnableScheduling
public class ChekoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChekoApplication.class, args);
	}

}
