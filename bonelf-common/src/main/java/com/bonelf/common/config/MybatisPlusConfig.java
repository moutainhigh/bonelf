package com.bonelf.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.bonelf.common.config.handler.CustomMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置
 * mybatisplus配置动态数据源时，切记需要关闭自带的自动数据源配置 exclude = DruidDataSourceAutoConfigure.class
 * @author bonelf
 */
@Configuration
@MapperScan(value={"com.bonelf.**.mapper*"})
public class MybatisPlusConfig {

	/**
	 * 自定义公共字段自动注入
	 */
	@ConditionalOnMissingBean
	@Bean
	public MetaObjectHandler metaObjectHandler() {
		return new CustomMetaObjectHandler();
	}
}
