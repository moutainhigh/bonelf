package com.bonelf.productservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bonelf.productservice.domain.bo.CalcPriceBO;
import com.bonelf.productservice.domain.entity.Spu;
import com.bonelf.productservice.domain.query.CalcPriceQuery;
import com.bonelf.productservice.domain.vo.SpuVO;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author caiyuan
 * @since 2020-08-27
 */
public interface SpuService extends IService<Spu> {

	/**
	 * 详情
	 * @param spuId
	 * @return
	 */
	SpuVO getDetail(Long spuId);

	/**
	 * 合计点击量
	 * @return
	 */
	void sumStatistic();

	/**
	 * 商品售出
	 * @return
	 */
	void spuSoldOut(long spuId);

	/**
	 * 更新库存
	 * @param skuId
	 */
	void updateStockBySkuId(long skuId);

	/**
	 * 计算价格
	 * @param calcPriceQuery
	 * @return
	 */
	CalcPriceBO calcPrice(CalcPriceQuery calcPriceQuery);
}
