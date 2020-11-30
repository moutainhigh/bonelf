package com.bonelf.search;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.bonelf.cicada.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * <p>
 * 默认情况下，扫描入口类同级及其子级包下的所有文件，这里配置了扫描公共配置
 * 注入Mapper显示NoBean因为Common的@MapperScan idea找不到，不影响使用
 * </p>
 * @author bonelf
 * @since 2020/10/6 18:58
 */
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.bonelf")
@EnableCircuitBreaker
@SpringBootApplication(scanBasePackages = {"com.bonelf.common", "com.bonelf.system"}, exclude = DruidDataSourceAutoConfigure.class)
public class SystemServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext application = SpringApplication.run(SystemServiceApplication.class, args);
		Environment env = application.getEnvironment();
		//String ip = InetAddress.getLocalHost().getHostAddress();
		String ip = IpUtil.getWlanV4Ip();
		String port = env.getProperty("server.port");
		String path = env.getProperty("server.servlet.context-path");
		log.info("\n----------------------------------------------------------\n\t" +
				"Application is running! Access URLs:\n\t" +
				"Local: \t\thttp://localhost:" + port + path + "\n\t" +
				"External: \thttp://" + ip + ":" + port + path + "\n\t" +
				"----------------------------------------------------------");
	}
}