package com.bonelf.common.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 微服务相关bean注入
 */
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
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
}
