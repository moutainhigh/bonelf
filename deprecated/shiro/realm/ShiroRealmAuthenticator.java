package com.bonelf.common.core.shiro.realm;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bonelf.common.core.shiro.constant.AuthExceptionMsgConstant;
import com.bonelf.common.core.shiro.token.JwtAuthToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 认证realm选取
 */
@Slf4j
public class ShiroRealmAuthenticator extends ModularRealmAuthenticator {

	/**
	 * 根据用户类型判断使用哪个Realm
	 */
	@Override
	protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		super.assertRealmsConfigured();
		// 强制转换回自定义的CustomizedToken
		JwtAuthToken token = (JwtAuthToken)authenticationToken;
		// 登录类型
		String realmType = token.getRealmType().getRealmName();
		// 所有Realm
		Collection<Realm> realms = this.getRealms();
		// 登录类型对应的所有Realm
		Collection<Realm> typeRealms = new ArrayList<>();
		for (Realm realm : realms) {
			//根据登录类型和Realm的名称进行匹配区分
			if (realm.getName().trim().toLowerCase().contains(realmType.trim().toLowerCase())) {
				typeRealms.add(realm);
			}
		}

		// 判断是单Realm还是多Realm，有多个Realm就会使用所有配置的Realm。 只有一个的时候，就直接使用当前的Realm。
		if (typeRealms.size() == 1) {
			log.info("doSingleRealmAuthentication[{}] execute ", CollectionUtil.join(typeRealms, StrUtil.COMMA));
			return doSingleRealmAuthentication(typeRealms.iterator().next(), token);
		} else if (typeRealms.size() > 1) {
			log.info("doMultiRealmAuthentication[{}] execute ", CollectionUtil.join(typeRealms, StrUtil.COMMA));
			return doMultiRealmAuthentication(typeRealms, token);
		} else {
			throw new AuthenticationException(AuthExceptionMsgConstant.NO_REALM_TYPE_FIT);
		}
	}
}
