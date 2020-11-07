package com.bonelf.gateway.core.constant.enums;

import com.bonelf.cicada.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FreezeEnum implements CodeEnum {
	/**
	 *
	 */
	FREEZE(1, "冻结"),

	ENABLE(0, "启用"),
	;
	/**
	 * code 唯一code
	 */
	private Integer code;
	/**
	 * value 值
	 */
	private String value;
}
