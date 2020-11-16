package com.bonelf.common.core.shiro.constant;

/**
 * <p>
 * 认证异常信息，报错重新登录
 * </p>
 * @author bonelf
 * @since 2020/10/12 14:53
 */
public interface AuthExceptionMsgConstant {
	/**
	 * shiro 不存在的realm
	 */
	String NO_REALM_TYPE_FIT = "非法用户";

	String NOL_LOGIN = "游客无法访问";

	/**
	 * 需要重新登录
	 */
	String INVALID_TOKEN = "非法的用户凭据";
	/**
	 * 用户找不到
	 */
	String INVALID_USER = "非法用户";

	/**
	 * 需要重新登录
	 */
	String LOGIN_EXPIRE = "登录凭据过期，请重新登录";

	/**
	 * 用户失效，跳去登录页面，再登录接口再报一次账号已被锁定但是其他状态码的错
	 */
	String FROZEN = "账号已被锁定，请联系管理员!";

	/**
	 * 需要重新登录
	 */
	String EMPTY_TOKEN = "无认证信息";

	/**
	 * 需要重新登录
	 */
	String LOGIN_INSTEAD = "用户在其他设备登录，请重新登录";
}
