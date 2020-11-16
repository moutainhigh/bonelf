package com.bonelf.common.config;

import cn.hutool.core.collection.CollectionUtil;
import com.bonelf.common.constant.AuthConstant;
import com.bonelf.common.constant.ShiroRealmName;
import com.bonelf.common.core.shiro.constant.ShiroRealmEnum;
import com.bonelf.common.core.shiro.filter.JwtFilter;
import com.bonelf.common.core.shiro.realm.ApiShiroRealm;
import com.bonelf.common.core.shiro.realm.CustomerRealmAuthorizer;
import com.bonelf.common.core.shiro.realm.ShiroRealmAuthenticator;
import com.bonelf.common.core.shiro.realm.SysShiroRealm;
import com.bonelf.common.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import java.util.*;

/**
 * <p>
 * 全局配置了Shiro
 * fixme https://blog.csdn.net/lujiangui/article/details/82773646
 * </p>
 * @author bonelf
 * @since 2020/10/12 11:17
 */
@Slf4j
@Configuration
public class ShiroConfig {
	@Value("${spring.redis.database:0}")
	private String database;

	@Value("${spring.redis.port:6379}")
	private String port;

	@Value("${spring.redis.host:localhost}")
	private String host;

	@Value("${spring.redis.password:}")
	private String redisPassword;

