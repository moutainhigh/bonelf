package com.bonelf.testservice.client;

import com.bonelf.common.cloud.constant.ServiceNameConstant;
import com.bonelf.common.cloud.feign.FeignConfig;
import com.bonelf.common.domain.Result;
import com.bonelf.testservice.client.factory.OrderFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 * 订单远程服务调用
 * </p>
 * @author bonelf
 * @since 2020/10/5 21:12
 */
//@FeignClient(ServiceNameConstant.ORDER_SERVICE)
@FeignClient(contextId = "orderFeignClient",
		value = ServiceNameConstant.ORDER_SERVICE,
		configuration = FeignConfig.class,
		fallbackFactory = OrderFeignFallbackFactory.class)
public interface OrderFeignClient {
	//定义要调用的方法的路径
	@GetMapping("/bonelf/v1/productOrder/{orderId}")
	Result<?> getProductOrderById(@PathVariable("orderId") String orderId);
}

