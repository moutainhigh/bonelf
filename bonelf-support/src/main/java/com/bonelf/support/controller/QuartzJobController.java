package com.bonelf.support.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bonelf.common.domain.Result;
import com.bonelf.support.domain.entity.QuartzJob;
import com.bonelf.support.service.QuartzJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 定时任务在线管理
 * @author bonelf
 * @date  2019-01-02
 */
@RestController
@RequestMapping("/v1/sys/quartzJob")
@Slf4j
@Api(tags = "定时任务接口")
public class QuartzJobController {
	@Autowired
	private QuartzJobService quartzJobService;
	@Autowired
	private Scheduler scheduler;

	/**
	 * 分页列表查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<?> queryPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		QueryWrapper<QuartzJob> queryWrapper = Wrappers.query();
		Page<QuartzJob> page = new Page<>(pageNo, pageSize);
		IPage<QuartzJob> pageList = quartzJobService.page(page, queryWrapper);
		return Result.ok(pageList);

	}

	/**
	 * 添加定时任务
	 * @param quartzJob
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<?> add(@RequestBody QuartzJob quartzJob) {
		List<QuartzJob> list = quartzJobService.findByJobClassName(quartzJob.getJobClassName());
		if (list != null && list.size() > 0) {
			return Result.error("该定时任务类名已存在");
		}
		quartzJobService.saveAndScheduleJob(quartzJob);
		return Result.ok("创建定时任务成功");
	}

	/**
	 * 更新定时任务
	 * @param quartzJob
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public Result<?> edit(@RequestBody QuartzJob quartzJob) {
		try {
			quartzJobService.editAndScheduleJob(quartzJob);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			return Result.error("更新定时任务失败!");
		}
		return Result.ok("更新定时任务成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		QuartzJob quartzJob = quartzJobService.getById(id);
		if (quartzJob == null) {
			return Result.error("未找到对应实体");
		}
		quartzJobService.deleteAndStopJob(quartzJob);
		return Result.ok("删除成功!");

	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		if (ids == null || "".equals(ids.trim())) {
			return Result.error("参数不识别！");
		}
		for (String id : Arrays.asList(ids.split(","))) {
			QuartzJob job = quartzJobService.getById(id);
			quartzJobService.deleteAndStopJob(job);
		}
		return Result.ok("删除定时任务成功!");
	}

	/**
	 * 暂停定时任务
	 * @param jobClassName
	 * @return
	 */
	@GetMapping(value = "/pause")
	@ApiOperation(value = "暂停定时任务")
	public Result<Object> pauseJob(@RequestParam(name = "jobClassName", required = true) String jobClassName) {
		QuartzJob job = null;
		job = quartzJobService.getOne(new LambdaQueryWrapper<QuartzJob>().eq(QuartzJob::getJobClassName, jobClassName));
		if (job == null) {
			return Result.error("定时任务不存在！");
		}
		quartzJobService.pause(job);
		return Result.ok("暂停定时任务成功");
	}

	/**
	 * 启动定时任务
	 * @param jobClassName
	 * @return
	 */
	@GetMapping(value = "/resume")
	@ApiOperation(value = "恢复定时任务")
	public Result<Object> resumeJob(@RequestParam(name = "jobClassName", required = true) String jobClassName) {
		QuartzJob job = quartzJobService.getOne(new LambdaQueryWrapper<QuartzJob>().eq(QuartzJob::getJobClassName, jobClassName));
		if (job == null) {
			return Result.error("定时任务不存在！");
		}
		quartzJobService.resumeJob(job);
		//scheduler.resumeJob(JobKey.jobKey(job.getJobClassName().trim()));
		return Result.ok("恢复定时任务成功");
	}

	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/queryById", method = RequestMethod.GET)
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		QuartzJob quartzJob = quartzJobService.getById(id);
		return Result.ok(quartzJob);
	}

	/**
	 * 立即执行
	 * @param id
	 * @return
	 */
	@GetMapping("/execute")
	public Result<?> execute(@RequestParam(name = "id", required = true) String id) {
		QuartzJob quartzJob = quartzJobService.getById(id);
		if (quartzJob == null) {
			return Result.error("未找到对应实体");
		}
		try {
			quartzJobService.execute(quartzJob);
		} catch (Exception e) {
			log.info("定时任务 立即执行失败>>" + e.getMessage());
			return Result.error("执行失败!");
		}
		return Result.ok("执行成功!");
	}
}
