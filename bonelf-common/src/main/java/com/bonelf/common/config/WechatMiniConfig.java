package com.bonelf.common.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisConfigImpl;
import com.bonelf.common.config.property.WechatProperty;
import com.bonelf.common.constant.CacheConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * <p>
 * 支付配置
 * </p>
 * @author guaishou
 * @since 2020/9/9 14:38
 */
@Component
public class WechatMiniConfig {
	@Autowired
	private WechatProperty wechatProperty;
	@Value("${spring.redis.host:localhost}")
	private String host;
	@Value("${spring.redis.database:0}")
	private String database;
	@Value("${spring.redis.port:6379}")
	private Integer port;
	@Value("${spring.redis.password:}")
	private String password;
	@Value("${spring.redis.timeout:2000ms}")
	private Duration timeout;
	@Value("${spring.redis.jedis.pool.max-wait:100}")
	private Long maxWait;
	@Value("${spring.redis.jedis.pool.max-idle:100}")
	private Integer maxIdle;
	@Value("${spring.redis.jedis.pool.max-total:1024}")
	private Integer maxTotal;


	@Bean
	public WxMaService wxMaService() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		// 设置配置
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWait);
		//jedis 第一次启动时，会报错
		jedisPoolConfig.setTestOnBorrow(false);
		jedisPoolConfig.setTestOnReturn(true);
		int timeoutMils = Long.valueOf(timeout.getSeconds() * 1000).intValue();
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeoutMils, password, database);

		WxMaRedisConfigImpl config = new WxMaRedisConfigImpl(jedisPool);
		config.setRedisKeyPrefix(CacheConstant.WX_MA_KEY_PREFIX);
		config.setAppid(wechatProperty.getMini().getAppid());
		config.setSecret(wechatProperty.getSecret());
		config.setMsgDataFormat("JSON");
		//config.setToken(token);
		//config.setAesKey(aesKey);
		WxMaService wxMaService = new WxMaServiceImpl();
		wxMaService.setWxMaConfig(config);
		return wxMaService;
	}
}