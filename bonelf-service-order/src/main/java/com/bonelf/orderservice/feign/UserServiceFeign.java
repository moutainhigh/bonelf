package com.bonelf.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface UserServiceFeign {
}
