package com.bonelf.support.service.impl;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bonelf.common.constant.enums.YesOrNotEnum;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.support.domain.entity.QuartzJob;
import com.bonelf.support.mapper.QuartzJobMapper;
import com.bonelf.support.service.QuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 定时任务在线管理
 * @author bonelf
 * @version V1.1
 * @date 2019-04-28
 */
@Slf4j
@Service
public class QuartzJobServiceImpl extends ServiceImpl<QuartzJobMapper, QuartzJob> implements QuartzJobService {
	@Autowired
	private Scheduler scheduler;

	/**
	 * 立即执行的任务分组
	 */
	private static final String JOB_TEST_GROUP = "test_group";

	@Override
	public List<QuartzJob> findByJobClassName(String jobClassName) {
		return baseMapper.findByJobClassName(jobClassName);
	}

	/**
	 * 保存&启动定时任务
	 */
	@Override
	public boolean saveAndScheduleJob(QuartzJob quartzJob) {
		if (YesOrNotEnum.N.getCode().equals(quartzJob.getStatus())) {
			// 定时器添加
			this.schedulerAdd(quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
		}
		// DB设置修改
		quartzJob.setDelFlag(YesOrNotEnum.N.getCode());
		return this.save(quartzJob);
	}

	/**
	 * 恢复定时任务
	 */
	@Override
	public boolean resumeJob(QuartzJob quartzJob) {
		schedulerDelete(quartzJob.getJobClassName().trim());
		schedulerAdd(quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
		quartzJob.setStatus(YesOrNotEnum.N.getCode());
		return this.updateById(quartzJob);
	}

	/**
	 * 编辑&启停定时任务
	 * @throws SchedulerException
	 */
	@Override
	public boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException {
		if (YesOrNotEnum.N.getCode().equals(quartzJob.getStatus())) {
			schedulerDelete(quartzJob.getJobClassName().trim());
			schedulerAdd(quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
		} else {
			scheduler.pauseJob(JobKey.jobKey(quartzJob.getJobClassName().trim()));
		}
		return this.updateById(quartzJob);
	}

	/**
	 * 删除&停止删除定时任务
	 */
	@Override
	public boolean deleteAndStopJob(QuartzJob job) {
		schedulerDelete(job.getJobClassName().trim());
		boolean ok = this.removeById(job.getQrtzId());
		return ok;
	}

	@Override
	public void execute(QuartzJob quartzJob) throws Exception {
		String jobName = quartzJob.getJobClassName().trim();
		Date startDate = new Date();
		String ymd = DatePattern.PURE_DATETIME_FORMAT.format(startDate);
		String identity = jobName + ymd;
		//3秒后执行 只执行一次
		startDate.setTime(startDate.getTime() + 3000L);
		// 定义一个Trigger
		SimpleTrigger trigger = (SimpleTrigger)TriggerBuilder.newTrigger()
				.withIdentity(identity, JOB_TEST_GROUP)
				.startAt(startDate)
				.build();
		// 构建job信息
		JobDetail jobDetail = JobBuilder.newJob(getClass(jobName).getClass()).withIdentity(identity).usingJobData("parameter", quartzJob.getParameter()).build();
		// 将trigger和 jobDetail 加入这个调度
		scheduler.scheduleJob(jobDetail, trigger);
		// 启动scheduler
		scheduler.start();
	}

	@Override
	public void pause(QuartzJob quartzJob) {
		schedulerDelete(quartzJob.getJobClassName().trim());
		quartzJob.setStatus(YesOrNotEnum.Y.getCode());
		this.updateById(quartzJob);
	}

	/**
	 * 添加定时任务
	 * @param jobClassName
	 * @param cronExpression
	 * @param parameter
	 */
	private void schedulerAdd(String jobClassName, String cronExpression, String parameter) {
		try {
			// 启动调度器
			scheduler.start();

			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName).usingJobData("parameter", parameter).build();

			// 表达式调度构建器(即任务执行的时间)
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName).withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new BonelfException("创建定时任务失败");
		} catch (RuntimeException e) {
			throw new BonelfException(e.getMessage());
		} catch (Exception e) {
			throw new BonelfException("后台找不到该类名：" + jobClassName);
		}
	}

	/**
	 * 删除定时任务
	 * @param jobClassName
	 */
	private void schedulerDelete(String jobClassName) {
		try {
			scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName));
			scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName));
			scheduler.deleteJob(JobKey.jobKey(jobClassName));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BonelfException("删除定时任务失败");
		}
	}

	private static Job getClass(String classname) throws Exception {
		Class<?> class1 = Class.forName(classname);
		return (Job)class1.newInstance();
	}

}
