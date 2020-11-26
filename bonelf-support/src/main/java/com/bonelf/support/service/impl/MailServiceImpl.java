package com.bonelf.support.service.impl;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailAccount;
import com.bonelf.common.config.property.BonelfProperty;
import com.bonelf.common.constant.BonelfConstant;
import com.bonelf.common.constant.enums.VerifyCodeTypeEnum;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.CommonBizExceptionEnum;
import com.bonelf.common.util.redis.RedisUtil;
import com.bonelf.support.constant.CacheConstant;
import com.bonelf.support.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private BonelfProperty bonelfProperty;

	@Override
	public String sendVerify(String mail, VerifyCodeTypeEnum bizType) {
		if (redisUtil.get(String.format(CacheConstant.LOGIN_VERIFY_CODE, bizType.getCode(), mail)) != null) {
			throw new BonelfException(CommonBizExceptionEnum.NO_REPEAT_SUBMIT, redisUtil.getExpire(String.format(CacheConstant.LOGIN_VERIFY_CODE, bizType.getCode(), mail)));
		}
		String code = RandomUtil.randomNumbers(6);
		//mailUtil.sendVerifyMail(phone, code);
		MailAccount account = new MailAccount();
		account.setHost(bonelfProperty.getMail().getSmtp());
		account.setAuth(true);
		account.setFrom(bonelfProperty.getMail().getUsername());
		account.setUser(bonelfProperty.getMail().getUsername());
		account.setPass(bonelfProperty.getMail().getPassword());
		cn.hutool.extra.mail.MailUtil.send(account, mail, bonelfProperty.getAppName(),
				BonelfConstant.VERIFY_HTML.replace("{CODE}", code).replace("{APPNAME}", bonelfProperty.getAppName()), true);
		redisUtil.set(String.format(CacheConstant.LOGIN_VERIFY_CODE, bizType.getCode(),  mail), code, CacheConstant.VERIFY_CODE_EXPIRED_SECOND);
		return code;
	}
}
