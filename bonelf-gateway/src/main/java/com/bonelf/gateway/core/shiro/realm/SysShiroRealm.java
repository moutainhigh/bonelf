package com.bonelf.gateway.core.shiro.realm;

import com.bonelf.gateway.client.UserFeignClient;
import com.bonelf.gateway.domain.CommonUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 系统用户realm
 * </p>
 * @author bonelf
 * @since 2020/10/14 11:42
 */
@Slf4j
public class SysShiroRealm extends AbstractShiroRealm {
	@Autowired
	@Lazy
	protected UserFeignClient userFeignClient;

	@Override
	protected Map<String, Set<String>> getUserRolesAndPermission(Long userId) {
		return userFeignClient.getSysUserRolesAndPermission(userId).getResult();
	}

	@Override
	protected CommonUser getCommonUser(Long userId) {
		return userFeignClient.getSysUserById(userId).getResult();
	}
}
