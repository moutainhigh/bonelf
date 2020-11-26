package com.bonelf.common.cloud.feign;

import cn.hutool.core.util.StrUtil;
import com.gateway.constant.AuthFeignConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Configuration
public class FeignConfig implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			String header = request.getHeader(AuthFeignConstant.AUTH_HEADER);

			log.debug(" Authorization-UserName :" + header);
			requestTemplate.header(AuthFeignConstant.AUTH_HEADER, AuthFeignConstant.FEIGN_REQ_FLAG_PREFIX + (StrUtil.isEmpty(header) ? "" : " ") + header);
		}
	}
}
