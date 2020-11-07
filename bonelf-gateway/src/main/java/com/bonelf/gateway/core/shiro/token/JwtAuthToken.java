package com.bonelf.gateway.core.shiro.token;

import com.bonelf.gateway.core.shiro.constant.ShiroRealmEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.BearerToken;

/**
 * JwtToken
 **/
public class JwtAuthToken extends BearerToken {
	private static final long serialVersionUID = 1L;
	@Setter
	@Getter
	private ShiroRealmEnum realmType;

	public JwtAuthToken(String token) {
		super(token);
	}

	public JwtAuthToken(String token, ShiroRealmEnum realmType) {
		super(token);
		this.realmType = realmType;
	}

	public JwtAuthToken(String token, String host) {
		super(token, host);
	}
}
