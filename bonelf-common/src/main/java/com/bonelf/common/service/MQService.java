package com.bonelf.common.service;

import com.bonelf.common.core.websocket.constant.ChannelEnum;

public interface MQService {

	<T> void send(String topic, String tag, T message);

	<T> void send(ChannelEnum channel, String tag, T message);
}
