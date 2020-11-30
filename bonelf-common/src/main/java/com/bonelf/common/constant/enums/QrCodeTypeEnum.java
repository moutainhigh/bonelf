package com.bonelf.common.constant.enums;

import com.bonelf.cicada.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 二维码业务
 */
@Getter
@AllArgsConstructor
public enum QrCodeTypeEnum implements CodeEnum {
	/**
	 *
	 */
	EXAMPLE("example", "例子"),

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