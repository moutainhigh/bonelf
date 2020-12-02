package com.bonelf.productservice.bus;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bonelf.common.util.redis.RedisUtil;
import com.bonelf.productservice.constant.CacheConstant;
import com.bonelf.productservice.constant.MQRecvTag;
import com.bonelf.productservice.domain.entity.Sku;
import com.bonelf.productservice.domain.entity.Spu;
import com.bonelf.productservice.service.SkuService;
import com.bonelf.productservice.service.SpuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author: lockie
 * @Date: 2020/4/21 11:05
 * @Description: 消费者监听
 */
@Slf4j
@Component
public class MQConsumeMsgListenerProcessor implements MessageListenerConcurrently {
	@Autowired
	private SpuService spuService;
	@Autowired
	private SkuService skuService;
	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 默认msg里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
	 * 不要抛异常，如果没有return CONSUME_SUCCESS ，consumer会重新消费该消息，直到return CONSUME_SUCCESS
	 * @param msgList
	 * @param consumeConcurrentlyContext
	 * @return
	 */
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgList, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
		if (CollectionUtils.isEmpty(msgList)) {
			log.info("MQ接收消息为空，直接返回成功");
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		MessageExt messageExt = msgList.get(0);
		log.info("MQ接收到的消息为：" + messageExt.toString());
		try {
			String topic = messageExt.getTopic();
			String tags = messageExt.getTags();
			String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
			if (MQRecvTag.SPU_STATISTIC_SUM.equals(tags)) {
				spuService.sumStatistic();
			} else if (MQRecvTag.PRODUCT_PAID_TAG.equals(tags)) {
				// FIXME: 2020/12/1 数量
				spuService.spuSoldOut(Long.parseLong(body));
			} else if (MQRecvTag.STOCK_SKU_TAG.equals(tags)) {
				skuService.update(Wrappers.<Sku>lambdaUpdate().set(Sku::getStock, (Integer)redisUtil.hget(CacheConstant.SKU_STOCK_HASH, body)).eq(Sku::getSkuId, Long.parseLong(body)));
				// 更新 Spu 表总库存 XXX 这里耗时可能有点大
				spuService.updateStockBySkuId(Long.parseLong(body));
			} else if (MQRecvTag.STOCK_SPU_TAG.equals(tags)) {
				spuService.update(Wrappers.<Spu>lambdaUpdate().set(Spu::getStock, (Integer)redisUtil.hget(CacheConstant.SKU_STOCK_HASH, body)).eq(Spu::getSpuId, Long.parseLong(body)));
			}
			log.info("MQ消息topic={}, tags={}, 消息内容={}", topic, tags, body);
		} catch (Exception e) {
			log.error("获取MQ消息内容异常", e);
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
