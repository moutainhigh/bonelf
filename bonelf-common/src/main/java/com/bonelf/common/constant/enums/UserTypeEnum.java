package com.bonelf.common.constant.enums;

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
public enum UserTypeEnum {
	/**
	 * api
	 */
	API_SHIRO_REALM("app"),
	/**
	 * sys
	 */
	SYS_SHIRO_REALM("web"),
	;
	/**
	 * realmName Bean 名称
	 */
	private String realmName;
}
