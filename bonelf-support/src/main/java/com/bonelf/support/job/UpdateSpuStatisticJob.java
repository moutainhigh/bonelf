package com.bonelf.support.job;

import com.bonelf.common.core.websocket.constant.ChannelEnum;
import com.bonelf.common.service.MQService;
import com.bonelf.support.constant.MQSendTag;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品服务定时统计点击量、销售量
 * @author
 */
@Slf4j
@Component
public class UpdateSpuStatisticJob implements Job {
	@Autowired
	private MQService mqService;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		mqService.send(ChannelEnum.PRODUCT, MQSendTag.SPU_STATISTIC_SUM_TAG, "");
	}
}
