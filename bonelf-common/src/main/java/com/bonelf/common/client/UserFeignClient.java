package com.bonelf.common.client;

import com.bonelf.common.client.factory.UserFeignFallbackFactory;
import com.bonelf.common.cloud.constant.ServiceNameConstant;
import com.bonelf.common.cloud.feign.FeignConfig;
import com.bonelf.common.domain.Result;
import com.bonelf.common.domain.vo.ApiUser;
import com.bonelf.common.domain.vo.SysUser;
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
@FeignClient(contextId = "userFeignClient", value = ServiceNameConstant.USER_SERVICE,
		configuration = FeignConfig.class, fallbackFactory = UserFeignFallbackFactory.class)
public interface UserFeignClient {
	@Deprecated
	@GetMapping("/user/v1/getUser")
	Result<ApiUser> getApiUserById(@RequestParam Long userId);

	// FIXME: 2020/10/12 实现
	@Deprecated
	default Result<SysUser> getSysUserById(long userId) {
		return Result.ok(new SysUser());
	}

	@GetMapping("/user/v1/getPermission")
	@Deprecated
	Result<Map<String, Set<String>>> getApiUserRolesAndPermission(Long userId);

	// FIXME: 2020/10/12 实现
	@Deprecated
	default Result<Map<String, Set<String>>> getSysUserRolesAndPermission(Long userId) {
		return Result.ok(new HashMap<String, Set<String>>(2) {{
			put("role", Collections.emptySet());
			put("permissions", Collections.emptySet());
		}});
	}
}

