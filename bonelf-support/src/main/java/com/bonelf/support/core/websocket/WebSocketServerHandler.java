package com.bonelf.support.core.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.bonelf.common.constant.AuthConstant;
import com.bonelf.common.core.websocket.SocketMessage;
import com.bonelf.common.core.websocket.SocketRespMessage;
import com.bonelf.common.core.websocket.constant.ChannelEnum;
import com.bonelf.common.core.websocket.constant.MessageRecvCmdEnum;
import com.bonelf.common.core.websocket.constant.MessageSendCmdEnum;
import com.bonelf.common.core.websocket.constant.OnlineStatusEnum;
import com.bonelf.common.service.MQService;
import com.bonelf.common.util.redis.RedisUtil;
import com.bonelf.support.constant.CacheConstant;
import com.bonelf.support.constant.MQSendTag;
import com.bonelf.support.service.impl.SocketMessageService;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <p>
 * websocket
 * -@ChannelHandler.Sharable：线程安全的
 * </p>
 * @author bonelf
 * @since 2020/10/18 18:34
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private WebsocketMap websocketMap;
	@Autowired
	private SocketMessageService socketMessageService;
	@Autowired
	private MQService mqService;
	/**
	 * 使用redis发布订阅收发消息
	 */
	//@Autowired
	//@Deprecated
	//private StringRedisTemplate stringRedisTemplate;
	//@Autowired
	//private RedisTemplate<String, Object> redisTemplate;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
		handleWebSocketFrame(ctx, msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("exceptionCaught", cause);
		String userId = ctx.channel().attr(WebsocketMap.USER_ID_CHANNEL_KEY).get();
		if (StringUtils.hasText(userId)) {
			redisUtil.hset(CacheConstant.WEB_SOCKET_SESSION_HASH, userId, OnlineStatusEnum.OFFLINE.getCode());
			websocketMap.getSocketSessionMap().remove(userId);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("channelInactive");
		String userId = ctx.channel().attr(WebsocketMap.USER_ID_CHANNEL_KEY).get();
		if (StringUtils.hasText(userId)) {
			redisUtil.hset(CacheConstant.WEB_SOCKET_SESSION_HASH, userId, OnlineStatusEnum.OFFLINE.getCode());
			websocketMap.getSocketSessionMap().remove(userId);
		}
	}

	/**
	 * handShake 流程
	 * WebSocketServerProtocolHandler.ServerHandshakeStateEvent->WebSocketServerProtocolHandler.HandshakeComplete
	 * @param ctx
	 * @param evt
	 * @throws Exception
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		log.info("userEventTriggered");
		if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
			this.handleHandleCompleteEvt(ctx, evt);
		}
	}

	/**
	 * 握手成功事件
	 * @param ctx
	 * @param evt
	 */
	private void handleHandleCompleteEvt(ChannelHandlerContext ctx, Object evt) {
		WebSocketServerProtocolHandler.HandshakeComplete httpRequest = (WebSocketServerProtocolHandler.HandshakeComplete)evt;
		log.info("connect event");
		String token = httpRequest.requestHeaders().get(AuthConstant.WEBSOCKET_HEADER);
		log.debug("header:" + JSON.toJSONString(token));
		// FIXME: 2020/10/14 测试用户编号
		String userId = "1";
		if (StringUtils.hasText(userId)) {
			boolean flag = redisUtil.hset(CacheConstant.WEB_SOCKET_SESSION_HASH, userId, OnlineStatusEnum.ONLINE.getCode());
			SocketMessage<?> message = SocketMessage.builder().userIds(userId).cmdId(MessageRecvCmdEnum.INIT_CALLBACK.getCode()).build();
			if (flag) {
				message.setMessage("连接服务器成功");
				ctx.channel().attr(WebsocketMap.USER_ID_CHANNEL_KEY).set(userId);
				websocketMap.getSocketSessionMap().put(userId, ctx.channel());
				//websocketMap.getChannelGroup().add(ctx.channel());
				this.sendMsg2AllChannel(userId, message);
			} else {
				message.setMessage("连接服务器失败");
				this.sendMsg2AllChannel(userId, message);
			}
		}
	}

	/**
	 * 给所有定义接入websocket的发送消息
	 * @param userId
	 * @param message
	 */
	private void sendMsg2AllChannel(String userId, SocketMessage<?> message) {
		for (ChannelEnum value : ChannelEnum.values()) {
			mqService.send(value.getTopicName(), String.format(MQSendTag.WEBSOCKET, message.getCmdId()),
					SocketRespMessage.builder()
							.fromUid(userId)
							.socketMessage(message)
							.build()
			);
			//redisTemplate.convertAndSend(value.getChannelName(),
			//		SocketRespMessage.builder()
			//				.fromUid(userId)
			//				.socketMessage(message)
			//				.build()
			//);
		}
	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		if (frame instanceof TextWebSocketFrame) {
			log.info("TextWebSocketFrame:{}", ((TextWebSocketFrame)frame).text());
			handleTextMessage(ctx.channel().attr(WebsocketMap.USER_ID_CHANNEL_KEY).get(), (TextWebSocketFrame)frame);
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		if (frame instanceof CloseWebSocketFrame) {
			ctx.writeAndFlush(frame.retainedDuplicate()).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		if (frame instanceof BinaryWebSocketFrame) {
			log.info("BinaryWebSocketFrame");
			return;
		}
		if (frame instanceof PongWebSocketFrame) {
			return;
		}
	}

	private void handleTextMessage(String userId, TextWebSocketFrame frame) {
		if (!StringUtils.hasText(frame.text())) {
			return;
		}
		//转SocketMessageHelper
		SocketRespMessage respMessage = new SocketRespMessage();
		SocketMessage<?> socketMessage;
		try {
			// 需要根据cmdId确定转发的服务 所以只有在这里解析一遍数据
			socketMessage = JSON.parseObject(frame.text(), SocketMessage.class);
		} catch (JSONException e) {
			log.warn("收到错误的websocket消息，message：{}", frame.text());
			SocketMessage<String> errMsg = new SocketMessage<>();
			errMsg.setCmdId(MessageSendCmdEnum.ERR_MSG.getCode());
			errMsg.setData(frame.text());
			errMsg.setMessage("invalid message");
			socketMessageService.sendMessage(userId, errMsg.buildMsg());
			return;
		}
		respMessage.setSocketMessage(socketMessage);
		respMessage.setFromUid(userId);
		for (MessageRecvCmdEnum value : MessageRecvCmdEnum.values()) {
			if (value.getChannel() != null && value.getCode().equals(socketMessage.getCmdId())) {
				for (ChannelEnum channelEnum : value.getChannel()) {
					//stringRedisTemplate.convertAndSend(channelEnum.getChannelName(), JSON.toJSONString(respMessage));
					//redisTemplate.convertAndSend(channelEnum.getChannelName(), respMessage);
					mqService.send(channelEnum.getTopicName(), String.format(MQSendTag.WEBSOCKET, respMessage.getSocketMessage().getCmdId()), respMessage);
				}
				break;
			}
		}
	}

}