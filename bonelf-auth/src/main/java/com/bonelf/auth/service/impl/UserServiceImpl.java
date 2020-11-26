package com.bonelf.auth.service.impl;

import com.bonelf.auth.client.provider.UserClient;
import com.bonelf.auth.domain.entity.User;
import com.bonelf.auth.domain.request.RegisterUserRequest;
import com.bonelf.auth.service.UserService;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.CommonBizExceptionEnum;
import com.bonelf.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserClient userClient;

	/**
	 * 根据用户唯一标识获取用户信息
	 * FIXME 此处添加缓存
	 * @param uniqueId uniqueId
	 * @return
	 */
	@Override
	//@Cacheable(value = "#id", condition = "#result.getSuccess()")
	public Result<User> getByUniqueId(String uniqueId) {
		Result<User> user = userClient.getUserByUniqueId(uniqueId);
		if (!user.getSuccess()) {
			throw new BonelfException(CommonBizExceptionEnum.BUSY);
		}
		return user;
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
	public User registerByMail(String mail) {
		Result<User> userResult = userClient.registerByMail(mail);
		if (!userResult.getSuccess() || userResult.getResult() == null) {
			throw new UsernameNotFoundException("register fail");
		}
		return userResult.getResult();
	}

	@Override
	public Result<User> registerByOpenId(RegisterUserRequest registerUser) {
		return userClient.registerByOpenId(registerUser);
	}
}
