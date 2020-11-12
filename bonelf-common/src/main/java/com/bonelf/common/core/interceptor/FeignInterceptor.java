package com.bonelf.common.core.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局拦截器
 **/
@Slf4j
public class FeignInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		return true;
		//String secretKey = request.getHeader(AuthFeignConstant.AUTH_HEADER);
		//if (secretKey != null) {
		//	//log.info("通过网关访问服务：{}，secretKey：{}", request.getRequestURI(), secretKey);
		//	return true;
		//}
		//response.setContentType("application/json; charset=utf-8");
		//PrintWriter writer = response.getWriter();
		//log.warn("未通过网关访问服务：{}", request.getRequestURI());
		//writer.write(JSON.toJSONString(Result.<String>error("please request the api through the gateway"), SerializerFeature.PrettyFormat));
		//return false;
	}
}

