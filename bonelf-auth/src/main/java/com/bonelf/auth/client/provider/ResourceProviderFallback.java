package com.bonelf.auth.client.provider;

import com.bonelf.auth.domain.Resource;
import com.bonelf.common.domain.Result;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ResourceProviderFallback implements ResourceProvider {
	@Override
	public Result<Set<Resource>> resources() {
		return Result.ok(new HashSet<Resource>());
	}

	@Override
	public Result<Set<Resource>> resources(String username) {
		return Result.ok(new HashSet<Resource>());
	}
}
