package com.bonelf.auth.exception;

import com.bonelf.common.core.exception.AbstractBaseExceptionEnum;
import lombok.Getter;

@Getter
public enum AuthErrorType implements AbstractBaseExceptionEnum {

	INVALID_REQUEST(40001, "无效请求"),
	INVALID_CLIENT(40002, "无效client_id"),
	INVALID_GRANT(40003, "无效授权"),
	INVALID_SCOPE(40004, "无效scope"),
	INVALID_TOKEN(40005, "无效token"),
	INSUFFICIENT_SCOPE(40010, "授权不足"),
	REDIRECT_URI_MISMATCH(40020, "redirect url不匹配"),
	ACCESS_DENIED(40030, "拒绝访问"),
	METHOD_NOT_ALLOWED(40040, "不支持该方法"),
	SERVER_ERROR(40050, "权限服务错误"),
	UNAUTHORIZED_CLIENT(40060, "未授权客户端"),
	UNAUTHORIZED(40061, "未授权"),
	UNSUPPORTED_RESPONSE_TYPE(40070, " 支持的响应类型"),
	UNSUPPORTED_GRANT_TYPE(40071, "不支持的授权类型");

	/**
	 * 错误类型码
	 */
	private Integer status;
	/**
	 * 错误类型描述信息
	 */
	private String message;

	AuthErrorType(Integer status, String msg) {
		this.status = status;
		this.message = msg;
	}

}
