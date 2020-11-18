package com.bonelf.common.constant;

/**
 * <p>
 * Shiro realm bean 名称
 * (see gateway ShiroRealmEnum)
 * 生成token要携带参数，只好提到公共模块。
 * gateway使用枚举
 * 这么处理了
 * </p>
 * @author bonelf
 * @since 2020/10/12 10:29
 */
public interface ShiroRealmName {
	/**
	 * API
	 */
	String API_SHIRO_REALM = "apiShiroRealm";
	/**
	 * SYS
	 */
	String SYS_SHIRO_REALM = "sysShiroRealm";
}
