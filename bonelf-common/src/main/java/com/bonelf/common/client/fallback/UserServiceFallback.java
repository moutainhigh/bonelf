package com.bonelf.common.client.fallback;


import com.bonelf.common.client.UserFeignClient;
import com.bonelf.common.domain.Result;
import com.bonelf.common.domain.vo.ApiUser;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * feign请求失败返回
 * </p>
 * @author bonelf
 * @since 2020/10/6 0:21
 */
@Slf4j
public class UserServiceFallback implements UserFeignClient {

	@Setter
	private Throwable cause;

	@Override
	public Result<ApiUser> getApiUserById(Long userId) {
		log.error("Feign请求失败 userId{}", userId);
		return Result.error("Feign请求失败");
	}

	@Override
	public Result<Map<String, Set<String>>> getApiUserRolesAndPermission(Long userId) {
        log.error("Feign请求失败 userId{}", userId);
		return Result.error("Feign请求失败");
	}
}
