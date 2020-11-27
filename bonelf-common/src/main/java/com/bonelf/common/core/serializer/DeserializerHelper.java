/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.common.core.serializer;

import java.lang.reflect.Field;
import java.math.BigDecimal;

class DeserializerHelper {
	/**
	 * 根据类型返回参数
	 * @param field
	 * @param str
	 * @param name
	 * @return
	 */
	static Object getByFieldType(Field field, String str, String name) {
		if (field.getType() == String.class) {
			return str;
		} else if (field.getType() == Long.class ||
				field.getType() == Long.TYPE) {
			return Long.parseLong(str);
		} else if (
				field.getType() == Double.class ||
						field.getType() == Double.TYPE) {
			return Double.parseDouble(str);
		} else if (
				field.getType() == Integer.class ||
						field.getType() == Integer.TYPE) {
			return Integer.parseInt(str);
		} else if (
				field.getType() == BigDecimal.class) {
			return new BigDecimal(str);
		} else {
			throw new IllegalArgumentException(name + " can't be replaced on not number or string value");
		}
	}
}
