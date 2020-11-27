/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.common.core.validator.annotation;

import com.bonelf.common.core.validator.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * <p>
 * DTO枚举校验注解
 * 在controller上加@Validated 生效，或者加载DTO上（需要能获得枚举）
 * </p>
 * @author bonelf
 * @since 2020/7/9 9:20
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(EnumValid.List.class)
@Constraint(validatedBy = {EnumValidator.class})
public @interface EnumValid {
	/**
	 * valid的参数
	 */
	Class<?>[] groups() default {};

	/**
	 * valid的调用参数
	 */
	Class<? extends Payload>[] payload() default {};

	/**
	 * 异常信息
	 */
	String message() default "{*.validation.constraint.Enum.message}";

	/**
	 * 能否为空
	 */
	boolean nullable() default false;

	/**
	 * 枚举类
	 */
	Class<?> clazz();

	/**
	 * 枚举唯一的code
	 */
	String method() default "getCode";

	/**
	 * Defines several {@link Enum} annotations on the same element.
	 * @see Enum
	 */
	@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
	@Retention(RUNTIME)
	@interface List {
		EnumValid[] value();
	}
}

