/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.pay.controller.api;

import com.alibaba.fastjson.JSON;
import com.bonelf.common.core.websocket.constant.ChannelEnum;
import com.bonelf.common.domain.Result;
import com.bonelf.common.service.MQService;
import com.bonelf.pay.constant.MQSendTag;
import com.bonelf.pay.domain.dto.PayDTO;
import com.bonelf.pay.domain.vo.WxPayVO;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/pay")
public class PayController {
	@Resource
	private BestPayService bestPayService;
	@Autowired
	private MQService mqService;

	/**
	 * WX:
	 * {
	 * "nonceStr": "0e216mWMWCOMUqzm",
	 * "signType": "MD5",
	 * "packAge": "prepay_id=wx2211152287571655b9f3bf648064500000",
	 * "timeStamp": "1603336522",
	 * "paySign": "677150438D0C726B432F19741306A286"
	 * }
	 * @param payDto
	 * @return
	 */
	@ApiOperation(value = "支付", httpMethod = "POST")
	@PostMapping("")
	public Result<?> pay(@RequestBody PayDTO payDto) {
		// FIXME: 2020/12/1
		PayRequest payRequest = null;
		log.info("\n【发起支付】request={}", JSON.toJSONString(payRequest));
		PayResponse payResponse = bestPayService.pay(payRequest);
		log.info("\n【发起支付】response={}", JSON.toJSONString(payResponse));
		if ("ali".equals(payDto.getPayType())) {
			log.info("\n【返回参数】resp={}", payResponse.getBody());
			return Result.ok(payResponse.getBody());
		} else {
			WxPayVO pay = WxPayVO.builder()
					.nonceStr(payResponse.getNonceStr())
					.signType(payResponse.getSignType())
					.packAge(payResponse.getPackAge())
					.timeStamp(payResponse.getTimeStamp())
					.paySign(payResponse.getPaySign())
					.build();
			log.info("\n【返回参数】resp={}", JSON.toJSONString(pay));
			return Result.ok(pay);
		}
	}

	@ApiIgnore
	@ApiOperation(value = "微信回调", notes = "退款和支付一个接口，微信退款可自定义回调接口，没有则是同一个")
	@GetMapping("/notify/wx")
	public String notifyWx() {
		Long orderId = 1L;
		Long spuId = 1L;
		mqService.send(ChannelEnum.PRODUCT, MQSendTag.PRODUCT_PAID_TAG, spuId);
		return "<xml><return_code><![CDATA[SUCCESS]]</return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
	}

	@ApiIgnore
	@ApiOperation(value = "阿里回调", notes = "退款和支付一个接口")
	@GetMapping("/notify/ali")
	public String notifyAli() {
		Long orderId = 1L;
		Long spuId = 1L;
		mqService.send(ChannelEnum.PRODUCT, MQSendTag.PRODUCT_PAID_TAG, spuId);
		return "success";
	}
}
