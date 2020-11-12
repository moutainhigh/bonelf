package com.bonelf.gateway.core.shiro.realm;

import com.bonelf.cicada.util.NetUtil;
import com.bonelf.gateway.core.constant.CacheConstant;
import com.bonelf.gateway.core.constant.enums.FreezeEnum;
import com.bonelf.gateway.core.shiro.constant.AuthExceptionMsgConstant;
import com.bonelf.gateway.core.shiro.constant.ShiroRealmEnum;
import com.bonelf.gateway.core.shiro.token.JwtAuthToken;
import com.bonelf.gateway.domain.CommonUser;
import com.bonelf.gateway.util.JwtTokenUtil;
import com.bonelf.gateway.util.SpringContextUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Map;
import java.util.Set;

/**
 * 用户登录鉴权和获取用户授权
 * 校验用户、用户权限；token过期自动登录
 * @author bonelf
 */
@Slf4j
public abstract class AbstractShiroRealm extends AuthorizingRealm {

	/**
	 * 必须重写此方法，不然Shiro会报错
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtAuthToken;
	}

	/**
	 * 权限信息认证(包括角色以及权限)是用户访问controller的时候才进行验证(redis存储的此处权限信息)
	 * 触发检测用户权限时才会调用此方法，例如checkRole,checkPermission
	 * @param principals 身份信息
	 * @return AuthorizationInfo 权限信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		log.debug("===============Shiro权限认证开始==============");
		Long userId;
		if (principals != null) {
			CommonUser commonUser = (CommonUser)principals.getPrimaryPrincipal();
			userId = commonUser.getUserId();
		} else {
			throw new AuthenticationException(AuthExceptionMsgConstant.INVALID_USER);
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Map<String, Set<String>> roleAndPermission = this.getUserRolesAndPermission(userId);
		// 设置用户拥有的角色集合，比如“admin,test”
		Set<String> roleSet = roleAndPermission.get("roles");
		info.setRoles(roleSet);
		// 设置用户拥有的权限集合，比如“sys:role:add,sys:user:add”
		Set<String> permissionSet = roleAndPermission.get("permissions");
		info.addStringPermissions(permissionSet);
		log.debug("===============Shiro权限认证成功==============");
		return info;
	}

	/**
	 * 用户信息认证是在用户进行登录的时候进行验证(不存redis)
	 * 也就是说验证用户输入的账号和密码是否正确，错误抛出异常
	 * @param auth 用户登录的账号密码信息
	 * @return 返回封装了用户信息的 AuthenticationInfo 实例
	 * @throws AuthenticationException AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
		String token = (String)auth.getCredentials();
		if (token == null) {
			log.warn("————————身份认证失败，非法请求，IP：{}——————————", NetUtil.getIpAddrByRequest(SpringContextUtils.getHttpServletRequest()));
			throw new AuthenticationException(AuthExceptionMsgConstant.EMPTY_TOKEN);
		}
		// 校验token有效性
		CommonUser loginUser = this.checkUserTokenIsEffect(token);
		return new SimpleAuthenticationInfo(loginUser, token, getName());
	}

	/**
	 * JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能）
	 * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)，缓存有效期设置为Jwt有效时间的2倍
	 * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
	 * 用户过期时间 = Jwt有效时间 * 2。
	 * @param token Shiro JWTToken中的credentials 这个token解析时如果过期一个刷新周期了会引起ExpiredJwtException，所以不可解析
	 * 获取用户id
	 * @return user
	 */
	public CommonUser checkUserTokenIsEffect(String token) {
		CommonUser commonUser;
		// getClaimFromToken已设置允许时间不让抛异常
		Claims claims;
		try {
			claims = JwtTokenUtil.getClaimFromToken(token);
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			throw new AuthenticationException(AuthExceptionMsgConstant.LOGIN_EXPIRE);
		}
		String userIdStr = claims.getId();
		long userId;
		try {
			userId = Long.parseLong(userIdStr);
		} catch (NumberFormatException e) {
			throw new AuthenticationException(AuthExceptionMsgConstant.INVALID_TOKEN);
		}
		// 查询用户信息
		commonUser = this.getCommonUser(userId);
		if (commonUser == null) {
			throw new AuthenticationException(AuthExceptionMsgConstant.INVALID_USER);
		}
		// 判断用户状态
		if (FreezeEnum.FREEZE.getCode().equals(commonUser.getStatus())) {
			throw new AuthenticationException(AuthExceptionMsgConstant.FROZEN);
		}
		return commonUser;
	}

