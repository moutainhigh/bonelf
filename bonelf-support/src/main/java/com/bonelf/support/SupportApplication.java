package com.bonelf.support;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>
 * 服务支持 服务
 * </p>
 * @author bonelf
 * @since 2020/10/6 18:58
 */
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.bonelf")
@EnableCircuitBreaker
// mybatisplus配置动态数据源时，切记需要关闭自带的自动数据源配置 exclude = DruidDataSourceAutoConfigure.class
@SpringBootApplication(scanBasePackages = {"com.bonelf.common", "com.bonelf.support"}, exclude = DruidDataSourceAutoConfigure.class)
public class SupportApplication {

	public static void main(String[] args) throws UnknownHostException {
		ConfigurableApplicationContext application = SpringApplication.run(SupportApplication.class, args);
		Environment env = application.getEnvironment();
		String ip = InetAddress.getLocalHost().getHostAddress();
		String port = env.getProperty("server.port");
		String path = StrUtil.nullToDefault(env.getProperty("server.servlet.context-path"), "/");
		String portWebSocket = StrUtil.nullToDefault(env.getProperty("ws.port"), "8026");
		String pathWebSocket = StrUtil.nullToDefault(env.getProperty("ws.context-path"), "/");
		log.info("\n----------------------------------------------------------\n\t" +
				"Support Application is running! Access URLs:\n\t" +
				"Local: \t\thttp://localhost:" + port + path + "\n\t" +
				"External: \thttp://" + ip + ":" + port + path + "\n\t" +
				"Websocket: \tws://" + ip + ":" + portWebSocket + pathWebSocket + "\n" +
				"----------------------------------------------------------");
	}
}