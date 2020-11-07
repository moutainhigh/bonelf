package com.bonelf.gateway.core.constant;

/**
 * <p>
 * redis缓存常量
 * </p>
 * @author bonelf
 * @since 2020/10/11 17:27
 */
public interface CacheConstant {
	/*===========================系统===========================*/
	/**
	 * redis 用户当前生效的Token + token（用户使用的Token）  HASH
	 */
	String API_USER_TOKEN_PREFIX = BonlfConstant.PROJECT_NAME + ":api:userToken:%s";
	/**
	 * 同上
	 */
	String SYS_USER_TOKEN_PREFIX = BonlfConstant.PROJECT_NAME + ":sys:userToken:%s";

}
