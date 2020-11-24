package com.bonelf.support.controller;

import com.bonelf.common.core.websocket.SocketRespMessage;
import com.bonelf.common.domain.Result;
import com.bonelf.support.service.impl.SocketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * websocket服务请求
 * </p>
 * @author bonelf
 * @since 2020/10/18 20:29
 */
@RestController
@RequestMapping("/websocket")
public class WebsocketController {

	@Autowired
	private SocketMessageService socketMessageService;

	/**
	 * <p>
	 * 发送消息
	 * </p>
	 * @author bonelf
	 * @since 2020/10/5 21:57
	 */
	@PostMapping("/v1/sendMessage")
	public Result<String> sendMessage(@RequestBody SocketRespMessage message) {
		int result = socketMessageService.sendMessage(message.getFromUid(), message.getSocketMessage());
		return SocketMessageService.SEND_OK == result ? Result.ok() : Result.error();
	}
}
