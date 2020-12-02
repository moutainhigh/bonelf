package com.bonelf.productservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bonelf.productservice.domain.entity.Spu;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author caiyuan
 * @since 2020-08-27
 */
public interface SpuMapper extends BaseMapper<Spu> {

	/**
	 * 更新spu价格、库存等信息
	 * @param spuId
	 */
	int updateSpuBySku(Long spuId);

	int updateSpuStockBySkuId(long skuId);
}
