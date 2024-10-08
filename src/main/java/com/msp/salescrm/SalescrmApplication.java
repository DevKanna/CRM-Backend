package com.msp.salescrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SalescrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalescrmApplication.class, args);
	}

}
