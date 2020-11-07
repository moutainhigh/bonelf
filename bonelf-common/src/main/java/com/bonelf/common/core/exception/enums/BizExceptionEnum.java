package com.bonelf.common.core.exception.enums;

import com.bonelf.common.core.exception.AbstractBaseExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 公用异常信息
 * 401 认证异常 400 接口传参异常
 * </p>
 * @author bonelf
 * @since 2020/10/12 10:48
 */
@Getter
@AllArgsConstructor
public enum BizExceptionEnum implements AbstractBaseExceptionEnum {
	/*===========================接口传参异常===========================*/
	REQUEST_NULL(400, "未获取到传参"),
	JSON_SERIALIZE_EXCEPTION(400, "传参类型错误，请检查"),
	REQUEST_INVALIDATE(400, "参数不符合要求:[%s]"),
	/*===========================Shiro：401 见GlobalExceptionHandler AuthenticationException===========================*/
	//这个在advice里捕获不了的问题？
	SHIRO_AUTH_EXCEPTION(401, "%s"),
	NOT_LOGIN(401, "请先登录"),
	EMPTY_TOKEN(401, "访问非法"),
	LOGIN_EXPIRED(401, "登录过期，请重新登录"),
	INVALID_TOKEN(401, "非法的用户凭据"),
	LOGIN_INSTEAD(401, "用户在其他设备登录，请重新登录"),
	/*===========================aop异常信息===========================*/
	NO_REPEAT_SUBMIT(402, "技能冷却中，请等待%s秒"),

	SERVER_ERROR(500, "服务器异常:%s");
	/**
	 * 服务状态码 版本号+模块号+序号 类似10101
	 */
	private Integer status;
	/**
	 * 异常信息
	 */
	private String message;
}
