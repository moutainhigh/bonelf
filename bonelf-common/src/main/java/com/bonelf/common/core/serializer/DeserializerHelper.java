/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.common.core.serializer;

import java.math.BigDecimal;

class DeserializerHelper {
	/**
	 * 根据类型返回参数
	 * @param clazz
	 * @param str
	 * @param name
	 * @return
	 */
	static Object getByFieldType(Class<?> clazz, String str, String name) {
		if (clazz == String.class) {
			return str;
		} else if (clazz == Long.class ||
				clazz == Long.TYPE) {
			return Long.parseLong(str);
		} else if (
				clazz == Double.class ||
						clazz == Double.TYPE) {
			return Double.parseDouble(str);
		} else if (
				clazz == Integer.class ||
						clazz == Integer.TYPE) {
			return Integer.parseInt(str);
		} else if (clazz == BigDecimal.class) {
			return new BigDecimal(str);
		} else {
			throw new IllegalArgumentException(name + " can't be replaced on not number or string value");
		}
	}
}
