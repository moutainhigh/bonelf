/*
 * Copyright (c) 2020. Bonelf.
 */

package com.bonelf.orderservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bonelf.orderservice.domain.entity.ProductOrder;


public interface ProductOrderService extends IService<ProductOrder> {
	/**
	 * 订单超时
	 * @param orderId
	 */
	void orderCancel(Long orderId);
}
