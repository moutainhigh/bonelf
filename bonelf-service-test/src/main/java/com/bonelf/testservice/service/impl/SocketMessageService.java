package com.bonelf.testservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonelf.cicada.enums.EnumFactory;
import com.bonelf.common.client.SupportFeignClient;
import com.bonelf.common.config.handler.FastJson2JsonRedisSerializer;
import com.bonelf.common.core.websocket.SocketMessage;
import com.bonelf.common.core.websocket.SocketRespMessage;
import com.bonelf.common.core.websocket.constant.MessageRecvCmdEnum;
import com.bonelf.common.util.SocketUtil;
import com.bonelf.testservice.core.websocket.WebsocketMap;
import com.bonelf.testservice.domain.dto.TestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collection;

/**
 * 使用redis订阅来接收消息并发布，SpringBootWebsocket写法
 **/
@Component
@Slf4j
public class SocketMessageService implements MessageListener {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private SupportFeignClient supportFeignClient;
	@Autowired
	private WebsocketMap websocketMap;

	/**
	 * 网上都是传递String 但是我发现如果传递对象接受也能使用JSON.parseObject解析，并且toString方法和在redis存对象时内容一样
	 * 所以我试着使用redis里的deserialize方法解析body 的 byte。非常成功。
	 * 本项目走的序列化为自定义的FastJson{@link FastJson2JsonRedisSerializer}
	 * 注意开启 ParserConfig.getGlobalInstance().setAutoTypeSupport(true); {@link com.bonelf.common.config.WebMvcConfig#fastJsonHttpMessageConverter()}
	 * MessageListenerAdapter 的实现是使用的RedisSerializer的deserialize body数据 可以尝试
	 * {@link org.springframework.data.redis.listener.adapter.MessageListenerAdapter#extractMessage(Message)}
	 * @param respMsg class:DefaultMessage body 传递的数据
	 * @param bytes pattern（convertedChannel）可以使用stringSerializer.deserialize 查看内容
	 */
	@Override
	public void onMessage(Message respMsg, @Nullable byte[] bytes) {
		//SocketRespMessage message = JSON.parseObject(respMsg.toString(), SocketRespMessage.class); //此代码也可反序列化成功
		SocketRespMessage message = (SocketRespMessage)redisTemplate.getValueSerializer().deserialize(respMsg.getBody());
		log.info("接收到消息 {}", message);
		if (message == null) {
			log.warn("can't deserialize message or deserialize fail:{}", respMsg.toString());
			return;
		}
		SocketMessage<?> socketMessage = message.getSocketMessage();
		MessageRecvCmdEnum cmdEnum = EnumFactory.getByCode(socketMessage.getCmdId(), MessageRecvCmdEnum.class);
		JSONObject data = (JSONObject)socketMessage.getData();
		switch (cmdEnum) {
			case PING_PONG:
				SocketRespMessage socketRespMessage = SocketRespMessage.builder()
						.fromUid(message.getFromUid())
						.socketMessage(socketMessage.buildMsg())
						.build();
				supportFeignClient.sendMessage(socketRespMessage);
				break;
			case TEST:
				TestDTO testDto = SocketUtil.parseSocketData(data, TestDTO.class);
				log.debug("Parse Data:" + JSON.toJSONString(testDto));
				break;
			default:
				//pass
		}
		// Temp Code For restore
		//String[] userIds = socketMessage.getUserIds().split(StrUtil.COMMA);
		//for (String userId : userIds) {
		//	SocketRespMessage socketRespMessage = SocketRespMessage.builder()
		//			.fromUid(userId)
		//			.socketMessage(socketMessage).build();
		//	supportFeignClient.sendMessage(socketRespMessage);
		//}
	}

	/**
	 * 发送消息
	 * @param userId 对象
	 * @param message
	 */
	public void sendMessage(String userId, SocketMessage<?> message) {
		sendMessage(userId, JSON.toJSONString(message));
	}

	/**
	 * 发送消息
	 * @param userIds 对象
	 * @param message
	 */
	public void sendMessage(Collection<String> userIds, SocketMessage<?> message) {
		for (String userId : userIds) {
			sendMessage(userId, JSON.toJSONString(message));
		}
	}

	/**
	 * 对所有人发送消息
	 * @param message
	 */
	public void sendAllMessage(SocketMessage<?> message) {
		websocketMap.getSocketSessionMap().forEach((key, value) -> {
			try {
				value.sendMessage(new TextMessage(JSON.toJSONString(message)));
			} catch (IOException e) {
				log.error("消息发送失败");
				e.printStackTrace();
			}
		});
	}

	private void sendMessage(String userId, String message) {
		WebSocketSession session = websocketMap.getSocketSessionMap().get(userId);
		if (session != null && session.isOpen()) {
			try {
				session.sendMessage(new TextMessage(message));
			} catch (IOException e) {
				log.error("消息发送失败");
				e.printStackTrace();
			}
		}
	}
}