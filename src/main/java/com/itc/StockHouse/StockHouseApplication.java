package com.itc.StockHouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockHouseApplication {
	public static void main(String[] args) {
		SpringApplication.run(StockHouseApplication.class, args);
	}
}
