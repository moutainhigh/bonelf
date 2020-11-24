package com.bonelf.common.core.aop.annotation;

import java.lang.annotation.*;

/**
 * 必须是Feign请求的注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MustFeignRequest {
}
