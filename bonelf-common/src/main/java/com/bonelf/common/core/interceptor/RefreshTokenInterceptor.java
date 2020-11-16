package com.bonelf.common.core.interceptor;

import com.bonelf.common.constant.AuthConstant;
import com.bonelf.common.constant.CacheConstant;
import com.bonelf.common.constant.ShiroRealmName;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.BizExceptionEnum;
import com.bonelf.common.domain.CommonUser;
import com.bonelf.common.util.JwtTokenUtil;
import com.bonelf.common.util.redis.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 刷新token机制拦截器
 * </p>
 * @author Chenyuan
 * @since 2020/11/7 16:47
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {

	private RedisUtil redisUtil;

	public RefreshTokenInterceptor(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}

	/**
	 * 检查token是否需要刷新
	 * @param request
	 * @param response
	 * @param handler
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = this.getToken(request);
		if (token == null) {
			//没有token就不用走是否刷新，也不用管是否过滤，这些Shiro都做了
			return true;
		}
		// 从登录数据获得id和subject，不再解析token，因为解析token耗时挺长，可以试试大概2-3秒，整个接口最好只解析一次
		CommonUser loginUser = (CommonUser)SecurityUtils.getSubject().getPrincipal();
		//CommonUser loginUser = null;
		if (loginUser == null) {
			//没有token就不用走是否刷新，也不用管是否过滤，这些Shiro都做了
			return true;
		}
		try {
			//缓存里的合法token
			String cacheToken = (String)redisUtil.get(String.format(CacheConstant.API_USER_TOKEN_PREFIX, loginUser.getUserId()));
			//使用redis实现   单点登录
			if (cacheToken == null) {
				throw new BonelfException(BizExceptionEnum.LOGIN_EXPIRED);
			}
			Claims claimsCache = JwtTokenUtil.getClaimFromToken(cacheToken);
			if (!token.equals(cacheToken)) {
				if (claimsCache.get(AuthConstant.REFRESH_CLAIM_FLAG) != null) {
					//1刷新token但是客户端没有存新的token（无需处理） 让前端主动调接口获取 不然就等过期
					//并注释下面的刷新token相关的代码免得走一遍解析缓存token费时 refresh_time后重新登录
				} else {
					//2 用户在其他设备登录导致过期
					throw new BonelfException(BizExceptionEnum.LOGIN_INSTEAD);
				}
			}
			//提醒前端刷新token ，如果不处理等refresh_time时间token彻底过期就再需要登录，不使用刷新token也是可以的
			if (claimsCache != null && claimsCache.get(AuthConstant.REFRESH_CLAIM_FLAG) != null) {
				//已经刷新过 需要主动刷新 TODO 刷新token的接口 让前端主动调接口获取
				response.setHeader(AuthConstant.RESP_HEADER, "expired");
			} else {
				String newAuthorization = JwtTokenUtil.generateRefreshToken(loginUser.getUserId(), loginUser.getUsername(), ShiroRealmName.API_SHIRO_REALM);
				redisUtil.set(String.format(CacheConstant.API_USER_TOKEN_PREFIX, loginUser.getUserId()), newAuthorization, AuthConstant.REFRESH_SECOND);
				response.setHeader(AuthConstant.RESP_HEADER, newAuthorization);
			}
		} catch (ExpiredJwtException e) {
			throw new BonelfException(BizExceptionEnum.LOGIN_EXPIRED);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader(AuthConstant.HEADER);
		return token == null ? null : JwtTokenUtil.rmPrefix(token);
	}
}
