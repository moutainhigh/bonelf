package com.bonelf.support.core.exception;

import com.bonelf.common.core.exception.AbstractBaseExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 助手服务异常信息定义
 * </p>
 * @author bonelf
 * @since 2020/10/12 10:48
 */
@Getter
@AllArgsConstructor
public enum SupportExceptionEnum implements AbstractBaseExceptionEnum {
	VERIFY_CODE_EXPIRE(90001, "验证码不存在或已过期");
	/**
	 * 服务状态码 版本号+模块号+序号 类似10101
	 */
	private Integer status;
	/**
	 * 异常信息
	 */
	private String message;
}
