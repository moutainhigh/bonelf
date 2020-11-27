/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.testservice.client.fallback;

import com.bonelf.common.domain.Result;
import com.bonelf.testservice.client.UserFeignClient;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * 签权服务
 * </p>
 * @author bonelf
 * @since 2020/11/17 15:37
 */
@Component("userFeignClientFallback")
public class UserFeignClientFallback implements UserFeignClient {

	@Override
	public Result<Map<String, Object>> getUserByUniqueId(String uniqueId) {
        return Result.error();
	}
}
