package com.boutiquepierrotbleu.boutiquepierrotbleu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@EnableScheduling
@SpringBootApplication
public class BoutiquepierrotbleuApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoutiquepierrotbleuApplication.class, args);
	}

}
