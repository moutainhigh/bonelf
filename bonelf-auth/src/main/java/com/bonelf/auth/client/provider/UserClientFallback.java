package com.bonelf.auth.client.provider;

import com.bonelf.auth.domain.entity.Role;
import com.bonelf.auth.domain.request.RegisterUserRequest;
import com.bonelf.auth.domain.response.UserResponse;
import com.bonelf.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 签权服务
 * </p>
 * @author bonelf
 * @since 2020/11/17 15:37
 */
public class UserClientFallback implements UserClient {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public Result<UserResponse> getUserByUniqueId(String uniqueId) {
		// FIXME: 2020/11/19 超时报错返回error
		//User user = new User();
		//user.setEnabled(true);
		//user.setAccountNonExpired(true);
		//user.setAccountNonLocked(true);
		//user.setCredentialsNonExpired(true);
		//user.setMobile("13758233010");
		//user.setNickname("nickname");
		//user.setPassword("123456");
		//user.setUsername("13758233010");
		//user.setOpenId("test-open-id");
		//user.setVerifyCode("980826");
		//user.setUserId(-1L);
		//return Result.ok(user);
        return Result.error();
	}

	@Override
	public Result<Set<Role>> queryRolesByUserId(Long userId) {
		// FIXME: 2020/11/19 超时报错返回error
		return Result.ok(new HashSet<Role>());
	}

	@Override
	public Result<UserResponse> registerByPhone(String phone) {
		return Result.error();
	}

	@Override
	public Result<UserResponse> registerByMail(String mail) {
		return Result.error();
	}

	@Override
	public Result<UserResponse> registerByOpenId(RegisterUserRequest registerUser) {
		return Result.error();
	}
}
