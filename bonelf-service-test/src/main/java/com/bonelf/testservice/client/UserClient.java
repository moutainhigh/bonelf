package com.bonelf.testservice.client;

import com.bonelf.common.cloud.constant.ServiceNameConstant;
import com.bonelf.common.cloud.feign.FeignConfig;
import com.bonelf.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * <p>
 * 签权服务
 * </p>
 * @author bonelf
 * @since 2020/11/17 15:37
 */
@FeignClient(contextId = "userClient", value = ServiceNameConstant.USER_SERVICE, configuration = FeignConfig.class, fallback = UserClientFallback.class)
public interface UserClient {

    /**
     * 查询用户
     * @param uniqueId
     * @return
     */
    @GetMapping(value = "/bonelf/user/v1/getUser")
    Result<Map<String, Object>> getUserByUniqueId(@RequestParam("uniqueId") String uniqueId);
}
