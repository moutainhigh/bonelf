package com.bonelf.auth.client.provider;

import com.bonelf.auth.entity.Role;
import com.bonelf.auth.entity.User;
import com.bonelf.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * <p>
 * 签权服务
 * </p>
 * @author Chenyuan
 * @since 2020/11/17 15:37
 */
@FeignClient(name = "organization", fallback = OrganizationProviderFallback.class)
public interface OrganizationProvider {

    @GetMapping(value = "/user")
    Result<User> getUserByUniqueId(@RequestParam("uniqueId") Long uniqueId);

    @GetMapping(value = "/role/user/{userId}")
    Result<Set<Role>> queryRolesByUserId(@PathVariable("userId") Long userId);
}
