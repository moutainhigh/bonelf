package com.bonelf.common.service.impl;

import com.bonelf.common.client.SupportFeignClient;
import com.bonelf.common.constant.CacheConstant;
import com.bonelf.common.domain.Result;
import com.bonelf.common.service.DbDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * 数据库自动工具
 * </p>
 * @author bonelf
 * @since 2020/10/14 13:13
 */
@CacheConfig(cacheNames = CacheConstant.CACHE_NAME_7_DAY)
@Service
public class DbDictServiceImpl implements DbDictService {
	/**
	 *  使用restTemplate对system模块发起请求 获取dict数据
	 */
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private SupportFeignClient supportFeignClient;

	/**
	 * 实现缓存
	 * @param code
	 * @param value
	 * @return
	 */
	@Cacheable(value = CacheConstant.DB_DICT, condition = "!'-'.equals(#result)")
	public String queryDictTextByKey(String code, String value) {
		Result<String> result = supportFeignClient.queryDictTextByKey(code, value);
		return result != null && result.getSuccess() ? result.getResult() : "-";
	}

	public String queryDictTextByKeyNoCache(String code, String value) {
		Result<String> result = supportFeignClient.queryDictTextByKey(code, value);
		return result != null && result.getSuccess() ? result.getResult() : "-";
	}
}
