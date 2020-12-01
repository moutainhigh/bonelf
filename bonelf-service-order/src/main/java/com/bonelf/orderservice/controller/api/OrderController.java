package com.bonelf.orderservice.controller.api;

import com.bonelf.common.domain.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

	@ApiOperation(value = "订单列表" , notes = "有的需求喜欢把所有类型订单放在一起")
	@GetMapping("/orderList")
	public Result<?> orderList() {
		return Result.ok();
	}
}
