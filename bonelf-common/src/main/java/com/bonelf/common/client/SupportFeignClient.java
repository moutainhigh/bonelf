package com.bonelf.common.client;

import com.bonelf.common.client.factory.SupportFeignFallbackFactory;
import com.bonelf.common.cloud.constant.ServiceNameConstant;
import com.bonelf.common.cloud.feign.FeignConfig;
import com.bonelf.common.core.websocket.SocketRespMessage;
import com.bonelf.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 服务提供 服务 feign
 */
@FeignClient(contextId = "supportFeignClient", value = ServiceNameConstant.SUPPORT_SERVICE,
		configuration = FeignConfig.class, fallbackFactory = SupportFeignFallbackFactory.class)
public interface SupportFeignClient {

	@PostMapping("/bonelf/v1/websocket/sendMessage")
	Result<String> sendMessage(@RequestBody SocketRespMessage message);

	@GetMapping("/bonelf/v1/sys/dbdict/getByCode")
	Result<String> queryDictTextByKey(@RequestParam String code, @RequestParam String value);
}
