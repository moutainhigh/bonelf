package com.bonelf.cicada.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ByteUtil {
	/**
	 * 将byte数组转化为Object对象
	 * @return
	 */
	public static <T> T toObject(byte[] bytes) {
		T object = null;
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			// 从objectInputStream流中读取一个对象
			object = (T)objectInputStream.readObject();
			byteArrayInputStream.close();
			objectInputStream.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;// 返回对象
	}
}
