package com.bonelf.support.domain.dto;

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

	/**
	 * 邮箱
	 */
	@ApiModelProperty("邮箱")
	@Pattern(regexp = RegexpConstant.VALIDATE_MAIL, message = "邮箱格式不正确")
	private String mail;

	/**
	 * 验证码类型 mail image phone
	 * @see com.bonelf.common.constant.enums.VerifyCodeTypeEnum
	 */
	@ApiModelProperty("验证码类型")
	private String businessType;
}
