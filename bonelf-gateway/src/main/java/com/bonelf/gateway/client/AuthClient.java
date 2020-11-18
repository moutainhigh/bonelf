package com.bonelf.gateway.client;

import com.bonelf.gateway.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "auth", fallback = AuthClient.AuthProviderFallback.class)
public interface AuthClient {
	/**
	 * 调用签权服务，判断用户是否有权限
	 * @param authentication
	 * @param url
	 * @param method
	 * @return
	 */
	@PostMapping(value = "/bonelf/auth/permission")
	Result<Boolean> auth(@RequestHeader(HttpHeaders.AUTHORIZATION) String authentication, @RequestParam("url") String url, @RequestParam("method") String method);

	@Component
	class AuthProviderFallback implements AuthClient {
		/**
		 * 降级统一返回无权限
		 * @param authentication
		 * @param url
		 * @param method
		 * @return <pre>
		 * Result:
		 * {
		 *   code:"-1"
		 *   mesg:"系统异常"
		 * }
		 * </pre>
		 */
		@Override
		public Result<Boolean> auth(String authentication, String url, String method) {
			return Result.error();
		}
	}
}
