package com.bonelf.common.cloud.constant;

/**
 * <p>
 * 服务间调用、拦截鉴权名称
 * </p>
 * @author bonelf
 * @since 2020/10/5 23:40
 */
public interface AuthFeignConstant {
	/**
	 * 网关调用请求头标识
	 */
	String AUTH_HEADER = "Authorization-UserName";
}
