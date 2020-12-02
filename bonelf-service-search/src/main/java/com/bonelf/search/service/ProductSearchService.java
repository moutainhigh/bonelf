/*
 * Copyright (c) 2020. Bonelf.
 */

package com.bonelf.search.service;

import com.bonelf.search.domain.dto.ProductSearchDTO;
import com.bonelf.search.domain.vo.ProductSearchVO;
import org.springframework.data.domain.Page;

public interface ProductSearchService {
	/**
	 * 搜索
	 * @param productSearchDto
	 * @return
	 */
	Page<ProductSearchVO> searchProduct(ProductSearchDTO productSearchDto);
}
