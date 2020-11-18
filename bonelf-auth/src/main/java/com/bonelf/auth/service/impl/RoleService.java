package com.bonelf.auth.service.impl;

import com.bonelf.auth.client.provider.OrganizationProvider;
import com.bonelf.auth.entity.Role;
import com.bonelf.auth.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private OrganizationProvider organizationProvider;

    @Override
    public Set<Role> queryUserRolesByUserId(Long userId) {
        return organizationProvider.queryRolesByUserId(userId).getResult();
    }

}
