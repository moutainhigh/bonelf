package com.bonelf.gateway.service;

import com.bonelf.gateway.domain.Result;
import org.springframework.security.jwt.Jwt;

public interface AuthService {
	/**
	 * 调用签权服务，判断用户是否有权限
	 * @param authentication
	 * @param url
	 * @param method
	 * @return Result
	 */
	Result<Boolean> authenticate(String authentication, String url, String method);

	/**
	 * 查看签权服务器返回结果，有权限返回true
	 * @param authResult
	 * @return
	 */
	boolean hasPermission(Result<Boolean> authResult);

	/**
	 * 调用签权服务，判断用户是否有权限
	 * @param authentication
	 * @param url
	 * @param method
	 * @return true/false
	 */
	boolean hasPermission(String authentication, String url, String method);

	/**
	 * 是否无效authentication
	 * @param authentication
	 * @return
	 */
	boolean invalidJwtAccessToken(String authentication);

	/**
	 * 从认证信息中提取jwt token 对象
	 * @param authentication 认证信息  Authorization: bearer header.payload.signature
	 * @return Jwt对象
	 */
	Jwt getJwt(String authentication);
}
