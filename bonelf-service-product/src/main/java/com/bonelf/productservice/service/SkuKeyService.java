package com.bonelf.productservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bonelf.productservice.domain.entity.SkuKey;

import java.util.List;

public interface SkuKeyService extends IService<SkuKey> {

	/**
	 * 规格定义列表
	 * @param spuId
	 * @return
	 */
	List<SkuKey> selectSkuList(Long spuId);
}
