package com.bonelf.gateway.service.impl;

import com.bonelf.gateway.client.AuthClient;
import com.bonelf.gateway.domain.Result;
import com.bonelf.gateway.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthClient authProvider;

	/**
	 * Authorization认证开头是"bearer "
	 */
	private static final int BEARER_BEGIN_INDEX = 7;

	/**
	 * jwt token 密钥，主要用于token解析，签名验证
	 */
	@Value("${spring.security.oauth2.jwt.signing-key:123456}")
	private String signingKey;
	/**
	 * jwt验签
	 */
	private RsaVerifier verifier;

	@Override
	public Result<Boolean> authenticate(String authentication, String url, String method) {
		return authProvider.auth(authentication, url, method);
		//return authenticationService.decide(new HttpServletRequestAuthWrapper(request, url, method))
	}

	@Override
	public boolean hasPermission(Result<Boolean> authResult) {
		log.debug("签权结果:{}", authResult.getResult());
		return authResult.getSuccess() && (boolean)authResult.getResult();
	}

	@Override
	public boolean hasPermission(String authentication, String url, String method) {
		//token是否有效
		if (invalidJwtAccessToken(authentication)) {
			return Boolean.FALSE;
		}
		//从认证服务获取是否有权限
		return hasPermission(authenticate(authentication, url, method));
	}

	@Override
	public boolean invalidJwtAccessToken(String authentication) {
		verifier = Optional.ofNullable(verifier).orElse(new RsaVerifier(signingKey));
		//是否无效true表示无效
		boolean invalid = Boolean.TRUE;

		try {
			Jwt jwt = getJwt(authentication);
			jwt.verifySignature(verifier);
			invalid = Boolean.FALSE;
		} catch (InvalidSignatureException | IllegalArgumentException ex) {
			log.warn("user token has expired or signature error ");
		}
		return invalid;
	}

	@Override
	public Jwt getJwt(String authentication) {
		return JwtHelper.decode(StringUtils.substring(authentication, BEARER_BEGIN_INDEX));
	}
}
