package com.bonelf.auth.service;

import com.bonelf.auth.entity.User;
import com.bonelf.common.domain.Result;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 签权服务
 * </p>
 * @author bonelf
 * @since 2020/11/17 15:37
 */
@Service
public interface UserService {

    /**
     * 根据用户唯一标识获取用户信息
     * @param userId
     * @return
     */
	Result<User> getByUniqueId(Long userId);

	/**
	 * 注册
	 * @param phone
	 * @return
	 */
	User registerByPhone(String phone);

	/**
	 * 微信注册
	 * @param openId
	 * @param unionId
	 * @return
	 */
	Result<User> registerByOpenId(@NonNull String openId,@NonNull String unionId);
}
