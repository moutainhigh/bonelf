/*
 * Copyright (c) 2020. Bonelf.
 */

package com.bonelf.search.controller.api;

import com.bonelf.common.domain.Result;
import com.bonelf.search.domain.dto.ProductSearchDTO;
import com.bonelf.search.domain.vo.ProductSearchVO;
import com.bonelf.search.service.ProductSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("产品搜索")
@RestController
@RequestMapping("/product")
public class ProductSearchController {
	@Autowired
	private ProductSearchService productSearchService;

	@ApiParam("搜索")
	@RequestMapping("/search")
	public Result<ProductSearchVO> search(ProductSearchDTO productSearchDto) {
		Page<ProductSearchVO> productSearchVoPage = productSearchService.searchProduct(productSearchDto);
		return Result.ok();
	}


}
