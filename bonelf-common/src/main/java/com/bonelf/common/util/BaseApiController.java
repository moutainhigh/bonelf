package com.bonelf.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bonelf.common.constant.AuthConstant;
import com.bonelf.common.constant.CacheConstant;
import com.bonelf.common.constant.ShiroRealmName;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.BizExceptionEnum;
import com.bonelf.common.domain.CommonUser;
import com.bonelf.common.util.redis.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * controller层
 * 理论不用@Component
 */
@Component
public abstract class BaseApiController {
	//随着继承BaseApiController 即注入 可以的话试试private
	@Autowired
	protected RedisUtil redisUtil;
	@Autowired
	protected HttpServletRequest request;

	/**
	 * <p>
	 * 默认分页
	 * </p>
	 * @author guaishou
	 * @since 2020/9/15 15:39
	 */
	public <T> Page<T> defaultPage() {
		long page;
		long limit;
		String pageSizeStr = request.getParameter("page");
		String pageIndexStr = request.getParameter("limit");
		if (pageSizeStr == null || pageIndexStr == null) {
			throw new BonelfException(BizExceptionEnum.REQUEST_INVALIDATE, "分页参数未传");
		}
		try {
			//页数
			page = Long.parseLong(pageSizeStr);
			//每页记录条数
			limit = Long.parseLong(pageIndexStr);
		} catch (NumberFormatException e) {
			throw new BonelfException(BizExceptionEnum.REQUEST_INVALIDATE, "分页参数错误");
		}
		if (limit > 500) {
			throw new BonelfException(BizExceptionEnum.REQUEST_INVALIDATE, "页数超出限制");
		}
		return new Page<>(page, limit);
	}


	public <T> Page<T> defaultPage(Long pageDefault, Long limitDefault) {
		long page;
		long limit;
		String pageSizeStr = request.getParameter("page");
		String pageIndexStr = request.getParameter("limit");
		//页数
		page = pageSizeStr == null ? pageDefault : Long.parseLong(pageSizeStr);
		//每页记录条数
		limit = pageIndexStr == null ? limitDefault : Long.parseLong(pageIndexStr);
		return new Page<T>(page, limit);
	}

	/**
	 * 获得用户编号
	 * @return
	 */
	protected Long getUserId() {
		CommonUser loginUser = (CommonUser)SecurityUtils.getSubject().getPrincipal();
		//另LoginInterceptor往request中加了id
		return loginUser == null ? Long.parseLong(getClaims().getId()) : loginUser.getUserId();
	}

	/**
	 * 获得用户编号
	 * @return
	 */
	protected Long getUserIdCanNull() {
		CommonUser loginUser = (CommonUser)SecurityUtils.getSubject().getPrincipal();
		if (loginUser != null) {
			return loginUser.getUserId();
		}
		Claims claims;
		try {
			claims = getClaims();
		} catch (ExpiredJwtException | BonelfException e) {
			return null;
		}
		return Long.parseLong(claims.getId());
	}

	/**
	 * 获得用户手机号
	 * @return
	 */
	protected String getPhone() {
		CommonUser loginUser = (CommonUser)SecurityUtils.getSubject().getPrincipal();
		if (loginUser != null) {
			return loginUser.getUsername();
		}
		return getClaims().getSubject();
	}

	/**
	 * <p>
	 * 活动claims
	 * {link->AbstractShiroRealm}
	 * 请不要使用这个获取token信息，请使用 (CommonUser)SecurityUtils.getSubject().getPrincipal()
	 * </p>
	 * @author guaishou
	 * @since 2020/9/3 10:54
	 */
	@Deprecated
	protected Claims getClaims() {
		String token = request.getHeader(AuthConstant.HEADER);
		if (!StringUtils.hasText(token)) {
			throw new BonelfException(BizExceptionEnum.LOGIN_EXPIRED);
		}
		try {
			//此处token经过Shiro已经设置为新值，不存在缓存中token过期的可能,，但还是校验了
			Claims claims = JwtTokenUtil.getClaimFromToken(token);
			final Date expiration = claims.getExpiration();
			if (expiration.before(new Date())) {
				//提醒前端刷新token 1已在JwtFilter处理 2放开JwtFilter的鉴权但是通过像getUserIdCanNull获得id时也要处理返回
				ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
				HttpServletResponse response = servletRequestAttributes == null ? null : servletRequestAttributes.getResponse();
				//缓存token是否已被处理
				String cacheToken = (String)redisUtil.get(String.format(CacheConstant.API_USER_TOKEN_PREFIX, claims.getId()));
				if (cacheToken == null) {
					throw new BonelfException(BizExceptionEnum.NOT_LOGIN);
				}
				Claims cacheClaims = JwtTokenUtil.getClaimFromToken(cacheToken);
				if (response != null) {
					this.putNewToken2Resp(response, cacheClaims);
				}
			}
			return claims;
		} catch (ExpiredJwtException e) {
			throw new BonelfException(BizExceptionEnum.LOGIN_EXPIRED);
		}
	}

	private void putNewToken2Resp(HttpServletResponse response, Claims cacheClaims) {
		if (cacheClaims.get(AuthConstant.REFRESH_CLAIM_FLAG) != null) {
			//已经刷新过 需要主动刷新
			response.setHeader(AuthConstant.RESP_HEADER, "expired");
		} else {
			String newAuthorization = JwtTokenUtil.generateRefreshToken(cacheClaims.getId(), cacheClaims.getSubject(), ShiroRealmName.API_SHIRO_REALM);
			redisUtil.set(String.format(CacheConstant.API_USER_TOKEN_PREFIX, cacheClaims.getId()), newAuthorization, AuthConstant.REFRESH_SECOND);
			response.setHeader(AuthConstant.RESP_HEADER, newAuthorization);
		}
	}

	public static void main(String[] args) {
		//System.out.println(JwtTokenUtil.generateToken(2L, "13758233011", ShiroRealmName.API_SHIRO_REALM));
		//一个过期的token
		long time = System.currentTimeMillis();
		System.out.println("===");
		Claims c = JwtTokenUtil.getClaimFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzc1ODIzMzAxMSIsInJlYWxtVHlwZSI6ImFwaVNoaXJvUmVhbG0iLCJleHAiOjE2MDQxMTE2NDgsImp0aSI6IjIifQ.tf-ghHUZMpq26EblqCWd6lEl3Wcwzc871HzbbaK8yd6wHx9nagVdwxl00nMjGBa2QwORwLvWrY_VRqAXGEf1xQ");
		System.out.println(c.getId());
		System.out.println(c.getSubject());
		System.out.println(c.getExpiration());
		System.out.println(System.currentTimeMillis() - time);
	}
}
