package com.bonelf.common.core.aop.annotation.dict;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 数据库字典
 * 通常如“退款原因”这类 用户可能修改，而代码很少使用其值 的存到数据库字典，并搭配缓存使用
 * </p>
 * @author bonelf
 * @since 2020/10/11 17:44
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DbDict {
	/**
	 *  数据code
	 */
	String value();
	/**
	 * 是否使用定时的缓存
	 * （不会实时修改，但是能减小数据库服务压力，提高访问速度）
	 */
	boolean cached() default true;

	/**
	 * 在前端解析时返回对应枚举值 不设置代表不返回
	 */
	String nameSuffix() default "Name";
}
