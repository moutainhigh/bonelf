package com.bonelf.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bonelf.userservice.domain.dto.AccountLoginDTO;
import com.bonelf.userservice.domain.dto.WechatLoginDTO;
import com.bonelf.userservice.domain.entity.User;
import com.bonelf.userservice.domain.vo.LoginVO;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.Map;
import java.util.Set;

public interface UserService extends IService<User> {
	/**
	 * 权限
	 * @param userId
	 * @return
	 */
	Map<String, Set<String>> getApiUserRolesAndPermission(Long userId);

	/**
	 * 微信登录
	 * @return
	 */
	LoginVO login(WechatLoginDTO wechatLoginDto) throws WxErrorException;

	/**
	 * 账号密码的登录
	 * @return
	 */
	LoginVO loginByAccount(AccountLoginDTO dto);
	/**
	 * 发送验证码
	 * @param username
	 * @return
	 */
	String sendVerify(String username);
}
