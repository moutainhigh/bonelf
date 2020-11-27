/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.common.core.serializer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 序列化消息，将 指定规则字符串 转为 指定字符串
 * \@StrReplace(from = "[^\\u0000-\\uFFFF]", to = " ") :禁用emoji表情
 * 需搭配 @JsonDeserialize(using = StrReplaceDeserializer.class)使用
 * @author bonelf
 * @date 2020-11-27 09:12:00
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
//这样没效果
//@JsonDeserialize(using = StrReplaceDeserializer.class)
public @interface StrReplace {
	/**
	 * from regex
	 * @return
	 */
	String from();

	/**
	 * to string
	 * @return
	 */
	String to() default "";
}
