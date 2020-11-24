package com.bonelf.support.controller;

import com.bonelf.cicada.enums.EnumFactory;
import com.bonelf.common.constant.enums.VerifyCodeTypeEnum;
import com.bonelf.common.domain.Result;
import com.bonelf.support.domain.dto.VerifyCodeDTO;
import com.bonelf.support.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定时任务在线管理
 * @author bonelf
 * @date  2019-01-02
 */
@RestController
@RequestMapping("/sms")
@Slf4j
@Api(tags = "短信服务接口")
public class SmsController {
	@Autowired
	private SmsService smsService;


	@ApiOperation("验证码")
	@PostMapping(value = "/v1/sendVerify")
	public Result<String> sendVerify(@Validated @RequestBody VerifyCodeDTO accountLoginDto) {
		// FIXME: 2020/11/2 投入使用后删除此返回值 移动到Support服务
		return Result.ok(smsService.sendVerify(accountLoginDto.getPhone(), (VerifyCodeTypeEnum)EnumFactory.getByCode(accountLoginDto.getBusinessType(), VerifyCodeTypeEnum.class)));
	}
}
