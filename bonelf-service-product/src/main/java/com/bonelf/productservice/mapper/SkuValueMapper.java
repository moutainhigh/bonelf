package com.bonelf.productservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bonelf.productservice.domain.entity.SkuValue;

import java.util.List;

public interface SkuValueMapper extends BaseMapper<SkuValue> {
	/**
	 * 查询不属于skuValueIds上级skuKey的其他skuValue
	 * @param skuValueIds
	 * @return
	 */
	List<SkuValue> selectSkuValueDiffFromSkuKey(Long[] skuValueIds);
	/**
	 * 根据SkuValueIds差规格字符串信息
	 * @param skuValueIds
	 * @return
	 */
	String selectSpecsBySkuValueIds(long[] skuValueIds);
}