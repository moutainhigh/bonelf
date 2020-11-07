package com.bonelf.common.core.aop.annotation.dict;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 子属性是否支持字典扩展；主要为了提高效率，从而没必要判断所有field
 * </p>
 * @author bonelf
 * @since 2020/10/11 17:45
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DictField {
}
