package com.bonelf.auth;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.bonelf")
@EnableCircuitBreaker
@SpringBootApplication(scanBasePackages = {"com.bonelf.common", "com.bonelf.auth"}, exclude = DruidDataSourceAutoConfigure.class)
public class Oauth2Application {
	public static void main(String[] args) {
		SpringApplication.run(Oauth2Application.class, args);
	}

}
