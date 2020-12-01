package com.bonelf.productservice.controller.api;


import com.bonelf.common.domain.Result;
import com.bonelf.common.util.BaseApiController;
import com.bonelf.productservice.domain.vo.SkuApiVO;
import com.bonelf.productservice.domain.vo.SkuKeyApiVO;
import com.bonelf.productservice.domain.vo.SpuVO;
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
