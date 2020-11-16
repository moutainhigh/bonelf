package com.bonelf.productservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bonelf.productservice.domain.entity.SkuValue;

public interface SkuValueService extends IService<SkuValue> {
	/**
	 * 根据id获取规格中文
	 * @param skuValueIds
	 * @return
	 */
	String getSpecsBySkuValueIds(long[] skuValueIds);

}
