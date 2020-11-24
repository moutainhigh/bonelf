/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.common.core.security;

import com.alibaba.fastjson.JSON;
import com.bonelf.common.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * <p>
 * 异常封装
 * 不是设置这个没有用，先留着
 * </p>
 * @author bonelf
 * @since 2020/11/21 11:53
 */
@Slf4j
public class AuthExceptionHandler extends OAuth2AccessDeniedHandler {

	@Override
	protected ResponseEntity<?> enhanceResponse(ResponseEntity<?> result, Exception authException) {
		Result<?> resp;
		ResponseEntity<Result<?>> responseEntity;
		log.error("AUTH ERROR:{}, EXP:{}", JSON.toJSONString(result.getBody()), authException.getMessage());
		if (authException instanceof OAuth2Exception) {
			OAuth2Exception oAuth2Exception = (OAuth2Exception)authException;
			resp = Result.error(40005, "无效token");
			responseEntity = ResponseEntity.status(oAuth2Exception.getHttpErrorCode())
					.body(resp);
		} else {
			resp = Result.error(result.getBody() == null ? null : result.getBody().toString());
			responseEntity = ResponseEntity.<Result<?>>ok(resp);
		}
		return responseEntity;
	}
}
