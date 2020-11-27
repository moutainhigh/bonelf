/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.common.core.serializer;

import com.bonelf.cicada.util.CipherCryptUtil;
import com.bonelf.common.constant.AuthConstant;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.CommonBizExceptionEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * 加密传输加密注解
 * 加解密JS: common->classpath：static/js/doCrypted.js
 * @author bonelf
 * @date 2020-11-27 09:12:00
 */
@Slf4j
public class CipherEncryptSerializer extends JsonSerializer<Object> {
	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		String valueStr = value.toString();
		String fieldName = gen.getOutputContext().getCurrentName();
		try {
			//反射获取字段类型 如果转了下划线这里要转回驼峰取field
			Field field = gen.getCurrentValue().getClass().getDeclaredField(fieldName);
			String str;
			try {
				str = CipherCryptUtil.encrypt(valueStr, AuthConstant.FRONTEND_PASSWORD_CRYPTO, AuthConstant.FRONTEND_SALT_CRYPTO);
			} catch (Exception e) {
				log.error("加密失败", e);
				throw new BonelfException(CommonBizExceptionEnum.CRYPT_ERROR);
			}
			if (field.getType() == String.class) {
				gen.writeString(str);
			} else if (field.getType() == Long.class ||
					field.getType() == Long.TYPE ||
					field.getType() == BigDecimal.class ||
					field.getType() == Double.class ||
					field.getType() == Double.TYPE ||
					field.getType() == Integer.class ||
					field.getType() == Integer.TYPE) {
				gen.writeNumber(str);
			} else {
				gen.writeObject(value);
				//throw new IllegalArgumentException("CipherDecrypt can't be replaced on not number or string value");
			}
			return;
		} catch (NoSuchFieldException e) {
			log.warn("NoSuchFieldException", e);
		}
		gen.writeString(valueStr);
	}
}