	@Value("${server.servlet.context-path:}")
	private String ctxPath;

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * shiro 过滤器
	 * Filter Chain定义说明
	 * 1、一个URL可以配置多个Filter，使用逗号分隔
	 * 2、当设置多个过滤器时，全部验证通过，才视为通过
	 * 3、部分过滤器可指定参数，如perms，roles
	 *
	 * anon:例子/admins/**=anon 没有参数，表示可以匿名使用。
	 * authc:例如/admins/user/**=authc表示需要认证(登录)才能使用，没有参数
	 * roles(角色)：例子/admins/user/**=roles[admin],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，当有多个参数时，例如admins/user/**=roles["admin,guest"],每个参数通过才算通过，相当于hasAllRoles()方法。
	 * perms（权限）：例子/admins/user/**=perms[user:add:*],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，例如/admins/user/**=perms["user:add:*,user:modify:*"]，当有多个参数时必须每个参数都通过才通过，想当于isPermitedAll()方法。
	 * rest：例子/admins/user/**=rest[user],根据请求的方法，相当于/admins/user/**=perms[user:method] ,其中method为post，get，delete等。
	 * port：例子/admins/user/**=port[8081],当请求的url的端口不是8081是跳转到schemal://serverName:8081?queryString,其中schmal是协议http或https等，serverName是你访问的host,8081是url配置里port的端口，queryString
	 * 是你访问的url里的？后面的参数。
	 * authcBasic：例如/admins/user/**=authcBasic没有参数表示httpBasic认证
	 * ssl:例子/admins/user/**=ssl没有参数，表示安全的url请求，协议为https
	 * user:例如/admins/user/**=user没有参数表示必须存在用户，当登入操作时不做检查
	 * @param securityCustomerManager 自定义securityCustomerManager
	 * @return shiroFilter
	 */
	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityCustomerManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityCustomerManager);
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		//不用加ctxPath
		if (CollectionUtil.isNotEmpty(AuthConstant.EXCLUDE_URLS)) {
			for (String noAuthUrl : AuthConstant.EXCLUDE_URLS) {
				filterChainDefinitionMap.put( noAuthUrl, "anon");
			}
		}
		filterChainDefinitionMap.put("/favicon.ico", "anon");
		filterChainDefinitionMap.put("/error", "anon");
		filterChainDefinitionMap.put("/sys/error/**", "anon");
		filterChainDefinitionMap.put("/swagger-ui", "anon");
		filterChainDefinitionMap.put("/swagger**/**", "anon");
		filterChainDefinitionMap.put("/v1/user/loginByAccount", "anon");
		filterChainDefinitionMap.put("/v1/user/sendVerify", "anon");
		filterChainDefinitionMap.put("/v1/user/wxLogin", "anon");
		// 添加自定义过滤器为jwt
		Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
		filterMap.put("jwt", new JwtFilter(ShiroRealmEnum.API_SHIRO_REALM));
		shiroFilterFactoryBean.setFilters(filterMap);
		// <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
		//filterChainDefinitionMap.put(ctxPath + "/**", "jwt");
		filterChainDefinitionMap.put("/v1/user/getUserFeign", "anon");
		filterChainDefinitionMap.put("/**", "jwt");
		// 未授权界面返回的JSON
		shiroFilterFactoryBean.setUnauthorizedUrl("/sys/error/401");
		//不使用登录，如果不设置如果在Filter中executeLogin返回false就会弹出登录弹窗
		//shiroFilterFactoryBean.setLoginUrl(ctxPath + "/sys/login");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * 系统自带的Realm管理，主要针对多realm 认证
	 */
	@Bean("authenticator")
	public ModularRealmAuthenticator modularRealmAuthenticator() {
		//自己重写的ModularRealmAuthenticator
		ShiroRealmAuthenticator modularRealmAuthenticator = new ShiroRealmAuthenticator();
		modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
		return modularRealmAuthenticator;
	}

	@Bean
	public ModularRealmAuthorizer modularRealmAuthorizer() {
		return new CustomerRealmAuthorizer();
	}

	/**
	 * api接口shiro控制器
	 * @return ShiroRealm
	 */
	@Bean(ShiroRealmName.API_SHIRO_REALM)
	public AuthorizingRealm apiShiroRealm() {
		return new ApiShiroRealm();
	}

	/**
	 * 系统管理接口shiro控制器
	 * @return ShiroRealm
	 */
	@Bean(ShiroRealmName.SYS_SHIRO_REALM)
	public AuthorizingRealm sysShiroRealm() {
		return new SysShiroRealm();
	}

	/**
	 * 自定义securityCustomerManager
	 * @return securityCustomerManager
	 */
	@Bean("securityCustomerManager")
	@DependsOn({ShiroRealmName.API_SHIRO_REALM, ShiroRealmName.SYS_SHIRO_REALM, "redisManager"})
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		List<Realm> realms = new ArrayList<>();
		realms.add(apiShiroRealm());
		realms.add(sysShiroRealm());
		securityManager.setRealms(realms);
		// 需要在realm定义之前
		securityManager.setAuthenticator(modularRealmAuthenticator());
		// 新增下面这个授权控制
		securityManager.setAuthorizer(modularRealmAuthorizer());
		securityManager.setRealms(realms);
		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		securityManager.setSubjectDAO(subjectDAO);

		// 自定义缓存实现 使用redis
		securityManager.setCacheManager(redisCacheManager());
		return securityManager;
	}

	/**
	 * cacheManager 缓存 redis实现
	 * 使用的是shiro-redis开源插件
	 * @return
	 */
	public RedisCacheManager redisCacheManager() {
		log.info("===============(1)创建缓存管理器RedisCacheManager=======");
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisManager());
		//redis中针对不同用户缓存(此处的id需要对应user实体中的id字段,用于唯一标识)
		redisCacheManager.setPrincipalIdFieldName("userId");
		//用户权限信息缓存时间
		redisCacheManager.setExpire(200000);
		return redisCacheManager;
	}

	/**
	 * 配置shiro redisManager
	 * 使用的是shiro-redis开源插件
	 * @return
	 */
	@Bean
	public RedisManager redisManager() {
		log.info("===============(2)创建RedisManager,连接Redis..URL= " + host + ":" + port);
		RedisManager redisManager = new RedisManager();
		redisManager.setHost((StringUtils.hasText(host) ? host : "localhost") + ":" + (StringUtils.hasText(port) ? port : 6379));
		redisManager.setDatabase(StringUtils.hasText(database) ? Integer.parseInt(database) : 0);
		redisManager.setTimeout(0);
		if (!StringUtils.isEmpty(redisPassword)) {
			redisManager.setPassword(redisPassword);
		}
		return redisManager;
	}

	/**
	 * 下面的代码是添加注解支持
	 * @return
	 */
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	@Bean
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
}
