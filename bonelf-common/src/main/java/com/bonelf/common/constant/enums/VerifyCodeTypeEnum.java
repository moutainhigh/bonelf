package com.bonelf.common.constant.enums;

import com.bonelf.cicada.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 所有数据库字段枚举
 */
@Getter
@AllArgsConstructor
public enum VerifyCodeTypeEnum implements CodeEnum {
	/**
	 *
	 */
	LOGIN("login", "登录"),

	;
	/**
	 * code 唯一code
	 */
	private String code;
	/**
	 * value 值
	 */
	private String value;
}