package com.bonelf.common.core.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接收消息后发送到redis的实体
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocketRespMessage {
	/**
	 * 用户id 一对一消息
	 */
	private String fromUid;

	/**
	 * socketMessage 都是JSONObject，在socketMessageService再转为需要的 取? 反正fastjson序列化时找不到对象使用的就是JSONObject
	 */
	private SocketMessage<?> socketMessage;
}
