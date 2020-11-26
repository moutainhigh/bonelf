/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.auth.core.oauth2.service;

import com.bonelf.auth.domain.entity.User;
import com.bonelf.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 手机验证码登录校验
 * @author bonelf
 */
@Slf4j
@Service("mailUserDetailsService")
public class MailUserDetailsService extends CustomUserDetailsService {

	@Autowired
	private UserService userService;

	/**
	 * 调用/auth/token 调用这个方法校验
	 * @param uniqueId mail
	 * @author bonelf
	 * @date 2020-11-19 11:21
	 */
	@Override
	public UserDetails loadUserByUsername(String uniqueId) {
		User user = userService.getByUniqueId(uniqueId).getResult();
		if (user == null) {
			// 验证码正确 但是用户不存在注册
			user = userService.registerByMail(uniqueId);
		}
		log.debug("load user by mail:{}", user.toString());
		// 如果为mail模式，从短信服务中获取验证码（动态密码） 其实可以去了加密
		//String credentials = userClient.getSmsCode(uniqueId, "LOGIN");
		//String credentials = passwordEncoder.encode(user.getVerifyCode());
		//String credentials = user.getVerifyCode();
		String credentials = new BCryptPasswordEncoder().encode(user.getVerifyCode());
		return new org.springframework.security.core.userdetails.User(
				user.getMail(),
				credentials,
				user.getEnabled(),
				user.getAccountNonExpired(),
				user.getCredentialsNonExpired(),
				user.getAccountNonLocked(),
				super.obtainGrantedAuthorities(user));
	}
}