/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.common.core.serializer;

import com.bonelf.cicada.enums.CodeEnum;
import com.bonelf.cicada.enums.EnumFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 加密传输解密注解
 * 加解密JS: classpath：static/js/doCrypted.js
 * @author bonelf
 * @date 2020-11-27 09:12:00
 */
@Slf4j
public class CodeEnumDeserializer extends JsonDeserializer<CodeEnum> {
	@Override
	public CodeEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String value = StringDeserializer.instance.deserialize(p, ctxt);
		try {
			return EnumFactory.getByCode(value, (Class<CodeEnum>)p.getCurrentValue().getClass());
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("enum define error, only support enum which extends CodeEnum. ");
		}
	}
}
