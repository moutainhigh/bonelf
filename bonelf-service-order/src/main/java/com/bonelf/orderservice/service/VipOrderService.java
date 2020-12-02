/*
 * Copyright (c) 2020. Bonelf.
 */

package com.bonelf.orderservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bonelf.orderservice.domain.entity.VipOrder;

/**
 * <p>
 * 订单
 * </p>
 * @author Chenyuan
 * @since 2020/12/1 16:16
 */
public interface VipOrderService extends IService<VipOrder> {
	/**
	 * 订单超时
	 * @param orderId
	 */
	void orderCancel(Long orderId);
}
