package com.bonelf.common.core.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 用于GateWay服务过滤SpringMVC配置的标志（与SpringMVC不兼容）
 * see GateWayApplication (~不同项目使用doc注释会报红，但是能ctrl跳转)
 * 使用：
 * 1.@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebMvcConfig.class,String2DateConfig.class})
 * 2.@SpringBootApplication(exclude = {String2DateConfig.class,WebMvcConfig.class})
 * 报错了
 * 所以写了这个注解，报错原因 (java.lang.ArrayStoreException: sun.reflect.annotation.TypeNotPresentExceptionProxy 内容大概是 在class path下找不到需要依赖的注解)
 * 猜想：因为我们对spring-boot-starter-web进行了exclusion 所以当在GatewayApplication中直接应用这两个与之相关的config会找不到class而报错，而间接使用注解则可行
 * 要用公共配置难免会碰到不兼容的问题.....((/- -)/
 * 总之跟期待能使用FilterType.ASSIGNABLE_TYPE的方式排除spring-boot-starter-web的配置
 *
 * 已通过gateway不依赖common解决
 * </p>
 * @author bonelf
 * @since 2020/10/11 22:46
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface MvcConfig {
}
