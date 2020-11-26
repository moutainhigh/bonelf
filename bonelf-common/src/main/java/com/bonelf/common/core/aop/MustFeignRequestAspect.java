package com.bonelf.common.core.aop;

import com.bonelf.common.core.aop.annotation.MustFeignRequest;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.CommonBizExceptionEnum;
import com.gateway.constant.AuthFeignConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 必须是Feign请求处理 的切面
 * @author bonelf
 **/
@Aspect
@Component
@Slf4j
public class MustFeignRequestAspect {

	@Autowired
	private HttpServletRequest request;

	@Pointcut("@annotation(mustFeignRequest)")
	public void pointCut(MustFeignRequest mustFeignRequest) {
	}

	@Around(value = "pointCut(mustFeignRequest)", argNames = "point,mustFeignRequest")
	public Object around(ProceedingJoinPoint point, MustFeignRequest mustFeignRequest) throws Throwable {
		String head = request.getHeader(AuthFeignConstant.AUTH_HEADER);
		if (head == null || !head.startsWith(AuthFeignConstant.FEIGN_REQ_FLAG_PREFIX)) {
			throw new BonelfException(CommonBizExceptionEnum.REQ_NOT_FEIGN);
		}
		return point.proceed();
	}

}
