package com.bonelf.productservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bonelf.productservice.domain.entity.SkuKey;

import java.util.List;

public interface SkuKeyMapper extends BaseMapper<SkuKey> {

	/**
	 * 所有规格列表
	 * @param spuId
	 * @return
	 */
	List<SkuKey> selectSkuList(Long spuId);
}