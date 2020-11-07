package com.bonelf.testservice.client.factory;

import com.bonelf.testservice.client.OrderFeignClient;
import com.bonelf.testservice.client.fallback.OrderServiceFallback;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderFeignFallbackFactory implements FallbackFactory<OrderFeignClient> {

    @Override
    public OrderFeignClient create(Throwable throwable) {
        OrderServiceFallback fallback = new OrderServiceFallback();
        fallback.setCause(throwable);
        return fallback;
    }
}