package com.bonelf.userservice.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.bonelf.common.constant.RegexpConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@ApiModel("微信登录")
public class AccountLoginDTO {
	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	@Pattern(regexp = RegexpConstant.VALIDATE_PHONE, message = "手机号格式不正确")
	private String username;

	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "加密密码", example = "7dc28b0f08849a44")
	//@Pattern(regexp = RegexpConstant.NUMBERS_AND_LETTERS, message = "手机号格式不正确")
	private String password;

	/**
	 * 登录验证码
	 */
	@ApiModelProperty(value = "加密密码", name = "verify_code")
	@JSONField(name = "verify_code", serialize = false)
	@TableField(exist = false)
	private String verifyCode;
}
