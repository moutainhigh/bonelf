/*
 * Copyright (c) 2020. Bonelf.
 */

package com.bonelf.common.util;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * <p>
 * TODO 账号管理
 * </p>
 * @author Chenyuan
 * @since 2020/12/31 16:32
 */
@Component
public class ContextUtil {

	/**
	 * TODO principal获取的是手机号，openId，邮箱，用户生成编号等能代表用户账号唯一键，需要根据这些获取用户编号
	 * 获得用户编号
	 * @return
	 */
	protected Long getUserId() {
		System.out.println("User:" + JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication()));
		return 1L;
	}

	/**
	 * 获得用户编号
	 * @return
	 */
	protected Long getUserIdCanNull() {
		System.out.println("User:" + JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication()));
		return 1L;
	}

	/**
	 * 获得用户手机号
	 * @return
	 */
	protected String getPhone() {
		System.out.println("User:" + JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication()));
		return (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
