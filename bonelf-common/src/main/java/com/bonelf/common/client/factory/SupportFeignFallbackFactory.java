package com.bonelf.common.client.factory;

import com.bonelf.common.client.SupportFeignClient;
import com.bonelf.common.client.fallback.SupportFeignFallback;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class SupportFeignFallbackFactory implements FallbackFactory<SupportFeignClient> {

    @Override
    public SupportFeignClient create(Throwable throwable) {
        SupportFeignFallback fallback = new SupportFeignFallback();
        fallback.setCause(throwable);
        return fallback;
    }
}