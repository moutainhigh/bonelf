package com.bonelf.common.constant;

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
	 * cacheable缓存空间1
	 */
	String CACHE_NAME_5_MINUTES = "cache-name1";
	/**
	 * cacheable缓存空间2
	 */
	String CACHE_NAME_7_DAY = "cache-name2";

	/**
	 * 重复提交aop + sessionId + servletPath
	 */
	String NO_REPEAT_SUBMIT = BonlfConstant.PROJECT_NAME + ":noRepeatSubmit:%s:%s";
	/**
	 * redis 用户当前生效的Token + token（用户使用的Token）
	 * 使用gateway的token静态常量
	 */
	@Deprecated
	String API_USER_TOKEN_KEY = BonlfConstant.PROJECT_NAME + ":api:userToken:%s";
	/**
	 * 同上
	 * 使用gateway的token静态常量
	 */
	@Deprecated
	String SYS_USER_TOKEN_KEY = BonlfConstant.PROJECT_NAME + ":sys:userToken:%s";

	/**
	 * socket session hash 存储在线状态
	 */
	String WEB_SOCKET_SESSION_HASH = BonlfConstant.PROJECT_NAME + ":websocket:session";

	/*
	 * session 有效时间 如果不使用hash表存储session使用
	 */
	//long SESSION_TIME = -1;

	/**
	 * socket redis发布订阅 使用户ChannelEnum中的频道
	 */
	String WEB_SOCKET_CHANNEL = BonlfConstant.PROJECT_NAME + ":websocket:channel:%s";

	/**
	 * 字典缓存
	 */
	String DB_DICT = BonlfConstant.PROJECT_NAME + ":dbDict";

	/*===========================common===========================*/

	/*===========================测试===========================*/

	/*===========================订单===========================*/

}
