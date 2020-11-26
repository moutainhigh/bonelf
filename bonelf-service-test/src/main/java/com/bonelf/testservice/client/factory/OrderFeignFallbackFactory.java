package com.bonelf.testservice.client.factory;

import com.bonelf.testservice.client.OrderFeignClient;
import com.bonelf.testservice.client.fallback.OrderServiceFallback;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderFeignFallbackFactory implements FallbackFactory<OrderFeignClient> {

    @Override
    public OrderFeignClient create(Throwable throwable) {
        OrderServiceFallback fallback = new OrderServiceFallback();
        log.error("Feign error", throwable);
        fallback.setCause(throwable);
        return fallback;
    }
}