	/**
	 * 清除当前用户的权限认证缓存
	 * @param principals 权限信息
	 */
	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	/**
	 * redisKey
	 * @return
	 */
	@Deprecated
	protected String getRedisKey() {
		return CacheConstant.API_USER_TOKEN_PREFIX;
	}

	/**
	 * 权限列表
	 * @param userId
	 * @return
	 */
	protected abstract Map<String, Set<String>> getUserRolesAndPermission(Long userId);

	/**
	 * 用户
	 * @param userId
	 * @return
	 */
	protected abstract CommonUser getCommonUser(Long userId);

	/**
	 * payload中存放对应的realm信息
	 * @return
	 */
	@Deprecated
	protected ShiroRealmEnum realmEnum() {
		return ShiroRealmEnum.API_SHIRO_REALM;
	}

	/**
	 * JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能）
	 * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)，缓存有效期设置为Jwt有效时间的2倍
	 * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
	 * 3、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
	 * 4、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
	 * 注意： 前端请求Header中设置Authorization保持不变，校验有效性以缓存中的token为准。
	 * 用户过期时间 = Jwt有效时间 * 2。
	 *
	 * 新的刷新token方式见{link BaseApiController#getClaims}
	 *
	 * 多端登录时 每个设备的token都是独立的
	 * @param token Shiro JWTToken中的credentials 这个token解析时如果过期一个刷新周期了会引起ExpiredJwtException，所以不可解析
	 * 获取用户id，
	 * 问题：
	 * 1这个机制导致不能简单使用redis缓存用户id和token关系做单点登录（不然就是再缓存一个关系或者把允许过期时间拉到Long.maxValue
	 * 这样不太好）。
	 * 2重新登录旧的token就一直在缓存中等待超时删除
	 * 3单点登录要实现必须加一个再加个id和用户端存的token对应关系
	 * @return jwtTokenRefresh
	 */
	//@Deprecated
	//public CommonUser checkUserTokenEffect(String token) {
	//	String redisKey = this.getRedisKey();
	//	String cacheToken = (String)redisUtil.get(String.format(redisKey, token));
	//	if (!StringUtils.hasText(cacheToken)) {
	//		cacheToken = token;
	//	}
	//	CommonUser commonUser;
	//	// 校验token有效性 cacheToken如果不存在就代表真的过期了，需要重新登录
	//	ShiroRealmEnum realmEnum = realmEnum();
	//	// getClaimFromToken已设置允许时间不让抛异常
	//	Claims claims;
	//	try {
	//		claims = JwtTokenUtil.getClaimFromToken(cacheToken);
	//	} catch (ExpiredJwtException e) {
	//		e.printStackTrace();
	//		throw new AuthenticationException(AuthExceptionMsgConstant.LOGIN_EXPIRE);
	//	}
	//	String userIdStr = claims.getId();
	//	Long userId;
	//	try {
	//		userId = Long.parseLong(userIdStr);
	//	} catch (NumberFormatException e) {
	//		redisUtil.del(String.format(redisKey, token));
	//		throw new AuthenticationException(AuthExceptionMsgConstant.INVALID_TOKEN);
	//	}
	//	// 查询用户信息
	//	commonUser = this.getCommonUser(userId);
	//	if (commonUser == null) {
	//		throw new AuthenticationException(AuthExceptionMsgConstant.INVALID_USER);
	//	}
	//	// 判断用户状态
	//	if (FreezeEnum.FREEZE.getCode().equals(commonUser.getStatus())) {
	//		throw new AuthenticationException(AuthExceptionMsgConstant.FROZEN);
	//	}
	//	// token过期需要替换
	//	if (claims.getExpiration().before(new Date())) {
	//		// 生成新的token不返回给前端 而是在redis里存了一个oldToken->newToken的对应关系
	//		String newAuthorization = JwtTokenUtil.generateToken(userId, commonUser.getUsername(), realmEnum == null ? "" : realmEnum.getRealmName());
	//		// 设置超时时间
	//		redisUtil.set(String.format(redisKey, token), newAuthorization, AuthConstant.REFRESH_SECOND);
	//		log.info("——————————用户在线操作，更新token保证不掉线————————————— " + token);
	//	}
	//	return commonUser;
	//}
}
