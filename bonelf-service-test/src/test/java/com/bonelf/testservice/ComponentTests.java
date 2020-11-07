package com.bonelf.testservice;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.bonelf.common.util.JsonUtil;
import com.bonelf.common.util.redis.RedisUtil;
import com.bonelf.testservice.domain.vo.TestConverterVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 验证依赖注入的bean和配置是否正确
 * </p>
 * @author bonelf
 * @since 2020/10/11 16:09
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ComponentTests {

	@Autowired
	private RedisUtil redisUtil;

	@Test
	public void jsonUtilTest() {
		log.info(JsonUtil.objToJson(new TestConverterVO()));
	}

	@Test
	public void redisTest() {
		final String testKey = "test-redis";
		final long time = 3000L;
		redisUtil.set(testKey, testKey, time);
		log.debug("==Redis测试 Common===");
		log.debug("==getExpire(<3000):{}", redisUtil.getExpire(testKey));
		log.debug("==get(test-redis):{}", redisUtil.get(testKey));

		final String testIncrKey = "test-redis-incr";
		redisUtil.set(testIncrKey, 1, time);
		log.debug("==get(incr-origin:1):{}", redisUtil.get(testIncrKey));
		redisUtil.incr(testIncrKey, 3);
		log.debug("==get(incr:4):{}", redisUtil.get(testIncrKey));
		redisUtil.decr(testIncrKey, 1);
		log.debug("==get(decr:3):{}", redisUtil.get(testIncrKey));

		final String testHSet = "test-redis-hset";
		final String testHSet1 = "test-redis-hset-1";
		final String testHSet2 = "test-redis-hset-2";
		redisUtil.hset(testHSet, testHSet1, testHSet1);
		redisUtil.hset(testHSet, testHSet2, testHSet2);
		log.debug("==hget(test-redis-hset-1):{}", redisUtil.<String>hget(testHSet, testHSet1));
		log.debug("==hget(test-redis-hset-2):{}", redisUtil.<String>hget(testHSet, testHSet2));
		final String testHSet3 = "test-redis-hset-3";
		final String testHSet4 = "test-redis-hset-4";
		Map<String, Object> hmset = new HashMap<String, Object>(2){{
			put(testHSet3, 3);
			put(testHSet4, testHSet4);
		}};
		redisUtil.hmset(testHSet, hmset);
		redisUtil.hincr(testHSet, testHSet3, 2);
		log.debug("==hmget(above4):{}", JSON.toJSONString(redisUtil.<String, String>hmget(testHSet)));

		final String testSet = "test-redis-set";
		redisUtil.del(testSet);
		final String testSet1 = "test-redis-set-1";
		final String testSet2 = "test-redis-set-2";
		redisUtil.sSet(testSet, testSet1, testSet2, testSet1);
		log.debug("==sGet(2 Value):{}", JSON.toJSONString(redisUtil.sGet(testSet)));

		final String testList = "test-redis-list";
		redisUtil.del(testList);
		final String testList1 = "test-redis-list-1";
		final String testList2 = "test-redis-list-2";
		redisUtil.lSet(testList, CollectionUtil.newLinkedList(testList1, testSet2, testList2), time);
		log.debug("==lGet(3 Value):{}", JSON.toJSONString(redisUtil.lGetAll(testList)));

		final String testZset = "test-redis-zset";
		final String testZset1 = "test-redis-zset-1";
		final String testZset2 = "test-redis-zset-2";
		final String testZset3 = "test-redis-zset-3";
		final String testZset4 = "test-redis-zset-4";
		redisUtil.zAdd(testZset, testZset1, 2);
		redisUtil.zAdd(testZset, testZset2, 1);
		redisUtil.zAdd(testZset, testZset3, 3);
		redisUtil.zAdd(testZset, testZset4, 4);
		log.debug("==zRank(0):{}", redisUtil.zRank(testZset, testZset2));
		log.debug("==zRange(1&3):{}", redisUtil.range(testZset, 1, 2));
	}

}
