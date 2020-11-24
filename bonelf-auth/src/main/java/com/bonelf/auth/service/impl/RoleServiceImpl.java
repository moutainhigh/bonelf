package com.bonelf.auth.service.impl;

import com.bonelf.auth.client.provider.UserClient;
import com.bonelf.auth.entity.Role;
import com.bonelf.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private UserClient organizationProvider;

    @Override
    public Set<Role> queryUserRolesByUserId(Long userId) {
        return organizationProvider.queryRolesByUserId(userId).getResult();
    }

}
