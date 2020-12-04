package com.bonelf.userservice.core.exception;

import com.bonelf.common.core.exception.AbstractBaseExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 用户服务异常信息定义
 * </p>
 * @author bonelf
 * @since 2020/10/12 10:48
 */
@Getter
@AllArgsConstructor
public enum UserExceptionEnum implements AbstractBaseExceptionEnum {
	FREEZE_USER("A0202", "账号已被锁定"),
	ALREADY_REGISTER("A0001", "此账号已注册");
	/**
	 * 服务状态码 版本号+模块号+序号 类似10101
	 */
	private String status;
	/**
	 * 异常信息
	 */
	private String message;
}
