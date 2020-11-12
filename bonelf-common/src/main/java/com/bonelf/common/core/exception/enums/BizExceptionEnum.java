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
	REQUEST_INVALIDATE_EMPTY(400, "%s"),
	PAGE_NOT_FOUND(400, "分页参数错误"),
	PAGE_ERROR(400, "分页参数错误"),
	PAGE_LIMIT_ERROR(400, "页数超出限制"),
	/*===========================Shiro：401 见GlobalExceptionHandler AuthenticationException===========================*/
	SHIRO_AUTH_EXCEPTION(401, "%s"),
	NOT_LOGIN(401, "请先登录"),
	EMPTY_TOKEN(401, "访问非法"),
	LOGIN_EXPIRED(401, "登录过期，请重新登录"),
	INVALID_TOKEN(401, "非法的用户凭据"),
	LOGIN_INSTEAD(401, "用户在其他设备登录，请重新登录"),
	/*===========================数据库访问信息===========================*/
	DB_RESOURCE_NULL(404, "%s不存在"),
	/*===========================服务器异常信息===========================*/
	NO_REPEAT_SUBMIT(402, "技能冷却中，请等待%s秒"),
	SERVER_ERROR(500, "服务器异常:%s"),
	THIRD_FAIL(500, "三方调用失败:%s"),
	DECRYPT_ERROR(500, "解密失败");
	/**
	 * 服务状态码 版本号+模块号+序号 类似10101
	 */
	private Integer status;
	/**
	 * 异常信息
	 */
	private String message;
}
