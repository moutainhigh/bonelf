package com.bonelf.common.config.property;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "ali.oss")
public class OssProperty {
	/**
	 * id
	 */
	private String accessKeyId;
	/**
	 * 秘钥
	 */
	private String accessSecret;
	/**
	 * 节点
	 */
	private String endpoint;
	/**
	 * 库名
	 */
	private String bucketName;
	/**
	 * url
	 */
	private String bucketUrl;
}
