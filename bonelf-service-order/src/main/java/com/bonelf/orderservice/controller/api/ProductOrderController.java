package com.bonelf.orderservice.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productOrder")
public class ProductOrderController {

	@GetMapping("/getOrderById")
	public String norm(@RequestParam String orderId) {
		System.out.println(orderId);
		return "ok";
	}
}
