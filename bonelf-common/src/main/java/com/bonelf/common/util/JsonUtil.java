package com.bonelf.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * <p>
 * Jackson转换方法
 * </p>
 * @author Chenyuan
 * @since 2020/11/3 23:11
 */
@Slf4j
public class JsonUtil {
	private static ObjectMapper objectMapper = SpringContextUtils.getBean(ObjectMapper.class);

	public static <T> String objToJson(T obj) {
		if (obj == null) {
			return null;
		}

		try {
			return obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			log.warn("obj To json is error", e);
			return null;
		}
	}

	/**
	 * 返回格式化好的json串
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public static <T> String objToJsonPretty(T obj) {
		if (obj == null) {
			return null;
		}

		try {
			return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			log.warn("obj To json pretty is error", e);
			return null;
		}
	}

	public static <T> T json2Object(String json, Class<T> clazz) {
		if (StringUtils.isEmpty(json) || clazz == null) {
			return null;
		}

		try {
			return clazz.equals(String.class) ? (T)json : objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			log.warn("json To obj is error", e);
			return null;
		}
	}

	/**
	 * 通过   TypeReference    处理List<User>这类多泛型问题
	 * @param json
	 * @param typeReference
	 * @param <T>
	 * @return
	 */
	public static <T> T json2Object(String json, TypeReference typeReference) {
		if (StringUtils.isEmpty(json) || typeReference == null) {
			return null;
		}

		try {
			return (T)(typeReference.getType().equals(String.class) ? json : objectMapper.readValue(json, typeReference));
		} catch (Exception e) {
			log.warn("json To obj is error", e);
			return null;
		}
	}

	/**
	 * 通过jackson 的javatype 来处理多泛型的转换
	 * @param json
	 * @param collectionClazz
	 * @param elements
	 * @param <T>
	 * @return
	 */
	public static <T> T json2Object(String json, Class<?> collectionClazz, Class<?>... elements) {
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClazz, elements);

		try {
			return objectMapper.readValue(json, javaType);
		} catch (Exception e) {
			log.warn("json To obj is error", e);
			return null;
		}
	}
}
