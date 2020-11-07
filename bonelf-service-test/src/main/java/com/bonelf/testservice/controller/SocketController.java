package com.bonelf.testservice.controller;

import com.bonelf.common.core.websocket.SocketRespMessage;
import com.bonelf.testservice.service.impl.SocketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/socket")
public class SocketController {
	@Autowired
	private SocketMessageService socketMessageService;

	@PostMapping("/send")
	public String restTemplateTest(@RequestBody SocketRespMessage socketMessage) {
		socketMessageService.sendMessage(socketMessage.getFromUid(), socketMessage.getSocketMessage());
		return "ok";
	}
}
