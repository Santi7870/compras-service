package com.agrolink360.compras_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ComprasServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ComprasServiceApplication.class, args);
	}
}
