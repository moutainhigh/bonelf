/*
 * Copyright (c) 2020. Bonelf.
 */

package com.bonelf.search;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 * 测试类
 * 添加@Transactional注解测试数据将回滚
 * </p>
 * @author bonelf
 * @since 2020/10/11 16:09
 */
//@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
	private String test;

	@Test
	public void test() {
		System.out.println(test);
	}

}
