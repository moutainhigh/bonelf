package com.bonelf.auth.service.impl;

import com.bonelf.auth.client.provider.UserClient;
import com.bonelf.auth.entity.User;
import com.bonelf.auth.service.UserService;
import com.bonelf.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserClient userClient;

	/**
	 * 根据用户唯一标识获取用户信息
	 * FIXME 此处添加缓存
	 * @param userId userId
	 * @return
	 */
	@Override
	@Cacheable(value = "#id", condition = "#result.getSuccess()")
	public Result<User> getByUniqueId(Long userId) {
		return userClient.getUserByUniqueId(userId);
	}

	@Override
	public User registerByPhone(String phone) {
		Result<User> userResult = userClient.registerByPhone(phone);
		if (!userResult.getSuccess() || userResult.getResult() == null) {
			throw new UsernameNotFoundException("register fail");
		}
		return userResult.getResult();
	}

	@Override
	public Result<User> registerByOpenId(@NonNull String openId, @NonNull String unionId) {
		return userClient.registerByOpenId(openId, unionId);
	}
}
