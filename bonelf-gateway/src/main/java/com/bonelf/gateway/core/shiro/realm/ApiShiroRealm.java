package com.bonelf.gateway.core.shiro.realm;

import com.bonelf.gateway.client.UserFeignClient;
import com.bonelf.gateway.domain.CommonUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户登录鉴权和获取用户授权
 * 校验用户、用户权限；token过期自动登录
 * api用户realm
 * </p>
 * @author bonelf
 * @since 2020/10/14 11:42
 */
public class ApiShiroRealm extends AbstractShiroRealm {
	@Autowired
	@Lazy
	protected UserFeignClient userFeignClient;

	@Override
	protected Map<String, Set<String>> getUserRolesAndPermission(Long userId) {
		return userFeignClient.getApiUserRolesAndPermission(userId);
	}

	@Override
	protected CommonUser getCommonUser(Long userId) {
		return userFeignClient.getApiUserById(userId);
	}
}
