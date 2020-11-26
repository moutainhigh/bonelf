package com.bonelf.testservice.client;

import com.bonelf.common.domain.Result;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * 签权服务
 * </p>
 * @author bonelf
 * @since 2020/11/17 15:37
 */
@Component
public class UserClientFallback implements UserClient {

	@Override
	public Result<Map<String, Object>> getUserByUniqueId(String uniqueId) {
        return Result.error();
	}
}
