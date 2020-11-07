package com.bonelf.orderservice.service.impl;

import com.bonelf.cicada.enums.EnumFactory;
import com.bonelf.common.core.websocket.SocketMessage;
import com.bonelf.common.core.websocket.SocketRespMessage;
import com.bonelf.common.core.websocket.constant.MessageRecvCmdEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * 使用redis订阅来接收消息并发布，SpringBootWebsocket写法
 **/
@Component
@Slf4j
public class SocketMessageService implements MessageListener {
	//通过feign请求发送消息
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void onMessage(Message respMsg, @Nullable byte[] bytes) {
		//SocketRespMessage message = JSON.parseObject(respMsg.toString(), SocketRespMessage.class); //此代码也可反序列化成功
		SocketRespMessage message = (SocketRespMessage)redisTemplate.getValueSerializer().deserialize(respMsg.getBody());
		log.info("接收到消息 {}", message);
		SocketMessage<?> socketMessage = message.getSocketMessage();
		MessageRecvCmdEnum cmdEnum = EnumFactory.getByCode(socketMessage.getCmdId(), MessageRecvCmdEnum.class);
		switch (cmdEnum) {
			case PING_PONG:
				SocketRespMessage socketRespMessage = SocketRespMessage.builder()
						.fromUid(message.getFromUid())
						.socketMessage(socketMessage.buildMsg())
						.build();
				//supportFeignClient.sendMessage(socketRespMessage);
				break;
			default:
				//pass
		}

	}
}