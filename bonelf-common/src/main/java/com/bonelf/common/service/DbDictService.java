package com.bonelf.common.service;

/**
 * <p>
 * 数据库自动工具
 * </p>
 * @author bonelf
 * @since 2020/10/14 13:13
 */
public interface DbDictService {

	/**
	 * 实现缓存
	 * @param code
	 * @param value
	 * @return
	 */
	String queryDictTextByKey(String code, String value);

	String queryDictTextByKeyNoCache(String code, String value);
}
