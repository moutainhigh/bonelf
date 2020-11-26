/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.common.client.fallback;

import com.bonelf.common.client.ResourceProvider;
import com.bonelf.common.domain.Resource;
import com.bonelf.common.domain.Result;

import java.util.HashSet;
import java.util.Set;

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
