package com.bonelf.common.config;

import com.bonelf.common.config.property.RocketmqProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bonelf
 * @description mq生产者配置
 * @date 2020/4/21 10:28
 */
@Slf4j
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "rocketmq.producer")
public class MQProducerConfig {
	@Autowired
	private RocketmqProperty rocketmqProperty = new RocketmqProperty();
	// 消息最大值
	private Integer maxMessageSize;
	// 消息发送超时时间
	private Integer sendMsgTimeOut;
	// 失败重试次数
	private Integer retryTimesWhenSendFailed;

	/**
	 * mq 生成者配置
	 * @return
	 * @throws MQClientException
	 */
	@Bean
	public DefaultMQProducer defaultProducer() throws MQClientException {
		log.info("defaultProducer 正在创建---------------------------------------");
		DefaultMQProducer producer = new DefaultMQProducer(rocketmqProperty.getGroupName());
		producer.setNamesrvAddr(rocketmqProperty.getNamesrvAddr());
		producer.setVipChannelEnabled(false);
		producer.setMaxMessageSize(maxMessageSize);
		producer.setSendMsgTimeout(sendMsgTimeOut);
		producer.setRetryTimesWhenSendAsyncFailed(retryTimesWhenSendFailed);
		producer.start();
		log.info("rocketmq producer server 开启成功----------------------------------");
		return producer;
	}
}
