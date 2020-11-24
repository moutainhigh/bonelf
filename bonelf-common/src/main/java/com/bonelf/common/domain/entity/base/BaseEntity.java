package com.bonelf.common.domain.entity.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 注释：pojo顶层父类实体 定义公共字段
 * @author bonelf
 * @date 2020/4/22 0022 13:53
 */
@Data
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = -6714561894310252775L;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(value = "create_time", fill = FieldFill.INSERT, insertStrategy = FieldStrategy.NOT_NULL)
	private LocalDateTime createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(value = "update_time", fill = FieldFill.UPDATE, insertStrategy = FieldStrategy.NOT_NULL)
	private LocalDateTime updateTime;

}
