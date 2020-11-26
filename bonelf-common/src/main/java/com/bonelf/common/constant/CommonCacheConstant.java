package com.bonelf.common.constant;

/**
 * <p>
 * redis缓存常量
 * 注意：服务间不通用
 * </p>
 * @author bonelf
 * @since 2020/10/11 17:27
 */
public interface CommonCacheConstant {
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
	String NO_REPEAT_SUBMIT = BonelfConstant.PROJECT_NAME + ":noRepeatSubmit:%s:%s";
	/**
	 * redis 用户当前生效的Token + token（用户使用的Token）  HASH
	 * 新的OAuth2已不适用公共redis KEY存储
	 */
	@Deprecated
	String API_USER_TOKEN_PREFIX = BonelfConstant.PROJECT_NAME + ":api:userToken:%s";
	/**
	 * 同上
	 */
	@Deprecated
	String SYS_USER_TOKEN_PREFIX = BonelfConstant.PROJECT_NAME + ":sys:userToken:%s";

	/**
	 * socket redis发布订阅 使用户ChannelEnum中的频道
	 * 推荐使用mq，因为使用redis做发布订阅意味着所有的服务的缓存必须是一个库
	 */
	@Deprecated
	String WEB_SOCKET_CHANNEL = BonelfConstant.PROJECT_NAME + ":websocket:channel:%s";

	/**
	 * 字典缓存
	 */
	String DB_DICT = BonelfConstant.PROJECT_NAME + ":dbDict";

	/*===========================common===========================*/
	/**
	 * wxma redis key prefix
	 */
	String WX_MA_KEY_PREFIX = BonelfConstant.PROJECT_NAME + ":";
}
