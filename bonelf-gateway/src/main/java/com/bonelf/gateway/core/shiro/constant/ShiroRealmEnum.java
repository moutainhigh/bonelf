package com.bonelf.gateway.core.shiro.constant;

import com.bonelf.gateway.core.constant.ShiroRealmName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 认证方法枚举
 * realmName可以同名 意味着两个都走
 * clazz
 * </p>
 * @author bonelf
 * @since 2020/10/12 11:31
 */
@Getter
@AllArgsConstructor
public enum ShiroRealmEnum {
	/**
	 * api
	 * {@link com.bonelf.gateway.core.shiro.realm.ApiShiroRealm}
	 */
	API_SHIRO_REALM(ShiroRealmName.API_SHIRO_REALM),
	/**
	 * sys
	 * {@link com.bonelf.gateway.core.shiro.realm.SysShiroRealm}
	 */
	SYS_SHIRO_REALM(ShiroRealmName.SYS_SHIRO_REALM),
	;
	/**
	 * realmName Bean 名称
	 */
	private String realmName;
}
