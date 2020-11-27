package com.bonelf.common.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "bonelf.oauth2")
public class Oauth2Property {
	/**
	 * Oauth2JwtProperty
	 */
	private Oauth2JwtProperty jwt = new Oauth2JwtProperty();
	/**
	 * 不需要认证的api
	 */
	private String[] noAuth = new String[]{};
}
