/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.auth.core.exception;

import com.bonelf.common.constant.BizConstants;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.domain.Result;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * <p>
 * 自定义的异常封装
 * </p>
 * @author bonelf
 * @since 2020/11/21 9:46
 */
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
class CustomOauthException extends OAuth2Exception {

	@Getter
	private final Result<?> result;

	CustomOauthException(OAuth2Exception oAuth2Exception) {
		super(oAuth2Exception.getSummary(), oAuth2Exception);
		this.result = Result.error(AuthExceptionEnum.valueOf(oAuth2Exception.getOAuth2ErrorCode().toUpperCase()), oAuth2Exception);
	}

	CustomOauthException(InternalAuthenticationServiceException b) {
		super(b.getMessage(), b);
		if (b.getCause() != null && b.getCause() instanceof BonelfException) {
			BonelfException bonelfException = (BonelfException)b.getCause();
			this.result = Result.error(bonelfException.getCode(), b.getMessage());
		} else {
			this.result = Result.error(BizConstants.CODE_500, b.getMessage());
		}
	}
}