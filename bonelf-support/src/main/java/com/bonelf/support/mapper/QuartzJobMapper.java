package com.bonelf.support.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bonelf.support.domain.entity.QuartzJob;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 定时任务在线管理
 * @author bonelf
 * @version V1.0
 * @date 2019-01-02
 */
@Repository
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {
	List<QuartzJob> findByJobClassName(@Param("jobClassName") String jobClassName);
}
