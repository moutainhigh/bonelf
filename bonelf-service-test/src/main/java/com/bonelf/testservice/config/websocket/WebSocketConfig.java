package com.bonelf.testservice.config.websocket;

import com.bonelf.testservice.core.websocket.WebSocketInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;

/**
 * 配置类 ,采用redis发布订阅功能区做websocket消息的不通服务之间的传递
 **/
//@Configuration
//@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	@Resource
	private WebSocketHandler mainWebSocketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		///{userId}
		registry.addHandler(mainWebSocketHandler, "/")
				.setAllowedOrigins("*").addInterceptors(webSocketInterceptor());
	}

	@Bean
	public HandshakeInterceptor webSocketInterceptor() {
		return new WebSocketInterceptor();
	}

	//@Bean
	//public WebSocketHandler zeusWebSocketHandler() {
	//	return new MainWebSocketHandler();
	//}

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler threadPoolScheduler = new ThreadPoolTaskScheduler();
		threadPoolScheduler.setThreadNamePrefix("SockJS-");
		threadPoolScheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
		threadPoolScheduler.setRemoveOnCancelPolicy(true);
		return threadPoolScheduler;
	}
}