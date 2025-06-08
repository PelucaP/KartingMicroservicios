package com.example.rack_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient // Or @EnableEurekaClient
@EnableFeignClients
public class RackServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RackServiceApplication.class, args);
	}

}
