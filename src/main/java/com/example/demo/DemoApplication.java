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

	@Component
	public class LogGenerator {
		private final Logger logger = LoggerFactory.getLogger(LogGenerator.class);
		private final Random random = new Random();
		private final String[] messages = {
				"Processing user request",
				"Checking database connection",
				"Validating input data",
				"Performing background task",
				"Cleaning up resources"
		};

		@Scheduled(fixedRate = 2000)
		public void generateInfoLogs() { // Renamed method
			logger.info(messages[random.nextInt(messages.length)]);
		}
	}
}

// TODO: Unit Test Updates
// This change significantly alters the behavior of the LogGenerator.
// Any existing unit tests for LogGenerator, especially those that might have
// asserted the presence of intentional exceptions or error log messages,
// must be updated or removed accordingly.
// New tests should be added if necessary to reflect the new intended behavior
// (i.e., only logging informational messages).