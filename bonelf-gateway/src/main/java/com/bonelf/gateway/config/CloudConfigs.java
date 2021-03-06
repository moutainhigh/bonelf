package com.bonelf.gateway.config;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 微服务相关bean注入
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.bonelf")
@EnableCircuitBreaker
@Configuration
public class CloudConfigs {


	/**
	 * <p>
	 * 使用restTemplate进行服务间调用
	 * </p>
	 * @author bonelf
	 * @since 2020/10/5 21:08
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
