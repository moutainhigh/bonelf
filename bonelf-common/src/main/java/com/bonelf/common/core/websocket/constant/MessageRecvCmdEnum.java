package com.bonelf.common.core.websocket.constant;

import com.bonelf.cicada.enums.CodeEnum;
import lombok.Getter;

@Getter
public enum MessageRecvCmdEnum implements CodeEnum {
	/**
	 * 用户连接提示
	 */
	INIT_CALLBACK(0, "连接成功提示", ChannelEnum.TEST),

	/**
	 * 心跳测试
	 */
	PING_PONG(1, "心跳测试", ChannelEnum.ORDER),

	/**
	 * 普通测试
	 */
	TEST(2, "普通测试", ChannelEnum.TEST),
	;
	private Integer code;
	private String value;
	/**
	 * 发送的频道（服务） Tips：ChannelEnum.values()全部
	 */
	private ChannelEnum[] channel;

	MessageRecvCmdEnum(Integer code, String desc, ChannelEnum... channel) {
		this.code = code;
		this.value = desc;
		this.channel = channel;
	}
}
