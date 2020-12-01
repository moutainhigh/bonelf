/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.support.constant;

public interface MQSendTag {
	/**
	 * Websocket消息发送 format: cmdId
	 */
	String WEBSOCKET = "SocketTag_%d";

	/**
	 * 缓存点击量入库
	 */
	String SPU_CLICK_SUM = "SpuClickSumTag";

	/**
	 * 缓存点击量入库
	 */
	String SPU_STATISTIC_SUM_TAG = "SpuStatisticSumTag";
}
