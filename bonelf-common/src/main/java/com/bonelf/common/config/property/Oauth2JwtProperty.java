package com.bonelf.common.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "bonelf.oauth2.jwt")
public class Oauth2JwtProperty {
	/**
	 * jwt签名
	 * use keystore instead
	 */
	@Deprecated
	private String signingKey = "123456";
	/**
	 * classpath 密钥文件
	 */
	private String keystore;
	/**
	 * 密码
	 */
	private String password = "=bonelf=";
	/**
	 * 别名
	 */
	private String alias = "bonelf";
}
