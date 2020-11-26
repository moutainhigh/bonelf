package com.bonelf.common.client.factory;

import com.bonelf.common.client.UserFeignClient;
import com.bonelf.common.client.fallback.UserServiceFallback;
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