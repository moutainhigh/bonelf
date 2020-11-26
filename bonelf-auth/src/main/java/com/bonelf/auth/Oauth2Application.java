package com.bonelf.auth;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.bonelf.cicada.util.IpUtil;
import com.bonelf.common.config.security.WebServerSecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.bonelf.common", "com.bonelf.auth"})
@EnableCircuitBreaker
//不知道为何在 @SpringBootApplication exclude WebServerSecurityConfig 不行
@ComponentScan(value = {"com.bonelf.common", "com.bonelf.auth"},
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebServerSecurityConfig.class}))
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
public class Oauth2Application {
	public static void main(String[] args) {
		ConfigurableApplicationContext application = SpringApplication.run(Oauth2Application.class, args);
		Environment env = application.getEnvironment();
		//String ip = InetAddress.getLocalHost().getHostAddress();
		String ip = IpUtil.getWlanV4Ip();
		String port = env.getProperty("server.port");
		String path = env.getProperty("server.servlet.context-path");
		log.info("\n----------------------------------------------------------\n\t" +
				"Application is running! Access URLs:\n\t" +
				"Local: \t\thttp://localhost:" + port + path + "\n\t" +
				"External: \thttp://" + ip + ":" + port + path + "\n" +
				"----------------------------------------------------------");
	}

}
