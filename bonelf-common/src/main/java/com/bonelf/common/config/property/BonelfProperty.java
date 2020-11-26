package com.bonelf.common.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "bonelf")
public class BonelfProperty {
	/**
	 * 项目网址
	 */
	private String baseUrl = "undefined";

	private String appName = "bonelf总结项目";

	private MailProperty mail = new MailProperty();
}
