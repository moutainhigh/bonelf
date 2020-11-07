package com.bonelf.gateway.client;

import com.bonelf.gateway.domain.vo.ApiUser;
import com.bonelf.gateway.domain.vo.SysUser;

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
//@FeignClient(contextId = "userFeignClient", value = ServiceNameConstant.USER_SERVICE, fallbackFactory = UserFeignFallbackFactory.class)
public interface UserFeignClient {
	// FIXME: 2020/10/12 实现
	default ApiUser getApiUserById(long userId) {
		return new ApiUser();
	}

	default SysUser getSysUserById(long userId) {
		return new SysUser();
	}

	default Map<String, Set<String>> getApiUserRolesAndPermission(Long userId) {
		return new HashMap<String, Set<String>>(2) {{
			put("role", Collections.emptySet());
			put("permissions", Collections.emptySet());
		}};
	}

	default Map<String, Set<String>> getSysUserRolesAndPermission(Long userId) {
		return new HashMap<String, Set<String>>(2) {{
			put("role", Collections.emptySet());
			put("permissions", Collections.emptySet());
		}};
	}
}

