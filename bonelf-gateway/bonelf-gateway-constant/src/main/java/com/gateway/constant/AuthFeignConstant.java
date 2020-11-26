/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gateway.constant;

/**
 * <p>
 * 服务间调用、拦截鉴权名称
 * </p>
 * @author bonelf
 * @since 2020/10/5 23:40
 */
public interface AuthFeignConstant {
	/**
	 * 网关、Feign调用请求头标识
	 */
	String AUTH_HEADER = "Authorization-Flag";
	/**
	 * feign请求特别标识内容
	 */
	String FEIGN_REQ_FLAG_PREFIX = "feign";
}
