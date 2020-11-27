/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.common.core.serializer;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class RestObjectMapper extends ObjectMapper {
	public RestObjectMapper() {
		//pretty format
		//this.writerWithDefaultPrettyPrinter();
		this.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN));
		//序列化两边接受方字段不足 不报UnrecognizedPropertyException，只解析对应的
		this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		//驼峰转下划线
		//this.setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE);
		// 字段和值都加引号
		//this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		this.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
		this.findAndRegisterModules();
		/*
		 * 序列换成json时,将所有的long变成string
		 * 因为js中得数字类型不能包含所有的java long值
		 */
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		//simpleModule.addSerializer(Double.TYPE, ToStringSerializer.instance);
		//simpleModule.addSerializer(Double.class, ToStringSerializer.instance);
		simpleModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
		//旧：DateTimeFormatter.ISO_DATE_TIME
		simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
		//旧：DateTimeFormatter.ISO_DATE
		//simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
		//旧：DateTimeFormatter.ISO_TIME
		simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
		//空转null
		JsonDeserializer<String> serializer = new StdDeserializer<String>(String.class) {
			@Override
			public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				String value = StringDeserializer.instance.deserialize(p, ctxt);
				if (value == null || "".equals(value.trim()) || "null".equals(value) || "undefined".equals(value)) {
					return null;
				}
				return value;
			}
		};
		simpleModule.addDeserializer(String.class, serializer);
		this.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		this.registerModule(simpleModule);
	}

	private static class NullValueSerializer extends JsonSerializer<Object> {
		@Override
		public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			String fieldName = gen.getOutputContext().getCurrentName();
			try {
				//反射获取字段类型 如果转了下划线这里要转回驼峰取field
				Field field = gen.getCurrentValue().getClass().getDeclaredField(fieldName);
				if (Objects.equals(field.getType(), String.class)) {
					//字符串null返回空字符串
					gen.writeString(StrUtil.EMPTY);
					return;
				} else if (Objects.equals(field.getType(), Collection.class) || field.getType().isArray()) {
					//List字段如果为null,输出为[],而非null
					gen.writeStartArray();
					gen.writeEndArray();
					return;
				} else if (Objects.equals(field.getType(), Map.class)) {
					//map型空值返回{}
					gen.writeStartObject();
					gen.writeEndObject();
					return;
				} else if (Objects.equals(field.getType(), Boolean.class)) {
					//空布尔值返回false
					gen.writeBoolean(false);
					return;
				}
			} catch (NoSuchFieldException ignored) {
			}
			//默认返回""  (是否输出值为null的字段)
			gen.writeString(StrUtil.EMPTY);
		}
	}
}
