package com.bonelf.common.core.websocket.constant;

import com.bonelf.common.cloud.constant.ServiceNameConstant;
import com.bonelf.common.constant.CommonCacheConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通道枚举
 */
@Getter
@AllArgsConstructor
public enum ChannelEnum {
	/**
	 * 测试服务
	 */
	TEST(String.format(CommonCacheConstant.WEB_SOCKET_CHANNEL, ServiceNameConstant.TEST_SERVICE)),
	/**
	 * 订单服务
	 */
	ORDER(String.format(CommonCacheConstant.WEB_SOCKET_CHANNEL, ServiceNameConstant.ORDER_SERVICE)),
	;
	private String channelName;
}
