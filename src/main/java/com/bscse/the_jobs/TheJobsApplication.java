package com.bscse.the_jobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TheJobsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TheJobsApplication.class, args);
	}

}
