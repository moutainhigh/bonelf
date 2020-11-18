package com.bonelf.auth.client.provider;

import com.bonelf.auth.domain.Resource;
import com.bonelf.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient(name = "resource", fallback = ResourceProviderFallback.class)
public interface ResourceProvider {

    @GetMapping(value = "/resource/all")
    Result<Set<Resource>> resources();

    @GetMapping(value = "/resource/user/{username}")
    Result<Set<Resource>> resources(@PathVariable("username") String username);
}
