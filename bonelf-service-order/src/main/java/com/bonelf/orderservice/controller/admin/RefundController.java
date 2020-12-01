package com.bonelf.orderservice.controller.admin;

import com.bonelf.common.domain.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 退款记录
 * </p>
 * @author Chenyuan
 * @since 2020/12/1 14:23
 */
@RestController
@RequestMapping("/mgr/refund")
public class RefundController {

	@ApiOperation("退款原因列表选择")
	@GetMapping("/refundReason")
	public Result<?> refundReason() {
		return Result.ok();
	}

	@ApiOperation("创建退款")
	@GetMapping("/createRefund")
	public Result<?> createRefund() {
		return Result.ok();
	}

	@ApiOperation("退款详情")
	@GetMapping("/{refundId}")
	public Result<?> refundDetail(@ApiParam("退款详情") @PathVariable String refundId) {
		return Result.ok();
	}
}
