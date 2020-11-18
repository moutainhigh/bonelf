package com.bonelf.auth.client.provider;

import com.bonelf.auth.entity.Role;
import com.bonelf.auth.entity.User;
import com.bonelf.common.domain.Result;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 签权服务
 * </p>
 * @author Chenyuan
 * @since 2020/11/17 15:37
 */
@Component
public class OrganizationProviderFallback implements OrganizationProvider {

    @Override
    public Result<User> getUserByUniqueId(Long uniqueId) {
        return Result.ok(new User());
    }

    @Override
    public Result<Set<Role>> queryRolesByUserId(Long userId) {
        return Result.ok(new HashSet<Role>());
    }
}
