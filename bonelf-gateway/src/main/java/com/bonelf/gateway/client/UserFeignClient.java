package com.bonelf.gateway.client;

import com.bonelf.gateway.client.factory.UserFeignFallbackFactory;
import com.bonelf.gateway.domain.GateWayResult;
import com.bonelf.gateway.domain.vo.ApiUser;
import com.bonelf.gateway.domain.vo.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户认证远程服务调用
 * </p>
 * @author bonelf
 * @since 2020/10/5 21:12
 */
@FeignClient(contextId = "userFeignClient", value = "user-service", fallbackFactory = UserFeignFallbackFactory.class)
public interface UserFeignClient {
	@GetMapping("/v1/user/getUser")
	GateWayResult<ApiUser> getApiUserById(@RequestParam Long userId);

	// FIXME: 2020/10/12 实现
	default GateWayResult<SysUser> getSysUserById(long userId) {
		return GateWayResult.ok(new SysUser());
	}

	@GetMapping("/v1/user/getPermission")
	GateWayResult<Map<String, Set<String>>> getApiUserRolesAndPermission(Long userId);

	// FIXME: 2020/10/12 实现
	default GateWayResult<Map<String, Set<String>>> getSysUserRolesAndPermission(Long userId) {
		return GateWayResult.ok(new HashMap<String, Set<String>>(2) {{
			put("role", Collections.emptySet());
			put("permissions", Collections.emptySet());
		}});
	}
}

