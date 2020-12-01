/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.pay.controller.api;

import com.bonelf.common.core.websocket.constant.ChannelEnum;
import com.bonelf.common.service.MQService;
import com.bonelf.pay.constant.MQSendTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PayController {
	@Autowired
	private MQService mqService;

	@GetMapping("/notify")
	public String notifyWx() {
		Long orderId = 1L;
		Long spuId = 1L;
		mqService.send(ChannelEnum.PRODUCT, MQSendTag.PRODUCT_PAID_TAG, spuId);
		return "<xml>XXX</xml>";
	}
}
