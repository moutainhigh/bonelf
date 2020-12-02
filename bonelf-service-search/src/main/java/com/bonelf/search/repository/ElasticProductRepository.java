/*
 * Copyright (c) 2020. Bonelf.
 */

package com.bonelf.search.repository;

import com.bonelf.search.domain.entity.ProductSearch;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.Future;

public interface ElasticProductRepository extends ElasticsearchRepository<ProductSearch, Long> {
	/**
	 * 流方式查询
	 * @param spuType
	 * @return
	 */
	Streamable<ProductSearch> findByKeywords(Short spuType);

	List<ProductSearch> findBySpuType(Short spuType);

	//删除
	Long deleteBySpuCode(String spuCode);

	//异步查询
	@Async
	Future<ProductSearch> findOneBySpuCode(String spuCode);

	@Async
	ListenableFuture<ProductSearch> findOneBySpuType(Short spuType);

	/**
	 * match查询并设置operator
	 * @param title
	 * @param operator
	 * @return
	 */
	@Query("{\"match\": {\"title\":{ \"query\": \"?0\",\"operator\":\"?1\"}}}")
	ProductSearch findByQueryExp(String title, String operator);

}
