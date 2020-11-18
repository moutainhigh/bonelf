package com.bonelf.auth.service.impl;

import com.bonelf.auth.client.provider.OrganizationProvider;
import com.bonelf.auth.entity.User;
import com.bonelf.auth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private OrganizationProvider organizationProvider;

    @Override
    public User getByUniqueId(Long uniqueId) {
        return organizationProvider.getUserByUniqueId(uniqueId).getResult();
    }
}
