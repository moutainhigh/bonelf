package com.bonelf.productservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bonelf.productservice.domain.dto.SkuEditDTO;
import com.bonelf.productservice.domain.dto.SkuKeyEditDTO;
import com.bonelf.productservice.domain.entity.Sku;
import com.bonelf.productservice.domain.vo.SkuApiVO;
import com.bonelf.productservice.domain.vo.SkuKeyApiVO;
import com.bonelf.productservice.domain.vo.SkuKeyMgrVO;

import java.util.List;

public interface SkuService extends IService<Sku> {
	/**
	 * 规格列表
	 * @param spuId 商品编号
	 * @return
	 */
	List<SkuKeyApiVO> getSkuList(Long spuId);

	/**
	 * 规格信息
	 * @param skuValueId
	 * @return
	 */
	SkuApiVO getSkuInfoList(Long[] skuValueId);

	/**
	 * 规格定义编辑
	 * @param spuId
	 * @return
	 */
	List<SkuKeyMgrVO> getSkuKey(Long spuId);
	/**
	 * 规格信息编辑
	 * @param spuId
	 * @return
	 */
	List<Sku> getSku(Long spuId);

	/**
	 * 保存规格定义
	 * @param sku
	 */
	void saveSkuKey(SkuKeyEditDTO sku);

	/**
	 * 保存Sku
	 * @param sku
	 */
	void saveSku(SkuEditDTO sku);
}
