/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.support.constant;

import com.bonelf.common.constant.BonelfConstant;

public interface CacheConstant {
	/**
	 * 验证码过期时间
	 */
	long VERIFY_CODE_EXPIRED_SECOND = 5 * 60L;

	/**
	 * 验证码 businessType phone
	 */
	String LOGIN_VERIFY_CODE = BonelfConstant.PROJECT_NAME + ":%s:%s";

	/*
	 * session 有效时间 如果不使用hash表存储session使用
	 */
	//long SESSION_TIME = -1;

	/**
	 * socket session hash 存储在线状态
	 */
	String WEB_SOCKET_SESSION_HASH = BonelfConstant.PROJECT_NAME + ":websocket:session";
}
