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
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 加密传输解密注解
 * 加解密JS: classpath：static/js/doCrypted.js
 * @author bonelf
 * @date 2020-11-27 09:12:00
 */
@Slf4j
public class CipherDecryptDeserializer extends JsonDeserializer<Object> {

	@Override
	public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String value = StringDeserializer.instance.deserialize(p, ctxt);
		String fieldName = p.getCurrentName();
		try {
			//反射获取字段类型 如果转了下划线这里要转回驼峰取field
			Field field = p.getCurrentValue().getClass().getDeclaredField(fieldName);
			if (field.getType() != String.class) {
				throw new IllegalArgumentException("CipherCrypt can't be replaced on not string value");
			}
			//CipherCrypt cipherCrypt = field.getAnnotation(CipherCrypt.class);
			String str;
			try {
				str = CipherCryptUtil.decrypt(value, AuthConstant.FRONTEND_PASSWORD_CRYPTO, AuthConstant.FRONTEND_SALT_CRYPTO);
			} catch (Exception e) {
				log.error("解密失败", e);
				throw new BonelfException(CommonBizExceptionEnum.DECRYPT_ERROR);
			}
			return DeserializerHelper.getByFieldType(field.getType(), str, "cipherDecrypt");
			//return str;
		} catch (NoSuchFieldException e) {
			log.warn("NoSuchFieldException", e);
		}
		return value;
	}
}
