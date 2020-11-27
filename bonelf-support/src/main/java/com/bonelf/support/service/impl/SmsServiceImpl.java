package com.bonelf.support.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.bonelf.common.constant.enums.VerifyCodeTypeEnum;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.CommonBizExceptionEnum;
import com.bonelf.common.util.SmsUtil;
import com.bonelf.common.util.redis.RedisUtil;
import com.bonelf.support.constant.CacheConstant;
import com.bonelf.support.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private SmsUtil smsUtil;

	@Override
	public String sendVerify(String phone, VerifyCodeTypeEnum bizType) {
		if (redisUtil.get(String.format(CacheConstant.VERIFY_CODE, bizType.getCode(), phone)) != null) {
			throw new BonelfException(CommonBizExceptionEnum.NO_REPEAT_SUBMIT, redisUtil.getExpire(String.format(CacheConstant.VERIFY_CODE, bizType.getCode(), phone)));
		}
		String code = RandomUtil.randomNumbers(6);
		//smsUtil.sendVerify(phone, code);
		redisUtil.set(String.format(CacheConstant.VERIFY_CODE, bizType.getCode(),  phone), code, CacheConstant.VERIFY_CODE_EXPIRED_SECOND);
		return code;
	}
}
