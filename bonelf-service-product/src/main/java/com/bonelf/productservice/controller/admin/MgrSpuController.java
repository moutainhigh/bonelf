package com.bonelf.productservice.controller.admin;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bonelf.common.constant.enums.YesOrNotEnum;
import com.bonelf.common.domain.Result;
import com.bonelf.productservice.domain.dto.SkuEditDTO;
import com.bonelf.productservice.domain.dto.SkuKeyEditDTO;
import com.bonelf.productservice.domain.entity.Sku;
import com.bonelf.productservice.domain.entity.Spu;
import com.bonelf.productservice.domain.vo.SkuKeyMgrVO;
import com.bonelf.productservice.service.SkuService;
import com.bonelf.productservice.service.SpuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 * @author caiyuan
 * @since 2020-07-31
 */
@RestController
@RequestMapping("/v1/system/spu")
@Slf4j
@Api(tags = "商品表")
public class MgrSpuController {
	@Autowired
	private SkuService skuService;
	@Autowired
	private SpuService spuService;

	@ApiOperation(value = "禁用规格")
	@PostMapping("/disableSku")
	public Result<?> disableSku(@ApiParam("商品编号") @RequestParam Long spuId) {
		spuService.update(Wrappers.<Spu>lambdaUpdate().set(Spu::getEnableSku, YesOrNotEnum.N.getCode()).eq(Spu::getSpuId, spuId));
		return Result.ok();
	}

	@ApiOperation(value = "规格键列表")
	@GetMapping("/getSkuKey")
	public Result<?> getSkuKey(@ApiParam("商品编号") @RequestParam Long spuId) {
		List<SkuKeyMgrVO> skuVo = skuService.getSkuKey(spuId);
		return Result.ok(skuVo);
	}

	@ApiOperation(value = "规格列表")
	@GetMapping("/getSku")
	public Result<?> getSku(@ApiParam("商品编号") @RequestParam Long spuId) {
		List<Sku> skuVo = skuService.getSku(spuId);
		return Result.ok(skuVo);
	}

	@ApiOperation(value = "规格键编辑")
	@PostMapping("/editSkuKey")
	public Result<?> editSkuKey(@RequestBody SkuKeyEditDTO sku) {
		skuService.saveSkuKey(sku);
		return Result.ok();
	}

	@ApiOperation(value = "规格编辑")
	@PostMapping("/editSku")
	public Result<?> editSku(@RequestBody SkuEditDTO sku) {
		spuService.update(Wrappers.<Spu>lambdaUpdate().set(Spu::getEnableSku, YesOrNotEnum.Y.getCode()).eq(Spu::getSpuId, sku.getSpuId()));
		skuService.saveSku(sku);
		return Result.ok();
	}
}

