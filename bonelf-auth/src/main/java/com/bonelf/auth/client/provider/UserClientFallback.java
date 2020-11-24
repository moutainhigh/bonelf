package com.bonelf.auth.client.provider;

import com.bonelf.auth.entity.Role;
import com.bonelf.auth.entity.User;
import com.bonelf.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 签权服务
 * </p>
 * @author bonelf
 * @since 2020/11/17 15:37
 */
@Component
public class UserClientFallback implements UserClient {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public Result<User> getUserByUniqueId(Long uniqueId) {
		// FIXME: 2020/11/19 超时报错返回error
		User user = new User();
		user.setEnabled(true);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setMobile("13758233010");
		user.setNickname("nickname");
		user.setPassword("123456");
		user.setUsername("13758233010");
		user.setOpenId("test-open-id");
		user.setVerifyCode("980826");
		user.setUserId(-1L);
		return Result.ok(user);
        //return Result.error();
	}

	@Override
	public Result<Set<Role>> queryRolesByUserId(Long userId) {
		// FIXME: 2020/11/19 超时报错返回error
		return Result.ok(new HashSet<Role>());
	}

	@Override
	public Result<User> registerByPhone(String phone) {
		return Result.error();
	}

	@Override
	public Result<User> registerByOpenId(@NonNull String openId,@NonNull String unionId) {
		return Result.error();
	}
}
