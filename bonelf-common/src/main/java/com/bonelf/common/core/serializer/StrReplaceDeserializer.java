/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.common.core.serializer;

import com.bonelf.common.core.serializer.annotation.StrReplace;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 字符串序列化替换注解
 * @author bonelf
 * @date 2020-11-27 09:12:00
 */
@Slf4j
public class StrReplaceDeserializer extends JsonDeserializer<Object> {

	@Override
	public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String value = StringDeserializer.instance.deserialize(p, ctxt);
		String fieldName = p.getCurrentName();
		try {
			//反射获取字段类型 如果转了下划线这里要转回驼峰取field
			Field field = p.getCurrentValue().getClass().getDeclaredField(fieldName);
			if (field.isAnnotationPresent(StrReplace.class)) {
				StrReplace strReplace = field.getAnnotation(StrReplace.class);
				String str = value.replaceAll(strReplace.from(), strReplace.to());
				return DeserializerHelper.getByFieldType(field, str, strReplace.getClass().getSimpleName());
				//return str;
			}
		} catch (NoSuchFieldException e) {
			log.warn("NoSuchFieldException", e);
		}
		return value;
	}
}
