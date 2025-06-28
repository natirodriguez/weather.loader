package com.example.weather.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class);

	public static void main(String[] args) {
		
		logger.info("Corriendo Weather Loader.");

		SpringApplication.run(Application.class, args);
	}

}
