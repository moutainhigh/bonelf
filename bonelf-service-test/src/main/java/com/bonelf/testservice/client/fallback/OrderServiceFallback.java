package com.bonelf.testservice.client.fallback;

import com.bonelf.testservice.client.OrderFeignClient;
import lombok.Setter;

/**
 * <p>
 * feign请求失败返回
 * </p>
 * @author bonelf
 * @since 2020/10/6 0:21
 */
public class OrderServiceFallback implements OrderFeignClient {

    @Setter
    private Throwable cause;

    @Override
    public String getProductOrderById(String orderId) {
        return "feign fallback";
    }
}
