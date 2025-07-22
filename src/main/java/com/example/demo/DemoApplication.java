package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Random;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// @Component
	// public class LogGenerator {
	// 	private final Logger logger = LoggerFactory.getLogger(LogGenerator.class);
	// 	private final Random random = new Random();
	// 	private final String[] messages = {
	// 		"Processing user request",
	// 		"Checking database connection",
	// 		"Validating input data",
	// 		"Performing background task",
	// 		"Cleaning up resources"
	// 	};

	// 	@Scheduled(fixedRate = 2000)
	// 	public void generateLogsAndExceptions() {
	// 		logger.info(messages[random.nextInt(messages.length)]);

	// 		if (random.nextInt(100) < 30) {
	// 			String[] exceptions = {
	// 				"NullPointerException",
	// 				"IllegalArgumentException",
	// 				"RuntimeException",
	// 				"ArrayIndexOutOfBoundsException"
	// 			};
	// 			String exceptionType = exceptions[random.nextInt(exceptions.length)];
	// 			try {
	// 				throw new RuntimeException("Random " + exceptionType + " occurred!");
	// 			} catch (Exception e) {
	// 				logger.error("Exception occurred", e);
	// 			}
	// 		}
	// 	}
	// }
}