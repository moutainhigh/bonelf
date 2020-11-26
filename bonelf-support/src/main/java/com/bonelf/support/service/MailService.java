package com.bonelf.support.service;

import com.bonelf.common.constant.enums.VerifyCodeTypeEnum;

public interface MailService {
	/**
	 * 发送验证码
	 * @param username
	 * @param bizType
	 * @return
	 */
	String sendVerify(String username, VerifyCodeTypeEnum bizType);
}
