package com.bonelf.gateway.core.shiro.realm;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Collection;
import java.util.Set;

/**
 * 认证方法
 */
public class CustomerRealmAuthorizer extends ModularRealmAuthorizer {
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        assertRealmsConfigured();
        // 所有Realm
        Collection<Realm> realms = this.getRealms();

        Set<String> realmNames = principals.getRealmNames();
        boolean isPermit = true;
        if (CollectionUtil.isNotEmpty(realmNames)) {
            for (String name : realmNames) {
                String realmName = ConvertUtils.convert(name);
                AuthorizingRealm realm = (AuthorizingRealm)CollectionUtil.findOne(realms, item -> realmName.trim().toLowerCase().equals(item.getName().trim().toLowerCase()));
                isPermit &= realm.isPermitted(principals, permission);
            }
        } else {
            isPermit = false;
        }
        return isPermit;
    }
}
