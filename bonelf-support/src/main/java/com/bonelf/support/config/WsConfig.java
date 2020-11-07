package com.bonelf.support.config;

import com.bonelf.support.WsServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WsConfig {
	@Bean
	public WsServer wsServer() {
		return WsServer.getInstance();
	}
}
