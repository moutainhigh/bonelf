/*
 * Copyright (c) 2020. Bonelf.
 */

package com.bonelf.search;

import com.bonelf.search.domain.entity.ProductSearch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 * 验证依赖注入的bean和配置是否正确
 * </p>
 * @author bonelf
 * @since 2020/10/11 16:09
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ComponentTests {
	@Autowired
	private ElasticsearchRestTemplate elasticsearchRestTemplate;

	@Test
	public void jsonUtilTest() {
		//log.info(JsonUtil.objToJson(new TestConverterVO()));
	}


	/**
	 * 创建索引库
	 */
	@Test
	void textIndex() {
		//设置索引信息(绑定实体类)  返回IndexOperations
		IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(ProductSearch.class);
		//创建索引库
		indexOperations.create();
		//Creates the index mapping for the entity this IndexOperations is bound to.
		//为该IndexOperations绑定到的实体创建索引映射。  有一个为给定类创建索引的重载,需要类的字节码文件
		Document mapping = indexOperations.createMapping();
		//writes a mapping to the index  将刚刚通过类创建的映射写入索引
		indexOperations.putMapping(mapping);
	}

}
