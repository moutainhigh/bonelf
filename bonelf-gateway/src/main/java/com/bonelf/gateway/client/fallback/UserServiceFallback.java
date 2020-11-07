package com.bonelf.gateway.client.fallback;


import com.bonelf.gateway.client.UserFeignClient;
import lombok.Setter;

/**
 * <p>
 * feign请求失败返回
 * </p>
 * @author bonelf
 * @since 2020/10/6 0:21
 */
public class UserServiceFallback implements UserFeignClient {

    @Setter
    private Throwable cause;
}
