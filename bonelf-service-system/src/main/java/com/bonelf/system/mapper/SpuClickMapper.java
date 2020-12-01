/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bonelf.system.domain.entity.SpuClick;
import com.bonelf.system.domain.vo.ChartVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface SpuClickMapper extends BaseMapper<SpuClick> {
	/**
	 * 查询表格
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<ChartVO> selectQueryChart(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
