package com.bonelf.userservice.domain.dto;

import com.bonelf.common.constant.RegexpConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@ApiModel("验证码发送")
public class VerifyCodeDTO {
	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	@Pattern(regexp = RegexpConstant.VALIDATE_PHONE, message = "手机号格式不正确")
	private String phone;
}
