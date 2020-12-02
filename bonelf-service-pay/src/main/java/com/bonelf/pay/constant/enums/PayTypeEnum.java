/*
 * Copyright (c) 2020. Bonelf.
 */

package com.bonelf.pay.constant.enums;

import com.bonelf.cicada.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayTypeEnum implements CodeEnum {
	/**
	 *
	 */
	ALI(0, "支付表"),

	WECHAT(1, "微信"),

	MINI(2, "小程序"),
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
