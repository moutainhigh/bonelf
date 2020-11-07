package com.bonelf.common.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.BizExceptionEnum;

/**
 * <p>
 * socket工具类 仅用于本项目socket实现
 * </p>
 * @author bonelf
 * @since 2020/10/20 23:23
 */
public class SocketUtil {

	/**
	 * socket对象
	 * @param data 对象
	 * @param clazz 转化类
	 * @param <T> 类型
	 * @return 信息
	 */
	public static <T> T parseSocketData(JSONObject data, Class<T> clazz) {
		if (data == null) {
			throw new BonelfException(BizExceptionEnum.REQUEST_NULL);
		}
		T obj;
		try {
			obj = data.toJavaObject(clazz);
		} catch (JSONException e) {
			throw new BonelfException(BizExceptionEnum.JSON_SERIALIZE_EXCEPTION);
		}
		return obj;
	}
}
