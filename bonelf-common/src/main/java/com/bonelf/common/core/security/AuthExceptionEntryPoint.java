package com.bonelf.common.core.security;

import com.bonelf.common.domain.Result;
import com.bonelf.common.util.JsonUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 异常封装
 * </p>
 * @author bonelf
 * @since 2020/11/21 11:53
 */
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException) throws ServletException {
		//Throwable cause = authException.getCause();
		response.setStatus(HttpStatus.OK.value());
			response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
			Result<?> result;
			try {
			if (authException instanceof InsufficientAuthenticationException) {
				result = Result.error(40005, "用户凭据非法");
			} else {
				result = Result.error(40005, authException.getMessage());
			}
			response.getWriter().write(JsonUtil.toJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

