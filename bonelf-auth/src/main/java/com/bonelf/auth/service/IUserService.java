package com.bonelf.auth.service;

import com.bonelf.auth.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 签权服务
 * </p>
 * @author Chenyuan
 * @since 2020/11/17 15:37
 */
@Service
public interface IUserService {

    /**
     * 根据用户唯一标识获取用户信息
     *
     * @param uniqueId
     * @return
     */
    @Cacheable(value = "#id")
    User getByUniqueId(Long uniqueId);
}
