package com.bonelf.common.core.exception;

/**
 * <p>
 * 服务异常枚举接口
 * </p>
 * @author bonelf
 * @since 2020/10/11 17:35
 */
public interface AbstractBaseExceptionEnum {
	String getStatus();

	String getMessage();
}
