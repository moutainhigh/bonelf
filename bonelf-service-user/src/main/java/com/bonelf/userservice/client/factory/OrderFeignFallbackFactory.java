package com.bonelf.userservice.client.factory;

import com.bonelf.userservice.client.OrderFeignClient;
import com.bonelf.userservice.client.fallback.OrderServiceFallback;
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