package com.bonelf.support.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ws")
public class WsProperty {
	/**
	 * port
	 */
	private Integer port = 8026;
	/**
	 * websocket的path
	 */
	private String contextPath = "/bonelf";
	/**
	 * 子协议
	 */
	private String subprotocols = "WebSocket";
	/**
	 * 是否允许扩展 默认false
	 */
	private Boolean allowExtensions = true;
	/**
	 * 数据荷载最大长度默认65536
	 */
	private Integer maxFrameSize = 65536 * 10;
	/**
	 * 标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
	 */
	private Integer soBackLog = 1024;

	private Ssl ssl = new Ssl();

	@Data
	public static class Ssl {
		/**
		 * ssl证书类型
		 */
		private String type = "JKS";
		/**
		 * ssl证书路径
		 */
		private String path;
		/**
		 * ssl证书密码
		 */
		private String password = "";
	}

}
