package com.bonelf.common.core.aop.annotation.dict;

import com.bonelf.cicada.enums.CodeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 枚举字典
 * 通常如“订单状态”这类 用户不关心，而代码经常使用其值 的存到枚举字典
 * </p>
 * @author bonelf
 * @since 2020/10/11 17:45
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumDict {
	/**
	 *  枚举类
	 */
	Class<? extends CodeEnum> value();
	/**
	 * 在前端解析时返回对应枚举值 不设置代表不返回
	 */
	String nameSuffix() default "Name";
}
