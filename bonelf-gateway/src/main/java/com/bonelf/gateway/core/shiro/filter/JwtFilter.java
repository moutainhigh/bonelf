package com.bonelf.gateway.core.shiro.filter;

import com.bonelf.gateway.core.constant.AuthConstant;
import com.bonelf.gateway.core.shiro.constant.AuthExceptionMsgConstant;
import com.bonelf.gateway.core.shiro.constant.ShiroRealmEnum;
import com.bonelf.gateway.core.shiro.token.JwtAuthToken;
import com.bonelf.gateway.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 鉴权登录拦截器
 * 校验token合法性
 **/
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
	protected ShiroRealmEnum shiroRealm;

	public JwtFilter(ShiroRealmEnum shiroRealm) {
		this.shiroRealm = shiroRealm;
	}

	/**
	 * 执行登录认证
	 * @param request request
	 * @param response response
	 * @param mappedValue mappedValue
	 * @return isAccessAllowed
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		AuthenticationToken token = createToken(request, response);
		try {
			executeLogin(request, response);
			return true;
		} catch (AuthenticationException e) {
			return onLoginFailure(token, e, request, response);
		} catch (Exception e) {
			return onLoginFailure(token, new AuthenticationException(AuthExceptionMsgConstant.LOGIN_EXPIRE, e), request, response);
		}
	}

	/**
	 * 登录失败处理
	 * @param token
	 * @param e
	 * @param request
	 * @param response
	 * @return 是否跳转到登录页面
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		try {
			HttpServletResponse httpServletResponse = (HttpServletResponse)response;
			//设置编码，否则中文字符在重定向时会变为空字符串
			//request.setAttribute("errMsg", message);
			httpServletResponse.sendRedirect("/sys/error/401?errMsg=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
		} catch (IOException exp) {
			log.error(exp.getMessage());
		}
		return false;
	}

	/**
	 * 每次请求执行登录
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String token = this.getToken(httpServletRequest);
		JwtAuthToken jwtToken;
		jwtToken = new JwtAuthToken(token, shiroRealm);
		// 提交给realm进行登入，如果错误他会抛出异常并被捕获
		getSubject(request, response).login(jwtToken);
		// 如果没有抛出异常则代表登入成功，返回true
		return true;
	}

	/**
	 * 对跨域提供支持
	 * 如果配置了nginx，并且nginx配置了跨域。这一块可以注释
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, httpServletRequest.getHeader(HttpHeaders.ORIGIN));
		httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,OPTIONS,PUT,DELETE");
		httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, httpServletRequest.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS));
		// 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
		if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
			httpServletResponse.setStatus(HttpStatus.OK.value());
			return false;
		}
		return super.preHandle(request, response);
	}


	private String getToken(HttpServletRequest request) {
		String token = request.getHeader(AuthConstant.HEADER);
		//redis
		if (token == null) {
			throw new AuthenticationException(AuthExceptionMsgConstant.EMPTY_TOKEN);
		}
		return JwtTokenUtil.rmPrefix(token);
	}
}
