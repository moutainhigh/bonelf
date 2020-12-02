package com.bonelf.common.service.impl;

import com.bonelf.common.core.websocket.constant.ChannelEnum;
import com.bonelf.common.service.MQService;
import com.bonelf.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "rocketmq", value = "enable", havingValue = "true")
public class MQServiceImpl implements MQService {
	@Autowired
	private DefaultMQProducer defaultMQProducer;

	@Override
	public <T> void send(String topic, String tag, T message) {
		try {
			defaultMQProducer.send(new Message(topic, tag, JsonUtil.toJson(message).getBytes()));
		} catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
			log.error("消息发送失败", e);
		}
	}

	@Override
	public <T> void send(ChannelEnum channel, String tag, T message) {
		send(channel.getTopicName(), tag, message);
	}
}
