package com.bonelf.common.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.bonelf.common.config.property.AliSmsProperty;
import com.bonelf.common.constant.AliSmsTemplateCode;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.BizExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 发送验证码工具
 * @author chuanfu
 * @version v1.0
 * @date 2019-10-10
 */
@Component
public class SmsUtil {
	@Autowired
	private AliSmsProperty smsProperty;

	/**
	 * 短信发送
	 * @param telephone 发送号码
	 * @param code 验证码
	 * @author chuanfu
	 * @date 2019-3-23
	 */
	public Integer sendVerify(String telephone, String code) {
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsProperty.getAccessKeyId(), smsProperty.getAccessSecret());
		IAcsClient client = new DefaultAcsClient(profile);
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain("dysmsapi.aliyuncs.com");
		request.setSysVersion("2017-05-25");
		request.setSysAction("SendSms");
		request.putQueryParameter("RegionId", "cn-hangzhou");
		request.putQueryParameter("PhoneNumbers", telephone);
		request.putQueryParameter("SignName", smsProperty.getSignName());
		request.putQueryParameter("TemplateCode", AliSmsTemplateCode.VERIFY);
		request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
		CommonResponse response = null;
		try {
			response = client.getCommonResponse(request);
		} catch (ClientException e) {
			e.printStackTrace();
			throw new BonelfException(BizExceptionEnum.THIRD_FAIL, e.getErrCode());
		}
		return response.getHttpStatus();

	}
}
