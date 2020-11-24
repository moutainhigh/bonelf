package com.bonelf.common.core.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * <p>
 * debug用拦截器
 * </p>
 * @author bonelf
 * @since 2020/11/7 16:47
 */
@Slf4j
public class DebugInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//出现Unauthorized会这个request重定向到/error 暂时没找到什么原因
		Enumeration<String> headers = request.getHeaders(HttpHeaders.AUTHORIZATION);
		String authentication = "";
		if (headers.hasMoreElements()) {
			authentication = request.getHeaders(HttpHeaders.AUTHORIZATION).nextElement();
		}
		String method = request.getMethod();
		String url = request.getRequestURI();
		log.debug("url:{},method:{},headers:{}", url, method, authentication);
		return true;
	}
}
