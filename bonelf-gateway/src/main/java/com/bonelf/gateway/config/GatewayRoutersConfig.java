package com.bonelf.gateway.config;

import com.bonelf.gateway.handler.HystrixFallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * 路由配置信息
 */
@Slf4j
@Configuration
//@AllArgsConstructor
public class GatewayRoutersConfig {
    @Autowired
    private HystrixFallbackHandler hystrixFallbackHandler;

    @Bean
    public RouterFunction<?> routerFunction() {
        return RouterFunctions.route(
                RequestPredicates.path("/fallback").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), hystrixFallbackHandler);

    }

}
