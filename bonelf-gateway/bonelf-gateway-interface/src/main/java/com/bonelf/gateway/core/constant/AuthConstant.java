package com.bonelf.gateway.core.constant;

import cn.hutool.core.collection.CollectionUtil;

import java.util.List;

/**
 * <p>
 * 服务调用 令牌常量信息存储
 * </p>
 * @author bonelf
 * @since 2020/10/11 17:27
 */
public interface AuthConstant {
	/**
	 * 认证头
	 */
	String HEADER = "Authorization";
	/**
	 * 刷新token返回在头部的token信息
	 */
	String RESP_HEADER = "NewAuthorization";
	/**
	 * websocket认证头
	 */
	String WEBSOCKET_HEADER = "Sec-WebSocket-Protocol";
	/**
	 * token前缀
	 */
	String TOKEN_PREFIX = "Bearer ";
	/**
	 * 前面
	 */
	String ISSUER = "Bonelf";
	/**
	 * 密钥
	 */
	String SECRET = "==bonelfSecret==";
	/**
	 * 过期时间 7天
	 */
	long EXPIRATION_SECOND = 604800L;
	/**
	 * 自动重新登录时间 （真实过期时间）
	 * 在REFRESH_SECOND-EXPIRATION_SECOND期间 token过期将通过对应自动刷新
	 */
	long REFRESH_SECOND = 2 * EXPIRATION_SECOND;
	/**
	 * token payload 数据
	 */
	String CLAIMS_REALM_TYPE = "realmType";
	/**
	 * 刷新token payload 标记
	 */
	String REFRESH_CLAIM_FLAG = "refreshFlag";
	/**
	 * 前端MD5加密秘钥 8byte
	 */
	String FRONTEND_SAIT_CRYPTO = "==cjFE==";
	/**
	 * 数据库数据MD5加密秘钥
	 */
	String DATABASE_SALT_MD5 = "=choujiangDB=";

	@Deprecated
	List<String> EXCLUDE_URLS = CollectionUtil.newLinkedList(

	);
}
