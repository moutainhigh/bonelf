package com.bonelf.gateway.client.factory;

import com.bonelf.gateway.client.UserFeignClient;
import com.bonelf.gateway.client.fallback.UserServiceFallback;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserFeignFallbackFactory implements FallbackFactory<UserFeignClient> {

    @Override
    public UserFeignClient create(Throwable throwable) {
        UserServiceFallback fallback = new UserServiceFallback();
        fallback.setCause(throwable);
        return fallback;
    }
}