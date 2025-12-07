package com.mypensamiento.mypensamiento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;



@SpringBootApplication
public class MyPensamientoApplication {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(MyPensamientoApplication.class);

	public static void main(String[] args) {




		SpringApplication.run(MyPensamientoApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void logStartup() {
		logger.info("MyPensamiento is ready");
	}

}
