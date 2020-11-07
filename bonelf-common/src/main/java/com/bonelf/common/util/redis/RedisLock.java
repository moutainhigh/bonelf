package com.bonelf.common.util.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Redis锁
 * https://www.jianshu.com/p/9b5bc060d01b
 * https://blog.csdn.net/XianMuDuJiHen/article/details/94866251
 **/
@Component
@Slf4j
public class RedisLock {

	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * 加锁
	 * @param key key
	 * @param value 当前时间+超时时间
	 * @return
	 */
	public boolean lock(String key, String value) {
		Boolean isSet = redisTemplate.opsForValue().setIfAbsent(key, value);
		if (isSet != null && isSet) {
			return true;
		}
		// 防止死锁
		// currentValue=A 两个线程的value都是B 其中一个线程拿到锁
		String currentValue = redisTemplate.opsForValue().get(key);
		//如果锁过期
		if (!StringUtils.isEmpty(currentValue)
				&& Long.parseLong(currentValue) < System.currentTimeMillis()) {
			/* 这段代码保证只有一个线程拿到锁 */
			// 获取上一个锁的时间
			String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
			return !StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue);
		}

		return false;
	}

	public boolean tryLock(String key, String value, long time) {
		Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value);
		if (result != null && result) {
			//成功则设置ttl
			redisTemplate.expire(key, time, TimeUnit.SECONDS);
			return true;
		} else {
			//失败则检查一下锁有没有设置ttl，没有则设置上
			Long milliSec = redisTemplate.getExpire(key, TimeUnit.SECONDS);
			if (milliSec != null && milliSec <= 0) {
				redisTemplate.expire(key, time, TimeUnit.MILLISECONDS);
			}
			return false;
		}
	}

	public Long getExpiredTime(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	public void unlock(String key, String value) {
		try {
			String currentValue = redisTemplate.opsForValue().get(key);
			if (!StringUtils.isEmpty(currentValue)) {
				redisTemplate.opsForValue().getOperations().delete(key);
			}
		} catch (Exception e) {
			log.error("【redis分布式锁】 解锁异常");
		}

	}

}
