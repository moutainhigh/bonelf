package com.bonelf.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.bonelf.auth.client.provider.UserClient;
import com.bonelf.auth.domain.entity.User;
import com.bonelf.auth.domain.request.RegisterUserRequest;
import com.bonelf.auth.domain.response.UserResponse;
import com.bonelf.auth.service.UserService;
import com.bonelf.common.constant.enums.YesOrNotEnum;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.CommonBizExceptionEnum;
import com.bonelf.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
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
	public User getByUniqueId(String uniqueId) {
		Result<UserResponse> user = userClient.getUserByUniqueId(uniqueId);
		if (!user.getSuccess()) {
			throw new BonelfException(CommonBizExceptionEnum.BUSY);
		}
		User userResult = getUserFromUserResp(user);
		return userResult;
	}

	@Override
	public User registerByPhone(String phone) {
		Result<UserResponse> userResp = userClient.registerByPhone(phone);
		if (!userResp.getSuccess() || userResp.getResult() == null) {
			throw new UsernameNotFoundException("register fail");
		}
		User userResult = getUserFromUserResp(userResp);
		return userResult;
	}

	/**
	 * 封装用户
	 * @param userResp
	 * @return
	 */
	private User getUserFromUserResp(Result<UserResponse> userResp) {
		User userResult = new User();
		BeanUtil.copyProperties(userResp.getResult(), userResult);
		userResult.setEnabled(YesOrNotEnum.N.getCode().equals(userResp.getResult().getStatus()));
		userResult.setAccountNonExpired(true);
		userResult.setCredentialsNonExpired(true);
		userResult.setAccountNonLocked(YesOrNotEnum.N.getCode().equals(userResp.getResult().getStatus()));
		return userResult;
	}

	@Override
	public User registerByMail(String mail) {
		Result<UserResponse> userResp = userClient.registerByMail(mail);
		if (!userResp.getSuccess() || userResp.getResult() == null) {
			throw new UsernameNotFoundException("register fail");
		}
		User userResult = getUserFromUserResp(userResp);
		return userResult;
	}

	@Override
	public User registerByOpenId(RegisterUserRequest registerUser) {
		Result<UserResponse> userResp = userClient.registerByOpenId(registerUser);
		if (userResp.getSuccess()) {
			User userResult = getUserFromUserResp(userResp);
			return userResult;
		} else {
			//注册失败
			throw new InvalidGrantException("注册失败");
		}
	}
}
