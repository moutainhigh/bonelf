package com.bonelf.auth.service;

import com.bonelf.auth.entity.Role;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 签权服务
 * </p>
 * @author Chenyuan
 * @since 2020/11/17 15:37
 */
@Service
public interface IRoleService {

    Set<Role> queryUserRolesByUserId(Long userId);

}
