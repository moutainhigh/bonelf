package com.bonelf.common.util;

import cn.hutool.core.util.RandomUtil;
import com.bonelf.common.constant.AuthConstant;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>jwt token工具类</p>
 * <pre>
 *     jwt的claim里一般包含以下几种数据:
 *         1. iss -- token的发行者
 *         2. sub -- 该JWT所面向的用户
 *         3. aud -- 接收该JWT的一方
 *         4. exp -- token的失效时间
 *         5. nbf -- 在此时间段之前,不会被处理
 *         6. iat -- jwt发布时间
 *         7. jti -- jwt唯一标识,防止重复使用
 * </pre>
 * @author chenyuan
 * @date 2020-02-22 17:21:00
 */
public class JwtTokenUtil {
	/**
	 * 去掉Bearer
	 */
	public static String rmPrefix(String token) {
		if (token.startsWith(AuthConstant.TOKEN_PREFIX)) {
			token = token.substring(AuthConstant.TOKEN_PREFIX.length());
		}
		return token;
	}

	/**
	 * 获取手机号从token中
	 */
	public static String getPhoneFromToken(String token) {
		return getClaimFromToken(token).getSubject();
	}

	/**
	 * 获取userId从token中
	 * 实际token存于缓存中，请不要那header中的token直接获取用户编号
	 */
	public static String getUserIdFromToken(String token) {
		return getClaimFromToken(token).getId();
	}

	/**
	 * 获取jwt发布时间
	 */
	public static Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token).getIssuedAt();
	}

	/**
	 * 获取jwt失效时间
	 */
	public static Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token).getExpiration();
	}

	/**
	 * 获取jwt接收者
	 */
	public static String getAudienceFromToken(String token) {
		return getClaimFromToken(token).getAudience();
	}

	/**
	 * 获取私有的jwt claim
	 */
	public static String getPrivateClaimFromToken(String token, String key) {
		return getClaimFromToken(token).get(key).toString();
	}

	/**
	 * 获取jwt的payload部分
	 */
	public static Claims getClaimFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(AuthConstant.SECRET)
				//在这个时间内不会报错，之外抛出ExpiredJwtException
				.setAllowedClockSkewSeconds(AuthConstant.REFRESH_SECOND - AuthConstant.EXPIRATION_SECOND)
				.parseClaimsJws(token)
				.getBody();
	}

	//public static Claims getClaimFromTokenIgnoreExpire(String token) {
	//	return Jwts.parser()
	//			.setSigningKey(AuthConstant.SECRET)
	//			//在这个时间内不会报错，之外抛出ExpiredJwtException
	//			.setAllowedClockSkewSeconds(Long.MAX_VALUE)
	//			.parseClaimsJws(token)
	//			.getBody();
	//}

	public static void parseToken(String token) throws JwtException {
		Jwts.parser().setSigningKey(AuthConstant.SECRET).parseClaimsJws(token).getBody();
	}

	/**
	 * 验证token是否失效
	 * true:过期   false:没过期
	 */
	public static Boolean isTokenExpired(String token) {
		try {
			final Date expiration = getExpirationDateFromToken(token);
			return expiration.before(new Date());
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	/**
	 * 验证token是否合法 包括过期 很多想象中不合法的token都合法，不建议使用
	 */
	@Deprecated
	public static Boolean isVerify(String token) {
		return Jwts.parser().isSigned(token);
	}

	/**
	 * 生成token(通过用户名和签名时候用的随机数)
	 */
	public static <T> String generateToken(T userId, String phone, Map<String, Object> claims) {
		return doGenerateToken(claims, userId.toString(), phone);
	}

	public static <T> String generateRefreshToken(T userId, String phone, String realmName) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(AuthConstant.CLAIMS_REALM_TYPE, realmName);
		claims.put(AuthConstant.REFRESH_CLAIM_FLAG, "");
		return doGenerateToken(claims, userId.toString(), phone);
	}

	public static <T> String generateToken(T userId, String phone, String realmName) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(AuthConstant.CLAIMS_REALM_TYPE, realmName);
		return doGenerateToken(claims, userId.toString(), phone);
	}

	/**
	 * 生成token
	 */
	private static String doGenerateToken(Map<String, Object> claims, String id, String subject) {
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + AuthConstant.EXPIRATION_SECOND * 1000);

		return Jwts.builder()
				/*
				 * payload setId、setSubject、setExpiration等实际上是放在claims的map里，而setClaims是赋值操作会覆盖Id，所以setClaims第一个设置
				 * */
				.setClaims(claims)
				/*
				 * 签发者
				 * */
				.setIssuer(AuthConstant.ISSUER)
				/*
				 * 签发时间
				 * */
				.setIssuedAt(createdDate)
				/*
				 * id
				 * */
				.setId(id)
				/*
				 * 主题
				 * */
				.setSubject(subject)
				/*
				 * 过期时间
				 * */
				.setExpiration(expirationDate)
				/*
				 * 签名算法以及密匙
				 * */
				.signWith(SignatureAlgorithm.HS512, AuthConstant.SECRET)
				.compact();
	}

	/**
	 * 获取混淆MD5签名用的随机字符串
	 */
	public static String getRandomKey() {
		return RandomUtil.randomString(6);
	}

}