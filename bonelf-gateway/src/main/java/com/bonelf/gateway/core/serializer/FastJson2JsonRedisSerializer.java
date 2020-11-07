package com.bonelf.gateway.core.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	private Class<T> clazz;

	public FastJson2JsonRedisSerializer(Class<T> clazz) {
		this.clazz = clazz;
	}

	public byte[] serialize(T t) throws SerializationException {
		return t == null ? new byte[0] : JSON.toJSONString(t, new SerializerFeature[]{SerializerFeature.WriteClassName}).getBytes(DEFAULT_CHARSET);
	}

	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes != null && bytes.length > 0) {
			String str = new String(bytes, DEFAULT_CHARSET);
			return JSON.parseObject(str, this.clazz);
		} else {
			return null;
		}
	}
}
