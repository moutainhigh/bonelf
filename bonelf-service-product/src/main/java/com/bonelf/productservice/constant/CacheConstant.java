/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.productservice.constant;

import com.bonelf.common.constant.BonelfConstant;

public interface CacheConstant {
	/**
	 * 商品点击缓存
	 */
	String SPU_CLICK_HASH = BonelfConstant.PROJECT_NAME + ":spuClick";
	/**
	 * 商品售出缓存
	 */
	String SPU_SOLD_HASH = BonelfConstant.PROJECT_NAME + ":spuSold";
}
