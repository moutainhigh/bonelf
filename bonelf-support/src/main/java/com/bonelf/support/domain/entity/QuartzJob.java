package com.bonelf.support.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 定时任务在线管理
 * 系统定时器 此表只是数据表 定时器实现为QRZT_表
 * @author  bonelf
 * @date 2019-01-02
 */
@Data
@TableName("sys_quartz_job")
public class QuartzJob implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	//@TableId(type = IdType.ASSIGN_ID)
	@TableId(type = IdType.ASSIGN_ID)
	private String qrtzId;
	/**
	 * 任务类名
	 */
	private String jobClassName;
	/**
	 * cron表达式
	 */
	private String cronExpression;
	/**
	 * 参数
	 */
	private String parameter;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 状态 0正常 1停止
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;
	/**
	 * 修改时间
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**
	 * 删除状态
	 */
	private Integer delFlag;

}
