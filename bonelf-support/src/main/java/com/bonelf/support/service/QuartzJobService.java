package com.bonelf.support.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bonelf.support.domain.entity.QuartzJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 *  定时任务在线管理
 * @author bonelf
 * @date 2019-04-28
 * @version: V1.1
 */
public interface QuartzJobService extends IService<QuartzJob> {

	List<QuartzJob> findByJobClassName(String jobClassName);

	boolean saveAndScheduleJob(QuartzJob quartzJob);

	boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException;

	boolean deleteAndStopJob(QuartzJob quartzJob);

	boolean resumeJob(QuartzJob quartzJob);

	/**
	 * 执行定时任务
	 * @param quartzJob
	 */
	void execute(QuartzJob quartzJob) throws Exception;

	/**
	 * 暂停任务
	 * @param quartzJob
	 * @throws SchedulerException
	 */
	void pause(QuartzJob quartzJob);
}
