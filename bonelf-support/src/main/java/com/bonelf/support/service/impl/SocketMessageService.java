package com.bonelf.support.service.impl;

import com.alibaba.fastjson.JSON;
import com.bonelf.common.core.websocket.SocketMessage;
import com.bonelf.support.core.websocket.WebsocketMap;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 使用redis订阅来接收消息并发布，SpringBootWebsocket写法
 **/
@Slf4j
@Service
public class SocketMessageService {
	public static final int SEND_OK = 0;
	public static final int SEND_FAIL = -1;
	@Autowired
	private WebsocketMap websocketMap;

	/**
	 * 发送消息
	 * @param userId 对象
	 * @param message
	 */
	public int sendMessage(String userId, SocketMessage message) {
		return sendMessage(userId, JSON.toJSONString(message));
	}

	/**
	 * 发送消息
	 * @param userIds 对象
	 * @param message
	 */
	public List<String> sendMessage(Collection<String> userIds, SocketMessage message) {
		List<String> failIds = new ArrayList<>();
		for (String userId : userIds) {
			if(SEND_FAIL == sendMessage(userId, JSON.toJSONString(message))){
				failIds.add(userId);
			}
		}
		return failIds;
	}

	/**
	 * 对所有人发送消息
	 * @param message
	 */
	public void sendAllMessage(SocketMessage message) {
		websocketMap.getSocketSessionMap().forEach((key, value) -> {
			if (value != null && !value.isOpen()) {
				value.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
			}
		});
		//websocketMap.getChannelGroup().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
	}

	private int sendMessage(String userId, String message) {
		Channel session = websocketMap.getSocketSessionMap().get(userId);
		//Channel session = websocketMap.getChannelGroup().find(userId);
		if (session != null && !session.isOpen()) {
			session.writeAndFlush(new TextWebSocketFrame(message));
			return SocketMessageService.SEND_OK;
		} else {
			return SocketMessageService.SEND_FAIL;
		}
	}
}