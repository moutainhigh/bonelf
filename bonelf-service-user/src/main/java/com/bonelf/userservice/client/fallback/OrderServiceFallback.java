package com.bonelf.userservice.client.fallback;

import com.bonelf.common.domain.Result;
import com.bonelf.userservice.client.OrderFeignClient;
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
    public Result<?> getProductOrderById(String orderId) {
        return Result.error("feign fallback");
    }
}
