package com.bonelf.productservice.controller.api;


import cn.hutool.core.bean.BeanUtil;
import com.bonelf.common.domain.Result;
import com.bonelf.common.util.BaseApiController;
import com.bonelf.productservice.domain.bo.CalcPriceBO;
import com.bonelf.productservice.domain.dto.CalcPriceDTO;
import com.bonelf.productservice.domain.dto.ConfirmOrderDTO;
import com.bonelf.productservice.domain.query.CalcPriceQuery;
import com.bonelf.productservice.domain.query.PriceInfo;
import com.bonelf.productservice.domain.vo.*;
import com.bonelf.productservice.service.SkuService;
import com.bonelf.productservice.service.SpuService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 注释：
 * @author caiyuan
 * @version v1.0
 * @ClassName SpuController
 * @date 2020/8/28 0028 9:45
 */
@RestController
@RequestMapping("/api/spu")
public class SpuController extends BaseApiController {
	@Autowired
	private SpuService spuService;
	@Autowired
	private SkuService skuService;

	@PostMapping("/calcPrice")
	@ApiOperation("价格计算 不是订单确认，是在点击购物车时选择计算价格")
	public Result<?> calcPrice(@RequestBody CalcPriceDTO calcPriceDto) {
		CalcPriceVO calcPriceInfo = new CalcPriceVO();
		//计算价格的逻辑往往比计算价格接口逻辑更复杂，需要封装参数，并且公共方法用于调用
		CalcPriceQuery calcPriceQuery = BeanUtil.copyProperties(calcPriceDto, CalcPriceQuery.class);
		//中间产物接收接口，避免重复数据库查询获取数据
		calcPriceQuery.setPriceInfo(new PriceInfo() {
			@Override
			public void getCoupon(UserCouponVO coupon) {
				calcPriceInfo.setUserCoupon(coupon);
			}
		});
		//然后根据 calcPriceQuery 计算价格CalcPriceBO(没有userCoupon参数，只有价格相关参数)，再装配CalcPriceVO
		CalcPriceBO calcPriceBo = spuService.calcPrice(calcPriceQuery);
		return Result.ok(calcPriceInfo);
	}

	@PostMapping("/confirmOrder")
	@ApiOperation("订单确认")
	public Result<?> confirmOrder(@RequestBody ConfirmOrderDTO confirmOrderDto) {
		CalcPriceQuery calcPriceQuery = BeanUtil.copyProperties(confirmOrderDto, CalcPriceQuery.class);
		calcPriceQuery.setAutoSelectCoupon(false);
		//计算价格 返回地址信息、订单信息等
		return Result.ok();
	}

	@PostMapping("")
	@ApiOperation("添加")
	public Result<?> add() {

		return Result.ok();
	}

	@PutMapping("/{spuId}")
	@ApiOperation("修改")
	public Result<?> edit() {

		return Result.ok();
	}

	@GetMapping("")
	@ApiOperation("分页查询")
	public Result<?> list() {

		return Result.ok();
	}

	@GetMapping("/{spuId}")
	@ApiOperation("详情")
	public Result<SpuVO> detail(@ApiParam("编号") @PathVariable Long spuId) {
		SpuVO spuVO = spuService.getDetail(spuId);
		return Result.ok(spuVO);
	}

	/**
	 * 给选中的 skuValueId 查询是否有合法的组合
	 * @param spuId
	 * @return
	 */
	@GetMapping("/getSkuList")
	public Result<?> getSkuList(@RequestParam Long spuId) {
		List<SkuKeyApiVO> skuVO = skuService.getSkuList(spuId);
		return Result.ok(skuVO);
	}

	/**
	 * 给选中的 skuValueIds 查询是否有合法的组合
	 * @param skuValueIds
	 * @return
	 */
	@GetMapping("/getSkuInfoList")
	public Result<?> getSkuInfoList(@RequestParam(required = false) Long[] skuValueIds) {
		SkuApiVO skuVO = skuService.getSkuInfoList(skuValueIds);
		return Result.ok(skuVO);
	}
}
