package com.bonelf.gateway.client.fallback;


import com.bonelf.gateway.client.UserFeignClient;
import com.bonelf.gateway.domain.GateWayResult;
import com.bonelf.gateway.domain.vo.ApiUser;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * feign请求失败返回
 * </p>
 * @author bonelf
 * @since 2020/10/6 0:21
 */
public class UserServiceFallback implements UserFeignClient {

    @Setter
    private Throwable cause;

    @Override
    public GateWayResult<ApiUser> getApiUserById(Long userId) {
        return GateWayResult.error();
    }

    @Override
    public GateWayResult<Map<String, Set<String>>> getApiUserRolesAndPermission(Long userId) {
        return GateWayResult.error();
    }
}
