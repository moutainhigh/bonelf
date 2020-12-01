package com.bonelf.common.core.websocket.constant;

import com.bonelf.common.cloud.constant.ServiceNameConstant;
import com.bonelf.common.constant.CommonCacheConstant;
import com.bonelf.common.constant.MQTopic;
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
	@Deprecated
	TEST_REDIS(String.format(CommonCacheConstant.WEB_SOCKET_CHANNEL, ServiceNameConstant.TEST_SERVICE)),
	/**
	 * 订单服务
	 */
	@Deprecated
	ORDER_REDIS(String.format(CommonCacheConstant.WEB_SOCKET_CHANNEL, ServiceNameConstant.ORDER_SERVICE)),
	/**
	 * 测试服务
	 */
	TEST(String.format(MQTopic.MQ_WEB_SOCKET_CHANNEL, ServiceNameConstant.TEST_SERVICE)),
	/**
	 * 订单服务
	 */
	ORDER(String.format(MQTopic.MQ_WEB_SOCKET_CHANNEL, ServiceNameConstant.ORDER_SERVICE)),

	PRODUCT(String.format(MQTopic.MQ_WEB_SOCKET_CHANNEL, ServiceNameConstant.PRODUCT)),

	PROMOTION(String.format(MQTopic.MQ_WEB_SOCKET_CHANNEL, ServiceNameConstant.PROMOTION)),

	SYSTEM(String.format(MQTopic.MQ_WEB_SOCKET_CHANNEL, ServiceNameConstant.SYSTEM));
	private String topicName;
}
